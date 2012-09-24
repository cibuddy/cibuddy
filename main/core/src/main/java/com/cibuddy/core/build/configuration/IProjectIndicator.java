package com.cibuddy.core.build.configuration;

import com.cibuddy.core.build.indicator.IBuildStatusIndicator;

/**
 * Purpose: Identify an IBuildStatusIndicator associated with an IProjectSetup.
 * 
 * @author Mirko Jahn <mirkojahn@gmail.com>
 * @version 1.0
 * @since 1.0
 */
public interface IProjectIndicator {
    
    IBuildStatusIndicator getProjectSetupIndicator();
    
    /**
     * This method provides a generic mechanism to obtain the indicator.
     * 
     * However not everything could be expressed as a filter, so this method
     * might also return null. In such case, the class should provide the id of
     * the service. Otherwise a mapping will not be possible, unless the indicator
     * itself could be returned directly. Something like an export of a configuration
     * might be larger issues here.
     * 
     * @return A string using OSGi's filter definition for identifying an
     *        indicator uniquely.
     * @since 1.0
     */
    String getIndicatorFilter();
    
    /**
     * Obtain the unique id of the indicator.
     * 
     * The implementor could provide this if known or always return something 
     * smaller than 0.
     * 
     * @return -1 for none found and a number &gt;=0 for the known id.
     * @since 1.0
     */
    int getIndicatorId();
}
