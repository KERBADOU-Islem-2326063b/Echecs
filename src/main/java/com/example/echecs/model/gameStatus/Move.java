package com.example.echecs.model.gameStatus;

import com.example.echecs.model.pieces.ChessPiece;

public class Move {
    private ChessPiece piece;
    private int fromRow;
    private int fromCol;;
    private int toRow;
    private int toCol;
    private ChessPiece capturedPiece;

    public Move(ChessPiece piece, int fromRow, int fromCol, int toRow, int toCol, ChessPiece capturedPiece) {
        this.piece = piece;
        this.fromRow = fromRow;
        this.fromCol = fromCol;
        this.toRow = toRow;
        this.toCol = toCol;
        this.capturedPiece = capturedPiece;
    }

    public ChessPiece getPiece() {
        return piece;
    }

    public int getFromRow() {
        return fromRow;
    }

    public int getFromCol() {
        return fromCol;
    }

    public int getToRow() {
        return toRow;
    }

    public int getToCol() {
        return toCol;
    }

    public ChessPiece getCapturedPiece() {
        return capturedPiece;
    }
}
