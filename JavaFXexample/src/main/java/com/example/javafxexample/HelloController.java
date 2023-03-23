package com.example.javafxexample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private TextField textField;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText(String.format("Hello %s!", textField.getText()));
    }
}