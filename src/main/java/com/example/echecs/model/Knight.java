package com.example.echecs.model;

public class Knight {
    public String color;

    public Knight(String color) {
        this.color = color;
    }

    public String getColor() {
        return this.color;
    }

    public boolean canMove(int rowIndex, int colIndex){
        return true;
    }
}
