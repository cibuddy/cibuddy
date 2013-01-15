package com.cibuddy.hid;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDeviceInfo;
import com.codeminders.hidapi.HIDManager;
import java.io.IOException;

/**
 * A simple test to see if the native libraries could be loaded.
 * 
 * @author Mirko Jahn <mirkojahn@gmail.com>
 * @version 1.0
 */
public class HIDFunctionalTest {
    
    /**
     * Loads the native libraries before test execution.
     * @since 1.0
     */
    static {
        // load the native libraries automatically (only once per JVM instance)
        ClassPathLibraryLoader.loadNativeHIDLibrary();
    }
    
    /**
     * Main method, just printing all connected devices.
     * 
     * @param args - not used.
     */
    public static void main(String[] args) throws IOException {
        // Create an instance of the manager used to handle the HID devices
        HIDManager manager = HIDManager.getInstance();
        // print a list with all connected HID devices
        printDevices(manager);
        // we're in the native world, make sure we clean-up
        manager.release();
    }
    
    /**
     * Print a list of connected HID devices.
     * 
     * @param manager - HIDManager instance to use
     * @return List of connected HID devices
     * @throws IOException in case access to the device didn't work.
     */
    private static HIDDeviceInfo[] printDevices(HIDManager manager) throws IOException{
        HIDDeviceInfo[] devs = manager.listDevices();
        System.out.println("HID Devices:\n\n");
        for(int i=0;i<devs.length;i++)
        {
            System.out.println(""+i+".\t"+devs[i]);
            System.out.println("---------------------------------------------\n");
        }
        System.gc();
        return devs;
    }
}
