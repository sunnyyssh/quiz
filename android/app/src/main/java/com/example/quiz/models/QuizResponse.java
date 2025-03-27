package com.example.quiz.models;

import java.io.Serializable;
import java.util.List;

public class QuizResponse implements Serializable {
    private String title;
    private List<QuestionResponse> questions;

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public List<QuestionResponse> getQuestions() {
        return questions;
    }
}