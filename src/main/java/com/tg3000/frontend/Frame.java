package com.tg3000.frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.tg3000.backend.Connection;
import com.tg3000.backend.Coordinate;


public class Frame extends JFrame 
{
    Connection connection;
    Coordinate[] positions;

    public Frame (int port, String host) 
    {
        connection = new Connection(port, host);

        this.addComponentListener(new ComponentAdapter() 
        {
            public void componentMoved(ComponentEvent e) 
            {
                sendNewPos();
            }
        });

        
        setResizable(true);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Test");
        setVisible(true);
    }

    private void sendNewPos () 
    {
        Point point = this.getLocationOnScreen();
        connection.client.clientPosChanged(point.getX(), point.getY());
    }

    private void callBack(Coordinate[] allCoordinates) 
    {
        positions = allCoordinates;
    }
}
