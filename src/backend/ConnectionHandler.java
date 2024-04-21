package backend;

import java.net.*;
import java.io.*;

public class ConnectionHandler {
    private int port;

    public ConnectionHandler(int port) {
        this.port = port;
        if (!isPortBeingUsed()) {
            startServer();
        } else {
            System.out.println("Server is already open");
        }
    }

    private boolean isPortBeingUsed() {
        ServerSocket check = null;

        try {
            check = new ServerSocket(port);
            check.close();
            return false;
        } catch(IOException e) {
            return true;
        }
        
    }

    private ServerSocket startServer() {
        try {
            return new ServerSocket(port);
        } catch(IOException e) {
            System.out.printf("Couldn't start server. Exception: %s", e);
        }
        return null;
    }
}