package com.cibuddy.ibuddy;

import java.io.IOException;

/**
 *
 * @author mirkojahn
 */
public interface IBuddyFigure {

    short DEVICE_VENDOR = 0x1130;
    byte ALL_OFF = (byte) 0xff;
    
    byte[] SET_COMMAND = new byte[]{ (byte) 0x00, 
        (byte) 0x55, (byte) 0x53, (byte) 0x42, (byte) 0x43, 
        (byte) 0x00, (byte) 0x40, (byte) 0x02, (byte) 0x00};
    
    byte [] SETUP_COMMAND = new byte[] { (byte) 0x00,
        (byte) 0x22, (byte) 0x09, (byte) 0x00, (byte) 0x02, 
        (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00};
    
    void close() throws IOException;

    void open() throws IOException;

    void resetEverything();

    void setCurrentColor(Color currentColor);

    void setHeart(boolean heart);
}
