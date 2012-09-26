/*
 * Copyright (C) 2012 Mirko Jahn <mirkojahn@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cibuddy.ibuddy.impl;

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
