package com.example.library;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class TransactionViewController {

    @FXML private TableView<Transaction> transactionTable;
    @FXML private TableColumn<Transaction, String> colDate;
    @FXML private TableColumn<Transaction, String> colBookId;
    @FXML private TableColumn<Transaction, String> colStudentId;
    @FXML private TableColumn<Transaction, String> colType;

    public void setTransactions(List<Transaction> transactions) {
        colDate.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getDate()));

        colBookId.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getBookId()));

        colStudentId.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStudentId()));

        colType.setCellValueFactory(data -> {
            String type = data.getValue().getType();
            return new SimpleStringProperty(type.equals("1") ? "Issued" : "Returned");
        });

        transactionTable.setItems(FXCollections.observableArrayList(transactions));
    }
}