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

/**
 * Representation of a build project.
 * 
 * This interface represents the minimal information required to handle a project. 
 * 
 * @author mirkojahn
 * @version 1.0
 * @since 1.0
 */
public interface IProject {
    
    
    /**
     * The unique id of a project on a build server.
     * 
     * This could be the same as the project name, however there must be a 
     * uniqueness constraint, because this will be used by the individual build
     * server tests to obtain the correct project.
     * 
     * @return unique project id on the build server. In many case the same as the project name.
     */
    String getProjectId();
    
    /**
     * The server hosting this project.
     * 
     * The server where this project could be retrieved. The server instance is
     * also used for authentication requests, so the server should match these
     * requirements in order to fulfill the job.
     * 
     * @return Server to use for accessing the project.
     */
    IServer getServer();
    
    /**
     * Core method for retrieving the current status of the build for a project.
     * 
     * This is the main method one has to call when the status of a project has
     * to be determined or used. You could influence how aggressive the update of
     * the status is with the flag, where a <code>false</code> leaves the update
     * mechanism to the <code>IProject</code> implementer class.
     * 
     * @param forceUpdate make sure the state is the latest available if set to true.
     * @return the state of the project
     * @since 1.0
     * @see #obtainProjectState() 
     */
    IProjectState obtainProjectState(boolean forceUpdate);
    
    
    /**
     * Core method for retrieving the current status of the build for a project.
     * 
     * This is the main method one has to call when the status of a project has
     * to be determined. Internally this one most likely delegates to the overwritten
     * version with the boolean, using <code>false</code> as the parameter to 
     * leave the decision weather a cached state should be used or a new one has
     * to be fetched over the network.
     * 
     * @return the state of the project.
     * @since 1.0
     * @see #obtainProjectState(boolean) 
     */
    IProjectState obtainProjectState();
    
}
