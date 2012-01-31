package com.cibuddy.ibuddy;

/**
 *
 * @author mirkojahn
 */
public enum Turn {
    LEFT(1), RIGHT(2), NEUTRAL(3);
    private int code;

    private Turn(int c) {
        code = c;
    }

    public int getCode() {
        return code;
    }
}
