package com.cibuddy.core.build.indicator;

/**
 *
 * @author Mirko Jahn <mirkojahn@gmail.com>
 */
public abstract class AbstractBuildStatusIndicator implements IBuildStatusIndicator {

    @Override
    public void indicate(StatusAction myAction) {
        if (StatusAction.SUCCESS.equals(myAction)) {
            success();
        } else if (StatusAction.WARNING.equals(myAction)) {
            warning();
        } else if (StatusAction.FAILURE.equals(myAction)) {
            failure();
        } else if (StatusAction.BUILDING.equals(myAction)) {
            building();
        } else if (StatusAction.OFF.equals(myAction)) {
            off();
        }
    }
    
    
}
