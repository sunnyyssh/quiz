package com.example.quiz.models;

import java.util.List;

public class Question {
    private String ask;
    private List<String> options;
    private String answer; // Used only when creating quizzes

    // Constructor
    public Question(String ask, List<String> options, String answer) {
        this.ask = ask;
        this.options = options;
        this.answer = answer;
    }

    // Getters and Setters
    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}