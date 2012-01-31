package com.cibuddy.core.impl;

import com.cibuddy.core.build.configuration.BuildTestConfiguration;
import java.util.Timer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;

/**
 *
 * "Mirko Jahn" <mirkojahn@gmail.com>
 * @version 1.0
 */
public class Activator  implements BundleActivator{

    Timer caretaker;
    private final long EXECUTION_DELAY = 10*1000; // 10 Seconds
    // FIXME: make the heartbeat interval of the server configurable
    private final long HEARTBEAT_INTERVAL = 5*60*1000; // 5 Minutes
    
    private static ServiceTracker buildConfigTracker;
    
    @Override
    public void start(BundleContext bc) throws Exception {
        buildConfigTracker = new ServiceTracker(bc, BuildTestConfiguration.class.getName(), null);
        buildConfigTracker.open();
        
        HeartBeat heartBeat = new HeartBeat(buildConfigTracker);
        caretaker = new Timer();
        caretaker.schedule(heartBeat, EXECUTION_DELAY, HEARTBEAT_INTERVAL);

    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        caretaker.cancel();
    }
    
}
