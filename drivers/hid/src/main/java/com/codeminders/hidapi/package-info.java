/**
 * This package is copied (and slightly modified) from the javahidapi project.
 * 
 * The main differences between this and the javahidapi project are that not all
 * classes are present here and the code was enhanced on a few occasions, as well 
 * as the javadoc, to better support users of the library.
 * 
 * To interact in OSGi with USB devices, look for <code>com.codeminders.hidapi.HIDDeviceInfo</code>
 * class based services. This library will expose all identified USB devices as
 * such services, so you should not worry about how to obtain those.
 * @version 1.1
 */
package com.codeminders.hidapi;