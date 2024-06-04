package com.example.echecs.model;

public class Pawn {
    private String color;
    public Pawn(String color) {
        this.color = color;
    }
    public String getColor() {
        return this.color;
    }

    public boolean canMove(int rowIndex, int colIndex){
        return true;
    }
}
