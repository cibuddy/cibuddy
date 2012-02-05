package com.cibuddy.core.impl;

import com.cibuddy.core.build.configuration.BuildTestConfiguration;
import java.util.TimerTask;
import org.osgi.util.tracker.ServiceTracker;

/**
 *
 * @author mirkojahn
 */
public class HeartBeat extends TimerTask {

    ServiceTracker buildTestConfigurationTracker;
    public HeartBeat(ServiceTracker tracker) {
        buildTestConfigurationTracker = tracker;
        BuildTestConfiguration bt = null;
    }
    @Override
    public void run() {
        // no idea, why this doesn't work... maybe it is to late and I should take a nap
//        BuildTestConfiguration[] configs = (BuildTestConfiguration[])buildTestConfigurationTracker.getServices();
        Object[] objs = buildTestConfigurationTracker.getServices();
        if(objs != null && objs.length > 0){
            for(int i=0;i<objs.length;i++){
                try{
                    if(objs[i] != null){
                        BuildTestConfiguration btc = (BuildTestConfiguration) objs[i];
                        btc.update();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("no TestConfiguration services found.");
        }
        
    }
    
}
