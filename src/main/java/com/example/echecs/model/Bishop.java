package com.example.echecs.model;

public class Bishop extends ChessPiece {
    public Bishop(String color, int columnIndex, int rowIndex) {
        super(color, columnIndex, rowIndex);
    }

    @Override
    public boolean canMove(int targetCol, int targetRow, ChessPiece[][] board) {
        // Calcul des différences de colonne et de ligne entre la position cible et la position actuelle du fou
        int columnDifference = Math.abs(targetCol - columnIndex);
        int rowDifference = Math.abs(targetRow - rowIndex);

        // Vérifier si le mouvement est diagonal
        if (columnDifference != rowDifference) {
            return false;
        }

        // Calcul de la direction de la colonne et de la ligne du mouvement
        int columnDirection = (targetCol - columnIndex) / columnDifference;
        int rowDirection = (targetRow - rowIndex) / rowDifference;

        // Vérification des cases intermédiaires pour s'assurer qu'il n'y a pas d'obstacles sur la trajectoire diagonale
        for (int i = 1; i < columnDifference; i++) {
            if (board[rowIndex + i * rowDirection][columnIndex + i * columnDirection] != null) {
                return false;
            }
        }

        // Vérifier si la case cible est vide ou contient une pièce adverse
        return board[targetRow][targetCol] == null || !board[targetRow][targetCol].getColor().equals(color);
    }
}
