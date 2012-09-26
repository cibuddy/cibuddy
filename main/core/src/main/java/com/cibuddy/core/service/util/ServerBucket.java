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
package com.cibuddy.core.service.util;

import com.cibuddy.core.build.server.IServer;
import org.osgi.framework.ServiceRegistration;

/**
 * Lightweight Key Value holder Object.
 * 
 * This object is intended to minimize footprint for storing single key Value Pair
 * associations in one object. This could also be done with other means, but
 * was not done for the sake of simplicity and readability.
 * 
 * @author mirkojahn
 * @since 1.0
 * @version 1.0
 */
public class ServerBucket {
    
    private ServiceRegistration registration;
    private IServer server;

    public ServerBucket(ServiceRegistration sr, IServer iServer) {
        this.registration = sr;
        this.server = iServer;
    }
    
    public ServiceRegistration getServiceRegistration() {
        return registration;
    }

    public IServer getServer() {
        return server;
    }
    
}
