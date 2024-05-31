package com.tg3000.frontend;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.ont.Graphics;
import java.ont.color.*;
public class Panel extends JPanel{
    Coordinate[] coordinates; 
    Coordinate myPos; 

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        g.drawOval(300, 300, 50, 50);
        for(Coordinate coordinate : coordinates)
        {
            g.drawLine(0, 0, coordinate.x-myPos.x, coordinate.y-myPos.y);
        }
        
    }

    public Panel(Coordinate myPos, Coordinate[] coordinates)
    {
        this.myPos = myPos;
        this.coordinates = coordinates;
    }

    public void setNewPositions(Coordinate myPos, Coordinate[] coordinates)
    {
        this.myPos = myPos;
        this.coordinates = coordinates;
        repaint();
    }
}
