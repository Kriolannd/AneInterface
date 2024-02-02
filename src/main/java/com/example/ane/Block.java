package com.example.ane;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Block extends Rectangle {
    private boolean selected = false;
    private boolean isHovered = false;
    private final Anchor topLeftAnchor;
    private final Anchor topRightAnchor;
    private final Anchor bottomLeftAnchor;
    private final Anchor bottomRightAnchor;
    private final BorderRect borderRect;
    private double xBoundRadius;
    private double yBoundRadius;

    Block(double x, double y, double width, double height, double xBoundRadius, double yBoundRadius, double scaleFactor) {
        super(x, y, width * scaleFactor, height * scaleFactor);
        this.xBoundRadius = xBoundRadius * scaleFactor;
        this.yBoundRadius = yBoundRadius * scaleFactor;
        topLeftAnchor = new Anchor(this.getX(), this.getY(), 5 * scaleFactor, Color.YELLOW);
        topRightAnchor = new Anchor(this.getX() + this.getWidth(), this.getY(), 5 * scaleFactor, Color.YELLOW);
        bottomLeftAnchor = new Anchor(this.getX(), this.getY() + getHeight(), 5 * scaleFactor, Color.YELLOW);
        bottomRightAnchor = new Anchor(this.getX() + this.getWidth(), this.getY() + this.getHeight(), 5 * scaleFactor, Color.YELLOW);
        borderRect = new BorderRect(x, y, width, height, xBoundRadius, yBoundRadius, scaleFactor);
    }

    public void updateOnDrag(double deltaX, double deltaY) {
        this.setX(this.getX() + deltaX);
        this.setY(this.getY() + deltaY);
        topLeftAnchor.updateDeltaPosition(deltaX, deltaY);
        topRightAnchor.updateDeltaPosition(deltaX, deltaY);
        bottomLeftAnchor.updateDeltaPosition(deltaX, deltaY);
        bottomRightAnchor.updateDeltaPosition(deltaX, deltaY);
        borderRect.updateDeltaPosition(deltaX, deltaY);
    }

    public void updateOnScroll(double x, double y, double delta) {
        double scaleFactor = 1 + (delta / 40 / 10);
        topLeftAnchor.updateSize(scaleFactor);
        topRightAnchor.updateSize(scaleFactor);
        bottomLeftAnchor.updateSize(scaleFactor);
        bottomRightAnchor.updateSize(scaleFactor);
        borderRect.updateSize(scaleFactor);
        this.setWidth(this.getWidth() * scaleFactor);
        this.setHeight(this.getHeight() * scaleFactor);
        this.setXBoundRadius(this.getXBoundRadius() * scaleFactor);
        this.setYBoundRadius(this.getYBoundRadius() * scaleFactor);

        double xDistBlock = x - this.getX();
        double newXDistBlock = scaleFactor * xDistBlock;
        double yDistBlock = y - this.getY();
        double newYDistBlock = scaleFactor * yDistBlock;
        this.setX(x - newXDistBlock);
        this.setY(y - newYDistBlock);

        double xDistBound = x - borderRect.getX();
        double newXDistBound = scaleFactor * xDistBound;
        double yDistBound = y - borderRect.getY();
        double newYDistBound = scaleFactor * yDistBound;
        borderRect.updateAbsolutePosition(x - newXDistBound, y - newYDistBound);

        topLeftAnchor.updateAbsolutePosition(this.getX(), this.getY());
        topRightAnchor.updateAbsolutePosition(this.getX() + this.getWidth(), this.getY());
        bottomLeftAnchor.updateAbsolutePosition(this.getX(), this.getY() + getHeight());
        bottomRightAnchor.updateAbsolutePosition(this.getX() + this.getWidth(), this.getY() + this.getHeight());
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

    public BorderRect getBorderRect() {
        return borderRect;
    }

    public Anchor getTopLeftAnchor() {
        return topLeftAnchor;
    }

    public Anchor getTopRightAnchor() {
        return topRightAnchor;
    }

    public Anchor getBottomLeftAnchor() {
        return bottomLeftAnchor;
    }

    public Anchor getBottomRightAnchor() {
        return bottomRightAnchor;
    }

    public double getXBoundRadius() {
        return xBoundRadius;
    }

    public void setXBoundRadius(double xBoundRadius) {
        this.xBoundRadius = xBoundRadius;
    }

    public double getYBoundRadius() {
        return yBoundRadius;
    }

    public void setYBoundRadius(double yBoundRadius) {
        this.yBoundRadius = yBoundRadius;
    }
}
