package com.example.echecs.model;

public class Bishop {
    public String color;

    public Bishop(String color) {
        this.color = color;
    }    public String getColor() {
        return this.color;
    }

    public boolean canMove(int rowIndex, int colIndex){
        return true;
    }
}
