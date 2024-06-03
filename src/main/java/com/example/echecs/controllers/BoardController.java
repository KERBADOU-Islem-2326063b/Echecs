package com.example.echecs.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class BoardController {
    int clickNumber = 0;
    int firstClickedCol;
    int firstClickedRow;

    ImageView firstClickedImg;
    ImageView secondClickedImg;

    @FXML
    private void onCaseClick(Event event) {
        ImageView clickedImageView = (ImageView) event.getSource();

        Image clickedEchiquier1 = new Image("file:src/main/resources/com/example/echecs/img/echiquier1_clique.png");
        Image clickedEchiquier2 = new Image("file:src/main/resources/com/example/echecs/img/echiquier2_clique.png");
        Image echiquier1 = new Image("file:src/main/resources/com/example/echecs/img/echiquier1.png");
        Image echiquier2 = new Image("file:src/main/resources/com/example/echecs/img/echiquier2.png");

        if (clickNumber == 2) {
            resetImages(echiquier1, echiquier2);
            return;
        }

        int columnIndex = GridPane.getColumnIndex(clickedImageView);
        int rowIndex = GridPane.getRowIndex(clickedImageView);

        if (clickNumber < 2 && !(firstClickedCol == columnIndex && firstClickedRow == rowIndex)) {
            updateImage(clickedImageView, columnIndex, rowIndex, clickedEchiquier1, clickedEchiquier2);

            if (clickNumber == 1) {
                firstClickedRow = rowIndex;
                firstClickedCol = columnIndex;
                firstClickedImg = clickedImageView;
            } else if (clickNumber == 2) {
                secondClickedImg = clickedImageView;
            }
        }
    }

    private void resetImages(Image echiquier1, Image echiquier2) {
        if ((firstClickedCol + firstClickedRow) % 2 == 0) {
            firstClickedImg.setImage(echiquier2);
        }
        else firstClickedImg.setImage(echiquier1);

        if ((GridPane.getColumnIndex(secondClickedImg) + GridPane.getRowIndex(secondClickedImg)) % 2 == 0) {
            secondClickedImg.setImage(echiquier2);
        }
        else secondClickedImg.setImage(echiquier1);

        clickNumber = 0;

        // Pour pouvoir réappuyer sur la même case après un reset
        firstClickedCol = -1;
        firstClickedRow = -1;
    }

    private void updateImage(ImageView imageView, int columnIndex, int rowIndex, Image clickedEchiquier1, Image clickedEchiquier2) {
        if ((columnIndex + rowIndex) % 2 == 0) {
            imageView.setImage(clickedEchiquier2);
        } else {
            imageView.setImage(clickedEchiquier1);
        }
        clickNumber++;
    }
}