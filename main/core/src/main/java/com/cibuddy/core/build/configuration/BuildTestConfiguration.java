package com.cibuddy.core.build.configuration;

import java.util.List;

/**
 *
 * @author mirkojahn
 */
public interface BuildTestConfiguration {
    
    List<ProjectTrigger> getProjectTriggers();
    
    List<StatusIndicatorTrigger> getStatusIndicatorTriggers();
    
    String getAlias();
    
}
