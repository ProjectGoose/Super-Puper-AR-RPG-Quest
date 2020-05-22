package com.example.superpuper_ar_rpg.AppObjects.quest;

public class MapQuestDetailsDto {
    private String text;
    private String body;
    private String author;

    public MapQuestDetailsDto(String text, String body, String author) {
        this.text = text;
        this.body = body;
        this.author = author;
    }

    public String getText() {
        return text;
    }

    public String getBody() {
        return body;
    }

    public String getAuthor() {
        return author;
    }
}
