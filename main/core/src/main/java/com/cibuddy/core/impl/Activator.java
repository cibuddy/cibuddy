package com.cibuddy.core.impl;

import com.cibuddy.core.build.configuration.BuildTestConfiguration;
import java.util.Timer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.util.tracker.ServiceTracker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * "Mirko Jahn" <mirkojahn@gmail.com>
 *
 * @version 1.0
 */
public class Activator implements BundleActivator {

    private static final Logger LOG = LoggerFactory.getLogger(Activator.class);
    private Timer caretaker;
    private final long EXECUTION_DELAY = 10 * 1000; // 10 Seconds
    // FIXME: make the heartbeat interval of the server configurable
    private final long HEARTBEAT_INTERVAL = 1 * 60 * 1000; // 1 Minute
    private static ServiceTracker buildConfigTracker;

    @Override
    public void start(BundleContext bc) throws Exception {
        buildConfigTracker = new ServiceTracker(bc, BuildTestConfiguration.class.getName(), null);
        buildConfigTracker.open();

        HeartBeat heartBeat = new HeartBeat(buildConfigTracker);
        caretaker = new Timer();
        caretaker.schedule(heartBeat, EXECUTION_DELAY, HEARTBEAT_INTERVAL);
        //detectOS();
    }

    @Override
    public void stop(BundleContext bc) throws Exception {
        caretaker.cancel();
        if (buildConfigTracker != null) {
            buildConfigTracker.close();
            buildConfigTracker = null;
        }
    }

    public static void detectOS() {
        if (isWindows()) {
            System.out.println("This is Windows: " + System.getProperty("os.name") + " (arch: " + System.getProperty("os.arch") + ")");
        } else if (isMac()) {
            System.out.println("I'm a Mac: " + System.getProperty("os.name") + " (arch: " + System.getProperty("os.arch") + ")");
        } else if (isUnix()) {
            System.out.println("This is Unix or Linux: " + System.getProperty("os.name") + " (arch: " + System.getProperty("os.arch") + ")");
        } else if (isSolaris()) {
            System.out.println("This is Solaris: " + System.getProperty("os.name") + " (arch: " + System.getProperty("os.arch") + ")");
        } else {
            System.out.println("Your OS is not support!!");
            System.out.println("Your OS identified itself as: " + System.getProperty("os.name") + " (arch: " + System.getProperty("os.arch") + ")");
        }
    }

    public static boolean isWindows() {

        String os = System.getProperty("os.name").toLowerCase();
        // windows
        return (os.indexOf("win") >= 0);

    }

    public static boolean isMac() {

        String os = System.getProperty("os.name").toLowerCase();
        // Mac
        return (os.indexOf("mac") >= 0);

    }

    public static boolean isUnix() {

        String os = System.getProperty("os.name").toLowerCase();
        // linux or unix
        return (os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0);

    }

    public static boolean isSolaris() {

        String os = System.getProperty("os.name").toLowerCase();
        // Solaris
        return (os.indexOf("sunos") >= 0);

    }
}
