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
