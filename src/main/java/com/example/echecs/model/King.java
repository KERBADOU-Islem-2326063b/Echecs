package com.example.echecs.model;

import java.util.ArrayList;
import java.util.List;

public class King extends ChessPiece {
    public King(String color, int columnIndex, int rowIndex) {
        super(color, columnIndex, rowIndex);
    }

    public ChessPiece copy() {
        return new Pawn(this.getColor(), this.getColumnIndex(), this.getRowIndex());
    }

    @Override
    public boolean canMove(int targetCol, int targetRow, ChessPiece[][] board) {
        int columnDifference = Math.abs(targetCol - columnIndex);
        int rowDifference = Math.abs(targetRow - rowIndex);

        if (columnDifference <= 1 && rowDifference <= 1) {
            ChessPiece targetPiece = board[targetRow][targetCol];
            if (targetPiece == null || !targetPiece.getColor().equals(this.color)) {
                ChessPiece originalPiece = board[targetRow][targetCol];
                board[targetRow][targetCol] = this;
                board[this.rowIndex][this.columnIndex] = null;

                boolean isSafeMove = !isUnderAttack(targetCol, targetRow, board);

                board[this.rowIndex][this.columnIndex] = this;
                board[targetRow][targetCol] = originalPiece;

                return isSafeMove;
            }
        }
        return false;
    }
    public boolean isCheck(ChessPiece[][] board) {
        return isUnderAttack(this.columnIndex, this.rowIndex, board);
    }

    public boolean isCheckmate(ChessPiece[][] board) {
        if (!isCheck(board)) {
            return false;
        }

        if (hasSafeMove(board)) {
            return false;
        }

        if (canBlockOrCapture(board)) {
            return false;
        }

        return true;
    }

    private boolean hasSafeMove(ChessPiece[][] board) {
        for (int row = -1; row <= 1; row++) {
            for (int col = -1; col <= 1; col++) {
                int newRow = this.rowIndex + row;
                int newCol = this.columnIndex + col;
                if (isInBounds(newRow, newCol) && canMove(newCol, newRow, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean canBlockOrCapture(ChessPiece[][] board) {
        List<ChessPiece> threateningPieces = getThreateningPieces(board);

        for (ChessPiece threateningPiece : threateningPieces) {
            List<ChessPiece> alliedPieces = getAlliedPieces(this.color, board);

            for (ChessPiece alliedPiece : alliedPieces) {
                if (canCapture(threateningPiece, alliedPiece, board)) {
                    return true;
                }
            }

            if (canBlock(threateningPiece, alliedPieces, board)) {
                return true;
            }
        }
        return false;
    }

    private boolean canBlock(ChessPiece threateningPiece, List<ChessPiece> alliedPieces, ChessPiece[][] board) {
        List<int[]> path = getPath(threateningPiece);

        for (int[] position : path) {
            int row = position[0];
            int col = position[1];

            for (ChessPiece alliedPiece : alliedPieces) {
                if (alliedPiece.canMove(col, row, board) && !isUnderAttack(col, row, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<int[]> getPath(ChessPiece threateningPiece) {
        List<int[]> path = new ArrayList<>();
        int dRow = Integer.compare(threateningPiece.getRowIndex(), this.rowIndex);
        int dCol = Integer.compare(threateningPiece.getColumnIndex(), this.columnIndex);

        int row = this.rowIndex + dRow;
        int col = this.columnIndex + dCol;

        while (row != threateningPiece.getRowIndex() || col != threateningPiece.getColumnIndex()) {
            path.add(new int[]{row, col});
            row += dRow;
            col += dCol;
        }

        return path;
    }

    private List<ChessPiece> getAlliedPieces(String color, ChessPiece[][] board) {
        List<ChessPiece> alliedPieces = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null && piece.getColor().equals(color)) {
                    alliedPieces.add(piece);
                }
            }
        }
        return alliedPieces;
    }

    private boolean canCapture(ChessPiece threateningPiece, ChessPiece alliedPiece, ChessPiece[][] board) {
        return alliedPiece.canMove(threateningPiece.getColumnIndex(), threateningPiece.getRowIndex(), board);
    }

    private List<ChessPiece> getThreateningPieces(ChessPiece[][] board) {
        List<ChessPiece> threateningPieces = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null && !piece.getColor().equals(this.color) && piece.canMove(this.columnIndex, this.rowIndex, board)) {
                    threateningPieces.add(piece);
                }
            }
        }
        return threateningPieces;
    }

    private boolean isUnderAttack(int targetCol, int targetRow, ChessPiece[][] board) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                ChessPiece piece = board[row][col];
                if (piece != null && !piece.getColor().equals(this.color) && piece.canMove(targetCol, targetRow, board)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8;
    }
}
