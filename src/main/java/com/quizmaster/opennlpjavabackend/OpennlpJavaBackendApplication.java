package com.quizmaster.opennlpjavabackend;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class OpennlpJavaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(OpennlpJavaBackendApplication.class, args);
	}

	@GetMapping("/")
	public String baseRoute() {
		SentenceAnalyzer analyzer = new SentenceAnalyzer("Killer Whales communicate through echolocation");
        try {
            return analyzer.setUpQuestion(analyzer.detectSentence());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
	}
}
