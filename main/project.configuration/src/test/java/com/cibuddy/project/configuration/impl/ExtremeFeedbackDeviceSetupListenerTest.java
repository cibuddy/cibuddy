package com.cibuddy.project.configuration.impl;

import com.cibuddy.core.build.configuration.IProjectSetup;
import com.cibuddy.project.configuration.impl.installer.ExtremeFeedbackDeviceSetupListener;
import java.io.File;
import junit.framework.Assert;
import org.apache.felix.fileinstall.ArtifactInstaller;
import org.junit.Test;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;

/**
 * Basic test for the XFDs configuration load.
 * 
 * General tests to verify that loading the xfd configurations actually works.
 * This includes parsing, as well as service exposure in the OSGi service registry.
 * 
 * @author Mirko Jahn <mirkojahn@gmail.com>
 * @version 1.0
 * @since 1.0
 */
public class ExtremeFeedbackDeviceSetupListenerTest extends AbstractOSGIEnvBaseTest {
    private static String filterString = "(&(" + Constants.OBJECTCLASS + "=" + ArtifactInstaller.class.getName() + ")(installerType="+ExtremeFeedbackDeviceSetupListener.class.getName()+"))";
    private File projectsConfigFile = new File("src/test/resources/project-setup.xml");
    
    
    /**
     * First test to see that everything is setup correctly.
     * 
     * The goal of this test is to verify that the exposure of a well defined
     * XML configuration actually works and that the exposed configuration object
     * could be retrieved from the OSGi registry.
     * 
     * @throws Exception indicating problems.
     * @since 1.0
     */
    @Test
    public void test() throws Exception {
        Assert.assertTrue(true);
        Assert.assertNotNull("BundleContext is not set.", Activator.getBundleContext());
        
        ServiceReference[] srs = getRegistry().getServiceReferences(ArtifactInstaller.class.getName(), filterString);
        Assert.assertNotNull(srs);
        // there should be just one in our test
        Assert.assertTrue(srs.length == 1);
        // get the service
        ArtifactInstaller ai = (ArtifactInstaller)getRegistry().getService(srs[0]);
        Assert.assertTrue("File does not exist! Check path and setup!", projectsConfigFile.exists());
                
        if(ai instanceof ExtremeFeedbackDeviceSetupListener){
            // check if the ArtifactInstaller could handle a configuration file
            boolean canHandle = ai.canHandle(projectsConfigFile);
            Assert.assertTrue("The file failed the verification and appears to be not valid.", canHandle);
            
            // now check exposure
            ai.install(projectsConfigFile);
            ServiceReference sr2 = getRegistry().getServiceReference(IProjectSetup.class.getName());
            // after registration, there should be at least one...
            if(sr2 == null){
                Assert.fail("No IProjectSetup service found when expected! Check registration...");
            }
            // now see, if we could cast it to the correct class
            IProjectSetup setup = (IProjectSetup)getRegistry().getService(sr2);
            Assert.assertNotNull("Setup object of IProjectSetup service couldn't be retrieved", setup);
            
        } else {
            Assert.fail("Expected only one service exposed coming from this bundle.");
        }
    }
}
