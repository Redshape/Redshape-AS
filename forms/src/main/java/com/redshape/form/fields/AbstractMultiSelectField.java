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

package com.redshape.form.fields;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AbstractMultiSelectField<T> extends AbstractSelectField<T> {
	private static final long serialVersionUID = 3242971188656570012L;

	private List<T> selected = new ArrayList<T>();
	
	protected AbstractMultiSelectField() {
		this(null);
	}
	
	protected AbstractMultiSelectField( String id ) {
		this(id, null);
	}
	
	protected AbstractMultiSelectField( String id, String name ) {
		super(id, name);
	}

	@Override
	public boolean hasMultiValues() {
		return true;
	}

	@Override
	public void setValue( T value ) {
		if ( value == null ) {
			return;
		}

		if ( value instanceof Collection ) {
			this.setValues( (Collection<T>) value );
			return;
		}

		if ( !this.checkConstraintsFor(value) ) {
			throw new IllegalStateException("Not bounded value provided!");
		}

		this.selected.add( value );
	}

	protected boolean checkConstraintsFor( Object value ) {
		boolean bounded = false;
		for ( Object constraintItem : this.getOptions().values() ) {
			if ( constraintItem.toString().equals( value.toString() ) ) {
				bounded = true;
				break;
			}
		}

		return bounded;
	}

	public void setValues( Collection<T> values ) {
		if ( !this.isValue(values) ) {
			throw new IllegalArgumentException("Values constraint exception");
		}

		this.selected.addAll(values);
	}

	protected boolean isValue( Collection<T> values ) {
		for ( T value : values ) {
			if ( !this.isValue(value) ) {
				return false;
			}
		}

		return true;
	}

	protected boolean isValue( T value ) {
		for ( T registered : this.getOptions().values() ) {
			if ( registered.toString().equals( value.toString() ) ) {
				return true;
			}
		}

		return false;
	}

	@Override
	public T getValue() {
		if ( this.selected.isEmpty() ) {
			return null;
		}

		return this.selected.iterator().next();
	}

	public List<T> getValues() {
		return this.selected;
	}
}
