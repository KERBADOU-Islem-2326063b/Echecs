package com.example.echecs.model;

public class Rook extends ChessPiece {
    // Constructeur pour initialiser la couleur et la position de la tour
    public Rook(String color, int columnIndex, int rowIndex) {
        super(color, columnIndex, rowIndex);
    }

    @Override
    public boolean canMove(int targetCol, int targetRow, ChessPiece[][] board) {
        // Vérifier si le mouvement est horizontal ou vertical
        if (targetCol == columnIndex || targetRow == rowIndex) {
            // Calcul de la direction de la colonne et de la ligne du mouvement
            int columnDirection = (targetCol - columnIndex) == 0 ? 0 : (targetCol - columnIndex) / Math.abs(targetCol - columnIndex);
            int rowDirection = (targetRow - rowIndex) == 0 ? 0 : (targetRow - rowIndex) / Math.abs(targetRow - rowIndex);

            // Vérification des cases intermédiaires pour s'assurer qu'il n'y a pas d'obstacles sur la trajectoire
            for (int i = 1; i < Math.max(Math.abs(targetCol - columnIndex), Math.abs(targetRow - rowIndex)); i++) {
                int currentCol = columnIndex + i * columnDirection;
                int currentRow = rowIndex + i * rowDirection;
                if (board[currentRow][currentCol] != null) {
                    return false; // Si une pièce est rencontrée sur le chemin, le mouvement est invalide
                }
            }

            // Récupérer la pièce cible
            ChessPiece targetPiece = board[targetRow][targetCol];

            // Vérifier si la case cible est vide ou contient une pièce adverse
            return targetPiece == null || !targetPiece.getColor().equals(this.color);
        }

        // Si le mouvement n'est pas valide, retourner false
        return false;
    }
}
