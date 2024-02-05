package com.example.ane;

import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class Anchor extends Rectangle implements Update{
    private boolean selected;
    Anchor(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(Color.rgb(61, 140, 57));
        gc.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    public void updateOnScroll(double x, double y, double scaleFactor) {
        this.updateSize(scaleFactor);
        double xDist = x - this.getX();
        double newXDist = scaleFactor * xDist;
        double yDist = y - this.getY();
        double newYDist = scaleFactor * yDist;
        this.updateAbsolutePosition(x - newXDist, y - newYDist);
    }

    @Override
    public void updateAbsolutePosition(double newX, double newY) {
        this.setX(newX);
        this.setY(newY);
    }

    @Override
    public void updateDeltaPosition(double deltaX, double deltaY) {
        this.setX(this.getX() + deltaX);
        this.setY(this.getY() + deltaY);
    }

    @Override
    public void updateSize(double scaleFactor) {
        this.setWidth(this.getWidth() * scaleFactor);
        this.setHeight(this.getHeight() * scaleFactor);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
