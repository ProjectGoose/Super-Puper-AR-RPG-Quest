package com.example.superpuper_ar_rpg.AppObjects.quest;

public class TextUnit extends Unit{
    private String correctAnswer;

    public TextUnit(String question){
        super(1, question);
    }


    @Override
    public String getQuestion() {
        return null;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
