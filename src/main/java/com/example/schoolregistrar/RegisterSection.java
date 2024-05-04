package com.example.schoolregistrar;

public class RegisterSection {
    private String crn;
    private String courseName;
    private String semester;
    private String time;
    private String professor;
    private String days;

    public RegisterSection(String crn, String courseName, String semester, String time, String professor, String days) {
        this.crn = crn;
        this.courseName = courseName;
        this.semester = semester;
        this.time = time;
        this.professor = professor;
        this.days = days;
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

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfessor() {
        return professor;
    }

    public void setProfessor(String professor) {
        this.professor = professor;
    }

    public String getDays() {
        return this.days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
