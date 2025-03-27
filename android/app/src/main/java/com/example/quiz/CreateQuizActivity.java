package com.example.quiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quiz.adapters.QuestionAdapter;

public class CreateQuizActivity extends AppCompatActivity {

    private QuestionAdapter questionAdapter;
    private EditText quizTitleEditText;

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
        // Validate input and create quiz
        // We'll implement the API call in the next step
    }
}