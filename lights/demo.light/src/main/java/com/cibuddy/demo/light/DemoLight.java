package com.cibuddy.demo.light;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;


public class DemoLight extends JFrame {

    private Container content;
    private JPanel oldPanel;

    public DemoLight() {
        super("Jenkins Status Light Indicator");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Error setting native Look and Feel: " + e);
        }
        addWindowListener(new ExitListener());
        content = getContentPane();
        content.setPreferredSize(new Dimension(400, 400));
        content.setBackground(Color.lightGray);
        content.setBackground(Color.white);
        JPanel drawingArea = new RedLight();
        //content.add(drawingArea, BorderLayout.WEST);
        pack();
        setVisible(true);
    }

    public void updateCircle(Light panel) {
        if (oldPanel != null) {
            content.remove(oldPanel);
        }
        oldPanel = panel;
        content.add(panel, BorderLayout.WEST);
        pack();
        setVisible(true);
    }
    
    public void turnOff(){
        if (oldPanel != null) {
            content.remove(oldPanel);
        }
        oldPanel = null;
        pack();
        setVisible(true);
    }
}
class ExitListener extends WindowAdapter {

    @Override
    public void windowClosing(WindowEvent event) {
        //System.exit(0);
    }
}
