package com.tg3000.backend;

import java.net.*;
import java.io.*;

import com.tg3000.frontend.Frame.CallBack;

// Handles the connection with the server for easy usage in Frontend
// If no server has been established before for this port/machine, also establishes the server
public class Connection{
    private int port;

    public Server server;
    public Client client;

    public Connection(int port, String host, CallBack callBack) {
        this.port = port;

        if (!isPortBeingUsed()) {
            server = new Server(port);
        } else {
            System.out.println("Server is already open, connecting to server");
        }
        client = new Client(port, host, callBack);
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