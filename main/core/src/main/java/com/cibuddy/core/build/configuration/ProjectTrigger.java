package com.cibuddy.core.build.configuration;

/**
 *
 * @author mirkojahn
 */
public interface ProjectTrigger {
    
//    BuildStatus getTriggerBuildStatus();
//    
    boolean forceUpdate();
    boolean validate();
    
//    List<IBuildProject> getBuildProjectToCheck();
}
