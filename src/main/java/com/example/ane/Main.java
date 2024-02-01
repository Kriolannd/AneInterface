package com.example.ane;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.*;

import java.util.List;

public class Main extends Application {
    private GraphicsContext gc;
    private MeshGrid meshGrid;
    private double mouseX;
    private double mouseY;
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(createContent());
        primaryStage.show();
    }

    public Scene createContent() {
        Canvas canvas = new Canvas(1280, 720);
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
        scene.setOnMouseMoved(mouseEvent -> {
            mouseX = mouseEvent.getSceneX();
            mouseY = mouseEvent.getSceneY();
        });
        scene.setOnMouseDragged(mouseEvent -> {
            scene.setCursor(Cursor.CLOSED_HAND);
            meshGrid.updateOnDrag(
                    mouseEvent.getSceneX() - mouseX,
                    mouseEvent.getSceneY() - mouseY,
                    canvas.getWidth(),
                    canvas.getHeight()
            );
        });
        scene.setOnMouseReleased(mouseEvent -> {
            scene.setCursor(Cursor.DEFAULT);
        });
        scene.setOnScroll(scrollEvent -> {
            meshGrid.updateOnScroll(scrollEvent.getDeltaY(), canvas.getWidth(), canvas.getHeight());
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
    }
}