package com.tg3000.backend;

import java.net.*;
import java.io.*;

public class Connection{
    private int port;

    public Server server;
    public Client client;

    public Connection(int port, String host) {
        this.port = port;

        if (!isPortBeingUsed()) {
            server = new Server(port);
        } else {
            System.out.println("Server is already open");
        }
        client = new Client(port, host);
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

    
}