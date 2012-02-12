package com.cibuddy.project.configuration.impl;

import com.cibuddy.core.build.configuration.BuildTestConfiguration;
import com.cibuddy.project.configuration.schema.BuildTestConfigurationType;
import com.cibuddy.project.configuration.schema.Setup;
import java.io.File;
import java.util.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.apache.felix.fileinstall.ArtifactInstaller;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mirkojahn
 */
public class ProjectConfigurationListener implements ArtifactInstaller {

    private static final Logger LOG = LoggerFactory.getLogger(ProjectConfigurationListener.class);
    private static final String packageName = "com.cibuddy.project.configuration.schema";

    Map<File,List<ServiceRegistration>> configuredFiles = new HashMap<File,List<ServiceRegistration>>();
            
    @Override
    public boolean canHandle(File file) {
        LOG.info("Handle: " + file.getAbsolutePath());
        if (file == null) {
            return false;
        } else if (file.getName().endsWith(".xml")) {
            try {
                JAXBContext jc = JAXBContext.newInstance(packageName,this.getClass().getClassLoader());
                Unmarshaller unmarshaller = jc.createUnmarshaller();
                Setup config = (Setup) unmarshaller.unmarshal(file);
                return true;
            } catch (Exception ex) {
                // couldn't parse xml with expected schema... do nothing.
                // FIXME: this is nasty! in case this is not an xml according to the schema this is wrong!
                LOG.debug("Problems setting up configuration file.",ex);
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public void install(File file) throws Exception {
        LOG.info("Handle Install: " + file.getAbsolutePath());
        try {
            JAXBContext jc = JAXBContext.newInstance(packageName,this.getClass().getClassLoader());
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            Setup config = (Setup) unmarshaller.unmarshal(file);
            List<BuildTestConfigurationType> configurations = config.getConfiguration();
            Iterator<BuildTestConfigurationType> iter = configurations.iterator();
            List<ServiceRegistration> configServices = new ArrayList<ServiceRegistration>();
            while(iter.hasNext()) {
                BuildTestConfigurationType bct = iter.next();
                BuildTestConfiguration dbtcConfig = new DefaultBuildTestConfiguration(bct);
                ServiceRegistration  sr = registerServer(dbtcConfig, file);
                configServices.add(sr);
            }
            configuredFiles.put(file, configServices);
        } catch (Exception e) {
            LOG.warn("Problems setting up configuration file.",e);
        }
    }

    @Override
    public void update(File file) throws Exception {
        // This could be more granular, but for now this is ok.
        LOG.info("Handle Update: " + file.getAbsolutePath());
        uninstall(file);
        install(file);
    }

    @Override
    public void uninstall(File file) throws Exception {
        LOG.info("Handle Uninstall: " + file.getAbsolutePath());
        List<ServiceRegistration> srs = configuredFiles.get(file);
        if(srs != null && srs.size() > 0) {
            Iterator<ServiceRegistration> srsIterator = srs.iterator();
            while(srsIterator.hasNext()) {
                ServiceRegistration sr = srsIterator.next();
                try {
                    sr.unregister();
                } catch (Exception e) {
                    // do nothing
                }
            }
            configuredFiles.remove(file);
        }
    }
    
    private ServiceRegistration registerServer(BuildTestConfiguration dbtc, File f) {
        Dictionary dict = new Hashtable();
        safePut(dict,BuildTestConfiguration.BUILD_TEST_ALIAS, dbtc.getAlias());
        safePut(dict,BuildTestConfiguration.BUILD_TEST_SOURCE, f.getPath());
        return Activator.getBundleContext().registerService(BuildTestConfiguration.class.getName(), (BuildTestConfiguration)dbtc, dict);
    }
    
    private void safePut(Dictionary dict, String key, Object value) {
        if(key != null && value != null){
            dict.put(key, value);
        }
    }
}
