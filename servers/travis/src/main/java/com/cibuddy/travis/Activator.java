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
package com.cibuddy.travis;

import com.cibuddy.core.build.configuration.IConfigurationInstaller;
import java.util.Dictionary;
import java.util.Hashtable;
import org.apache.felix.fileinstall.ArtifactInstaller;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

/**
 *
 * @author mirkojahn
 */
public class Activator implements BundleActivator {

    private static BundleContext ctx;
    private ServiceRegistration serverConfigurationListener;
    
    @Override
    public void start(BundleContext bc) throws Exception {
        ctx = bc;
        Dictionary dict = new Hashtable();
        dict.put("description", "Service monitoring .travis configuration contributions for Travis-CI server usage");
        serverConfigurationListener = bc.registerService(new String[]{ArtifactInstaller.class.getName(),IConfigurationInstaller.class.getName()}, new ServerConfigurationListener(), dict);
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        ctx = null;
        if(serverConfigurationListener != null){
            serverConfigurationListener.unregister();
            serverConfigurationListener = null;
        }
    }
    
    public static BundleContext getBundleContext() {
        return ctx;
    }
    
}
