/**
 * This package is copied (and slightly modified) from the <a title="javahidapi homepage" href="http://code.google.com/p/javahidapi/">javahidapi project</a>.
 * 
 * <p>
 * The main differences between this and the javahidapi project are that not all
 * classes are present here and the code was enhanced on a few occasions, as well 
 * as the javadoc, to better support users of the library.
 * </p>
 * 
 * <p>
 * To interact in OSGi with USB devices, look for <code>com.codeminders.hidapi.HIDDeviceInfo</code>
 * class based services. This library will expose all identified USB devices as
 * such services, so you should not worry about how to obtain those.
 * </p>
 * 
 * <b>Attention!</b>
 * <p>
 * In order to use the native code you have to ensure the proper environment is
 * available on you operating system. In case of Windows and Mac OS X, you should
 * be good to go. However on Linux libusb and others in a specific version are
 * required. Check these links to ensure everything works as expected:
 * <ul>
 *  <li><a title="javahidapi homepage" href="http://code.google.com/p/javahidapi/">javahidapi project</a></li>
 *  <li><a title="libusb homepage" href="http://www.libusb.org/">libusb homepage</a></li>
 *  <li><a title="libusb github page" href="https://github.com/signal11/hidapi">libusb github page</a></li>
 * </ul>
 * </p>
 * @version 1.1 Changed various internal details like the way how notifications
 *          of USB device connect and disconnects are propagated. All changes are
 *          basically introduced by the redesign of the javahidapi project code.
 *          CIBuddy never had an official release in till version 1.1, but to reflect
 *          the changes that happened here, the version bump of the package was 
 *          introduced. Chances are equal to zero that your effect by that when 
 *          reading this.
 * @since 1.0
 */
package com.codeminders.hidapi;