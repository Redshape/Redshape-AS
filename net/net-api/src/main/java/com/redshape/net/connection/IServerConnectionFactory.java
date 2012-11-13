/*
 * Copyright 2012 Cyril A. Karpenko
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.redshape.net.connection;

import com.redshape.net.IServer;
import com.redshape.net.ServerType;

/**
 * @author nikelin
 * @date 12:57
 */
public interface IServerConnectionFactory {

    public void registerSupport( ServerType type, Class<? extends IServerConnection> connectionSupport );
    
    public IServerConnection createConnection( IServer server );
    
}
