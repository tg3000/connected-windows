package com.tg3000.frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.tg3000.backend.Connection;

public class Window extends JFrame {

    private static final int WIDTH = 700;
    private static final int HEIGHT = 700;

    public Connection connection;

    public Window(int connection_port, String host) {
        connection = new Connection(connection_port, host);


        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved( ComponentEvent ce ) {
                Point point = ce.getComponent().getLocation();
                connection.client.clientPosChanged(point.getX(), point.getY());
            }
        });
        setWindowProperties();
    }

    private void setWindowProperties() {
        setResizable(true);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Test");
        setVisible(true);
    }

}
