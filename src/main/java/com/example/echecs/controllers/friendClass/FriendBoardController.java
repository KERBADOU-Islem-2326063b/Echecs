package com.example.echecs.controllers.friendClass;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
public class FriendBoardController {
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
    private Timeline whiteTimer;
    private Timeline blackTimer;
    int whiteSecondsRemaining = GameController.getInitialTimeInSeconds(); // On récupère le temps défini avant
    int blackSecondsRemaining = GameController.getInitialTimeInSeconds();

    private King blackKing;
    private King whiteKing;

    private Image whiteCaseImage, greenCaseImage, whiteCaseClickedImage, greenCaseClickedImage, whiteCaseDotImage, greenCaseDotImage, redSquareImage;
    @FXML
    public void initialize() {
        initializeTimers(); // Initilisation du temps
        initializeImagesLabels(); // Initialisation des images
        initializeBoard(); // Initialisation du plateau de jeu
        player1ImageView.getStyleClass().add("player-turn");
        updateBoard(); // Mise à jour de l'affichage du plateau

        // On initialise les boutons
        undoMovement.setOnAction(this::handleUndoButton);
        surrenderButton.setOnAction(this::handleSurrenderButton);
        saveGameButton.setOnAction(this::handleSaveButton);
    }


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

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int remainingSeconds = seconds % 60;
        return String.format("%02d:%02d", minutes, remainingSeconds);
    }

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

    private ImageView createSquareImageView(int row, int col) {
        // Création d'une ImageView pour une case du plateau
        ImageView squareImageView = new ImageView();
        squareImageView.setFitWidth(72);
        squareImageView.setFitHeight(72);
        squareImageView.setOnMouseClicked(this::onBoardClick);
        resetCases(squareImageView, row, col); // Réinitialisation de l'apparence de la case
        return squareImageView;
    }

    private ImageView createPieceImageView(ChessPiece piece) {
        // Création d'une ImageView pour une pièce d'échecs
        ImageView pieceImageView = new ImageView(new Image(piece.getImagePath()));
        pieceImageView.setUserData(piece);
        pieceImageView.setOnMouseClicked(this::onBoardClick);
        pieceImageView.setFitWidth(70);
        pieceImageView.setFitHeight(70);
        return pieceImageView;
    }

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

    private void clearHighlightedSquares() {
        // Effacer les cases mises en évidence
        for (ImageView squareImageView : highlightedSquares) {
            resetCases(squareImageView, GridPane.getRowIndex(squareImageView), GridPane.getColumnIndex(squareImageView));
        }
        highlightedSquares.clear();
    }

    private void highlightValidMoves(ChessPiece piece) {
        clearHighlightedSquares();
        // Mettre en évidence des mouvements valides pour une pièce donnée
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                if (piece.canMove(col, row, board)) {
                    dotSquare(row, col, piece);
                }
            }
        }
    }

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
            squareImageView.setOnMouseClicked(this::onBoardClick);
        }
    }

    private void highlightSelectedSquare(ImageView squareImageView) {
        // On met en surbriance le carré selectionné
        int col = GridPane.getColumnIndex(squareImageView);
        int row = GridPane.getRowIndex(squareImageView);
        squareImageView.setImage((row + col) % 2 == 0 ? whiteCaseClickedImage : greenCaseClickedImage);
    }

    private void resetCases(ImageView squareImageView, int row, int col) {
        // On remet l'image d'origine a la case selectionée
        squareImageView.setImage((row + col) % 2 == 0 ? whiteCaseImage : greenCaseImage);
    }

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
        selectedPiece = null;
        updateTurnLabel();
    }

    private void endGame(String message) {
        // On met fin à la partie
        String currentText = logJeu.getText();
        String newText = currentText + "\n" + message;

        logJeu.setText(newText);
        whiteTimer.stop();
        blackTimer.stop();
        endGame = true;
    }

    private void updateTurnLabel() {
        // Mettre à jour le label de tour et vérifier l'échec ou echec et mat

        // On change de tour et on check si y a un echec/echec et mat
        if (whiteKing.isCheckmate(board)) {
            System.out.println("Blanc echec et mat");
            endGame("ECHEC ET MAT ! Victoire des noirs");
            return;
        }
        if (blackKing.isCheckmate(board)) {
            System.out.println("Noir echec et mat");
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

    private void highlightCheckSquare(King king) {
        // On met en rouge le carré en dessous du roi
        int kingRow = king.getRowIndex();
        int kingCol = king.getColumnIndex();
        ImageView squareImageView = (ImageView) getNodeFromGridPane(boardGrid, kingRow, kingCol);
        if (squareImageView != null) {
            squareImageView.setImage(redSquareImage);
        }
    }

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

    private void handleSurrenderButton(ActionEvent event) {
        // Gestion de l'événement de clic sur le bouton "Abandonner"
        String winner = whiteTurn ? "noirs" : "blancs";
        endGame("Abandon, victoire des " + winner);
    }

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

    public void updateLogJeu(String move) {
        // On met a jour l'historique des messages
        String currentText = logJeu.getText();
        String playerColor = whiteTurn ? "Vous :" : "Adversaire :";
        String newText = currentText + "\n" + playerColor + " " + move;

        logJeu.setText(newText);
    }

    private String coorPiece(int col, int row) {
        // On traduit en coordonne echec les colonnes et lignes
        char colLabel = (char) ('a' + col);
        int rowLabel = 8 - row;
        return colLabel + Integer.toString(rowLabel);
    }


    private void handleSaveButton(ActionEvent event)  {
        // On sauvegarde dans un fichier CSV l'etat du jeu d'Echec
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
}
