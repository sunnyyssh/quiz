package com.example.quiz.adapters;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quiz.R;
import java.util.ArrayList;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private List<QuestionItem> questions = new ArrayList<>();

    public static class QuestionItem {
        public String questionText = "";
        public String[] options = new String[]{"", "", "", ""};
        public int correctAnswerIndex = 0;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.question_item, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, @SuppressLint("RecyclerView") int position) {
        QuestionItem item = questions.get(position);

        // Set current values
        holder.questionEditText.setText(item.questionText);
        holder.option1EditText.setText(item.options[0]);
        holder.option2EditText.setText(item.options[1]);
        holder.option3EditText.setText(item.options[2]);
        holder.option4EditText.setText(item.options[3]);
        holder.correctAnswerRadioGroup.check(getRadioIdForIndex(item.correctAnswerIndex));

        // Add text watchers
        holder.questionEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                questions.get(position).questionText = s.toString();
            }
        });

        // Add similar text watchers for all option EditTexts
        holder.option1EditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                questions.get(position).options[0] = s.toString();
            }
        });
        holder.option2EditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                questions.get(position).options[1] = s.toString();
            }
        });
        holder.option3EditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                questions.get(position).options[2] = s.toString();
            }
        });
        holder.option4EditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                questions.get(position).options[3] = s.toString();
            }
        });

        // Radio group listener
        holder.correctAnswerRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            questions.get(position).correctAnswerIndex = getIndexForRadioId(checkedId);
        });
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public void addQuestion() {
        questions.add(new QuestionItem());
        notifyItemInserted(questions.size() - 1);
    }

    public List<QuestionItem> getQuestions() {
        return questions;
    }

    private int getRadioIdForIndex(int index) {
        switch (index) {
            case 0: return R.id.option1Radio;
            case 1: return R.id.option2Radio;
            case 2: return R.id.option3Radio;
            case 3: return R.id.option4Radio;
            default: return -1;
        }
    }

    private int getIndexForRadioId(int id) {
        if (id == R.id.option1Radio) return 0;
        if (id == R.id.option2Radio) return 1;
        if (id == R.id.option3Radio) return 2;
        if (id == R.id.option4Radio) return 3;
        return 0;
    }


    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        EditText questionEditText;
        EditText option1EditText, option2EditText, option3EditText, option4EditText;
        RadioGroup correctAnswerRadioGroup;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionEditText = itemView.findViewById(R.id.questionEditText);
            option1EditText = itemView.findViewById(R.id.option1EditText);
            option2EditText = itemView.findViewById(R.id.option2EditText);
            option3EditText = itemView.findViewById(R.id.option3EditText);
            option4EditText = itemView.findViewById(R.id.option4EditText);
            correctAnswerRadioGroup = itemView.findViewById(R.id.correctAnswerRadioGroup);
        }
    }
}