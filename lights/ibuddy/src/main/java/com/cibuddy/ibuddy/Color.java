package com.cibuddy.ibuddy;

/**
 *
 * @author mirkojahn
 */
public enum Color {

    BLACK(7), RED(6), GREEN(5), YELLOW(4), BLUE(3), PURPLE(2), CYAN(1), LIGHTBLUE(0);

    private int code;

    private Color(int c) {
        code = c;
    }

    public int getCode() {
        return code;
    }
}
