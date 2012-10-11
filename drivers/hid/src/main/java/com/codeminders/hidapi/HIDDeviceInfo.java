package com.codeminders.hidapi;

import java.io.IOException;

/**
 * Container class which contains HID device properties.
 * 
 * An instance of this class is exposed as an OSGi Service for every connected
 * USB device. The list of devices is updated by the bundle on a regular basis.
 * The service is annotated with properties defined in the 
 * <code>com.cibuddy.hid.HIDServiceConstants</code> interface to allow for easier
 * service discovery for your specific device.
 *
 * @author lord
 * @version 1.1 
 * @since 1.1 overwritten <code>#hashcode()</code> and <code>#equals()</code> methods.
 */
public class HIDDeviceInfo
{
    private String path;
    private int    vendor_id;
    private int    product_id;
    private String serial_number;
    private int    release_number;
    private String manufacturer_string;
    private String product_string;
    private int    usage_page;
    private int    usage;
    private int    interface_number;

    /**
     * Protected constructor, used from JNI Allocates a new
     * <code>HIDDeviceInfo</code> object.
     * 
     * @since 1.0
     */
    HIDDeviceInfo()
    {
    }
    
    /** 
     * Get the platform-specific device path. 
     * 
     * A unique path for a device during a JVM instance run (restart of the JVM
     * will produce a different path), that will change when a device is detached
     * and re-attached. 
     * 
     * @return the string value
     * @since 1.0
     */
    public String getPath()
    {
        return path;
    }
    
    /** 
     * Get the device USB vendor ID. 
     * 
     * @return integer value
     * @since 1.0
     */
    public int getVendor_id()
    {
        return vendor_id;
    }
    
    /** 
     * Get the device USB product ID.
     * 
     * @return the integer value
     * @since 1.0
     */
    public int getProduct_id()
    {
        return product_id;
    }
    
    /** 
     * Get the device serial number.
     * 
     * @return the string value
     * @since 1.0
     */
    public String getSerial_number()
    {
        return serial_number;
    }
    
    /** 
     * Get the device release number in binary-coded decimal,
     * also known as device version number. 
     * 
     * @return the integer value
     * @since 1.0
     */
    public int getRelease_number()
    {
        return release_number;
    }
    
    /** 
     * Get the device manufacturer string. 
     * !Not all devices support this!
     * 
     * @return the string value
     * @since 
     */
    public String getManufacturer_string()
    {
        return manufacturer_string;
    }
    
    /** 
     * Get the device product string.
     * !Not all devices support this!
     * 
     * @return the integer value
     * @since
     */
    public String getProduct_string()
    {
        return product_string;
    }
    
    /** 
     * Get the device usage page (Windows/Mac only).
     * 
     * @return the integer value
     * @since 1.0
     */
    public int getUsage_page()
    {
        return usage_page;
    }
    
    /** 
     * Get the device usage (Windows/Mac only).
     * 
     * @return the integer value
     * @since 1.0
     */
    public int getUsage()
    {
        return usage;
    }
    
    /**
     * Get the USB interface which this logical device represents. 
     * 
     * Valid on both Linux implementations in all cases,
     * and valid on the Windows implementation only if the device
     * contains more than one interface.
     * 
     * @return the integer value
     * @since 1.0
     */
    public int getInterface_number()
    {
        return interface_number;
    }
    
    /**
     *  Open a HID device using a path name from this class.  
     *  Used from JNI.
     *
     * @return return a reference to the <code>HIDDevice</code> object
     * @throws IOException
     * @since 1.0
     */
    public native HIDDevice open() throws IOException;
    
    /**
     * Override method for conversion this object to <code>java.lang.String</code> object.
     *
     * @return return a reference to the <code>String<code> object
     * @since 1.0
     */
    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        builder.append("HIDDeviceInfo [path=");
        builder.append(path);
        builder.append(", vendor_id=");
        builder.append(vendor_id);
        builder.append(", product_id=");
        builder.append(product_id);
        builder.append(", serial_number=");
        builder.append(serial_number);
        builder.append(", release_number=");
        builder.append(release_number);
        builder.append(", manufacturer_string=");
        builder.append(manufacturer_string);
        builder.append(", product_string=");
        builder.append(product_string);
        builder.append(", usage_page=");
        builder.append(usage_page);
        builder.append(", usage=");
        builder.append(usage);
        builder.append(", interface_number=");
        builder.append(interface_number);
        builder.append("]");
        return builder.toString();
    }

    /**
     * Override of the hashcode method from <code>java.lang.Object</code>.
     * 
     * The hashcode is calculated out of path, vendor id, product id and serial 
     * number. The path alone could have been enough, but might be null, so the others have
     * been included as well.
     * 
     * @return the hashcode calculated out of path, vendor id, product id and serial number.
     * @since 1.1 new method added by Mirko Jahn
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.path != null ? this.path.hashCode() : 0);
        hash = 11 * hash + this.vendor_id;
        hash = 11 * hash + this.product_id;
        hash = 11 * hash + (this.serial_number != null ? this.serial_number.hashCode() : 0);
        return hash;
    }

    /**
     * Override of the equals method from <code>java.lang.Object</code>.
     * 
     * Similar to the <code>#hashcode()</code> method, the following parameter of
     * the object are considered: path, vendor id, product id and serial number.
     * 
     * @param obj the object to compare to.
     * @return true in case they are equal.
     * @since 1.1 new method added by Mirko Jahn
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HIDDeviceInfo other = (HIDDeviceInfo) obj;
        // might be null, so make sure this doesn't crash
        if ((this.path == null) ? (other.path != null) : !this.path.equals(other.path)) {
            return false;
        }
        if (this.vendor_id != other.vendor_id) {
            return false;
        }
        if (this.product_id != other.product_id) {
            return false;
        }
        // might be null, so make sure this doesn't crash
        if ((this.serial_number == null) ? (other.serial_number != null) : !this.serial_number.equals(other.serial_number)) {
            return false;
        }
        return true;
    }
    
}
