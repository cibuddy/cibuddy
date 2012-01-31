package com.cibuddy.delcom.lights;

/**
 *
 * @author mirkojahn
 */
public enum Color {
    BLACK((byte)0xFF), 
    GREEN((byte)0xFE), 
    RED ((byte)0xFD), 
    YELLOW((byte)0xFB), // depending on device might also be blue
    BLUE((byte)0xFB); // depending on device might also be yellow
    
    private byte code;

    private Color(byte c) {
        code = c;
    }

    public byte getCode() {
        return code;
    }
}
