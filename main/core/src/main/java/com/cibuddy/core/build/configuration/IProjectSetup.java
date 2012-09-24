package com.cibuddy.core.build.configuration;

import com.cibuddy.core.build.indicator.IBuildStatusIndicator;
import com.cibuddy.core.build.server.IProject;
import java.util.List;

/**
 * Project setup configuration interface.
 * 
 * The IProjectSetup interface represents a service interface exposed 
 * by any bundle that offers build configurations CIBuddy is capable of testing 
 * for. The bundle has to expose a service with this interface and the two properties
 * with the keys defined identical to the Strings defined in this interface. The
 * core bundle will periodically collect the available services and call the 
 * update method, which then has to update the configuration content and indicate
 * the build status. This is part of the implementation to ensure that the correct
 * status is indicated with the configured build indicators (usually the build lights).
 * 
 * @author mirkojahn
 * @since 1.0
 * @version 1.0
 */
public interface IProjectSetup extends IConfigurationService {
    
    /**
     * Obtain the list of Projects associated with this particular setup.
     * 
     * @return all projects configured.
     */
    List<IProject> getProjects();
    
}
