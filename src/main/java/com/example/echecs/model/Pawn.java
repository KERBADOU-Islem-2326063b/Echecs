package com.example.echecs.model;

public class Pawn extends ChessPiece {
    // Constructeur pour initialiser la couleur et la position du pion
    public Pawn(String color, int columnIndex, int rowIndex) {
        super(color, columnIndex, rowIndex);
    }

    @Override
    public boolean canMove(int targetCol, int targetRow, ChessPiece[][] board) {
        // Déterminer la direction du mouvement en fonction de la couleur du pion
        int direction = color.equals("White") ? -1 : 1;
        // Ligne de départ du pion en fonction de sa couleur
        int startRow = color.equals("White") ? 6 : 1;

        // Vérifier si le mouvement est un déplacement d'une case vers l'avant
        if (targetCol == columnIndex && board[targetRow][targetCol] == null) {
            if (targetRow == rowIndex + direction) {
                return true; // Le pion peut se déplacer d'une case vers l'avant s'il n'y a pas de pièce dans la case cible
            }
            if (rowIndex == startRow && targetRow == rowIndex + 2 * direction) {
                return board[rowIndex + direction][columnIndex] == null; // Le pion peut se déplacer de deux cases vers l'avant depuis sa position de départ s'il n'y a pas de pièce dans les cases intermédiaires
            }
        }

        // Vérifier si le mouvement est une capture en diagonale
        if (Math.abs(targetCol - columnIndex) == 1 && targetRow == rowIndex + direction) {
            return board[targetRow][targetCol] != null && !board[targetRow][targetCol].getColor().equals(color); // Le pion peut capturer une pièce adverse en diagonale
        }

        // Si le mouvement n'est pas valide, retourner false
        return false;
    }
}
