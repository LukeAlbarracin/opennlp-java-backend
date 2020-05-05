package com.quizmaster.opennlpjavabackend;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import java.io.OutputStream;
import org.springframework.core.io.WritableResource;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class QuizController {

    // NLA. Add read.
    @Value("gs://nla-cs125x-opennlp-java-backend/quiz1/question1.txt")
    private Resource _file1;

    @Value("gs://nla-cs125x-opennlp-java-backend/quiz1/question2.txt")
    private Resource _file2;

    @Value("gs://nla-cs125x-opennlp-java-backend/quiz1/question3.txt")
    private Resource _file3;

    private ArrayList<Resource> _questions;

    /*
    public QuizController()  {
        _questions = new ArrayList<Resource>(3);
        _questions.add(_file1);
        _questions.add(_file2);
        _questions.add(_file3);
    }
    */
    
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String readDefaultRoute() throws IOException {
        return StreamUtils.copyToString(
            _file1.getInputStream(),
            Charset.defaultCharset()) + "\n";
    }

    @RequestMapping(value = "/1", method = RequestMethod.GET)
    public String readFile1() throws IOException {
        return StreamUtils.copyToString(
            _file1.getInputStream(),
            Charset.defaultCharset()) + "\n";
    }

    @RequestMapping(value = "/2", method = RequestMethod.GET)
    public String readFile2() throws IOException {
        return StreamUtils.copyToString(
            _file2.getInputStream(),
            Charset.defaultCharset()) + "\n";
    }

    @RequestMapping(value = "/3", method = RequestMethod.GET)
    public String readFile3() throws IOException {
        return StreamUtils.copyToString(
            _file3.getInputStream(),
            Charset.defaultCharset()) + "\n";
    }            

    // NLA. Add write.
    @RequestMapping(value = "/", method = RequestMethod.POST)
    String writeGcs(@RequestBody String data) throws IOException {
        try (OutputStream os = ((WritableResource) _file1).getOutputStream()) {
            os.write(data.getBytes());
        }
        return "file was updated\n";
    }

	//@GetMapping("/process")
    // public String processRoute(@RequestParam(name="name", required = false, defaultValue = "") String name) {
    // NLA. Add lower level request mapping
    // - https://dzone.com/articles/using-the-spring-requestmapping-annotation
    //@RequestMapping(value = "/name", method = RequestMethod.GET)
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String process() throws IOException {
        String name = "Donald Trump is the president.";
		SentenceAnalyzer analyzer = new SentenceAnalyzer(name);
        try {
            return analyzer.setUpQuestion(analyzer.detectSentence());
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
	}    

    @RequestMapping(value = "/process/{questionId}", method = RequestMethod.GET)
    public String process(@PathVariable int questionId) throws IOException {
        Resource quiz = getQuestion(questionId);
        String input = StreamUtils.copyToString(
            quiz.getInputStream(),
            Charset.defaultCharset());
		SentenceAnalyzer analyzer = new SentenceAnalyzer(input);
        try {
            String output = analyzer.setUpQuestion(analyzer.detectSentence());
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
	}

    // NLA. Now process query parameter sent by client
    // - https://stackoverflow.com/questions/32201441/how-do-i-retrieve-query-parameters-in-spring-boot
    @RequestMapping(value="/process", method = RequestMethod.GET)
    public String getInput(@RequestParam("input") String input){
		SentenceAnalyzer analyzer = new SentenceAnalyzer(input);
        try {
            String output = analyzer.setUpQuestion(analyzer.detectSentence());
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }        
    }

    private Resource getQuestion(int id) {
        switch(id) {
            case 1:
                return _file1;
            case 2:
                return _file2;
            case 3:
                return _file3;
            default:
                return _file1;
        }
    }

}