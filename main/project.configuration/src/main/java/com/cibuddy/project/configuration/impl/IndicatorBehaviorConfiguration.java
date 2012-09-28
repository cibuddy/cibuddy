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
package com.cibuddy.project.configuration.impl;

import com.cibuddy.core.build.configuration.IIndicatorBehaviorConfiguration;
import com.cibuddy.core.build.configuration.IProjectSetup;
import com.cibuddy.core.build.indicator.StatusAction;
import com.cibuddy.core.build.server.BuildStatus;
import com.cibuddy.core.build.server.IProject;
import com.cibuddy.project.configuration.jaxb.v1_0.indicator.ColorIndicator;
import com.cibuddy.project.configuration.jaxb.v1_0.indicator.Config;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mirko Jahn <mirkojahn@gmail.com>
 */
public class IndicatorBehaviorConfiguration implements IIndicatorBehaviorConfiguration {
    
    private static final Logger LOG = LoggerFactory.getLogger(IndicatorBehaviorConfiguration.class);
    Config indicatorXMLConfig;
    
    public IndicatorBehaviorConfiguration (Config conf){
        indicatorXMLConfig = conf;
    }
    
    @Override
    public StatusAction evaluate(IProjectSetup setup) {
        List<IProject> projects = setup.getProjects();
        // prepare holders
        //List<BuildStatus> configuredBuildStatiMatches = new ArrayList<BuildStatus>();
        BuildStatus[] result = new BuildStatus[projects.size()];
        Iterator<IProject> projectIter = projects.iterator();
        // this for loop is a bit different than usual...
        for (int i=0; projectIter.hasNext(); i++) {
            // think about breaking this down to avoid problems with exceptions for instance
            // FIXME: this might cause NPE's, be more robust here
            result[i] = projectIter.next().obtainProjectState().getProjectColor();
        }
        Iterator<ColorIndicator> ciIterator = indicatorXMLConfig.getAction().iterator();
        while (ciIterator.hasNext()) {
            ColorIndicator ci = ciIterator.next();
            String condition = ci.getCondition();
            if (condition.equalsIgnoreCase("default")) {
                String indicate = ci.getIndicate();
                return StatusAction.get(indicate);
            } else {
                // obtains the list of BuildStatus objects from their string representations
                // that are used to match a certain "action" to indicate
                List<BuildStatus> expStati = getBuildStatusListFromStrings(ci.getStatus());
                if (checkCondition(ci.getCondition(), expStati, result)) {
                    String indicate = ci.getIndicate();
                    return StatusAction.get(indicate);
                }
            }
        }
        return null;
    }

    @Override
    public String getName() {
        return indicatorXMLConfig.getName();
    }
    
    private List<BuildStatus> getBuildStatusListFromStrings(List<String> stati){
        List<BuildStatus> statusList = new ArrayList<BuildStatus>();
        Iterator<String> iter = stati.iterator();
        while(iter.hasNext()){
            statusList.add(BuildStatus.valueOf(iter.next()));
        }
        return statusList;
    }
    
    private boolean checkCondition(String condition, List<BuildStatus> configuredBuildStatiMatches, BuildStatus[] result) {
        int hits = 0;
        for(int i=0;i<result.length;i++){
            Iterator<BuildStatus> iter = configuredBuildStatiMatches.iterator();
            // check for each possible match configured
            while(iter.hasNext()){
                BuildStatus bs = iter.next();
                if(result[i].equals(bs)){
                    hits++;
                    LOG.debug("match found: actual status [{}] vs. expected status [{}]", result[i], bs);
                    break;
                } else {
                    LOG.debug("no match: actual status [{}] vs. expected status [{}]", result[i], bs);
                }
            }
        }
        LOG.debug("hits found: {}",hits);
        if(condition.equalsIgnoreCase("all")){
            LOG.debug("checking if all are matching");
            if(hits == result.length){
                return true;
            } else {
                LOG.debug("false: {} vs. {}", hits, result.length);
            }
        } else if(condition.contains(":")){
            LOG.debug("boundary matching rule identified");
            String[] boundaries = condition.split(":");
            if(boundaries[1].contains("*")) {
                LOG.debug("matching rule with unbound upper bound identified.");
                if(Integer.parseInt(boundaries[0]) <= hits) {
                    LOG.debug("matching rule!");
                    return true;
                }
            } else {
                float boundary = Integer.parseInt(boundaries[0]) / Integer.parseInt(boundaries[1]);
                float found = hits/result.length;
                if(boundary <= found) {
                    LOG.debug("matching rule: {}/{} <= {}/{}", new Object [] {boundaries[0], boundaries[1], String.valueOf(hits), String.valueOf(result.length)});
                    return true;
                }
            }
        } 
        return false;
    }
}
