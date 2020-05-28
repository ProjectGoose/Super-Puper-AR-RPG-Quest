package com.example.superpuper_ar_rpg.AppObjects.GroupsOfViews;

import android.widget.EditText;
import android.widget.TextView;

import com.example.superpuper_ar_rpg.AppObjects.GroupsOfViews.GroupOfView;

public class GroupOfViewText extends GroupOfView {
    private String correctAnswer;
    private TextView question;
    private EditText answer;

    public GroupOfViewText(String correctAnswer, TextView question, EditText answer){
        super(1);
        this.correctAnswer = correctAnswer;
        this.question = question;
        this.answer = answer;
    }

    public boolean isCorrect(){
        return answer.getText().toString().equals(correctAnswer);
    }

    public TextView getQuestion() {
        return question;
    }

    public EditText getAnswer() {
        return answer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
