package com.cibuddy.core.service.util;

import com.cibuddy.core.build.server.IServer;
import org.osgi.framework.ServiceRegistration;

/**
 * Lightweight Key Value holder Object.
 * 
 * This object is intended to minimize footprint for storing single key Value Pair
 * associations in one object. This could also be done with other means, but
 * was not done for the sake of simplicity and readability.
 * 
 * @author mirkojahn
 * @since 1.0
 * @version 1.0
 */
public class ServerBucket {
    
    private ServiceRegistration registration;
    private IServer server;

    public ServerBucket(ServiceRegistration sr, IServer iServer) {
        this.registration = sr;
        this.server = iServer;
    }
    
    public ServiceRegistration getServiceRegistration() {
        return registration;
    }

    public IServer getServer() {
        return server;
    }
    
}
