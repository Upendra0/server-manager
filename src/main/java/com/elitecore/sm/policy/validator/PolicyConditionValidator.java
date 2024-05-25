package com.elitecore.sm.policy.validator;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.policy.dao.IPolicyConditionDao;
import com.elitecore.sm.policy.model.Policy;
import com.elitecore.sm.policy.model.PolicyCondition;

/**
 * The Policy Validator class
 * 
 *
 */
@Component
public class PolicyConditionValidator extends BaseValidator {

	private PolicyCondition policyCondition;
	
	@Autowired
	IPolicyConditionDao policyConditionDao;
	
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.common.validator.BaseValidator#supports(java.lang.Class)
	 */
	public boolean supports(Class<?> clazz) {
		return Policy.class.isAssignableFrom(clazz);
	}
	

	/**
	 * Validate Basic Parameter of Service
	 * @param target
	 * @param errors
	 * @param moduleName
	 * @param validateForImport
	 */
	public void validatePolicyParameters(Object target, Errors errors, String moduleName, boolean validateForImport) {
		
		this.errors = errors;
		policyCondition = (PolicyCondition) target;
		
		isValidate(SystemParametersConstant.POLICY_CONDITION_NAME, 
					policyCondition.getName(), 
				   "name", 
				   moduleName, 
				   policyCondition.getName(), 
				   validateForImport);
		
		isValidate(SystemParametersConstant.POLICY_CONDITION_DESCRIPTION, 
					policyCondition.getDescription(), 
				   "description", 
				   moduleName, 
				   policyCondition.getDescription(), 
				   validateForImport);
		
		if(policyCondition.getType() != null && "expression".equalsIgnoreCase(policyCondition.getType())){
			isValidate(SystemParametersConstant.POLICY_CONDITION_EXPRESSION, 
					policyCondition.getConditionExpression(), 
					"conditionExpression", 
					moduleName, 
					policyCondition.getConditionExpression(), 
					validateForImport);
			
			if(policyCondition.getConditionExpression() != null && !policyCondition.getConditionExpression().isEmpty()){
				/*if(false) {  //TODO: Here need to call Function which can validate expression and if invalidate execute this block. Refer MED-4190,3945,3923
					setErrorFieldErrorMessage(String.valueOf(policyCondition.getConditionExpression()), "conditionExpression", policyCondition.getConditionExpression(), "conditionExpression", validateForImport,  "Policy.condition.expression.wrong", getMessage("Policy.condition.expression.wrong"));
				}*/
			}
		}
		if("dynamic".equalsIgnoreCase(policyCondition.getType())&& StringUtils.isNotBlank(policyCondition.getUnifiedField())){
			isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,policyCondition.getUnifiedField(),"unifiedField",moduleName,policyCondition.getUnifiedField(),validateForImport);
		}
		if("dynamic".equalsIgnoreCase(policyCondition.getType())&&!StringUtils.isNotBlank(policyCondition.getValue())){
			errors.rejectValue("value", "database.query.value.invalid", getMessage("database.query.value.invalid"));
		}

	}


	public void validateNameForUniqueness(PolicyCondition policyCondition2, Object object,
			List<ImportValidationErrors> importErrorList, Object object2, boolean b, int serverId) {
		policyCondition = policyCondition2;
		this.importErrorList = importErrorList;
		long count = policyConditionDao.getConditionByAlies(policyCondition2.getName(), serverId);
		if(count > 0){
			isValidate(SystemParametersConstant.POLICY_UNIQUE_FAILED,policyCondition2.getName(), "Name", "Policy Condition", "", true);
		}
	}
}