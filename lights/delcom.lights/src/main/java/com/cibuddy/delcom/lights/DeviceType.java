package com.cibuddy.delcom.lights;

/**
 *
 * @author mirkojahn
 */
public enum DeviceType {
    G1("4643"),// 0x1223 http://www.delcomproducts.com/downloads/USBVIDM.pdf 
    G2("45184"); // http://www.delcomproducts.com/downloads/USBVIHID.pdf
    
    private String type;

    private DeviceType(String t) {
        type = t;
    }

    public String getType() {
        return type;
    }
}
