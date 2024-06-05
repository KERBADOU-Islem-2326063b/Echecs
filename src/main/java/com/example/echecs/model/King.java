package com.example.echecs.model;

import java.util.ArrayList;
import java.util.List;

public class King extends ChessPiece {
    // Constructeur pour initialiser la couleur et la position du roi
    public King(String color, int columnIndex, int rowIndex) {
        super(color, columnIndex, rowIndex);
    }

    @Override
    public boolean canMove(int targetCol, int targetRow, ChessPiece[][] board) {
        // Calculer la différence entre la position actuelle et la position cible
        int columnDifference = Math.abs(targetCol - columnIndex);
        int rowDifference = Math.abs(targetRow - rowIndex);

        // Vérifier si le mouvement est d'une case dans n'importe quelle direction
        if (columnDifference <= 1 && rowDifference <= 1) {
            ChessPiece targetPiece = board[targetRow][targetCol];
            // Vérifier si la case cible est vide ou contient une pièce adverse
            if (targetPiece == null || !targetPiece.getColor().equals(this.color)) {
                // Vérifier si la case cible n'est pas attaquée
                return !isUnderAttack(targetCol, targetRow, board);
            }
        }
        return false;
    }

    // Vérifie si le roi est en échec
    public boolean isCheck(ChessPiece[][] board) {
        if (isUnderAttack(this.columnIndex, this.rowIndex, board)) {
            // Vérifier si le roi peut se déplacer vers une case sûre
            if (hasSafeMove(board)) return false;
            // Vérifier si une pièce alliée peut capturer la pièce menaçante
            if (canCaptureThreat(board)) return false;
            // Si aucune des conditions précédentes n'est remplie, le roi est en échec
            return true;
        }
        return false;
    }

    // Vérifie si le roi a un mouvement sûr disponible
    private boolean hasSafeMove(ChessPiece[][] board) {
        for (int row = -1; row <= 1; row++) {
            for (int col = -1; col <= 1; col++) {
                int newRow = this.rowIndex + row;
                int newCol = this.columnIndex + col;
                if (isInBounds(newRow, newCol) && canMove(newCol, newRow, board)) {
                    return true;  // Mouvement sûr trouvé
                }
            }
        }
        return false;  // Aucun mouvement sûr disponible
    }

    // Vérifie si une pièce alliée peut capturer une pièce menaçante
    private boolean canCaptureThreat(ChessPiece[][] board) {
        List<ChessPiece> threateningPieces = getThreateningPieces(board);

        for (ChessPiece threateningPiece : threateningPieces) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    ChessPiece alliedPiece = board[row][col];
                    if (alliedPiece != null && alliedPiece.getColor().equals(this.color)
                            && alliedPiece.canMove(threateningPiece.getColumnIndex(), threateningPiece.getRowIndex(), board)) {
                        return true;  // Une pièce alliée peut capturer la pièce menaçante
                    }
                }
            }
        }
        return false;  // Aucune pièce alliée ne peut capturer les pièces menaçantes
    }

    // Obtient les pièces menaçantes pour le roi
    private List<ChessPiece> getThreateningPieces(ChessPiece[][] board) {
        List<ChessPiece> threateningPieces = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null && !piece.getColor().equals(this.color) && piece.canMove(this.columnIndex, this.rowIndex, board)) {
                    threateningPieces.add(piece);
                }
            }
        }
        return threateningPieces;
    }

    // Vérifie si une position est attaquée par une pièce adverse
    private boolean isUnderAttack(int targetCol, int targetRow, ChessPiece[][] board) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null && !piece.getColor().equals(this.color) && piece.canMove(targetCol, targetRow, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Vérifie si les coordonnées sont dans les limites du plateau
    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
}
