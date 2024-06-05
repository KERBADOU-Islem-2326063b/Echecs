package com.example.echecs.controllers;

import com.example.echecs.model.*;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

public class BoardController {
    private ChessPiece[][] board = new ChessPiece[8][8];
    private List<ImageView> highlightedSquares = new ArrayList<>();
    private ChessPiece selectedPiece;
    private boolean whiteTurn = true;
    private boolean endGame = false;
    private ImageView oldClickedSquare;
    private ChessPiece[][] previousBoardState = new ChessPiece[8][8];

    @FXML
    private GridPane boardGrid;
    @FXML
    private Label turnLabel;

    private King blackKing;
    private King whiteKing;

    private Image whiteCaseImage = new Image("file:src/main/resources/com/example/echecs/img/white_case.png");
    private Image greenCaseImage = new Image("file:src/main/resources/com/example/echecs/img/green_case.png");
    private Image whiteCaseClickedImage = new Image("file:src/main/resources/com/example/echecs/img/white_case_clicked.png");
    private Image greenCaseClickedImage = new Image("file:src/main/resources/com/example/echecs/img/green_case_clicked.png");
    private Image whiteCaseDotImage = new Image("file:src/main/resources/com/example/echecs/img/white_case_dot.png");
    private Image greenCaseDotImage = new Image("file:src/main/resources/com/example/echecs/img/green_case_dot.png");
    private Image redSquareImage = new Image("file:src/main/resources/com/example/echecs/img/red_square.png");

    @FXML
    public void initialize() {
        initializeBoard();
        previousBoardState = copyBoardState(board);
        initializeBoardUI();
    }

    private void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn("Black", i, 1);
            board[6][i] = new Pawn("White", i, 6);
        }

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

        blackKing = new King("Black", 4, 0);
        whiteKing = new King("White", 4, 7);
        board[0][4] = blackKing;
        board[7][4] = whiteKing;
    }

    private void initializeBoardUI() {
        boardGrid.getChildren().clear();
        for (int row = 0; row < 8; ++row) {
            for (int col = 0; col < 8; ++col) {
                ImageView squareImageView = createSquareImageView(row, col);
                boardGrid.add(squareImageView, col, row);

                ChessPiece piece = board[row][col];
                if (piece != null) {
                    ImageView pieceImageView = createPieceImageView(piece);
                    boardGrid.add(pieceImageView, col, row);
                }
            }
        }
    }

    private ChessPiece[][] copyBoardState(ChessPiece[][] original) {
        ChessPiece[][] copy = new ChessPiece[8][8];
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (original[row][col] != null) {
                    copy[row][col] = original[row][col].copy();
                }
            }
        }
        return copy;
    }

    private ImageView createSquareImageView(int row, int col) {
        ImageView squareImageView = new ImageView();
        squareImageView.setFitWidth(72);
        squareImageView.setFitHeight(72);
        squareImageView.setOnMouseClicked(this::onBoardClick);
        resetCases(squareImageView, row, col);
        return squareImageView;
    }

    private ImageView createPieceImageView(ChessPiece piece) {
        ImageView pieceImageView = new ImageView(new Image(piece.getImagePath()));
        pieceImageView.setUserData(piece);
        pieceImageView.setOnMouseClicked(this::onBoardClick);
        pieceImageView.setFitWidth(70);
        pieceImageView.setFitHeight(70);
        return pieceImageView;
    }

    private void updateBoardUI() {
        boardGrid.getChildren().clear();
        initializeBoardUI();
    }

    private void onBoardClick(Event event) {
        Node clickedNode = (Node) event.getSource();
        int targetCol = GridPane.getColumnIndex(clickedNode);
        int targetRow = GridPane.getRowIndex(clickedNode);

        if (clickedNode instanceof ImageView) {
            ChessPiece clickedPiece = (ChessPiece) (clickedNode).getUserData();
            if (clickedPiece != null) {
                if (clickedPiece.getColor().equals(whiteTurn ? "White" : "Black")) {
                    if (selectedPiece != null) {
                        resetCases(oldClickedSquare, selectedPiece.getRowIndex(), selectedPiece.getColumnIndex());
                    }
                    selectedPiece = clickedPiece;
                    oldClickedSquare = (ImageView) getNodeFromGridPane(boardGrid, selectedPiece.getRowIndex(), selectedPiece.getColumnIndex());
                    highlightSelectedSquare(oldClickedSquare);
                    highlightValidMoves(selectedPiece);
                } else if (selectedPiece != null && selectedPiece.canMove(targetCol, targetRow, board)) {
                    movePiece(selectedPiece, targetCol, targetRow);
                }
            } else if (selectedPiece != null && selectedPiece.canMove(targetCol, targetRow, board)) {
                movePiece(selectedPiece, targetCol, targetRow);
            }
        }
    }

    private void movePiece(ChessPiece piece, int targetCol, int targetRow) {
        board[piece.getRowIndex()][piece.getColumnIndex()] = null;
        piece.setPosition(targetRow, targetCol);
        board[targetRow][targetCol] = piece;

        updateBoardUI();
        switchTurn();
        clearHighlightedSquares();
    }

    private void clearHighlightedSquares() {
        for (ImageView squareImageView : highlightedSquares) {
            resetCases(squareImageView, GridPane.getRowIndex(squareImageView), GridPane.getColumnIndex(squareImageView));
        }
        highlightedSquares.clear();
    }

    private void highlightValidMoves(ChessPiece piece) {
        clearHighlightedSquares();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (piece.canMove(col, row, board)) {
                    Node node = getNodeFromGridPane(boardGrid, row, col);
                    if (node instanceof ImageView squareImageView) {
                        if (board[row][col] != null && !board[row][col].getColor().equals(piece.getColor())) {
                            // Highlight capture moves with red squares
                            squareImageView.setImage(redSquareImage);
                        } else {
                            // Highlight normal moves
                            if ((col + row) % 2 == 0) squareImageView.setImage(whiteCaseDotImage);
                            else squareImageView.setImage(greenCaseDotImage);
                        }
                        highlightedSquares.add(squareImageView);
                        squareImageView.setOnMouseClicked(this::onBoardClick);
                    }
                }
            }
        }
    }

    private void highlightSelectedSquare(ImageView squareImageView) {
        int col = GridPane.getColumnIndex(squareImageView);
        int row = GridPane.getRowIndex(squareImageView);
        if ((row + col) % 2 == 0) {
            squareImageView.setImage(whiteCaseClickedImage);
        } else {
            squareImageView.setImage(greenCaseClickedImage);
        }
    }

    private void resetCases(ImageView squareImageView, int row, int col) {
        if ((row + col) % 2 == 0) {
            squareImageView.setImage(whiteCaseImage);
        } else {
            squareImageView.setImage(greenCaseImage);
        }
    }

    private void switchTurn() {
        whiteTurn = !whiteTurn;
        selectedPiece = null;
        if (whiteTurn) {
            turnLabel.setText("C'est au tour des blancs !");
            if (whiteKing.isCheck(board)) {
                highlightCheckSquare(whiteKing);
                turnLabel.setText("C'est au tour des blancs !\nRoi Blanc en échec !");
            }
        } else {
            turnLabel.setText("C'est au tour des noirs !");
            if (blackKing.isCheck(board)) {
                highlightCheckSquare(blackKing);
                turnLabel.setText("C'est au tour des noirs !\nRoi Noir en échec !");
            }
        }
    }

    private void highlightCheckSquare(King king) {
        int kingRow = king.getRowIndex();
        int kingCol = king.getColumnIndex();
        ImageView squareImageView = (ImageView) getNodeFromGridPane(boardGrid, kingRow, kingCol);
        if (squareImageView != null) {
            squareImageView.setImage(redSquareImage);
        }
    }

    private Node getNodeFromGridPane(GridPane gridPane, int row, int col) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == row &&
                    GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == col) {
                return node;
            }
        }
        return null;
    }

    private void endGame() {
        if (!whiteTurn) turnLabel.setText("C'est la fin de la partie, victoire des noirs !");
        else turnLabel.setText("C'est la fin de la partie, victoire des blancs !");
        endGame = true;
    }
}
