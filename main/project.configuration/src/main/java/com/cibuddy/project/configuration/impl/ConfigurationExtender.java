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
package com.cibuddy.project.configuration.impl;

import com.cibuddy.core.build.configuration.IConfigurationInstaller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.util.tracker.BundleTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Extender to track configuration files located within bundles.
 *
 * @author Mirko Jahn <mirkojahn@gmail.com>
 * @version 1.0
 * @since 1.0
 */
public class ConfigurationExtender extends BundleTracker {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigurationExtender.class);
    public static final String CONFIGURATION_HEADER = "CIBuddy-Configuration";

    public ConfigurationExtender(BundleContext context) {
        super(context, Bundle.ACTIVE, null);
    }

    @Override
    public Object addingBundle(Bundle bundle, BundleEvent event) {
        String configurations = (String) bundle.getHeaders().get(
                CONFIGURATION_HEADER);
        if (configurations != null) { 
            // found something
            String[] confs = configurations.split(",");
            // go through each configured file
            if (confs != null && confs.length > 0) {
                for (int i = 0; i < confs.length; i++) {
                    try {
                        URL entry = bundle.getEntry(confs[i]);
                        ServiceReference[] srs = Activator.getBundleContext().getServiceReferences(IConfigurationInstaller.class.getName(), null);
                        if (srs == null && srs.length == 0) {
                            // no services, no point in trying to register anything
                            break;
                        } else {
                            for (int j = 0; j < srs.length; j++) {
                                IConfigurationInstaller ci = (IConfigurationInstaller) Activator.getBundleContext().getService(srs[j]);
                                if (ci.canHandle(entry)) {
                                    try {
                                        ci.install(entry, event.getBundle());
                                    } catch (Exception ex) {
                                        LOG.debug("Installation failed for configuration file found under URL: {}", entry);
                                    }
                                }
                            }
                        }
                    } catch (InvalidSyntaxException ex) {
                        LOG.debug("problem retrieving the configuration installer service", ex);
                    }
                }
            }

        }
        return bundle;
    }

    @Override
    public void removedBundle(Bundle bundle, BundleEvent event, Object object) {
        super.removedBundle(bundle, event, object);
    }

    private String getContent(URL url) throws IOException {
        StringBuilder builder = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inLine;
        while ((inLine = in.readLine()) != null) {
            builder.append(inLine);
        }
        return builder.toString();
    }
}
