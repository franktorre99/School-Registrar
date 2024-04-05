package com.example.schoolregistrar;

public class UpcomingAssignment {
    private String name;
    private String dueDate;
    private String dueTime;

    public UpcomingAssignment(String name, String dueDate, String dueTime) {
        this.name = name;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getDueTime() {
        return dueTime;
    }

    public void setDueTime(String dueTime) {
        this.dueTime = dueTime;
    }
}
