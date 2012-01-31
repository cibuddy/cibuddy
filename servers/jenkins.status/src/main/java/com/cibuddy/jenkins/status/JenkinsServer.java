package com.cibuddy.jenkins.status;

import com.cibuddy.core.build.server.IBuildProject;
import com.cibuddy.core.build.server.IServer;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
    public static final String  JENKINS_SERVER_JOBS = "/api/xml?tree=jobs[name,url,color,lastBuild[result,id,building,number,actions[causes[userName]]]]";
    
    private URI uri;
    private String name;
    private String source;
    private HashMap<String,IBuildProject> projects = new HashMap<String,IBuildProject>();
    private String jenkins_version = null;
    
    public JenkinsServer(String source, String serverName, URI serverUri){
        try {
            this.uri = serverUri;
            this.name = serverName;
            this.source = source;
            LOG.debug("New Jenkins Server to include under"+serverUri);
            
            HttpClient client = new DefaultHttpClient();

            HttpClient httpclient = new DefaultHttpClient();
            
            URI u = new URI(serverUri.toString()+JENKINS_SERVER_JOBS);
//            System.out.println(u.toString());
            HttpGet httpget = new HttpGet(u);
            HttpResponse response = httpclient.execute(httpget);
            
            HeaderIterator it = response.headerIterator("X-Jenkins");
            while (it.hasNext()) {
                jenkins_version = ((BufferedHeader)it.next()).getValue();
                LOG.debug("Jenkins Version identified: "+jenkins_version);
            }
            
            HttpEntity entity = response.getEntity();
            checkResult(response.getStatusLine().getStatusCode());
            if (entity != null) {
                byte[] bytes = EntityUtils.toByteArray(entity);
                JobParser parser = new JobParser(bytes);
                parser.parseDocument();
                projects = parser.getBuildJobs();
            }
        } catch (Exception ex) {
            // do nothing
            LOG.warn("Problem occured while adding Jenkins Server to configuration", ex);
        } 
    }

    private static void checkResult(int i) throws IOException {
        if(i/100!=2)
            throw new IOException();
    }
    
    @Override
    public URI getBuildServerURL() {
        return uri;
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
    public Collection<IBuildProject> getBuildProjects() {
        return projects.values();
    }

    @Override
    public IBuildProject getProjectByName(String projectName) {
        return projects.get(projectName);
    }

    @Override
    public String getBuildServerAlias() {
        return name;
    }

    @Override
    public String getBuildServerSource() {
        return source;
    }
    
}
