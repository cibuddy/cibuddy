package com.cibuddy.delcom.lights;

import java.io.IOException;

/**
 *
 * @author mirkojahn
 */
public interface IDelcomLight {
    
    public void open() throws IOException;
    
    public void close() throws IOException;
    
    void setColor(Color color) throws IOException;
    
    Color getCurrentColor();
    
    void reset() throws IOException;
    
    DeviceType getDeviceType();
}
