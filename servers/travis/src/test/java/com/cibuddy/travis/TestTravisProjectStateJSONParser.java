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
package com.cibuddy.travis;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Scanner;
import junit.framework.Assert;
import org.junit.Test;

/**
 *
 * @author Mirko Jahn <mirkojahn@gmail.com>
 */
public class TestTravisProjectStateJSONParser {
    
    @Test
    public void testIt() throws FileNotFoundException, URISyntaxException {
        File jsonFile = new File("src/test/resources/com/cibuddy/travis/travis_sample.json");
        Assert.assertTrue("XML File doesn't exist.", jsonFile.exists());
        String json =  new Scanner(jsonFile).useDelimiter("\\Z").next();
        TravisProjectState project = TravisProjectState.builder(json);
        Assert.assertNotNull("project is null, but should be populated through the json string.", project);
    }
}
