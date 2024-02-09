package com.example.ane;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.shape.Rectangle;

public class BorderRect extends Rectangle implements Update {
    private double xBoundRadius;
    private double yBoundRadius;
    private double lineWidth;
    BorderRect(double x, double y, double width, double height, double xBoundRadius, double yBoundRadius, double scaleFactor) {
        super(x - 3 * scaleFactor, y - 3 * scaleFactor, (width + 6) * scaleFactor, (height + 6) * scaleFactor);
        lineWidth = 3 * scaleFactor;
        this.xBoundRadius = xBoundRadius * (width + 6) / width * scaleFactor;
        this.yBoundRadius = yBoundRadius * (height + 6) / height * scaleFactor;
    }

    public void draw(GraphicsContext gc) {
        gc.setLineWidth(this.getLineWidth());
        gc.strokeRoundRect(
                this.getX() + this.getLineWidth() / 2,
                this.getY() + this.getLineWidth() / 2,
                this.getWidth() - this.getLineWidth(),
                this.getHeight() - this.getLineWidth(),
                this.getXBoundRadius(),
                this.getYBoundRadius()
        );
        gc.fill();
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
        this.setXBoundRadius(this.getXBoundRadius() * scaleFactor);
        this.setYBoundRadius(this.getYBoundRadius() * scaleFactor);
        this.setLineWidth(this.getLineWidth() * scaleFactor);
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

    public double getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(double lineWidth) {
        this.lineWidth = lineWidth;
    }
}
