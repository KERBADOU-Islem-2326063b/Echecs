package com.example.echecs.model.gameStatus;

import com.example.echecs.model.gameStatus.Move;
import com.example.echecs.model.pieces.ChessPiece;

import java.io.IOException;
import java.util.List;
import java.io.FileWriter;

public class GameState {
    private ChessPiece[][] board;
    private List<Move> moveHistory;
    private boolean whiteTurn;
    private int whiteSecondsRemaining;
    private int blackSecondsRemaining;

    public GameState(ChessPiece[][] board, List<Move> moveHistory, boolean whiteTurn, int whiteSecondsRemaining, int blackSecondsRemaining) {
        this.board = board;
        this.moveHistory = moveHistory;
        this.whiteTurn = whiteTurn;
        this.whiteSecondsRemaining = whiteSecondsRemaining;
        this.blackSecondsRemaining = blackSecondsRemaining;
    }

    public String toCSV() {
        StringBuilder csvBuilder = new StringBuilder();

        // On met l'etat des pieces
        csvBuilder.append("Board State:\n");
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null) {
                    csvBuilder.append(piece.getClass().getSimpleName()).append(",")
                            .append(piece.getColor()).append(",")
                            .append(row).append(",")
                            .append(col).append("\n");
                }
            }
        }

        // On ajoute l'historique de la partie
        csvBuilder.append("\nMove History:\n");
        for (Move move : moveHistory) {
            csvBuilder.append(move.getPiece().getClass().getSimpleName()).append(",")
                    .append(move.getFromRow()).append(",")
                    .append(move.getFromCol()).append(",")
                    .append(move.getToRow()).append(",")
                    .append(move.getToCol()).append(",")
                    .append(move.getCapturedPiece() != null ? move.getCapturedPiece().getClass().getSimpleName() : "None").append("\n");
        }

        // On ajoute d'autre details du jeu
        csvBuilder.append("\nGame Details:\n");
        csvBuilder.append("WhiteTurn,").append(whiteTurn).append("\n");
        csvBuilder.append("WhiteTimeRemaining,").append(whiteSecondsRemaining).append("\n");
        csvBuilder.append("BlackTimeRemaining,").append(blackSecondsRemaining).append("\n");

        return csvBuilder.toString();
    }

    public void saveToFile(String filePath) throws IOException {
        FileWriter writer = new FileWriter(filePath);
        writer.write(toCSV());
        writer.close();
    }
}