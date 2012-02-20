package com.cibuddy.gogo.shell.extension;

import com.cibuddy.core.build.indicator.IBuildStatusIndicator;
import java.util.Iterator;
import java.util.List;
import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

@Command(scope = "cibuddy", name = "indicator-list", description = "Display a list of all connected indicators.")
public class IndicatorListCommand extends OsgiCommandSupport {

    private List indicators;

    public List getIndicators() {
        return indicators;
    }

    public void setIndicators(List indicators) {
        this.indicators = indicators;
    }
    
    @Override
    protected Object doExecute() throws Exception {
        Iterator iter = indicators.iterator();
        int i = -1;
        while(iter.hasNext()){
            i++;
            
            IBuildStatusIndicator ibsi = (IBuildStatusIndicator) iter.next();
            System.out.println("Indicator : ["+i+"] "+ibsi.getComponentId() + ":" + ibsi.getIndicatorId());
        }
        if(i<0){
            System.out.println("No indicators found." );
        }
        return null;
    }
}