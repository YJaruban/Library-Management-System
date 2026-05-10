package com.example.library;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.util.ArrayList;
import java.util.List;

public class ValidationController {

    @FXML private TableView<Book> tableView;
    @FXML private TableColumn<Book, String> colId;
    @FXML private TableColumn<Book, String> colTitle;
    @FXML private TableColumn<Book, String> colISBN;
    @FXML private TableColumn<Book, Integer> colCopies;
    @FXML private TableColumn<Book, Integer> colAvailable;
    @FXML private TableColumn<Book, Double> colPrice;
    @FXML private TableColumn<Book, String> colStatus;

    private List<String[]> students;
    private List<String[]> transactions;
    private List<Student> studentObjects = new ArrayList<>();
    private List<Transaction> transactionObjects = new ArrayList<>();

    public void loadData(List<String[]> rawBooks, List<String[]> rawStudents, List<String[]> rawTransactions) {
        this.students = rawStudents;
        this.transactions = rawTransactions;

        studentObjects.clear();
        transactionObjects.clear();

        for (String[] s : rawStudents) {
            if (s.length >= 2) {
                studentObjects.add(new Student(s[0], s[1]));
            }
        }

        for (String[] t : rawTransactions) {
            if (t.length >= 4) {
                transactionObjects.add(new Transaction(t[0], t[1], t[2], t[3]));
            }
        }

        ObservableList<Book> list = FXCollections.observableArrayList();

        for (String[] b : rawBooks) {
            try {
                Book book = new Book(
                        b[0],
                        b[1],
                        b[2],
                        Integer.parseInt(b[3]),
                        Integer.parseInt(b[4]),
                        Double.parseDouble(b[5])
                );

                validateBook(book);
                list.add(book);

            } catch (Exception e) {
                list.add(new Book("ERROR", "Invalid Row", "-", 0, 0, 0.0, "Invalid"));
            }
        }

        colId.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getId()));
        colTitle.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTitle()));
        colISBN.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIsbn()));
        colCopies.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getCopies()).asObject());
        colAvailable.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getAvailable()).asObject());
        colPrice.setCellValueFactory(data -> new SimpleDoubleProperty(data.getValue().getPrice()).asObject());
        colStatus.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatus()));

        tableView.setEditable(true);

        colId.setCellFactory(TextFieldTableCell.forTableColumn());
        colTitle.setCellFactory(TextFieldTableCell.forTableColumn());
        colISBN.setCellFactory(TextFieldTableCell.forTableColumn());
        colCopies.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colAvailable.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        colPrice.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));

        colId.setOnEditCommit(event -> {
            Book book = event.getRowValue();
            book.setId(event.getNewValue());
            validateBook(book);
            tableView.refresh();
        });

        colTitle.setOnEditCommit(event -> {
            Book book = event.getRowValue();
            book.setTitle(event.getNewValue());
            validateBook(book);
            tableView.refresh();
        });

        colISBN.setOnEditCommit(event -> {
            Book book = event.getRowValue();
            book.setIsbn(event.getNewValue());
            validateBook(book);
            tableView.refresh();
        });

        colCopies.setOnEditCommit(event -> {
            Book book = event.getRowValue();
            book.setCopies(event.getNewValue());
            validateBook(book);
            tableView.refresh();
        });

        colAvailable.setOnEditCommit(event -> {
            Book book = event.getRowValue();
            book.setAvailable(event.getNewValue());
            validateBook(book);
            tableView.refresh();
        });

        colPrice.setOnEditCommit(event -> {
            Book book = event.getRowValue();
            book.setPrice(event.getNewValue());
            validateBook(book);
            tableView.refresh();
        });

        tableView.setItems(list);
        highlightInvalidRows();
    }

    private boolean validBookId(String id) {
        return id != null && id.matches("[A-Za-z]{2}\\d{2}");
    }

    private boolean validISBN13(String isbn) {
        if (isbn == null || !isbn.matches("\\d{13}")) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 12; i++) {
            int digit = Character.getNumericValue(isbn.charAt(i));
            if (i % 2 == 0) {
                sum += digit;
            } else {
                sum += digit * 3;
            }
        }

        int checkDigit = (10 - (sum % 10)) % 10;
        return checkDigit == Character.getNumericValue(isbn.charAt(12));
    }

    private void validateBook(Book book) {
        boolean valid =
                validBookId(book.getId()) &&
                        validISBN13(book.getIsbn()) &&
                        book.getCopies() >= 0 &&
                        book.getCopies() <= 2 &&
                        book.getAvailable() >= 0 &&
                        book.getAvailable() <= book.getCopies();

        if (valid) {
            book.setStatus("Valid");
        } else {
            book.setStatus("Invalid");
        }
    }

    private void highlightInvalidRows() {
        tableView.setRowFactory(tv -> new TableRow<>() {
            @Override
            protected void updateItem(Book book, boolean empty) {
                super.updateItem(book, empty);

                if (empty || book == null) {
                    setStyle("");
                } else if ("Invalid".equals(book.getStatus())) {
                    setStyle("-fx-background-color: #ffcccccc;");
                } else {
                    setStyle("");
                }
            }
        });
    }

    @FXML
    private void openReport() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("report-view.fxml"));
        Parent root = loader.load();

        ReportController controller = loader.getController();
        controller.setTransactions(transactions);

        Stage stage = new Stage();
        stage.setTitle("Books Issued Report");
        stage.setScene(new Scene(root, 650, 420));
        stage.show();
    }

    @FXML
    private void openAverage() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("average-view.fxml"));
        Parent root = loader.load();

        AverageController controller = loader.getController();
        controller.setBooks(new ArrayList<>(tableView.getItems()));

        Stage stage = new Stage();
        stage.setTitle("Average Book Cost");
        stage.setScene(new Scene(root, 400, 250));
        stage.show();
    }

    @FXML
    private void openSearch() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("search-view.fxml"));
        Parent root = loader.load();

        SearchController controller = loader.getController();
        controller.setBooks(new ArrayList<>(tableView.getItems()));

        Stage stage = new Stage();
        stage.setTitle("Book Availability");
        stage.setScene(new Scene(root, 600, 400));
        stage.show();
    }
    @FXML
    private void openStudentsView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("student-view.fxml"));
        Parent root = loader.load();

        StudentViewController controller = loader.getController();
        controller.setStudents(studentObjects);

        Stage stage = new Stage();
        stage.setTitle("Student Details");
        stage.setScene(new Scene(root, 550, 400));
        stage.show();
    }

    @FXML
    private void openTransactionsView() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("transaction-view.fxml"));
        Parent root = loader.load();

        TransactionViewController controller = loader.getController();
        controller.setTransactions(transactionObjects);

        Stage stage = new Stage();
        stage.setTitle("Transaction Details");
        stage.setScene(new Scene(root, 750, 400));
        stage.show();
    }
}