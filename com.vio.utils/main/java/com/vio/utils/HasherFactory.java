package com.vio.utils;

import org.apache.log4j.Logger;

import java.util.Map;
import java.util.HashMap;

/**
 * @author flare
 * @author nikelin
 */
public class HasherFactory {
    private static final Logger log = Logger.getLogger( HasherFactory.class );

    private static HasherFactory defaultInstance = new HasherFactory();
    private Map<Class<? extends IHasher>, IHasher> hashers = new HashMap<Class<? extends IHasher>, IHasher>();

    public static HasherFactory getDefault() {
        return defaultInstance;
    }

    public static void setDefault( HasherFactory factory ) {
        defaultInstance = factory;
    }

    protected HasherFactory() {
        this.init();
    }

    protected void init() {
        try {
            for ( Class<? extends IHasher> clazz : Registry.getPackagesLoader()
                                               .<IHasher>getClasses("com.vio.utils.hashers", new InterfacesFilter( new Class[] { IHasher.class }, true ) ) ) {
                this.hashers.put(clazz, clazz.newInstance() );
            }
        } catch ( Throwable e ) {
            log.error( e.getMessage(), e );
        }
    }

    public IHasher getHasher( Class<? extends IHasher> clazz ) {
        return this.hashers.get(clazz);
    }

    public IHasher getHasher( String name ) {
        for( IHasher hasher : this.hashers.values() ) {
            if ( hasher.getName().equals(name) ) {
                return hasher;
            }
        }

        return null;
     }
}