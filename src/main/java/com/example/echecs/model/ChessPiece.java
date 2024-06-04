package com.example.echecs.model;

public abstract class ChessPiece {
    protected String color;
    protected int columnIndex;
    protected int rowIndex;

    public ChessPiece(String color, int columnIndex, int rowIndex) {
        this.color = color;
        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
    }

    public String getColor() {
        return color;
    }

    public int getColumnIndex() {
        return columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setPosition(int columnIndex, int rowIndex) {
        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
    }

    public abstract boolean canMove(int targetCol, int targetRow, ChessPiece[][] board);

    public String getImagePath() {
        return "file:src/main/resources/com/example/echecs/img/" + color.toLowerCase() + "_" + getClass().getSimpleName().toLowerCase() + ".png";
    }
}