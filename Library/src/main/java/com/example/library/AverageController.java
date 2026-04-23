package com.example.library;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.List;

public class AverageController {

    @FXML private Label resultLabel;

    private List<Book> books;

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    @FXML
    private void onCalculate() {
        if (books == null || books.isEmpty()) {
            resultLabel.setText("No books loaded");
            return;
        }

        double total = 0;
        for (Book b : books) {
            total += b.getPrice();
        }

        double average = total / books.size();
        resultLabel.setText(String.format("Average Cost: %.2f", average));
    }
}