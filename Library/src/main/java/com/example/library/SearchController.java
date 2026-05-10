package com.example.library;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.FileWriter;
import java.util.List;

public class SearchController {

    @FXML private TextField searchField;
    @FXML private ListView<String> listView;

    private List<Book> books;

    public void setBooks(List<Book> books) {
        this.books = books;
    }


    @FXML
    private void onSearch() {
        String input = searchField.getText().trim().toLowerCase();
        ObservableList<String> results = FXCollections.observableArrayList();

        if (input.isEmpty()) {
            listView.setItems(results);
            return;
        }

        for (Book b : books) {
            String title = b.getTitle().toLowerCase();

            if (title.contains(input)) {
                results.add(
                        "Book ID: " + b.getId() +
                                " | Title: " + b.getTitle() +
                                " | ISBN: " + b.getIsbn() +
                                " | Copies: " + b.getCopies() +
                                " | Available: " + b.getAvailable() +
                                " | Price: " + b.getPrice()
                );
            }
        }
        if (results.isEmpty()) {
            results.add("No matching books found.");
        }

        listView.setItems(results);
    }

    @FXML
    private void onExport() {
        try (FileWriter writer = new FileWriter("search_result.txt")) {
            for (String item : listView.getItems()) {
                writer.write(item + System.lineSeparator());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}