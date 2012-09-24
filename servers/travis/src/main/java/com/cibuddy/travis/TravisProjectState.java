package com.cibuddy.travis;

import com.cibuddy.core.build.server.BuildStatus;
import com.cibuddy.core.build.server.IProjectState;
import com.google.gson.Gson;
import java.net.URI;
import java.util.Date;

/**
 *
 * @author Mirko Jahn <mirkojahn@gmail.com>
 * 
 * match this layout
 * 
{
  "public_key": "-----BEGIN RSA PUBLIC KEY-----\nMIGJAoGBANpQDMQYI+uplggbgzNgRuGVkqVbDAuoE3VJPNc8Pf/L4iAqc+oBhr7U\nW7uBPcuV8elqiociYukRzwVVC7VcjUIVPIXel5y6uHyle9WC6ryGhQLmv8ZBLvT0\n0FR8Jo3FOhbYvBC9moYlL/WaiNYdcAJgV7nIDopFu7Xj39ymgw+fAgMBAAE=\n-----END RSA PUBLIC KEY-----\n",
  "slug": "cibuddy/cibuddy",
  "last_build_id": 2374816,
  "last_build_started_at": "2012-09-07T23:00:29Z",
  "last_build_status": 0,
  "last_build_duration": 372,
  "last_build_result": 0,
  "last_build_number": "32",
  "id": 9440,
  "description": "Visualization Project for Continuous Integration Servers",
  "last_build_language": null,
  "last_build_finished_at": "2012-09-07T23:39:48Z"
}
 */
public class TravisProjectState implements IProjectState {

    
    private String public_key;
    private String slug;
    private long last_build_id;
    private Date last_build_started_at;
    private int last_build_status;
    private long last_build_duration;
    private int last_build_number;
    private long id;
    private String description;
    private String last_build_language;
    private Date last_build_finished_at;
    private String etag;
    private final long timestamp;
    
    TravisProjectState() {
        this.timestamp = System.currentTimeMillis();
    }
    
    protected static TravisProjectState builder(String json){
        TravisProjectState project = new Gson().fromJson(json, TravisProjectState.class);
        return project;
    }
    

    @Override
    public String getProjectName() {
        return slug;
    }

    @Override
    public String getBuildStatus() {
        return String.valueOf(last_build_status);
    }

    @Override
    public BuildStatus getProjectColor() {
        if(last_build_status == 0) {
            return BuildStatus.blue;
        } else if(last_build_status > 0) {
            return BuildStatus.red;
        } else {
            return BuildStatus.unknown;
        }
    }
    
    // the holder for the JSON object
    
    
    public String getPublic_key() {
        return public_key;
    }

    public void setPublic_key(String public_key) {
        this.public_key = public_key;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public long getLast_build_id() {
        return last_build_id;
    }

    public void setLast_build_id(long last_build_id) {
        this.last_build_id = last_build_id;
    }

    public Date getLast_build_started_at() {
        return last_build_started_at;
    }

    public void setLast_build_started_at(Date last_build_started_at) {
        this.last_build_started_at = last_build_started_at;
    }

    public int getLast_build_status() {
        return last_build_status;
    }

    public void setLast_build_status(int last_build_status) {
        this.last_build_status = last_build_status;
    }

    public long getLast_build_duration() {
        return last_build_duration;
    }

    public void setLast_build_duration(long last_build_duration) {
        this.last_build_duration = last_build_duration;
    }

    public int getLast_build_number() {
        return last_build_number;
    }

    public void setLast_build_number(int last_build_number) {
        this.last_build_number = last_build_number;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLast_build_language() {
        return last_build_language;
    }

    public void setLast_build_language(String last_build_language) {
        this.last_build_language = last_build_language;
    }

    public Date getLast_build_finished_at() {
        return last_build_finished_at;
    }

    public void setLast_build_finished_at(Date last_build_finished_at) {
        this.last_build_finished_at = last_build_finished_at;
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + (this.slug != null ? this.slug.hashCode() : 0);
        hash = 79 * hash + (int) (this.last_build_id ^ (this.last_build_id >>> 32));
        hash = 79 * hash + this.last_build_number;
        hash = 79 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TravisProjectState other = (TravisProjectState) obj;
        if ((this.slug == null) ? (other.slug != null) : !this.slug.equals(other.slug)) {
            return false;
        }
        if (this.last_build_id != other.last_build_id) {
            return false;
        }
        if (this.last_build_number != other.last_build_number) {
            return false;
        }
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    @Override
    public long getTimeStamp() {
        return timestamp;
    }

    @Override
    public String getProjectId() {
        return slug;
    }

}
