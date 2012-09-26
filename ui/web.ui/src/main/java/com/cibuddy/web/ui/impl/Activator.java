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
package com.cibuddy.web.ui.impl;

import com.cibuddy.core.build.indicator.IBuildStatusIndicator;
import com.cibuddy.core.build.server.IServer;
import com.cibuddy.web.ui.servlets.IServerListServlet;
import java.util.Hashtable;
import javax.servlet.ServletException;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.http.HttpContext;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.util.tracker.ServiceTrackerCustomizer;

public class Activator implements BundleActivator, ServiceTrackerCustomizer {

    private ServiceTracker httpTracker;
    private static ServiceTracker serverTracker;
    private static ServiceTracker statusIndicatorTracker;
    private String serviceName = "/cibuddy";
    private static BundleContext ctx = null;

    /**
     * {@inheritDoc}
     *
     * @see
     * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
     */
    @Override
    public void start(BundleContext context) throws Exception {
        ctx = context;
        httpTracker = new ServiceTracker(context, HttpService.class.getName(),
                this);
        httpTracker.open();
        serverTracker = new ServiceTracker(context, IServer.class.getName(), null);
        serverTracker.open();
        statusIndicatorTracker = new ServiceTracker(context, IBuildStatusIndicator.class.getName(), null);
        statusIndicatorTracker.open();
    }

    /**
     * {@inheritDoc}
     *
     * @see
     * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
     */
    @Override
    public void stop(BundleContext context) throws Exception {
        if (httpTracker != null) {
            HttpService serv = ((HttpService) httpTracker.getService());
            if (serv != null) {
                serv.unregister(serviceName);
            }
            httpTracker.close();
        }
    }

    public static IServer[] getServers() {
        return (IServer[]) serverTracker.getServices();
    }

    public static IBuildStatusIndicator[] getReasoningServiceProvider() {
        return (IBuildStatusIndicator[]) statusIndicatorTracker.getServices();
    }

    public static BundleContext getBundleContext() {
        return ctx;
    }

    @Override
    public Object addingService(ServiceReference reference) {
        Hashtable<String, String> initparams = new Hashtable<String, String>();
        initparams.put("name", serviceName);
        HttpService http = null;
        try {
            http = ((HttpService) ctx.getService(reference));
            HttpContext context = http.createDefaultHttpContext();
            
//            http.registerServlet(serviceName, new OSGiRuntimeServlet(), initparams, context);
            
            Hashtable<String, String> initparams2 = new Hashtable<String, String>();
            initparams2.put("name", "/iservers");
            http.registerServlet("/iservers", new IServerListServlet(), initparams2, context);
            
            http.registerResources("/cibuddy/res", "/cibuddy/res", context);
        } catch (ServletException e) {
            e.printStackTrace();
        } catch (NamespaceException e) {
            e.printStackTrace();
        }

        return http;
    }

    public void modifiedService(ServiceReference reference, Object service) {
        // do nothing
    }

    public void removedService(ServiceReference reference, Object service) {
        // nothing todo
    }
}
