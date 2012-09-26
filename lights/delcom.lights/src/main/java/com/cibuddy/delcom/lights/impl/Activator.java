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
package com.cibuddy.delcom.lights.impl;

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

    private static String filterString = "(&(" + Constants.OBJECTCLASS + "="+HIDDeviceInfo.class.getName()+")("+HIDServiceConstants.VENDOR_ID+"=4037)("+HIDServiceConstants.PRODUCT_ID+"=45184))";
    
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
