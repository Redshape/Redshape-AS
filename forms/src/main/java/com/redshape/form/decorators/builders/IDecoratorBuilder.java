package com.redshape.form.decorators.builders;

import com.redshape.form.decorators.DecoratorAttribute;
import com.redshape.form.decorators.IDecorator;
import com.redshape.form.decorators.Placement;

import java.util.List;
import java.util.Map;

public interface IDecoratorBuilder {

	public IDecoratorBuilder withAttribute( DecoratorAttribute name, Object value );
	
	public IDecoratorBuilder withAttributes( Map<DecoratorAttribute, Object> attributes );
	
	public IDecorator withFormFieldDecorator();
	
	public IDecorator withComposedDecorator( IDecorator... decorators );
	
	public IDecorator withComposedDecorator( List<IDecorator> decorators );
	
	public IDecorator withTagDecorator( String tagName, Placement placement );
	
	public IDecorator withLegendDecorator();
	
}