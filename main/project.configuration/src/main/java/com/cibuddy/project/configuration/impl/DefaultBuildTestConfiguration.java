package com.cibuddy.project.configuration.impl;

import com.cibuddy.core.build.configuration.BuildTestConfiguration;
import com.cibuddy.core.build.indicator.IBuildStatusIndicator;
import com.cibuddy.core.build.server.BuildStatus;
import com.cibuddy.core.build.server.IBuildProject;
import com.cibuddy.project.configuration.schema.ActionIndicatorType;
import com.cibuddy.project.configuration.schema.BuildTestConfigurationType;
import com.cibuddy.project.configuration.schema.ProjectType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mirkojahn
 */
public class DefaultBuildTestConfiguration implements BuildTestConfiguration {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultBuildTestConfiguration.class);
    private BuildTestConfigurationType bct;
    
    public DefaultBuildTestConfiguration(BuildTestConfigurationType bct) {
        this.bct = bct;
    }

    @Override
    public String getAlias() {
        return bct.getAlias();
    }

    @Override
    public void update() {
        // get the "build light"
        IBuildStatusIndicator ibsi = Helper.getStatusIndicator(bct.getTrigger().getStatusIndicator());
        
        // get the results of the projects to test for
        BuildStatus[] result = new BuildStatus[bct.getProjects().getProject().size()];
        Iterator<ProjectType> projectIterator = bct.getProjects().getProject().iterator();
        int i = 0;
        while(projectIterator.hasNext()){
            ProjectType pt = projectIterator.next();
            IBuildProject ibp = Helper.getProject(pt,true);
            if(ibp != null){
                SimpleProjectTrigger spt = new SimpleProjectTrigger(ibp);
                spt.check();
                LOG.debug("Project found with current status: "+spt.getLastBuildStatus());
                result[i] = spt.getLastBuildStatus();
            } else {
                LOG.debug("no project found... indicating unknown as build status.");
                result[i] = BuildStatus.unknown;
            }
            i++;
        }
        
        // analyse the results according to the defined actions
        Iterator<ActionIndicatorType> aitIterator = bct.getTrigger().getAction().iterator();
        while(aitIterator.hasNext()){
            ActionIndicatorType ait = aitIterator.next();
            String status = ait.getStatus();
            if(status.equalsIgnoreCase("default")){
                String indicate = ait.getIndicate();
                Helper.indicateLight(indicate, ibsi);
                return;
            } else {
                BuildStatus bStatus = BuildStatus.valueOf(status);
                if(Helper.checkCondition(ait.getCondition(), bStatus, result)){
                    String indicate = ait.getIndicate();
                    Helper.indicateLight(indicate, ibsi);
                    // we're done, exit here.
                    return;
                }
            }
            
        }
    }

    @Override
    public List<IBuildProject> getProjects() {
        List<IBuildProject> ibps = new ArrayList<IBuildProject>();
        Iterator<ProjectType> projectIterator = bct.getProjects().getProject().iterator();
        int i = 0;
        while(projectIterator.hasNext()){
            i++;
            ProjectType pt = projectIterator.next();
            IBuildProject ibp = Helper.getProject(pt, true);
            if(ibp != null){
                ibps.add(ibp);
            } 
        }
        return ibps;
    }
    
}
