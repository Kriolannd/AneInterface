package com.example.ane;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.*;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {
    private GraphicsContext gc;
    private MeshGrid meshGrid;
    private final List<Block> blocks = new ArrayList<>();

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

        canvas.setOnMouseMoved(mouseEvent -> {canvas.onMouseMoved(mouseEvent, blocks);});
        canvas.setOnMouseDragged(mouseEvent -> {canvas.onMouseDragged(mouseEvent, meshGrid, scene, blocks);});
        canvas.setOnMouseReleased(mouseEvent -> {canvas.onMouseReleased(mouseEvent, scene, blocks);});
        canvas.setOnScroll(scrollEvent -> {canvas.onMouseScroll(scrollEvent, meshGrid, blocks);});
        canvas.setOnMouseClicked(mouseEvent -> {canvas.onMouseClicked(mouseEvent, blocks);});

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
            if (block.isHovered()) {
//                block.getAnchors().forEach(anchor -> {
//                    gc.setFill(Color.GREEN);
//                    gc.fillOval(anchor.getCenterX() - anchor.getRadius(), anchor.getCenterY() - anchor.getRadius(), 2 * anchor.getRadius(), 2 * anchor.getRadius());
//                });
                gc.setFill(Color.GREEN);
                gc.fillRect(
                        block.getBorderRect().getX(),
                        block.getBorderRect().getY(),
                        block.getBorderRect().getWidth(),
                        block.getBorderRect().getHeight()
                );
            }

            gc.setFill(Color.BLACK);
            gc.fillRect(block.getX(), block.getY(), block.getWidth(), block.getHeight());
        });
    }
}