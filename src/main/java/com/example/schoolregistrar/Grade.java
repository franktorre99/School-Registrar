package com.example.schoolregistrar;

public class Grade {
    private String crn;
    private String courseName;
    private String grade;

    public Grade(String crn, String courseName, String grade) {
        this.crn = crn;
        this.courseName = courseName;
        this.grade = grade;
    }

    public String getCrn() {
        return crn;
    }

    public void setCrn(String crn) {
        this.crn = crn;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
