package com.example.echecs.model;

public class Tower {
    public String color;

    public Tower(String color) {
        this.color = color;
    }
    public String getColor() {
        return this.color;
    }

    public boolean canMove(int rowIndex, int colIndex){
        return true;
    }
}
