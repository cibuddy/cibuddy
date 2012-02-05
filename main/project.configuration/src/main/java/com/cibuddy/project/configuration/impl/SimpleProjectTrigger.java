package com.cibuddy.project.configuration.impl;

import com.cibuddy.core.build.server.BuildStatus;
import com.cibuddy.core.build.server.IBuildProject;
import com.cibuddy.project.configuration.ProjectTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mirkojahn
 */
public class SimpleProjectTrigger implements ProjectTrigger {
    
    private static final Logger LOG = LoggerFactory.getLogger(SimpleProjectTrigger.class);
    private IBuildProject myProject;
    private BuildStatus myLastBuildStatus = BuildStatus.unknown;
    private boolean update;
    
    public SimpleProjectTrigger(IBuildProject project){
        this(project, false);
    }
    public SimpleProjectTrigger(IBuildProject project, boolean forceUpdate){
        myProject = project;
        update = forceUpdate;
    }

    @Override
    public void check() {
        if(forceUpdate()){
            myProject.refresh();
        }
        LOG.debug("checking project status. Current Value is: {}",myProject.getProjectColor());
        myLastBuildStatus = myProject.getProjectColor();
        
    }

    @Override
    public boolean forceUpdate() {
        return update;
    }
    
    @Override
    public BuildStatus getLastBuildStatus(){
        return myLastBuildStatus;
    }
    
}
