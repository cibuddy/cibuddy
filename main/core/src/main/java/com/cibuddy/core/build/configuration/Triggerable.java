package com.cibuddy.core.build.configuration;

/**
 * In case an IProjectSetup should be updated regularly, this must be implemented.
 * 
 * @author Mirko Jahn <mirkojahn@gmail.com>
 */
public interface Triggerable {
    String UPDATE_TRIGGER_TYPE = "updateTriggerType";
    
    void updateIndicator();
    
}
