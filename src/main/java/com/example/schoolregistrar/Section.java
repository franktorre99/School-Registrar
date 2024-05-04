package com.example.schoolregistrar;

import java.util.ArrayList;

public class Section {
    private Course course;
    private int crn;
    private Professor professor;
    private String semester;
    private String days;
    private ArrayList<Student> roster;

    public Section(Course course, int crn, Professor professor, String semester, String days) {
        this.course = new Course(course);
        this.crn = crn;
        this.professor = professor;
        this.semester = semester;
        this.days = days;
    }

    public int getCrn() {
        return crn;
    }

    public void setCrn(int crn) {
        this.crn = crn;
    }

    public Professor getProfessor() {
        return professor;
    }

    public void setProfessor(Professor professor) {
        this.professor = new Professor(professor);
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = new Course(course);
    }

    public String getSemester() {
        return this.semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getDays() {
        return this.days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public ArrayList<Student> getRoster() {
        return roster;
    }

    @Override
    public String toString() {
        return String.format(crn + " " + course.toString() + " " + professor.toString());
    }
}
