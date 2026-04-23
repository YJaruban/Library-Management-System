package com.example.library;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class HelloController {

    @FXML private TextField booksField;
    @FXML private TextField studentsField;
    @FXML private TextField transactionsField;

    private File booksFile;
    private File studentsFile;
    private File transactionsFile;

    @FXML
    protected void onImportBooks() {
        booksFile = chooseFile();
        if (booksFile != null) {
            booksField.setText(booksFile.getAbsolutePath());
        }
    }

    @FXML
    protected void onImportStudents() {
        studentsFile = chooseFile();
        if (studentsFile != null) {
            studentsField.setText(studentsFile.getAbsolutePath());
        }
    }

    @FXML
    protected void onImportTransactions() {
        transactionsFile = chooseFile();
        if (transactionsFile != null) {
            transactionsField.setText(transactionsFile.getAbsolutePath());
        }
    }

    private File chooseFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select CSV File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        return fileChooser.showOpenDialog(new Stage());
    }

    @FXML
    protected void onProceed() {
        if (booksFile == null || studentsFile == null || transactionsFile == null) {
            showAlert("Please select all three CSV files.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("validation-view.fxml"));
            Parent root = loader.load();

            ValidationController controller = loader.getController();
            controller.loadData(
                    readCSV(booksFile.getAbsolutePath()),
                    readCSV(studentsFile.getAbsolutePath()),
                    readCSV(transactionsFile.getAbsolutePath())
            );

            Stage stage = new Stage();
            stage.setTitle("Validation");
            stage.setScene(new Scene(root, 950, 500));
            stage.show();

        } catch (Exception e) {
            showAlert("Error opening validation screen");
            e.printStackTrace();
        }
    }

    private List<String[]> readCSV(String filePath) {
        List<String[]> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                data.add(line.split(","));
            }
        } catch (Exception e) {
            showAlert("Error reading file: " + filePath);
        }

        return data;
    }

    private void showAlert(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}