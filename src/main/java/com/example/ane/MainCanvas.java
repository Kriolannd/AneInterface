package com.example.ane;

import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

import java.util.*;

public class MainCanvas extends Canvas {
    private boolean selected = false;
    private double mousePrevX;
    private double mousePrevY;
    private boolean locked = false;
    private double scrollCount;
    private double scaleFactor;
    private AnimationTimer timer;
    private GraphicsContext gc;
    private MeshGrid meshGrid;
    private final List<Block> blocks = new ArrayList<Block>();
    private final double MAX_SCROLL_COUNT = 25;
    MainCanvas(double width, double height) {
        super(width, height);
        scrollCount = 0;
        scaleFactor = 1;
        gc = MainCanvas.this.getGraphicsContext2D();
        meshGrid = new MeshGrid(1280, 720, 1, Color.GREEN);


        timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                onUpdate();
            }
        };
        timer.start();
    }

    public void onUpdate() {
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        gc.setFill(Color.rgb(220, 220, 220));
        gc.fillRect(0,0, this.getWidth(), this.getHeight());

        meshGrid.draw(gc);
        blocks.forEach(block -> {
            block.draw(gc);
        });
    }

    public void onMouseMoved(MouseEvent mouseEvent) {
        mousePrevX = mouseEvent.getX();
        mousePrevY = mouseEvent.getY();

        blocks.forEach(block -> {block.setHovered(false);});
        List<Block> hoveredBlocks = blocks.stream().filter(block -> !locked && block.getBorderRect().contains(mouseEvent.getX(), mouseEvent.getY())).toList();
        if (!hoveredBlocks.isEmpty()) {
            Block hoveredBlock = hoveredBlocks.get(hoveredBlocks.size() - 1);
            hoveredBlock.setHovered(true);
            if (hoveredBlock.getLeftAnchor().contains(mouseEvent.getX(), mouseEvent.getY()) ||
                    hoveredBlock.getRightAnchor().contains(mouseEvent.getX(), mouseEvent.getY())) {
                this.setCursor(Cursor.H_RESIZE);
            } else if (hoveredBlock.getTopAnchor().contains(mouseEvent.getX(), mouseEvent.getY()) ||
                    hoveredBlock.getBottomAnchor().contains(mouseEvent.getX(), mouseEvent.getY())) {
                this.setCursor(Cursor.V_RESIZE);
            } else this.setCursor(Cursor.DEFAULT);
        } else this.setCursor(Cursor.DEFAULT);
    }

    public void onMousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            blocks.forEach(block -> {
                block.setSelected(false);
            });
            List<Block> selectedBlocks = blocks.stream().filter(block -> block.getBorderRect().contains(mouseEvent.getX(), mouseEvent.getY())).toList();
            if (!selectedBlocks.isEmpty()) {
                Block selectedBlock = selectedBlocks.get(selectedBlocks.size() - 1);
                Collections.swap(blocks, blocks.indexOf(selectedBlock), blocks.size() - 1);
                selectedBlock.setSelected(true);
            }
        }
    }

    public void onMouseDragged(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            this.setCursor(Cursor.CLOSED_HAND);
            double mouseCurrentX = mouseEvent.getX();
            double mouseCurrentY = mouseEvent.getY();
            double deltaX = mouseCurrentX - mousePrevX;
            double deltaY = mouseCurrentY - mousePrevY;

            blocks.forEach(block -> {
                if (block.isHovered() || block.isSelected()) {
                    if (!locked) {
                        block.setSelected(true);
                        locked = true;
                    }

                    block.setHovered(true);

                    if (block.getLeftAnchor().contains(mouseCurrentX, mouseCurrentY) || block.getLeftAnchor().isSelected()) {
                        block.getLeftAnchor().setSelected(true);
                        block.resizeWithLeftAnchor(deltaX, scaleFactor);
                        this.setCursor(Cursor.H_RESIZE);
                    } else if (block.getRightAnchor().contains(mouseCurrentX, mouseCurrentY) || block.getRightAnchor().isSelected()) {
                        block.resizeWithRightAnchor(deltaX, scaleFactor);
                        block.getRightAnchor().setSelected(true);
                        this.setCursor(Cursor.H_RESIZE);
                    } else if (block.getTopAnchor().contains(mouseCurrentX, mouseCurrentY) || block.getTopAnchor().isSelected()) {
                        block.resizeWithTopAnchor(deltaY, scaleFactor);
                        block.getTopAnchor().setSelected(true);
                        this.setCursor(Cursor.V_RESIZE);
                    } else if (block.getBottomAnchor().contains(mouseCurrentX, mouseCurrentY) || block.getBottomAnchor().isSelected()) {
                        block.resizeWithBottomAnchor(deltaY, scaleFactor);
                        block.getBottomAnchor().setSelected(true);
                        this.setCursor(Cursor.V_RESIZE);
                    } else {
                        block.updateOnDrag(deltaX, deltaY);
                    }
                }
            });

            if (!locked) {
                this.setSelected(true);
                locked = true;
            }

            if (selected) {
                meshGrid.updateOnDrag(
                        deltaX,
                        deltaY,
                        this.getWidth(),
                        this.getHeight()
                );

                blocks.forEach(block -> {
                    block.updateOnDrag(deltaX, deltaY);
                });
            }

            mousePrevX = mouseCurrentX;
            mousePrevY = mouseCurrentY;
        }
    }

    public void onMouseReleased(MouseEvent mouseEvent) {
        this.setCursor(Cursor.DEFAULT);
        blocks.forEach(block -> {
            block.setSelected(false);
            block.getLeftAnchor().setSelected(false);
            block.getRightAnchor().setSelected(false);
            block.getTopAnchor().setSelected(false);
            block.getBottomAnchor().setSelected(false);
        });
        this.setSelected(false);
        locked = false;
    }

    public void onMouseScroll(ScrollEvent scrollEvent) {
        double delta = scrollEvent.getDeltaY();
        if (scrollCount + (delta / 40) <= MAX_SCROLL_COUNT && scrollCount + (delta / 40) >= 0) {
            scrollCount += (delta / 40);
            scaleFactor *= (1 + (delta / 40 / 10));
            blocks.forEach(block -> {
                block.updateOnScroll(scrollEvent.getX(), scrollEvent.getY(), delta, scaleFactor);
            });
            meshGrid.updateOnScroll(delta, this.getWidth(), this.getHeight());
        } else if (scrollCount + (delta / 40) > MAX_SCROLL_COUNT) {
            scrollCount = MAX_SCROLL_COUNT;
        } else if (scrollCount + (delta / 40) < 0) {
            scrollCount = 0;
        }
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                Block block = new Block(mouseEvent.getX(), mouseEvent.getY(), 100, 100, 20, 20, scaleFactor);
                block.setSelected(true);
                blocks.add(block);
            }

            blocks.forEach(block -> {block.setSelected(false);});
            List<Block> selectedBlocks = blocks.stream().filter(block -> block.getBorderRect().contains(mouseEvent.getX(), mouseEvent.getY())).toList();
            if (!selectedBlocks.isEmpty()) {
                Block selectedBlock = selectedBlocks.get(selectedBlocks.size() - 1);
                Collections.swap(blocks, blocks.indexOf(selectedBlock), blocks.size() - 1);
                selectedBlock.setSelected(true);
            }
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public MeshGrid getMeshGrid() {
        return meshGrid;
    }

    public void setMeshGrid(MeshGrid meshGrid) {
        this.meshGrid = meshGrid;
    }
}
