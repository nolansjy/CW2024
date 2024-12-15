module com.example.demo {	
	requires com.fasterxml.jackson.core;
	requires com.fasterxml.jackson.databind;
	requires transitive java.desktop;
	requires javafx.base;
	requires javafx.controls;
	requires transitive javafx.graphics;
	
	opens com.example.demo to javafx.fxml;
	exports com.example.demo;
	exports com.example.demo.controller;
}