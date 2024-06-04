package com.example.echecs.model;

public class Queen extends ChessPiece {
    public Queen(String color, int columnIndex, int rowIndex) {
        super(color, columnIndex, rowIndex);
    }

    @Override
    public boolean canMove(int targetCol, int targetRow, ChessPiece[][] board) {
        int columnDifference = Math.abs(targetCol - columnIndex);
        int rowDifference = Math.abs(targetRow - rowIndex);

        if (columnDifference == rowDifference || targetCol == columnIndex || targetRow == rowIndex) {
            int columnDirection = (targetCol - columnIndex) == 0 ? 0 : (targetCol - columnIndex) / columnDifference;
            int rowDirection = (targetRow - rowIndex) == 0 ? 0 : (targetRow - rowIndex) / rowDifference;

            for (int i = 1; i < Math.max(columnDifference, rowDifference); i++) {
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