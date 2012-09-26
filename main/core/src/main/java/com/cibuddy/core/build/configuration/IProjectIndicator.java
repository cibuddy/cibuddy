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
package com.cibuddy.core.build.configuration;

import com.cibuddy.core.build.indicator.IBuildStatusIndicator;

/**
 * Purpose: Identify an IBuildStatusIndicator associated with an IProjectSetup.
 * 
 * @author Mirko Jahn <mirkojahn@gmail.com>
 * @version 1.0
 * @since 1.0
 */
public interface IProjectIndicator {
    
    IBuildStatusIndicator getProjectSetupIndicator();
    
    /**
     * This method provides a generic mechanism to obtain the indicator.
     * 
     * However not everything could be expressed as a filter, so this method
     * might also return null. In such case, the class should provide the id of
     * the service. Otherwise a mapping will not be possible, unless the indicator
     * itself could be returned directly. Something like an export of a configuration
     * might be larger issues here.
     * 
     * @return A string using OSGi's filter definition for identifying an
     *        indicator uniquely.
     * @since 1.0
     */
    String getIndicatorFilter();
    
    /**
     * Obtain the unique id of the indicator.
     * 
     * The implementor could provide this if known or always return something 
     * smaller than 0.
     * 
     * @return -1 for none found and a number &gt;=0 for the known id.
     * @since 1.0
     */
    int getIndicatorId();
}
