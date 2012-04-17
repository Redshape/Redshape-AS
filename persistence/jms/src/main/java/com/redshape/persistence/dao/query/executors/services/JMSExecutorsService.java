package com.redshape.persistence.dao.query.executors.services;

import com.redshape.persistence.dao.DAOException;
import com.redshape.persistence.dao.query.IQuery;
import com.redshape.persistence.dao.query.executors.result.IExecutorResult;
import com.redshape.persistence.dao.query.executors.result.IExecutorResultFactory;
import com.redshape.persistence.entities.IEntity;
import com.redshape.persistence.jms.protocol.IQueryMarshaller;
import com.redshape.persistence.jms.protocol.ProtocolException;
import com.redshape.utils.Constants;
import org.apache.log4j.Logger;

import javax.jms.*;
import java.lang.IllegalStateException;
import java.util.UUID;

/**
 * @author Cyril A. Karpenko <self@nikelin.ru>
 * @package com.redshape.persistence.dao.executors.services
 * @date 1/25/12 {1:49 PM}
 */
public class JMSExecutorsService implements IQueryExecutorService {
    private static final Logger log = Logger.getLogger( JMSExecutorsService.class );

    private QueueConnection connection;
    private QueueSession session;

    private IQueryMarshaller protocol;

    private MessageProducer producer;

    private Queue destinationQueueAddr;
    private Queue destination;
    private String destinationQueue;
    
    private Object sendingLock = new Object();
    
    private int timeout = Constants.TIME_SECOND * 15;

    public JMSExecutorsService( QueueConnection connection,
                                IQueryMarshaller marshaller,
                                String destinationQueue )
            throws DAOException {
        this.connection = connection;
        this.protocol = marshaller;
        this.destinationQueue = destinationQueue;
        this.checkFields();
        this.init();
    }

    @Override
    public void setResultObjectsFactory(IExecutorResultFactory factory) {
        throw new UnsupportedOperationException();
    }

    protected QueueSession getSession() {
        return this.session;
    }

    protected int getTimeout() {
        return this.timeout;
    }

    public void setTimeout( int timeout ) {
        this.timeout = timeout;
    }
    
    protected void checkFields() {
        if ( this.connection == null ) {
            throw new IllegalStateException("<null>: JMS connection not provided");
        }
    }

    protected QueueConnection getConnection() {
        return connection;
    }

    protected IQueryMarshaller getProtocol() {
        return this.protocol;
    }

    protected String getDestinationQueue() {
        return destinationQueue;
    }

    protected void createSession() throws DAOException {
        try {
            this.session = this.connection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            this.session.run();
        } catch ( JMSException e ) {
            throw new DAOException("Unable to establish JMS session through provided connection");
        }
    }

    protected void checkSession() throws DAOException {
        try {
            this.session.commit();
        } catch ( Throwable e ) {
            if ( !( e instanceof javax.jms.IllegalStateException ) ) {
                throw new IllegalStateException(e);
            }

            this.createSession();
        }
    }
    
    protected void init() throws DAOException {
        this.createSession();

        try {
            this.destinationQueueAddr = this.getSession().createQueue( this.getDestinationQueue() );
            this.producer = this.getSession().createSender( destinationQueueAddr );
        } catch ( JMSException e ) {
            throw new DAOException( "Unable to start messages producing thread", e );
        }

        try {
            this.destination = this.getSession().createQueue("results-" + System.nanoTime() + "-receiver");
        } catch ( JMSException e ) {
            throw new DAOException( "Unable to start messages consuming thread", e );
        }
    }
    
    @Override
    public <T extends IEntity> IExecutorResult<T> execute(IQuery<T> query) throws DAOException {
        try {
            synchronized (this.sendingLock) {
                this.checkSession();

                String uid = "ID:" + UUID.randomUUID().toString();

                long start = System.currentTimeMillis();
                Message message = this.getSession().createObjectMessage();
                message.setJMSCorrelationID(uid);
                message.setStringProperty("UID", uid);
                message.setJMSReplyTo(this.destination);
                this.getProtocol().marshal(message, query);

                this.producer.send( this.destinationQueueAddr, message );

                Message respond = this.getSession().createConsumer(this.destination)
                        .receive(this.getTimeout());
                if ( respond == null ) {
                    throw new DAOException("Request processing failed!");
                }

                respond.acknowledge();

                IExecutorResult<T> result = this.getProtocol().unmarshalResult( respond );
                log.debug("Processed in " + (System.currentTimeMillis() - start) + "ms");
                return result;
            }
        } catch ( ProtocolException e ) {
            throw new DAOException( "Query serializing failed", e );
        } catch ( JMSException e ) {
            throw new DAOException( "Failed to proceed message send", e );
        }
    }
}
