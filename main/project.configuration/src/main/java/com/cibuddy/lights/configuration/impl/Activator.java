package com.cibuddy.lights.configuration.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 *
 * "Mirko Jahn" <mirkojahn@gmail.com>
 * @version 1.0
 */
public class Activator implements BundleActivator {

    @Override
    public void start(BundleContext bc) throws Exception {
        System.out.println("Started lights.configuration...");
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        System.out.println("Stopped lights.configuration...");
    }
    
}
