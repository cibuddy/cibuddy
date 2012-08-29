package com.cibuddy.hid.impl;

import java.util.Timer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Bundle Activator.
 * 
 * Initiate the native code loading and starting the Timer for updating the USB
 * device list and exposing them as OSGi Services of type <code>com.codeminders.hidapi.HIDDeviceInfo</code>.
 * 
 * "Mirko Jahn" <mirkojahn@gmail.com>
 * @version 1.0
 */
public class Activator implements BundleActivator {
    
    private static BundleContext bctx;
    private HIDServiceManager manager;
    private Timer usbDeviceUpdateTimer;
    private final long EXECUTION_DELAY = 10*1000; // 10 seconds
    private final long UPDATE_INTERVAL = 5*1000; // 5 seconds
    private static final Logger LOG = LoggerFactory.getLogger(Activator.class);
    
    /**
     * {@inheritDoc}
     * 
     * @since 1.0
     */
    @Override
    public void start(BundleContext bc) throws Exception {
        bctx = bc;
        try {
            LOG.info("Loading native HID libraries. This can't be undone, until the JVM got bounced.");
            System.loadLibrary("hidapi-jni");
            LOG.debug("Done loading native libraries. Don't even think about calling update on this bundle with ID: "+bc.getBundle().getBundleId());
            
        } catch (Throwable e) {
            LOG.warn("Huston we have a problem. Loading of the hid driver failed.",e);
            System.out.println("Huston we have a problem. Loading of the hid driver failed: "+e.getMessage());
            e.printStackTrace(System.out);
            throw new Exception("Start Problem with bundle "+bc.getBundle().getBundleId(),e);
        }
        manager = new HIDServiceManager();
        // use the method that is not throwing any exceptions here!
        manager.run();
        usbDeviceUpdateTimer = new Timer();
        usbDeviceUpdateTimer.schedule(manager, EXECUTION_DELAY, UPDATE_INTERVAL);
        LOG.info("Finished exposing HID devices as services.");
    }

    /**
     * {@inheritDoc}
     * 
     * @since 1.0
     */
    @Override
    public void stop(BundleContext bc) throws Exception {
        LOG.info("Do not try to reinstall this Bundle! You will fail horrobly! BundleId: "+bc.getBundle().getBundleId());
        if(usbDeviceUpdateTimer!=null){
            usbDeviceUpdateTimer.cancel();
            usbDeviceUpdateTimer = null;
        }
        if(manager != null){
            // can't clean-up singleton... setting to null at least.
            manager.shutdown();
            manager = null;
        }
    }
    
    /**
     * Provide the current BundleContext of the bundle.
     * 
     * This might be null in case the bundle was not started yet.
     * @return the BundleContext.
     * 
     * @since 1.0
     */
    protected static BundleContext getContext(){
        return bctx;
    }
}
