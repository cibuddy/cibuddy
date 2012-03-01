package com.cibuddy.delcom.lights.impl;

import com.cibuddy.core.build.indicator.IBuildStatusIndicator;
import com.cibuddy.delcom.lights.Color;
import com.cibuddy.delcom.lights.DeviceType;
import com.cibuddy.delcom.lights.IDelcomLight;
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
public class DelcomLightHandle implements IBuildStatusIndicator {
    
    private static final Logger LOG = LoggerFactory.getLogger(DelcomLightHandle.class);
    private final ServiceReference hidDriverService;
    private final IDelcomLight delcomLight;
    private final HIDDeviceInfo deviceInfo;
    private ServiceRegistration sr;
    
    public DelcomLightHandle(ServiceReference hidServiceRef, DeviceType t) throws IOException {
        hidDriverService = hidServiceRef;
        if(t.equals(DeviceType.G2)){
            deviceInfo = (HIDDeviceInfo) Activator.getBundleContext().getService(hidServiceRef);
            delcomLight = new DelcomLightG2(deviceInfo);
        } else {
            // no G1 support right now
            throw new IOException("Not supported for G1 delcom device yet.");
        }
        delcomLight.open();
    }
    
    public HIDDeviceInfo getHIDDeviceInfo(){
        return deviceInfo;
    }
    
    public boolean register() {
        Dictionary dict = new Hashtable();
        dict.put(IBuildStatusIndicator.COMPONENT_ID, getComponentId());
        dict.put(IBuildStatusIndicator.INDICATOR_ID, getIndicatorId());
        dict.put("DeviceInfoProperties", deviceInfo);
        ServiceRegistration sr = Activator.getBundleContext().registerService(IBuildStatusIndicator.class.getName(),this, dict);
        if(sr != null){
            System.out.println("Exposed delcom light: "+getComponentId()+":"+getIndicatorId());
            LOG.info("Exposed delcom light: "+getComponentId()+":"+getIndicatorId());
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
        return delcomLight.getDeviceType().name();
    }

    public void success() {
        try{
            LOG.debug("indicating success.");
            delcomLight.setColor(Color.GREEN);
        }catch(Exception e){
            LOG.warn("Problem indicating success. ",e);
        }
    }

    public void warning() {
        try{
            LOG.debug("indicating warning.");
            delcomLight.setColor(Color.YELLOW);
        }catch(Exception e){
            LOG.warn("Problem indicating warning. ",e);
        }
    }

    public void failure() {
        try{
            LOG.debug("indicating failure.");
            delcomLight.setColor(Color.RED);
        }catch(Exception e){
            LOG.warn("Problem indicating failure. ",e);
        }
    }

    public void building() {
        // blinking yellow is not supported yet
        LOG.info("Indicating 'building' not supported yet by the delcom beacon.");
    }
    
    public void off(){
        try {
            LOG.debug("indicating light OFF/black.");
            delcomLight.setColor(Color.BLACK);
        } catch (IOException ex) {
            LOG.warn("Problem turning the light off. ",ex);
            
        }
    }
    
}
