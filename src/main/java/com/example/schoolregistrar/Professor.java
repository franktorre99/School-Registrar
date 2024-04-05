package com.example.schoolregistrar;

import java.util.ArrayList;

public class Professor implements User {
    private String firstName;
    private String lastName;
    private int id;
    private ArrayList<Section> sectionsTaught;

    public Professor(String firstName, String lastName, int id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.id = id;
        this.sectionsTaught = new ArrayList<Section>();
    }

    public Professor() {
        this.firstName = "";
        this.lastName = "";
        this.id = 0;
        this.sectionsTaught = new ArrayList<>();
    }

    public Professor(Professor other) {
        this.firstName = other.firstName;
        this.lastName = other.lastName;
        this.id = other.id;
        this.sectionsTaught = new ArrayList<Section>(other.sectionsTaught);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Section> getSectionsTaught() {
        return sectionsTaught;
    }

    public void setSectionsTaught(ArrayList<Section> sectionsTaught) {
        this.sectionsTaught = new ArrayList<>(sectionsTaught);
    }

    @Override
    public String toString() {
        return String.format(firstName + " " + lastName + " " + id);
    }
}