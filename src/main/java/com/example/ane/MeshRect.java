package com.example.ane;

import javafx.scene.shape.Rectangle;

public class MeshRect extends Rectangle {
    MeshRect(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public void updateOnDrag(double deltaX, double deltaY, double xSize, double ySize) {
        if (this.getX() + deltaX > xSize) {
            this.setX(this.getX() + deltaX - xSize);
        } else if (this.getX() + deltaX < 0) {
            this.setX(this.getX() + deltaX + xSize);
        } else {
            this.setX(this.getX() + deltaX);
        }
        if (this.getY() + deltaY > ySize) {
            this.setY(this.getY() + deltaY - ySize);
        } else if (this.getY() + deltaY < 0) {
            this.setY(this.getY() + deltaY + ySize);
        } else {
            this.setY(this.getY() + deltaY);
        }
    }
}