package com.cibuddy.hid.test;

import com.codeminders.hidapi.ClassPathLibraryLoader;
import com.codeminders.hidapi.HIDDeviceInfo;
import com.codeminders.hidapi.HIDManager;
import java.io.IOException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A simple test to see if the native libraries could be loaded.
 * 
 * @author Mirko Jahn <mirkojahn@gmail.com>
 * @version 1.0
 */
@Ignore
public class HIDManagerTest {
    
    /**
     * Loads the native libraries before the test execution.
     * @since 1.0
     */
    @BeforeClass
    public static void beforeEverything() {
        // load the native libraries automatically (only once per JVM instance)
        ClassPathLibraryLoader.loadNativeHIDLibrary();
    }
    
    /**
     * Test how many devices are found if run twice.
     * 
     * Listing all devices twice and checking if the lists are identical. If not,
     * most likely one device is still occupied by the first request (Windows is
     * supposed to behave like that in some cases). You might as well have just 
     * removed/added the HID device during the test run.
     * 
     * @throws IOException in case access to the driver was not possible
     * @throws InterruptedException 
     */
    @Test
    public void testListDevices() throws IOException, InterruptedException {
        HIDManager manager = HIDManager.getInstance();
        HIDDeviceInfo[] firstRun = printDevices(manager);
        System.out.println("Second run: Checking for HID devices again...");
        HIDDeviceInfo[] secondRun = printDevices(manager);
        
        Assert.assertArrayEquals("The list of found HID devices is NOT equal. Make sure you didn't connect/disconnect a device while running the test", 
                firstRun, 
                secondRun);
        manager.release();
    }
    
    /**
     * Main method, just printing all connected devices.
     * 
     * @param args - not used.
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
