package com.vio.server.policy;

import com.vio.exceptions.ExceptionWithCode;
import com.vio.server.IServer;
import com.vio.server.ServerException;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Jun 14, 2010
 * Time: 1:46:10 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IPolicy<T, V extends IServer> {

    public void setContext( V server );

    public V getContext();

    public boolean applicate( T subject );

    public ExceptionWithCode getLastException();

    public void resetLastException();

}