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
    ImageView firstChessClickedImg;

    @FXML
    GridPane boardGrid;

    @FXML
    private void onCaseClick(Event event) {
        // Si on clique pas d'abord sur un pion, on sort de la fonction
        if (clickNumber == 0) return;

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
            GridPane.setRowIndex(firstChessClickedImg, rowIndex);
            GridPane.setColumnIndex(firstChessClickedImg, columnIndex);

            if (clickNumber == 2) {
                secondCaseClickedImg = clickedImageView;
            }
        }
    }

    private void onChessAndCaseClick(int columnIndex, int rowIndex) {
        ImageView clickedImageView = null;

        for (Node node : boardGrid.getChildren()) {
            if (node instanceof ImageView) {
                int currentColumnIndex = GridPane.getColumnIndex(node);
                int currentRowIndex = GridPane.getRowIndex(node);

                if (currentColumnIndex == columnIndex && currentRowIndex == rowIndex) {
                    clickedImageView = (ImageView) node;
                    break;
                }
            }
        }

        if (clickedImageView!= null) {
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

            if (clickNumber < 2 &&!(firstClickedCol == columnIndex && firstClickedRow == rowIndex)) {
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

        columnIndex = GridPane.getColumnIndex(clickedImageView);
        rowIndex = GridPane.getRowIndex(clickedImageView);

        if (clickNumber == 0) {
            firstChessClickedImg = clickedImageView;
            onChessAndCaseClick(columnIndex, rowIndex);
        }

        // Si on clique sur une autre piece, a faire plus tard
        else  {
            // boardGrid.getChildren().remove(firstChessClickedImg);
        };
    }
}