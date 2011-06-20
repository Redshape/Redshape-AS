package com.redshape.applications.bootstrap;

import com.redshape.utils.PackagesLoader;
import com.redshape.utils.config.IConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Jun 22, 2010
 * Time: 2:42:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public abstract class AbstractBootstrapAction implements IBootstrapAction {
	/**
	 * Current action markers
	 */
	private Set<ActionMarker> markers = new HashSet<ActionMarker>();

    /**
     * Identifier for current bootstrap action (must be unique)
     * @var Object
     */
    private Object id;
    
    @Autowired( required = true )
    private IConfig config;
    
    @Autowired( required = true )
    private PackagesLoader packagesLoader;

    /**
     * Identifiers for actions on which current action depends on
     * @var Collection<Object> 
     */
    private Collection<Object> dependencies = new HashSet<Object>();

    private IBootstrap bootstrap;

    public void setBootstrap( IBootstrap bootstrap ) {
        this.bootstrap = bootstrap;
    }

    public IBootstrap getBootstrap() {
        return this.bootstrap;
    }

    public void setConfig( IConfig config ) {
    	this.config = config;
    }
    
    public IConfig getConfig() {
    	return this.config;
    }
    
    /**
     * Change current id to given one
     * @param id
     */
    public void setId( Object id ) {
        this.id = id;
    }

    /**
     * Get current action identifier
     * @return
     */
    public Object getId() {
        return this.id;
    }

    /**
     * Get all dependencies for current action
     * @return
     */
    public Collection<Object> getDependencies() {
        return this.dependencies;
    }

    /**
     * Add new dependency for action
     * @param id
     */
    public void addDependency( Object id ) {
        this.dependencies.add(id);
    }

	public void addMarker( ActionMarker marker ) {
		this.markers.add(marker);
	}

	public boolean hasMarkers( ActionMarker... markers ) {
		return this.markers.containsAll( Arrays.asList( markers ) );
	}

	public Collection<ActionMarker> getMarkers() {
		return this.markers;
	}

	public boolean hasMarker( ActionMarker marker ) {
		return this.markers.contains( marker );
	}

    public boolean hasDependencies() {
        return this.dependencies.isEmpty();
    }
    
    public void setPackagesLoader( PackagesLoader loader ) {
    	this.packagesLoader = loader;
    }
    
    protected PackagesLoader getPackagesLoader() {
    	return this.packagesLoader;
    }
}
