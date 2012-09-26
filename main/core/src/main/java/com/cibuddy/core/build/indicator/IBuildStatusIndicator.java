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
