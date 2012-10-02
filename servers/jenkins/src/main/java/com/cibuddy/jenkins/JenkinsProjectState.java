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
package com.cibuddy.jenkins;

import com.cibuddy.core.build.server.BuildStatus;
import com.cibuddy.core.build.server.IProjectState;
import com.google.gson.Gson;
import java.net.URL;


/**
 *
 * Sample:
{
  "name": "Karaf",
  "lastBuild": {
    "result": "UNSTABLE",
    "number": 1553,
    "building": false,
    "id": "2012-09-12_15-12-24"
  },
  "url": "https://builds.apache.org/job/Karaf/",
  "color": "yellow"
}
 * 
 * 
 * @author mirkojahn
 */
public class JenkinsProjectState implements IProjectState {
    
    // Gson Object properties:
    private String name;
    private URL url;
    private String color;
    private LastBuild lastBuild;
    private String description;
    private final long timestamp;
    
    protected static JenkinsProjectState builder(String json){
        JenkinsProjectState project = new Gson().fromJson(json, JenkinsProjectState.class);
        return project;
    }
    
    // not everyone can create an instance!
    private JenkinsProjectState(){
        this.timestamp = System.currentTimeMillis();
    }
    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    LastBuild getLastBuild() {
        return lastBuild;
    }

    void setLastBuild(LastBuild lastBuild) {
        this.lastBuild = lastBuild;
    }
    
    public int getBuildNumber() {
        return lastBuild.getNumber();
    }

    public String getColor() {
        return color;
    }
    
    void setName(String name){
        this.name = name;
    }
    
    void setColor(String color){
        this.color = color;
    }

    @Override
    public String getProjectName() {
        return name;
    }

    @Override
    public BuildStatus getProjectColor() {
        BuildStatus status;
        try {
            status = BuildStatus.valueOf(color);
            // the build status or unknown in case of null
            if(status == null){
                status = BuildStatus.unknown;
            }
        } catch (Exception e) {
            status = BuildStatus.unknown;
        }
        return status;
    }

    @Override
    public String getBuildStatus() {
        return lastBuild.getResult();
    }

    @Override
    public String toString() {
        return "BuildJob{" + "name=" + name + ", color=" + color + ", buildNumber=" + getBuildNumber() + ", lastBuildResult=" + getBuildStatus() + '}';
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public long getTimeStamp() {
        return timestamp;
    }

    @Override
    public String getProjectId() {
        return getProjectName();
    }
    

}
class LastBuild {
    private String result;
    private int number;
    private boolean building;
    private String id;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean isBuilding() {
        return building;
    }

    public void setBuilding(boolean building) {
        this.building = building;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
