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

import com.cibuddy.core.build.indicator.IBuildStatusIndicator;
import java.util.Iterator;
import java.util.List;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

@Command(scope = "cib", name = "list-efds", description = "Display a list of all connected eXtreme Feedback Devices.")
public class IndicatorListCommand extends OsgiCommandSupport {

    private List indicators;

    public List getIndicators() {
        return indicators;
    }

    public void setIndicators(List indicators) {
        this.indicators = indicators;
    }
    
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
        Iterator iter = indicators.iterator();
        int i = -1;
        while(iter.hasNext()){
            i++;
            
            IBuildStatusIndicator ibsi = (IBuildStatusIndicator) iter.next();
            System.out.println("eXtreme Feedback Device : ["+i+"] "+ibsi.getComponentId() + ":" + ibsi.getIndicatorId());
        }
        if(i<0){
            System.out.println("No eXtreme Feedback Device found." );
        }
        return null;
    }
}