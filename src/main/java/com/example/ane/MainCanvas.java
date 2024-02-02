package com.example.ane;

import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class MainCanvas extends Canvas {
    private boolean selected = false;
    private double mouseX;
    private double mouseY;
    private boolean locked = false;
    private double scrollCount;
    private double scaleFactor;
    private AnimationTimer timer;
    private GraphicsContext gc;
    private MeshGrid meshGrid;
    private final List<Block> blocks = new ArrayList<Block>();
    private final List<Block> selectedBlocks = new ArrayList<Block>();
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
        List<Dot> mesh = meshGrid.getMesh();

        mesh.forEach(dot -> {
            gc.setFill(dot.getColor());
            gc.fillOval(dot.getCenterX() - 1, dot.getCenterY() - 1, dot.getRadius() * 2, dot.getRadius() * 2);
        });

        blocks.forEach(block -> {
            gc.setFill(Color.rgb(0, 50, 0));
            BorderRect borderRect = block.getBorderRect();
            gc.fillRoundRect(
                    borderRect.getX(),
                    borderRect.getY(),
                    borderRect.getWidth(),
                    borderRect.getHeight(),
                    borderRect.getXBoundRadius(),
                    borderRect.getYBoundRadius()
            );
            if (block.isHovered() || block.isSelected()) {
                gc.setFill(Color.GREEN);
                gc.fillRoundRect(
                        borderRect.getX(),
                        borderRect.getY(),
                        borderRect.getWidth(),
                        borderRect.getHeight(),
                        borderRect.getXBoundRadius(),
                        borderRect.getYBoundRadius()
                );
            }

            gc.setFill(Color.rgb(225, 225, 225));
            gc.fillRoundRect(block.getX(), block.getY(), block.getWidth(), block.getHeight(), block.getXBoundRadius(), block.getYBoundRadius());
        });
    }

    public void onMouseMoved(MouseEvent mouseEvent) {
        mouseX = mouseEvent.getX();
        mouseY = mouseEvent.getY();

        blocks.forEach(block -> {
            block.setHovered(!locked && block.contains(mouseEvent.getX(), mouseEvent.getY()));
        });

    }

    public void onMouseDragged(MouseEvent mouseEvent) {
        this.setCursor(Cursor.CLOSED_HAND);

        selectedBlocks.forEach(block -> {
            block.setSelected(false);
        });
        selectedBlocks.clear();

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

        if(selected) {
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

    public void onMouseReleased(MouseEvent mouseEvent) {
        this.setCursor(Cursor.DEFAULT);
        blocks.forEach(block -> {
            if (block.isSelected()) {
                block.setSelected(false);
            }
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
                block.updateOnScroll(scrollEvent.getX(), scrollEvent.getY(), delta);
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
                selectedBlocks.add(block);
            }
        }

        selectedBlocks.forEach(block -> {
            block.setSelected(false);
        });
        selectedBlocks.clear();

        blocks.forEach(block -> {
            if (block.contains(mouseEvent.getX(), mouseEvent.getY())) {
                block.setSelected(true);
                selectedBlocks.add(block);
            }
        });
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
