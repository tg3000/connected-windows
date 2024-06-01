package com.tg3000.backend;

import java.io.Serializable;
import java.util.ArrayList;

// ServerMessage is received by clients and contains data about the whereabouts of other JFrames and also its "client_id"
public class ServerMessage implements Serializable{
    public ArrayList<Coordinate> coords;
    public int client_id;
    public static final long serialVersionUID = 5678L;

    public ServerMessage(ArrayList<Coordinate> coords, int client_id) {
        this.coords = coords;
        this.client_id = client_id;
    }
}
