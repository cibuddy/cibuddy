package com.codeminders.hidapi;

import java.io.IOException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Mirko Jahn <mirkojahn@gmail.com>
 */
@Ignore
public class HIDManagerTest {
    
    @BeforeClass
    public static void beforeEverything() {
        ClassPathLibraryLoader.loadNativeHIDLibrary();
    }
    
    @Test
    public void testListDevices() throws IOException, InterruptedException {
        HIDManager manager = HIDManager.getInstance();
        HIDDeviceInfo[] devs = manager.listDevices();
        System.out.println("Devices:\n\n");
        for(int i=0;i<devs.length;i++)
        {
            System.out.println(""+i+".\t"+devs[i]);
            System.out.println("---------------------------------------------\n");
        }
        System.gc();
        System.out.println("Checking for USB devices again...");
        HIDDeviceInfo[] devs2 = manager.listDevices();
        System.out.println("Devices2:\n\n");
        for(int i=0;i<devs2.length;i++)
        {
            System.out.println(""+i+".\t"+devs2[i]);
            System.out.println("---------------------------------------------\n");
        }
        System.gc();
        Assert.assertArrayEquals("The list of found USB devices is NOT equal. Make sure you didn't connect/disconnect a device while running the test", 
                devs, 
                devs2);
        manager.release();
    }
}
