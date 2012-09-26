package com.redshape.ascript.evaluation.functions.arrays;

import com.redshape.ascript.IEvaluator;
import com.redshape.utils.InvocationException;
import com.redshape.utils.Lambda;

import java.util.List;

/**
 * @author nikelin
 * @date 20/04/11
 * @package com.redshape.ascript.evaluation.functions.arrays
 */
public class CARFunction extends Lambda<Object> {
	private IEvaluator evaluator;

	public CARFunction( IEvaluator evaluatorContext ) {
		this.evaluator = evaluatorContext;
	}

	@Override
	public Object invoke( Object... args ) throws InvocationException {
		try {
			this.assertArgumentsCount( args, 1 );
			this.assertArgumentType( args[0], List.class );

			return ( (List) args[0] ).get(0);
		} catch ( Throwable e ) {
			throw new InvocationException( e.getMessage(), e );
		}
	}

}
