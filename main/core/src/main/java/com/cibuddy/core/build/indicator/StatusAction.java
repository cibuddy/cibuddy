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
