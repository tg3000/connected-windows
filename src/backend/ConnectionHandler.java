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
        ArrayList<ObjectOutputStream> outputStreams = new ArrayList<ObjectOutputStream>();
        ArrayList<ObjectInputStream> inputStreams = new ArrayList<ObjectInputStream>();
        ArrayList<Thread> clientHandlers = new ArrayList<Thread>();

        public Server() {
            try {
                serverSocket = new ServerSocket(port);
            } catch(IOException e) {
                throw new RuntimeException("Couldn't start server: %s", e);
            }

            Thread acceptConnections = new Thread(){
                public void run() {
                    try {
                        Socket curr_socket = serverSocket.accept();
                        clientSockets.add(curr_socket);
                        inputStreams.add(new ObjectInputStream(curr_socket.getInputStream()));
                        outputStreams.add(new ObjectOutputStream(curr_socket.getOutputStream()));
                        int pos = clientSockets.size()-1;
                        Thread clientHandler = new Thread() {
                            public void run() {
                                ObjectInputStream input = inputStreams.get(pos);
                                try {
                                    try {
                                        ClientMessage clientMessage = (ClientMessage) input.readObject();
                                    } catch(ClassNotFoundException e) {
                                        throw new RuntimeException("Couldn't read input class: %s", e);
                                    }
                                } catch (IOException e) {
                                    System.out.printf("Error occured or connection has been closed. Closing connection %d", pos);
                                    try {
                                        clientSockets.get(pos).close();
                                        inputStreams.get(pos).close();
                                        outputStreams.get(pos).close();
                                    } catch(IOException b) {
                                        throw new RuntimeException("Couldn't close connections: %s", b);
                                    }
                                    
                                    clientSockets.remove(pos);
                                    inputStreams.remove(pos);
                                    outputStreams.remove(pos);
                                }
                                
                            }
                        };
                        clientHandler.run();
                        clientHandlers.add(clientHandler);
                        Thread.sleep(1);
                    } catch(Exception e) {
                      throw new RuntimeException("");
                    }
                }
            };
            acceptConnections.run();
        }

        private void gotNewMessage(int client_pos, ClientMessage clientMessage) {
            for (ObjectOutputStream output : outputStreams) {

            }
        }
    }


    private class Client {
        Socket connToServer;
        OutputStream outputStream;
        InputStream inputStream;


        public Client() {
            try {
                connToServer = new Socket("localhost", port);
                outputStream = new ObjectOutputStream(connToServer.getOutputStream());
                inputStream = new ObjectInputStream(connToServer.getInputStream());
            } catch(IOException e) {
                throw new RuntimeException("Couldn't connect to server: %s", e);
            }

            sendMessage();
        }
    }

    private class ClientMessage {
        public float x;
        public float y;

        public ClientMessage(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private class ServerMessage {
        // TODO
    }

    public void sendMessage() {
        
    }
}