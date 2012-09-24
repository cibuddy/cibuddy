package com.cibuddy.core.build.server;

/**
 *
 * @author Mirko Jahn <mirkojahn@gmail.com>
 */
public interface IProjectState {
    
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
     * The human readable name of the project.
     * 
     * @return the name of the project
     */
    String getProjectName();
    
    /**
     * The description of the project (if available).
     * 
     * @return Human readable description of the project.
     */
    String getDescription();
    
    /**
     * String representation of the build status.
     * 
     * This interface is important in case the native status could not be mapped
     * to a status exposed by the {@link BuildStatus} interface. 
     * 
     * @return Status string of the latest build in the native build servers 
     *      string representation.
     */
    String getBuildStatus();
    
    /**
     * The colorized, visual representation of the projects build status.
     * 
     * Not all build system employ the same color scheme and thus are compatible.
     * However, this method is supposed to return the closest match possible. The
     * {@link BuildStatus} types are adapted from 
     * <a href="http://jenkins-ci.org/">Jenkins-CI</a>, so this should look familiar.
     * 
     * @return colored build status of the latest build
     */
    BuildStatus getProjectColor();
    
    
    /**
     * State Timestamp.
     * 
     * The timestamp when this state was captured. This is used for comparisons
     * between cached states as well as to decide if and when to obtain a new
     * state snapshot.
     * 
     * @return timestamp in ms since Epoch (including TZ correction).
     */
    long getTimeStamp();
}
