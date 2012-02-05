package com.cibuddy.project.configuration.impl;

import com.cibuddy.core.build.indicator.IBuildStatusIndicator;
import com.cibuddy.core.build.server.BuildStatus;
import com.cibuddy.core.build.server.IBuildProject;
import com.cibuddy.core.build.server.IServer;
import com.cibuddy.project.configuration.schema.ProjectType;
import com.cibuddy.project.configuration.schema.SimpleStatusIndicatorType;
import java.util.Collection;
import java.util.Iterator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to not overload the rest of the code with utility code.
 * 
 * @author mirkojahn
 */
public class Helper {

    private static final Logger LOG = LoggerFactory.getLogger(Helper.class);
    // FIXME: don't forget to update the project with the heartbeat!!!
    
    static IBuildProject getProject(ProjectType pt) {
        Object[] servers = Activator.getServerTracker().getServices();
        if(servers != null && servers.length > 0) {
            for(int i=0;i<servers.length;i++) {
                IServer server = ((IServer)servers[i]);
                 Collection<IBuildProject> projects = server.getBuildProjects();
                 Iterator<IBuildProject> iter = projects.iterator();
                 while(iter.hasNext()) {
                     IBuildProject project = iter.next();
                     if(pt.getProjectURL() != null 
                             && pt.getProjectURL().equalsIgnoreCase(project.getRootURI().toString())) {
                         LOG.debug("matching project: {}", project.toString());
                         return project;
                     } else if(pt.getName() != null && pt.getServerAlias() != null && pt.getServerSource() != null 
                             && pt.getName().equalsIgnoreCase(project.getProjectName())
                             && pt.getServerAlias().equalsIgnoreCase(server.getBuildServerAlias())
                             && pt.getServerSource().equalsIgnoreCase(server.getBuildServerSource())) {
                         return project;
                     } else {
                         LOG.debug("Project is not matching: {}  against {}",pt.getProjectURL(),project.getRootURI());
                     }
                 }
            }
        }
        return null;
    }

    static IBuildStatusIndicator getStatusIndicator(SimpleStatusIndicatorType statusIndicator) {
        Object[] ibsis = Activator.getBuildIndicatorTracker().getServices();
        if(ibsis != null && ibsis.length>0) {
            if(statusIndicator.getId() != null 
                    && statusIndicator.getId().intValue() < ibsis.length) {
                LOG.info("Found StatusIndicator to use in configuration: {}",ibsis[statusIndicator.getId().intValue()]);
                return (IBuildStatusIndicator)ibsis[statusIndicator.getId().intValue()];
            }
            for(int i=0;i<ibsis.length;i++) {
                IBuildStatusIndicator ibsi = (IBuildStatusIndicator)ibsis[i];
                if(ibsi.getComponentId() != null && ibsi.getIndicatorId() != null
                        && ibsi.getComponentId().equalsIgnoreCase(statusIndicator.getComponendId())
                        && ibsi.getIndicatorId().equalsIgnoreCase(statusIndicator.getIndicatorId())){
                    LOG.info("Found StatusIndicator to use in configuration: {}",ibsi);
                    return ibsi;
                }
            }
        }
        return null;
    }

    static boolean checkCondition(String condition, BuildStatus bStatus, BuildStatus[] result) {
        int hits = 0;
        for(int i=0;i<result.length;i++){
            if(result[i].equals(bStatus)){
                hits++;
            } else {
                LOG.debug("no match: {} vs. {}", result[i], bStatus);
            }
        }
        LOG.trace("hits found: {}",hits);
        if(condition.equalsIgnoreCase("all")){
            LOG.trace("checking if all are matching");
            if(hits == result.length){
                return true;
            } else {
                LOG.trace("false: {} vs. {}", hits, result.length);
            }
        } else if(condition.contains(":")){
            LOG.trace("boundary matching rule identified");
            String[] boundaries = condition.split(":");
            if(boundaries[1].contains("*")) {
                LOG.trace("matching rule with unbound upper bound identified.");
                if(Integer.parseInt(boundaries[0]) == hits) {
                    LOG.trace("matching rule!");
                    return true;
                }
            } else {
                LOG.trace("matching rule with fixed upper bound identified.");
                if(Integer.parseInt(boundaries[0]) == hits && Integer.parseInt(boundaries[1]) == result.length) {
                    LOG.trace("matching rule!");
                    return true;
                }
            }
        } else if(condition.contains("%")){
            LOG.trace("percentage matching rule identified");
            // we need to have an indicator if this is a upper or lower boundary (lower is more intuitive)
            LOG.warn("conditon not supported yet. Will be ignored => not matching.");
        }
                // FIXME: also enable percentage
        return false;
    }

    static void indicateLight(String indicate, IBuildStatusIndicator ibsi) {
        System.out.println("to indicate: "+indicate);
        SimpleStatusIndicatorTrigger ssit = new SimpleStatusIndicatorTrigger(ibsi,StatusAction.get(indicate));
        ssit.enableStatusIndicator();
    }
}
