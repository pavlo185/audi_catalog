module com.example.demo3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.databind;


    opens com.example.demo3 to javafx.fxml;
    exports com.example.demo3;
    exports com.example.demo;
    opens com.example.demo to javafx.fxml;
}