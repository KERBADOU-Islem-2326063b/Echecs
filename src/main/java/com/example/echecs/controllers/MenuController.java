package com.example.echecs.controllers;

import com.example.echecs.model.pieces.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Contrôleur du menu principal pour le jeu d'échecs.
 */
public class MenuController {
    @FXML
    private GridPane boardGrid;

    @FXML
    private Label Name;

    private ChessPiece[][] board = new ChessPiece[8][8];
    private Image whiteCaseImage, greenCaseImage;
    private King blackKing;
    private King whiteKing;

    /**
     * Méthode d'initialisation appelée après le chargement du fichier FXML.
     */
    @FXML
    public void initialize() {
        if (GameController.getFirstName() != null) Name.setText(GameController.getFirstName());
        initializeImagesLabels(); // Initialisation des images
        initializeBoard(); // Initialisation du plateau de jeu
        updateBoard(); // Mise à jour de l'affichage du plateau de jeu
    }

    /**
     * Initialise les images pour les cases du plateau.
     */
    private void initializeImagesLabels() {
        whiteCaseImage = new Image("file:src/main/resources/com/example/echecs/img/white_case.png");
        greenCaseImage = new Image("file:src/main/resources/com/example/echecs/img/green_case.png");
    }

    /**
     * Initialise les pièces sur le plateau de jeu.
     */
    private void initializeBoard() {
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn("Black", i, 1);
            board[6][i] = new Pawn("White", i, 6);
        }

        // Initialisation des tours
        board[0][0] = new Rook("Black", 0, 0);
        board[0][7] = new Rook("Black", 7, 0);
        board[7][0] = new Rook("White", 0, 7);
        board[7][7] = new Rook("White", 7, 7);

        // Initialisation des cavaliers
        board[0][1] = new Knight("Black", 1, 0);
        board[0][6] = new Knight("Black", 6, 0);
        board[7][1] = new Knight("White", 1, 7);
        board[7][6] = new Knight("White", 6, 7);

        // Initialisation des fous
        board[0][2] = new Bishop("Black", 2, 0);
        board[0][5] = new Bishop("Black", 5, 0);
        board[7][2] = new Bishop("White", 2, 7);
        board[7][5] = new Bishop("White", 5, 7);

        // Initialisation des reines
        board[0][3] = new Queen("Black", 3, 0);
        board[7][3] = new Queen("White", 3, 7);

        // Initialisation des rois
        blackKing = new King("Black", 4, 0);
        whiteKing = new King("White", 4, 7);
        board[0][4] = blackKing;
        board[7][4] = whiteKing;
    }

    /**
     * Met à jour l'affichage de la grille de jeu.
     */
    private void updateBoard() {
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

    /**
     * Crée une ImageView pour une case du plateau.
     *
     * @param row La ligne de la case.
     * @param col La colonne de la case.
     * @return L'ImageView de la case.
     */
    private ImageView createSquareImageView(int row, int col) {
        ImageView squareImageView = new ImageView();
        squareImageView.setFitWidth(72);
        squareImageView.setFitHeight(72);
        resetCases(squareImageView, row, col);
        return squareImageView;
    }

    /**
     * Crée une ImageView pour une pièce d'échecs.
     *
     * @param piece La pièce d'échecs.
     * @return L'ImageView de la pièce.
     */
    private ImageView createPieceImageView(ChessPiece piece) {
        ImageView pieceImageView = new ImageView(new Image(piece.getImagePath()));
        pieceImageView.setUserData(piece);
        pieceImageView.setFitWidth(70);
        pieceImageView.setFitHeight(70);
        return pieceImageView;
    }

    /**
     * Réinitialise l'image de la case sélectionnée.
     *
     * @param squareImageView L'ImageView de la case.
     * @param row             La ligne de la case.
     * @param col             La colonne de la case.
     */
    private void resetCases(ImageView squareImageView, int row, int col) {
        squareImageView.setImage((row + col) % 2 == 0 ? whiteCaseImage : greenCaseImage);
    }

    /**
     * Gère le clic sur le bouton "Jouer contre un ami".
     */
    @FXML
    private void onFriendButtonClick() {
        if (GameController.getFirstName() == null) {
            return;
        }
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/echecs/views/friendFXML/FriendSettings.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) boardGrid.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/example/echecs/stylesheets/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gère le clic sur le bouton "Jouer contre l'ordinateur".
     */
    @FXML
    private void onComputerButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/echecs/views/computerFXML/ComputerSettings.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) boardGrid.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/example/echecs/stylesheets/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gère le clic sur le bouton "Tournoi".
     */
    @FXML
    private void onTournamentButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/echecs/views/tournamentFXML/TournamentSettings.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) boardGrid.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/example/echecs/stylesheets/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gère le clic sur le bouton "Se connecter".
     */
    @FXML
    private void onLoginButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/echecs/views/accountFXML/Login.fxml"));
            Parent root = fxmlLoader.load();
            Stage stage = (Stage) boardGrid.getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/example/echecs/stylesheets/style.css").toExternalForm());
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
