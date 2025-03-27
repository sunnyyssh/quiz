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

import com.example.quiz.controller.QuizController;
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

            showLoadingDialog("Loading quiz...");

            QuizController.getInstance().fetchQuiz(Long.parseLong(quizId), new QuizController.QuizCallback() {
                @Override
                public void onQuizLoaded(QuizResponse quiz) {
                    dismissLoadingDialog();
                    Intent intent = new Intent(MainActivity.this, QuestionActivity.class);
                    intent.putExtra("quiz", quiz);

                    QuizController.getInstance().startNewQuiz(quiz, Long.parseLong(quizId));
                    startActivity(intent);
                }

                @Override
                public void onError(int code, String message) {
                    dismissLoadingDialog();
                    if (code == 404) {
                        showErrorDialog("Quiz not found with ID: " + quizId);
                    } else {
                        showErrorDialog("Error: " + message);
                    }
                }
            });
        });

    }

    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

    private android.app.ProgressDialog progressDialog;

    private void showLoadingDialog(String message) {
        progressDialog = new android.app.ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissLoadingDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}