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

import org.apache.felix.gogo.commands.Command;
import org.apache.karaf.shell.console.OsgiCommandSupport;

/**
 * Provide information about the current system.
 * 
 * At the moment this command only provides information about the running OS and
 * architecture. Later, it should reveal information about the installed system
 * and configuration details in a convenient way.
 * 
 * @author Mirko Jahn <mirkojahn@gmail.com>
 * @since 1.0
 * @version 1.0
 */
@Command(scope = "cib", name = "whoAmI", description = "Displays details about myself.")
public class ItsMeCommand extends OsgiCommandSupport{
    
    /**
     * Main method triggered by the console.
     * 
     * This method is triggered by the console after hitting the command in the 
     * "bash". 
     * 
     * @return currently only null
     * @throws Exception in case something went downhill - nothing forseen so far.
     */
    @Override
    protected Object doExecute() throws Exception {
        detectOS();
        return null;
    }
    
    /**
     * Core logic for detecting the OS.
     */
    private void detectOS() {
        if (isWindows()) {
            System.out.println("I'm a Windows: " + System.getProperty("os.name") + " (arch: " + System.getProperty("os.arch") + ")");
        } else if (isMac()) {
            System.out.println("I'm a Mac: " + System.getProperty("os.name") + " (arch: " + System.getProperty("os.arch") + ")");
        } else if (isUnix()) {
            System.out.println("I'm a Linux... or Unix - maybe: " + System.getProperty("os.name") + " (arch: " + System.getProperty("os.arch") + ")");
        } else if (isSolaris()) {
            System.out.println("I'm a Solaris: " + System.getProperty("os.name") + " (arch: " + System.getProperty("os.arch") + ")");
        } else {
            System.out.println("I'm an OS that is currently not support!");
            System.out.println("I identify myself as: " + System.getProperty("os.name") + " (arch: " + System.getProperty("os.arch") + ")");
        }
    }

    /**
     * Detect if the host OS matches Windows.
     * 
     * @return true in case of a match
     */
    private boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        // windows
        return (os.indexOf("win") >= 0);
    }

    /**
     * Detect if the host OS matches Macintosh.
     * 
     * @return true in case of a match
     */
    private boolean isMac() {
        String os = System.getProperty("os.name").toLowerCase();
        // Mac
        return (os.indexOf("mac") >= 0);
    }

    /**
     * Detect if the host OS matches Linux/Unix.
     * 
     * @return true in case of a match
     */
    private boolean isUnix() {
        String os = System.getProperty("os.name").toLowerCase();
        // linux or unix
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);
    }

    /**
     * Detect if the host OS matches Solaris.
     * 
     * @return true in case of a match
     */
    private boolean isSolaris() {
        String os = System.getProperty("os.name").toLowerCase();
        // Solaris
        return (os.indexOf("sunos") >= 0);
    }

}
