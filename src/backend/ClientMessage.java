package backend;

import java.io.Serializable;

public class ClientMessage implements Serializable{
    public double[] cord;
    public static final long serialVersionUID = 1234L;

    public ClientMessage(double x, double y) {
        cord = new double[] {x, y};
    }
}
