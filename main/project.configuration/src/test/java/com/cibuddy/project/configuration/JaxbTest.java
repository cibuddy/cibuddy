package com.cibuddy.project.configuration;

import com.cibuddy.project.configuration.schema.BuildTestConfigurationType;
import com.cibuddy.project.configuration.schema.Setup;
import java.io.File;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class JaxbTest {

    @Test
    public void testEligibilityXML() throws Exception {
        String packageName = this.getClass().getPackage().getName()+".schema";
        File xmlFile = new File("src/test/resources/sample.xml");
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
