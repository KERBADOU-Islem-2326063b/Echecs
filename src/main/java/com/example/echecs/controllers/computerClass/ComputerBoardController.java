package com.example.echecs.controllers.computerClass;

import com.example.echecs.controllers.GameController;
import com.example.echecs.model.gameStatus.GameState;
import com.example.echecs.model.gameStatus.Move;
import com.example.echecs.model.pieces.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Controlleur pour le jeu d'échec contre un bot
 */
public class ComputerBoardController {
    private ChessPiece[][] board = new ChessPiece[8][8];
    private List<Move> moveHistory = new ArrayList<>();
    private List<ImageView> highlightedSquares = new ArrayList<>();
    private ChessPiece selectedPiece;
    private boolean whiteTurn = true;
    private boolean endGame = false;
    private ImageView oldClickedSquare;

    @FXML
    private GridPane boardGrid;

    @FXML
    private Label logJeu;

    @FXML
    private ImageView player1ImageView;

    @FXML
    private ImageView player2ImageView;
    @FXML
    private Label whiteTimeLabel = new Label("00:00");
    @FXML
    private Label blackTimeLabel = new Label("00:00");
    @FXML
    private Button undoMovement;
    @FXML
    private Button surrenderButton;
    @FXML
    private Button saveGameButton;
    @FXML
    private Label Name;
    @FXML
    private Label rightName;
    private Timeline whiteTimer;
    private Timeline blackTimer;
    int whiteSecondsRemaining = GameController.getInitialTimeInSeconds(); // On récupère le temps défini avant
    int blackSecondsRemaining = GameController.getInitialTimeInSeconds();

    private King blackKing;
    private King whiteKing;

    private Image whiteCaseImage, greenCaseImage, whiteCaseClickedImage, greenCaseClickedImage, whiteCaseDotImage, greenCaseDotImage, redSquareImage;

    /**
     * Initialise le contrôleur et prépare le jeu pour commencer.
     */
    @FXML
    public void initialize() {
        if (GameController.getFirstName() != null) {
            Name.setText(GameController.getFirstName());
            rightName.setText(GameController.getFirstName());
            GameController.updateStats(GameController.getFirstName(), GameController.getLastName(), false);
        }
        initializeImagesLabels(); // Initialisation des images

        if (GameController.getCharge() == 0)  {
            initializeBoard(); // Initialisation du plateau de jeu
            initializeTimers(); // Initilisation du temps
        }
        else restoreGameState(GameController.getChargedFile());
        player1ImageView.getStyleClass().add("player-turn");
        updateBoard(); // Mise à jour de l'affichage du plateau

        // On initialise les boutons
        undoMovement.setOnAction(this::handleUndoButton);
        surrenderButton.setOnAction(this::handleSurrenderButton);
        saveGameButton.setOnAction(this::handleSaveButton);
    }


    /**
     * Initialise les timers pour les joueurs blanc et noir.
     */
    public void initializeTimers() {
        whiteTimeLabel.setText(formatTime(whiteSecondsRemaining));
        blackTimeLabel.setText(formatTime(blackSecondsRemaining));

        // Initialisation du temps pour le joueur blanc
        whiteTimer = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    if (whiteSecondsRemaining > 0) --whiteSecondsRemaining;
                    whiteTimeLabel.setText(formatTime(whiteSecondsRemaining));
                    if (whiteSecondsRemaining <= 0) {
                        endGame("Fin du temps imparti, victoire des noirs");
                    }
                })
        );
        whiteTimer.setCycleCount(Timeline.INDEFINITE);

        blackTimer = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    if (blackSecondsRemaining > 0) --blackSecondsRemaining;
                    blackTimeLabel.setText(formatTime(blackSecondsRemaining));
                    if (blackSecondsRemaining <= 0) {
                        endGame("Fin du temps imparti, victoire des blancs");
                    }
                })
        );
        blackTimer.setCycleCount(Timeline.INDEFINITE);

        // On lance le timer
        whiteTimer.play();
    }

    /**
     * Formate le temps restant sous forme de minutes et secondes.
     *
     * @param seconds Le nombre total de secondes.
     * @return Une chaîne de caractères représentant le temps sous forme MM:SS.
     */
    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

    /**
     * Initialise les images utilisées pour représenter le plateau de jeu.
     */
    private void initializeImagesLabels() {
        // Chargement des images depuis les ressources
        whiteCaseImage = new Image("file:src/main/resources/com/example/echecs/img/white_case.png");
        greenCaseImage = new Image("file:src/main/resources/com/example/echecs/img/green_case.png");
        whiteCaseClickedImage = new Image("file:src/main/resources/com/example/echecs/img/white_case_clicked.png");
        greenCaseClickedImage = new Image("file:src/main/resources/com/example/echecs/img/green_case_clicked.png");
        whiteCaseDotImage = new Image("file:src/main/resources/com/example/echecs/img/white_case_dot.png");
        greenCaseDotImage = new Image("file:src/main/resources/com/example/echecs/img/green_case_dot.png");
        redSquareImage = new Image("file:src/main/resources/com/example/echecs/img/red_square.png");
    }

    /**
     * Initialise le plateau de jeu avec les pièces de départ.
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
        // Mis a jour de l'affichage de la grille de jeu
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
     * @return L'ImageView créée pour la case.
     */
    private ImageView createSquareImageView(int row, int col) {
        // Création d'une ImageView pour une case du plateau
        ImageView squareImageView = new ImageView();
        squareImageView.setFitWidth(72);
        squareImageView.setFitHeight(72);
        squareImageView.setOnMouseClicked(this::onBoardClick);
        resetCases(squareImageView, row, col); // Réinitialisation de l'apparence de la case
        return squareImageView;
    }

    /**
     * Crée une ImageView pour une pièce d'échecs.
     *
     * @param piece La pièce dont l'ImageView doit être créée.
     * @return L'ImageView créée pour la pièce.
     */
    private ImageView createPieceImageView(ChessPiece piece) {
        // Création d'une ImageView pour une pièce d'échecs
        ImageView pieceImageView = new ImageView(new Image(piece.getImagePath()));
        pieceImageView.setUserData(piece);
        pieceImageView.setOnMouseClicked(this::onBoardClick);
        pieceImageView.setFitWidth(70);
        pieceImageView.setFitHeight(70);
        return pieceImageView;
    }

    /**
     * Gère le clic sur une case du plateau.
     *
     * @param event L'événement de clic.
     */
    private void onBoardClick(Event event) {
        // Vérifier si la partie est terminée
        if (endGame) return;

        // Récupérer le noeud (ImageView) cliqué sur le plateau
        Node clickedNode = (Node) event.getSource();
        // Récupérer la colonne et la ligne de la case cliquée
        Integer targetCol = GridPane.getColumnIndex(clickedNode);
        Integer targetRow = GridPane.getRowIndex(clickedNode);

        // Vérifier si la case cliquée contient une pièce d'échecs
        if (clickedNode instanceof ImageView) {
            ChessPiece clickedPiece = (ChessPiece) clickedNode.getUserData();

            // Si une pièce est présente sur la case cliquée, gérer sa sélection
            if (clickedPiece != null) {
                handlePieceSelection(clickedPiece);
            }
            // Sinon, si une pièce est déjà sélectionnée et que le mouvement est valide, déplacer la pièce
            else if (selectedPiece != null && targetCol != null && targetRow != null && selectedPiece.canMove(targetCol, targetRow, board)) {
                animatePieceMove(selectedPiece, targetCol, targetRow);
            }
        }
    }

    /**
     * Sélectionne une pièce sur le plateau.
     *
     * @param clickedPiece La pièce sélectionnée.
     */
    private void handlePieceSelection(ChessPiece clickedPiece) {
        // Vérifier si la pièce sélectionnée est de la couleur correspondant au tour actuel
        if (clickedPiece.getColor().equals(whiteTurn ? "White" : "Black")) {
            // Si une pièce était déjà sélectionnée, réinitialiser son apparence
            if (selectedPiece != null) {
                resetCases(oldClickedSquare, selectedPiece.getRowIndex(), selectedPiece.getColumnIndex());
            }
            // Mettre à jour la pièce sélectionnée et mettre en évidence sa case
            selectedPiece = clickedPiece;
            ImageView clickedSquare = (ImageView) getNodeFromGridPane(boardGrid, selectedPiece.getRowIndex(), selectedPiece.getColumnIndex());
            highlightSelectedSquare(clickedSquare);
            oldClickedSquare = clickedSquare;
            // Mettre en évidence les mouvements valides pour la pièce sélectionnée
            highlightValidMoves(selectedPiece);
        }
        // Si une pièce est déjà sélectionnée et que le clic est sur une case vide ou une case avec une pièce adverse,
        // vérifier si le mouvement est valide et déplacer la pièce le cas échéant
        else if (selectedPiece != null && selectedPiece.canMove(clickedPiece.getColumnIndex(), clickedPiece.getRowIndex(), board)) {
            animatePieceMove(selectedPiece, clickedPiece.getColumnIndex(), clickedPiece.getRowIndex());
        }
    }

    /**
     * Anime le mouvement d'une pièce sur le plateau.
     *
     * @param piece La pièce à déplacer.
     * @param targetCol La colonne de destination.
     * @param targetRow La ligne de destination.
     */
    private void animatePieceMove(ChessPiece piece, int targetCol, int targetRow) {
        int row = piece.getRowIndex();
        int col = piece.getColumnIndex();

        // On recupere l'imageview de la piece
        ImageView pieceImageView = (ImageView) getNodeFromGridPane(boardGrid, row, col, piece);
        if (pieceImageView == null) return;

        // On retire les cases avec des points
        clearHighlightedSquares();

        // On calcul la distance pour l'animation
        double distanceX = (targetCol - col) * 70;
        double distanceY = (targetRow - row) * 70;

        // On met la piece devant pour l'animation
        pieceImageView.toFront();

        // On cree l'animation
        TranslateTransition transition = new TranslateTransition(Duration.millis(450), pieceImageView);
        transition.setByX(distanceX);
        transition.setByY(distanceY);
        transition.setOnFinished(event -> {
            // Quand l'animation fini, on met a jour le resultat final
            movePiece(piece, targetCol, targetRow);
        });

        transition.play();
    }

    /**
     * Déplace une pièce sur le plateau.
     *
     * @param piece La pièce à déplacer.
     * @param targetCol La colonne de destination.
     * @param targetRow La ligne de destination.
     */
    private void movePiece(ChessPiece piece, int targetCol, int targetRow) {
        int row = piece.getRowIndex();
        int col = piece.getColumnIndex();

        // On sauvegarde le mouvement effectué et la piece mangé dans l'historique
        ChessPiece capturedPiece = board[targetRow][targetCol];
        Move newMove = new Move(piece, row, col, targetRow, targetCol, capturedPiece);
        moveHistory.add(newMove);

        String move = coorPiece(col, row) + " -> " + coorPiece(targetCol, targetRow);

        // On déplace la pièce
        board[row][col] = null;
        piece.setPosition(targetRow, targetCol);
        board[targetRow][targetCol] = piece;

        // On met à jour le tableau
        updateBoard();

        updateLogJeu(move);

        // On change de tour
        switchTurn();

    }

    /**
     * Efface les cases mises en évidence.
     */
    private void clearHighlightedSquares() {
        for (ImageView squareImageView : highlightedSquares) {
            resetCases(squareImageView, GridPane.getRowIndex(squareImageView), GridPane.getColumnIndex(squareImageView));
        }
        highlightedSquares.clear();
    }

    /**
     * Met en évidence les mouvements valides pour une pièce donnée.
     *
     * @param piece La pièce dont les mouvements valides doivent être mis en évidence.
     */
    private void highlightValidMoves(ChessPiece piece) {
        clearHighlightedSquares();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (piece.canMove(col, row, board)) {
                    dotSquare(row, col, piece);
                }
            }
        }
    }

    /**
     * Met en évidence les cases ou les mouvements sont possibles
     *
     * @param row ligne de la case
     * @param col colonne de la case
     * @param piece piece selectionnée
     */
    private void dotSquare(int row, int col, ChessPiece piece) {
        // Change un carré en un carré avec un point
        Node node = getNodeFromGridPane(boardGrid, row, col);
        if (node instanceof ImageView squareImageView) {
            if (board[row][col] != null && !board[row][col].getColor().equals(piece.getColor())) {
                squareImageView.setImage(redSquareImage);
            } else {
                squareImageView.setImage((col + row) % 2 == 0 ? whiteCaseDotImage : greenCaseDotImage);
            }
            highlightedSquares.add(squareImageView);
            if (!whiteTurn) squareImageView.setOnMouseClicked(this::onBoardClick);
        }
    }

    /**
     * Modifie l'apparence d'une case pour indiquer qu'elle est sélectionnée.
     *
     * @param squareImageView L'ImageView de la case sélectionnée.
     */
    private void highlightSelectedSquare(ImageView squareImageView) {
        // On met en surbriance le carré selectionné
        int col = GridPane.getColumnIndex(squareImageView);
        int row = GridPane.getRowIndex(squareImageView);
        squareImageView.setImage((row + col) % 2 == 0 ? whiteCaseClickedImage : greenCaseClickedImage);
    }


    /**
     * Réinitialise l'apparence d'une case.
     *
     * @param squareImageView L'ImageView de la case à réinitialiser.
     * @param row La ligne de la case.
     * @param col La colonne de la case.
     */
    private void resetCases(ImageView squareImageView, int row, int col) {
        squareImageView.setImage((row + col) % 2 == 0 ? whiteCaseImage : greenCaseImage);
    }

    /**
     * Change de tour entre les joueurs.
     */
    private void switchTurn() {
        // On arrete le timer du joueur en cours, et relance l'autre
        if (whiteTurn) {
            whiteTimer.stop();
            blackTimer.play();
            player1ImageView.getStyleClass().remove("player-turn");
            player2ImageView.getStyleClass().add("player-turn");
        } else {
            blackTimer.stop();
            whiteTimer.play();
            player2ImageView.getStyleClass().remove("player-turn");
            player1ImageView.getStyleClass().add("player-turn");
        }
        whiteTurn = !whiteTurn;

        if (!endGame && !whiteTurn) {
            // Si ce n'est pas le tour des blancs et que la partie n'est pas terminée,
            // c'est le tour du robot de jouer.
            robotPlay();
        }

        selectedPiece = null;
        updateTurnLabel();
    }

    /**
     * Fait jouer le robot avec un mouvement aléatoire.
     */
    private void robotPlay() {
        Random random = new Random();
        // Liste pour stocker toutes les pièces noires avec des mouvements valides
        List<ChessPiece> blackPiecesWithValidMoves = new ArrayList<>();

        // Parcourir toutes les pièces du joueur actif
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null && piece.getColor().equals("Black")) { // Si c'est une pièce noire
                    // Vérifier si la pièce a un mouvement valide
                    for (int targetRow = 0; targetRow < 8; targetRow++) {
                        for (int targetCol = 0; targetCol < 8; targetCol++) {
                            if (piece.canMove(targetCol, targetRow, board)) {
                                // Vérifier si le mouvement expose le roi
                                if (!(piece instanceof King) || !((King) piece).isCheck(board)) {
                                    // Ajouter la pièce à la liste si elle a un mouvement valide qui ne met pas en danger le roi
                                    blackPiecesWithValidMoves.add(piece);
                                }
                                break; // Sortir de la boucle interne
                            }
                        }
                    }
                }
            }
        }

        // Choisir une pièce au hasard parmi celles avec des mouvements valides
        if (!blackPiecesWithValidMoves.isEmpty()) {
            ChessPiece randomPiece = blackPiecesWithValidMoves.get(random.nextInt(blackPiecesWithValidMoves.size()));

            // Trouver un mouvement valide pour la pièce choisie
            int randomTargetRow, randomTargetCol;
            do {
                randomTargetRow = random.nextInt(8);
                randomTargetCol = random.nextInt(8);
            } while (!randomPiece.canMove(randomTargetCol, randomTargetRow, board));

            // Effectuer le mouvement de la pièce choisie
            animatePieceMove(randomPiece, randomTargetCol, randomTargetRow);
        }
    }

    /**
     * Termine la partie de jeu.
     *
     * @param message Le message à afficher lors de la fin de la partie.
     */
    private void endGame(String message) {
        String currentText = logJeu.getText();
        String newText = currentText + "\n" + message;
        if (!whiteTurn) GameController.updateStats(GameController.getFirstName(), GameController.getLastName(), true);

        logJeu.setText(newText);
        whiteTimer.stop();
        blackTimer.stop();
        endGame = true;
    }

    /**
     * Met à jour le label du tour.
     */
    private void updateTurnLabel() {
        // Mettre à jour le label de tour et vérifier l'échec ou echec et mat

        // On change de tour et on check si y a un echec/echec et mat
        if (whiteKing.isCheckmate(board)) {
            endGame("ECHEC ET MAT ! Victoire des noirs");
            return;
        }
        if (blackKing.isCheckmate(board)) {
            endGame("ECHEC ET MAT ! Victoire des blancs");
            return;
        }

        if (whiteTurn) {
            if (whiteKing.isCheck(board)) {
                highlightCheckSquare(whiteKing);
                updateLogJeu("Roi Blanc en échec !");
            }
        } else {
            if (blackKing.isCheck(board)) {
                highlightCheckSquare(blackKing);
                updateLogJeu("Roi Noir en échec !");
            }
        }
    }

    /**
     * Met en évidence la case en échec du roi.
     *
     * @param king Le roi en échec.
     */
    private void highlightCheckSquare(King king) {
        // On met en rouge le carré en dessous du roi
        int kingRow = king.getRowIndex();
        int kingCol = king.getColumnIndex();
        ImageView squareImageView = (ImageView) getNodeFromGridPane(boardGrid, kingRow, kingCol);
        if (squareImageView != null) {
            squareImageView.setImage(redSquareImage);
        }
    }

    /**
     * Récupère un noeud spécifique dans le GridPane.
     *
     * @param gridPane Le GridPane dans lequel chercher le noeud.
     * @param row La ligne du noeud.
     * @param col La colonne du noeud.
     * @return Le noeud trouvé.
     */
    private Node getNodeFromGridPane(GridPane gridPane, int row, int col) {
        // Récupérer un noeud spécifique dans le GridPane
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == row &&
                    GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == col) {
                return node;
            }
        }
        return null;
    }

    /**
     * Récupère un noeud spécifique dans le GridPane qui contient la pièce.
     *
     * @param gridPane Le GridPane dans lequel chercher le noeud.
     * @param row La ligne du noeud.
     * @param col La colonne du noeud.
     * @param piece La pièce contenue dans le noeud.
     * @return Le noeud trouvé.
     */
    private Node getNodeFromGridPane(GridPane gridPane, int row, int col, ChessPiece piece) {
        // Récupérer un noeud spécifique dans le GridPane qui contient la pièce
        for (Node node : gridPane.getChildren()) {
            if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == row &&
                    GridPane.getColumnIndex(node) != null && GridPane.getColumnIndex(node) == col &&
                    node.getUserData() == piece) {
                return node;
            }
        }
        return null;
    }

    /**
     * Gère l'événement de clic sur le bouton "Abandonner".
     *
     * @param event L'événement de clic.
     */
    private void handleSurrenderButton(ActionEvent event) {
        // Gestion de l'événement de clic sur le bouton "Abandonner"
        String winner = whiteTurn ? "noirs" : "blancs";
        endGame("Abandon, victoire des " + winner);
    }

    /**
     * Annule le dernier mouvement effectué.
     *
     * @param event L'événement de clic.
     */
    private void handleUndoButton(ActionEvent event) {
        if (!moveHistory.isEmpty()) {
            // On prend le dernier mouvement effectué
            Move lastMove = moveHistory.remove(moveHistory.size() - 1);
            ChessPiece piece = lastMove.getPiece();
            int fromRow = lastMove.getFromRow();
            int fromCol = lastMove.getFromCol();
            int toRow = lastMove.getToRow();
            int toCol = lastMove.getToCol();
            ChessPiece capturedPiece = lastMove.getCapturedPiece();

            // On bouge la piece à sa position d'origine
            movePiece(piece, fromCol, fromRow);

            // Si une pièce a été capturée, on la remet dans sa position d'origine
            if (capturedPiece != null) {
                board[toRow][toCol] = capturedPiece;
                updateBoard(); // Update the board to reflect the replaced piece
            }
        }
    }

    /**
     * Met à jour l'historique des messages.
     *
     * @param move Le mouvement à ajouter à l'historique.
     */
    public void updateLogJeu(String move) {
        // On met a jour l'historique des messages
        String currentText = logJeu.getText();
        String playerColor = whiteTurn ? "Vous :" : "Adversaire :";
        String newText = currentText + "\n" + playerColor + " " + move;

        logJeu.setText(newText);
    }

    /**
     * Convertit les coordonnées en notation d'échecs.
     *
     * @param col La colonne.
     * @param row La ligne.
     * @return La notation d'échecs des coordonnées.
     */
    private String coorPiece(int col, int row) {
        char colLabel = (char) ('a' + col);
        int rowLabel = 8 - row;
        return colLabel + Integer.toString(rowLabel);
    }

    /**
     * Sauvegarde l'état du jeu dans un fichier CSV.
     *
     * @param event L'événement de clic.
     */
    private void handleSaveButton(ActionEvent event)  {
        GameState gameState = new GameState(board, moveHistory, whiteTurn, whiteSecondsRemaining, blackSecondsRemaining);

        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showSaveDialog(saveGameButton.getScene().getWindow());

        if (file != null) {
            try {
                gameState.saveToFile(file.getAbsolutePath());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Restaure l'état du jeu à partir d'un fichier CSV.
     *
     * @param gameStatus Le fichier CSV contenant l'état du jeu.
     */
    private void restoreGameState(File gameStatus) {
        try (BufferedReader reader = new BufferedReader(new FileReader(gameStatus))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String pieceType = parts[0];
                    String pieceColor = parts[1];
                    int row = Integer.parseInt(parts[2]);
                    int col = Integer.parseInt(parts[3]);

                    if (pieceType.equals("Pawn")) {
                        board[row][col] = new Pawn(pieceColor, col, row);
                    } else {
                        switch (pieceType) {
                            case "Rook":
                                board[row][col] = new Rook(pieceColor, col, row);
                                break;
                            case "Knight":
                                board[row][col] = new Knight(pieceColor, col, row);
                                break;
                            case "Bishop":
                                board[row][col] = new Bishop(pieceColor, col, row);
                                break;
                            case "Queen":
                                board[row][col] = new Queen(pieceColor, col, row);
                                break;
                            case "King":
                                board[row][col] = new King(pieceColor, col, row);
                                if (pieceColor.equals("Black")) {
                                    blackKing = (King) board[row][col];
                                } else {
                                    whiteKing = (King) board[row][col];
                                }
                                break;
                            default:
                                break;
                        }
                    }
                } else if (parts.length == 2) {
                    switch (parts[0]) {
                        case "WhiteTurn":
                            whiteTurn = Boolean.parseBoolean(parts[1]);
                            break;
                        case "WhiteTimeRemaining":
                            whiteSecondsRemaining = Integer.parseInt(parts[1]);
                            break;
                        case "BlackTimeRemaining":
                            blackSecondsRemaining = Integer.parseInt(parts[1]);
                            break;
                        default:
                            break;
                    }
                }
            }
            updateBoard();
            initializeTimers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
