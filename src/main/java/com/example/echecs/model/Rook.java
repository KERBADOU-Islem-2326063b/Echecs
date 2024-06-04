package com.example.echecs.model;

public class Rook extends ChessPiece {
    public Rook(String color, int columnIndex, int rowIndex) {
        super(color, columnIndex, rowIndex);
    }

    @Override
    public boolean canMove(int targetCol, int targetRow, ChessPiece[][] board) {
        if (targetCol == columnIndex || targetRow == rowIndex) {
            int columnDirection = (targetCol - columnIndex) == 0 ? 0 : (targetCol - columnIndex) / Math.abs(targetCol - columnIndex);
            int rowDirection = (targetRow - rowIndex) == 0 ? 0 : (targetRow - rowIndex) / Math.abs(targetRow - rowIndex);

            for (int i = 1; i < Math.max(Math.abs(targetCol - columnIndex), Math.abs(targetRow - rowIndex)); i++) {
                int currentCol = columnIndex + i * columnDirection;
                int currentRow = rowIndex + i * rowDirection;
                if (board[currentRow][currentCol] != null) {
                    return false;
                }
            }

            ChessPiece targetPiece = board[targetRow][targetCol];
            return targetPiece == null || !targetPiece.getColor().equals(this.color);
        }

        return false;
    }
}