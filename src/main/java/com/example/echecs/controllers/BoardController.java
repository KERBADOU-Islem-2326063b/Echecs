package com.example.echecs.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class BoardController {
    int clickNumber = 0;
    int columnIndex;
    int rowIndex;
    int firstClickedCol;
    int firstClickedRow;

    ImageView firstCaseClickedImg;
    ImageView secondCaseClickedImg;

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

        columnIndex = GridPane.getColumnIndex(clickedImageView);
        rowIndex = GridPane.getRowIndex(clickedImageView);

        if (clickNumber < 2 && !(firstClickedCol == columnIndex && firstClickedRow == rowIndex)) {
            updateImage(clickedImageView, columnIndex, rowIndex, clickedEchiquier1, clickedEchiquier2);

            if (clickNumber == 1) {
                firstClickedRow = rowIndex;
                firstClickedCol = columnIndex;
                firstCaseClickedImg = clickedImageView;
            } else if (clickNumber == 2) {
                secondCaseClickedImg = clickedImageView;
            }
        }
    }

    private void resetImages(Image echiquier1, Image echiquier2) {
        if ((firstClickedCol + firstClickedRow) % 2 == 0) {
            firstCaseClickedImg.setImage(echiquier2);
        }
        else firstCaseClickedImg.setImage(echiquier1);

        if ((GridPane.getColumnIndex(secondCaseClickedImg) + GridPane.getRowIndex(secondCaseClickedImg)) % 2 == 0) {
            secondCaseClickedImg.setImage(echiquier2);
        }
        else secondCaseClickedImg.setImage(echiquier1);

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

    @FXML
    private void onChessClick(Event event) {
        ImageView clickedImageView = (ImageView) event.getSource();

        if (clickNumber == 1) {
            System.out.println("recup image");
        }

        else System.out.println("change piece");
    }
}