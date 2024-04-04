package com.example.schoolregistrar;

public class Course {
    private String department;
    private int classNumber;
    private String className;

    public Course(String department, int classNumber, String className) {
        this.department = department;
        this.classNumber = classNumber;
        this.className = className;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public int getClassNumber() {
        return classNumber;
    }

    public void setClassNumber(int classNumber) {
        this.classNumber = classNumber;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
