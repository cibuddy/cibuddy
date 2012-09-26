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

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mirkojahn
 * @version 1.0
 * @since 1.0
 */
public enum StatusAction {

    SUCCESS("success"), WARNING("warning"), FAILURE("failure"), BUILDING("building"), OFF("off");
    
    
    private static final Logger LOG = LoggerFactory.getLogger(StatusAction.class);
    private static final Map<String, StatusAction> lookup = new HashMap<String, StatusAction>();

    static {
        for (StatusAction s : EnumSet.allOf(StatusAction.class)) {
            lookup.put(s.getCode(), s);
        }
    }
    private String code;

    private StatusAction(String c) {
        code = c;
    }

    public String getCode() {
        return code;
    }

    public static StatusAction get(String code) {
        if(LOG.isDebugEnabled()){
            LOG.debug("trying to lookup: " + code + " with result: " + lookup.get(code));
        }
        return lookup.get(code);
    }
}
