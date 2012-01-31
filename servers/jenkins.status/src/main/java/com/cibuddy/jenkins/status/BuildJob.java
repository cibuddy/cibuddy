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
        if("red".equalsIgnoreCase(lastBuildResult)){
            status =BuildStatus.RED;
        } else if("red_anime".equalsIgnoreCase(lastBuildResult)){
            status =BuildStatus.RED_ANIME;
        } else if("yellow".equalsIgnoreCase(lastBuildResult)){
            status =BuildStatus.YELLOW;
        } else if("yellow_anime".equalsIgnoreCase(lastBuildResult)){
            status =BuildStatus.YELLOW_ANIME;
        } else if("blue".equalsIgnoreCase(lastBuildResult)){
            status =BuildStatus.BLUE;
        } else if("blue_anime".equalsIgnoreCase(lastBuildResult)){
            status =BuildStatus.BLUE_ANIME;
        } else if("disabled".equalsIgnoreCase(lastBuildResult)){
            status =BuildStatus.DISABLED;
        } else if("disabled_anime".equalsIgnoreCase(lastBuildResult)){
            status =BuildStatus.DISABLED_ANIME;
        } else if("aborted".equalsIgnoreCase(lastBuildResult)){
            status =BuildStatus.ABORTED;
        } else if("aborted_anime".equalsIgnoreCase(lastBuildResult)){
            status =BuildStatus.ABORTED_ANIME;
        } else if("notbuilt".equalsIgnoreCase(lastBuildResult)){
            status =BuildStatus.NOTBUILT;
        } else if("notbuilt_anime".equalsIgnoreCase(lastBuildResult)){
            status =BuildStatus.NOTBUILD_ANIME;
        } else {
            // unknown status, use NOTBUILT as default to have minimal impact
            status = BuildStatus.NOTBUILT;
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
