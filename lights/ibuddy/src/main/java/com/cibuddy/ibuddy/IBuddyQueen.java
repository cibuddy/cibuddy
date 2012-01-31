package com.cibuddy.ibuddy;

import com.codeminders.hidapi.HIDDeviceInfo;

/**
 *
 * @author mirkojahn
 */
public class IBuddyQueen extends IBuddyDefault {
    
    public static final short DEVICE_PRODUCT = 0x0006;
    
    public IBuddyQueen (HIDDeviceInfo devInfo){
        super(devInfo);
    }
}