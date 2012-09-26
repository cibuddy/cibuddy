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

import com.cibuddy.core.build.configuration.IConfigurationInstaller;
import com.cibuddy.core.build.configuration.Triggerable;
import com.cibuddy.core.build.indicator.IBuildStatusIndicator;
import com.cibuddy.core.build.server.IServer;
import com.cibuddy.project.configuration.impl.installer.ExtremeFeedbackDeviceSetupListener;
import com.cibuddy.project.configuration.impl.installer.IndicatorBehaviorConfigurationListener;
import java.util.Hashtable;
import java.util.Timer;
import org.apache.felix.fileinstall.ArtifactInstaller;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

/**
 *
 * "Mirko Jahn" <mirkojahn@gmail.com>
 * @version 1.0
 */
public class Activator implements BundleActivator {

    private static BundleContext bctx;
    private ServiceRegistration indicatorBehaviorConfListenerServiceReg;
    private IndicatorBehaviorConfigurationListener indicatorBehaviorConfListener;
    
    private ServiceRegistration extremeFeedbackDeviceSetupListenerServiceReg;
    private ExtremeFeedbackDeviceSetupListener extremeFeedbackDeviceSetupListener;
    
    private static ServiceTracker serverTracker;
    private static ServiceTracker indicatorTracker;
    
    private Timer caretaker;
    private final long EXECUTION_DELAY = 10 * 1000; // 10 Seconds
    // FIXME: make the heartbeat interval of the server configurable
    private final long HEARTBEAT_INTERVAL = 1 * 60 * 1000; // 1 Minute
    private static ServiceTracker triggerableTracker;
    private final static String TRIGGERABLE_CRON_FILTER = 
                // the registered interface
                "(&(" + Constants.OBJECTCLASS + "=" + Triggerable.class.getName() + ")"
                + "("+Triggerable.UPDATE_TRIGGER_TYPE+"=cron)"
                + ")";
    private Filter triggerableFilter = null;
    private ConfigurationExtender configurationExtender;
    
    @Override
    public void start(BundleContext bc) throws Exception {
        bctx = bc;
        serverTracker = new ServiceTracker(bc, IServer.class.getName(), null);
        serverTracker.open();
        indicatorTracker = new ServiceTracker(bc, IBuildStatusIndicator.class.getName(), null);
        indicatorTracker.open();
        
        // Add listener for depoyment of indicator behavior configuration files
        {
            // add sub scoping to not mix local variables
            indicatorBehaviorConfListener = new IndicatorBehaviorConfigurationListener();
            Hashtable<String,String> dict = new Hashtable<String,String>();
            dict.put("description", "Service enables to configure the behavior of indicators with XML files.");
            dict.put("installerType", indicatorBehaviorConfListener.getClass().getName());
            indicatorBehaviorConfListenerServiceReg = bc.registerService(new String[]{ArtifactInstaller.class.getName(), IConfigurationInstaller.class.getName()}, indicatorBehaviorConfListener, dict);
        }
        // Add listener for deployment of eXtremeFeedbackDeviceSetup configuration files
        {
            extremeFeedbackDeviceSetupListener = new ExtremeFeedbackDeviceSetupListener();
            Hashtable<String,String> dict = new Hashtable<String,String>();
            dict.put("description", "Service enables to configure the setup of extreme feedback devices with XML files.");
            dict.put("installerType", extremeFeedbackDeviceSetupListener.getClass().getName());
            extremeFeedbackDeviceSetupListenerServiceReg = bc.registerService(new String[]{ArtifactInstaller.class.getName(), IConfigurationInstaller.class.getName()}, extremeFeedbackDeviceSetupListener, dict);
        }
        triggerableFilter = bc.createFilter(TRIGGERABLE_CRON_FILTER);
        triggerableTracker = new ServiceTracker(bc, triggerableFilter, null);
        triggerableTracker.open();

        HeartBeat heartBeat = new HeartBeat(triggerableTracker);
        caretaker = new Timer();
        caretaker.schedule(heartBeat, EXECUTION_DELAY, HEARTBEAT_INTERVAL);
        configurationExtender = new ConfigurationExtender(bc);
        configurationExtender.open();
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        bctx = null;
        if(indicatorBehaviorConfListenerServiceReg != null){
            indicatorBehaviorConfListenerServiceReg.unregister();
            indicatorBehaviorConfListenerServiceReg = null;
        }
        if(indicatorBehaviorConfListener!=null){
            // currently no clean-up needed. Keep it for later here...
            indicatorBehaviorConfListener = null;
        }
        if(extremeFeedbackDeviceSetupListenerServiceReg != null){
            extremeFeedbackDeviceSetupListenerServiceReg.unregister();
            extremeFeedbackDeviceSetupListenerServiceReg = null;
        }
        if(extremeFeedbackDeviceSetupListener!=null){
            // currently no clean-up needed. Keep it for later here...
            extremeFeedbackDeviceSetupListener = null;
        }
        if(serverTracker != null) {
            serverTracker.close();
            serverTracker = null;
        }
        if(indicatorTracker != null) {
            indicatorTracker.close();
            indicatorTracker = null;
        }
        caretaker.cancel();
        if (triggerableTracker != null) {
            triggerableTracker.close();
            triggerableTracker = null;
        }
        if(triggerableFilter != null){
            triggerableFilter = null;
        }
        if(configurationExtender!=null){
            configurationExtender.close();
            configurationExtender = null;
        }
    }
    
    static ServiceTracker getServerTracker() {
        return serverTracker;
    }
    
    public static BundleContext getBundleContext() {
        return bctx;
    }

    static ServiceTracker getBuildIndicatorTracker() {
        return indicatorTracker;
    }
    
}
