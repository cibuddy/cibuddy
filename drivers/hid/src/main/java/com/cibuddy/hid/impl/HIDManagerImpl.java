package com.cibuddy.hid.impl;

import com.cibuddy.hid.HIDServiceConstants;
import com.codeminders.hidapi.HIDDeviceInfo;
import com.codeminders.hidapi.HIDManager;
import java.io.IOException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HIDManagerImpl extends HIDManager {
    
    private static final Logger LOG = LoggerFactory.getLogger(HIDManagerImpl.class);
    private HashMap<String,ServiceRegistration> devices = new HashMap<String,ServiceRegistration>();
    
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
            LOG.debug("Found " + devs.length + " devices:");
        } else {
            LOG.debug("Found 0 devices:");
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
                Dictionary<String,?> dict = new Hashtable<String,String>();
                safePut(dict,HIDServiceConstants.VENDOR_ID, Integer.valueOf(dev.getVendor_id()).toString());
                safePut(dict,HIDServiceConstants.PRODUCT_ID, Integer.valueOf(dev.getProduct_id()).toString());
                safePut(dict,HIDServiceConstants.SERIAL_NUMBER, dev.getSerial_number());
                safePut(dict,HIDServiceConstants.MANUFACTURER_STRING, dev.getManufacturer_string());
                safePut(dict,HIDServiceConstants.PRODUCT_STRING, dev.getProduct_string());
                safePut(dict,HIDServiceConstants.RELEASE_NUMBER, Integer.valueOf(dev.getRelease_number()).toString());
                safePut(dict,HIDServiceConstants.INTERFACE_NUMBER, Integer.valueOf(dev.getInterface_number()).toString());
                safePut(dict,HIDServiceConstants.PATH, dev.getPath());
                safePut(dict,HIDServiceConstants.USAGE, Integer.valueOf(dev.getUsage()).toString());
                safePut(dict,HIDServiceConstants.USAGE_PAGE, Integer.valueOf(dev.getUsage_page()).toString());
                ServiceRegistration registedService = Activator.getContext().registerService(HIDDeviceInfo.class.getName(), dev, dict);
                devices.put(dev.getPath(), registedService);
                LOG.debug("Added:" + "\n" + dev + "\n");
            }
        }
    }
    
    private void safePut(Dictionary dict, String key, Object value) {
        if(key != null && value != null){
            dict.put(key, value);
        } else if(key != null){
            dict.put(key, "");
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
                    System.out.print("HID - Removal:" + "\n" + dev + "\n");
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
    public void deviceAdded( HIDDeviceInfo dev )
    {
       System.out.print("HID - Added:" + "\n" + dev + "\n");
       exposeDevice(dev);
    }
    
    /**
     * Callback method which will be called when HID device is
     * disconnected.
     *
     * @param dev Reference to the <code>HIDDeviceInfo</code> object.
     * @throws IOException
     */
    public void deviceRemoved( HIDDeviceInfo dev)
    {
        System.out.print("HID - Removal:" + "\n" + dev + "\n");
        withdrawDevice(dev);
    }
    
    public void shutdown() {
        if(enabled){
            enabled = false;
            Iterator<ServiceRegistration> iter = devices.values().iterator();
            while(iter.hasNext()){
                ServiceRegistration next = iter.next();
                if(next != null) {
                    next.unregister();
                }
            }
        }
    }
}
