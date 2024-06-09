package com.example.echecs.controllers.accountClass;

import com.example.echecs.controllers.GameController;
import com.example.echecs.model.pieces.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Cette classe gère la connexion et la création de comptes pour le jeu d'échecs.
 */
public class loginAccount {
    @FXML
    private GridPane boardGrid;
    @FXML
    private MenuButton playerlistButton;
    @FXML
    private TextField newUserField;
    @FXML
    private Label gamesPlayedLabel;
    @FXML
    private Label gamesWonLabel;
    @FXML
    private Label Name;
    private ChessPiece[][] board = new ChessPiece[8][8];
    private Image whiteCaseImage, greenCaseImage;
    private King blackKing;
    private King whiteKing;
    private Map<String, String[]> playersData = new HashMap<>();

    /**
     * Méthode d'initialisation appelée après le chargement du fichier FXML.
     */
    @FXML
    public void initialize() {
        if (GameController.getFirstName() != null) Name.setText(GameController.getFirstName());
        initializeImagesLabels(); // Initialisation des images
        initializeBoard(); // Initialisation du plateau de jeu
        updateBoard(); // Mise à jour de l'affichage du plateau de jeu
        chargePlayers(); // Chargement des joueurs
    }

    /**
     * Initialise les images des cases du plateau.
     */
    private void initializeImagesLabels() {
        whiteCaseImage = new Image("file:src/main/resources/com/example/echecs/img/white_case.png");
        greenCaseImage = new Image("file:src/main/resources/com/example/echecs/img/green_case.png");
    }

    /**
     * Initialise les pièces sur le plateau de jeu.
     */
    private void initializeBoard() {
        // Initialisation des pions
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
     * @param row La ligne de la case.
     * @param col La colonne de la case.
     * @return Une ImageView représentant la case.
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
     * @return Une ImageView représentant la pièce d'échecs.
     */
    private ImageView createPieceImageView(ChessPiece piece) {
        ImageView pieceImageView = new ImageView(new Image(piece.getImagePath()));
        pieceImageView.setUserData(piece);
        pieceImageView.setFitWidth(70);
        pieceImageView.setFitHeight(70);
        return pieceImageView;
    }

    /**
     * Réinitialise l'image d'une case du plateau.
     *
     * @param squareImageView L'ImageView de la case.
     * @param row             La ligne de la case.
     * @param col             La colonne de la case.
     */
    private void resetCases(ImageView squareImageView, int row, int col) {
        squareImageView.setImage((row + col) % 2 == 0 ? whiteCaseImage : greenCaseImage);
    }

    /**
     * Crée un nouveau compte utilisateur.
     */
    @FXML
    private void onCreate() {
        try {
            String userInput = newUserField.getText().trim();
            if (userInput.isEmpty() || !userInput.contains(" ")) {
                return;
            }

            String[] nameParts = userInput.split(" ");
            String firstName = nameParts[0];
            String lastName = nameParts[1];

            String newUserLine = firstName + "," + lastName + ",0,0";

            try (FileWriter writer = new FileWriter("src/main/resources/com/example/echecs/accountFiles/accounts.csv", true)) {
                writer.write("\n" + newUserLine);
            } catch (IOException e) {
                e.printStackTrace();
            }

            GameController.setFirstName(firstName);
            GameController.setLastName(lastName);
            GameController.setGamesPlayed(0);
            GameController.setGamesWon(0);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/echecs/views/Menu.fxml"));
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
     * Connecte un utilisateur existant.
     */
    @FXML
    private void onConnect() {
        try {
            GameController.setFirstName(playerlistButton.getText());
            String[] playerData = playersData.get(playerlistButton.getText());
            GameController.setLastName(playerData[1]);
            GameController.setGamesPlayed(Integer.parseInt(playerData[2]));
            GameController.setGamesWon(Integer.parseInt(playerData[3]));

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/echecs/views/Menu.fxml"));
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
     * Charge les informations des joueurs depuis un fichier CSV.
     */
    public void chargePlayers() {
        String line;
        String cvsSplitBy = ",";

        InputStream inputStream = getClass().getResourceAsStream("/com/example/echecs/accountFiles/accounts.csv");

        if (inputStream == null) {
            System.err.println("File not found: /com/example/echecs/accountFiles/accounts.csv");
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream))) {
            while ((line = br.readLine()) != null) {
                String[] player = line.split(cvsSplitBy);
                String firstName = player[0];
                MenuItem menuItem = new MenuItem(firstName);
                menuItem.setOnAction(event -> {
                    playerlistButton.setText(firstName);
                    updateLabels(firstName);
                });
                playerlistButton.getItems().add(menuItem);

                // Vérifie si le tableau de données du joueur contient suffisamment d'éléments
                if (player.length >= 4) {
                    playersData.put(firstName, player);
                } else {
                    System.err.println("Le tableau de données du joueur ne contient pas assez d'éléments : " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Met à jour les labels des informations du joueur.
     *
     * @param firstName Le prénom du joueur.
     */
    private void updateLabels(String firstName) {
        String[] player = playersData.get(firstName);
        if (player != null) {
            gamesPlayedLabel.setText("Parties jouées: " + player[2]);
            gamesWonLabel.setText("Parties gagnées: " + player[3]);
        }
    }
}
