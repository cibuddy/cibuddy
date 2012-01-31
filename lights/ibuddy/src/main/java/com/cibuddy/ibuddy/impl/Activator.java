package com.cibuddy.ibuddy.impl;

import com.cibuddy.hid.HIDServiceConstants;
import com.codeminders.hidapi.HIDDeviceInfo;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.util.tracker.ServiceTracker;

/**
 *
 * @author mirkojahn
 */
public class Activator implements BundleActivator{

    private static String filterString = "(&(" + Constants.OBJECTCLASS + "="+HIDDeviceInfo.class.getName()+")("+HIDServiceConstants.VENDOR_ID+"=4400)("+HIDServiceConstants.USAGE+">=1))";
    
    private static BundleContext ctx;
    private static ServiceTracker lightsTracker = null;
    private HIDDeviceInfoServiceTrackerCustomizer customizer;
    
    
    @Override
    public void start(BundleContext bc) throws Exception {
        ctx = bc;
        customizer = new HIDDeviceInfoServiceTrackerCustomizer();
        // some important services we might need
        lightsTracker = new ServiceTracker(bc, bc.createFilter(filterString), customizer);
        lightsTracker.open();

    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        if(customizer != null){
            customizer.close();
            customizer = null;
        }
        
        if(lightsTracker != null){
            lightsTracker.close();
            lightsTracker = null;
        }
    }
    
    public static BundleContext getBundleContext(){
        return ctx;
    }
    
}
