/*
 * Copyright 2012 Mirko Jahn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cibuddy.gogo.shell.extension;

import com.cibuddy.core.build.indicator.IBuildStatusIndicator;
import java.util.Iterator;
import java.util.List;
import org.apache.karaf.shell.console.Completer;
import org.apache.karaf.shell.console.completer.StringsCompleter;

/**
 * A completer that provides the list of available eXtreme Feedback Devices.
 * 
 * @author Mirko Jahn <mirkojahn@gmail.com>
 * @version 1.0
 * @since 1.0
 */
public class IndicatorCompleter implements Completer {
    
    private List indicators;

    public List getIndicators() {
        return indicators;
    }

    public void setIndicators(List indicators) {
        this.indicators = indicators;
    }
    /**
     * Main method of the completer.
     * 
     * @param buffer the beginning string typed by the user
     * @param cursor the position of the cursor
     * @param candidates the list of completions proposed to the user
     * 
     * @see Completer#complete(java.lang.String, int, java.util.List) 
     * @since 1.0
     */
    @Override
    public int complete(String buffer, int cursor, List candidates) {
        StringsCompleter delegate = new StringsCompleter();
        Iterator iter = indicators.iterator();
        int i = -1;
        while(iter.hasNext()){
            i++;
            IBuildStatusIndicator ibsi = (IBuildStatusIndicator) iter.next();
            // we only need the id for this one
            delegate.getStrings().add(String.valueOf(i));
        }
        return delegate.complete(buffer, cursor, candidates);
    }
}