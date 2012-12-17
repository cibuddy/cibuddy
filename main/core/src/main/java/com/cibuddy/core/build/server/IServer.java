/*
 * Copyright (C) 2012 Mirko Jahn <mirkojahn@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cibuddy.core.build.server;

import com.cibuddy.core.build.ProjectSetupException;
import com.cibuddy.core.security.AuthenticationException;
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
    
    URI getBuildServerURI() throws ProjectSetupException;
    
    String getBuildServerType() throws ProjectSetupException;
    
    String getBuildServerSource();
    
    String getBuildServerVersion() throws ProjectSetupException;
    
    String getBuildServerAlias() throws ProjectSetupException;
        
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
    IProjectState getProjectState(String projectId) throws AuthenticationException, ProjectSetupException;
    
    IProject getProject(String projectId) throws ProjectSetupException;
}
