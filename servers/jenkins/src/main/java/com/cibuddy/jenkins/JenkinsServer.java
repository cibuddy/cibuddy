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

import com.cibuddy.core.build.server.DefaultProjectImpl;
import com.cibuddy.core.build.server.IProject;
import com.cibuddy.core.build.server.IProjectState;
import com.cibuddy.core.build.server.IServer;
import com.cibuddy.core.security.AuthenticationToken;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BufferedHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @author mirkojahn
 */
public class JenkinsServer implements IServer {
    
    private static final Logger LOG = LoggerFactory.getLogger(JenkinsServer.class);
    public static final String JENKINS_HEADER = "X-Jenkins";
    public static final String JENKINS_SERVER_JOB = "/api/xml?tree=name,url,color,lastBuild[result,id,building,number,actions[causes[userName]]]";
    public static final String JENKINS_SERVER_JOB_JSON = "/api/json?tree=name,url,color,lastBuild[result,id,building,number]";
    public static final String JENKINS_SERVER_JOBS = "/api/xml?tree=jobs["+JENKINS_SERVER_JOB+"]";
    public static final String JENKINS_PROJECT_COLOR = "/api/xml?xpath=/*/color/text()";
    
    private URI serverUri;
    private String name;
    private String source;
    private String jenkins_version = null;
    
    public JenkinsServer(String source, String serverName, URI serverUri, AuthenticationToken token){
        try {
            this.serverUri = serverUri;
            this.name = serverName;
            this.source = source;
            LOG.debug("New Jenkins Server to include under"+serverUri);
            
            // check if Jenkins is reachable and in fact a jenkins server
            
            HttpClient httpclient = new DefaultHttpClient();
            HttpHead head = new HttpHead(serverUri);
            HttpResponse response = httpclient.execute(head);
            HeaderIterator it = response.headerIterator(JENKINS_HEADER);
            while (it.hasNext()) {
                jenkins_version = ((BufferedHeader)it.next()).getValue();
                LOG.debug("Jenkins Version identified: "+jenkins_version);
            }
        } catch (Exception ex) {
            // do nothing
            LOG.warn("Problem occured while adding Jenkins Server to configuration", ex);
        } 
    }
    
    private static void checkResult(int i) throws IOException {
        if(i/100!=2){
            throw new IOException("Server didn't respond with a status indicating success. Aborting now due to status: "+i);
        }
    }
    
    @Override
    public URI getBuildServerURI() {
        return serverUri;
    }

    @Override
    public String getBuildServerType() {
        return IServer.TYPE_JENKINS_SERVER;
    }
    
    @Override
    public String getBuildServerVersion() {
        return jenkins_version;
    }

    @Override
    public IProjectState getProjectState(String projectName) {
        try {
            String requestURI = getURLStringforProjectName(projectName, this)+JENKINS_SERVER_JOB_JSON;
            // get the project
            try {
                HttpClient httpclient = new DefaultHttpClient();

                HttpGet httpget = new HttpGet(new URI(requestURI));
                HttpResponse response = httpclient.execute(httpget);

                HttpEntity entity = response.getEntity();
                checkResult(response.getStatusLine().getStatusCode());
                if (entity != null) {
                    String json = EntityUtils.toString(entity, "utf-8");
                    return JenkinsProjectState.builder(json);
                }
            } catch (URISyntaxException ex) {
                java.util.logging.Logger.getLogger(JenkinsServer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException e) {
                LOG.info("Problem occured while checking Jenkins Server:"+requestURI,e);
            }
            
        } catch (UnsupportedEncodingException ex) {
            LOG.warn("Couldn't encode project URL for Jenkins CI build server project called: "+projectName, ex);
        } catch (MalformedURLException ex) {
            LOG.warn("Malformed project URL for Jenkins CI build server project called: "+projectName, ex);
        }
        return null;
    }

    @Override
    public String getBuildServerAlias() {
        return name;
    }

    @Override
    public String getBuildServerSource() {
        return source;
    }
    
    static String getURLStringforProjectName(String projectName, IServer server) throws UnsupportedEncodingException, MalformedURLException{
        String serverURIstring = server.getBuildServerURI().toURL().toExternalForm();
        if(!serverURIstring.endsWith("/")){
            serverURIstring = serverURIstring+"/";
         }
        // url encode name
        String projectNameEncoded = URLEncoder.encode(projectName, "UTF-8").replace("+", "%20");
        String projectString = new URL(serverURIstring+"job/"+projectNameEncoded).toString();
        return projectString;
    }

    @Override
    public IProject getProject(String projectId) {
        return new DefaultProjectImpl(projectId, this);
    }
    
}
