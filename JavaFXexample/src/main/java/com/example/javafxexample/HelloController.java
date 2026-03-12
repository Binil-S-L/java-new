package com.example.javafxexample;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {

    @FXML
    private Label counterLabel;

    private int count = 0;

    @FXML
    public void initialize() {
        counterLabel.setText("0");
    }

    @FXML
    protected void onIncrement() {
        counterLabel.setText(String.valueOf(++count));
    }

    @FXML
    protected void onDecrement() {
        counterLabel.setText(String.valueOf(--count));
    }

    @FXML
    protected void onReset() {
        count = 0;
        counterLabel.setText("0");
    }
}
