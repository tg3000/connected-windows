package com.tg3000.frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.tg3000.backend.ConnectionHandler;

public class Window extends JFrame {

    private static final int WIDTH = 700;
    private static final int HEIGHT = 700;

    public ConnectionHandler conHandler;

    public Window(int connection_port, String host) {
        conHandler = new ConnectionHandler(connection_port, host);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentMoved( ComponentEvent ce ) {
                Point point = ce.getComponent().getLocation();
                conHandler.client.clientPosChanged(point.getX(), point.getY());
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
