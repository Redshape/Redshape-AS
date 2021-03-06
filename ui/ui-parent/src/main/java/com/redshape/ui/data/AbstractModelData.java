/*
 * Copyright 2012 Cyril A. Karpenko
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.redshape.ui.data;

import com.redshape.ui.application.events.AppEvent;
import com.redshape.ui.application.events.EventDispatcher;
import com.redshape.ui.application.events.IEventHandler;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: nikelin
 * Date: 10.01.11
 * Time: 23:53
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractModelData extends EventDispatcher
										implements IModelData {
	private static final long serialVersionUID = -843642018276791545L;
	
	private Map<String, Object> values = new HashMap<String, Object>();
    private boolean isDirty;
    private Object relatedObject;

	@Override
    public void setRelatedObject( Object relatedObject ) {
    	this.relatedObject = relatedObject;
    }
    
    @SuppressWarnings("unchecked")
	public <V> V getRelatedObject() {
    	return (V) this.relatedObject;
    }
    
    @Override
    public void set( String name, Object value ) {
    	this.processValue(name, value);
        this.values.put(name, value);
        this.markChanged(name);
    }
    
    protected void processValue( String name, Object value ) {
		if ( value == null ) {
			return;
		}

    	if ( value instanceof IModelData ) {
    		this.processChildModel( name, (IModelData) value);
    	} else if ( value instanceof Collection ) {
    		this.processCollectionValue( name, (Collection<?>) value);
    	} else if ( value.getClass().isArray() ) {
    		this.processArrayValue( name, ( Object[] ) value );
    	}
    }
    
    protected void processArrayValue( String name, final Object[] data ) {
    	this.processCollectionValue( name, Arrays.asList(data) );
    }
    
    protected void processCollectionValue( String name, final Collection<?> data ) {
    	if ( data.isEmpty() ) {
    		return;
    	}
    	
    	Object testObject = null;
    	Iterator<?> iterator = data.iterator();
    	do { 
    		testObject = iterator.next();
    	} while ( testObject == null && iterator.hasNext() );
    	
    	if ( !(testObject instanceof IModelData) ) {
    		return;
    	}
    	
    	while ( iterator.hasNext() ) {
    		this.processChildModel(name, (IModelData) testObject);
    		
    		testObject = iterator.next();
    	}
    }
    
    protected void processChildModel( final String name, final IModelData model ) {
    	model.addListener( 
			ModelEvent.CHANGED, 
			new IEventHandler() {
				private static final long serialVersionUID = 8396714245364287214L;

				@Override
				public void handle(AppEvent event) {
					AbstractModelData.this.forwardEvent( ModelEvent.CHANGED, name, model );
				}
			}
		);
    }

    protected void markChanged( String fieldName ) {
    	this.makeDirty(true);
    	this.forwardEvent( ModelEvent.CHANGED, fieldName );
    }
    
    @Override
    public void makeDirty( boolean value ) {
    	this.isDirty = value;
    }
    
    @Override
    public boolean isDirty() {
    	return this.isDirty;
    }

	@Override
	public void remove() {
		this.forwardEvent( ModelEvent.REMOVED );
	}
    
    @SuppressWarnings("unchecked")
    @Override
	public <V> V get( String name ) {
        return (V) this.values.get(name);
    }

    @SuppressWarnings("unchecked")
	protected <V> Map<String, V> getAll() {
        return (Map<String, V>) this.values;
    }

}
