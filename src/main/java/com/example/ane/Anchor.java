package com.example.ane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Anchor extends Circle {
    Anchor(double x, double y, double radius, Color color) {
        super(x, y, radius, color);
    }

    public void updatePosition(double deltaX, double deltaY) {
        this.setCenterX(this.getCenterX() + deltaX);
        this.setCenterY(this.getCenterY() + deltaY);
    }
}
