package com.example.ane;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.*;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private GraphicsContext gc;
    private MeshGrid meshGrid;
    private double mouseX;
    private double mouseY;
    private final List<Block> blocks = new ArrayList<>();
    private boolean locked = false;
    private double deltaBlockX;
    private double deltaBlockY;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(createScene());
        primaryStage.show();
    }

    public Scene createScene() {
        MainCanvas canvas = new MainCanvas(1280, 720);
        gc = canvas.getGraphicsContext2D();

        meshGrid = new MeshGrid(Color.GREEN, 1280, 720);

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                onUpdate(canvas.getWidth(), canvas.getHeight());
            }
        };
        timer.start();

        AnchorPane root = new AnchorPane(canvas);
        root.setPrefSize(1280, 720);
        Scene scene = new Scene(root);
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                root.setPrefWidth(newValue.doubleValue());
                canvas.setWidth(newValue.doubleValue());
                meshGrid.updateOnResize(canvas.getWidth(), canvas.getHeight());
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                root.setPrefHeight(newValue.doubleValue());
                canvas.setHeight(newValue.doubleValue());
                meshGrid.updateOnResize(canvas.getWidth(), canvas.getHeight());
            }
        });
        canvas.setOnMouseMoved(mouseEvent -> {
            mouseX = mouseEvent.getX();
            mouseY = mouseEvent.getY();
        });
        canvas.setOnMouseDragged(mouseEvent -> {
            scene.setCursor(Cursor.CLOSED_HAND);

            blocks.forEach(block -> {
                if (block.contains(mouseEvent.getX(), mouseEvent.getY()) || block.isSelected()) {
                    if (!locked) {
                        deltaBlockX = mouseEvent.getX() - block.getX();
                        deltaBlockY = mouseEvent.getY() - block.getY();
                        block.setSelected(true);
                        locked = true;
                    }

                    if (block.isSelected()) {
                        block.setX(mouseEvent.getX() - deltaBlockX);
                        block.setY(mouseEvent.getY() - deltaBlockY);
                    }
                }
            });

            if (!locked) {
                canvas.setSelected(true);
                locked = true;
            }

            if(canvas.isSelected()) {
                meshGrid.updateOnDrag(
                        mouseEvent.getX() - mouseX,
                        mouseEvent.getY() - mouseY,
                        canvas.getWidth(),
                        canvas.getHeight()
                );
            }
        });
        canvas.setOnMouseReleased(mouseEvent -> {
            scene.setCursor(Cursor.DEFAULT);
            blocks.forEach(block -> {
                if (block.isSelected()) {
                    block.setSelected(false);
                }
            });
            canvas.setSelected(false);
            locked = false;
        });
        canvas.setOnScroll(scrollEvent -> {
            meshGrid.updateOnScroll(scrollEvent.getDeltaY(), canvas.getWidth(), canvas.getHeight());
        });
        canvas.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    blocks.add(new Block(mouseEvent.getX(), mouseEvent.getY(), 100, 100));
                }
            }
        });

        return scene;
    };

    private void onUpdate(double xSize, double ySize) {
        gc.clearRect(0, 0, xSize, ySize);
        List<Dot> mesh = meshGrid.getMesh();

        mesh.forEach(dot -> {
            gc.setFill(dot.getColor());
            gc.fillOval(dot.getX() - 1, dot.getY() - 1, 2, 2);
        });


        blocks.forEach(block -> {
            gc.setFill(Color.BLACK);
            gc.fillRect(block.getX(), block.getY(), block.getWidth(), block.getHeight());
        });
    }
}