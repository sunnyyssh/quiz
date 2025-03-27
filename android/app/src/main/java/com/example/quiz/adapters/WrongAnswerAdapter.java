package com.example.quiz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quiz.R;
import com.example.quiz.models.QuestionResponse;
import java.util.List;

public class WrongAnswerAdapter extends RecyclerView.Adapter<WrongAnswerAdapter.WrongAnswerViewHolder> {

    private List<QuestionResponse> wrongQuestions;
    private List<String> userAnswers;
    private List<String> correctAnswers;

    public WrongAnswerAdapter(List<QuestionResponse> wrongQuestions,
                              List<String> userAnswers,
                              List<String> correctAnswers) {
        this.wrongQuestions = wrongQuestions;
        this.userAnswers = userAnswers;
        this.correctAnswers = correctAnswers;
    }

    @NonNull
    @Override
    public WrongAnswerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.wrong_answer_item, parent, false);
        return new WrongAnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WrongAnswerViewHolder holder, int position) {
        QuestionResponse question = wrongQuestions.get(position);
        holder.questionText.setText(question.getAsk());
        holder.yourAnswerText.setText(userAnswers.get(position));
        holder.correctAnswerText.setText(correctAnswers.get(position));
    }

    @Override
    public int getItemCount() {
        return wrongQuestions.size();
    }

    static class WrongAnswerViewHolder extends RecyclerView.ViewHolder {
        TextView questionText, yourAnswerText, correctAnswerText;

        public WrongAnswerViewHolder(@NonNull View itemView) {
            super(itemView);
            questionText = itemView.findViewById(R.id.questionText);
            yourAnswerText = itemView.findViewById(R.id.yourAnswerText);
            correctAnswerText = itemView.findViewById(R.id.correctAnswerText);
        }
    }
}