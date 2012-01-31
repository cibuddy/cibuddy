package com.cibuddy.hid.impl;

import com.cibuddy.hid.HIDServiceConstants;
import com.codeminders.hidapi.HIDDeviceInfo;
import com.codeminders.hidapi.HIDManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import org.osgi.framework.ServiceRegistration;

public class HIDManagerImpl extends HIDManager {
    
    private HashMap<String,ServiceRegistration<HIDDeviceInfo>> devices = new HashMap<String,ServiceRegistration<HIDDeviceInfo>>();
    
    private final Object guard = new Object();
    private boolean enabled = true;
    /**
     * Creates a new <code>HIDManagerTest</code> object.
     */
    public HIDManagerImpl() throws IOException {
        super();
               
    }
    
    public void setup() throws IOException{
        HIDDeviceInfo[] devs = listDevices();
        if(devs != null) {
//            System.out.print("Found " + devs.length + " devices:");
        } else {
//            System.out.print("Found 0 devices:");
        }
        if(devs != null){
            for (int i=0;i<devs.length;i++){
                exposeDevice(devs[i]);
            }
        }
    }
    
    private void exposeDevice(HIDDeviceInfo dev) {
        if(enabled){
            synchronized(guard){
                Hashtable<String,String> dict = new Hashtable<String,String>();
                dict.put(HIDServiceConstants.VENDOR_ID, Integer.valueOf(dev.getVendor_id()).toString());
                dict.put(HIDServiceConstants.PRODUCT_ID, Integer.valueOf(dev.getProduct_id()).toString());
                dict.put(HIDServiceConstants.SERIAL_NUMBER, dev.getSerial_number());
                dict.put(HIDServiceConstants.MANUFACTURER_STRING, dev.getManufacturer_string());
                dict.put(HIDServiceConstants.PRODUCT_STRING, dev.getProduct_string());
                dict.put(HIDServiceConstants.RELEASE_NUMBER, Integer.valueOf(dev.getRelease_number()).toString());
                dict.put(HIDServiceConstants.INTERFACE_NUMBER, Integer.valueOf(dev.getInterface_number()).toString());
                dict.put(HIDServiceConstants.PATH, dev.getPath());
                dict.put(HIDServiceConstants.USAGE, Integer.valueOf(dev.getUsage()).toString());
                dict.put(HIDServiceConstants.USAGE_PAGE, Integer.valueOf(dev.getUsage_page()).toString());
                ServiceRegistration<HIDDeviceInfo> registedService = Activator.getContext().registerService(HIDDeviceInfo.class, dev, dict);
                devices.put(dev.getPath(), registedService);
//                System.out.print("Added:" + "\n" + dev + "\n");
            }
        }
    }
    
    private void withdrawDevice(HIDDeviceInfo dev) {
        if(enabled){
            synchronized(guard){
                ServiceRegistration<HIDDeviceInfo> registedService = devices.get(dev.getPath());
                if(registedService != null) {
                    registedService.unregister();
                    devices.remove(dev.getPath());
                    // calling close on the device doesn't make sence (already closed)
                    System.out.print("Removal:" + "\n" + dev + "\n");
                }
            }
        }
    }
    
    /**
     * Callback method which will be called when HID device is
     * connected.
     *
     * @param dev Reference to the <code>HIDDeviceInfo</code> object.
     * @throws IOException
     */
    @Override
    public void deviceAdded( HIDDeviceInfo dev )
    {
       System.out.print("XXX Added:" + "\n" + dev + "\n");
       exposeDevice(dev);
    }
    
    /**
     * Callback method which will be called when HID device is
     * disconnected.
     *
     * @param dev Reference to the <code>HIDDeviceInfo</code> object.
     * @throws IOException
     */
    @Override
    public void deviceRemoved( HIDDeviceInfo dev)
    {
        System.out.print("ZZZ Removal:" + "\n" + dev + "\n");
        withdrawDevice(dev);
    }
    
    public void shutdown() {
        if(enabled){
            enabled = false;
            Iterator<ServiceRegistration<HIDDeviceInfo>> iter = devices.values().iterator();
            while(iter.hasNext()){
                ServiceRegistration<HIDDeviceInfo> next = iter.next();
                if(next != null) {
                    next.unregister();
                }
            }
        }
    }
}
