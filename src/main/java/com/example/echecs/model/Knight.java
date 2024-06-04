package com.example.echecs.model;

public class Knight extends ChessPiece {
    public Knight(String color, int columnIndex, int rowIndex) {
        super(color, columnIndex, rowIndex);
    }

    @Override
    public boolean canMove(int targetCol, int targetRow, ChessPiece[][] board) {
        int columnDifference = Math.abs(targetCol - columnIndex);
        int rowDifference = Math.abs(targetRow - rowIndex);

        if ((columnDifference == 2 && rowDifference == 1) || (columnDifference == 1 && rowDifference == 2)) {
            ChessPiece targetPiece = board[targetRow][targetCol];
            return targetPiece == null || !targetPiece.getColor().equals(this.color);
        }

        return false;
    }
}
