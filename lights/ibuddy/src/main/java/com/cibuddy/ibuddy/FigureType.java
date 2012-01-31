package com.cibuddy.ibuddy;

/**
 *
 * @author mirkojahn
 */
public enum FigureType {
    
    IBUDDY_GENERATION_1(1), IBUDDY_GENERATION_2(2), DEVIL(5), QUEEN(5);
    private int type;

    private FigureType(int t) {
        type = t;
    }

    public int getType() {
        return type;
    }
}
