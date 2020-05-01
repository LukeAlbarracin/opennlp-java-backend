package com.quizmaster.opennlpjavabackend;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;
/**
 * Inspiration from 2 OpenNLP (Natural Language Processing) tutorials : 
 * https://www.baeldung.com/apache-open-nlp
 * https://www.programcreek.com/2012/05/opennlp-tutorial/
 */
public final class SentenceAnalyzer {
    private String input;
    public SentenceAnalyzer(final String s) {
        input = s;
    }
    public static void printArray(final Object[] array) {
        for (int i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
    }
    public String[] detectSentence() throws IOException {
        InputStream stream = new FileInputStream("en-token.bin");
        TokenizerModel model = new TokenizerModel(stream);
        TokenizerME tokenizer = new TokenizerME(model);
        String[] tokens = tokenizer.tokenize(input);
        return tokens;   
    }
    public Span[] detectPerson() throws IOException {
        InputStream stream = new FileInputStream("en-ner-person.bin");
        TokenNameFinderModel tFinder = new TokenNameFinderModel(stream);
        NameFinderME nFinder = new NameFinderME(tFinder);
        Span[] spans = nFinder.find(this.detectSentence());
        return spans;
    }
    public String[] partOfSpeechTags() throws IOException {
        InputStream stream = new FileInputStream("en-pos-perceptron.bin");
        POSModel model = new POSModel(stream);
        POSTaggerME tagger = new POSTaggerME(model);
        String[] tags = tagger.tag(detectSentence());
        return fixUnmarkedTags(tags);
    }
    public Span[] chunkSentences() throws IOException {
        InputStream stream = new FileInputStream("en-chunker.bin");
        ChunkerModel model = new ChunkerModel(stream);
        ChunkerME chunker = new ChunkerME(model);
        Span[] spans = chunker.chunkAsSpans(detectSentence(), partOfSpeechTags());
        return spans;
    }
    public List<String> getTrueMeanings() throws IOException {
        InputStream stream = new FileInputStream("en-lemmatizer.dict");
        DictionaryLemmatizer lem = new DictionaryLemmatizer(stream);
        List<String> lemmas = new LinkedList<String> (Arrays.asList(lem.lemmatize(detectSentence(), partOfSpeechTags())));
        lemmas.remove(new String("O"));
        return lemmas;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o == null || !(o instanceof SentenceAnalyzer)) {
            return false;
        }
        SentenceAnalyzer analyzer = (SentenceAnalyzer) o;
        try {
            return analyzer.getTrueMeanings().equals(this.getTrueMeanings());
        } catch (IOException exception) {
            System.out.println("IOException found: " + exception);
            return false;
        }
        // Idea : create a model that analyzes the similarity between answers
    }
    
    // The bold abductive assumption that all unknown words are proper nouns
    public String[] fixUnmarkedTags(String[] tags) {
        String[] newTags = tags;
        for (int i = 0; i < tags.length; i++) {
            // `` is what is returned when the part of speech is unrecognized
            if (tags[i].equals("``")) {
                newTags[i] = "NNP";
            }
        }
        return newTags;
    }

    public String setUpQuestion(final String[] sentence) {
        try {
            Span[] spans = this.chunkSentences();
            int endIndex = findEndOfPhrase(spans, 0);
            return concatStrings(sentence, 0, endIndex)
            + findRemainingPhrase(endIndex)
            + "?";
        } catch (IOException exception) {
            System.out.println(exception);
            return "ERROR IN GENERATING QUESTION";
        }
    }
    
    private String findRemainingPhrase(int index) {
        try {
            String[] sentence = detectSentence();
            String[] tags = partOfSpeechTags();
            String remainingPhrase = "";
            int acc = index;
            while (tags[acc].equals("TO") || tags[acc].equals("DT") || tags[acc].equals("PRP")) {
                remainingPhrase = remainingPhrase + " " + sentence[acc];
                if (acc >= sentence.length) {
                    return "FAILURE FAILURE FAILURE";
                }
                acc++;
            }
            return remainingPhrase;
        } catch (IOException exception) {
            return "";
        }
    }
    
    private String concatStrings(String[] words, int start, int end) {
        String newWord = "";
        for (int i = start; i < end; i++) {
            newWord = newWord + " " + words[i];
        }
        return newWord;
    }

    private int findEndOfPhrase(final Span[] spans, int acc) {
        int phraseEndIndex = 0;
        while (!spans[acc].getType().equals("VP")) {
            phraseEndIndex = spans[acc].getEnd();
            acc++;
            if (acc >= spans.length) {
                return -1;
            }
        }
        while (!spans[acc].getType().equals("NP")) {
            phraseEndIndex = spans[acc].getEnd();
            acc++;
            if (acc >= spans.length) {
                return -1;
            }
        }
        return phraseEndIndex;
    }
}