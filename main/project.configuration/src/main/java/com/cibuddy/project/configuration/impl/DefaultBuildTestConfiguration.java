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

/**
 *
 * @author mirkojahn
 */
public class DefaultBuildTestConfiguration implements BuildTestConfiguration {

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
            IBuildProject ibp = Helper.getProject(pt);
            if(ibp != null){
                System.out.println("xx using project: "+ibp);
                SimpleProjectTrigger spt = new SimpleProjectTrigger(ibp);
                spt.check();
                System.out.println("xx color: "+spt.getLastBuildStatus());
                result[i] = spt.getLastBuildStatus();
            } else {
                System.out.println("no project found... indicating unknown as build status.");
                result[i] = BuildStatus.unknown;
            }
            i++;
        }
        
        // analyse the results according to the defined actions
        Iterator<ActionIndicatorType> aitIterator = bct.getTrigger().getAction().iterator();
        while(aitIterator.hasNext()){
            ActionIndicatorType ait = aitIterator.next();
            String status = ait.getStatus();
            System.out.println("checking for: "+status);
            if(status.equalsIgnoreCase("default")){
                String indicate = ait.getIndicate();
                Helper.indicateLight(indicate, ibsi);
                return;
            } else {
                BuildStatus bStatus = BuildStatus.valueOf(status);
                if(Helper.checkCondition(ait.getCondition(), bStatus, result)){
                    System.out.println("match!");
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
            IBuildProject ibp = Helper.getProject(pt);
            if(ibp != null){
                ibps.add(ibp);
            } 
        }
        return ibps;
    }
    
}
