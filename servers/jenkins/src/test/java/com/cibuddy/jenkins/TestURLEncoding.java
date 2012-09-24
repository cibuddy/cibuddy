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
