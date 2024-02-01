package com.example.ane;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class MeshGrid {
    private final List<Dot> mesh = new ArrayList<>();
    private double spacing;
    private final Color COLOR;
    private final double MAX_SCROLL_PIXEL_COUNT = 1000;
    private double scrollPixelCount;

    MeshGrid(Color color, double xSize, double ySize) {
        COLOR = color;
        spacing = 10;
        scrollPixelCount = 0;
        fill(xSize, ySize);
    };

    public void fill(double xSize, double ySize) {
        mesh.clear();
        for (int i = 0; i < xSize / spacing; i++) {
            for (int j = 0; j < ySize / spacing; j++) {
                this.add(new Dot(i * spacing, j * spacing, COLOR));
            }
        }
    }

    public void updateOnResize(double xSize, double ySize) {
        mesh.clear();
        for (int i = 0; i < xSize / 10; i++) {
            for (int j = 0; j < ySize / 10; j++) {
                this.add(new Dot(i * 10, j * 10, COLOR));
            }
        }
    }

    public void updateOnDrag(double deltaX, double deltaY, double xSize, double ySize) {
        mesh.forEach(dot -> {
            dot.updateOnDrag(deltaX, deltaY, xSize, ySize);
        });
    }

    public void updateOnScroll(double delta, double xSize, double ySize) {
        System.out.println(scrollPixelCount + delta);
        if (scrollPixelCount + delta <= MAX_SCROLL_PIXEL_COUNT && scrollPixelCount + delta >= 0) {
            scrollPixelCount += delta;
            if (spacing + (delta / 40) > 15) {
                spacing = 10;
            } else if (spacing + (delta / 40) < 10) {
                spacing = 15;
            } else {
                spacing += (delta / 40);
            }
            fill(xSize, ySize);
        } else if (scrollPixelCount + delta > MAX_SCROLL_PIXEL_COUNT) {
            scrollPixelCount = MAX_SCROLL_PIXEL_COUNT;
        } else if (scrollPixelCount + delta < 0) {
            scrollPixelCount = 0;
        }
    }

    public void add(Dot dot) {
        mesh.add(dot);
    }

    public List<Dot> getMesh() {
        return mesh;
    }
}
