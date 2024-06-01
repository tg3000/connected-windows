package com.tg3000.backend;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

// Server that get's instantiated once per port / machine, which communicates to each client and distributes positions to every client
public class Server {
    private ServerSocket serverSocket = null;

    // Client connections
    private ArrayList<Socket> clientSockets = new ArrayList<Socket>();
    private final Object lock_clientSockets = new Object();

    // This OutputStream is a one way stream to send data from the server to the client
    private ArrayList<ObjectOutputStream> outputStreams = new ArrayList<ObjectOutputStream>();
    private final Object lock_outputStreams = new Object();

    // This InputStream is a one way stream to collect data from the client
    private ArrayList<ObjectInputStream> inputStreams = new ArrayList<ObjectInputStream>();
    private final Object lock_inputStreams = new Object();

    // Every client's coordinate on screen
    private ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
    private final Object lock_coords = new Object();

    // Every InputHandler
    private ArrayList<InputHandler> inputHandlers = new ArrayList<InputHandler>();
    private final Object lock_inputHandlers = new Object();

    public Server(int port) {
        try {
            serverSocket = new ServerSocket(port);
        } catch(IOException e) {
            throw new RuntimeException("Couldn't start server: %s\n", e);
        }

        ConnectionHandler conHandler = new ConnectionHandler();
        conHandler.start();
    }

    // Safely removes any reference to the client, whose connection has being closed
    public void closeConnection(int client_id) {
        System.out.printf("Closing connection with client_id: %d\n", client_id);

        // Close streams and stop threads related to client
        try {
            inputStreams.get(client_id).close();
            outputStreams.get(client_id).close();
            clientSockets.get(client_id).close();
            inputHandlers.get(client_id).stopThread();
        } catch(IOException e) {
            throw new RuntimeException("Couldn't close connection: %s\n", e);
        }
        
        // Remove all objects refering to client in a ThreadSafe manner
        synchronized (lock_clientSockets)  {clientSockets.remove(client_id);};
        synchronized (lock_inputStreams)  {inputStreams.remove(client_id);};
        synchronized (lock_outputStreams)  {outputStreams.remove(client_id);};
        synchronized (lock_inputHandlers)  {inputHandlers.remove(client_id);}; 
        synchronized (lock_coords) {coords.remove(client_id);};

        // Newer Connections get newer "client_id"s: if client_id 1 closes it's connection -> client_id 2 would become client_id 1
        for (InputHandler inputHandler : inputHandlers) {
            inputHandler.updateClientID(client_id);
        }

        // Send out all the positions again but this time without the closed Connection
        sendPositions();
    }

    // writes all current Frame positions as "ServerMessage"s to each client
    private void sendPositions() {
        for (int i = 0; i < outputStreams.size(); i++) {
            try {
                outputStreams.get(i).writeObject(new ServerMessage((ArrayList<Coordinate>) coords.clone(), i));
            } catch(IOException e) {
                throw new RuntimeException("Server couldn't write to client: %s\n", e);
            }
        }
    }

    // A single thread for each client handling the input, which the client sends to this server
    // 1 Thread is needed for each 1 Client as readObject is ThreadBlocking
    private class InputHandler extends Thread {
        private int client_id;
        private boolean should_run = true;

        public InputHandler(int client_id) {
            super();
            this.client_id = client_id;
        }

        @Override
        public void run() {
            ObjectInputStream input = inputStreams.get(client_id);
            while (should_run) {
                try {
                    ClientMessage clientMessage = (ClientMessage) input.readObject();

                    // Check if this is the first message received by client or if this client already has a coordinate in list
                    if (coords.size() == client_id) {
                        synchronized(lock_coords) {coords.add(clientMessage.coord);}
                    } else {
                        synchronized(lock_coords) {coords.set(client_id, clientMessage.coord);}
                    }

                    // Sends updated coordinates to all clients including itself
                    sendPositions();
                    
                    Thread.sleep(1);
                } catch(ClassNotFoundException e) {
                    throw new RuntimeException("Couldn't read input class: %s\n", e);
                } catch (IOException e) {
                    System.out.printf("Error occured or connection has been closed (%s)\n", e);
                    closeConnection(client_id);
                    stopThread();
                    break;
                } catch (InterruptedException e) {
                    throw new RuntimeException("Couldn't sleep Thread: %s\n", e);
                }
            }
        }
        
        // If a client ID from before has been deleted, update current client_id (client_id is used as an index in ClientList)
        public void updateClientID(int deleted_client_id) {
            if (deleted_client_id < client_id) client_id -= 1;
        }

        public void stopThread() {
            should_run = false;
        }
    }

    // A thread, which accepts connections
    // Needs a seperate thread as to not block the main thread, the reason being that serverSocket.accept() is ThreadBlocking
    private class ConnectionHandler extends Thread {
        private boolean should_run = true;

        @Override 
        public void run() {
            while (should_run) {
                try {
                    // accept connection
                    Socket curr_socket = serverSocket.accept();
                    synchronized (lock_clientSockets) {clientSockets.add(curr_socket);}

                    // open streams
                    synchronized (lock_inputStreams) {inputStreams.add(new ObjectInputStream(curr_socket.getInputStream()));}
                    synchronized (lock_outputStreams) {outputStreams.add(new ObjectOutputStream(curr_socket.getOutputStream()));}
                    
                    int client_id = clientSockets.size()-1;

                    // Start InputHandler thread that manages this client's incoming data
                    InputHandler clientHandler = new InputHandler(client_id);
                    clientHandler.start();
                    synchronized (lock_inputHandlers) {inputHandlers.add(clientHandler);}

                    Thread.sleep(1);
                } catch(Exception e) {
                    stopThread();
                    throw new RuntimeException("Server couldn't accept client socket or open streams: %s", e);
                }
            }  
        }

        public void stopThread() {
            should_run = false;
        }
    }
}