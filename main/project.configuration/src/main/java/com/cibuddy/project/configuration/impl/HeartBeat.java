package com.cibuddy.project.configuration.impl;

import com.cibuddy.core.build.configuration.Triggerable;
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
    ServiceTracker triggerableTracker;
    
    public HeartBeat(ServiceTracker tracker) {
        triggerableTracker = tracker;
    }
    
    @Override
    public void run() {
        Object[] objs = triggerableTracker.getServices();
        if(objs != null && objs.length > 0){
            for(int i=0;i<objs.length;i++){
                try{
                    if(objs[i] != null){
                        Triggerable t = (Triggerable) objs[i];
                        t.updateIndicator();
                    }
                } catch (Exception e){
                    LOG.warn("Updating the TriggerableTracker raised an exception!",e);
                }
            }
        } else {
            LOG.debug("no TriggerableTracker services found.");
        }
    }
}
