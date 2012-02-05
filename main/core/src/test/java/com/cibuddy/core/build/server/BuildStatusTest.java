package com.cibuddy.core.build.server;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
/**
 *
 * @author mirkojahn
 */
public class BuildStatusTest {
    @Test
    public void testStringParsing() throws Exception {
        assertTrue(BuildStatus.get("blue").equals(BuildStatus.blue));
        assertTrue(!BuildStatus.get("red").equals(BuildStatus.blue));
    }
    
}
