package com.cibuddy.jenkins.status;

import com.cibuddy.core.build.server.IBuildProject;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 *
 * @author mirkojahn
 */
public class JobParser extends DefaultHandler {

    private static final Logger LOG = LoggerFactory.getLogger(JobParser.class);
    private HashMap<String, IBuildProject> jobs = new HashMap<String, IBuildProject>();
    private JenkinsServer server;
    private byte[] rawDocument;
    
    private BuildJob tempBuildJob = null;
    private String tempContent = null;
    private boolean isLatestBuild = false;
    
    public JobParser(byte[] bytes, JenkinsServer jkServer) {
        rawDocument = bytes;
        server = jkServer;
    }
    
    public JobParser(byte[] bytes, JenkinsServer jkServer, BuildJob job) {
        rawDocument = bytes;
        server = jkServer;
        tempBuildJob = job;
    }

    HashMap<String, IBuildProject> getBuildJobs(){
        return jobs;
    }
    
    BuildJob getCurrentBuildJob() {
        return tempBuildJob;
    }
    
    public void parseDocument() {
        ByteArrayInputStream stream = new ByteArrayInputStream(rawDocument);
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            // get a new instance of parser
            SAXParser sp = spf.newSAXParser();

            // parse content and register this class for call backs
            sp.parse(stream, this);
//            Iterator<Entry<String, IBuildProject>> iter = jobs.entrySet().iterator();
//            while(iter.hasNext()){
//                Entry<String, IBuildProject> entry = iter.next();
////                System.out.println("Key: "+entry.getKey() + " Content: " + entry.getValue());
//            }
        } catch (Exception e) {
            LOG.warn("Problem parsing XML status of Jenkins server", e);
        } finally {
            try {
                if (stream != null) {
                    stream.close();
                }
            } catch (Exception ex) {
                // ignore
            }
        }
    }

    //Event Handler
    @Override
    public void startElement(String uri, String localName, String tagName, Attributes attributes) throws SAXException {
        //reset
        tempContent = "";
//        System.out.println("start element:"+tagName);
        if (tagName.equalsIgnoreCase("job")) {
            //create a new instance of employee
            tempBuildJob = new BuildJob(server);
        } else if(tagName.equalsIgnoreCase("lastBuild")){
            isLatestBuild = true;
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        // according to the spec, we might need to concatinate.
        tempContent = new String(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName,
            String tagName) throws SAXException {
//        System.out.println("end element:"+tagName);
        // root element handling
        if (tagName.equalsIgnoreCase("job")) {
            jobs.put(tempBuildJob.getUri().toString(), tempBuildJob);
            tempBuildJob = null;
            tempContent = null;
        } else if(tagName.equalsIgnoreCase("name")) {
            LOG.debug("setting project name: "+tempContent);
            tempBuildJob.setName(tempContent);
        } else if(tagName.equalsIgnoreCase("url")) {
            try {
                LOG.debug("setting project uri: "+tempContent);
                tempBuildJob.setURI(new URI(tempContent));
            } catch (URISyntaxException ex) {
                LOG.warn("Malformed URI: "+tempContent,ex);
            }
        } else if(tagName.equalsIgnoreCase("color")) {
            LOG.debug("setting project color: "+tempContent);
            tempBuildJob.setColor(tempContent);
        } 
        // latest build information
        else if(tagName.equalsIgnoreCase("number")) {
            if(isLatestBuild ){
                LOG.debug("setting project buildnumber: "+tempContent);
                tempBuildJob.setBuildNumber(Integer.parseInt(tempContent));
            }
        } else if(tagName.equalsIgnoreCase("username")) {
            if(isLatestBuild ){
                LOG.debug("setting username of last change: "+tempContent);
                tempBuildJob.setUserName(tempContent);
            }
        } else if(tagName.equalsIgnoreCase("result")) {
            if(isLatestBuild ){
                LOG.debug("setting project result: "+tempContent);
                tempBuildJob.setLastBuildResult(tempContent);
            }
        } else if(tagName.equalsIgnoreCase("lastBuild")){
            isLatestBuild = false;
        }
        // finish
        else {
            // reset temporary node content
            tempContent = "";
        }
    }
}
