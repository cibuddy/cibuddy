package com.cibuddy.project.configuration.impl;

import com.cibuddy.core.build.indicator.IBuildStatusIndicator;
import com.cibuddy.project.configuration.StatusIndicatorTrigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mirkojahn
 */
public class SimpleStatusIndicatorTrigger implements StatusIndicatorTrigger {

    private static final Logger LOG = LoggerFactory.getLogger(SimpleStatusIndicatorTrigger.class);
    private IBuildStatusIndicator buildIndicator;
    private StatusAction myAction;

    public SimpleStatusIndicatorTrigger(IBuildStatusIndicator indicators, StatusAction action) {
        buildIndicator = indicators;
        myAction = action;
    }

    public void setAction(StatusAction action) {
        myAction = action;
    }

    @Override
    public void enableStatusIndicator() {
        LOG.debug("trying to indicate an action: {}",myAction.getCode());
        if(buildIndicator != null) {
            if (StatusAction.SUCCESS.equals(myAction)) {
                buildIndicator.success();
            } else if (StatusAction.WARNING.equals(myAction)) {
                buildIndicator.warning();
            } else if (StatusAction.FAILURE.equals(myAction)) {
                buildIndicator.failure();
            } else if (StatusAction.BUILDING.equals(myAction)) {
                buildIndicator.building();
            }
        } else {
            LOG.info("The build indicator was not found. Make sure the configuration is correct.");
        }
    }
}
