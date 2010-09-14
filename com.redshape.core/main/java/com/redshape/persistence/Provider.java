package com.redshape.persistence;

import com.redshape.config.ConfigException;
import com.redshape.persistence.managers.IManagersFactory;
import com.redshape.persistence.managers.ManagersFactory;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.SQLException;

public final class Provider {
    private static final Logger log = Logger.getLogger( Provider.class );
    private static IManagersFactory managersFactory = ManagersFactory.getDefault();
    private static EntityManagerFactory ejbFactory;
    private static EntityManager ejbManager;

    public static EntityManagerFactory buildManagersFactory() throws ConfigException, SQLException, ProviderException {
        return managersFactory.getEJBFactory();
    }

    public static EntityManager createManager() throws ConfigException, SQLException, ProviderException {
        return getEJBFactory().createEntityManager();
    }

    public static EntityManager getManager() throws ConfigException, SQLException, ProviderException {
        if ( ejbManager == null || !ejbManager.isOpen() ) {
            ejbManager = createManager();
        }

        return ejbManager;
    }

    public static void setManagersFactory( IManagersFactory factory ) {
        managersFactory = factory;
    }

    public static IManagersFactory getManagersFactory() {
        return managersFactory;
    }

    public static EntityManagerFactory getEJBFactory() throws ConfigException, SQLException, ProviderException {
        if (null == ejbFactory) {
            ejbFactory = buildManagersFactory();
        }

        return ejbFactory;
    }

}