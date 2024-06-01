package com.tg3000.backend;

import java.io.Serializable;

// Simple class representing a Coordinate
public class Coordinate implements Serializable{
    public double x;
    public double y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
