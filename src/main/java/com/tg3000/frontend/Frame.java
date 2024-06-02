package com.tg3000.frontend;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import com.tg3000.backend.*;

// The Frame that the user gets to see
public class Frame extends JFrame {
    Connection connection;
    Coordinate[] positions;
    Coordinate ownPos;
    Panel panel;

    public static final int WIDTH = 600;
    public static final int HEIGHT = 600;

    public Frame(int port, String host) {
        super();

        // Add FrameMover to Listeners
        FrameMover frameMover = new FrameMover(this);
        this.addMouseListener(frameMover);
        this.addMouseMotionListener(frameMover);

        positions = new Coordinate[0];
        ownPos = new Coordinate(0,0);

        // Create panel on which everything will be drawn and set JFrame properties
        panel = new Panel(ownPos, positions);
        add(panel);
        setResizable(false);
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);
        setVisible(true);

        // Sets the "positions" Array and "ownPos" Coordinate to the new values, retrieved from server
        CallBack callback = (positions, ownPos) -> {
            this.positions = positions;
            this.ownPos = ownPos;
            panel.setNewPositions(ownPos, positions);
        };
        connection = new Connection(port, host, callback);
        sendNewPos();

        // Listens for JFrame movement
        this.addComponentListener(new ComponentAdapter() {
            public void componentMoved(ComponentEvent e) {
                sendNewPos();
            }
        });
    }

    // Give this Frames changed position to the connectionHandler which sends it to the server
    private void sendNewPos() {
        Point point = this.getLocationOnScreen();
        connection.client.clientPosChanged(point.getX(), point.getY());
    }

    // Callback function to handle messages from server containing data about the positions of other frames
    public interface CallBack {
        public void call(Coordinate[] positions, Coordinate ownPos);
    }

    // Mouse Listener to be able to move the JFrame without a Titlebar
    static class FrameMover extends MouseAdapter{
        JFrame jframe;
        private Point mouseDownCompCoords = null;

        public FrameMover(JFrame jframe) {
            super();
            this.jframe = jframe;
        }

        public void mouseReleased(MouseEvent e) {
            mouseDownCompCoords = null;
        }

        public void mousePressed(MouseEvent e) {
            mouseDownCompCoords = e.getPoint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Point currCoords = e.getLocationOnScreen();
            jframe.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
        }
    }
}
