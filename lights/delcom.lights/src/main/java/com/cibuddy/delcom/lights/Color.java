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
package com.cibuddy.delcom.lights;

/**
 *
 * @author mirkojahn
 */
public enum Color {
    BLACK((byte)0xFF), 
    GREEN((byte)0xFE), // depending on device might also be blue
    RED ((byte)0xFD), 
    YELLOW((byte)0xFB), 
    BLUE((byte)0xFE); // depending on device might also be green
    
    private byte code;

    private Color(byte c) {
        code = c;
    }

    public byte getCode() {
        return code;
    }
}
