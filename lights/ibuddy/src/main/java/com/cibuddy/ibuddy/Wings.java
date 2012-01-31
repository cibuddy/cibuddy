package com.cibuddy.ibuddy;

/**
 *
 * @author mirkojahn
 */
public enum Wings {
    UP(1), DOWN(2), NEUTRAL(3);
    private int code;

    private Wings(int c) {
        code = c;
    }

    public int getCode() {
        return code;
    }
}
