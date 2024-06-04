package com.example.echecs.model;

public class King extends ChessPiece {
    // Constructeur pour initialiser la couleur et la position du roi
    public King(String color, int columnIndex, int rowIndex) {
        super(color, columnIndex, rowIndex);
    }

    @Override
    public boolean canMove(int targetCol, int targetRow, ChessPiece[][] board) {
        // Calcul des différences de colonne et de ligne entre la position cible et la position actuelle du roi
        int columnDifference = Math.abs(targetCol - columnIndex);
        int rowDifference = Math.abs(targetRow - rowIndex);

        // Vérifier si la différence de colonne et de ligne est d'au plus 1, ce qui signifie un mouvement valide du roi
        if (columnDifference <= 1 && rowDifference <= 1) {
            // Récupérer la pièce cible
            ChessPiece targetPiece = board[targetRow][targetCol];
            // Vérifier si la case cible est vide ou contient une pièce adverse
            return targetPiece == null || !targetPiece.getColor().equals(this.color);
        }

        // Si le mouvement n'est pas valide, retourner false
        return false;
    }
}
