package com.cibuddy.project.configuration.impl;

import com.cibuddy.core.build.indicator.IBuildStatusIndicator;
import com.cibuddy.core.build.server.IServer;
import java.util.Dictionary;
import java.util.Hashtable;
import org.apache.felix.fileinstall.ArtifactInstaller;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

/**
 *
 * "Mirko Jahn" <mirkojahn@gmail.com>
 * @version 1.0
 */
public class Activator implements BundleActivator {

    private static BundleContext bctx;
    private ServiceRegistration testProjectConfigurationListener;
    private static ServiceTracker serverTracker;
    private static ServiceTracker indicatorTracker;
    
    
    @Override
    public void start(BundleContext bc) throws Exception {
        bctx = bc;
        serverTracker = new ServiceTracker(bc, IServer.class.getName(), null);
        serverTracker.open();
        indicatorTracker = new ServiceTracker(bc, IBuildStatusIndicator.class.getName(), null);
        indicatorTracker.open();
        Dictionary dict = new Hashtable();
        dict.put("description", "Service allowing to check and indicate a build status.");
        testProjectConfigurationListener = bc.registerService(ArtifactInstaller.class.getName(), new ProjectConfigurationListener(), dict);
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        bctx = null;
        if(testProjectConfigurationListener != null){
            testProjectConfigurationListener.unregister();
            testProjectConfigurationListener = null;
        }
        if(serverTracker != null) {
            serverTracker.close();
            serverTracker = null;
        }
        if(indicatorTracker != null) {
            indicatorTracker.close();
            indicatorTracker = null;
        }
    }
    
    static ServiceTracker getServerTracker() {
        return serverTracker;
    }
    
    static BundleContext getBundleContext() {
        return bctx;
    }

    static ServiceTracker getBuildIndicatorTracker() {
        return indicatorTracker;
    }
    
}
