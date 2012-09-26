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
