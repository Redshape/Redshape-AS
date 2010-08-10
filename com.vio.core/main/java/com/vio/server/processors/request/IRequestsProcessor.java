package com.vio.server.processors.request;

import com.vio.io.protocols.core.IProtocol;
import com.vio.io.protocols.core.request.IRequest;
import com.vio.server.ISocketServer;
import com.vio.server.ServerException;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Jun 6, 2010
 * Time: 3:21:23 AM
 * To change this template use File | Settings | File Templates.
 */
public interface IRequestsProcessor<V extends ISocketServer, T extends IRequest> {

    public void setServerContext( V protocol );

    public V getServerContext();

    public void onRequest( T request ) throws ServerException, IOException;

}