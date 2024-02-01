package com.example.ane;

import javafx.scene.shape.Rectangle;

public class Block extends Rectangle {
    private boolean selected = false;

    Block(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
