package com.cibuddy.core.build.configuration;

import java.net.URL;
import org.osgi.framework.Bundle;

/**
 *
 * @author Mirko Jahn <mirkojahn@gmail.com>
 */
public interface IConfigurationInstaller {

    boolean canHandle(URL url);

    void install(URL url, Bundle bundle) throws Exception;

    void uninstall(URL url, Bundle bundle) throws Exception;

    void update(URL url, Bundle bundle) throws Exception;
    
}
