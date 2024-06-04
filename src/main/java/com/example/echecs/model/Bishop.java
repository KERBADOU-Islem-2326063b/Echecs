package com.example.echecs.model;

public class Bishop extends ChessPiece {
    public Bishop(String color, int columnIndex, int rowIndex) {
        super(color, columnIndex, rowIndex);
    }

    @Override
    public boolean canMove(int targetCol, int targetRow, ChessPiece[][] board) {
        int columnDifference = Math.abs(targetCol - columnIndex);
        int rowDifference = Math.abs(targetRow - rowIndex);

        if (columnDifference != rowDifference) {
            return false;
        }

        int columnDirection = (targetCol - columnIndex) / columnDifference;
        int rowDirection = (targetRow - rowIndex) / rowDifference;

        for (int i = 1; i < columnDifference; i++) {
            if (board[rowIndex + i * rowDirection][columnIndex + i * columnDirection] != null) {
                return false;
            }
        }

        return board[targetRow][targetCol] == null || !board[targetRow][targetCol].getColor().equals(color);
    }
}
