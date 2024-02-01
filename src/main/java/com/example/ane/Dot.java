package com.example.ane;

import javafx.scene.paint.Color;

public class Dot {
    private double x;
    private double y;
    private Color color;

    Dot (double x, double y) {
        this.x = x;
        this.y = y;
    }

    Dot(double x, double y, Color color) {
        this(x, y);
        this.color = color;
    }

    public void updateOnDrag(double deltaX, double deltaY, double xSize, double ySize) {
        if (this.x + deltaX > xSize) {
            this.x += deltaX - xSize;
        } else if (this.x + deltaX < 0) {
            this.x += deltaX + xSize;
        } else {
            this.x += deltaX;
        }
        if (this.y + deltaY > ySize) {
            this.y += deltaY - ySize;
        } else if (this.y + deltaY < 0) {
            this.y += deltaY + ySize;
        } else {
            this.y += deltaY;
        }
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
