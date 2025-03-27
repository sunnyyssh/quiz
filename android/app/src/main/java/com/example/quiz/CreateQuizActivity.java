package com.example.quiz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quiz.adapters.QuestionAdapter;
import com.example.quiz.controller.QuizController;
import com.example.quiz.models.Quiz;
import com.example.quiz.models.Question;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateQuizActivity extends AppCompatActivity {

    private QuestionAdapter questionAdapter;
    private EditText quizTitleEditText;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quiz);

        // Initialize views
        quizTitleEditText = findViewById(R.id.quizTitleEditText);
        RecyclerView questionsRecyclerView = findViewById(R.id.questionsRecyclerView);
        Button addQuestionButton = findViewById(R.id.addQuestionButton);
        Button cancelButton = findViewById(R.id.cancelButton);
        Button createQuizButton = findViewById(R.id.createQuizButton);

        // Setup RecyclerView
        questionAdapter = new QuestionAdapter();
        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionsRecyclerView.setAdapter(questionAdapter);

        // Add initial question
        questionAdapter.addQuestion();

        // Button listeners
        addQuestionButton.setOnClickListener(v -> questionAdapter.addQuestion());
        cancelButton.setOnClickListener(v -> finish());
        createQuizButton.setOnClickListener(v -> createQuiz());
    }

    private void createQuiz() {
        String title = quizTitleEditText.getText().toString().trim();

        // Validate quiz title
        if (title.isEmpty()) {
            showError("Please enter a quiz title");
            return;
        }

        // Get all questions from adapter
        List<QuestionAdapter.QuestionItem> questionItems = questionAdapter.getQuestions();
        List<Question> questions = new ArrayList<>();

        // Validate each question
        for (int i = 0; i < questionItems.size(); i++) {
            QuestionAdapter.QuestionItem item = questionItems.get(i);

            if (item.questionText == null || item.questionText.isEmpty()) {
                showError("Question " + (i+1) + ": Please enter question text");
                return;
            }

            for (int j = 0; j < item.options.length; j++) {
                if (item.options[j] == null || item.options[j].isEmpty()) {
                    showError("Question " + (i+1) + ": Please enter all options");
                    return;
                }
            }

            questions.add(new Question(
                    item.questionText,
                    new ArrayList<>(Arrays.asList(item.options)),
                    item.options[item.correctAnswerIndex]
            ));
        }

        // Create quiz object
        Quiz quiz = new Quiz(title, questions);

        // Show loading dialog
        showLoading("Creating quiz...");

        // Call API through QuizController
        QuizController.getInstance().createQuiz(quiz, new QuizController.CreateQuizCallback() {
            @Override
            public void onQuizCreated(long quizId) {
                dismissLoading();
                showQuizCreatedDialog(quizId);
            }

            @Override
            public void onError(int code, String message) {
                dismissLoading();
                showError("Failed to create quiz: " + message);
            }
        });
    }

    private void showQuizCreatedDialog(long quizId) {
        new AlertDialog.Builder(this)
                .setTitle("Quiz Created Successfully")
                .setMessage("Quiz ID: " + quizId + "\n\nShare this ID with others to take your quiz!")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Return to main activity
                    Intent intent = new Intent(this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                })
                .setCancelable(false)
                .show();
    }

    private void showLoading(String message) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void dismissLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}