package com.example.schoolregistrar;

import com.google.cloud.Timestamp;

import java.util.Date;

public class Assignment {
    private String name;
    private String category;
    private String description;
    private Date dueDate;
    private Time dueTime;

    public Assignment(String name, String category, String description, Date dueDate, Time dueTime) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Time getDueTime() {
        return dueTime;
    }

    public void setDueTime(Time dueTime) {
        this.dueTime = dueTime;
    }
}
