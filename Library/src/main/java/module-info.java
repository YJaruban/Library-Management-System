module com.example.library {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.testng;

    opens com.example.library to javafx.fxml;
    exports com.example.library;
}