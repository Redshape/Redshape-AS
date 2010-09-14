package com.redshape.persistence.managers;

import com.redshape.config.ConfigException;
import com.redshape.config.IConfig;
import com.redshape.persistence.ProviderException;
import com.redshape.persistence.entities.IEntity;
import com.redshape.utils.Registry;
import org.apache.log4j.Logger;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Feb 11, 2010
 * Time: 1:50:09 PM
 * To change this template use File | Settings | File Templates.
 */
public class HibernateManagersFactory extends ManagersFactory {
    private static final Logger log = Logger.getLogger( HibernateManagersFactory.class );

    public IManager forEntity( Class<? extends IEntity> entity ) throws InstantiationException {
        try {
            for ( IManager m : this.getManagers() ) {
                if ( m.getEntityClass().isAssignableFrom( entity.getClass() ) ) {
                    return m;
                }
            }

            return this.getDefaultManagerFor( entity );
        } catch ( Throwable e ) {
            throw new InstantiationException("Method to create validator for given entity is not exists.");
        }
    }

    public Map<String, Object> createConfigObject() throws ConfigException {
        IConfig databaseConfigNode = Registry.getConfig().get("database");

        Map<String, Object> override = new HashMap<String, Object>();
        override.put("hibernate.connection.driver_class", databaseConfigNode.get("adapter") );
        override.put("hibernate.connection.url", databaseConfigNode.get("uri").value() );
        override.put("hibernate.connection.username", databaseConfigNode.get("user").value() );
        override.put("hibernate.connection.password", databaseConfigNode.get("password").value() );
        override.put("hibernate.connection.useUnicode", databaseConfigNode.get("useUTF8").value() );
        override.put("hibernate.connection.characterEncoding", databaseConfigNode.get("charset").value() );

        return override;
    }

    public EntityManagerFactory getEJBFactory() throws ConfigException, SQLException, ProviderException {
       return Persistence.createEntityManagerFactory( Registry.getConfig().get("database").get("persistenceUnit").value(), this.createConfigObject() );
    }
}