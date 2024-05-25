package com.elitecore.sm.license.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.license.model.Circle;

/**
 * Validation class for Circle
 * 
 * @author sterlite
 *
 */
@Component
public class CircleValidator extends BaseValidator {

	Circle circle;

	@Override
	public boolean supports(Class<?> clazz) {
		return Circle.class.equals(clazz);
	}

	/**
	 * Validate Circle details
	 * 
	 * @param target
	 * @param errors
	 * @param importErrorList
	 * @param moduleName
	 * @param validateForImport
	 */
	public void validateCircleDetailsParam(Object target, Errors errors, String moduleName, boolean validateForImport) {

		this.errors = errors;
		circle = (Circle) target;
		isValidate(SystemParametersConstant.CIRCLE_NAME, circle.getName(), "name", moduleName, circle.getName(), validateForImport);
		isValidate(SystemParametersConstant.CIRCLE_DESCRIPTION, circle.getDescription(), "description", moduleName, circle.getDescription(), validateForImport);
	}

}
