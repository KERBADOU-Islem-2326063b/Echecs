package com.example.echecs.model;

public class Knight extends ChessPiece {
    // Constructeur pour initialiser la couleur et la position du cavalier
    public Knight(String color, int columnIndex, int rowIndex) {
        super(color, columnIndex, rowIndex);
    }

    public ChessPiece copy() {
        return new Pawn(this.getColor(), this.getColumnIndex(), this.getRowIndex());
    }


    @Override
    public boolean canMove(int targetCol, int targetRow, ChessPiece[][] board) {
        // Calcul des différences de colonne et de ligne entre la position cible et la position actuelle du cavalier
        int columnDifference = Math.abs(targetCol - columnIndex);
        int rowDifference = Math.abs(targetRow - rowIndex);

        // Vérifier si le mouvement correspond aux mouvements possibles d'un cavalier
        if ((columnDifference == 2 && rowDifference == 1) || (columnDifference == 1 && rowDifference == 2)) {
            // Récupérer la pièce cible
            ChessPiece targetPiece = board[targetRow][targetCol];
            // Vérifier si la case cible est vide ou contient une pièce adverse
            return targetPiece == null || !targetPiece.getColor().equals(this.color);
        }

        // Si le mouvement n'est pas valide, retourner false
        return false;
    }
}
