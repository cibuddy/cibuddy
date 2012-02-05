package com.cibuddy.jenkins.status;

import com.cibuddy.core.build.server.BuildStatus;
import com.cibuddy.core.build.server.IBuildProject;
import java.net.URI;

/**
 *
 * @author mirkojahn
 */
public class BuildJob implements IBuildProject {
    
    private String name;
    private URI uri;
    private String color;
    private int buildNumber;
    private String username;
    private String lastBuildResult;

    public BuildJob(){
        
    }
    
    public int getBuildNumber() {
        return buildNumber;
    }

    public String getColor() {
        return color;
    }

    public String getLastBuildResult() {
        return lastBuildResult;
    }

    public URI getUri() {
        return uri;
    }

    public String getUsername() {
        return username;
    }
    
    void setName(String name){
        this.name = name;
    }
    
    void setURI(URI uri){
        this.uri = uri;
    }
    
    void setColor(String color){
        this.color = color;
    }
    
    void setUserName(String name){
        this.username = name;
    }
    
    void setBuildNumber(int number){
        this.buildNumber = number;
    }
    
    void setLastBuildResult(String result){
        this.lastBuildResult = result;
    }

    public URI getRootURI() {
        return getUri();
    }

    public String getProjectName() {
        return name;
    }

    public void refresh() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public BuildStatus getProjectColor() {
        BuildStatus status;
        try {
            status = BuildStatus.get(color);
        } catch (Exception e) {
            status = BuildStatus.unknown;
        }
        return status;
    }

    public String getBuildStatus() {
        return lastBuildResult;
    }

    @Override
    public String toString() {
        return "BuildJob{" + "name=" + name + ", uri=" + uri + ", color=" + color + ", buildNumber=" + buildNumber + ", username=" + username + ", lastBuildResult=" + lastBuildResult + '}';
    }
    

}
