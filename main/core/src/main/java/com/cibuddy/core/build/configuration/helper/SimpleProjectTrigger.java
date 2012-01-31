package com.cibuddy.core.build.configuration.helper;

import com.cibuddy.core.build.configuration.ProjectTrigger;
import com.cibuddy.core.build.server.BuildStatus;
import com.cibuddy.core.build.server.IBuildProject;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author mirkojahn
 */
public class SimpleProjectTrigger implements ProjectTrigger {
    
    private List<IBuildProject> myProjects;
    private BuildStatus myStatus;
    private boolean update;
    
    public SimpleProjectTrigger(List<IBuildProject> projects, BuildStatus status){
        this(projects, status, false);
    }
    public SimpleProjectTrigger(List<IBuildProject> projects, BuildStatus status, boolean forceUpdate){
        myProjects = projects;
        myStatus = status;
        update = forceUpdate;
    }

    @Override
    public boolean validate() {
        Iterator<IBuildProject> iter = myProjects.iterator();
        while(iter.hasNext()){
            IBuildProject project = iter.next();
            if(forceUpdate()){
                project.refresh();
            }
            if(myStatus.equals(project.getProjectColor())){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean forceUpdate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
}
