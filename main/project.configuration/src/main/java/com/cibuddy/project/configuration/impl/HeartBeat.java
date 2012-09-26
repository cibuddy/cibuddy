/*
 * Copyright (C) 2012 Mirko Jahn <mirkojahn@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
