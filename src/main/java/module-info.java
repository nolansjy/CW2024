module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
	requires javafx.graphics;
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires java.desktop;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo.controller;
}