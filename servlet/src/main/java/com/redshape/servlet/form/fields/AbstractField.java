package com.redshape.servlet.form.fields;

import com.redshape.servlet.form.AbstractFormItem;
import com.redshape.servlet.form.IFormField;
import com.redshape.servlet.form.RenderMode;
import com.redshape.servlet.form.decorators.ErrorsDecorator;
import com.redshape.servlet.form.decorators.FormFieldDecorator;
import com.redshape.servlet.form.render.IFormFieldRenderer;
import com.redshape.validators.IValidator;
import com.redshape.validators.result.IValidationResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class AbstractField<T> extends AbstractFormItem implements IFormField<T> {
	private static final long serialVersionUID = 5498825562953448526L;
	
	private String label;
	private List<IValidationResult> validationResults = new ArrayList<IValidationResult>();
	private IFormFieldRenderer<?> renderer;
	private T value;
	private List<IValidator<T, IValidationResult>> validators
									= new ArrayList<IValidator<T, IValidationResult>>();
	
	protected AbstractField() {
		this(null);
	}

	public AbstractField( String id ) {
		this(id, id);
	}
	
	protected AbstractField( String id, String name ) {
		super(id, name);
	}

	@Override
	public Collection<IValidationResult> getValidationResults() {
		return validationResults;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void setRenderer( IFormFieldRenderer renderer) {
		this.renderer = renderer;
	}

    @Override
    public void resetState() {
        this.validationResults.clear();;
    }

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String getLabel() {
		return this.label;
	}

	@Override
	public void setValue(T value) {
		this.value = value;
	}

	@Override
	public T getValue() {
		return this.value;
	}

	@SuppressWarnings("rawtypes")
	protected IFormFieldRenderer getRenderer() {
		return this.renderer;
	}
	
	@Override
	public String render() {
		return this.render( RenderMode.FULL );
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String render( RenderMode mode ) {
		if ( this.getRenderer() == null ) {
			throw new IllegalArgumentException("<null>");
		}
		
		if ( !this.hasDecorator( FormFieldDecorator.class ) ) {
			this.setDecorator( new FormFieldDecorator() );
		}
		
		if ( !this.hasDecorator( ErrorsDecorator.class ) ) {
			this.setDecorator( new ErrorsDecorator() );
		}
		
		return this.getRenderer().render(this, mode);
	}
	
	@Override
	public void addValidator(IValidator<T, IValidationResult> validator) {
		this.validators.add(validator);
	}

	@Override
	public void addValidators(
			Collection<IValidator<T, IValidationResult>> validators) {
		this.validators.addAll( validators );
	}

	@Override
	public void clearValidators() {
		this.validators.clear();
	}

	@Override
	public void removeValidator(IValidator<T, IValidationResult> validator) {
		this.validators.remove(validator);
	}

	@Override
	public boolean isValid() {
		boolean result = true;
		for ( IValidator<T, IValidationResult> validator : this.validators ) {
			IValidationResult validationResult = validator.validate( this.getValue() );
			boolean validatorResult = validationResult.isValid();
            result = result && validatorResult;
			this.validationResults.add( validationResult );
		}
		
		return result;
	}
	
}