package com.redshape.server;

import com.redshape.io.server.IServer;
import com.redshape.io.server.policy.IPolicy;
import com.redshape.io.server.policy.PolicyType;

import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.collections.map.MultiKeyMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Aug 2, 2010
 * Time: 2:39:58 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractServerFactory implements IServerFactory {
    private MultiKeyMap policies = new MultiKeyMap();    
    private Map<String, Object> properties = new HashMap<String, Object>();

    public AbstractServerFactory() {
        this( new HashMap<String, Object>() );
    }

    public AbstractServerFactory( Map<String, Object> properties ) {
        this.properties = properties;
    }

    protected void bindProperties( IServer<?, ?> server, Map<String, Object> properties ) {
        for ( String name : properties.keySet() ) {
            if ( server.isPropertySupports(name) ) {
                server.setProperty( name, properties.get(name) );
            }
        }
    }

    @SuppressWarnings("unchecked")
	protected <T, V> void bindPolicies( IServer<T,V> server, MultiKeyMap policies ) {
        for ( final Object itemKey : policies.keySet() ) {
            final IPolicy<V> policy = (IPolicy<V>) policies.get(itemKey);
            final Object[] keys = ( (MultiKey) itemKey ).getKeys();

            server.addPolicy( (Class<T>) keys[0], (PolicyType) keys[1], (IPolicy<V>) policy );
        }
    }

    @Override
    public void addPolicy( Class<?> protocolContext, PolicyType type, IPolicy<?> policy ) {
        this.policies.put( protocolContext, type, policy );
    }

    @Override
    public MultiKeyMap getPolicies() {
        return this.policies;
    }

    @Override
    public void setProperties( Map<String, Object> properties ) {
        this.properties = properties;
    }

    @Override
    public Map<String, Object> getProperties() {
        return this.properties;
    }

    public Object getProperty( String name ) {
        return this.properties.get(name);
    }

    public void setProperty( String name, Object value ) {
        this.properties.put( name, value );
    }
    
}