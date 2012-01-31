package com.cibuddy.core.build.server;

import java.net.URI;
import java.util.Collection;

/**
 *
 * @author mirkojahn
 */
public interface IServer {
    
    String SP_SERVER_URL = "server.url";
    String SP_SERVER_SOURCE = "server.source";
    String SP_SERVER_ALIAS = "server.alias";
    String SP_SERVER_TYPE = "server.type";
    String SP_SERVER_VERSION = "server.version";
    String TYPE_JENKINS_SERVER = "jenkins";
    String TYPE_HUDSON_SERVER = "hudson";
//    String CRUISE_CONTROL_SERVER = "CruiseControl";
//    String TEAM_CITY_SERVER = "TeamCity";
//    String BAMBOO_SERVER = "Bamboo";
    
    URI getBuildServerURL();
    
    String getBuildServerType();
    
    String getBuildServerSource();
    
    String getBuildServerVersion();
    
    String getBuildServerAlias();
    
    Collection<IBuildProject> getBuildProjects();
    
    IBuildProject getProjectByName(String projectName);
}
