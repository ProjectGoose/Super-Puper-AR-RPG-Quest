package com.example.superpuper_ar_rpg.AppObjects.quest;

import java.util.ArrayList;

public class RadioUnit extends Unit {
    private int correctAnswer;
    private ArrayList<String> answers = new ArrayList<>();

    public RadioUnit(String question){
        super(2, question);
    }

    public void setAnswers(ArrayList<String> answers) {
        this.answers = answers;
    }

    public RadioUnit(String question, int correctAnswer, ArrayList<String> answers) {
        super(2, question);
        this.correctAnswer = correctAnswer;
        this.answers = answers;
    }

    @Override
    public String getQuestion() {
        return question;
    }

    public void setCorrectAnswer(int correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public int getCorrectAnswer() {
        return correctAnswer;
    }

    public ArrayList<String> getAnswers() {
        return answers;
    }
}
