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

package com.redshape.persistence.utils;

import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author Cyril A. Karpenko <self@nikelin.ru>
 * @package com.redshape.persistence.utils
 * @date 1/25/12 {4:35 PM}
 */
public class JMSConnectionUtil {

    public static final QueueConnection createConnection()
            throws JMSException, NamingException {
        InitialContext ctx = new InitialContext();
        QueueConnectionFactory factory = (QueueConnectionFactory) ctx.lookup("queue/connectionFactory");
        QueueConnection connection = factory.createQueueConnection();

        connection.start();

        return connection;
    }

}
