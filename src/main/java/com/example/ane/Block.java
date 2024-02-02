package com.example.ane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Block extends Rectangle {
    private boolean selected = false;
    private boolean isHovered = false;
    private final List<Anchor> anchors = new ArrayList<>();
    private final BorderRect borderRect;

    Block(double x, double y, double width, double height) {
        super(x, y, width, height);
        anchors.add(new Anchor(this.getX(), this.getY(), 5, Color.YELLOW));
        anchors.add(new Anchor(this.getX() + this.getWidth(), this.getY(), 5, Color.YELLOW));
        anchors.add(new Anchor(this.getX(), this.getY() + getHeight(), 5, Color.YELLOW));
        anchors.add(new Anchor(this.getX() + this.getWidth(), this.getY() + this.getHeight(), 5, Color.YELLOW));
        borderRect = new BorderRect(x - 2, y - 2, width + 4, height + 4);
    }

    public void updateOnDrag(double deltaX, double deltaY) {
        this.setX(this.getX() + deltaX);
        this.setY(this.getY() + deltaY);
        anchors.forEach(anchor -> {anchor.updatePosition(deltaX, deltaY);});
        borderRect.updatePosition(this.getX() - 2, this.getY() - 2);
    }

    public void updateOnScroll(double x, double y, double delta) {
        this.setWidth(this.getWidth() * (1 + (delta / 40 / 10)));
        this.setHeight(this.getHeight() * (1 + (delta / 40 / 10)));
        borderRect.updateSize(this.getWidth() + 4, this.getHeight() + 4);

        double xDist = x - this.getX();
        double newXDist = (1 + (delta / 40 / 10)) * xDist;
        double yDist = y - this.getY();
        double newYDist = (1 + (delta / 40 / 10)) * yDist;
        this.setX(x - newXDist);
        this.setY(y - newYDist);
        borderRect.updatePosition(this.getX() - 2, this.getY() - 2);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isHovered() {
        return isHovered;
    }

    public void setHovered(boolean hovered) {
        isHovered = hovered;
    }

    public List<Anchor> getAnchors() {
        return anchors;
    }

    public BorderRect getBorderRect() {
        return borderRect;
    }
}
