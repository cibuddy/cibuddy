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
package com.cibuddy.jenkins;

import com.cibuddy.core.build.configuration.IConfigurationInstaller;
import com.cibuddy.core.build.server.IServer;
import com.cibuddy.core.service.util.ServerBucket;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;
import org.apache.felix.fileinstall.ArtifactInstaller;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mirkojahn
 */
public class ServerConfigurationListener implements ArtifactInstaller, IConfigurationInstaller {

    private static final Logger LOG = LoggerFactory.getLogger(ServerConfigurationListener.class);
    private HashMap<String, HashMap<String, ServerBucket>> configuredServers = new HashMap<String, HashMap<String, ServerBucket>>();

    @Override
    public boolean canHandle(File file) {
        LOG.info("Handle: "+file.getAbsolutePath());
        try {
            return canHandle(file.toURI().toURL());
        } catch (MalformedURLException ex) {
        }
        return false;
    }

    @Override
    public boolean canHandle(URL url) {
        if (url == null) {
            return false;
        } else if (url.getFile().endsWith(".jenkins")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void install(File file) throws Exception {
        LOG.info("Handle Install: "+file.getAbsolutePath());
        install(file.toURI().toURL(), null);
    }
    
    @Override
    public void install(URL url, Bundle bundle) throws Exception {
        HashMap<String, ServerBucket> registeredServers = new HashMap<String, ServerBucket>();
        // if called from outside this bundle, use the external bundle context.
        BundleContext bc;
        if(bundle == null) {
            bc = Activator.getBundleContext();
        } else {
            bc = bundle.getBundleContext();
        }
            
        try {
            // extract all configured servers and register them separately
            HashMap<String, String> servers = getServers(url);
            Iterator<Entry<String, String>> iter = servers.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, String> server = iter.next();
                try {
                    JenkinsServer js = new JenkinsServer(url.getFile(), server.getKey(), new URI(server.getValue()),null);
                    ServiceRegistration sr = registerServer(js,bc);
                    registeredServers.put(js.getBuildServerAlias(), new ServerBucket(sr, js));
                } catch (Exception e) {
                    // something went wrong here do nothing (for now)
                    LOG.warn("Problem to register Jenkins instance.", e);
                }
            }
            // remember what we got for later clean-up (if the service was not registered by an external bundle)
            if(bundle == null){
                configuredServers.put(url.getFile(), registeredServers);
            } // else => osgi will automatically remove all service for other bundles when their lifecycle prohibits it.
            
        } catch (Exception e) {
            LOG.warn("Problems setting up configuration file.",e);
        }
    }

    @Override
    public void update(File file) throws Exception {
        // This could be more granular, but for now this is ok.
        LOG.info("Handle Update: "+file.getAbsolutePath());
        update(file.toURI().toURL(),null);
    }
    
    @Override
    public void update(URL url, Bundle bundle) throws Exception {
        uninstall(url,bundle);
        install(url, bundle);
    }

    @Override
    public void uninstall(File file) throws Exception {
        LOG.info("Handle Uninstall: "+file.getAbsolutePath());
        uninstall(file.toURI().toURL(),null);
    }
    
    @Override
    public void uninstall(URL url, Bundle bundle) throws Exception {
        HashMap<String, ServerBucket> registeredServers = configuredServers.get(url.getFile());
        // unexpose services
        if(registeredServers != null) {
            Iterator<Entry<String, ServerBucket>> iter = registeredServers.entrySet().iterator();
            while(iter.hasNext()){
                Entry<String, ServerBucket> entry = iter.next();
                try {
                    entry.getValue().getServiceRegistration().unregister();
                    // remove the service from this map
                    registeredServers.remove(entry.getKey());
                } catch(Exception e) {
                    // if it can't be unregistered... ignore
                    LOG.info("Couldn't unregister Service for Server instance", e);
                }
            }
            // drop "global" references to the file (in case we track it)
            configuredServers.remove(url.getFile());
        } // else => we do not have a track of this "file"
    }

    private HashMap<String, String> getServers(URL url) throws FileNotFoundException {
        Properties props = new Properties();
        HashMap<String, String> servers = new HashMap<String, String>();
        InputStream is = null;
        try {
            is = url.openStream(); 
            props.load(is);
            Iterator<Entry<Object, Object>> iter = props.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<Object, Object> server = iter.next();
                servers.put((String) server.getKey(), (String) server.getValue());
            }
        } catch (Exception e) {
            LOG.warn("Problem loading properties file with Jenkins endpoints: ", e);
        } finally {
            try {
                if(is != null){
                    is.close();
                }
            } catch (Exception ex) {
            }
        }
        return servers;
    }

    private ServiceRegistration registerServer(JenkinsServer js, BundleContext bc) {
        Dictionary dict = new Hashtable();
        safePut(dict,IServer.SP_SERVER_URL, js.getBuildServerURL());
        safePut(dict,IServer.SP_SERVER_ALIAS, js.getBuildServerAlias());
        safePut(dict,IServer.SP_SERVER_TYPE, js.getBuildServerType());
        safePut(dict,IServer.SP_SERVER_VERSION, js.getBuildServerVersion());
        return bc.registerService(IServer.class.getName(), js, dict);
    }
    
    private void safePut(Dictionary dict, String key, Object value) {
        if(key != null && value != null){
            dict.put(key, value);
        }
    }

}
