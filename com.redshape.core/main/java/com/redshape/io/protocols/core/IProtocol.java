package com.redshape.io.protocols.core;

import com.redshape.api.dispatchers.IDispatcher;
import com.redshape.config.ConfigException;
import com.redshape.exceptions.ExceptionWithCode;
import com.redshape.io.protocols.core.readers.IRequestReader;
import com.redshape.io.protocols.core.readers.ReaderException;
import com.redshape.io.protocols.core.request.IRequest;
import com.redshape.io.protocols.core.response.IResponse;
import com.redshape.io.protocols.core.sources.input.BufferedInput;
import com.redshape.io.protocols.core.sources.output.OutputStream;
import com.redshape.io.protocols.core.writers.IResponseWriter;
import com.redshape.io.protocols.core.writers.WriterException;
import com.redshape.io.protocols.core.request.RequestType;
import com.redshape.server.processors.request.IRequestsProcessor;
import com.redshape.server.processors.connection.IClientsProcessor;

import java.util.Collection;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Jul 31, 2010
 * Time: 4:11:22 PM
 * To change this template use File | Settings | File Templates.
 */
public interface IProtocol<
                T extends IRequest,
                D extends IDispatcher,
                V extends IResponse,
                I extends BufferedInput> {

    public void setReader( IRequestReader<I, T> reader );

    public IRequestReader<I, T> getReader();

    public void setWriter( IResponseWriter writer );

    public IResponseWriter getWriter();

    public void setRequestsProcessor( Class<? extends IRequestsProcessor<?, T>> processorClass );

    public IRequestsProcessor<?, T> createRequestsProcessor() throws ProtocolException;

    public void setClientsProcessor( IClientsProcessor processor );

    public IClientsProcessor getClientsProcessor();

    public void writeResponse( OutputStream stream, V response) throws WriterException;

    public void writeResponse( OutputStream stream, Collection<? extends V> responses ) throws WriterException;

    public void writeResponse( OutputStream stream, ExceptionWithCode exception ) throws WriterException;

    public T readRequest( I stream ) throws ReaderException;

    public boolean isAnonymousAllowed() throws ConfigException;

    public IProtocolVersion getProtocolVersion();

    public String getConstant( Constants id );

    public D getRequestDispatcher( RequestType type );

    public void setRequestsDispatcher( RequestType type, D dispatcher );

}