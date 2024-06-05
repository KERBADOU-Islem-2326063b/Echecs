package com.example.echecs.model;

public abstract class ChessPiece {
    // Attributs représentant la couleur et la position de la pièce
    protected String color;
    protected int columnIndex;
    protected int rowIndex;

    // Constructeur pour initialiser la couleur et la position de la pièce
    public ChessPiece(String color, int columnIndex, int rowIndex) {
        this.color = color;
        this.columnIndex = columnIndex;
        this.rowIndex = rowIndex;
    }

    // Méthode pour obtenir la couleur de la pièce
    public String getColor() {
        return color;
    }

    // Méthodes pour obtenir les indices de colonne et de ligne de la pièce
    public int getColumnIndex() {
        return columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    // Méthode pour définir la position de la pièce sur le plateau
    public void setPosition(int rowIndex, int columnIndex) {
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    // Méthode abstraite pour vérifier si une pièce peut se déplacer vers une position cible
    public abstract boolean canMove(int targetCol, int targetRow, ChessPiece[][] board);

    public ChessPiece copy() {
        return new Pawn(this.getColor(), this.getColumnIndex(), this.getRowIndex());
    }

    // Méthode pour obtenir le chemin de l'image représentant la pièce
    public String getImagePath() {
        return "file:src/main/resources/com/example/echecs/img/" + color.toLowerCase() + "_" + getClass().getSimpleName().toLowerCase() + ".png";
    }

}
