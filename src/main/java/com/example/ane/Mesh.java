package com.example.ane;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Mesh {
    private final List<MeshRect> mesh = new ArrayList<MeshRect>();
    private double spacing;
    private double width;
    private double height;

    Mesh(double xSize, double ySize, double width, double height) {
        spacing = width;
        this.width = width;
        this.height = height;
        fill(xSize, ySize);
        spacing = width;
    };

    public void draw(GraphicsContext gc) {
        mesh.forEach(meshRect -> {
            gc.setLineWidth(0.2);
            gc.setFill(Color.RED);
            gc.strokeRect(
                    meshRect.getX() + 0.1,
                    meshRect.getY() + 0.1,
                    meshRect.getWidth() - 0.2,
                    meshRect.getHeight() - 0.2
            );
            gc.fill();
        });
    }

    public void fill(double xSize, double ySize) {
        mesh.clear();
        for (int i = 0; i < xSize / spacing; i++) {
            for (int j = 0; j < ySize / spacing; j++) {
                this.add(new MeshRect(i * spacing, j * spacing, spacing, spacing));
            }
        }
    }

    public void updateOnResize(double xSize, double ySize) {
        fill(xSize, ySize);
    }

    public void updateOnDrag(double deltaX, double deltaY, double xSize, double ySize) {
        mesh.forEach(meshRect -> {
            meshRect.updateOnDrag(deltaX, deltaY, xSize, ySize);
        });
    }

    public void updateOnScroll(double x, double y, double delta) {
        double scaleFactor;
        if (delta > 0) {
            scaleFactor = (1 + (delta / 40 / 10));
        } else {
            scaleFactor = 1 / (1 - (delta / 40 / 10));
        }
        this.setSpacing(this.getSpacing() * scaleFactor);

        double finalScaleFactor = scaleFactor;
        mesh.forEach(meshRect -> {
            meshRect.setWidth(meshRect.getWidth() * finalScaleFactor);
            meshRect.setHeight(meshRect.getHeight() * finalScaleFactor);
            double xDistBlock = x - meshRect.getX();
            double newXDistBlock = finalScaleFactor * xDistBlock;
            double yDistBlock = y - meshRect.getY();
            double newYDistBlock = finalScaleFactor * yDistBlock;
            meshRect.setX(x - newXDistBlock);
            meshRect.setY(y - newYDistBlock);
        });
    }

    public void add(MeshRect dot) {
        mesh.add(dot);
    }

    public List<MeshRect> getMesh() {
        return mesh;
    }

    public double getSpacing() {
        return spacing;
    }

    public void setSpacing(double spacing) {
        this.spacing = spacing;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}