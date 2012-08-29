package com.codeminders.hidapi;

import java.io.IOException;

/**
 * Thrown if HID Device with given criteria could not be found
 *
 * @author lord
 * @version 1.0
 */
public class HIDDeviceNotFoundException extends IOException
{
    /**
     * Constructs a <code>HIDDeviceNotFoundException</code> with no detailed error message.
     * @since 1.0
     */
    public HIDDeviceNotFoundException()
    {
    }
    
    /**
     * Constructs a <code>HIDDeviceNotFoundException</code> with the specified error message.
     * @since 1.0
     */
    public HIDDeviceNotFoundException(String message)
    {
        super(message);
    }
}