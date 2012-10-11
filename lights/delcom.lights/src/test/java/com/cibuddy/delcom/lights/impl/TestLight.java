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

import com.cibuddy.core.build.indicator.IBuildStatusIndicator;
import de.kalpatec.pojosr.framework.launch.ClasspathScanner;
import de.kalpatec.pojosr.framework.launch.PojoServiceRegistry;
import de.kalpatec.pojosr.framework.launch.PojoServiceRegistryFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.osgi.framework.ServiceReference;

/**
 * 
 * @author mirkojahn
 */
public class TestLight {
    PojoServiceRegistry registry;
    
    @Before
    public void before() throws Exception {
        Map config = new HashMap();
        config.put(PojoServiceRegistryFactory.BUNDLE_DESCRIPTORS, new ClasspathScanner().scanForBundles());
        ServiceLoader<PojoServiceRegistryFactory> loader = ServiceLoader.load(PojoServiceRegistryFactory.class);
        registry = loader.iterator().next().newPojoServiceRegistry(config);
        // make sure this bundle is started by manually loading the activator 
        // (we're not yet a bundle to be picked up automatically)
        Activator activator = new Activator();
        activator.start(registry.getBundleContext());
    }
    
    /**
     * Integration Test for delcom device
     * 
     * Because we actually require hardware being connected to the test machine,
     * do not enable this test without knowing what you're doing.
     */
    @Ignore
    @Test
    public void testLight() throws InterruptedException{
        Assert.assertNotNull("BundleContext is not set.", Activator.getBundleContext());
        
        ServiceReference sr = registry.getServiceReference(IBuildStatusIndicator.class.getName());
        Assert.assertNotNull("No service registered, although expected to be "
                + "there for the delcom light test.",sr);
        IBuildStatusIndicator ibsi = (IBuildStatusIndicator)registry.getService(sr);
        Assert.assertNotNull("No matching IBuildStatusIndicator service found", ibsi);
        // so far this service could have been anything (any indicator)
        DelcomLightHandle dlh = (DelcomLightHandle)ibsi;
        // the above line will fail for all other indicators (make sure you only have one - the correct one! - connected!)
        
        Thread.sleep(5000);
        // now check if turning on and off the light really works
        dlh.success();
        Thread.sleep(2000);
        dlh.failure();
        Thread.sleep(2000);
        dlh.off();
    }
}
