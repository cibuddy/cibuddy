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

/**
 * Main class for handling USB devices.
 * 
 * You should never ever have to interact with this class. Instead, you should
 * be using the exposed <code>com.codeminders.hidapi.HIDDeviceInfo</code> OSGi
 * services exposed by this class.
 * 
 * @author Mirko Jahn <mirkojahn@gmail.com>
 * @version 1.0
 */
public class HIDManagerImpl extends TimerTask{
    
    private static final Logger LOG = LoggerFactory.getLogger(HIDManagerImpl.class);
    private HashMap<String,ServiceRegistration> devices = new HashMap<String,ServiceRegistration>();
    private Set<HIDDeviceInfo> hidInfodevices = new HashSet<HIDDeviceInfo>();
    
    private final Object guard = new Object();
    private volatile boolean enabled = true;
    private HIDManager manager = null;
    
    /**
     * Creates a new <code>HIDManagerImpl</code> object.
     * 
     * @since 1.0
     */
    public HIDManagerImpl() throws IOException {
        manager = HIDManager.getInstance();
    }
    
    /**
     * Updates the internal list of exposed USB devices.
     * 
     * It is safe to call this method several times. In fact, it is called 
     * internally by a timer on a regular basis.
     * 
     * @throws IOException In case access to the usb driver (the USB devices
     *      in particular) doesn't work, an Exception is propagated.
     * 
     * @since 1.0
     */
    public synchronized void updateDeviceList() throws IOException {
        HIDDeviceInfo[] newdevs = manager.listDevices();
        // some logging
        if(newdevs != null) {
            LOG.debug("Found {} devices:", newdevs.length);
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
    
    /**
     * Helper method that exposes a given device as an OSGi service.
     * 
     * @param dev the device to expose as a service
     * 
     * @since 1.0
     */
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
    
    /**
     * Null safe Helper method for setting properties.
     * 
     * @param dict Dictionary to put properties to.
     * @param key The key to use
     * @param value The value to use
     * 
     * @since 1.0
     */
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
     * 
     * @since 1.0
     */
    protected void deviceAdded( HIDDeviceInfo dev )
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
     * 
     * @since 1.0
     */
    protected void deviceRemoved( HIDDeviceInfo dev)
    {
        System.out.print("HID - Removal:" + "\n" + dev + "\n");
        withdrawDevice(dev);
    }
    
    /**
     * Removes all exposed devices and released obtained resources.
     * 
     * This method is called by the OSGi environment (through the Activator, so 
     * you do not have to interact with this one).
     * @since 1.0
     */
    protected void shutdown() {
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

    /**
     * Updates the list of USB devices.
     * 
     * @since 1.0
     */
    @Override
    public void run() {
        try {
            updateDeviceList();
        } catch (IOException ex) {
            LOG.info(HIDManagerImpl.class.getName(),ex);
        }
    }
}
