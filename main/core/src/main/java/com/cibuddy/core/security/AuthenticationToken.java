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
