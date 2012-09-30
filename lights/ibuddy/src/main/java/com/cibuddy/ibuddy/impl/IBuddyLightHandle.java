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
/*
 * 
 */
package com.cibuddy.ibuddy.impl;

import com.cibuddy.core.build.indicator.AbstractBuildStatusIndicator;
import com.cibuddy.core.build.indicator.IBuildStatusIndicator;
import com.cibuddy.ibuddy.*;
import com.codeminders.hidapi.HIDDeviceInfo;
import java.io.IOException;
import java.util.Dictionary;
import java.util.Hashtable;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author mirkojahn
 */
public class IBuddyLightHandle extends AbstractBuildStatusIndicator {
    
    private static final Logger LOG = LoggerFactory.getLogger(IBuddyLightHandle.class);
    private final IBuddyDefault buddyFigure;
    private final HIDDeviceInfo deviceInfo;
    private ServiceRegistration sr;
    private final String figure;
    
    public IBuddyLightHandle(ServiceReference hidServiceRef) throws IOException {
        deviceInfo = (HIDDeviceInfo) Activator.getBundleContext().getService(hidServiceRef);
        if(deviceInfo.getProduct_id() == FigureType.IBUDDY_GENERATION_1.getType()){
            buddyFigure = new IBuddyFirstGen(deviceInfo);
            figure = "iBuddyG1";
        } else if(deviceInfo.getProduct_id() == FigureType.IBUDDY_GENERATION_2.getType()){
            buddyFigure = new IBuddyDefault(deviceInfo);
            figure = "iBuddyG2";
        } else if (deviceInfo.getProduct_id() == FigureType.DEVIL.getType()){
            buddyFigure = new IBuddyDevil(deviceInfo);
            figure = "iBuddyDevil";
        } else if (deviceInfo.getProduct_id() == FigureType.QUEEN.getType()){
            buddyFigure = new IBuddyQueen(deviceInfo);
            figure = "iBuddyQueen";
        } else {
            LOG.warn("unsupported i-Buddy device. Falling back to default G2-Buddy for this details: "+deviceInfo);
            buddyFigure = new IBuddyDefault(deviceInfo);
            figure = "unknown iBuddy - handle as G2 device.";
        }
        buddyFigure.open();
    }
    
    public HIDDeviceInfo getHIDDeviceInfo(){
        return deviceInfo;
    }
    
    public boolean register() {
        Dictionary dict = new Hashtable();
        dict.put(IBuildStatusIndicator.COMPONENT_ID, getComponentId());
        dict.put(IBuildStatusIndicator.INDICATOR_ID, getIndicatorId());
        dict.put("DeviceInfoProperties", deviceInfo);
        sr = Activator.getBundleContext().registerService(IBuildStatusIndicator.class.getName(),this, dict);
        if(sr != null){
            LOG.info("Exposed iBuddy Figure: "+getComponentId()+":"+getIndicatorId());
            return true;
        } else {
            return false;
        }
    }
    
    public void unregister(){
        if(sr != null){
            sr.unregister();
        }
    }

    @Override
    public String getComponentId() {
        return this.getClass().getPackage().getName();
    }

    @Override
    public String getIndicatorId() {
        // FIXME: for more than one instance use the unique id
        return figure;
    }

    @Override
    public void success() {
        try{
            LOG.debug("indicating success.");
            buddyFigure.resetEverything();
            buddyFigure.setCurrentColor(Color.GREEN);
            buddyFigure.setHeart(true);
        } catch(Exception e){
            LOG.warn("Problem indicating success. ",e);
        }
    }

    @Override
    public void warning() {
        try{
            LOG.debug("indicating warning.");
            buddyFigure.resetEverything();
            buddyFigure.setCurrentColor(Color.YELLOW);
        } catch(Exception e){
            LOG.warn("Problem indicating warning. ",e);
        }
    }

    @Override
    public void failure() {
        try{
            LOG.debug("indicating failure.");
            buddyFigure.resetEverything();
            buddyFigure.setCurrentColor(Color.RED);
        } catch(Exception e){
            LOG.warn("Problem indicating failure. ",e);
        }
    }

    @Override
    public void building() {
        LOG.info("Indicating 'building' by beating heart.");
        buddyFigure.enableBeatingHeart();
    }
    
    
    @Override
    public void off(){
        try {
            LOG.debug("indicating light OFF/black.");
            buddyFigure.resetEverything();
        } catch (Exception ex) {
            LOG.warn("Problem turning the light off. ",ex);
            
        }
    }
}
