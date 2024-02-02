package com.example.ane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Dot extends Circle {
    private Color color;

    Dot (double x, double y, double radius) {
        super(x, y, radius);
    }

    Dot(double x, double y, double radius, Color color) {
        this(x, y, radius);
        this.color = color;
    }

    public void updateOnDrag(double deltaX, double deltaY, double xSize, double ySize) {
        if (this.getCenterX() + deltaX > xSize) {
            this.setCenterX(this.getCenterX() + deltaX - xSize);
        } else if (this.getCenterX() + deltaX < 0) {
            this.setCenterX(this.getCenterX() + deltaX + xSize);
        } else {
            this.setCenterX(this.getCenterX() + deltaX);
        }
        if (this.getCenterY() + deltaY > ySize) {
            this.setCenterY(this.getCenterY() + deltaY - ySize);
        } else if (this.getCenterY() + deltaY < 0) {
            this.setCenterY(this.getCenterY() + deltaY + ySize);
        } else {
            this.setCenterY(this.getCenterY() + deltaY);
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
