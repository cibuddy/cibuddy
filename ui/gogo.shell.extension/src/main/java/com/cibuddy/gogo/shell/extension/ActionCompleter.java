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