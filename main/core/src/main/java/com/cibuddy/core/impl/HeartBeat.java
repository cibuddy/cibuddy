package com.cibuddy.core.impl;

import com.cibuddy.core.build.configuration.BuildTestConfiguration;
import java.util.TimerTask;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mirkojahn
 */
public class HeartBeat extends TimerTask {

    private static final Logger LOG = LoggerFactory.getLogger(HeartBeat.class);
    ServiceTracker buildTestConfigurationTracker;
    public HeartBeat(ServiceTracker tracker) {
        buildTestConfigurationTracker = tracker;
        BuildTestConfiguration bt = null;
    }
    @Override
    public void run() {
        Object[] objs = buildTestConfigurationTracker.getServices();
        if(objs != null && objs.length > 0){
            for(int i=0;i<objs.length;i++){
                try{
                    if(objs[i] != null){
                        BuildTestConfiguration btc = (BuildTestConfiguration) objs[i];
                        btc.update();
                    }
                } catch (Exception e){
                    LOG.warn("Updating the BuildTestConfiguration raised an exception!",e);
                }
            }
        } else {
            LOG.debug("no TestConfiguration services found.");
        }
        
    }
    
}
