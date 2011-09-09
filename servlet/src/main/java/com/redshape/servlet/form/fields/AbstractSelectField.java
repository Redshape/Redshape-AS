package com.redshape.servlet.form.fields;

import com.redshape.servlet.form.IDataFormField;
import com.redshape.servlet.form.data.IFieldDataProvider;
import org.apache.commons.collections.OrderedMap;
import org.apache.commons.collections.map.LinkedMap;

import java.util.Map;

public class AbstractSelectField<T> extends AbstractField<T> implements IDataFormField<T> {
	private static final long serialVersionUID = 7948336414439747793L;
	private OrderedMap options = new LinkedMap();
	private IFieldDataProvider dataProvider;

	public AbstractSelectField() {
		this(null);
	}
	
	public AbstractSelectField( String id ) {
		this(id, id);
	}
	
	public AbstractSelectField( String id, String name ) {
		super(id, name );
	}

	public IFieldDataProvider getDataProvider() {
		return dataProvider;
	}

	@Override
	public void setDataProvider(IFieldDataProvider<T> provider) {
		this.dataProvider = provider;
		this.invalidate();
	}

	public void addOptions( Map<String, T> values ) {
		if ( values == null ) {
			return;
		}

		this.options.putAll( values );
	}
	
	public void addOption( String name, T value ) {
		if ( name == null ) {
			throw new IllegalArgumentException("<nul>");

		}
		this.options.put( name, value );
	}

	protected void invalidate() {
		if ( this.getDataProvider() == null ) {
			return;
		}

		if ( !this.getDataProvider().invalidate() ) {
			this.removeAll();
			this.addOptions( this.getDataProvider().provideCollection() );
		}
	}

	public Map<String, T> getOptions() {
		this.invalidate();

		return this.options;
	}

	public void removeAll() {
		this.options.clear();
	}
	
	public void removeOption( String name ) {
		if ( name == null ) {
			throw new IllegalArgumentException("<null>");
		}

		this.options.remove(name);
	}
	
}
