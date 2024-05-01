package com.tg3000.backend;

import java.net.*;
import java.io.*;

public class Client{
    private Socket connToServer;
    private ObjectOutputStream outputStream;
    private ObjectInputStream inputStream;
    private InputHandler inputHandler;

    public Client(int port, String host) {
        try {
            connToServer = new Socket(host, port);
        } catch(IOException e) {
            throw new RuntimeException("Couldn't connect to server: %s\n", e);
        }

        try { outputStream = new ObjectOutputStream(connToServer.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException("Client couldn't open output stream: %s\n", e);
        }

        try {
            inputStream = new ObjectInputStream(connToServer.getInputStream());

            inputHandler = new InputHandler();
            inputHandler.start();
        } catch (IOException e) {
            throw new RuntimeException("Client couldn't open input stream: %s\n", e);
        }
    }

    private class InputHandler extends Thread{
        private boolean should_run = true;

        @Override
        public void run() {
            while (should_run) {
                try {
                    gotNewMessage((ServerMessage) inputStream.readObject());
                } catch(ClassNotFoundException e) {
                    throw new RuntimeException("ClassNotFoundException in at client: %s\n", e);
                } catch(IOException e) {
                    System.out.printf("Closing client connection: %s\n", e);
                    closeConnection();
                    stopThread();
                }
            }
        }

        public void stopThread() {
            should_run = false;
        }
    }

    public void closeConnection() {
        System.out.printf("Closing connection\n");
        try {
            outputStream.close();
            inputStream.close();
            connToServer.close();
        } catch(IOException e) {
            throw new RuntimeException("Couldn't close connection to server or streams: %s\n", e);
        }
    }

    private void gotNewMessage(ServerMessage message) {
        // @ann1k43 und @E-Hippo euer job :D
        for (Coordinate coord : message.coords) {
            System.out.printf("x: %.0f; y: %.0f ||  ", coord.x, coord.y);
        }
        System.out.printf("\n");
    }

    public void clientPosChanged(double x, double y) {
        try {
            outputStream.writeObject(new ClientMessage(x, y));
        } catch(IOException e) {
            closeConnection();
        }
    }
}
