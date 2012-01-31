package com.cibuddy.core.build.indicator;

/**
 *
 * @author mirkojahn
 */
public interface ICustomBuildStatusIndicator extends IBuildStatusIndicator {
    
    void enableCustomStatus(IBuildStatusIndicatorConfiguration config);
}
