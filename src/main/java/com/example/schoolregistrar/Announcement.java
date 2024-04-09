package com.example.schoolregistrar;

public class Announcement {
    private String title;
    private String text;

    public Announcement(String title, String text) {
        this.title = title;
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return String.format("%s \n %s \n", this.title, this.text);
    }
}
