package com.cibuddy.demo.light;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *
 * "Mirko Jahn" <mirkojahn@gmail.com>
 * @version 1.0
 */
public class Light extends JPanel {
    
    private Color color;

    public Light(Color color) {
        this.color = color;
        setPreferredSize(new Dimension(400, 400));
        setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        setBackground(Color.white);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Get the drawing area 
        int dy = getSize().height;
        int dx = getSize().width;
        int mid_y = dy / 2;
        int mid_x = dx / 2;
        // Set current drawing color
        g.setColor(color);
        g.fillOval(mid_x - 180, mid_y - 180, 350, 350);

    }
}

class RedLight extends Light {
    public RedLight(){
        super(Color.RED);
    }
}


class BlueLight extends Light {
    public BlueLight(){
        super(Color.BLUE);
    }
}


class GreenLight extends Light {
    public GreenLight(){
        super(Color.GREEN);
    }
}

class YellowLight extends Light {
    public YellowLight(){
        super(Color.YELLOW);
    }
}