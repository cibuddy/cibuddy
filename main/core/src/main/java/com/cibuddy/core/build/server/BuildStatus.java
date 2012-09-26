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
package com.cibuddy.core.build.server;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mirkojahn
 */
public enum BuildStatus {

    red("red"), red_anime("red_anime"),
    yellow("yellow"), yellow_anime("yellow_anime"),
    blue("blue"), blue_anime("blue_anime"),
// make it easier (those are not necessary in 99.999% of all use cases)
//    grey("grey"), grey_anime("grey_anime"),
//    disabled("disabled"), disabled_anime("disabled_anime"),
//    aborted("aborted"), aborted_anime("aborted_anime"),
//    notbuilt("notbuilt"), notbuilt_anime("notbuilt_anime"),
    unknown("unknown");
    
    private static final Map<String, BuildStatus> lookup = new HashMap<String, BuildStatus>();
    private static final Logger LOG = LoggerFactory.getLogger(BuildStatus.class);
    
    static {
        for (BuildStatus s : EnumSet.allOf(BuildStatus.class)) {
            lookup.put(s.getCode(), s);
        }
    }
    private String code;

    private BuildStatus(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static BuildStatus get(String code) {
        if(LOG.isDebugEnabled()){
            LOG.debug("trying to lookup: {} with result: {}",code, lookup.get(code));
        }
        return lookup.get(code);
    }
}
