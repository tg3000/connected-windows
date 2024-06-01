package com.tg3000.backend;

import java.net.*;
import java.io.*;

import com.tg3000.frontend.Frame.CallBack;

// Client that handles communication to server
// One instance of this programm will have both a server and client running
public class Client {
    private Socket connToServer;

    // One way stream to send data to server
    private ObjectOutputStream outputStream;

    // One way stream to receive data from server
    private ObjectInputStream inputStream;

    // Thread for reading from server
    private InputHandler inputHandler;

    // Callback used to send received data from server back to JFrame, which in turn 
    // transfers it to panel for drawing
    private CallBack callBack;


    public Client(int port, String host, CallBack callBack) {
        this.callBack = callBack;
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

    // Thread for receiving data from server
    private class InputHandler extends Thread{
        private boolean should_run = true;

        @Override
        public void run() {
            while (should_run) {
                try {
                    gotNewMessage((ServerMessage) inputStream.readObject());
                } catch(ClassNotFoundException e) {
                    throw new RuntimeException("ClassNotFoundException in client: %s\n", e);
                } catch(IOException e) {
                    System.out.printf("Client closing connection with Server: %s\n", e);
                    closeConnection();
                    stopThread();
                }
            }
        }

        public void stopThread() {
            should_run = false;
        }
    }

    // Closes connection to server
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

    // Calls callback function to give the newly received data from server to the JFrame
    private void gotNewMessage(ServerMessage message) {
        Coordinate[] positions = new Coordinate[message.coords.size()];
        for (int i = 0; i < message.coords.size(); i++) {
            positions[i] = message.coords.get(i);
        }
        callBack.call(positions, message.coords.get(message.client_id));
    }

    // Method that gets called when JFrame moves
    // Sends new position to server
    public void clientPosChanged(double x, double y) {
        try {
            outputStream.writeObject(new ClientMessage(x, y));
        } catch(IOException e) {
            closeConnection();
        }
    }
}
