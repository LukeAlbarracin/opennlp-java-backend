package com.quizmaster.opennlpjavabackend;
/**
import org.springframework.cloud.gcp.data.datastore.core.mapping.Entity;
import org.springframework.data.annotation.Id;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "quizzes")
public class Quiz {
    @Id
    Long id;

    ArrayList<Question> questions;

    String username;

    public Quiz(List<Question> questions, String username) {
        this.questions = questions;
        this.username = username;
    }

    public long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + this.id +
            ", questions='" + this.questions + '\'' +
            ", username='" + this.username + '\'' +
            '}';
    }
}**/
