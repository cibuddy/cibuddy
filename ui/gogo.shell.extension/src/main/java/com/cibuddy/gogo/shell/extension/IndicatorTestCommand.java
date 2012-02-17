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
        int toMatch = Integer.parseInt(arg);
        boolean match = false;
        while(iter.hasNext()){
            i++;
            IBuildStatusIndicator ibsi = (IBuildStatusIndicator) iter.next();
            if(i == toMatch) {
                // indicate success
                System.out.println("indicating success on indicator: "+toMatch);
                if(action.equals("success")){
                    ibsi.success();
                } else if (action.equals("failure")){
                    ibsi.failure();
                } else if (action.equals("warning")){
                    ibsi.warning();
                } else if (action.equals("building")){
                    ibsi.building();
                } else if (action.equals("off")){
                    // FIXME: provide an "off" action on the indicator
                }
                
                match = true;
                break;
            }
        }
        if(!match){
            System.out.println("No match found for id " + toMatch + " in " + (i+1) + " tested indicators." );
        }
        return null;
    }
}