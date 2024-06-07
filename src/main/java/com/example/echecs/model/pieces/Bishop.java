package com.example.echecs.model.pieces;

public class Bishop extends ChessPiece {
    public Bishop(String color, int columnIndex, int rowIndex) {
        super(color, columnIndex, rowIndex);
    }

    public ChessPiece copy() {
        return new Pawn(this.getColor(), this.getColumnIndex(), this.getRowIndex());
    }

    @Override
    public boolean canMove(int targetCol, int targetRow, ChessPiece[][] board) {
        // Calcul des différences de colonne et de ligne entre la position cible et la position actuelle du fou
        int columnDifference = Math.abs(targetCol - columnIndex);
        int rowDifference = Math.abs(targetRow - rowIndex);
        int columnDirection;
        int rowDirection;

        // Vérifier si le mouvement est diagonal
        if (columnDifference != rowDifference) {
            return false;
        }

        // Calcul de la direction de la colonne et de la ligne du mouvement
        if (columnDifference != 0) {
            columnDirection = (targetCol - columnIndex) / columnDifference;
        }
        else columnDirection = 0;

        if (rowDifference != 0) {
            rowDirection = (targetRow - rowIndex) / rowDifference;
        }
        else rowDirection = 0;

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
