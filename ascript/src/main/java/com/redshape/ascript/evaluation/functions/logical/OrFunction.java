package com.redshape.ascript.evaluation.functions.logical;

import com.redshape.ascript.IEvaluator;
import com.redshape.utils.Function;

import java.lang.reflect.InvocationTargetException;

/**
 * @author nikelin
 * @date 20/04/11
 * @package com.api.deployer.expressions.evaluation.functions.conditional
 */
public class OrFunction extends Function<Object, Boolean> {

	private IEvaluator evaluator;

	public OrFunction( IEvaluator evaluatorContext ) {
		this.evaluator = evaluatorContext;
	}

	@Override
	public Boolean invoke(Object... arguments) throws InvocationTargetException {
		this.assertArgumentsType( arguments, Boolean.class );

		boolean result = true;
		for ( Object argument : arguments ) {
			result = result || (Boolean) argument;
		}

		return result;
	}

}
