package com.example.ane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Anchor extends Circle implements Update{
    Anchor(double x, double y, double radius, Color color) {
        super(x, y, radius, color);
    }

    @Override
    public void updateAbsolutePosition(double newX, double newY) {
        this.setCenterX(newX);
        this.setCenterY(newY);
    }

    @Override
    public void updateDeltaPosition(double deltaX, double deltaY) {
        this.setCenterX(this.getCenterX() + deltaX);
        this.setCenterY(this.getCenterY() + deltaY);
    }

    @Override
    public void updateSize(double scaleFactor) {
        this.setRadius(this.getRadius() * scaleFactor);
    }
}
