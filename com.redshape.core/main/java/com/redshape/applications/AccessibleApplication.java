package com.redshape.applications;

import com.redshape.applications.bootstrap.Bootstrap;
import org.apache.log4j.Logger;

/**
 * Abstract realization of accessible application
 *
 * @author nikelin
 * @project vio
 * @package com.vio.applications
 * @date Apr 19, 2010
 */
public abstract class AccessibleApplication extends Application implements IAccessibleApplication {
    private static final Logger log = Logger.getLogger( AccessibleApplication.class );

    public AccessibleApplication( Class<?> context, String[] args, Bootstrap bootstrap ) throws ApplicationException {
        super( context, args, bootstrap );
    }

}