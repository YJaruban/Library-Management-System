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

        boolean wildcard = input.endsWith("*");
        String query = wildcard ? input.substring(0, input.length() - 1) : input;

        for (Book b : books) {
            String title = b.getTitle().toLowerCase();

            boolean match;
            if (wildcard) {
                match = title.startsWith(query);
            } else {
                match = title.equals(query);
            }

            if (match) {
                results.add(
                        "Title: " + b.getTitle() +
                                " | Book ID: " + b.getId() +
                                " | Available: " + b.getAvailable()
                );
            }
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