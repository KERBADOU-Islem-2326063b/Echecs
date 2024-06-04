package com.example.echecs.model;

public class Pawn extends ChessPiece {
    public Pawn(String color, int columnIndex, int rowIndex) {
        super(color, columnIndex, rowIndex);
    }

    @Override
    public boolean canMove(int targetCol, int targetRow, ChessPiece[][] board) {
        int direction = color.equals("White") ? -1 : 1;
        int startRow = color.equals("White") ? 6 : 1;

        if (targetCol == columnIndex && board[targetRow][targetCol] == null) {
            if (targetRow == rowIndex + direction) {
                return true;
            }
            if (rowIndex == startRow && targetRow == rowIndex + 2 * direction) {
                return board[rowIndex + direction][columnIndex] == null;
            }
        }

        if (Math.abs(targetCol - columnIndex) == 1 && targetRow == rowIndex + direction) {
            return board[targetRow][targetCol] != null && !board[targetRow][targetCol].getColor().equals(color);
        }

        return false;
    }
}
