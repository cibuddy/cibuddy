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
package com.cibuddy.ibuddy;

import java.io.IOException;

/**
 *
 * @author mirkojahn
 */
public interface IBuddyFigure {

    short DEVICE_VENDOR = 0x1130;
    byte ALL_OFF = (byte) 0xff;
    
    byte[] SET_COMMAND = new byte[]{ (byte) 0x00, 
        (byte) 0x55, (byte) 0x53, (byte) 0x42, (byte) 0x43, 
        (byte) 0x00, (byte) 0x40, (byte) 0x02, (byte) 0x00};
    
    byte [] SETUP_COMMAND = new byte[] { (byte) 0x00,
        (byte) 0x22, (byte) 0x09, (byte) 0x00, (byte) 0x02, 
        (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00};
    
    void close() throws IOException;

    void open() throws IOException;

    void resetEverything();

    void setCurrentColor(Color currentColor);

    void setHeart(boolean heart);
}
