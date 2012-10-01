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

/**
 * Class providing type information for the different i-Buddies.
 * 
 * Each i-Buddy type is encoded with a unique ProductId when connected to the 
 * USB. This id is then exposed in the HID and taken for comparison. All tested
 * values are encoded in this class (others might certainly be possible, but are
 * not tested - as mentioned).
 * 
 * @author mirkojahn
 * @since 1.0
 * @version 1.0
 */
public enum FigureType {
    
    IBUDDY_GENERATION_1(1), IBUDDY_GENERATION_2(2), IBUDDY_BLACK(3), DEVIL(5), QUEEN(6);
    private int type;

    private FigureType(int t) {
        type = t;
    }

    public int getType() {
        return type;
    }
}
