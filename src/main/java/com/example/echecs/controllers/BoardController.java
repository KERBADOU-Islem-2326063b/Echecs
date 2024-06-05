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
    // Le plateau de jeu et les pièces
    private ChessPiece[][] board = new ChessPiece[8][8];
    // Les cases jouables
    private List<ImageView> highlightedSquares = new ArrayList<>();
    // La pièce sélectionnée
    private ChessPiece selectedPiece;
    // Tour du joueur blanc ou noir
    private boolean whiteTurn = true;
    // Si c'est la fin
    private boolean endGame = false;
    // Carré précédemment cliqué
    private ImageView oldClickedSquare;

    @FXML
    private GridPane boardGrid;
    @FXML
    private Label turnLabel;

    King BlackKing;
    King WhiteKing;

    @FXML
    public void initialize() {
        // Initialisation du plateau de jeu
        initializeBoard();
        // Mise à jour du plateau de jeu
        updateBoard();
    }

    // Initialisation du plateau de jeu avec les pièces
    private void initializeBoard() {
        // Placement initial des pions
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn("Black", i, 1);
            board[6][i] = new Pawn("White", i, 6);
        }

        // Placement initial des autres pièces
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

        BlackKing = new King("Black", 4, 0);
        WhiteKing = new King("White", 4, 7);
        board[0][4] = BlackKing;
        board[7][4] = WhiteKing;
    }

    // Mise à jour du plateau de jeu avec les pièces
    private void updateBoard() {
        // Effacer le plateau de jeu
        boardGrid.getChildren().clear();
        // Parcourir le plateau pour afficher les cases et les pièces
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ImageView squareImageView = new ImageView();
                squareImageView.setFitWidth(72);
                squareImageView.setFitHeight(72);

                // Changer l'image en fonction de la couleur de la case
                resetCases(squareImageView, row, col);

                // Vérifier si le roi est en échec pour changer la couleur de la case
                if (board[row][col] instanceof King) {
                    King king = (King) board[row][col];
                    if (king.isCheck(board)) {
                        squareImageView.setImage(new Image("file:src/main/resources/com/example/echecs/img/red_square.png"));
                    }
                }

                boardGrid.add(squareImageView, col, row);

                // Afficher les pièces sur le plateau
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

    // Gestionnaire d'événements lorsqu'une case est cliquée
    private void onCaseClick(Event event) {
        System.out.println("click");
        if (selectedPiece == null) return;

        Node clickedNode = (Node) event.getSource();
        int targetCol = GridPane.getColumnIndex(clickedNode);
        int targetRow = GridPane.getRowIndex(clickedNode);

        // Vérifier si la pièce sélectionnée peut se déplacer vers la case cliquée
        if (selectedPiece.canMove(targetCol, targetRow, board)) {
            // Déplacer la pièce
            movePiece(selectedPiece, targetCol, targetRow);

            // Passer au tour suivant
            switchTurn();
        }
    }

    // Gestionnaire d'événements lorsqu'une pièce est cliquée
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
                highlightValidMoves(piece);
                changeSquareColor(squareImageView, col, row);
            }
        }
        onCaseClick(event);
    }

    // Déplacer une pièce sur le plateau
    private void movePiece(ChessPiece piece, int targetCol, int targetRow) {
        board[piece.getRowIndex()][piece.getColumnIndex()] = null;
        piece.setPosition(targetCol, targetRow);
        board[targetRow][targetCol] = piece;
        updateBoard();
    }

    // Changer la couleur de la case cliquée pour indiquer la sélection
    private void changeSquareColor(ImageView targetSquare, int targetCol, int targetRow) {
        if ((targetRow + targetCol) % 2 == 0) {
            if (oldClickedSquare != null) oldClickedSquare.setImage(new Image("file:src/main/resources/com/example/echecs/img/green_case.png"));
            targetSquare.setImage(new Image("file:src/main/resources/com/example/echecs/img/white_case_clicked.png"));
        } else {
            if (oldClickedSquare != null) oldClickedSquare.setImage(new Image("file:src/main/resources/com/example/echecs/img/white_case.png"));
            targetSquare.setImage(new Image("file:src/main/resources/com/example/echecs/img/green_case_clicked.png"));
        }
        oldClickedSquare = targetSquare;
    }

    // Récupérer la colonne de la GridPane à telle indice
    private Node getNodeFromGridPane(GridPane gridPane, int row, int col) {
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return node;
            }
        }
        return null;
    }
    // Méthode pour mettre en avant les cases déplacables
    private void highlightValidMoves(ChessPiece piece) {
        highlightedSquares.clear();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Node node = getNodeFromGridPane(boardGrid, row, col);
                ImageView squareImageView = (ImageView) node;
                resetCases(squareImageView, row, col);

                if (piece.canMove(col, row, board)) {
                    if (node instanceof ImageView) {
                        if ((col + row) % 2 == 0) squareImageView.setImage(new Image("file:src/main/resources/com/example/echecs/img/white_case_dot.png"));
                        else squareImageView.setImage(new Image("file:src/main/resources/com/example/echecs/img/green_case_dot.png"));
                        highlightedSquares.add(squareImageView);
                    }
                    squareImageView.setOnMouseClicked(this::onCaseClick);
                }
            }
        }
    }

    private void resetCases(ImageView squareImageView, int row, int col) {
        if ((row + col) % 2 == 0) {
            squareImageView.setImage(new Image("file:src/main/resources/com/example/echecs/img/white_case.png"));
        } else {
            squareImageView.setImage(new Image("file:src/main/resources/com/example/echecs/img/green_case.png"));
        }
    }

    // Passer au tour suivant
    private void switchTurn() {
        whiteTurn = !whiteTurn;
        // On met à jour le tour et si c'est la fin de la partie
        if (whiteTurn) {
            turnLabel.setText("C'est au tour des blancs !");
            if (WhiteKing.isCheck(board)) {
                endGame();
            }
        } else {
            turnLabel.setText("C'est au tour des noirs !");
            if (BlackKing.isCheck(board)) {
                endGame();
            }
        }
        selectedPiece = null;
        // Mise à jour du plateau pour refléter les changements (notamment pour l'affichage de l'échec)
        updateBoard();
    }

    // Fonction pour mettre fin à la partie
    private void endGame() {
        if (!whiteTurn) turnLabel.setText("C'est la fin de la partie, victoire des noirs !");
        else turnLabel.setText("C'est la fin de la partie, victoire des blancs !");
        endGame = true;
    }
}
