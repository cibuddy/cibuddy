package com.cibuddy.delcom.lights;

/**
 *
 * @author mirkojahn
 */
public enum Color {
    BLACK((byte)0xFF), 
    GREEN((byte)0xFE), // depending on device might also be blue
    RED ((byte)0xFD), 
    YELLOW((byte)0xFB), 
    BLUE((byte)0xFE); // depending on device might also be green
    
    private byte code;

    private Color(byte c) {
        code = c;
    }

    public byte getCode() {
        return code;
    }
}
