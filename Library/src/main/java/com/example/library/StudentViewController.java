package com.example.library;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;

public class StudentViewController {

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> colStudentId;
    @FXML private TableColumn<Student, String> colFirstName;

    public void setStudents(List<Student> students) {
        colStudentId.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getStudentId()));

        colFirstName.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getFirstName()));

        studentTable.setItems(FXCollections.observableArrayList(students));
    }
}