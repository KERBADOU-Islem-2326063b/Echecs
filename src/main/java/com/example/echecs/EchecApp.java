package com.example.echecs;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class EchecApp extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(EchecApp.class.getResource("views/Echec.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 665, 527);
        stage.setTitle("Echecs");

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}