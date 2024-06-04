package com.example.echecs.controllers;

import com.example.echecs.model.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

public class BoardController {
    private ChessPiece[][] board = new ChessPiece[8][8];
    private ChessPiece selectedPiece;
    private boolean whiteTurn = true;
    private ImageView oldClickedSquare;

    @FXML
    private GridPane boardGrid;

    @FXML
    public void initialize() {
        initializeBoard();
        updateBoardUI();
    }

    private void initializeBoard() {
        // Add Pawns
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn("Black", i, 1);
            board[6][i] = new Pawn("White", i, 6);
        }

        // Add other pieces
        board[0][0] = new Rook("Black", 0, 0);
        board[0][7] = new Rook("Black", 7, 0);
        board[7][0] = new Rook("White", 0, 7);
        board[7][7] = new Rook("White", 7, 7);

        board[0][1] = new Knight("Black", 1, 0);
        board[0][6] = new Knight("Black", 6, 0);
        board[7][1] = new Knight("White", 1, 7);
        board[7][6] = new Knight("White", 6, 7);

        board[0][2] = new Bishop("Black", 2, 0);
        board[0][5] = new Bishop("Black", 5, 0);
        board[7][2] = new Bishop("White", 2, 7);
        board[7][5] = new Bishop("White", 5, 7);

        board[0][3] = new Queen("Black", 3, 0);
        board[7][3] = new Queen("White", 3, 7);

        board[0][4] = new King("Black", 4, 0);
        board[7][4] = new King("White", 4, 7);
    }

    private void updateBoardUI() {
        boardGrid.getChildren().clear();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ImageView squareImageView = new ImageView();
                squareImageView.setFitWidth(70);
                squareImageView.setFitHeight(70);

                if ((row + col) % 2 == 0) {
                    squareImageView.setImage(new Image("file:src/main/resources/com/example/echecs/img/green_case.png"));
                } else {
                    squareImageView.setImage(new Image("file:src/main/resources/com/example/echecs/img/white_case.png"));
                }
                squareImageView.setOnMouseClicked(this::onCaseClick);

                boardGrid.add(squareImageView, col, row);

                ChessPiece piece = board[row][col];
                if (piece != null) {
                    ImageView pieceImageView = new ImageView(new Image(piece.getImagePath()));
                    pieceImageView.setUserData(piece);
                    pieceImageView.setOnMouseClicked(this::onChessClick);
                    pieceImageView.setFitWidth(70);
                    pieceImageView.setFitHeight(70);
                    boardGrid.add(pieceImageView, col, row);
                }
            }
        }
    }

    private void onCaseClick(Event event) {
        if (selectedPiece == null) return;

        Node clickedNode = (Node) event.getSource();
        int targetCol = GridPane.getColumnIndex(clickedNode);
        int targetRow = GridPane.getRowIndex(clickedNode);

        if (selectedPiece.canMove(targetCol, targetRow, board)) {
            movePiece(selectedPiece, targetCol, targetRow);
            switchTurn();
        }
    }

    private void onChessClick(Event event) {
        ImageView clickedImageView = (ImageView) event.getSource();
        ChessPiece piece = (ChessPiece) clickedImageView.getUserData();
        if (piece != null && piece.getColor().equals(whiteTurn ? "White" : "Black")) {
            selectedPiece = piece;
            int row = GridPane.getRowIndex(clickedImageView);
            int col = GridPane.getColumnIndex(clickedImageView);
            Node targetSquare = null;

            for (Node node : boardGrid.getChildren()) {
                if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                    targetSquare = node;
                    break;
                }
            }

            if (targetSquare instanceof ImageView squareImageView) {
                changeSquareColor(squareImageView, col, row);
            }
        }
    }

    private void movePiece(ChessPiece piece, int targetCol, int targetRow) {
        board[piece.getRowIndex()][piece.getColumnIndex()] = null;
        piece.setPosition(targetCol, targetRow);
        board[targetRow][targetCol] = piece;
        updateBoardUI();
    }

    private void changeSquareColor(ImageView targetSquare, int targetCol, int targetRow) {
        if ((targetRow + targetCol) % 2 == 0) {
            if (oldClickedSquare != null) oldClickedSquare.setImage(new Image("file:src/main/resources/com/example/echecs/img/white_case.png"));
            targetSquare.setImage(new Image("file:src/main/resources/com/example/echecs/img/green_case_clicked.png"));
        } else {
            if (oldClickedSquare != null) oldClickedSquare.setImage(new Image("file:src/main/resources/com/example/echecs/img/green_case.png"));
            targetSquare.setImage(new Image("file:src/main/resources/com/example/echecs/img/white_case_clicked.png"));
        }
        oldClickedSquare = targetSquare;
    }

    private void switchTurn() {
        whiteTurn = !whiteTurn;
        selectedPiece = null;
    }
}

