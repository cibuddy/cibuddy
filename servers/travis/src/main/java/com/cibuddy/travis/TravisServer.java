package com.cibuddy.travis;

import com.cibuddy.core.build.server.DefaultProjectImpl;
import com.cibuddy.core.build.server.IProject;
import com.cibuddy.core.build.server.IProjectState;
import com.cibuddy.core.build.server.IServer;
import com.cibuddy.core.security.AuthenticationToken;
import java.io.IOException;
import java.net.URI;
import java.util.WeakHashMap;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mirko Jahn <mirkojahn@gmail.com>
 */
public class TravisServer implements IServer {

    private static final Logger LOG = LoggerFactory.getLogger(TravisServer.class);
    private final String name; 
    private final URI serveruri;
    private String source;
    private final String version = "unknown";// not sure how to obtain a version string here
    
    private WeakHashMap<String,TravisProjectState> jobCache = new WeakHashMap<String,TravisProjectState>();
    
    public TravisServer(String source, String name, URI serveruri, AuthenticationToken token) {
        this.name = name;
        this.serveruri = serveruri;
        this.source = source;
        LOG.debug("New Travis-CI Server to include under"+serveruri);
            
    }
    
    @Override
    public URI getBuildServerURL() {
        return serveruri;
    }

    @Override
    public String getBuildServerType() {
        return IServer.TYPE_TRAVIS_CI_SERVER;
    }

    @Override
    public String getBuildServerSource() {
        return source;
    }

    @Override
    public String getBuildServerVersion() {
        return version;
    }

    @Override
    public String getBuildServerAlias() {
        return name;
    }

    /**
     * Get the "build project" by name.
     * 
     * @param projectName or "slug" in case of Travis-CI. Usually the combination of "owner/repository"
     * @return The buildProject representation
     */
    @Override
    public IProjectState getProjectState(String projectName) {
        try {
            // http://travis-ci.org/cibuddy/cibuddy.json
            return getProject(projectName, null);
        } catch (Exception ex) {
            LOG.warn("Trying to obtain "+projectName+" from "+serveruri.toString()+" failed.", ex);
        }
        return null;
    }
    
    /**
     * 
     * @param projectName in case of travis this is called slug ("owner/project")
     * @param etag if not null, a cache lookup will be done
     * @return
     * @throws Exception 
     */
    public TravisProjectState getProject(String projectName, String etag) throws Exception {
        // this assumes the concatination works (might be the source of a bug)
        String serverURIstring = serveruri.toString();
        if(!serverURIstring.endsWith("/")){
            serverURIstring = serverURIstring+"/";
        }
        URI u = new URI(serverURIstring + projectName+".json");
        HttpClient httpclient = new DefaultHttpClient();
        TravisProjectState bj = jobCache.get(projectName);
        if(etag != null && bj != null){
            // check the cache first
            HttpHead head = new HttpHead(u);
            HttpResponse response = httpclient.execute(head);
            Header h = response.getFirstHeader("Etag");
            if(bj.getEtag().equals(h.getValue())) {
                // we found a cache hit, stop here
                return bj;
            }
        }
        
        HttpGet httpget = new HttpGet(u);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        checkResult(response.getStatusLine().getStatusCode());
        if (entity != null) {
            String json = EntityUtils.toString(entity, "utf-8");
            TravisProjectState tbp = TravisProjectState.builder(json);
            if(tbp != null) {
                Header h = response.getFirstHeader("Etag");
                if(h != null && h.getValue() != null){
                    tbp.setEtag(h.getValue());
                    jobCache.put(projectName, tbp);
                } else {
                    LOG.warn("Something is ODD with the caching mechanism. No Etags are found for results. This is a bug!!!");
                }
            }
            return tbp;
        }
        return null;
    }
    
    // so simple do not expose or test
    private void checkResult(int i) throws IOException {
        if(i/100!=2){
            throw new IOException();
        }
    }

    @Override
    public IProject getProject(String projectId) {
        return new DefaultProjectImpl(projectId, this);
    }
}
