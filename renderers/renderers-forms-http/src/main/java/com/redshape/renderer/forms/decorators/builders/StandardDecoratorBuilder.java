package com.redshape.renderer.forms.decorators.builders;

import com.redshape.form.decorators.DecoratorAttribute;
import com.redshape.form.decorators.IDecorator;
import com.redshape.form.decorators.Placement;
import com.redshape.form.decorators.builders.IDecoratorBuilder;
import com.redshape.renderer.forms.decorators.ComposedDecorator;
import com.redshape.renderer.forms.decorators.FormFieldDecorator;
import com.redshape.renderer.forms.decorators.LegendDecorator;
import com.redshape.renderer.forms.decorators.TagDecorator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StandardDecoratorBuilder implements IDecoratorBuilder {
	private Map<DecoratorAttribute, Object> attributes = new HashMap<DecoratorAttribute, Object>();
	
	@Override
	public IDecorator withLegendDecorator() {
		return new LegendDecorator();
	}

	@Override
	public IDecoratorBuilder withAttribute(DecoratorAttribute name, Object value) {
		this.attributes.put(name, value);
		return this;
	}

	@Override
	public IDecoratorBuilder withAttributes(Map<DecoratorAttribute, Object> attributes) {
		this.attributes.putAll(attributes);
		return this;
	}

	@Override
	public IDecorator withComposedDecorator(IDecorator... decorators) {
		return this.withComposedDecorator( Arrays.asList(decorators) );
	}

	@Override
	public IDecorator withComposedDecorator(List<IDecorator> decorators) {
		ComposedDecorator decorator = new ComposedDecorator(decorators);
		decorator.setAttributes( this.attributes );
		return decorator;
	}

	@Override
	public IDecorator withFormFieldDecorator() {
		FormFieldDecorator decorator = new FormFieldDecorator();
		decorator.setAttributes( this.attributes );
		return decorator;
	}
	
	@Override
	public IDecorator withTagDecorator(String tagName, Placement placement) {
		TagDecorator decorator = new TagDecorator(tagName, placement);
		decorator.setAttributes( this.attributes );
		return decorator;
	}

}