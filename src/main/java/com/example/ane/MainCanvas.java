package com.example.ane;

import javafx.scene.canvas.Canvas;

public class MainCanvas extends Canvas {
    private boolean selected = false;
    MainCanvas(double x, double y) {
        super(x, y);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
