package com.tg3000.backend;

import java.io.Serializable;

// Message from client to server containing client's position
public class ClientMessage implements Serializable {
    public Coordinate coord;

    public ClientMessage(double x, double y) {
        coord = new Coordinate(x, y);
    }
}
