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
package com.cibuddy.core.security;

import java.io.Serializable;

/**
 * Generic carrier interface for authentication information.
 * 
 * This interface intends to abstract all authentication requirements in the most
 * generic possible way. Subclasses will further refine extended requirements.
 *
 * @author Mirko Jahn <mirkojahn@gmail.com>
 * @since 1.0
 * @version 1.0
 */
public interface AuthenticationToken extends Serializable {
    
    /**
     * The principal is the entity used for authentication. 
     * 
     * This could be something as simple as a username or more complex, like a
     * X.509 certificate. What this acutally reflects is application specific
     * and defined in the implementation classes.
     * 
     * @return the principal
     * @since 1.0
     */
    Object getPrincipal();
    
    /**
     * The credentials required for authentication.
     * 
     * The credentials could be something like a password, a token or a key file
     * for example. Ultimately this is defined by the application and the class
     * implementing this interface.
     * 
     * @return credentials associated to this principal
     * @since 1.0
     */
    Object getCredentials();
    
}
