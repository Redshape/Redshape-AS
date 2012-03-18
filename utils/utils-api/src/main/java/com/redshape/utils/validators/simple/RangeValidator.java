package com.redshape.utils.validators.simple;

import com.redshape.utils.ILambda;
import com.redshape.utils.range.IRange;
import com.redshape.utils.range.RangeBuilder;
import com.redshape.utils.range.RangeUtils;
import com.redshape.utils.range.normalizers.NumericNormalizer;
import com.redshape.utils.validators.AbstractValidator;
import com.redshape.utils.validators.result.ValidationResult;

/**
 * @author Cyril A. Karpenko <self@nikelin.ru>
 * @package com.redshape.validators.impl.common
 * @date 8/16/11 8:49 PM
 */
public class RangeValidator extends AbstractValidator<String, ValidationResult> {
	private IRange<Integer> range;
	private ILambda<Integer> normalizer;

	public RangeValidator( IRange<Integer> range ) {
		this(range, new NumericNormalizer() );
	}

	public RangeValidator( IRange<Integer> range, ILambda<Integer> normalizer ) {
		if ( range == null ) {
			throw new IllegalArgumentException("<null>");
		}

		this.normalizer = normalizer;
		this.range = range;
	}

	@Override
	public boolean isValid(String value) {
		return value == null || value.isEmpty()
				|| RangeUtils.checkIntersections( range, RangeBuilder.fromString(value, this.normalizer) );
	}

	@Override
	public ValidationResult validate(String value) {
		return new ValidationResult( this.isValid(value),
				"Given value not fit acceptable values range: " + this.range.getStart() + " - " + this.range.getEnd() );
	}

}