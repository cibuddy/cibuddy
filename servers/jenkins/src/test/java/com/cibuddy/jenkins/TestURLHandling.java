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

import com.cibuddy.core.build.server.IProject;
import com.cibuddy.core.build.server.IProjectState;
import com.cibuddy.core.build.server.IServer;
import com.cibuddy.core.security.AuthenticationException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * This test only serves as a reference and reminder.
 * 
 * <p>Encoding Strings with spaces and special characters usually causes problems,
 * when done wrong. Additionally there are multiple ways of encoding a URL, so
 * this test class serves as a testbed to validate corner cases and illustrate
 * potential problems.</p>
 * 
 * <p>Additionally, handling multiple URL formats and Strings could be difficult
 * to get right, so here is a test for all identified problems as well.
 * 
 * @author Mirko Jahn <mirkojahn@gmail.com>
 */
public class TestURLHandling {
    
    private static Map<String,String> map = new Hashtable<String,String>();
    
    /**
     * setup of the strings to be compared after applying the encoding.
     */
    @BeforeClass
    public static void before () {
        map.put("some%20space%20between", "some space between");
        map.put("some%2Bsign_escaped", "some+sign_escaped");
        map.put("%C3%A4%C3%B6%C3%BC%C3%9F", "äöüß");
    }
    
    /**
     * Only test method checking for the compatibility of the created strings.
     * 
     * @throws UnsupportedEncodingException 
     */
    @Test
    public void testURL() throws UnsupportedEncodingException {
        Set<Entry<String, String>> entrySet = map.entrySet();
        Iterator<Entry<String, String>> iter = entrySet.iterator();
        while(iter.hasNext()){
            Entry<String, String> next = iter.next();
            // not nice, but the easiest way I came up with so far (without 3rd parties) or using URI
            String enc = URLEncoder.encode(next.getValue(),"UTF-8").replace("+", "%20");
            System.out.println("exp: "+next.getKey()+" - got: "+enc);
            Assert.assertEquals("URL encoding failed for this pair!",
                    next.getKey(), enc);
        }
    }
    
    @Test(expected=java.lang.NullPointerException.class)
    public void testURLCreation4NullServer() throws Exception {
        System.out.println(JenkinsServer.getURLStringforProjectName("myProject", null));
        Assert.fail();
    }
    
    @Test
    public void testURLCreation4PlainServer1() throws Exception {
        IServer server = new MockServer();
        String url = JenkinsServer.getURLStringforProjectName("myProject", server);
        Assert.assertEquals("The url string is not identical","http://localhost:8080/job/myProject", url);
    }
    
    @Test
    public void testURLCreation4PlainServer2() throws Exception {
        MockServer server = new MockServer();
        server.serverURI = new URI("http://localhost:8080/"); // trailing slash
        String url = JenkinsServer.getURLStringforProjectName("myProject", server);
        Assert.assertEquals("The url string is not identical","http://localhost:8080/job/myProject", url);
    }
    
    @Test
    public void testURLCreation4ServerWithPath1() throws Exception {
        MockServer server = new MockServer();
        server.serverURI = new URI("http://localhost:8080/jenkins");
        String url = JenkinsServer.getURLStringforProjectName("myProject", server);
        Assert.assertEquals("The url string is not identical","http://localhost:8080/jenkins/job/myProject", url);
    }
    
    
    @Test
    public void testURLCreation4ServerWithPath2() throws Exception {
        MockServer server = new MockServer();
        server.serverURI = new URI("http://localhost:8080/jenkins/");// trailing slash
        String url = JenkinsServer.getURLStringforProjectName("myProject", server);
        Assert.assertEquals("The url string is not identical","http://localhost:8080/jenkins/job/myProject", url);
    }
    
    class MockServer implements IServer {
        URI serverURI;
        String serverType;
        String serverSource;
        String serverVersion;
        String serverAlias;
        IProjectState projectState;
        IProject project;
        
        MockServer() throws URISyntaxException{
            serverURI = new URI("http://localhost:8080");
            serverType = IServer.TYPE_JENKINS_SERVER;
            serverSource = "/test/server/source.file";
            serverVersion = "1.001";
            serverAlias = "JUnitTestServer";
        }

        @Override
        public URI getBuildServerURI() {
            return serverURI;
        }

        @Override
        public String getBuildServerType() {
            return serverType;
        }

        @Override
        public String getBuildServerSource() {
            return serverSource;
        }

        @Override
        public String getBuildServerVersion() {
            return serverVersion;
        }

        @Override
        public String getBuildServerAlias() {
            return serverAlias;
        }

        @Override
        public IProjectState getProjectState(String projectId) throws AuthenticationException {
            return projectState;
        }

        @Override
        public IProject getProject(String projectId) {
            return project;
        }
    
    }
}
