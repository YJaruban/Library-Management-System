package com.example.library;

public class Book {
    private String id;
    private String title;
    private String isbn;
    private int copies;
    private int available;
    private double price;
    private String status;

    public Book(String id, String title, String isbn, int copies, int available, double price) {
        this(id, title, isbn, copies, available, price, "");
    }

    public Book(String id, String title, String isbn, int copies, int available, double price, String status) {
        this.id = id;
        this.title = title;
        this.isbn = isbn;
        this.copies = copies;
        this.available = available;
        this.price = price;
        this.status = status;
    }

    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getIsbn() { return isbn; }
    public int getCopies() { return copies; }
    public int getAvailable() { return available; }
    public double getPrice() { return price; }
    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
    public void setId(String id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setCopies(int copies) { this.copies = copies; }
    public void setAvailable(int available) { this.available = available; }
    public void setPrice(double price) { this.price = price; }
}