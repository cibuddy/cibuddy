package com.cibuddy.gogo.shell.extension;

import com.cibuddy.core.build.indicator.IBuildStatusIndicator;
import com.cibuddy.core.build.server.IBuildProject;
import com.cibuddy.core.build.server.IServer;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

@Command(scope = "cibuddy", name = "server-details", description = "Show details about the given server. Use \"list-servers\" to find the id of the server you're looking for.")
public class ServerDetailsCommand extends OsgiCommandSupport {

    private List servers;

    public List getServers() {
        return servers;
    }

    public void setServers(List indicators) {
        this.servers = indicators;
    }
    
    @Argument(index = 0, name = "arg", description = "The id of the server to look into.", required = true, multiValued = false)
    String arg = null;

    protected Object doExecute() throws Exception {
        Iterator iter = servers.iterator();
        int i = -1;
        int match = Integer.parseInt(arg);
        boolean hasMatch = false;
        while(iter.hasNext()){
            i++;
            IServer server = (IServer) iter.next();
            // the whole construct looks strange, but with blueprint this seems necessary
            if(i == match) {
                hasMatch = true;
                System.out.println("Server : ["+i+"] " 
                        + server.getBuildServerAlias() 
                        + "=" + server.getBuildServerURL() 
                        + " ["+ server.getBuildServerSource() 
                        + " " + server.getBuildServerType() 
                        + " " + server.getBuildServerVersion() 
                        + "]");
                Collection<IBuildProject> buildProjects = server.getBuildProjects();
                Iterator<IBuildProject> projectIter = buildProjects.iterator();
                while(projectIter.hasNext()){
                    IBuildProject project = projectIter.next();
                    System.out.println("Project: " 
                        + project.getProjectName() 
                        + " [status="+ project.getBuildStatus() 
                        + " color=" + project.getProjectColor() 
                        + " url=" + project.getRootURI() 
                        + "]");
                }
            }
        }
        if(!hasMatch){
            System.out.println("No matching server found." );
        }
        return null;
    }
}