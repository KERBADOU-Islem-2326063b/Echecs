package com.example.echecs.model;

public class Queen {
    public String color;

    public Queen(String color) {
        this.color = color;
    }

    public String getColor() {
        return this.color;
    }
    public boolean canMove(int rowIndex, int colIndex){
        return true;
    }
}
