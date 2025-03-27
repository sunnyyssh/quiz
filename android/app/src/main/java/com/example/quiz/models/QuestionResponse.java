package com.example.quiz.models;

import java.io.Serializable;
import java.util.List;

public class QuestionResponse implements Serializable {
    private String ask;
    private List<String> options;

    // Getters and Setters
    public String getAsk() {
        return ask;
    }

    public List<String> getOptions() {
        return options;
    }
}