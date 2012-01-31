package com.cibuddy.lights.configuration;

import com.cibuddy.lights.configuration.schema.BuildTestConfigurationType;
import com.cibuddy.lights.configuration.schema.Setup;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.junit.Test;

public class JaxbTest {

    @Test
    public void testEligibilityXML() throws Exception {
        String packageName = this.getClass().getPackage().getName()+".schema";
        File xmlFile = new File("src/test/resources/sample2.xml");
        assertTrue("XML File doesn't exist.", xmlFile.exists());

        JAXBContext jc = JAXBContext.newInstance(packageName);
        
        Unmarshaller unmarshaller = jc.createUnmarshaller();
	Setup config = (Setup) unmarshaller.unmarshal(xmlFile);

        List<BuildTestConfigurationType> configurations = config.getConfiguration();
        assertTrue(configurations != null);
        // saving such file
        //jc.createMarshaller().marshal(config, new File("mysample2.xml"));
    }
}
