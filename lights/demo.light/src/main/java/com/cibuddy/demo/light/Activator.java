package com.cibuddy.demo.light;

import com.cibuddy.core.build.indicator.IBuildStatusIndicator;
import java.util.Dictionary;
import java.util.Hashtable;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 *
 * "Mirko Jahn" <mirkojahn@gmail.com>
 *
 * @version 1.0
 */
public class Activator implements BundleActivator, IBuildStatusIndicator {

    private DemoLight dl = null;
    private static BundleContext ctx;
    private ServiceRegistration sr;
    
    @Override
    public void start(BundleContext bc) throws Exception {
        ctx = bc;
        dl = new DemoLight();
        Dictionary dict = new Hashtable();
        dict.put(IBuildStatusIndicator.COMPONENT_ID, getComponentId());
        dict.put(IBuildStatusIndicator.INDICATOR_ID, getIndicatorId());
        sr = Activator.getBundleContext().registerService(IBuildStatusIndicator.class.getName(),this, dict);
        if(sr != null){
            System.out.println("Exposed Demo Light (for testing): "+getComponentId()+":"+getIndicatorId());
        }

    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        if(sr != null){
            sr.unregister();
        }
        if(ctx != null){
            ctx = null;
        }
        dl.setVisible(false);
        dl.dispose();
        dl = null;
    }

    public static BundleContext getBundleContext(){
        return ctx;
    }
    @Override
    public String getComponentId(){
        return this.getClass().getPackage().getName();
    }

    @Override
    public String getIndicatorId() {
        return "DemoLight";
    }

    @Override
    public void success(){
//        System.out.println("indicating success");
        dl.updateCircle(DemoLight.GREEN_LIGHT);
    }

    @Override
    public void warning(){
//        System.out.println("indicating warning");
        dl.updateCircle(DemoLight.YELLOW_LIGHT);
    }
    
    @Override
    public void failure(){
//        System.out.println("indicating failure");
        dl.updateCircle(DemoLight.RED_LIGHT);
    }

    @Override
    public void building(){
//        System.out.println("indicating building");
        dl.updateCircle(DemoLight.BLUE_LIGHT);
    }
    
    @Override
    public void off(){
//        System.out.println("indicating light off");
        dl.turnOff();
    }
}
