package com.example.echecs.controllers.tournamentClass;

import com.example.echecs.controllers.GameController;
import com.example.echecs.model.pieces.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Contrôleur pour les paramètres du tournoi.
 */
public class TournamentSettings {
    @FXML
    private GridPane boardGrid;
    @FXML
    private MenuButton timeMenuButton;
    private ChessPiece[][] board = new ChessPiece[8][8];
    private Image whiteCaseImage, greenCaseImage;
    private King blackKing;
    private King whiteKing;

    /**
     * Initialise le contrôleur.
     */
    @FXML
    public void initialize() {
        initializeImagesLabels(); // Initialisation des images
        initializeBoard(); // Initialisation du plateau de jeu
        updateBoard();
        GameController.setInitialTimeInSeconds(600); // On met de base le temps à 10 minutes
    }

    /**
     * Initialise les images utilisées dans l'interface.
     */
    private void initializeImagesLabels() {
        // Chargement des images depuis les ressources
        whiteCaseImage = new Image("file:src/main/resources/com/example/echecs/img/white_case.png");
        greenCaseImage = new Image("file:src/main/resources/com/example/echecs/img/green_case.png");
    }

    /**
     * Initialise le plateau de jeu avec les pièces.
     */
    private void initializeBoard() {
        // Initialisation des pièces sur le plateau
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
     * Met à jour l'affichage du plateau de jeu.
     */
    private void updateBoard() {
        // Mise à jour de l'affichage de la grille de jeu
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
     * @return Une ImageView pour la case spécifiée.
     */
    private ImageView createSquareImageView(int row, int col) {
        // Création d'une ImageView pour une case du plateau
        ImageView squareImageView = new ImageView();
        squareImageView.setFitWidth(72);
        squareImageView.setFitHeight(72);
        resetCases(squareImageView, row, col);
        return squareImageView;
    }

    /**
     * Crée une ImageView pour une pièce d'échecs.
     *
     * @param piece La pièce d'échecs à afficher.
     * @return Une ImageView pour la pièce spécifiée.
     */
    private ImageView createPieceImageView(ChessPiece piece) {
        // Création d'une ImageView pour une pièce d'échecs
        ImageView pieceImageView = new ImageView(new Image(piece.getImagePath()));
        pieceImageView.setUserData(piece);
        pieceImageView.setFitWidth(70);
        pieceImageView.setFitHeight(70);
        return pieceImageView;
    }

    /**
     * Réinitialise l'image de la case spécifiée.
     *
     * @param squareImageView L'ImageView de la case.
     * @param row             La ligne de la case.
     * @param col             La colonne de la case.
     */
    private void resetCases(ImageView squareImageView, int row, int col) {
        // On remet l'image d'origine à la case sélectionnée
        squareImageView.setImage((row + col) % 2 == 0 ? whiteCaseImage : greenCaseImage);
    }

    /**
     * Gère l'action du menu.
     *
     * @param event L'événement déclencheur.
     */
    public void handleMenuAction(ActionEvent event) {
        MenuItem source = (MenuItem) event.getSource();
        timeMenuButton.setText(source.getText());

        if (timeMenuButton.getText().equals("5 minutes")) {
            GameController.setInitialTimeInSeconds(300); // 5 minutes
        } else if (timeMenuButton.getText().equals("10 minutes")) {
            GameController.setInitialTimeInSeconds(600); // 10 minutes
        } else GameController.setInitialTimeInSeconds(1200); // 20 minutes
    }

    /**
     * Gère l'action de démarrer une nouvelle partie.
     */
    @FXML
    private void onNewGame() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader
                    (getClass().getResource("/com/example/echecs/views/tournamentFXML/tournamentBoardGame.fxml"));
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
     * Gère l'action de charger une partie sauvegardée.
     */
    @FXML
    private void onChargeGame() {
        // Créer un sélecteur de fichiers
        FileChooser fileChooser = new FileChooser();

        // Afficher la boîte de dialogue d'ouverture de fichier
        File selectedFile = fileChooser.showOpenDialog(boardGrid.getScene().getWindow());

        // Si un fichier est sélectionné, procéder au chargement
        if (selectedFile != null) {
            GameController.setCharge(1);
            GameController.setChargedFile(selectedFile);

            // Définir le filtre d'extension pour ne permettre que les fichiers CSV
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Fichiers CSV (*.csv)", "*.csv");
            fileChooser.getExtensionFilters().add(extFilter);

            // On change de fenêtre
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/echecs/views/friendFXML/FriendBoardGame.fxml"));
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
}
