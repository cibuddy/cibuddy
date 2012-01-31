package com.cibuddy.jenkins.status;

import com.cibuddy.core.build.server.IServer;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 *
 * @author mirkojahn
 */
public class ServerBucket {
    
    private ServiceRegistration registration;
    private IServer server;

    ServerBucket(ServiceRegistration sr, JenkinsServer js) {
        this.registration = sr;
        this.server = js;
    }
    
    public ServiceRegistration getServiceRegistration() {
        return registration;
    }

    public IServer getServer() {
        return server;
    }
    
}
