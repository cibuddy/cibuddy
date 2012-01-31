package com.cibuddy.core.build.configuration.helper;

import com.cibuddy.core.build.configuration.BuildTestConfiguration;
import com.cibuddy.core.build.configuration.ProjectTrigger;
import com.cibuddy.core.build.configuration.StatusIndicatorTrigger;
import java.util.List;

/**
 *
 * @author mirkojahn
 */
public class DefaultBuildTestConfiguration implements BuildTestConfiguration {

    private List<ProjectTrigger> pTriggers;
    private List<StatusIndicatorTrigger> iTriggers;
    private String alias;
    
    public DefaultBuildTestConfiguration(List<ProjectTrigger> projectTriggers,
            List<StatusIndicatorTrigger> statusIndicatorTriggers, String alias) {
        pTriggers = projectTriggers;
        iTriggers = statusIndicatorTriggers;
        this.alias = alias;
    }
    
    @Override
    public List<ProjectTrigger> getProjectTriggers() {
        return pTriggers;
    }

    @Override
    public List<StatusIndicatorTrigger> getStatusIndicatorTriggers() {
        return iTriggers;
    }

    @Override
    public String getAlias() {
        return alias;
    }
    
}
