package backend;

import java.io.Serializable;
import java.util.ArrayList;

public class ServerMessage implements Serializable{
    public ArrayList<Coordinate> coords;
        public int client_id;
        public static final long serialVersionUID = 5678L;

        public ServerMessage(ArrayList<Coordinate> coords, int client_id) {
            this.cords = cords;
            this.client_id = client_id;
        }
}
