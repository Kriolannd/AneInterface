package com.example.ane;

import javafx.scene.shape.Rectangle;

public class BorderRect extends Rectangle {
    BorderRect(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public void updatePosition(double newX, double newY) {
        this.setX(newX);
        this.setY(newY);
    }

    public void updateSize(double newXSize, double newYSize) {
        this.setWidth(newXSize);
        this.setHeight(newYSize);
    }
}
