package com.cibuddy.hid.impl;

import com.cibuddy.hid.HIDServiceConstants;
import com.codeminders.hidapi.HIDDeviceInfo;
import com.codeminders.hidapi.HIDManager;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TimerTask;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HIDManagerImpl extends TimerTask{
    
    private static final Logger LOG = LoggerFactory.getLogger(HIDManagerImpl.class);
    private HashMap<String,ServiceRegistration> devices = new HashMap<String,ServiceRegistration>();
    private Set<HIDDeviceInfo> hidInfodevices = new HashSet<HIDDeviceInfo>();
    
    private final Object guard = new Object();
    private volatile boolean enabled = true;
    private HIDManager manager = null;
    
    /**
     * Creates a new <code>HIDManagerImpl</code> object.
     */
    public HIDManagerImpl() throws IOException {
        manager = HIDManager.getInstance();
    }
    
    public synchronized void updateDeviceList() throws IOException{
        HIDDeviceInfo[] newdevs = manager.listDevices();
        // some logging
        if(newdevs != null) {
            LOG.debug("Found " + newdevs.length + " devices:");
        } else {
            LOG.debug("Found 0 devices:");
        }
        // now sorting through the result
        Set<HIDDeviceInfo> newDevices = new HashSet<HIDDeviceInfo>();
        if(newdevs != null){
            newDevices.addAll(new ArrayList<HIDDeviceInfo>(Arrays.asList(newdevs)));
        }
        // first quickly check if something changed at all...
        if(hidInfodevices.equals(newDevices)){
            // nothing to do...
            return;
        }
        
        
        Set<HIDDeviceInfo> removedDevices = new HashSet<HIDDeviceInfo>();
        removedDevices.addAll(hidInfodevices);
        removedDevices.removeAll(newDevices);
        // TODO: now unregister all services for those devices
        if(removedDevices != null){
            Iterator<HIDDeviceInfo> removedDeviceIter = removedDevices.iterator();
            while (removedDeviceIter.hasNext()){
                withdrawDevice(removedDeviceIter.next());
            }
        }
        // obtain the new devices (not already known)
        Set<HIDDeviceInfo> tempNewDevices = new HashSet<HIDDeviceInfo>();
        tempNewDevices.addAll(newDevices);
        tempNewDevices.removeAll(hidInfodevices);
        if(tempNewDevices != null){
            Iterator<HIDDeviceInfo> newDeviceIter = tempNewDevices.iterator();
            while (newDeviceIter.hasNext()){
                exposeDevice(newDeviceIter.next());
            }
        }
        // set the deviceinfo set correctly
        hidInfodevices = newDevices;
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
                LOG.debug("HID - Added:" + "\n" + dev + "\n");
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
                ServiceRegistration registedService = devices.get(dev.getPath());
                if(registedService != null) {
                    registedService.unregister();
                    devices.remove(dev.getPath());
                    // calling close on the device doesn't make sence (already closed)
                    LOG.debug("HID - Removal:" + "\n" + dev + "\n");
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
            manager.release();
        }
    }

    @Override
    public void run() {
        try {
            updateDeviceList();
        } catch (IOException ex) {
            LOG.info(HIDManagerImpl.class.getName(),ex);
        }
    }
}
