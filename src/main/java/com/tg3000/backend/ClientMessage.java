package com.tg3000.backend;

import java.io.Serializable;

public class ClientMessage implements Serializable{
    public Coordinate coord;

    public ClientMessage(double x, double y) {
        coord = new Coordinate(x, y);
    }
}
