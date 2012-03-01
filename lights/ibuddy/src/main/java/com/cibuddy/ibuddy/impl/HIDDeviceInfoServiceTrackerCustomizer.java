package com.cibuddy.ibuddy.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 *
 * @author mirkojahn
 */
public class HIDDeviceInfoServiceTrackerCustomizer implements ServiceTrackerCustomizer {

    private ConcurrentMap<ServiceReference,IBuddyLightHandle> lightHandles = 
            new ConcurrentHashMap<ServiceReference,IBuddyLightHandle>(5);
    
    @Override
    public Object addingService(ServiceReference reference) {
        try {
            // new light found
            IBuddyLightHandle handle = new IBuddyLightHandle(reference);
            // provide BuildIndicators
            if(handle.register()){
                lightHandles.put(reference, handle);
            }
            return handle.getHIDDeviceInfo();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Activator.getBundleContext().getService(reference);
    }

    @Override
    public void modifiedService(ServiceReference reference, Object service) {
        // do nothing  (for now);
    }

    @Override
    public void removedService(ServiceReference reference, Object service) {
        IBuddyLightHandle handle = lightHandles.get(reference);
        if(handle != null){
            handle.unregister();
            lightHandles.remove(reference);
        }
    }
    
    public void close() {
        Iterator<Entry<ServiceReference,IBuddyLightHandle>> iter = lightHandles.entrySet().iterator();
        while(iter.hasNext()){
            Entry<ServiceReference,IBuddyLightHandle> entry = iter.next();
            entry.getValue().unregister();
            lightHandles.remove(entry.getKey());
        }
    }
}
