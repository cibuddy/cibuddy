package com.cibuddy.project.configuration.impl;

import com.cibuddy.core.build.configuration.IIndicatorBehaviorConfiguration;
import com.cibuddy.project.configuration.impl.installer.IndicatorBehaviorConfigurationListener;
import java.io.File;
import junit.framework.Assert;
import org.apache.felix.fileinstall.ArtifactInstaller;
import org.junit.Test;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

/**
 * Basic test for the indicator behavior configuration load.
 * 
 * General tests to verify that loading the behavior configurations actually works.
 * This includes parsing, as well as service exposure in the OSGi service registry.
 * 
 * @author Mirko Jahn <mirkojahn@gmail.com>
 * @version 1.0
 * @since 1.0
 */
public class IndicatorBehaviorConfigurationListenerTest extends AbstractOSGIEnvBaseTest {
    private static String filterString = "(&(" + Constants.OBJECTCLASS + "=" 
            + ArtifactInstaller.class.getName() + ")(installerType="
            + IndicatorBehaviorConfigurationListener.class.getName()+"))";
    private File behaviorConfigFile = new File("src/test/resources/indicator-config.xml");
    
    /**
     * First test to see that everything is setup correctly.
     * 
     * The goal of this test is to verify that the exposure of a well defined
     * XML configuration actually works and that the exposed configuration object
     * could be retrieved from the OSGi registry.
     * 
     * @throws Exception indicating problems.
     */
    @Test
    public void test() throws Exception {
        Assert.assertNotNull("BundleContext is not set.", Activator.getBundleContext());
        
        ServiceReference[] srs = getRegistry().getServiceReferences(ArtifactInstaller.class.getName(), filterString);
        Assert.assertNotNull(srs);
        // there should be just one in our test
        Assert.assertEquals("The expectation is to find only the service we just registered", 1, srs.length);
        // get the service
        ArtifactInstaller ai = (ArtifactInstaller)getRegistry().getService(srs[0]);
        Assert.assertTrue("File does not exist! Check path and setup!", behaviorConfigFile.exists());
        
        
        if(ai instanceof IndicatorBehaviorConfigurationListener){
            // check if the ArtifactInstaller could handle a configuration file
            boolean canHandle = ai.canHandle(behaviorConfigFile);
            Assert.assertTrue("The file failed the verification and appears to be not valid.", canHandle);
            
            // now check exposure
            ai.install(behaviorConfigFile);
            ServiceReference sr2 = getRegistry().getServiceReference(IIndicatorBehaviorConfiguration.class.getName());
            if(sr2 == null){
                Assert.fail("No IIndicatorBehaviorConfiguration service found when expected! Check registration...");
            }
            IIndicatorBehaviorConfiguration config = (IIndicatorBehaviorConfiguration)getRegistry().getService(sr2);
            Assert.assertNotNull("Server object of IIndicatorBehaviorConfiguration service couldn't be retrieved", config);
            
        } else {
            Assert.fail("Expected only one service exposed coming from this bundle.");
        }
    }
}
