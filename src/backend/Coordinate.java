package backend;

import java.io.Serializable;

public class Coordinate implements Serializable{
    public double x;
    public double y;

    public Coordinate(double x, double y) {
        this.x = x;
        this.y = y;
    }
}
