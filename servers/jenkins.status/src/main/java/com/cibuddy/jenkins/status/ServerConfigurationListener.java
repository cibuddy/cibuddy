package com.cibuddy.jenkins.status;

import com.cibuddy.core.build.server.IServer;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.Map.Entry;
import java.util.*;
import org.apache.felix.fileinstall.ArtifactInstaller;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mirkojahn
 */
public class ServerConfigurationListener implements ArtifactInstaller {

    private static final Logger LOG = LoggerFactory.getLogger(ServerConfigurationListener.class);
    private HashMap<String, HashMap<String, String>> files = new HashMap<String, HashMap<String, String>>();
    private HashMap<String, HashMap<String, ServerBucket>> configuredServers = new HashMap<String, HashMap<String, ServerBucket>>();

    @Override
    public boolean canHandle(File file) {
        LOG.info("Handle: "+file.getAbsolutePath());
        if (file == null) {
            return false;
        } else if (file.getName().endsWith(".jenkins")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void install(File file) throws Exception {
        LOG.info("Handle Install: "+file.getAbsolutePath());
        HashMap<String, String> servers = null;
        HashMap<String, ServerBucket> registeredServers = new HashMap<String, ServerBucket>();
        try {
            servers = getServers(file);
            Iterator<Entry<String, String>> iter = servers.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, String> server = iter.next();
                try {
                    JenkinsServer js = new JenkinsServer(file.getAbsolutePath(), server.getKey(), new URI(server.getValue()));
                    ServiceRegistration sr = registerServer(js);
                    registeredServers.put(js.getBuildServerAlias(), new ServerBucket(sr, js));
                } catch (Exception e) {
                    // something went wrong here do nothing (for now)
                    e.printStackTrace();
                    LOG.warn("Problem to register Jenkins instance.", e);
                }
            }
            // remember what we got
            configuredServers.put(file.getName(), registeredServers);
            files.put(file.getName(), servers);
        } catch (Exception e) {
            LOG.warn("Problems setting up configuration file.",e);
        }
    }

    @Override
    public void update(File file) throws Exception {
        // This could be more granular, but for now this is ok.
        LOG.info("Handle Update: "+file.getAbsolutePath());
        uninstall(file);
        install(file);
    }

    @Override
    public void uninstall(File file) throws Exception {
        LOG.info("Handle Uninstall: "+file.getAbsolutePath());
        HashMap<String, ServerBucket> registeredServers = configuredServers.get(file.getName());
        // unexpose services
        if(registeredServers != null) {
            Iterator<Entry<String, ServerBucket>> iter = registeredServers.entrySet().iterator();
            while(iter.hasNext()){
                Entry<String, ServerBucket> entry = iter.next();
                try {
                    entry.getValue().getServiceRegistration().unregister();
                } catch(Exception e) {
                    // if it can't be unregistered... ignore
                    LOG.info("Couldn't unregister Service for Server instance", e);
                }
            }
        }
        // drop references
        registeredServers.remove(file.getName());
        configuredServers.remove(file.getName());
    }

    private HashMap<String, String> getServers(File file) throws FileNotFoundException {
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream(file);
        HashMap<String, String> servers = new HashMap<String, String>();
        try {
            props.load(fis);
            Iterator<Entry<Object, Object>> iter = props.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<Object, Object> server = iter.next();
                servers.put((String) server.getKey(), (String) server.getValue());
            }

        } catch (Exception e) {
            LOG.warn("Problem loading properties file with Jenkins endpoints: ", e);
        } finally {
            try {
                fis.close();
            } catch (Exception ex) {
            }
        }
        return servers;
    }

    private ServiceRegistration registerServer(JenkinsServer js) {
        Dictionary dict = new Hashtable();
        safePut(dict,IServer.SP_SERVER_URL, js.getBuildServerURL());
        safePut(dict,IServer.SP_SERVER_ALIAS, js.getBuildServerAlias());
        safePut(dict,IServer.SP_SERVER_TYPE, js.getBuildServerType());
        safePut(dict,IServer.SP_SERVER_VERSION, js.getBuildServerVersion());
        return Activator.getBundleContext().registerService(IServer.class.getName(), js, dict);
    }
    
    private void safePut(Dictionary dict, String key, Object value) {
        if(key != null && value != null){
            dict.put(key, value);
        }
    }
}
