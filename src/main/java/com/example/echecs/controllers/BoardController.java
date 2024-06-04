package com.example.echecs.controllers;

import com.example.echecs.model.*;
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
    String type;
    String color;

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

        if (!(firstClickedCol == columnIndex && firstClickedRow == rowIndex) && canMove(color, type)) {
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
        ++clickNumber;
    }

    private boolean canMove(String color, String type) {
        int columnDifference = Math.abs(columnIndex - firstClickedCol);
        int rowDifference = Math.abs(rowIndex - firstClickedRow);

        if (type.equals("Knight")) {
            return (columnDifference == 2 && rowDifference == 1) || (columnDifference == 1 && rowDifference == 2);
        } else if (type.equals("Bishop")) {
            return columnDifference == rowDifference;

        } else if (type.equals("King")) {
            return columnDifference <= 1 && rowDifference <= 1;

        } else if (type.equals("Pawn")) {
            int pawnMovement = color.equals("White")? -1 : 1;
            return (columnIndex == firstClickedCol && (rowIndex - firstClickedRow == pawnMovement * 2 || rowIndex - firstClickedRow == pawnMovement));

        } else if (type.equals("Queen")) {
            return (columnDifference == rowDifference || (columnIndex == firstClickedCol || rowIndex == firstClickedRow));

        } else if (type.equals("Rook") || type.equals("Tower")) {
            return columnIndex == firstClickedCol || rowIndex == firstClickedRow;

        } else {
            return false;
        }
    }

    @FXML
    private void onChessClick(Event event) {
        ImageView clickedImageView = (ImageView) event.getSource();
        color = clickedImageView.getUserData().toString().substring(0, 5);
        type = clickedImageView.getUserData().toString().substring(5, clickedImageView.getUserData().toString().length());
        System.out.println(clickedImageView.getUserData());

        if (GridPane.getColumnIndex(clickedImageView) == null && GridPane.getRowIndex(clickedImageView) == null) {
            columnIndex = 0;
            rowIndex = 0;
        }
        else if (GridPane.getColumnIndex(clickedImageView) == null && GridPane.getRowIndex(clickedImageView) != null) {
            columnIndex = 0;
            rowIndex = GridPane.getRowIndex(clickedImageView);
        }

        else if (GridPane.getRowIndex(clickedImageView) == null && GridPane.getColumnIndex(clickedImageView) != null) {
            rowIndex = 0;
            columnIndex = GridPane.getColumnIndex(clickedImageView);
        }

        else {
            columnIndex = GridPane.getColumnIndex(clickedImageView);
            rowIndex = GridPane.getRowIndex(clickedImageView);
        }

        if (clickNumber == 0) {
            firstChessClickedImg = clickedImageView;
            onChessAndCaseClick(columnIndex, rowIndex);
        }

        else  {
            // boardGrid.getChildren().remove(clickedImageView);
        };
    }
}