package com.example.quiz.network;

import com.example.quiz.models.Quiz;
import com.example.quiz.models.QuizResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface QuizApiService {
    @POST("quiz")
    Call<Long> createQuiz(@Body Quiz quiz);

    @GET("quiz/{id}")
    Call<QuizResponse> getQuiz(@Path("id") long id);

    @GET("quiz/{id}/answers")
    Call<List<String>> getAnswers(@Path("id") long id);
}