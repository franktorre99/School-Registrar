package com.example.schoolregistrar;

import java.util.ArrayList;

public class Section {
    private Course course;
    private int crn;
    private Professor professor;
    private ArrayList<Student> roster;

    public Section(Course course, int crn, Professor professor) {
        this.course = course;
        this.crn = crn;
        this.professor = professor;
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
        this.professor = professor;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public ArrayList<Student> getRoster() {
        return roster;
    }
}
