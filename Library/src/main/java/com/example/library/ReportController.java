package com.example.library;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class ReportController {

    @FXML private DatePicker datePicker;
    @FXML private TableView<String[]> tableView;
    @FXML private TableColumn<String[], String> colDate;
    @FXML private TableColumn<String[], String> colBook;
    @FXML private TableColumn<String[], String> colStudent;
    @FXML private TableColumn<String[], String> colType;

    private List<String[]> transactions;

    public void setTransactions(List<String[]> transactions) {
        this.transactions = transactions;
    }

    @FXML
    private void onSearch() {
        LocalDate selectedDate = datePicker.getValue();

        if (selectedDate == null) {
            showAlert("Please select a date.");
            return;
        }

        ObservableList<String[]> result = FXCollections.observableArrayList();

        for (String[] row : transactions) {
            if (row.length >= 4) {
                LocalDate csvDate = parseCsvDate(row[0].trim());
                String csvType = row[3].trim();

                if (csvDate != null && csvDate.equals(selectedDate) && csvType.equals("1")) {
                    result.add(row);
                }
            }
        }

        colDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[0].trim()));
        colBook.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[1].trim()));
        colStudent.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()[2].trim()));
        colType.setCellValueFactory(data -> {
            String type = data.getValue()[3].trim();
            return new SimpleStringProperty(type.equals("1") ? "Issued" : "Returned");
        });

        tableView.setItems(result);

        if (result.isEmpty()) {
            showAlert("No issued books found for the selected date.");
        }
    }

    private LocalDate parseCsvDate(String dateText) {
        DateTimeFormatter[] formats = new DateTimeFormatter[] {
                DateTimeFormatter.ofPattern("M/d/yyyy"),
                DateTimeFormatter.ofPattern("MM/dd/yyyy")
        };

        for (DateTimeFormatter format : formats) {
            try {
                return LocalDate.parse(dateText, format);
            } catch (DateTimeParseException ignored) {
            }
        }
        return null;
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}