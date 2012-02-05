package com.cibuddy.project.configuration;

import com.cibuddy.core.build.server.BuildStatus;

/**
 *
 * @author mirkojahn
 */
public interface ProjectTrigger {
    
    /**
     * Indicates if this trigger has to update the content of a project every time
     * check is executed.
     * 
     * @return true in case an update has to be enforced by the trigger.check().
     */
    boolean forceUpdate();
    
    /**
     * This updates the status from the associated project. The project
     * is still in charge here to update it cached content to safe load and 
     * bandwidth. Only if forceUpdate is enable, the trigger forces the project
     * to actually update its content.
     * 
     */
    void check();
    
    /**
     * Provides the result of the last check.
     * 
     * @return the status of the last update call.
     */
    BuildStatus getLastBuildStatus();
    
    //String getLastBuildResult();
}
