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

import com.cibuddy.core.security.AuthenticationException;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mirko Jahn <mirkojahn@gmail.com>
 */
public class DefaultProjectImpl implements IProject {
    private final IServer server;
    private final String projectId;
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(DefaultProjectImpl.class);
    
    public DefaultProjectImpl(String projectId, IServer server) {
        this.server = server;
        this.projectId = projectId;
    }
    @Override
    public String getProjectId() {
        return projectId;
    }

    @Override
    public IServer getServer() {
        return server;
    }

    @Override
    public IProjectState obtainProjectState(boolean forceUpdate) {
        try {
            return server.getProjectState(projectId);
        } catch (AuthenticationException ex) {
            LOG.warn("Authentication failed. Check your credentials for project: "+projectId+" on Server: "+server.getBuildServerAlias(), ex);
        }
        return null;
    }

    @Override
    public IProjectState obtainProjectState() {
        return obtainProjectState(false);
    }
    
}
