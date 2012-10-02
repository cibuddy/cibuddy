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

import com.cibuddy.core.build.configuration.ConfigurationMaterializationException;
import com.cibuddy.core.build.configuration.IConfigurationService;
import com.cibuddy.core.build.configuration.IIndicatorBehaviorConfiguration;
import com.cibuddy.core.build.configuration.IProjectSetup;
import com.cibuddy.core.build.configuration.Triggerable;
import com.cibuddy.core.build.indicator.IBuildStatusIndicator;
import com.cibuddy.core.build.indicator.StatusAction;
import com.cibuddy.core.build.server.IProject;
import com.cibuddy.core.build.server.IServer;
import com.cibuddy.project.configuration.jaxb.v1_0.setup.ProjectType;
import com.cibuddy.project.configuration.jaxb.v1_0.setup.Xfd;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.osgi.framework.Constants;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mirko Jahn <mirkojahn@gmail.com>
 */
public class ExtremeFeedbackDeviceSetup implements Triggerable, IProjectSetup {
    
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(ExtremeFeedbackDeviceSetup.class);
    private Xfd xfd;
    
    /**
     *
     * @param xfd
     * @throws ConfigurationMaterializationException
     */
    public ExtremeFeedbackDeviceSetup (Xfd xfd) throws ConfigurationMaterializationException {
        setup(xfd);
    }
    
    private void setup(Xfd xfd){
        this.xfd = xfd;
    }
    
    @Override
    public void updateIndicator() {
        try {
            int index = 0;
            if(xfd.getIndicatorId() != null){
                // we have to set the index (not using the default of 0)
                index = Integer.parseInt(xfd.getIndicatorId());
            }
            IIndicatorBehaviorConfiguration psec = getConfiguration(xfd.getConfig());
            LOG.debug("index for configured indicator identified as {} for configuration {}", index, this);
            IBuildStatusIndicator ibsi = getBuildStatusIndicatorByServiceIndex(index);
            // FIXME: incorporate a better configuration of indicators
//            IProjectIndicator ipi = getProjectIndicator(xfd.getIndicatorId());
            if(psec != null){
                if(ibsi != null){
                    StatusAction sa = psec.evaluate(this);
                    if(sa != null) {
                        ibsi.indicate(sa);
                    }
                } else {
                    LOG.info("no indicator found - I can't indicate anything without that one ");
                }
            } else {
                LOG.info("no behavior configuration service found - I can't indicate anything without that one");
            }
        } catch(Exception e){
            // this might not be an issue, so don't warn, but raise concerns at least.
            LOG.info("updating the indicator didn't work.", e);
        }
    }

    @Override
    public List<IProject> getProjects() {
        List<IProject> projects = new ArrayList<IProject>();
        // assign the projects (this might fail due to missing server(s) f.i.
        Iterator<ProjectType> iter = xfd.getProject().iterator();
        while(iter.hasNext()){
            ProjectType pt = iter.next();
            IProject tempProject = null;
            if(pt.getServer() != null) {
                tempProject = getProject(pt.getServer(),pt);
            } else {
                tempProject = getProject(xfd.getServer(),pt);
            }
            if(tempProject != null){
                projects.add(tempProject);
            }
        }
        return projects;
    }
    
    private IProject getProject(String serverId, ProjectType pt) {
        boolean useFirstAsDefault = false;
        if(serverId == null){
            // use the server Id from the project itself
            serverId = pt.getServer();
            if(serverId == null) {
                // nothing was configured. Use the first server available
                useFirstAsDefault = true;
            }
        }
        IServer server;
        if(useFirstAsDefault) {
            // try the first Server we can find (nothing was configured)
            server = (IServer)Activator.getServerTracker().getService();
        } else {
            server = getServer(serverId);
        }
        
        if(server != null) {
            return server.getProject(pt.getId());
        } 
        return null;
    }
    
    private IServer getServer(String alias){
        String filterString = "";
        try {
            filterString = 
                // the registered interface
                "(&(" + Constants.OBJECTCLASS + "=" + IServer.class.getName() + ")"
                // the configuration name/ id(unique)
                + "("+IServer.SP_SERVER_ALIAS+"=" + alias +")"
                + ")";
            // this one is NOT null safe... check to be sure!
            ServiceReference[] srs = Activator.getBundleContext().getServiceReferences(IServer.class.getName(), filterString);
            
            if(srs == null || srs.length == 0) {
                LOG.info("no IServer service found with alias: "+alias);
                return null;
            } else if(srs.length > 1) {
                LOG.warn("There are more than on IServers exposed with the following alias: "+alias+" and thus is not uniquely identifyable... Using the first one instead!");
            } 
            return (IServer)Activator.getBundleContext().getService(srs[0]);
            
        } catch (InvalidSyntaxException ex) {
            LOG.info("Apparently the syntax is wrong! of the filter string: "+filterString, ex);
        }
        return null;
    }
    
    private IBuildStatusIndicator getBuildStatusIndicatorByServiceIndex(int index){
        ServiceReference[] srs = Activator.getBuildIndicatorTracker().getServiceReferences();
        if(srs == null || srs.length <=index){
            // there is no service of this kind
            LOG.debug("The index expected to select an indicator does not exist for index {} in services list.", index);
            return null;
        }
        LOG.debug("Retrieving indicator for index: {}", index);
        return (IBuildStatusIndicator)Activator.getBundleContext().getService(srs[index]);
    }
//    public static IProjectIndicator getProjectIndicator(String id) {
//        //FIXME: implement with Activator services
//        return null;
//    }
    
    private IIndicatorBehaviorConfiguration getConfiguration(String name) {
        String filterString = "";
        try {
            filterString = 
                // the registered interface
                "(&(" + Constants.OBJECTCLASS + "=" + IIndicatorBehaviorConfiguration.class.getName() + ")"
                // the configuration name/ id(unique)
                + "("+IConfigurationService.CONFIG_NAME+"=" + name +")"
                + ")";
            // this one is NOT null safe... check to be sure!
            ServiceReference[] srs = Activator.getBundleContext().getServiceReferences(IIndicatorBehaviorConfiguration.class.getName(), filterString);
            
            if(srs == null || srs.length == 0) {
                LOG.info("no configuration service found with name: "+name);
                return null;
            } else if(srs.length > 1) {
                LOG.warn("The configuration service with name: "+name+" is not uniquely identifyable... Using the first one instead!");
            } 
            return (IIndicatorBehaviorConfiguration)Activator.getBundleContext().getService(srs[0]);
            
        } catch (InvalidSyntaxException ex) {
            LOG.info("Apparently the syntax is wrong! of the filter string: "+filterString, ex);
        }
        return null;
    }

    @Override
    public String getName() {
        return xfd.getName();
    }
    
    @Override
    public String toString() {
        return "ExtremeFeedbackDeviceConfiguration -  [ID: "+getName()+"]";
    }
}
