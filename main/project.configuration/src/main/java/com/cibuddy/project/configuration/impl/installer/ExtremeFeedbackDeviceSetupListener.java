package com.cibuddy.project.configuration.impl.installer;

import com.cibuddy.core.build.configuration.ConfigurationMaterializationException;
import com.cibuddy.core.build.configuration.IProjectSetup;
import com.cibuddy.core.build.configuration.Triggerable;
import com.cibuddy.project.configuration.impl.Activator;
import com.cibuddy.project.configuration.impl.ExtremeFeedbackDeviceSetup;
import com.cibuddy.project.configuration.jaxb.v1_0.setup.Setup;
import com.cibuddy.project.configuration.jaxb.v1_0.setup.Xfd;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mirko Jahn <mirkojahn@gmail.com>
 * @version 1.0
 * @since 1.0
 */
public class ExtremeFeedbackDeviceSetupListener extends AbstractXMLArtifactInstaller<Setup> {
private static final Logger LOG = LoggerFactory.getLogger(IndicatorBehaviorConfigurationListener.class);
    private static final String packageName = Setup.class.getPackage().getName();

    @Override
    String getPackageName() {
        return packageName;
    }

    @Override
    Setup getRootTypeInstance() {
        return new Setup();
    }

    @Override
    void handle(Setup setup, URL url, Bundle bundle) {
        List<ServiceRegistration> configServices = new ArrayList<ServiceRegistration>();
        List<Xfd> xfds = setup.getXfd();
        Iterator<Xfd> iter = xfds.iterator();
        while(iter.hasNext()){
            try {
                ExtremeFeedbackDeviceSetup efds = new ExtremeFeedbackDeviceSetup(iter.next());
                // register each configuration
                Hashtable<String,String> dict = new Hashtable<String,String>();
                // right now, we only have cron triggered updates, but this might change
                safePut(dict, Triggerable.UPDATE_TRIGGER_TYPE, "cron");
                if(bundle == null) {
                    // manage the services exposed through none bundle related resources (no lifecycle)
                    ServiceRegistration  sr = registerConfigurationAsService(efds, url, dict, 
                            new String[]{IProjectSetup.class.getName(),Triggerable.class.getName()}, null);
                    configServices.add(sr);
                    
                } else {
                    // register with another bundle (as soon as that one disappears, the service goes as well)
                    ServiceRegistration  sr = registerConfigurationAsService(efds, url, dict, 
                            new String[]{IProjectSetup.class.getName(),Triggerable.class.getName()}, bundle.getBundleContext());
                }
                // trigger the configuration for the first time
                try {
                    efds.updateIndicator();
                } catch (Throwable t){
                    // anything could happen, so do not fail the rest of the execution
                }
            } catch (ConfigurationMaterializationException ex) {
                LOG.warn("Problems setting up configuration file.",ex);
            }
        }
        if(bundle == null){
            configuredURLs.put(url, configServices);
        }
    }
    
}
