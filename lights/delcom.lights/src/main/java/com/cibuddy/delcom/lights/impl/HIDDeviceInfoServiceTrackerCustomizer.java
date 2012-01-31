/*
 * 
 */
package com.cibuddy.delcom.lights.impl;

import com.cibuddy.delcom.lights.DeviceType;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

/**
 *
 * @author mirkojahn
 */
public class HIDDeviceInfoServiceTrackerCustomizer implements ServiceTrackerCustomizer {

    private HashMap<ServiceReference,DelcomLightHandle> lightHandles = 
            new HashMap<ServiceReference,DelcomLightHandle>();;
    
    @Override
    public Object addingService(ServiceReference reference) {
        try {
            // new light found
            DelcomLightHandle handle = new DelcomLightHandle(reference, DeviceType.G2);
            // provide BuildIndicators
            if(handle.register()){
                lightHandles.put(reference, handle);
            }
            return handle.getHIDDeviceInfo();
        } catch (IOException ex) {
            // do nothing
        }
        return Activator.getBundleContext().getService(reference);
    }

    @Override
    public void modifiedService(ServiceReference reference, Object service) {
        // do nothing  (for now);
    }

    @Override
    public void removedService(ServiceReference reference, Object service) {
        DelcomLightHandle handle = lightHandles.get(reference);
        if(handle != null){
            handle.unregister();
            lightHandles.remove(reference);
        }
    }
    
    public void close() {
        Iterator<Entry<ServiceReference,DelcomLightHandle>> iter = lightHandles.entrySet().iterator();
        while(iter.hasNext()){
            Entry<ServiceReference,DelcomLightHandle> entry = iter.next();
            entry.getValue().unregister();
            lightHandles.remove(entry.getKey());
        }
    }
}
