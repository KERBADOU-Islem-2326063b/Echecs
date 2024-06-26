package com.example.echecs.controllers.friendClass;

import com.example.echecs.controllers.GameController;
import com.example.echecs.model.pieces.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe représentant les paramètres pour jouer contre un ami
 */
public class FriendSettings {
    @FXML
    private GridPane boardGrid;
    @FXML
    private MenuButton timeMenuButton;
    @FXML
    private MenuButton playerlistButton;
    @FXML
    private Label Name;
    @FXML
    private Label ennemyName;
    private Map<String, String[]> playersData = new HashMap<>();
    private ChessPiece[][] board = new ChessPiece[8][8];
    private Image whiteCaseImage, greenCaseImage;
    private King blackKing;
    private King whiteKing;

    /**
     * Méthode d'initialisation appelée lors de la création du contrôleur.
     */
    @FXML
    public void initialize() {
        if (GameController.getFirstName() != null) Name.setText(GameController.getFirstName());
        initializeImagesLabels(); // Initialisation des images
        initializeBoard(); // Initialisation du plateau de jeu
        updateBoard();
        chargePlayers();
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
     * Met à jour l'affichage du plateau de jeu.
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
     * @param row L'indice de la ligne de la case.
     * @param col L'indice de la colonne de la case.
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
     * @param piece La pièce d'échecs à représenter.
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
     * Réinitialise l'affichage d'une case du plateau.
     *
     * @param squareImageView L'ImageView de la case à réinitialiser.
     * @param row             L'indice de la ligne de la case.
     * @param col             L'indice de la colonne de la case.
     */
    private void resetCases(ImageView squareImageView, int row, int col) {
        // On remet l'image d'origine à la case sélectionnée
        squareImageView.setImage((row + col) % 2 == 0 ? whiteCaseImage : greenCaseImage);
    }

    /**
     * Gère l'action du menu.
     *
     * @param event L'événement d'action du menu.
     */
    public void handleMenuAction(ActionEvent event) {
        MenuItem source = (MenuItem) event.getSource();
        timeMenuButton.setText(source.getText());

        if (timeMenuButton.getText().equals("5 minutes")) {
            GameController.setInitialTimeInSeconds(300); // 5 minutes
        } else if (timeMenuButton.getText().equals("10 minutes")) {
            GameController.setInitialTimeInSeconds(600); // 10 minutes
        } else {
            GameController.setInitialTimeInSeconds(1200); // 20 minutes
        }
    }

    /**
     * Charge les joueurs depuis un fichier CSV.
     */
    public void chargePlayers() {
        String line;
        String cvsSplitBy = ",";

        // Utilisation de ClassLoader pour obtenir la ressource sous forme de flux
        InputStream inputStream = getClass().getResourceAsStream("/com/example/echecs/accountFiles/accounts.csv");

        // Vérifie si le fichier a été trouvé
        if (inputStream == null) {
            System.err.println("Fichier introuvable : /com/example/echecs/accountFiles/accounts.csv");
            return;
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            while ((line = br.readLine()) != null) {
                String[] player = line.split(cvsSplitBy);
                String firstName = player[0];
                if (GameController.getFirstName().equals(firstName)) continue;
                MenuItem menuItem = new MenuItem(firstName);
                menuItem.setOnAction(event -> {
                    playerlistButton.setText(firstName);
                });
                playerlistButton.getItems().add(menuItem);
                playersData.put(firstName, player);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gère le choix de l'ennemi parmi la liste des joueurs chargés.
     */
    @FXML
    private void ennemyPlayer() {
        String selectedEnemyName = playerlistButton.getText();
        String[] enemyData = playersData.get(selectedEnemyName);
        if (enemyData != null) {
            ennemyName.setText(selectedEnemyName);
            GameController.setEnemyFirstName(selectedEnemyName);
            GameController.setEnemyLastName(enemyData[1]);
            GameController.setEnemyGamesPlayed(Integer.parseInt(enemyData[2]));
            GameController.setEnemyGamesWon(Integer.parseInt(enemyData[3]));
        }
    }

    /**
     * Ouvre une nouvelle partie.
     */
    @FXML
    private void onNewGame() {
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

    /**
     * Charge une partie existante.
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

