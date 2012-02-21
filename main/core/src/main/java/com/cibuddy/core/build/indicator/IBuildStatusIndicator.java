package com.cibuddy.core.build.indicator;

/**
 *
 * @author mirkojahn
 */
public interface IBuildStatusIndicator {
    String COMPONENT_ID = "component.id";
    String INDICATOR_ID = "indicator.id";
    String getComponentId();
    String getIndicatorId();
    void success();
    void warning();
    void failure();
    void building();
    void off();
}
