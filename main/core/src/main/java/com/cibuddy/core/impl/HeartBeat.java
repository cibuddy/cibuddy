package com.cibuddy.core.impl;

import com.cibuddy.core.build.configuration.BuildTestConfiguration;
import com.cibuddy.core.build.configuration.ProjectTrigger;
import com.cibuddy.core.build.configuration.StatusIndicatorTrigger;
import java.util.Iterator;
import java.util.List;
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
    }
    @Override
    public void run() {
        BuildTestConfiguration[] configs = (BuildTestConfiguration[])buildTestConfigurationTracker.getServices();
        if(configs != null && configs.length > 0){
            for(int i=0;i<configs.length; i++){
                try {
                    BuildTestConfiguration conf = configs[i];
                    boolean activate = false;
                    List<ProjectTrigger> pTriggers = conf.getProjectTriggers();
                    Iterator<ProjectTrigger> iter = pTriggers.iterator();
                    while(iter.hasNext()){
                        ProjectTrigger pTrigger = iter.next();
                        if(pTrigger.validate()) {
                            activate = true;
                        }
                    }
                    if(activate) {
                        List<StatusIndicatorTrigger> iTriggers = conf.getStatusIndicatorTriggers();
                        Iterator<StatusIndicatorTrigger> iterator = iTriggers.iterator();
                        while(iterator.hasNext()){
                            StatusIndicatorTrigger iTrigger = iterator.next();
                            iTrigger.enableStatusIndicator();
                        }
                    }
                } catch (Exception e){
                    
                }
            }
        }
    }
    
}
