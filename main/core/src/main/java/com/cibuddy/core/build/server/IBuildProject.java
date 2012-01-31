package com.cibuddy.core.build.server;

import java.net.URI;

/**
 *
 * @author mirkojahn
 */
public interface IBuildProject {
    
    URI getRootURI();
    String getProjectName();
    void refresh();
    String getBuildStatus();
    BuildStatus getProjectColor();
}
