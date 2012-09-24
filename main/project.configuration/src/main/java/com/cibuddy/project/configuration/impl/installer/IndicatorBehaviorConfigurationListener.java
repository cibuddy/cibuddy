package com.cibuddy.project.configuration.impl.installer;

import com.cibuddy.core.build.configuration.IIndicatorBehaviorConfiguration;
import com.cibuddy.project.configuration.jaxb.v1_0.indicator.Config;
import com.cibuddy.project.configuration.impl.IndicatorBehaviorConfiguration;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.osgi.framework.Bundle;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Installation Listener for IndicatorBehaviorConfigurations.
 * 
 * @author mirkojahn
 * @version 1.0
 * @since 1.0
 */
public class IndicatorBehaviorConfigurationListener extends AbstractXMLArtifactInstaller<Config> {

    private static final Logger LOG = LoggerFactory.getLogger(IndicatorBehaviorConfigurationListener.class);
    private static final String packageName = Config.class.getPackage().getName();

    @Override
    String getPackageName() {
        return packageName;
    }

    @Override
    Config getRootTypeInstance() {
        return new Config();
    }

    @Override
    void handle(Config config, URL url, Bundle bundle) {
        try {
            IIndicatorBehaviorConfiguration iibc = new IndicatorBehaviorConfiguration(config);
            List<ServiceRegistration> configServices = new ArrayList<ServiceRegistration>();
            // register the configuration
            if(bundle == null) {
                // manage the services exposed through none bundle related resources (no lifecycle)
                ServiceRegistration  sr = registerConfigurationAsService(iibc, url, IIndicatorBehaviorConfiguration.class);
                configServices.add(sr);
                configuredURLs.put(url, configServices);
            } else {
                // register with another bundle (as soon as that one disappears, the service goes as well)
                ServiceRegistration  sr = registerConfigurationAsService(iibc, url, new Hashtable<String,String>(),new String[]{IIndicatorBehaviorConfiguration.class.getName()}, bundle.getBundleContext());
            }
        } catch (Exception e) {
            LOG.warn("Problems setting up configuration file.",e);
        }
    }
    
}
