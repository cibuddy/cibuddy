/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cibuddy.ibuddy;

import com.codeminders.hidapi.HIDDeviceInfo;

/**
 *
 * @author mirkojahn
 */
public class IBuddyDevil extends IBuddyDefault {
    
    public static final short DEVICE_PRODUCT = 0x0005;
    
    public IBuddyDevil (HIDDeviceInfo devInfo){
        super(devInfo);
    }
}
