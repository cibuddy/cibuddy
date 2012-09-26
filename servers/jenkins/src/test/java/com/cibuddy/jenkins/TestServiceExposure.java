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
package com.cibuddy.jenkins;

import com.cibuddy.core.build.server.IProjectState;
import com.cibuddy.core.build.server.IServer;
import de.kalpatec.pojosr.framework.launch.ClasspathScanner;
import de.kalpatec.pojosr.framework.launch.PojoServiceRegistry;
import de.kalpatec.pojosr.framework.launch.PojoServiceRegistryFactory;
import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;
import org.apache.felix.fileinstall.ArtifactInstaller;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.osgi.framework.ServiceReference;

/**
 *
 * @author Mirko Jahn <mirkojahn@gmail.com>
 */
public class TestServiceExposure {
   
    PojoServiceRegistry registry;
    File jenkinsConfigFile = new File("src/test/resources/com/cibuddy/jenkins/jenkins-ci-public.jenkins");
    
    @Before
    public void before() throws Exception {
        Map config = new HashMap();
        config.put(PojoServiceRegistryFactory.BUNDLE_DESCRIPTORS, new ClasspathScanner().scanForBundles());
        ServiceLoader<PojoServiceRegistryFactory> loader = ServiceLoader.load(PojoServiceRegistryFactory.class);
        registry = loader.iterator().next().newPojoServiceRegistry(config);
        // make sure this bundle is started by manually loading the activator 
        // (we're not yet a bundle to be picked up automatically)
        Activator activator = new Activator();
        activator.start(registry.getBundleContext());
    }
    
    @Test
    public void test() throws Exception{
        Assert.assertNotNull("BundleContext is not set.", Activator.getBundleContext());
        
        ServiceReference sr = registry.getServiceReference(ArtifactInstaller.class.getName());
        Assert.assertNotNull("No service registered, although expected to be "
                + "there for the file based travis server configuration.",sr);
        
        ArtifactInstaller ai = (ArtifactInstaller)registry.getService(sr);
        Assert.assertTrue("File does not exist! Check path and setup!", jenkinsConfigFile.exists());
        
        if(ai instanceof ServerConfigurationListener){
            // check if the ArtifactInstaller could handle a configuration file
            boolean canHandle = ai.canHandle(jenkinsConfigFile);
            Assert.assertTrue("The file failed the verification and appears to be not valid.", canHandle);
            
            // ensure that there are no IServer's exposed first
            if(registry.getServiceReference(IServer.class.getName()) != null){
                Assert.fail("Found a ServiceReference prior to registration. This should not happen.");
            }
            // now check exposure of an IServer
            ai.install(jenkinsConfigFile);
            ServiceReference sr2 = registry.getServiceReference(IServer.class.getName());
            if(sr2 == null){
                Assert.fail("NO IServer service found when expected! Check registration...");
            }
            IServer server = (IServer)registry.getService(sr2);
            Assert.assertNotNull("Server object of IServer service couldn't be retrieved", server);
            
            // do a very very simple server configuration comparison
            Assert.assertEquals("The server alias is not identical to the one set in the configuration file.", "org.apache", server.getBuildServerAlias());
            Assert.assertEquals("The server type is not identical to the one expected.", IServer.TYPE_JENKINS_SERVER, server.getBuildServerType());
            Assert.assertEquals("The server URL is not identical to the one set in the configuration file.",new URI("https://builds.apache.org/"),server.getBuildServerURL());
            Assert.assertNotNull("The server version is not as expected!",server.getBuildServerVersion());
            Assert.assertTrue("Version string doesn't contain a dot as expected. Might have changed though...", server.getBuildServerVersion().contains("."));
            //Assert.assertEquals("The server version is not as expected! (they might have upgraded ;-))","1.480",server.getBuildServerVersion());
            Assert.assertEquals("The server source is not as expected!",jenkinsConfigFile.toURI().toURL().getFile(),server.getBuildServerSource());
            
            // this might fail at any point... do it just because of curiosity 
            // FIXME: only test this with a specific variable set and not with a network look-up!!!
//            IProjectState ibp = server.getProjectState("Karaf");
//            Assert.assertNotNull("No such project found on server - do we have internet access???",ibp);
//            Assert.assertNotNull("No build status available, but should be.",ibp.getBuildStatus());
//            Assert.assertNotNull("No build status received.",ibp.getProjectColor());
        } else {
            Assert.fail("Expected only one service exposed coming from this bundle.");
        }
    }
        
}
