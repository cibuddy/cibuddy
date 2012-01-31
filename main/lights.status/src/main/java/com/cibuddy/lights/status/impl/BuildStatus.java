package com.cibuddy.lights.status.impl;

/**
 *
 * "Mirko Jahn" <mirkojahn@gmail.com>
 * @version 1.0
 */
public class BuildStatus {
    
    //   ${server_url}/job/${project_name}/api/xml?xpath=/*/color/text()  returns: blue
    public final String BUILD_COLOR = "/api/xml?xpath=/*/color/text()";
    //   ${server_url}/api/xml?xpath=/*/inQueue/text()    returns: false
    public final String BUILD_IN_QUEUE = "/api/xml?xpath=/*/inQueue/text()";
    //   ${server_url}/api/xml?xpath=/*/healthReport/score/text()
    public final String BUILD_HEALTH_SCORE = "/api/xml?xpath=/*/healthReport/score/text()";
    
    public final String BUILD_LAST_BUILD = "";
    public final String BUILD_LAST_COMPLETED_BUILD = "";
    public final String BUILD_LAST_FAILED_BUILD = "";
    public final String BUILD_LAST_STABLE_BUILD = "";
    public final String BUILD_LAST_SUCCESSFUL_BUILD = "";
    public final String BUILD_LAST_UNSUCCESSFUL_BUILD = "";
    public final String BUILD_NEXT_BUILD_NUMBER = "";
//    public final String BUILD_ = "";
//    public final String BUILD_ = "";
//    public final String BUILD_ = "";
}
