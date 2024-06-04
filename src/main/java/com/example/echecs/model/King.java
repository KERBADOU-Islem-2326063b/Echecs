package com.example.echecs.model;

public class King {
    public String color;

    public King(String color) {
        this.color = color;
    }

    public String getColor() {
        return this.color;
    }

    public boolean canMove(int rowIndex, int colIndex){
        return true;
    }
}
