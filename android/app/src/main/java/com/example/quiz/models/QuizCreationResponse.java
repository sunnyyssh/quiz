package com.example.quiz.models;

import java.io.Serializable;

public class QuizCreationResponse implements Serializable {
    private long id; // Matches your server's int64 response

    public long getId() {
        return id;
    }
}