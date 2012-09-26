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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * This test only servers as a reference and reminder.
 * 
 * <p>Encoding Strings with spaces and special characters usually causes problems,
 * when done wrong. Additionally there are multiple ways of encoding a URL, so
 * this test class serfs as a testbed to validate corner cases and illustrate
 * potential problems.</p>
 * 
 * @author Mirko Jahn <mirkojahn@gmail.com>
 */
public class TestURLEncoding {
    
    private static Map<String,String> map = new Hashtable<String,String>();
    
    /**
     * setup of the strings to be compared after applying the encoding.
     */
    @BeforeClass
    public static void before () {
        map.put("some%20space%20between", "some space between");
        map.put("some%2Bsign_escaped", "some+sign_escaped");
        map.put("%C3%A4%C3%B6%C3%BC%C3%9F", "äöüß");
    }
    
    /**
     * Only test method checking for the compatibility of the created strings.
     * 
     * @throws UnsupportedEncodingException 
     */
    @Test
    public void testURL() throws UnsupportedEncodingException {
        Set<Entry<String, String>> entrySet = map.entrySet();
        Iterator<Entry<String, String>> iter = entrySet.iterator();
        while(iter.hasNext()){
            Entry<String, String> next = iter.next();
            // not nice, but the easiest way I came up with so far (without 3rd parties) or using URI
            String enc = URLEncoder.encode(next.getValue(),"UTF-8").replace("+", "%20");
            System.out.println("exp: "+next.getKey()+" - got: "+enc);
            Assert.assertEquals("URL encoding failed for this pair!",
                    next.getKey(), enc);
        }
    }
}
