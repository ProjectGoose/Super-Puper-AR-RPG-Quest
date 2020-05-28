package com.example.superpuper_ar_rpg.AppObjects.GroupsOfViews;

import android.content.Context;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class GroupOfViewRadio extends GroupOfView {
    private int correctAnswer;
    private TextView question;
    private RadioGroup radioGroup;

    public GroupOfViewRadio(int correctAnswer, TextView question, RadioGroup radioGroup){
        super(2);
        this.correctAnswer = correctAnswer;
        this.question = question;
        this.radioGroup = radioGroup;
    }

    public boolean isCorrect(){
        if(radioGroup.getCheckedRadioButtonId() == correctAnswer){
            return true;
        } else {
            return false;
        }
    }

    public TextView getQuestion() {
        return question;
    }

    public RadioGroup getRadioGroup() {
        return radioGroup;
    }
}
