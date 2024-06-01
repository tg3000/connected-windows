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
        g.drawOval(getSize().width / 2 - circleWidth / 2, getSize().height / 2 - circleHeight / 2, circleWidth, circleHeight);

        for(Coordinate coordinate : coordinates) {
            g.drawLine(getSize().width / 2, getSize().height / 2, (int) (coordinate.x - ownPos.x) + getSize().width / 2, (int) (coordinate.y - ownPos.y) + getSize().height / 2);
        }
    }
}
