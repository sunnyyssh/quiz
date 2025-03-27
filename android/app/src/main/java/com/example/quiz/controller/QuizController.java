package com.example.quiz.controller;

import com.example.quiz.models.Quiz;
import com.example.quiz.models.QuizResponse;
import com.example.quiz.models.QuestionResponse;
import com.example.quiz.network.QuizApiService;
import com.example.quiz.network.RetrofitClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuizController {
    private static QuizController instance;
    private QuizResponse currentQuiz;
    private List<Integer> selectedAnswers;
    private int currentQuestionIndex = 0;
    private long quizId;

    private QuizController() {}

    public static synchronized QuizController getInstance() {
        if (instance == null) {
            instance = new QuizController();
        }
        return instance;
    }

    // Initialization methods
    public void startNewQuiz(QuizResponse quiz, long quizId) {
        this.currentQuiz = quiz;
        this.selectedAnswers = new ArrayList<>(Collections.nCopies(quiz.getQuestions().size(), -1));
        this.currentQuestionIndex = 0;
        this.quizId = quizId;
    }

    public long getQuizId() {
        return quizId;
    }

    public QuizResponse getCurrentQuiz() {
        return currentQuiz;
    }

    public List<Integer> getSelectedAnswers() {
        return selectedAnswers;
    }

    // Quiz progress methods
    public QuestionResponse getCurrentQuestion() {
        return currentQuiz.getQuestions().get(currentQuestionIndex);
    }

    public int getQuestionCount() {
        return currentQuiz.getQuestions().size();
    }

    public int getCurrentQuestionIndex() {
        return currentQuestionIndex;
    }

    public void saveAnswer(int selectedOptionIndex) {
        selectedAnswers.set(currentQuestionIndex, selectedOptionIndex);
    }

    public void moveToNextQuestion() {
        if (currentQuestionIndex < currentQuiz.getQuestions().size() - 1) {
            currentQuestionIndex++;
        }
    }

    public void moveToPreviousQuestion() {
        if (currentQuestionIndex > 0) {
            currentQuestionIndex--;
        }
    }

    // Score calculation
    public int calculateScore(List<String> correctAnswers) {
        int score = 0;
        for (int i = 0; i < selectedAnswers.size(); i++) {
            if (selectedAnswers.get(i) != -1) {
                QuestionResponse question = currentQuiz.getQuestions().get(i);
                String selectedAnswer = question.getOptions().get(selectedAnswers.get(i));
                if (selectedAnswer.equals(correctAnswers.get(i))) {
                    score++;
                }
            }
        }
        return score;
    }

    // API Integration methods
    public void fetchQuiz(long quizId, QuizCallback callback) {
        RetrofitClient.getApiService().getQuiz(quizId).enqueue(new Callback<QuizResponse>() {
            @Override
            public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
                if (response.isSuccessful()) {
                    callback.onQuizLoaded(response.body());
                } else {
                    callback.onError(response.code(), "Quiz not found");
                }
            }

            @Override
            public void onFailure(Call<QuizResponse> call, Throwable t) {
                callback.onError(-1, t.getMessage());
            }
        });
    }

    public void fetchAnswers(long quizId, AnswersCallback callback) {
        RetrofitClient.getApiService().getAnswers(quizId).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful()) {
                    callback.onAnswersLoaded(response.body());
                } else {
                    callback.onError(response.code(), "Failed to load answers");
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                callback.onError(-1, t.getMessage());
            }
        });
    }

    public void createQuiz(Quiz quiz, CreateQuizCallback callback) {
        RetrofitClient.getApiService().createQuiz(quiz).enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if (response.isSuccessful()) {
                    callback.onQuizCreated(response.body());
                } else {
                    callback.onError(response.code(), "Failed to create quiz");
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                callback.onError(-1, t.getMessage());
            }
        });
    }

    // Callback interfaces
    public interface QuizCallback {
        void onQuizLoaded(QuizResponse quiz);
        void onError(int code, String message);
    }

    public interface AnswersCallback {
        void onAnswersLoaded(List<String> answers);
        void onError(int code, String message);
    }

    public interface CreateQuizCallback {
        void onQuizCreated(long quizId);
        void onError(int code, String message);
    }
}