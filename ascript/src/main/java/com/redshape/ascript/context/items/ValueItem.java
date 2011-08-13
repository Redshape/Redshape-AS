package com.redshape.ascript.context.items;

import com.redshape.ascript.EvaluationException;
import com.redshape.ascript.context.IEvaluationContextItem;

import java.lang.reflect.Method;


public class ValueItem implements IEvaluationContextItem {
	private Object value;
	
	public ValueItem( Object value ) {
		this.value = value;
	}
	
	@Override
	public <V> V getValue(String name) throws EvaluationException {
		throw new EvaluationException("Scalars does not have nested fields");
	}

	@SuppressWarnings("unchecked")
	@Override
	public <V> V getValue() throws EvaluationException {
		return (V) this.value;
	}
	
	@Override
	public Method getMethod( String name, int argumentsCount, Class<?>[] types ) throws EvaluationException {
		throw new EvaluationException("Restricted operation on scalar");
	}

}
