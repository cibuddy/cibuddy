/*
 * 
 */
package com.cibuddy.ibuddy.impl;

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
public class IBuddyLightHandle implements IBuildStatusIndicator {
    
    private static final Logger LOG = LoggerFactory.getLogger(IBuddyLightHandle.class);
    private final ServiceReference hidDriverService;
    private final IBuddyFigure buddyFigure;
    private final HIDDeviceInfo deviceInfo;
    private ServiceRegistration sr;
    private final String figure;
    
    public IBuddyLightHandle(ServiceReference hidServiceRef) throws IOException {
        hidDriverService = hidServiceRef;
        deviceInfo = (HIDDeviceInfo) Activator.getBundleContext().getService(hidServiceRef);
        if(deviceInfo.getProduct_id() == FigureType.IBUDDY_GENERATION_2.getType()){
            buddyFigure = new IBuddyDefault(deviceInfo);
            figure = "iBuddyG2";
        } else if (deviceInfo.getProduct_id() == FigureType.DEVIL.getType()){
            buddyFigure = new IBuddyDevil(deviceInfo);
            figure = "iBuddyDevil";
        } else if (deviceInfo.getProduct_id() == FigureType.QUEEN.getType()){
            buddyFigure = new IBuddyQueen(deviceInfo);
            figure = "iBuddyQueen";
        } else {
            LOG.info("unsupported i-Buddy device: "+deviceInfo);
            throw new IOException("Unsupported i-Buddy device.");
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
        ServiceRegistration sr = Activator.getBundleContext().registerService(IBuildStatusIndicator.class.getName(),this, dict);
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

    public String getComponentId() {
        return this.getClass().getPackage().getName();
    }

    public String getIndicatorId() {
        // FIXME: for more than one instance use the unique id
        return figure;
    }

    public void success() {
        try{
            buddyFigure.setCurrentColor(Color.GREEN);
        }catch(Exception e){
            // FIXME: do some logging...
        }
    }

    public void warning() {
        try{
            buddyFigure.setCurrentColor(Color.YELLOW);
        }catch(Exception e){
            // FIXME: do some logging...
        }
    }

    public void failure() {
        try{
            buddyFigure.setCurrentColor(Color.RED);
        }catch(Exception e){
            // FIXME: do some logging...
        }
    }

    public void building() {
        // blinking yellow is not supported yet
    }
    
}
