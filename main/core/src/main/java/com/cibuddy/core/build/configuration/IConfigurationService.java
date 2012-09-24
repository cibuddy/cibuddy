package com.cibuddy.core.build.configuration;

/**
 * 
 * @author Mirko Jahn <mirkojahn@gmail.com>
 * @version 1.0
 * @since 1.0
 */
public interface IConfigurationService {
    /** service property name key of the exposed configuration, refers to the value of the {@link IConfigurationService#getName() } method. */
    String CONFIG_NAME = "config.name";
    /** service source key of the configuration, usually refers to a file path */
    String CONFIG_SOURCE = "config.source";
    
    /**
     * The unique name of this particular configuration.
     * 
     * @return human readable string (ideally)
     */
    String getName();
}
