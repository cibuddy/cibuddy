/*
 * 
 */
package com.cibuddy.project.configuration.impl;

import de.kalpatec.pojosr.framework.launch.ClasspathScanner;
import de.kalpatec.pojosr.framework.launch.PojoServiceRegistry;
import de.kalpatec.pojosr.framework.launch.PojoServiceRegistryFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import org.junit.Before;

/**
 * Base test class for OSGi environment dependent tests.
 * 
 * This test class sports an junit <code>Before</code> annotate method to setup
 * the OSGi environment for this bundle. You could simply interact with the OSGi
 * env. by calling {@link #getRegistry() }.
 * 
 * @author Mirko Jahn <mirkojahn@gmail.com>
 */
public class AbstractOSGIEnvBaseTest {
    /* registry for OSGi services without OSGi */
    private PojoServiceRegistry registry;
    
    /**
     * Setup the environment with all services required.
     * 
     * The before in this case creates an emulated OSGi environment and publishes
     * all services expected. This is kinda slow, but way faster than starting a
     * real OSGi container each time. Because of the speed boost compared to a 
     * real integration test, we could stick to the good practice and start with
     * a new environment for each single test method execution.
     * 
     * @throws Exception indicating problems
     */
    @Before
    public void before() throws Exception {
        Map config = new HashMap();
        config.put(PojoServiceRegistryFactory.BUNDLE_DESCRIPTORS, new ClasspathScanner().scanForBundles());
        ServiceLoader<PojoServiceRegistryFactory> loader = ServiceLoader.load(PojoServiceRegistryFactory.class);
        registry = loader.iterator().next().newPojoServiceRegistry(config);
        // project specific setup to start this "bundle"
        Activator activator = new Activator();
        activator.start(registry.getBundleContext());
    }
    
    /**
     * Helper for Test classes to organize access to the registry.
     * 
     * @return registry to interact with the emulated OSGi env.
     */
    PojoServiceRegistry getRegistry(){
        return registry;
    }
}
