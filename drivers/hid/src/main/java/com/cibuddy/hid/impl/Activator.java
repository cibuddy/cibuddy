package com.cibuddy.hid.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 *
 * "Mirko Jahn" <mirkojahn@gmail.com>
 * @version 1.0
 */
public class Activator implements BundleActivator {
    
    private static BundleContext bctx;
    private HIDManagerImpl manager;

    @Override
    public void start(BundleContext bc) throws Exception {
        bctx = bc;
        System.out.println("Loading native HID libraries. This can't be undone, until the JVM got bounced.");
        System.loadLibrary("hid.driver");
        System.out.println("Done loading native libraries. Don't even think about calling update on this bundle with ID: "+bc.getBundle().getBundleId());
        manager = new HIDManagerImpl();
        manager.setup();
        System.out.println("Finished exposing HID devices as services.");
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        System.out.println("Do not try to reinstall this Bundle! You will fail horrobly! BundleId: "+bc.getBundle().getBundleId());
        if(manager != null){
            manager.shutdown();
        }
    }
    
    protected static BundleContext getContext(){
        return bctx;
    }
}
