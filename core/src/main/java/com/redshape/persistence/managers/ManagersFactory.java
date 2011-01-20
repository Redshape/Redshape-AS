package com.redshape.persistence.managers;

import com.redshape.config.IConfig;
import com.redshape.persistence.entities.IEntity;
import com.redshape.utils.InterfacesFilter;
import com.redshape.utils.PackageLoaderException;
import com.redshape.utils.PackagesLoader;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Feb 11, 2010
 * Time: 1:44:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
abstract public class ManagersFactory implements IManagersFactory {
    private static Logger log = Logger.getLogger( ManagersFactory.class );
    private static IManagersFactory defaultInstance = new HibernateManagersFactory();
    private static Map<Class<? extends IManagersFactory>, IManagersFactory> factories = new HashMap<Class<? extends IManagersFactory>, IManagersFactory>();
    
    private Map<Class<? extends IManager>, IManager> managers = new HashMap<Class<? extends IManager>, IManager>();
    private Map<Class<? extends IEntity>, IManager> entities = new HashMap<Class<? extends IEntity>, IManager>();
    
    @Autowired( required = true )
    private IConfig config;
    @Autowired( required = true )
    private PackagesLoader packagesLoader;
    
    public static IManagersFactory getDefault() {
        return defaultInstance;
    }

    public ManagersFactory() {
        try {
            this.initializeManagers();
        } catch ( Throwable e ) {
            log.error( e.getMessage(), e );       
        }
    }

    public static IManagersFactory createInstance( Class<? extends IManagersFactory> clazz ) throws InstantiationException, IllegalAccessException {
        IManagersFactory instance = factories.get(clazz);
        if ( instance != null ) {
            return instance;
        }

        instance = clazz.newInstance();

        factories.put( clazz, instance );

        return instance;
    }
    
    public void setPackagesLoader( PackagesLoader loader ) {
    	this.packagesLoader = loader;
    }
    
    protected PackagesLoader getPackagesLoader() {
    	return this.packagesLoader;
    }
    
    public void setConfig( IConfig config ) {
    	this.config = config;
    }
    
    protected IConfig getConfig() {
    	return this.config;
    }

    public IManager getForEntity( IEntity entity ) throws ManagerException {
        return this.getForEntity( entity.getClass() );
    }

    public IManager getForEntity( Class<? extends IEntity> entity ) throws ManagerException {
        IManager v = this.entities.get(entity);
        if ( v != null ) {
            return v;
        }

        try {
            v = this.forEntity( entity );
        } catch ( Throwable e ) {
            log.error( e.getMessage(), e );
            throw new ManagerException();
        }

        this.entities.put( entity, v );

        return v;
    }

    protected Collection<IManager> getManagers() {
        return this.managers.values();
    }

    protected IManager getDefaultManagerFor( Class<? extends IEntity> clazz ) {
        return new Manager( clazz );
    }

    abstract protected IManager forEntity( Class<? extends IEntity> entity ) throws InstantiationException;

    public <T extends Manager> T getManager( Class<T> clazz ) {
        try {
            IManager v =this.managers.get(clazz);
            if ( v != null ) {
                return (T) v;
            }

            v = clazz.newInstance();

            this.managers.put( clazz, v );

            return (T) v;
        } catch ( Throwable e ) {
            return null;
        }
    }

    private void initializeManagers() throws PackageLoaderException {
        for( Class managerClazz : this.getPackagesLoader().getClasses("com.redshape.persistence.managers", new InterfacesFilter( new Class[] { Manager.class} ) ) ) {
            try {
                IManager manager = (IManager) managerClazz.newInstance();
                this.managers.put( managerClazz, (IManager) managerClazz.newInstance() );
                this.entities.put( manager.getEntityClass(), manager );
            } catch ( Throwable e ) {
            }
        }
    }
}
