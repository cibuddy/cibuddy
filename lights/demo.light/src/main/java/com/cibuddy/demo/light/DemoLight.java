package com.cibuddy.demo.light;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.UIManager;


public class DemoLight extends JFrame {

    private Container content;
    private Light oldPanel;
    public final static Light BLUE_LIGHT = new BlueLight();
    public final static Light RED_LIGHT = new RedLight();
    public final static Light YELLOW_LIGHT = new YellowLight();
    public final static Light GREEN_LIGHT = new GreenLight();   
    public final static Light WHITE_LIGHT = new WhiteLight();    

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
        content.setBackground(Color.white);
        // set the "off" light
        updateCircle(WHITE_LIGHT);
    }

    public final void updateCircle(Light panel) {
        if (oldPanel != null) {
            content.remove(oldPanel);
        }
        oldPanel = panel;
        content.add(panel, BorderLayout.WEST);
        pack();
        setVisible(true);
        repaint();
    }
    
    public void turnOff(){
        updateCircle(WHITE_LIGHT);
    }
    
    /**
     * Obtain the color of the light (black in case the light is off).
     * 
     * @return color of the light.
     */
    public Color getLightColor(){
        // this should never be null (set in the constructor)
        return oldPanel.getLightColor();
    }
}
class ExitListener extends WindowAdapter {

    @Override
    public void windowClosing(WindowEvent event) {
        //System.exit(0);
    }
}
