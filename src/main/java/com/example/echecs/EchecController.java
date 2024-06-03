package com.example.echecs;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class EchecController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}