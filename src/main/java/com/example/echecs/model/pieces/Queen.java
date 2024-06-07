package com.example.echecs.model.pieces;

public class Queen extends ChessPiece {
    // Constructeur pour initialiser la couleur et la position de la reine
    public Queen(String color, int columnIndex, int rowIndex) {
        super(color, columnIndex, rowIndex);
    }

    public ChessPiece copy() {
        return new Pawn(this.getColor(), this.getColumnIndex(), this.getRowIndex());
    }

    /**
     *
     * @param targetCol
     * @param targetRow
     * @param board
     * @return
     */
    @Override
    public boolean canMove(int targetCol, int targetRow, ChessPiece[][] board) {
        // Calcul des différences de colonne et de ligne entre la position cible et la position actuelle de la reine
        int columnDifference = Math.abs(targetCol - columnIndex);
        int rowDifference = Math.abs(targetRow - rowIndex);

        // Vérifier si le mouvement est diagonal, horizontal ou vertical
        if (columnDifference == rowDifference || targetCol == columnIndex || targetRow == rowIndex) {
            // Calcul de la direction de la colonne et de la ligne du mouvement
            int columnDirection = (targetCol - columnIndex) == 0 ? 0 : (targetCol - columnIndex) / columnDifference;
            int rowDirection = (targetRow - rowIndex) == 0 ? 0 : (targetRow - rowIndex) / rowDifference;

            // Vérification des cases intermédiaires pour s'assurer qu'il n'y a pas d'obstacles sur la trajectoire
            for (int i = 1; i < Math.max(columnDifference, rowDifference); i++) {
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
