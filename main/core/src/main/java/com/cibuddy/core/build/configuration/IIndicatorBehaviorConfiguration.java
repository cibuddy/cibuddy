package com.cibuddy.core.build.configuration;

import com.cibuddy.core.build.indicator.StatusAction;


/**
 *
 * @author Mirko Jahn <mirkojahn@gmail.com>
 */
public interface IIndicatorBehaviorConfiguration extends IConfigurationService {
    
    StatusAction evaluate(IProjectSetup setup);
}
