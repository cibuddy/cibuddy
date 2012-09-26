/*
 * Copyright (C) 2012 Mirko Jahn <mirkojahn@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cibuddy.hid;

/**
 * Constants used to annotate exposed <code>com.codeminders.hidapi.HIDDeviceInfo</code> services.
 * 
 * @author mirkojahn
 * @version 1.0
 */
public interface HIDServiceConstants {
    /** 
     * The product string exposed by the USB device 
     * 
     * @since 1.0
     */
    String PRODUCT_STRING = "product.string";
    /** 
     * The manufacturer string exposed by the USB device 
     * 
     * @since 1.0
     */
    String MANUFACTURER_STRING = "manufacturer.string";
    /** 
     * The vendor id exposed by the USB device 
     * 
     * @since 1.0
     */
    String VENDOR_ID = "vendor.id";
    /** 
     * The product id exposed by the USB device 
     * 
     * @since 1.0
     */
    String PRODUCT_ID = "product.id";
    /** 
     * The serial number exposed by the USB device.
     * 
     * I've never seen this exposed by any device so far, but this might 
     * change in the future or is just related to the tested devices, not sure. 
     * 
     * @since 1.0
     */
    String SERIAL_NUMBER = "serial.number";
    /** 
     * The release number exposed by the USB device.
     * 
     * @since 1.0
     */
    String RELEASE_NUMBER = "release.number";
    /** 
     * The interface number exposed by the USB device.
     * 
     * This could very well be a negative number!!! 
     * 
     * @since 1.0
     */
    String INTERFACE_NUMBER = "interface.number";
    /** 
     * The unique path of the USB device.
     * 
     * This path is unique for a device connection during the existence of the 
     * JVM. A restart or a re-attachment of the USB device will always change the
     * path name/string, so it is not a good idea to use this one for uniquely
     * identifying a device throughout the restart of the application!
     * 
     * @since 1.0
     */
    String PATH = "path";
    /** 
     * The usage count of an USB device.
     * 
     * This does not always work. For instance on Linux systems, this does not 
     * indicate the actual usage count! You've been warned!
     * 
     * @since 1.0
     */
    String USAGE = "usage";
    /** 
     * The usage page exposed by the USB device.
     * 
     * The same problem as with the <code>USAGE</code> applies here as well.
     * 
     * @since 1.0
     */
    String USAGE_PAGE = "usage.page";
                
}
