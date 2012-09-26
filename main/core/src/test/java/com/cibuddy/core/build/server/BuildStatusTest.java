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
package com.cibuddy.core.build.server;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
/**
 * VERY simple test for the BuildStatus class.
 * 
 * @author mirkojahn
 * @since 1.0
 */
public class BuildStatusTest {
    
    /**
     * String comparison of the obtained BuildStatus.
     * 
     * This test is kinda hilarious - content wise, but I already had a typo, so
     * this still makes some sense. It also serves as the first test of this 
     * module...
     * 
     * @throws Exception test Exception
     * @since 1.0
     */
    @Test
    public void testStringParsing() throws Exception {
        assertTrue(BuildStatus.get("blue").equals(BuildStatus.blue));
        assertTrue(BuildStatus.get("red").equals(BuildStatus.red));
        // just make sure it is not always returning true.
        assertTrue(!BuildStatus.get("red").equals(BuildStatus.blue));
    }
    
}
