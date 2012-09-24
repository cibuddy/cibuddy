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
