package com.cibuddy.project.configuration.impl;

import com.cibuddy.core.build.indicator.IBuildStatusIndicator;
import com.cibuddy.core.build.server.BuildStatus;
import com.cibuddy.core.build.server.IBuildProject;
import com.cibuddy.core.build.server.IServer;
import com.cibuddy.project.configuration.schema.ProjectType;
import com.cibuddy.project.configuration.schema.SimpleStatusIndicatorType;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class to not overload the rest of the code with utility code.
 * 
 * @author mirkojahn
 */
public class Helper {

    private static final Logger LOG = LoggerFactory.getLogger(Helper.class);
    
    static IBuildProject getProject(ProjectType pt, boolean update) {
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
                         if(update) {
                             project.refresh();
                         }
                         return project;
                     } else if(pt.getName() != null && pt.getServerAlias() != null && pt.getServerSource() != null 
                             && pt.getName().equalsIgnoreCase(project.getProjectName())
                             && pt.getServerAlias().equalsIgnoreCase(server.getBuildServerAlias())
                             && pt.getServerSource().equalsIgnoreCase(server.getBuildServerSource())) {
                         if(update) {
                             project.refresh();
                         }
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

    static boolean checkCondition(String condition, List<BuildStatus> configuredBuildStatiMatches, BuildStatus[] result) {
        int hits = 0;
        for(int i=0;i<result.length;i++){
            Iterator<BuildStatus> iter = configuredBuildStatiMatches.iterator();
            // check for each possible match configured
            while(iter.hasNext()){
                BuildStatus bs = iter.next();
                if(result[i].equals(bs)){
                    hits++;
                    LOG.debug("match found: actual status [{}] vs. expected status [{}]", result[i], bs);
                    break;
                } else {
                    LOG.debug("no match: actual status [{}] vs. expected status [{}]", result[i], bs);
                }
            }
        }
        LOG.debug("hits found: {}",hits);
        if(condition.equalsIgnoreCase("all")){
            LOG.debug("checking if all are matching");
            if(hits == result.length){
                return true;
            } else {
                LOG.debug("false: {} vs. {}", hits, result.length);
            }
        } else if(condition.contains(":")){
            LOG.debug("boundary matching rule identified");
            String[] boundaries = condition.split(":");
            if(boundaries[1].contains("*")) {
                LOG.debug("matching rule with unbound upper bound identified.");
                if(Integer.parseInt(boundaries[0]) == hits) {
                    LOG.debug("matching rule!");
                    return true;
                }
            } else {
                float boundary = Integer.parseInt(boundaries[0]) / Integer.parseInt(boundaries[1]);
                float found = hits/result.length;
                if(found <= boundary) {
                    LOG.debug("matching rule: {}/{} <= {}/{}", new Object [] {boundaries[0], boundaries[1], String.valueOf(hits), String.valueOf(result.length)});
                    return true;
                }
            }
        } 
        return false;
    }

    static void indicateLight(String indicate, IBuildStatusIndicator ibsi) {
        LOG.debug("trying to indicate: "+indicate);
        SimpleStatusIndicatorTrigger ssit = new SimpleStatusIndicatorTrigger(ibsi,StatusAction.get(indicate));
        ssit.enableStatusIndicator();
    }
}
