package com.example.schoolregistrar;

public class Course {
    private String department;
    private int courseNumber;
    private String courseName;

    public Course(String department, int courseNumber, String courseName) {
        this.department = department;
        this.courseNumber = courseNumber;
        this.courseName = courseName;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getCourseNumber() {
        return courseNumber;
    }

    public void setCourseNumber(int courseNumber) {
        this.courseNumber = courseNumber;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    @Override
    public String toString() {
        return String.format(department + " " + courseNumber + " " + courseName);
    }
}
