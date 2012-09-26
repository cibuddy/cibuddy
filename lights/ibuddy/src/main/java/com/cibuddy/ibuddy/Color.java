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
 *
 * @author mirkojahn
 */
public enum Color {

    BLACK(7), RED(6), GREEN(5), YELLOW(4), BLUE(3), PURPLE(2), CYAN(1), LIGHTBLUE(0);

    private int code;

    private Color(int c) {
        code = c;
    }

    public int getCode() {
        return code;
    }
}
