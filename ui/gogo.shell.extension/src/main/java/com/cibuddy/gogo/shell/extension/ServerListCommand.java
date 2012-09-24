/*
 * Copyright 2012 Mirko Jahn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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