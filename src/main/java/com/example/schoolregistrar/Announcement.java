package com.example.schoolregistrar;

public class Announcement {
    private String title;

    private String text;

    private String time;

    private String date;

    public Announcement(String title, String text, String time, String date) {
        this.title = title;
        this.text = text;
        this.time = time;
        this.date = date;
    }

    public Announcement(String title, String text) {
        this.title = title;
        this.text = text;
        this.time = "";
        this.date =  "";
    }

    public String getTitle() {
        return title;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
