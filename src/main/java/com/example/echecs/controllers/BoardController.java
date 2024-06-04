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
    int firstClickedCol = -1;
    int firstClickedRow = -1;
    boolean whiteTurn = true;

    ImageView firstCaseClickedImg;
    ImageView secondCaseClickedImg;
    ImageView firstChessClickedImg;
    String type;
    String color;
    String firstColor;

    @FXML
    GridPane boardGrid;

    @FXML
    private void onCaseClick(Event event) {
        if (clickNumber == 0) return;

        ImageView clickedImageView = (ImageView) event.getSource();

        Image clickedEchiquier1 = new Image("file:src/main/resources/com/example/echecs/img/echiquier1_clique.png");
        Image clickedEchiquier2 = new Image("file:src/main/resources/com/example/echecs/img/echiquier2_clique.png");
        Image echiquier1 = new Image("file:src/main/resources/com/example/echecs/img/echiquier1.png");
        Image echiquier2 = new Image("file:src/main/resources/com/example/echecs/img/echiquier2.png");

        columnIndex = GridPane.getColumnIndex(clickedImageView);
        rowIndex = GridPane.getRowIndex(clickedImageView);

        if (!(firstClickedCol == columnIndex && firstClickedRow == rowIndex) && canMove(color, type)) {
            updateImage(clickedImageView, columnIndex, rowIndex, clickedEchiquier1, clickedEchiquier2);
            GridPane.setRowIndex(firstChessClickedImg, rowIndex);
            GridPane.setColumnIndex(firstChessClickedImg, columnIndex);

            if (clickNumber == 2) {
                secondCaseClickedImg = clickedImageView;
                resetImages(echiquier1, echiquier2);
                clickNumber = 0;
                whiteTurn = !whiteTurn;
            }
        }
    }

    private void onChessAndCaseClick(int columnIndex, int rowIndex) {
        ImageView clickedImageView = getNodeAtPosition(columnIndex, rowIndex);

        if (clickedImageView != null) {
            Image clickedEchiquier1 = new Image("file:src/main/resources/com/example/echecs/img/echiquier1_clique.png");
            Image clickedEchiquier2 = new Image("file:src/main/resources/com/example/echecs/img/echiquier2_clique.png");
            Image echiquier1 = new Image("file:src/main/resources/com/example/echecs/img/echiquier1.png");
            Image echiquier2 = new Image("file:src/main/resources/com/example/echecs/img/echiquier2.png");

            if (clickNumber == 2) {
                resetImages(echiquier1, echiquier2);
                return;
            }

            if (clickNumber < 2 && !(firstClickedCol == columnIndex && firstClickedRow == rowIndex)) {
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
        } else {
            firstCaseClickedImg.setImage(echiquier1);
        }

        if ((GridPane.getColumnIndex(secondCaseClickedImg) + GridPane.getRowIndex(secondCaseClickedImg)) % 2 == 0) {
            secondCaseClickedImg.setImage(echiquier2);
        } else {
            secondCaseClickedImg.setImage(echiquier1);
        }

        clickNumber = 0;
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

        switch (type) {
            case "Knight":
                return (columnDifference == 2 && rowDifference == 1) || (columnDifference == 1 && rowDifference == 2);
            case "Bishop":
                return columnDifference == rowDifference;
            case "King":
                return columnDifference <= 1 && rowDifference <= 1;
            case "Pawn":
                int pawnMovement = color.equals("White") ? -1 : 1;
                return (columnIndex == firstClickedCol && (rowIndex - firstClickedRow == pawnMovement || (rowIndex - firstClickedRow == pawnMovement * 2 && (firstClickedRow == 1 || firstClickedRow == 6)))) ||
                        (columnDifference == 1 && rowDifference == 1 && rowIndex - firstClickedRow == pawnMovement && isOpponentPieceAt(columnIndex, rowIndex));
            case "Queen":
                return columnDifference == rowDifference || (columnIndex == firstClickedCol || rowIndex == firstClickedRow);
            case "Tower":
                return columnIndex == firstClickedCol || rowIndex == firstClickedRow;
            default:
                return false;
        }
    }

    private boolean canClick(String color) {
        return whiteTurn ? color.equals("White") : color.equals("Black");
    }

    private boolean isOpponentPieceAt(int col, int row) {
        ImageView piece = getNodeAtPosition(col, row);
        if (piece == null) return false;
        String pieceColor = piece.getUserData().toString().substring(0, 5);
        return !pieceColor.equals(color);
    }

    private ImageView getNodeAtPosition(int col, int row) {
        for (Node node : boardGrid.getChildren()) {
            if (node instanceof ImageView) {
                Integer nodeCol = GridPane.getColumnIndex(node);
                Integer nodeRow = GridPane.getRowIndex(node);
                if (nodeCol != null && nodeRow != null && nodeCol == col && nodeRow == row) {
                    return (ImageView) node;
                }
            }
        }
        return null;
    }

    @FXML
    private void onChessClick(Event event) {
        ImageView clickedImageView = (ImageView) event.getSource();
        color = clickedImageView.getUserData().toString().substring(0, 5);
        type = clickedImageView.getUserData().toString().substring(5);

        if (!canClick(color)) return;

        columnIndex = GridPane.getColumnIndex(clickedImageView) == null ? 0 : GridPane.getColumnIndex(clickedImageView);
        rowIndex = GridPane.getRowIndex(clickedImageView) == null ? 0 : GridPane.getRowIndex(clickedImageView);

        if (clickNumber == 0) {
            firstChessClickedImg = clickedImageView;
            firstColor = color;
            onChessAndCaseClick(columnIndex, rowIndex);
        } else if (!color.equals(firstColor)) {
            boardGrid.getChildren().remove(clickedImageView);
        }
    }
}
