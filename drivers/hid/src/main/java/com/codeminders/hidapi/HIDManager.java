package com.codeminders.hidapi;

import java.io.IOException;

/**
 * This class is only used internally.
 * 
 * Do not use this class! The individual devices are exposed as services in your
 * OSGi environment!
 * 
 * @since 
 * 1.1 (actually NOT backwards compatible, but this was not my decision, but the original authors):
 * <ul>
 *  <li>Add and remove listener method stubs have been removed</li>
 *  <li>The class became a singleton instead of an abstract class</li>
 * </ul>
 *
 * @version 1.1
 * @author lord
 *
 */
public class HIDManager {

    private volatile static HIDManager instance = null;
    protected long peer;

    /**
     * Get list of all the HID devices attached to the system.
     *
     * @return list of devices
     * @throws IOException
     * @since 1.0
     */
    public native HIDDeviceInfo[] listDevices() throws IOException;

    /**
     * Initializing the underlying HID layer.
     *
     * @throws IOException
     * @since 1.0
     */
    private native void init() throws IOException;

    /**
     * Release underlying HID layer. 
     * 
     * This method must be called when <code>HIDManager</code> object is no 
     * longer needed. Failure to do so could cause memory leaks or unterminated 
     * threads. It is safe to call this method multiple times.
     *
     * @since 1.0
     */
    public native void release();

    /**
     * Constructor to create HID object manager. 
     * 
     * It must be invoked from subclass constructor to ensure proper initialization.
     * In version 1.1 of this class the constructor became private.
     *
     * @throws IOException exception when trying to access device information and
     * initializing native USB driver access.
     * @since 1.0 new method<br/>
     *        1.1 access changed to private
     */
    private HIDManager() throws IOException {
        init();
    }

    /**
     * Release HID manager. 
     * 
     * Will call release() when the JVM tries to clean-up this class. This method
     * should never be called directly, instead use the <codo>#release()</code>
     * method.
     *
     * @throws Throwable any problem that might occur when trying to free usb handles.
     * @since 1.0
     */
    protected void finalize() throws Throwable {
        // It is important to call release() if user forgot to do so,
        // since it frees pointer internal data structures and stops
        // thread under MacOS
        try {
            release();
        } finally {
            super.finalize();
        }
    }

    /**
     * Convenience method to find and open device by path.
     * 
     * This method internally calls <code>#listDevices()</code> and enumerates 
     * through the device list to obtain the device matching the path.
     *
     * @param path USB device path
     * @return open device reference <code>HIDDevice</code> object
     * @throws IOException in case of internal error
     * @throws HIDDeviceNotFoundException if devive was not found
     * @since 1.0
     */
    public HIDDevice openByPath(String path) throws IOException, HIDDeviceNotFoundException {
        HIDDeviceInfo[] devs = listDevices();
        for (HIDDeviceInfo d : devs) {
            if (d.getPath().equals(path)) {
                return d.open();
            }
        }
        throw new HIDDeviceNotFoundException();
    }

    /**
     * Convenience method to open a HID device using a Vendor ID (VID), Product
     * ID (PID) and optionally a serial number.
     * 
     * This method might not return the device you're looking for in case there
     * are multiple devices attached with the same parameters. The first one 
     * found will be returned. In case you want to address a specific device, try
     * using the USB path of the device.
     *
     * @param vendor_id USB vendor ID
     * @param product_id USB product ID
     * @param serial_number USB device serial number (could be <code>null</code>)
     * @return open device
     * @throws IOException in case of internal error
     * @throws HIDDeviceNotFoundException if devive was not found
     * @since 1.0
     */
    public HIDDevice openById(int vendor_id, int product_id, String serial_number) throws IOException, HIDDeviceNotFoundException {
        HIDDeviceInfo[] devs = listDevices();
        for (HIDDeviceInfo d : devs) {
            if (d.getVendor_id() == vendor_id && d.getProduct_id() == product_id
                    && (serial_number == null || d.getSerial_number().equals(serial_number))) {
                return d.open();
            }
        }
        throw new HIDDeviceNotFoundException();
    }

    /**
     * Get an instance of this singleton class (DON'T DO IT!!!).
     * 
     * You will never have to use this one, because everything is done for you 
     * in an OSGi environment. Just use the individual devices exposed as 
     * <code>com.codeminders.hidapi.HIDDeviceInfo</code> services in the OSGi 
     * registry.
     * 
     * @return an instance of this class
     * @throws IOException internal initialization exception.
     * @since 1.1 New method.
     */
    public static HIDManager getInstance() throws IOException {
        if (instance == null) {
            synchronized (HIDManager.class) {
                if (null == instance) {
                    instance = new HIDManager();
                }
            }
        }
        return instance;
    }
}
