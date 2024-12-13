module com.example.demo {	
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires java.desktop;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.graphics;
	
	opens com.example.demo to javafx.fxml;
	exports com.example.demo;
	exports com.example.demo.controller;
}