package com.cibuddy.core.build.configuration;

import com.cibuddy.core.build.server.IBuildProject;
import java.util.List;

/**
 * The BuildTestConfiguration Interface represents a service interface exposed 
 * by any bundle that offers build configurations CIBuddy is capable of testing 
 * for. The bundle has to expose a service with this interface and the two properties
 * with the keys defined identical to the Strings defined in this interface. The
 * core bundle will periodically collect the available services and call the 
 * update method, which then has to update the configuration content and indicate
 * the build status. This is part of the implementation to ensure that the correct
 * status is indicated with the configured build indicators (usually the build lights).
 * 
 * @author mirkojahn
 */
public interface BuildTestConfiguration {
    
    String BUILD_TEST_ALIAS = "build.test.alias";
    String BUILD_TEST_SOURCE = "build.test.source";
    
    /**
     * Obtain the list of Projects associated with this particular build configuration.
     * 
     * @return all projects of this build configuration.
     */
    List<IBuildProject> getProjects();
    
    /**
     * A unique Alias of this particular configuration.
     * 
     * @return human readable alias string
     */
    String getAlias();
    
    /**
     * Update the Projects contained in this configuration and indicate its status
     * with the configured build lights. This will most likely result in a network 
     * look-up of the build status for the individual projects.
     */
    void update();
}
