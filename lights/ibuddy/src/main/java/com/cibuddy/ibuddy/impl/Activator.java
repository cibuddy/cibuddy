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

import com.cibuddy.hid.HIDServiceConstants;
import com.codeminders.hidapi.HIDDeviceInfo;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mirkojahn
 */
public class Activator implements BundleActivator {

    private static final Logger LOG = LoggerFactory.getLogger(Activator.class);
    
    private static String filterString = "(&(" + Constants.OBJECTCLASS + "=" + HIDDeviceInfo.class.getName() + ")(" + HIDServiceConstants.VENDOR_ID + "=4400)(" + HIDServiceConstants.USAGE + ">=1))";
    // on linux, we do not know which of the two exposed hid devices is actually the correct one. We have to try both at runtime unfortunately.
    private static String filterString_linux = "(&(" + Constants.OBJECTCLASS + "=" + HIDDeviceInfo.class.getName() + ")(" + HIDServiceConstants.VENDOR_ID + "=4400))";
    private static BundleContext ctx;
    private static ServiceTracker lightsTracker = null;
    private HIDDeviceInfoServiceTrackerCustomizer customizer;

    @Override
    public void start(BundleContext bc) throws Exception {
        ctx = bc;
        customizer = new HIDDeviceInfoServiceTrackerCustomizer();
        
        // in case of unix, we can't make enough assumptions. Be less restrictive
        if(isUnix()) {
            // ultimately this will result in 2 hid devices, where only one will work
            filterString = filterString_linux;
            LOG.info("Using relaxed filter string for linux. This will result "
                    + "in 2 i-Buddy devices, although only one does in fact work! "
                    + "Test which one before applying your configuration!");
        }
        lightsTracker = new ServiceTracker(bc, bc.createFilter(filterString), customizer);
        lightsTracker.open();

    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        if (customizer != null) {
            customizer.close();
            customizer = null;
        }

        if (lightsTracker != null) {
            lightsTracker.close();
            lightsTracker = null;
        }
    }

    public final static BundleContext getBundleContext() {
        return ctx;
    }

    public final static boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        // linux or unix
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);

    }
}
