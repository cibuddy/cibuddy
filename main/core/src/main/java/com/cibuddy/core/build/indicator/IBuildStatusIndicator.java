package com.cibuddy.core.build.indicator;

/**
 * This interface represents the physical build status indicator.
 * 
 * What this means is that the instance of the class implementing this interface
 * is directly bound to a build indicator. This could be a simple light bulb or
 * something fancy as a Delcom light or an i-Buddy. 
 * 
 * Indicator providers are expected to expose an OSGi Service that implements this
 * Interface, while consumers should be looking for the exposed service. Please
 * note that multiplicity is not covered here. Some light might be able of handling
 * multiple consumers, some might not.
 * 
 * The exposed service must be annotated with all properties defines as constants
 * in this interface.
 * 
 * @author mirkojahn
 * @version 1.0
 * @since 1.0
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
    void indicate(StatusAction action);
}
