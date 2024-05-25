/**
 * 
 */
package com.elitecore.sm.errorreprocess.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.errorreprocess.model.RuleConditionDetails;


/**
 * @author Ranjitsinh Reval
 *
 */
@Component
public class RuleActionConditionValidator extends BaseValidator {
	
	private RuleConditionDetails ruleConditionDetails;
	
	
	/* (non-Javadoc)
	 * @see com.elitecore.sm.common.validator.BaseValidator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return RuleConditionDetails.class.isAssignableFrom(clazz);
	}
	
	
	/**
	 * Method will validate the action and condition expression details.
	 * @param target
	 * @param errors
	 * @param moduleName
	 * @param validateForImport
	 */
	public void validateActionConditionExpression(Object target, Errors errors, String moduleName, boolean validateForImport){
		this.errors = errors;
		ruleConditionDetails = (RuleConditionDetails) target;
		isValidate(SystemParametersConstant.POLICY_ACTION_EXPRESSION,ruleConditionDetails.getActionExpression(),"actionExpression",	moduleName,ruleConditionDetails.getActionExpression(),	validateForImport);
		isValidate(SystemParametersConstant.POLICY_CONDITION_EXPRESSION,ruleConditionDetails.getConditionExpression(),"conditionExpression",	moduleName,	ruleConditionDetails.getConditionExpression(),validateForImport);
		
	}

}
