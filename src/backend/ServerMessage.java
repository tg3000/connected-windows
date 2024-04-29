package backend;

import java.io.Serializable;

public class ServerMessage implements Serializable{
    public double[][] cords;
        public int client_id;
        public static final long serialVersionUID = 5678L;

        public ServerMessage(double[][] cords, int client_id) {
            this.cords = cords;
            this.client_id = client_id;
        }
}
