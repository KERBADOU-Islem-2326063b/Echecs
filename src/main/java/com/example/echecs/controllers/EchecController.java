package com.example.echecs.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class EchecController {

    @FXML
    private void onClick(Event event) {
        // On récupère l'objet de l'image cliquée
        ImageView clickedImageView = (ImageView) event.getSource();

        // On récupère l'indexe de la ligne et colonne cliqué de la GridPane
        int columnIndex = GridPane.getColumnIndex((Node) event.getSource());
        int rowIndex = GridPane.getRowIndex((Node) event.getSource());

        // On change l'image
        Image newImage = new Image(getClass().getResource("echiquier2_clique.png").toString());
        clickedImageView.setImage(newImage);

        System.out.println("Ligne : " + rowIndex + "\nColonne : " + columnIndex);
    }
}