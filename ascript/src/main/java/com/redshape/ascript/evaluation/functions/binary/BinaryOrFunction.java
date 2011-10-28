package com.redshape.ascript.evaluation.functions.binary;

import com.redshape.ascript.IEvaluator;
import com.redshape.utils.Function;

import java.lang.reflect.InvocationTargetException;

/**
 * @author nikelin
 * @date 20/04/11
 * @package com.redshape.ascript.evaluation.functions.binary
 */
public class BinaryOrFunction extends Function<Object, Integer> {

	private IEvaluator evaluator;

	public BinaryOrFunction( IEvaluator evaluatorContext ) {
		this.evaluator = evaluatorContext;
	}

	@Override
	public Integer invoke(Object... arguments) throws InvocationTargetException {
		this.assertArgumentsCount( arguments, 2 );
		this.assertArgumentType( arguments[0], Integer.class );
		this.assertArgumentType( arguments[1], Integer.class );

		return (Integer) arguments[0] | (Integer) arguments[1];
	}
}
