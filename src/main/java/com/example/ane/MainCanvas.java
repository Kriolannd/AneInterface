package com.example.ane;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

import java.util.List;

public class MainCanvas extends Canvas {
    private boolean selected = false;
    private double mouseX;
    private double mouseY;
    private boolean locked = false;
    private double scrollCount;
    private final double MAX_SCROLL_COUNT = 25;
    MainCanvas(double x, double y) {
        super(x, y);
        scrollCount = 0;
    }

    public void onMouseMoved(MouseEvent mouseEvent, List<Block> blocks) {
        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();

        blocks.forEach(block -> {
            block.setHovered(!locked && block.contains(mouseEvent.getX(), mouseEvent.getY()));
        });
    }

    public void onMouseDragged(MouseEvent mouseEvent, MeshGrid meshGrid, Scene scene, List<Block> blocks) {
        scene.setCursor(Cursor.CLOSED_HAND);

        blocks.forEach(block -> {
            if (block.contains(mouseEvent.getX(), mouseEvent.getY()) || block.isSelected()) {
                if (!locked) {
                    block.setSelected(true);
                    locked = true;
                }

                if (block.isSelected()) {
                    block.setHovered(true);
                    block.updateOnDrag(mouseEvent.getX() - mouseX, mouseEvent.getY() - mouseY);
                }
            }
        });

        if (!locked) {
            this.setSelected(true);
            locked = true;
        }

        if(this.isSelected()) {
            meshGrid.updateOnDrag(
                    mouseEvent.getX() - mouseX,
                    mouseEvent.getY() - mouseY,
                    this.getWidth(),
                    this.getHeight()
            );

            blocks.forEach(block -> {
                block.updateOnDrag(mouseEvent.getX() - mouseX, mouseEvent.getY() - mouseY);
            });
        }

        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();
    }

    public void onMouseReleased(MouseEvent mouseEvent, Scene scene, List<Block> blocks) {
        scene.setCursor(Cursor.DEFAULT);
        blocks.forEach(block -> {
            if (block.isSelected()) {
                block.setSelected(false);
            }
        });
        this.setSelected(false);
        locked = false;
    }

    public void onMouseScroll(ScrollEvent scrollEvent, MeshGrid meshGrid, List<Block> blocks) {
        double delta = scrollEvent.getDeltaY();
        if (scrollCount + (delta / 40) <= MAX_SCROLL_COUNT && scrollCount + (delta / 40) >= 0) {
            scrollCount += (delta / 40);
            blocks.forEach(block -> {
                block.updateOnScroll(scrollEvent.getX(), scrollEvent.getY(), scrollEvent.getDeltaY());
            });
            meshGrid.updateOnScroll(scrollEvent.getDeltaY(), this.getWidth(), this.getHeight());
        } else if (scrollCount + (delta / 40) > MAX_SCROLL_COUNT) {
            scrollCount = MAX_SCROLL_COUNT;
        } else if (scrollCount + (delta / 40) < 0) {
            scrollCount = 0;
        }
    }

    public void onMouseClicked(MouseEvent mouseEvent, List<Block> blocks) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                blocks.add(new Block(mouseEvent.getX(), mouseEvent.getY(), 100, 100));
            }
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
