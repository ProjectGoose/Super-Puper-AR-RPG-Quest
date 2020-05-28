package com.example.superpuper_ar_rpg.AppObjects.quest;

import java.util.ArrayList;

public class RadioUnit extends Unit {
    private int correctAnswer;
    private ArrayList<String> answers = new ArrayList<>();

    public RadioUnit(String question){
        super(2, question);
        answers.add(new String("one"));
        answers.add(new String("two"));
        answers.add(new String("three"));
        answers.add(new String("four"));
    }

    @Override
    public String getQuestion() {
        return null;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }
}
