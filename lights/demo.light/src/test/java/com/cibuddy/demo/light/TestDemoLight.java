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
package com.cibuddy.demo.light;

import java.awt.Color;
import junit.framework.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Test for visual representation of the Demo Light.
 * 
 * This test requires a visual verification (if the correct color is actually,
 * drawn), therefor the test is marked as ignore to only enable in case you see
 * issues. The test could also be improved to check more details and actually
 * verify that the visual representation is correct. However, one will always 
 * require to not run in headless mode, which is not good for CI environments.
 * 
 * @author Mirko Jahn <mirkojahn@gmail.com>
 * @version 1.0
 */
@Ignore
public class TestDemoLight {
    public static DemoLight light;
    
    @BeforeClass
    public static void beforeClass(){
        light = new DemoLight();
    }
    @Test
    public void testLightSwitching() throws InterruptedException {
        Thread.sleep(2000);
        light.updateCircle(DemoLight.GREEN_LIGHT);
        Assert.assertEquals("The light is not green as expected.", Color.GREEN, light.getLightColor());
        Thread.sleep(1000);
        light.updateCircle(DemoLight.BLUE_LIGHT);
        Assert.assertEquals("The light is not blue as expected.", Color.BLUE, light.getLightColor());
        Thread.sleep(1000);
        light.updateCircle(DemoLight.RED_LIGHT);
        Assert.assertEquals("The light is not red as expected.", Color.RED, light.getLightColor());
        Thread.sleep(1000);
        light.updateCircle(DemoLight.YELLOW_LIGHT);
        Assert.assertEquals("The light is not yellow as expected.", Color.YELLOW, light.getLightColor());
        Thread.sleep(1000);
        light.turnOff();
        Assert.assertEquals("The light is not white as expected (turned off).", Color.WHITE, light.getLightColor());
        Thread.sleep(1000);
        light.updateCircle(DemoLight.GREEN_LIGHT);
        Assert.assertEquals("The light is not green as expected.", Color.GREEN, light.getLightColor());
        Thread.sleep(1000);
        
    }
}
