package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quiz.adapters.WrongAnswerAdapter;
import com.example.quiz.controller.QuizController;
import com.example.quiz.models.QuestionResponse;
import java.util.ArrayList;
import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        // Initialize views
        TextView quizTitleText = findViewById(R.id.quizTitleText);
        TextView scoreText = findViewById(R.id.scoreText);
        TextView wrongAnswersTitle = findViewById(R.id.wrongAnswersTitle);
        RecyclerView wrongAnswersRecyclerView = findViewById(R.id.wrongAnswersRecyclerView);
        Button backToMainButton = findViewById(R.id.backToMainButton);

        // Get data from intent
        int score = getIntent().getIntExtra("score", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 0);
        List<String> correctAnswers = getIntent().getStringArrayListExtra("correctAnswers");

        // Set quiz title
        quizTitleText.setText(QuizController.getInstance().getCurrentQuiz().getTitle());

        // Set score
        scoreText.setText(String.format("%d/%d", score, totalQuestions));

        // Get wrong answers
        List<QuestionResponse> wrongQuestions = new ArrayList<>();
        List<String> userWrongAnswers = new ArrayList<>();
        List<String> correctAnswersForWrong = new ArrayList<>();

        for (int i = 0; i < QuizController.getInstance().getSelectedAnswers().size(); i++) {
            int selectedIndex = QuizController.getInstance().getSelectedAnswers().get(i);
            if (selectedIndex != -1) {
                QuestionResponse question = QuizController.getInstance().getCurrentQuiz().getQuestions().get(i);
                String userAnswer = question.getOptions().get(selectedIndex);
                if (!userAnswer.equals(correctAnswers.get(i))) {
                    wrongQuestions.add(question);
                    userWrongAnswers.add(userAnswer);
                    correctAnswersForWrong.add(correctAnswers.get(i));
                }
            }
        }

        // Setup RecyclerView if there are wrong answers
        if (!wrongQuestions.isEmpty()) {
            wrongAnswersTitle.setVisibility(View.VISIBLE);
            wrongAnswersRecyclerView.setVisibility(View.VISIBLE);

            wrongAnswersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            wrongAnswersRecyclerView.setAdapter(new WrongAnswerAdapter(
                    wrongQuestions,
                    userWrongAnswers,
                    correctAnswersForWrong
            ));
        }

        // Back to main button
        backToMainButton.setOnClickListener(v -> {
            Intent intent = new Intent(ResultsActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}