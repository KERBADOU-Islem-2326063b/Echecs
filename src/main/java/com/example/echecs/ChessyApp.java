package com.example.echecs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Classe principale de l'application ChessyApp.
 * Cette classe gère le démarrage de l'application et la création de la scène principale.
 */
public class ChessyApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("prism.lcdtext", "false");
        FXMLLoader fxmlLoader = new FXMLLoader(ChessyApp.class.getResource("views/Menu.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        scene.getStylesheets().add(getClass().getResource("stylesheets/style.css").toExternalForm());
        stage.setTitle("ChessyApp");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}