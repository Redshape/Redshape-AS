package com.redshape.utils.range;

import com.redshape.utils.Function;
import com.redshape.utils.IFunction;
import com.redshape.utils.range.impl.DefaultRangeParser;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Cyril A. Karpenko <self@nikelin.ru>
 * @package com.redshape.utils.range
 * @date 8/14/11 6:31 PM
 */
public final class RangeBuilder {
	public static IRangeParser parser = new DefaultRangeParser();

	public static <V extends IRange<Integer>> V fromString( String value ) {
		return parser.<Integer, V>parse( value, new Function<Object, Integer>() {
			@Override
			public Integer invoke(Object... arguments) throws InvocationTargetException {
				if ( arguments.length == 0 ) {
					throw new IllegalArgumentException("Non-empty arguments list expected");
				}

				if ( arguments[0] == null ) {
					return null;
				}

				return Integer.valueOf( String.valueOf(arguments[0]) );
			}
		});
	}

	public static <T extends Comparable<T>, V extends IRange<T>> V fromString(
			String value, IFunction<?, T> normalizer ) {
		if ( parser == null ) {
			throw new IllegalStateException("Ranges parser not defined!");
		}

		return parser.parse(value, normalizer);
	}

	public static <T extends Comparable<T>> IRange<T> createSingular(T value) {
		return new SingularRange( value );
	}

	public static <T extends Comparable<T>> IRange<T> createInterval( IntervalRange.Type type, T start, T end) {
		return new IntervalRange(type, start, end);
	}

	public static <T extends Comparable<T>> IRangeList<T> createList() {
		return new ListRange();
	}
}
