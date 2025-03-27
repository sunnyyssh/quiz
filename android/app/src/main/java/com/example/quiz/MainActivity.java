package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.quiz.models.QuizResponse;
import com.example.quiz.network.QuizApiService;
import com.example.quiz.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText quizIdEditText = findViewById(R.id.quizIdEditText);

        // Initialize buttons
        Button startQuizButton = findViewById(R.id.startQuizButton);
        Button createQuizButton = findViewById(R.id.createQuizButton);

        // Create Quiz Button Click
        createQuizButton.setOnClickListener(v -> {
            // Navigate to CreateQuizActivity
            Intent intent = new Intent(MainActivity.this, CreateQuizActivity.class);
            startActivity(intent);
        });

        // Start Quiz Button Click (placeholder)
        startQuizButton.setOnClickListener(v -> {
            String quizId = quizIdEditText.getText().toString();
            if (!quizId.isEmpty()) {
                // Call API to get quiz
                QuizApiService apiService = RetrofitClient.getApiService();

                apiService.getQuiz(Long.parseLong(quizId)).enqueue(new Callback<QuizResponse>() {
                    @Override
                    public void onResponse(Call<QuizResponse> call, Response<QuizResponse> response) {
                        if (response.isSuccessful()) {
                            Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                            intent.putExtra("quiz", response.body());
                            startActivity(intent);
                        } else if (response.code() == 404) {
                            showErrorDialog("Quiz not found with ID: " + quizId);
                        } else {
                            showErrorDialog("Error loading quiz: " + response.message());
                        }
                    }

                    @Override
                    public void onFailure(Call<QuizResponse> call, Throwable t) {
                        showErrorDialog("Error loading quiz (network error)");
                    }
                });
            }
        });

    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }
}