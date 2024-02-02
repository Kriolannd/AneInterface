package com.example.ane;

public interface Update {
    public void updateAbsolutePosition(double newX, double newY);
    public void updateDeltaPosition(double deltaX, double deltaY);
    public void updateSize(double scaleFactor);
}
