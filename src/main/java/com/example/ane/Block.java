package com.example.ane;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;

public class Block extends Rectangle {
    private boolean selected = false;
    private boolean hovered = false;
    private Color blockColor = new Color(0.86, 0.86, 0.86, 0.85);
    private Color blockBorderColor = Color.rgb(0, 50, 0);
    private Color blockAnchorColor = Color.rgb(61, 140, 57);
    private final Anchor leftAnchor;
    private final Anchor rightAnchor;
    private final Anchor topAnchor;
    private final Anchor bottomAnchor;
    private final List<Anchor> anchors = new ArrayList<Anchor>();
    private final BorderRect borderRect;
    private double xBoundRadius;
    private double yBoundRadius;
    private double interX;
    private double interY;

    Block(double x, double y, double width, double height, double xBoundRadius, double yBoundRadius, double scaleFactor) {
        super(x, y, width * scaleFactor, height * scaleFactor);
        this.interX = x;
        this.interY = y;
        this.xBoundRadius = xBoundRadius * scaleFactor;
        this.yBoundRadius = yBoundRadius * scaleFactor;
        leftAnchor = new Anchor(x - 3 * scaleFactor, y + (height * scaleFactor * (1 - 0.7) / 2), 3 * scaleFactor, 0.7 * height * scaleFactor);
        rightAnchor = new Anchor(x + width * scaleFactor, y + (height * scaleFactor * (1 - 0.7) / 2), 3 * scaleFactor, 0.7 * height * scaleFactor);
        topAnchor = new Anchor(x + (width * scaleFactor * (1 - 0.7) / 2), y - 3 * scaleFactor, 0.7 * width * scaleFactor, 3 * scaleFactor);
        bottomAnchor = new Anchor(x + (width * scaleFactor * (1 - 0.7) / 2), y + height * scaleFactor, 0.7 * width * scaleFactor, 3 * scaleFactor);
        anchors.add(leftAnchor);
        anchors.add(rightAnchor);
        anchors.add(topAnchor);
        anchors.add(bottomAnchor);
        borderRect = new BorderRect(x, y, width, height, xBoundRadius, yBoundRadius, scaleFactor);
    }

    public void draw(GraphicsContext gc) {
        gc.setFill(blockBorderColor);
        borderRect.draw(gc);
        if (hovered || selected) {
            gc.setFill(blockAnchorColor);
            borderRect.draw(gc);
            leftAnchor.draw(gc);
            rightAnchor.draw(gc);
            topAnchor.draw(gc);
            bottomAnchor.draw(gc);
        }

        gc.setFill(blockColor);
        gc.fillRoundRect(this.getX(), this.getY(), this.getWidth(), this.getHeight(),
                this.getXBoundRadius(), this.getYBoundRadius());
    }

    public void updateOnCanvasDrag(double deltaX, double deltaY) {
        this.setX(this.getX() + deltaX);
        this.setY(this.getY() + deltaY);
        leftAnchor.updateDeltaPosition(deltaX, deltaY);
        rightAnchor.updateDeltaPosition(deltaX, deltaY);
        topAnchor.updateDeltaPosition(deltaX, deltaY);
        bottomAnchor.updateDeltaPosition(deltaX, deltaY);
        borderRect.updateDeltaPosition(deltaX, deltaY);
    }

    public void updateOnDrag(double deltaX, double deltaY, double spacing) {
        interX += deltaX;
        interY += deltaY;
        int timesX = (int) ((interX - this.getX()) / spacing);
        int timesY = (int) ((interY - this.getY()) / spacing);
        this.setX(this.getX() + spacing * timesX);
        this.setY(this.getY() + spacing * timesY);
        leftAnchor.updateDeltaPosition(spacing * timesX, spacing * timesY);
        rightAnchor.updateDeltaPosition(spacing * timesX, spacing * timesY);
        topAnchor.updateDeltaPosition(spacing * timesX, spacing * timesY);
        bottomAnchor.updateDeltaPosition(spacing * timesX, spacing * timesY);
        borderRect.updateDeltaPosition(spacing * timesX, spacing * timesY);
    }

    public void updateOnScroll(double x, double y, double delta, double globalScaleFactor) {
        double scaleFactor;
        if (delta > 0) {
            scaleFactor = (1 + (delta / 40 / 10));
        } else {
            scaleFactor = 1 / (1 - (delta / 40 / 10));
        }

        if (isGoodWidth(this.getWidth() * scaleFactor, globalScaleFactor) && isGoodHeight(this.getHeight() * scaleFactor, globalScaleFactor)) {
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
            this.setInterX(this.getX());
            this.setInterY(this.getY());

            leftAnchor.updateOnScroll(x, y, scaleFactor);
            rightAnchor.updateOnScroll(x, y, scaleFactor);
            topAnchor.updateOnScroll(x, y, scaleFactor);
            bottomAnchor.updateOnScroll(x, y, scaleFactor);
            borderRect.updateOnScroll(x, y, scaleFactor);
        }
    }

    public void resizeWithLeftAnchor(double deltaX, double globalScaleFactor) {
        if (isGoodWidth(this.getWidth() - deltaX, globalScaleFactor)) {
            this.setX(this.getX() + deltaX);
            leftAnchor.updateDeltaPosition(deltaX, 0);
            topAnchor.updateDeltaPosition(deltaX, 0);
            bottomAnchor.updateDeltaPosition(deltaX, 0);
            borderRect.updateDeltaPosition(deltaX, 0);

            this.setWidth(this.getWidth() - deltaX);
            topAnchor.setWidth(topAnchor.getWidth() - deltaX);
            bottomAnchor.setWidth(bottomAnchor.getWidth() - deltaX);
            borderRect.setWidth(borderRect.getWidth() - deltaX);
        }
    }

    public void resizeWithRightAnchor(double deltaX, double globalScaleFactor) {
        if (isGoodWidth(this.getWidth() + deltaX, globalScaleFactor)) {
            rightAnchor.updateDeltaPosition(deltaX, 0);

            this.setWidth(this.getWidth() + deltaX);
            topAnchor.setWidth(topAnchor.getWidth() + deltaX);
            bottomAnchor.setWidth(bottomAnchor.getWidth() + deltaX);
            borderRect.setWidth(borderRect.getWidth() + deltaX);
        }
    }

    public void resizeWithTopAnchor(double deltaY, double globalScaleFactor) {
        if (isGoodHeight(this.getHeight() - deltaY, globalScaleFactor)) {
            this.setY(this.getY() + deltaY);
            leftAnchor.updateDeltaPosition(0, deltaY);
            rightAnchor.updateDeltaPosition(0, deltaY);
            topAnchor.updateDeltaPosition(0, deltaY);
            borderRect.updateDeltaPosition(0, deltaY);

            this.setHeight(this.getHeight() - deltaY);
            leftAnchor.setHeight(leftAnchor.getHeight() - deltaY);
            rightAnchor.setHeight(rightAnchor.getHeight() - deltaY);
            borderRect.setHeight(borderRect.getHeight() - deltaY);
        }
    }

    public void resizeWithBottomAnchor(double deltaY, double globalScaleFactor) {
        if (isGoodHeight(this.getHeight() + deltaY, globalScaleFactor)) {
            bottomAnchor.updateDeltaPosition(0, deltaY);

            this.setHeight(this.getHeight() + deltaY);
            leftAnchor.setHeight(leftAnchor.getHeight() + deltaY);
            rightAnchor.setHeight(rightAnchor.getHeight() + deltaY);
            borderRect.setHeight(borderRect.getHeight() + deltaY);
        }
    }

    public boolean isGoodWidth(double width, double globalScaleFactor) {return width >= 50 * globalScaleFactor;}

    public boolean isGoodHeight(double height, double globalScaleFactor) {return height >= 50 * globalScaleFactor;}

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public BorderRect getBorderRect() {
        return borderRect;
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

    public Anchor getLeftAnchor() {return leftAnchor;}

    public Anchor getRightAnchor() {return rightAnchor;}

    public Anchor getTopAnchor() {return topAnchor;}

    public Anchor getBottomAnchor() {return bottomAnchor;}

    public List<Anchor> getAnchors() {return anchors;}

    public void setBlockColor(Color blockColor) {this.blockColor = blockColor;}

    public void setBlockBorderColor(Color blockBorderColor) {this.blockBorderColor = blockBorderColor;}

    public void setBlockAnchorColor(Color blockAnchorColor) {this.blockAnchorColor = blockAnchorColor;}

    public double getInterX() {return interX;}

    public void setInterX(double interX) {this.interX = interX;}

    public double getInterY() {return interY;}

    public void setInterY(double interY) {this.interY = interY;}
}
