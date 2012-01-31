package com.cibuddy.core.build.configuration.helper;

import com.cibuddy.core.build.configuration.StatusIndicatorTrigger;
import com.cibuddy.core.build.indicator.IBuildStatusIndicator;
import com.cibuddy.core.build.server.IBuildProject;
import java.util.Iterator;
import java.util.List;
/**
 *
 * @author mirkojahn
 */
public class SimpleStatusIndicatorTrigger implements StatusIndicatorTrigger {

    private List<IBuildStatusIndicator> buildIndicators;
    private StatusAction myAction;
    
    public SimpleStatusIndicatorTrigger(List<IBuildStatusIndicator> indicators, StatusAction action){
        buildIndicators = indicators;
        myAction = action;
    }
    
    @Override
    public void enableStatusIndicator() {
        Iterator<IBuildStatusIndicator> iter = buildIndicators.iterator();
        while(iter.hasNext()){
            IBuildStatusIndicator indicator = iter.next();
            if(StatusAction.SUCCESS.equals(myAction)){
                indicator.success();
            } else if (StatusAction.WARNING.equals(myAction)) {
                indicator.warning();
            } else if (StatusAction.FAILURE.equals(myAction)) {
                indicator.failure();
            } else if (StatusAction.BUILDING.equals(myAction)) {
                indicator.building();
            }
        }
    }
    
}
