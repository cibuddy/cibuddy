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
package com.cibuddy.gogo.shell.extension;

import com.cibuddy.core.build.server.IServer;
import java.util.Iterator;
import java.util.List;
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

@Command(scope = "cib", name = "server-details", description = "Show details about the given server. Use \"list-servers\" to find the id of the server you're looking for.")
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

    /**
     * Main method triggered by the console.
     * 
     * This method is triggered by the console after hitting the command in the 
     * "bash". 
     * 
     * @return currently only null
     * @throws Exception in case something went downhill - nothing forseen so far.
     */
    @Override
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
                        + " [config source: "+ server.getBuildServerSource() 
                        + " | server type: " + server.getBuildServerType() 
                        + " | version: " + server.getBuildServerVersion() 
                        + "]");
            }
        }
        if(!hasMatch){
            System.out.println("No matching server found." );
        }
        return null;
    }
}