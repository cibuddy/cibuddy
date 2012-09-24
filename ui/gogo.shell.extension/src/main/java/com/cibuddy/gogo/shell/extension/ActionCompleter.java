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

import java.util.List;
import org.apache.karaf.shell.console.Completer;
import org.apache.karaf.shell.console.completer.StringsCompleter;

/**
 * <p> A very simple completer for indicator actions. </p>
 */
public class ActionCompleter implements Completer {

    /**
     * @param buffer the beginning string typed by the user
     * @param cursor the position of the cursor
     * @param candidates the list of completions proposed to the user
     */
    @Override
    public int complete(String buffer, int cursor, List candidates) {
        StringsCompleter delegate = new StringsCompleter();
        delegate.getStrings().add("success");
        delegate.getStrings().add("failure");
        delegate.getStrings().add("warning");
        delegate.getStrings().add("building");
        delegate.getStrings().add("off");
        return delegate.complete(buffer, cursor, candidates);
    }
}