package com.tg3000.backend;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

public class ConnectionHandler{
    private int port;
    private String host;

    public Server server;
    public Client client;

    public ConnectionHandler(int port, String host) {
        this.port = port;
        this.host = host;
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
    
    public class Server {
        ServerSocket serverSocket = null;
        ArrayList<Socket> clientSockets = new ArrayList<Socket>();
        ArrayList<ObjectOutputStream> outputStreams = new ArrayList<ObjectOutputStream>();
        ArrayList<ObjectInputStream> inputStreams = new ArrayList<ObjectInputStream>();
        ArrayList<Coordinate> coords = new ArrayList<Coordinate>();
        ArrayList<ClientHandler> clientHandlers = new ArrayList<ClientHandler>();

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
                        int client_id = clientSockets.size()-1;

                        ClientHandler clientHandler = new ClientHandler(client_id);
                        clientHandler.start();
                        clientHandlers.add(clientHandler);
                        Thread.sleep(1);
                    } catch(Exception e) {
                        throw new RuntimeException("Server couldn't accept client socket or open streams: %s", e);
                    }
                }
            };
            acceptConnections.start();
        }

        public void closeConnection(int client_id) {
            System.out.printf("Closing connection with client_id: %d\n", client_id);
            try {
                inputStreams.get(client_id).close();
                outputStreams.get(client_id).close();
                clientSockets.get(client_id).close();
            } catch(IOException e) {
                throw new RuntimeException("Couldn't close connection: %s", e);
            }
            
            clientSockets.remove(client_id);
            inputStreams.remove(client_id);
            outputStreams.remove(client_id);
            clientHandlers.remove(client_id);

            for (ClientHandler clientHandler : clientHandlers) {
                clientHandler.updateClientID(client_id);
            }
        }

        private void sendPositions() {
            for (int i = 0; i < outputStreams.size(); i++) {
                try {
                    outputStreams.get(i).writeObject(new ServerMessage(coords, i));
                } catch(IOException e) {
                    throw new RuntimeException("Server couldn't write to client: %s", e);
                }
            }
        }

        private class ClientHandler extends Thread {
            private int client_id;

            public ClientHandler(int client_id) {
                super();
                this.client_id = client_id;
            }

            @Override
            public void run() {
                ObjectInputStream input = inputStreams.get(client_id);
                try {
                    ClientMessage clientMessage = (ClientMessage) input.readObject();
                    if (coords.size() >= client_id) {
                        coords.add(clientMessage.coord);
                    } else {
                        coords.set(client_id, clientMessage.coord);
                    }
                    sendPositions();
                } catch(ClassNotFoundException e) {
                    throw new RuntimeException("Couldn't read input class: %s", e);
                } catch (IOException e) {
                    System.out.printf("Error occured or connection has been closed (%s)\n", e);
                    closeConnection(client_id);
                    this.interrupt();
                }
            }
            
            // If a client ID from before has been deleted, update current client_id (client_id is used as an index in ClientList)
            public void updateClientID(int deleted_client_id) {
                if (deleted_client_id < client_id) client_id -= 1;
            }
        }
    }


    public class Client{
        private Socket connToServer;

        private ObjectOutputStream outputStream;

        private ObjectInputStream inputStream;
        private Thread inputHandler;

        public Client() {
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
                inputHandler = new Thread() {
                    public void run() {
                        try {
                            gotNewMessage((ServerMessage) inputStream.readObject());
                        } catch(ClassNotFoundException e) {
                            throw new RuntimeException("ClassNotFoundException in at client: %s\n", e);
                        } catch(IOException e) {
                            System.out.printf("Closing client connection: %s\n", e);
                            closeConnection();
                            inputHandler.interrupt();
                        }
                    }
                };
                inputHandler.start();
            } catch (IOException e) {
                throw new RuntimeException("Client couldn't open input stream: %s\n", e);
            }
        }

        public void closeConnection() {
            System.out.printf("Closing connection\n");
            try {
                outputStream.close();
                inputStream.close();
                connToServer.close();
            } catch(IOException e) {
                throw new RuntimeException("Couldn't close connection to server or streams: %s", e);
            }
        }

        private void gotNewMessage(ServerMessage message) {
            // @ann1k43 und @E-Hippo euer job :D
            System.out.println("Something moved :O");
        }

        public void clientPosChanged(double x, double y) {
            try {
                outputStream.writeObject(new ClientMessage(x, y));
            } catch(IOException e) {
                closeConnection();
            }
        }
    }
}