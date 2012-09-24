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
import org.apache.felix.gogo.commands.Argument;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

@Command(scope = "cibuddy", name = "indicator-test", description = "Activate a distinct indicator.")
public class IndicatorTestCommand extends OsgiCommandSupport {

    private List indicators;

    public List getIndicators() {
        return indicators;
    }

    public void setIndicators(List indicators) {
        this.indicators = indicators;
    }
    
    @Argument(index = 0, name = "action", description = "The action to indicate.", required = true, multiValued = false)
    String action = null;

    @Argument(index = 1, name = "arg", description = "The id of the indicator to activate.", required = true, multiValued = false)
    String arg = null;

    @Override
    protected Object doExecute() throws Exception {
        Iterator iter = indicators.iterator();
        int i = -1;
        int toMatch = 0;
        boolean match = false;
        boolean all = false;
        if(arg != null){
            toMatch = Integer.parseInt(arg);
            match = false;
        } else {
            // no device mentioned, so we test all connected ones
            all = true;
        }
        System.out.println("Disclaimer: Not all devices support all actions!");
        while(iter.hasNext()){
            i++;
            // move iterator on forward
            IBuildStatusIndicator ibsi = (IBuildStatusIndicator) iter.next();
            // two distinct different usecases: 1) test ALL devices 2) test the matching one
            if(all == true || i == toMatch) {
                if(action == null) {
                    // iterate through all colors and name the device
                    System.out.println("Testing eXtreme Feedback Device: ["+toMatch+"] "+ibsi.getComponentId() + ":" + ibsi.getIndicatorId());
                    System.out.println(ibsi.toString());
                    // invoke the device...
                    indicate(StatusAction.SUCCESS, ibsi, true);
                    indicate(StatusAction.WARNING, ibsi, true);
                    indicate(StatusAction.BUILDING, ibsi, true);
                    indicate(StatusAction.FAILURE, ibsi, true);
                    indicate(StatusAction.OFF, ibsi, true);
                } else {
                    // only show one specific status action
                    System.out.println("indicating " + action + " on eXtreme Feedback Device: "+toMatch);
                    ibsi.indicate(StatusAction.get(action));
                }
                // the match is relevant, no matter where we are
                match = true;
                if(!all){
                    // prevent the break out of the loop in case we iterate through all
                    break;
                }
            }
        }
        if(!match){
            System.out.println("No match found for id " + toMatch + " in " + (i+1) + " tested indicators." );
        }
        return null;
    }
    
    private void indicate(StatusAction action, IBuildStatusIndicator ibsi, boolean sleep) throws InterruptedException{
        System.out.println("Indicating "+action);
        ibsi.indicate(action);
        if(sleep){
            Thread.sleep(2000);
        }
    }
}