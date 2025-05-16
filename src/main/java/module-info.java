module com.example.fpoeminiproject3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    opens com.example.fpoeminiproject3 to javafx.fxml;
    opens com.example.fpoeminiproject3.controller to javafx.fxml;
    opens com.example.fpoeminiproject3.model to javafx.base;
    opens com.example.fpoeminiproject3.view to javafx.fxml;

    exports com.example.fpoeminiproject3;
    exports com.example.fpoeminiproject3.controller;
    exports com.example.fpoeminiproject3.model;
    exports com.example.fpoeminiproject3.view;
}