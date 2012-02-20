package com.cibuddy.gogo.shell.extension;

import com.cibuddy.core.build.server.IServer;
import java.util.Iterator;
import java.util.List;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

@Command(scope = "cibuddy", name = "server-list", description = "Display the list of configured servers to check for build projects.")
public class ServerListCommand extends OsgiCommandSupport {

    private List servers;

    public List getServers() {
        return servers;
    }

    public void setServers(List serverList) {
        this.servers = serverList;
    }
    
    @Override
    protected Object doExecute() throws Exception {
        Iterator iter = servers.iterator();
        int i = -1;
        while(iter.hasNext()){
            i++;
            
            IServer server = (IServer) iter.next();
            System.out.println("Server : ["+i+"] " 
                    + server.getBuildServerAlias() 
                    + "=" + server.getBuildServerURL() 
                    + " ["+ server.getBuildServerSource() 
                    + " " + server.getBuildServerType() 
                    + " " + server.getBuildServerVersion() 
                    + "]");
        }
        if(i<0){
            System.out.println("No servers found." );
        }
        return null;
    }
}