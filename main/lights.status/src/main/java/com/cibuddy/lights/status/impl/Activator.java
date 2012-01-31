package com.cibuddy.lights.status.impl;

import com.codeminders.hidapi.HIDDeviceInfo;
import com.codeminders.hidapi.HIDManager;
import java.io.IOException;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Hello world!
 *
 */
public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext bc) throws Exception {
        // test jenkins server
        System.out.println("That's it! I'm done.");
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        System.out.println("Shutting down...");
    }
}
