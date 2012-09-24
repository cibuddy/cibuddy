package com.cibuddy.core.build.server;

import com.cibuddy.core.security.AuthenticationException;
import com.cibuddy.core.security.AuthenticationToken;
import java.net.URI;

/**
 * Representation of a build server instance.
 * 
 * No matter what backend server you want to query for build results, they all
 * are exposed using this interface.
 * 
 * @author mirkojahn
 * @since 1.0
 * @version 1.0
 */
public interface IServer {
    
    String SP_SERVER_URL = "server.url";
    String SP_SERVER_SOURCE = "server.source";
    String SP_SERVER_ALIAS = "server.alias";
    String SP_SERVER_TYPE = "server.type";
    String SP_SERVER_VERSION = "server.version";
    String TYPE_JENKINS_SERVER = "Jenkins";
    String TYPE_HUDSON_SERVER = "Hudson";
    String TYPE_TRAVIS_CI_SERVER = "Travis-CI";
    String TYPE_CRUISE_CONTROL_SERVER = "CruiseControl";
    String TYPE_TEAM_CITY_SERVER = "TeamCity";
    String TYPE_BAMBOO_SERVER = "Bamboo";
    
    URI getBuildServerURL();
    
    String getBuildServerType();
    
    String getBuildServerSource();
    
    String getBuildServerVersion();
    
    String getBuildServerAlias();
        
    /**
     * Obtains a specific project from the server.
     * 
     * This method must always provide reasonably accurate data about the status 
     * of a given project. If this data is remotely triggered, on demand loaded,
     * cached and only re-fetched in a defined time interval is implementation
     * specific. However, this should be implemented in a most efficient way by
     * waging network traffic against memory footprint.
     * 
     * @param projectId the unique name of the project on this server.
     * @throws AuthenticationException in case of failed authentication.
     * @return the state of the given project or null for none found or accessible.
     */
    IProjectState getProjectState(String projectId) throws AuthenticationException;
    
    IProject getProject(String projectId);
}
