package com.example.ane;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;

public class Main extends Application {
    private static final double INITIAL_WIDTH = 1280;
    private static final double INITIAL_HEIGHT = 720;
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setScene(createScene());
        primaryStage.show();
    }

    public Scene createScene() {
        MainCanvas canvas = new MainCanvas(INITIAL_WIDTH, INITIAL_HEIGHT);
        AnchorPane root = new AnchorPane(canvas);
        root.setPrefSize(INITIAL_WIDTH, INITIAL_HEIGHT);
        Scene scene = new Scene(root);

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                root.setPrefWidth(newValue.doubleValue());
                canvas.setWidth(newValue.doubleValue());
                canvas.getMesh().updateOnResize(canvas.getWidth(), canvas.getHeight());
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                root.setPrefHeight(newValue.doubleValue());
                canvas.setHeight(newValue.doubleValue());
                canvas.getMesh().updateOnResize(canvas.getWidth(), canvas.getHeight());
            }
        });

        canvas.setOnMouseMoved(canvas::onMouseMoved);
        canvas.setOnMouseDragged(canvas::onMouseDragged);
        canvas.setOnMouseReleased(canvas::onMouseReleased);
        canvas.setOnScroll(canvas::onMouseScroll);
        canvas.setOnMouseClicked(canvas::onMouseClicked);
        canvas.setOnMousePressed(canvas::onMousePressed);
        canvas.requestFocus();
        canvas.setOnKeyPressed(canvas::onKeyPressed);

        return scene;
    };
}