package com.example.superpuper_ar_rpg.AppObjects.quest;

class MapQuestDto {
    private long id;
    private String title;
    private String text;
    private String body;
    private String author;
    private double latitude;
    private double longitude;
    private double rating;

    MapQuestDto(MapQuest mapQuest){
        this.id = mapQuest.getId();
        this.title = mapQuest.getTitle();
        this.author = mapQuest.getAuthor();
        this.latitude = mapQuest.getCoordinates().latitude;
        this.longitude = mapQuest.getCoordinates().longitude;
        this.rating = mapQuest.getRating();
        this.body = mapQuest.getBody();
        this.text = mapQuest.getText();
    }

    MapQuestDto(MapQuestBriefDto briefDto, MapQuestDetailsDto detailsDto){
        this.id = briefDto.getId();
        this.title = briefDto.getTitle();
        this.author = detailsDto.getAuthor();
        this.latitude = briefDto.getLatitude();
        this.longitude = briefDto.getLongitude();
        this.rating = briefDto.getRating();
        this.body = detailsDto.getBody();
        this.text = detailsDto.getText();
    }

    public void setText(String body){
        text = body;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getAuthor() {
        return author;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getRating() {
        return rating;
    }

    public String getBody() {
        return body;
    }
}
