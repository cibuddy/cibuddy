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
