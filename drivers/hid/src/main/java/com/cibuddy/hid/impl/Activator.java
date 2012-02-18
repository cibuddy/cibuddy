package com.cibuddy.hid.impl;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * "Mirko Jahn" <mirkojahn@gmail.com>
 * @version 1.0
 */
public class Activator implements BundleActivator {
    
    private static BundleContext bctx;
    private HIDManagerImpl manager;
    private static final Logger LOG = LoggerFactory.getLogger(Activator.class);
    
    @Override
    public void start(BundleContext bc) throws Exception {
        bctx = bc;
        try {
            LOG.info("Loading native HID libraries. This can't be undone, until the JVM got bounced.");
            System.loadLibrary("hid.driver");
            LOG.debug("Done loading native libraries. Don't even think about calling update on this bundle with ID: "+bc.getBundle().getBundleId());
            manager = new HIDManagerImpl();
            manager.setup();
            LOG.info("Finished exposing HID devices as services.");
        } catch (Throwable e) {
            LOG.warn("Huston we have a problem. Loading of the hid driver failed.",e);
            System.out.println("Huston we have a problem. Loading of the hid driver failed."+e.getMessage());
            e.printStackTrace(System.out);
            throw new Exception("Start Problem with bundle "+bc.getBundle().getBundleId(),e);
        }
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        LOG.info("Do not try to reinstall this Bundle! You will fail horrobly! BundleId: "+bc.getBundle().getBundleId());
        if(manager != null){
            manager.shutdown();
        }
    }
    
    protected static BundleContext getContext(){
        return bctx;
    }
}
