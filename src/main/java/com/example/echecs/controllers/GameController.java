package com.example.echecs.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class GameController {
    private static int initialTimeInSeconds;

    public static int getInitialTimeInSeconds() {
        return initialTimeInSeconds;
    }
    public static void setInitialTimeInSeconds(int initialTimeInSeconds) {
        GameController.initialTimeInSeconds = initialTimeInSeconds;
    }
}
