package com.cibuddy.jenkins;

import com.cibuddy.core.build.configuration.IConfigurationInstaller;
import java.util.Dictionary;
import java.util.Hashtable;
import org.apache.felix.fileinstall.ArtifactInstaller;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 *
 * @author mirkojahn
 */
public class Activator implements BundleActivator {

    private static BundleContext ctx;
    private ServiceRegistration serverConfigurationListener;
    
    @Override
    public void start(BundleContext bc) throws Exception {
        ctx = bc;
        Dictionary dict = new Hashtable();
        dict.put("description", "Service monitoring *.jenkins configuration contributions for Jenkins server usage with felix.fileinstall");
        serverConfigurationListener = bc.registerService(new String[]{ArtifactInstaller.class.getName(),IConfigurationInstaller.class.getName()}, new ServerConfigurationListener(), dict);
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        ctx = null;
        if(serverConfigurationListener != null){
            serverConfigurationListener.unregister();
            serverConfigurationListener = null;
        }
    }
    
    public static BundleContext getBundleContext() {
        return ctx;
    }
    
}
