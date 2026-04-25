package com.example.library;

public class Student {
    private String studentId;
    private String firstName;

    public Student(String studentId, String firstName) {
        this.studentId = studentId;
        this.firstName = firstName;
    }

    public String getStudentId() { return studentId; }
    public String getFirstName() { return firstName; }

    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
}