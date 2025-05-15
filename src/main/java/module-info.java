module com.example.fpoeminiproject3 {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.fpoeminiproject3 to javafx.fxml;
    opens com.example.fpoeminiproject3.controller to javafx.fxml;
    exports com.example.fpoeminiproject3;
}