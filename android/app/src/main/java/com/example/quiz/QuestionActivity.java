package com.example.quiz;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.quiz.models.QuizResponse;
import com.example.quiz.models.QuestionResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionActivity extends AppCompatActivity {

    private TextView quizTitleText;
    private TextView progressText;
    private TextView questionText;
    private RadioGroup optionsRadioGroup;
    private Button nextButton;

    private QuizResponse quiz;
    private int currentQuestionIndex = 0;
    private List<Integer> selectedAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        // Initialize views
        quizTitleText = findViewById(R.id.quizTitleText);
        progressText = findViewById(R.id.progressText);
        questionText = findViewById(R.id.questionText);
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup);
        nextButton = findViewById(R.id.nextButton);
        Button backButton = findViewById(R.id.backButton);

        // Get quiz data from intent
        quiz = (QuizResponse) getIntent().getSerializableExtra("quiz");
        selectedAnswers = new ArrayList<>(Collections.nCopies(quiz.getQuestions().size(), -1));

        // Set initial data
        quizTitleText.setText(quiz.getTitle());
        showQuestion(currentQuestionIndex);

        // Button listeners
        backButton.setOnClickListener(v -> finish());

        nextButton.setOnClickListener(v -> {
            int selectedId = optionsRadioGroup.getCheckedRadioButtonId();
            if (selectedId != -1) {
                selectedAnswers.set(currentQuestionIndex, optionsRadioGroup.indexOfChild(findViewById(selectedId)));

                if (currentQuestionIndex < quiz.getQuestions().size() - 1) {
                    currentQuestionIndex++;
                    showQuestion(currentQuestionIndex);
                } else {
                    // All questions answered - show results
                    showResults();
                }
            }
        });
    }

    private void showQuestion(int index) {
        QuestionResponse question = quiz.getQuestions().get(index);

        // Update progress
        progressText.setText(String.format("Question %d/%d", index + 1, quiz.getQuestions().size()));

        // Set question text
        questionText.setText(question.getAsk());

        // Clear previous options
        optionsRadioGroup.removeAllViews();

        // Add new options
        for (int i = 0; i < question.getOptions().size(); i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(question.getOptions().get(i));
            radioButton.setId(View.generateViewId());
            radioButton.setPadding(16, 16, 16, 16);
            optionsRadioGroup.addView(radioButton);
        }

        // Restore selected answer if exists
        int selectedAnswer = selectedAnswers.get(index);
        if (selectedAnswer != -1) {
            optionsRadioGroup.check(optionsRadioGroup.getChildAt(selectedAnswer).getId());
        } else {
            optionsRadioGroup.clearCheck();
        }

        // Update button text
        nextButton.setText(index == quiz.getQuestions().size() - 1 ? "Finish" : "Next");
    }

    private void showResults() {
        // Calculate score and show results screen
        // We'll implement this in the next step
    }
}