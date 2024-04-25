package backend;

import java.net.*;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.io.*;

public class ConnectionHandler {
    private int port;
    private Server server;
    private Client client;

    public ConnectionHandler(int port) {
        this.port = port;
        if (!isPortBeingUsed()) {
            server = new Server();
        } else {
            System.out.println("Server is already open");
        }
        client = new Client();
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

    
    
    private class Server {
        ServerSocket serverSocket = null;
        ArrayList<Socket> clientSockets = new ArrayList<Socket>();

        public Server() {
            try {
                serverSocket = new ServerSocket(port);
            } catch(IOException e) {
                throw new RuntimeException("Couldn't start server: %s", e);
            }

            Thread acceptConnections = new Thread(){
                public void run() {
                    try {
                        clientSockets.add(serverSocket.accept());
                        Thread.sleep(1);
                    } catch(Exception e) {
                      throw new RuntimeException("");
                    }
                }
            };
            acceptConnections.run();

        }
    }

    private class Client {
        Socket connToServer;
        OutputStream os;

        public Client() {
            try {
                connToServer = new Socket("localhost", port);
            } catch(IOException e) {
                throw new RuntimeException("Couldn't connect to server: %s", e);
            }

            try {
                os = new ObjectOutputStream(connToServer.getOutputStream());
            } catch(IOException e) {
                throw new RuntimeException("Couldn't get outputstream: %s", e);
            }

            sendMessage();
        }
    }

    public void sendMessage() {
        
    }
}