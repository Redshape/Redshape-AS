package com.redshape.servlet.form;

import com.redshape.servlet.core.IHttpRequest;
import com.redshape.servlet.form.render.IFormRenderer;

import java.util.List;

public interface IForm extends com.redshape.servlet.form.IFormItem {

	public void copy( IForm form );

	public <T> void setValue( String path, T value );

	public <T> List<T> getValues( String name );

	public <T> T getValue( String name );

	public void setProcessHandler( com.redshape.servlet.form.IFormProcessHandler handler );
	
	public void process( IHttpRequest request ) throws com.redshape.servlet.form.InvalidDataException;
	
	public void setLegend( String legend );

	public boolean hasValue( String path );

	public String getLegend();
	
	public void setAction( String action );
	
	public String getAction();
	
	public void setMethod( String method );
	
	public String getMethod();
	
	public <T extends IForm> T findContext( String name );
	
	public <T, V extends IFormField<T>> V findField( String name );
	
	public void addField( IFormField<?> field );

	public void removeField( String path );

	public void removeContext( String path );

	public void remove();

	public void removeField( com.redshape.servlet.form.IFormField<?> field );
	
	public List<com.redshape.servlet.form.IFormField<?>> getFields();
	
	public void addSubForm( IForm form, String name );
	
	public void removeSubForm( String name );
	
	public List<IForm> getSubForms();
	
	public List<com.redshape.servlet.form.IFormItem> getItems();

	public IFormRenderer getRenderer();
	
	public void setRenderer( IFormRenderer renderer );
	
}
