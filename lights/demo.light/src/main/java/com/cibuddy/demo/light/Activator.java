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
    private BlueLight bl = new BlueLight();
    private RedLight rl = new RedLight();
    private YellowLight yl = new YellowLight();
    private GreenLight gl = new GreenLight();

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
            System.out.println("Exposed iBuddy Figure: "+getComponentId()+":"+getIndicatorId());
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
    public String getComponentId(){
        return this.getClass().getPackage().getName();
    }

    public String getIndicatorId() {
        return "DemoLight";
    }

    public void success(){
        dl.updateCircle(gl);
    }

    public void warning(){
        dl.updateCircle(yl);
    }
    
    public void failure(){
        dl.updateCircle(rl);
    }

    public void building(){
        dl.updateCircle(bl);
    }
}
