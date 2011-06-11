package com.redshape.persistence.entities;

import com.redshape.persistence.dao.ManagerException;

import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: Jun 24, 2010
 * Time: 4:35:18 PM
 * To change this template use File | Settings | File Templates.
 */
@MappedSuperclass
public abstract class AbstractMultilingualEntity<T extends IMultilingual<T>> extends AbstractEntity
                                                                          implements IMultilingual<T> {
	private static final long serialVersionUID = -2410675666548871832L;

	@ManyToOne
    private Locale locale;

    @ManyToOne
    private T original;

    @OneToMany( mappedBy = "original" )
    private Set<T> versions;

    abstract public boolean hasTranslation( Locale locale ) throws ManagerException;

    abstract public T getTranslation( Locale locale );

    public void setLocale( Locale locale ) {
        this.locale = locale;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setOriginal( T entity ) {
        this.original = entity;
    }

    public T getOriginal() {
        return this.original;
    }

    public Set<T> getVersions() {
        return this.versions;
    }
}
