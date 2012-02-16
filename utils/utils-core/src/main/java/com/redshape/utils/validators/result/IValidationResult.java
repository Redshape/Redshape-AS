package com.redshape.utils.validators.result;

/**
 * Represents simple validation result.
 * 
 * @author nikelin
 * @date 18/04/11
 * @package com.redshape.validators.result
 */
public interface IValidationResult {
	
	public boolean isValid();

	public String getMessage();
	
}