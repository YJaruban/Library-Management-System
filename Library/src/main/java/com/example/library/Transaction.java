package com.example.library;

public class Transaction {
    private String date;
    private String bookId;
    private String studentId;
    private String type;

    public Transaction(String date, String bookId, String studentId, String type) {
        this.date = date;
        this.bookId = bookId;
        this.studentId = studentId;
        this.type = type;
    }

    public String getDate() { return date; }
    public String getBookId() { return bookId; }
    public String getStudentId() { return studentId; }
    public String getType() { return type; }

    public void setDate(String date) { this.date = date; }
    public void setBookId(String bookId) { this.bookId = bookId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public void setType(String type) { this.type = type; }
}