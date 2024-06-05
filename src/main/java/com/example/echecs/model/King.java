package com.example.echecs.model;

import java.util.ArrayList;
import java.util.List;

import java.util.ArrayList;
import java.util.List;

public class King extends ChessPiece {
    public King(String color, int columnIndex, int rowIndex) {
        super(color, columnIndex, rowIndex);
    }

    // Méthode pour vérifier si le roi peut se déplacer vers une case spécifique
    @Override
    public boolean canMove(int targetCol, int targetRow, ChessPiece[][] board) {
        int columnDifference = Math.abs(targetCol - columnIndex);
        int rowDifference = Math.abs(targetRow - rowIndex);

        if (columnDifference <= 1 && rowDifference <= 1) {
            ChessPiece targetPiece = board[targetRow][targetCol];
            if (targetPiece == null || !targetPiece.getColor().equals(this.color)) {
                return !isUnderAttack(targetCol, targetRow, board);
            }
        }
        return false;
    }

    // Méthode pour vérifier si le roi est en échec
    public boolean isCheck(ChessPiece[][] board) {
        return isUnderAttack(this.columnIndex, this.rowIndex, board);
    }

    // Méthode pour vérifier si le roi est en échec et mat
    public boolean isCheckmate(ChessPiece[][] board) {
        if (!isCheck(board)) return false;

        if (hasSafeMove(board)) return false;

        if (canPawnProtectKing(board)) {
            return false;
        }

        return !canCaptureThreat(board);
    }

    // Méthode pour vérifier s'il existe un mouvement sûr pour le roi
    private boolean hasSafeMove(ChessPiece[][] board) {
        for (int row = -1; row <= 1; row++) {
            for (int col = -1; col <= 1; col++) {
                int newRow = this.rowIndex + row;
                int newCol = this.columnIndex + col;
                if (isInBounds(newRow, newCol) && canMove(newCol, newRow, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Méthode pour vérifier si un pion peut protéger le roi
    private boolean canPawnProtectKing(ChessPiece[][] board) {
        int direction = this.color.equals("White") ? -1 : 1; // Direction du mouvement du pion en fonction de la couleur

        // Vérifier tous les pions sur le plateau
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece pawn = board[row][col];
                if (pawn != null && pawn instanceof Pawn && pawn.getColor().equals(this.color)) {
                    // Vérifier si le pion peut se déplacer pour protéger le roi
                    int targetRow = this.rowIndex + direction * 2; // La ligne où le pion devrait se déplacer
                    int[] pawnColumns = {this.columnIndex - 1, this.columnIndex + 1}; // Colonnes à vérifier pour les pions adjacents
                    for (int pawnCol : pawnColumns) {
                        if (isInBounds(targetRow, pawnCol) && board[targetRow][pawnCol] == null) {
                            // Vérifier si le pion peut se déplacer à la position cible sans être capturé
                            ChessPiece pieceAtTargetPosition = board[targetRow][pawnCol];
                            if (pieceAtTargetPosition == null || !pieceAtTargetPosition.canMove(pawnCol, targetRow, board)) {
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    // Méthode pour vérifier si une menace peut être capturée
    private boolean canCaptureThreat(ChessPiece[][] board) {
        List<ChessPiece> threateningPieces = getThreateningPieces(board);

        for (ChessPiece threateningPiece : threateningPieces) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    ChessPiece alliedPiece = board[row][col];
                    if (alliedPiece != null && alliedPiece.getColor().equals(this.color)
                            && alliedPiece.canMove(threateningPiece.getColumnIndex(), threateningPiece.getRowIndex(), board)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Méthode pour obtenir les pièces menaçantes
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

    // Méthode pour vérifier si une case est attaquée
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

    // Méthode pour vérifier si une case est dans les limites du plateau
    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
}

