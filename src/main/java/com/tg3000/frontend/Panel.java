package com.tg3000.frontend;

import javax.swing.JPanel;
import java.awt.Graphics;

import com.tg3000.backend.*;

// The panel inside of the JFrame that handles all drawings connecting to other Frames
public class Panel extends JPanel{
    Coordinate[] coordinates; 
    Coordinate ownPos; 
    int circleWidth = 50;
    int circleHeight = 50;


    public Panel(Coordinate ownPos, Coordinate[] coordinates) {
        super();
        this.ownPos = ownPos;
        this.coordinates = coordinates;
    }

    // Sets new positions and calls repaint, which in turn calls paintComponent 
    public void setNewPositions(Coordinate ownPos, Coordinate[] coordinates) {
        this.ownPos = ownPos;
        this.coordinates = coordinates;
        repaint();
    }

    // Draws lines and ovals
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < coordinates.length; i++) {
            Coordinate circle1Absolute = coordinates[i];
            int x1 = (int) (circle1Absolute.x - ownPos.x) + getSize().width / 2;
            int y1 = (int) (circle1Absolute.y - ownPos.y) + getSize().height / 2;

            g.drawOval(x1 - circleWidth / 2, y1 - circleHeight / 2, circleWidth, circleHeight);

            for (int j = 1; j < coordinates.length; j++) {
                Coordinate circle2Absolute = coordinates[j];
                int x2 = (int) (circle2Absolute.x - ownPos.x) + getSize().width / 2;
                int y2 = (int) (circle2Absolute.y - ownPos.y) + getSize().height / 2;

                g.drawLine(x1, y1, x2, y2);
            }
        }
    }
}
