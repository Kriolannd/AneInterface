package com.example.ane;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class MeshGrid {
    private final List<Dot> mesh = new ArrayList<Dot>();
    private double spacing;
    private final Color COLOR;
    private double dotRadius;

    MeshGrid(double xSize, double ySize, double radius, Color color) {
        COLOR = color;
        spacing = 10;
        dotRadius = radius;
        fill(xSize, ySize);
    };

    public void fill(double xSize, double ySize) {
        mesh.clear();
        for (int i = 0; i < xSize / spacing; i++) {
            for (int j = 0; j < ySize / spacing; j++) {
                this.add(new Dot(i * spacing, j * spacing, dotRadius, COLOR));
            }
        }
    }

    public void updateOnResize(double xSize, double ySize) {
        fill(xSize, ySize);
    }

    public void updateOnDrag(double deltaX, double deltaY, double xSize, double ySize) {
        mesh.forEach(dot -> {
            dot.updateOnDrag(deltaX, deltaY, xSize, ySize);
        });
    }

    public void updateOnScroll(double delta, double xSize, double ySize) {
        if (spacing + (delta / 40) > 15) {
            this.setSpacing(10);
        } else if (spacing + (delta / 40) < 10) {
            this.setSpacing(15);
        } else {
            this.setSpacing(spacing + delta / 40);
        }
        fill(xSize, ySize);
    }

    public void add(Dot dot) {
        mesh.add(dot);
    }

    public List<Dot> getMesh() {
        return mesh;
    }

    public double getDotRadius() {
        return dotRadius;
    }

    public void setDotRadius(double dotRadius) {
        this.dotRadius = dotRadius;
    }

    public double getSpacing() {
        return spacing;
    }

    public void setSpacing(double spacing) {
        this.spacing = spacing;
    }
}
