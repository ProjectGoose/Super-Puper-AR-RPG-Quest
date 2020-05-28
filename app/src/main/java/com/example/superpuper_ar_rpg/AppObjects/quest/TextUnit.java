package com.example.superpuper_ar_rpg.AppObjects.quest;

public class TextUnit extends Unit{
    private String correctAnswer;

    public TextUnit(String question){
        super(1, question);
    }


    @Override
    public String getQuestion() {
        return question;
    }

    public TextUnit(String question, String correctAnswer) {
        super(1, question);
        this.correctAnswer = correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }
}
