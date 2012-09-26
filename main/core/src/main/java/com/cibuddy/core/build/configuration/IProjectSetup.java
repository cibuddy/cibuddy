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
import com.cibuddy.core.build.server.IProject;
import java.util.List;

/**
 * Project setup configuration interface.
 * 
 * The IProjectSetup interface represents a service interface exposed 
 * by any bundle that offers build configurations CIBuddy is capable of testing 
 * for. The bundle has to expose a service with this interface and the two properties
 * with the keys defined identical to the Strings defined in this interface. The
 * core bundle will periodically collect the available services and call the 
 * update method, which then has to update the configuration content and indicate
 * the build status. This is part of the implementation to ensure that the correct
 * status is indicated with the configured build indicators (usually the build lights).
 * 
 * @author mirkojahn
 * @since 1.0
 * @version 1.0
 */
public interface IProjectSetup extends IConfigurationService {
    
    /**
     * Obtain the list of Projects associated with this particular setup.
     * 
     * @return all projects configured.
     */
    List<IProject> getProjects();
    
}
