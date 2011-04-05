package com.redshape.ui.data;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 10.01.11
 * Time: 13:08
 * To change this template use File | Settings | File Templates.
 */
public interface IModelData extends Serializable {
	
	public void setRelatedObject( Object object );
	
	public <V> V getRelatedObject();
	
	public void makeDirty( boolean isDirty );
	
	public boolean isDirty();
	
	public <V> V get( String name );
	
	public void set( String name, Object value );

}
