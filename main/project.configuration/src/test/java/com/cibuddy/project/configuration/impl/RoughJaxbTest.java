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
package com.cibuddy.project.configuration.impl;

import com.cibuddy.project.configuration.jaxb.v1_0.indicator.ColorIndicator;
import com.cibuddy.project.configuration.jaxb.v1_0.indicator.Config;
import com.cibuddy.project.configuration.jaxb.v1_0.setup.Setup;
import com.cibuddy.project.configuration.jaxb.v1_0.setup.Xfd;
import java.io.File;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Simple JAXB parsing test to verify that the schema is still valid.
 * 
 * All this test does is to verify that the schema and the sample files are in
 * sync and could be used for reference.
 * 
 * @author Mirko Jahn <mirkojahn@gmail.com>
 * @version 1.0
 * @since 1.0
 */
public class RoughJaxbTest {
    
    /**
     * Test case for the indicator behavior configuration.
     * 
     * @throws Exception indicating problems.
     */
    @Test
    public void testIndicatorConfigXML() throws Exception {
        String packageName = new Config().getClass().getPackage().getName();
        File xmlFile = new File("src/test/resources/indicator-config.xml");
        assertTrue("XML File doesn't exist.", xmlFile.exists());

        JAXBContext jc = JAXBContext.newInstance(packageName);
        
        Unmarshaller unmarshaller = jc.createUnmarshaller();
	Config config = (Config) unmarshaller.unmarshal(xmlFile);

        List<ColorIndicator> actions = config.getAction();
        assertTrue(actions != null);
        // saving such file
        //jc.createMarshaller().marshal(config, new File("mysample2.xml"));
    }
    
    /**
     * Test case for the eXtremeFeedback device configuration xml itself.
     * 
     * @throws Exception indicating problems.
     */
    @Test
    public void testProjectsConfigXML() throws Exception {
        String packageName = new Setup().getClass().getPackage().getName();
        File xmlFile = new File("src/test/resources/project-setup.xml");
        assertTrue("XML File doesn't exist.", xmlFile.exists());

        JAXBContext jc = JAXBContext.newInstance(packageName);
        
        Unmarshaller unmarshaller = jc.createUnmarshaller();
	Setup setup = (Setup) unmarshaller.unmarshal(xmlFile);

        List<Xfd> xfds = setup.getXfd();
        assertTrue(xfds != null);
        // saving such file
        //jc.createMarshaller().marshal(setup, new File("mysample2.xml"));
    }
}
