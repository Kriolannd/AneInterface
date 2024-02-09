package com.example.ane;

import javafx.animation.AnimationTimer;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.*;
import javafx.scene.paint.Color;

import java.util.*;

public class MainCanvas extends Canvas {
    private boolean selected = false;
    private boolean locked = false;
    private double mousePrevX;
    private double mousePrevY;
    private double scrollCount;
    private double scaleFactor;
    private Color canvasColor;
    private final GraphicsContext gc;
    private final Mesh mesh;
    private final List<Block> blocks = new ArrayList<Block>();

    MainCanvas(double width, double height) {
        super(width, height);
        canvasColor = Color.rgb(220, 220, 220);
        scrollCount = 0;
        scaleFactor = 1;
        gc = MainCanvas.this.getGraphicsContext2D();
        mesh = new Mesh(width, height, 10, 10);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                onUpdate();
            }
        };
        timer.start();
    }

    public void onUpdate() {
        gc.clearRect(0, 0, this.getWidth(), this.getHeight());
        gc.setFill(canvasColor);
        gc.fillRect(0,0, this.getWidth(), this.getHeight());

        blocks.forEach(block -> {
            block.draw(gc);
        });
        mesh.draw(gc);
    }

    public void onMouseMoved(MouseEvent mouseEvent) {
        mousePrevX = mouseEvent.getX();
        mousePrevY = mouseEvent.getY();

        blocks.forEach(block -> {block.setHovered(false);});
        List<Block> hoveredBlocks = blocks.stream().filter(
                block -> block.getBorderRect().contains(mouseEvent.getX(), mouseEvent.getY())
        ).toList();

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
        double mouseCurrentX = mouseEvent.getX();
        double mouseCurrentY = mouseEvent.getY();
        double deltaX = mouseCurrentX - mousePrevX;
        double deltaY = mouseCurrentY - mousePrevY;
        this.setCursor(Cursor.CLOSED_HAND);

        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            blocks.forEach(block -> {
                block.setSelected(false);
            });

            List<Block> selectedBlocks = blocks.stream().filter(
                    block -> block.getBorderRect().contains(mouseEvent.getX(), mouseEvent.getY())
            ).toList();

            locked = false;
            if (!selectedBlocks.isEmpty()) {
                Block selectedBlock = selectedBlocks.get(selectedBlocks.size() - 1);
                Collections.swap(blocks, blocks.indexOf(selectedBlock), blocks.size() - 1);
                selectedBlock.setSelected(true);
                locked = true;

                if (selectedBlock.getLeftAnchor().contains(mouseCurrentX, mouseCurrentY)) {
                    selectedBlock.getLeftAnchor().setSelected(true);
                    selectedBlock.resizeWithLeftAnchor(deltaX, scaleFactor);
                    this.setCursor(Cursor.H_RESIZE);
                } else if (selectedBlock.getRightAnchor().contains(mouseCurrentX, mouseCurrentY)) {
                    selectedBlock.resizeWithRightAnchor(deltaX, scaleFactor);
                    selectedBlock.getRightAnchor().setSelected(true);
                    this.setCursor(Cursor.H_RESIZE);
                } else if (selectedBlock.getTopAnchor().contains(mouseCurrentX, mouseCurrentY)) {
                    selectedBlock.resizeWithTopAnchor(deltaY, scaleFactor);
                    selectedBlock.getTopAnchor().setSelected(true);
                    this.setCursor(Cursor.V_RESIZE);
                } else if (selectedBlock.getBottomAnchor().contains(mouseCurrentX, mouseCurrentY)) {
                    selectedBlock.resizeWithBottomAnchor(deltaY, scaleFactor);
                    selectedBlock.getBottomAnchor().setSelected(true);
                    this.setCursor(Cursor.V_RESIZE);
                }
            }
        }
    }

    public void onMouseDragged(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
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

                    if (block.getLeftAnchor().isSelected()) {
                        block.resizeWithLeftAnchor(deltaX, scaleFactor);
                    } else if (block.getRightAnchor().isSelected()) {
                        block.resizeWithRightAnchor(deltaX, scaleFactor);
                    } else if (block.getTopAnchor().isSelected()) {
                        block.resizeWithTopAnchor(deltaY, scaleFactor);
                    } else if (block.getBottomAnchor().isSelected()) {
                        block.resizeWithBottomAnchor(deltaY, scaleFactor);
                    } else {
                        block.updateOnDrag(deltaX, deltaY, mesh.getSpacing());
                    }
                }
            });

            if (!locked) {
                this.setSelected(true);
                locked = true;
            }

            if (selected) {
                mesh.updateOnDrag(
                        deltaX,
                        deltaY,
                        this.getWidth(),
                        this.getHeight()
                );

                blocks.forEach(block -> {
                    block.updateOnCanvasDrag(deltaX, deltaY);
                });
            }

            mousePrevX = mouseCurrentX;
            mousePrevY = mouseCurrentY;
        }
    }

    public void onMouseReleased(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            this.setCursor(Cursor.DEFAULT);
            blocks.forEach(block -> {
                block.setInterX(block.getX());
                block.setInterY(block.getY());
                block.setSelected(false);
                block.getLeftAnchor().setSelected(false);
                block.getRightAnchor().setSelected(false);
                block.getTopAnchor().setSelected(false);
                block.getBottomAnchor().setSelected(false);
            });
            this.setSelected(false);
            locked = false;
        }
    }

    public void onMouseScroll(ScrollEvent scrollEvent) {
        double delta = scrollEvent.getDeltaY();
        double MAX_SCROLL_COUNT = 25;
        if (scrollCount + (delta / 40) <= MAX_SCROLL_COUNT && scrollCount + (delta / 40) >= 0) {
            scrollCount += (delta / 40);
            scaleFactor *= (1 + (delta / 40 / 10));
            blocks.forEach(block -> {
                block.updateOnScroll(scrollEvent.getX(), scrollEvent.getY(), delta, scaleFactor);
            });
            mesh.updateOnScroll(scrollEvent.getX(), scrollEvent.getY(), delta);
        } else if (scrollCount + (delta / 40) > MAX_SCROLL_COUNT) {
            scrollCount = MAX_SCROLL_COUNT;
        } else if (scrollCount + (delta / 40) < 0) {
            scrollCount = 0;
        }
    }

    public void onMouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if (mouseEvent.getClickCount() == 2) {
                Optional<MeshRect> selectedMeshRect = mesh.getMesh().stream().filter(
                        meshRect -> meshRect.contains(mouseEvent.getX(), mouseEvent.getY())
                ).findFirst();
                if (selectedMeshRect.isPresent()) {
                    Block block = new Block(
                            selectedMeshRect.get().getX(),
                            selectedMeshRect.get().getY(),
                            100,
                            100,
                            20,
                            20,
                            scaleFactor
                    );
                    blocks.add(block);
                }
            }

            blocks.forEach(block -> {block.setSelected(false);});
            List<Block> selectedBlocks = blocks.stream().filter(block -> block.getBorderRect().contains(mouseEvent.getX(), mouseEvent.getY())).toList();
            if (!selectedBlocks.isEmpty()) {
                Block selectedBlock = selectedBlocks.get(selectedBlocks.size() - 1);
                Collections.swap(blocks, blocks.indexOf(selectedBlock), blocks.size() - 1);
                selectedBlock.setSelected(true);
                locked = true;
            }
        }
    }

    public void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode().equals(KeyCode.DELETE)) {
            List<Block> clonedBlocks = new ArrayList<Block>(blocks);
            clonedBlocks.forEach(block -> {
                if (block.isSelected()) {
                    blocks.remove(block);
                }
            });
        }
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void setCanvasColor(Color canvasColor) {this.canvasColor = canvasColor;}


    public Mesh getMesh() {return mesh;}
}
