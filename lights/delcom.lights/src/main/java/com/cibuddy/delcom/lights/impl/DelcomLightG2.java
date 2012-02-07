/*
 * 
 */
package com.cibuddy.delcom.lights.impl;

import com.cibuddy.delcom.lights.Color;
import com.cibuddy.delcom.lights.DeviceType;
import com.cibuddy.delcom.lights.IDelcomLight;
import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDDeviceInfo;
import java.io.IOException;

/**
 *
 * @author mirkojahn
 */
public class DelcomLightG2 implements IDelcomLight{

    // suitable for G1 and G2
    private final byte [] SET_STRUCTURE = new byte[] { (byte)0x65, (byte)0x02,
            (byte) 0x00, (byte) 0x00,
            (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00};
    private final int SET_BYTE = 2;
    
    // object specific immutable:
    private final HIDDeviceInfo deviceInfo;
    
    // object state specific
    private boolean enabled = false;
    private Color currentColor = Color.BLACK;
    private HIDDevice device;
    
    public DelcomLightG2(HIDDeviceInfo devInfo) {
        deviceInfo = devInfo;
        enabled = true;
    }
    
    @Override
    public void setColor(Color color) throws IOException{
        if(!enabled) {
            return;
        }
        byte[] buffer = SET_STRUCTURE.clone();
        buffer[SET_BYTE] = color.getCode();
        device.write(buffer);
        currentColor = color;
    }

    @Override
    public Color getCurrentColor() {
        return currentColor;
    }

    @Override
    public void reset() throws IOException {
        if(!enabled) {
            return;
        }
        setColor(Color.BLACK);
    }

    @Override
    public void open() throws IOException {
        if(enabled) {
            device = deviceInfo.open();
        }
    }

    @Override
    public void close() throws IOException {
        if(!enabled) {
            return;
        }
        device.close();
        enabled = false;
    }

    @Override
    public DeviceType getDeviceType() {
        return DeviceType.G2;
    }
    
}
