package com.elitecore.sm.policy.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.policy.dao.IPolicyDao;
import com.elitecore.sm.policy.model.Policy;

/**
 * The Policy Validator class
 * 
 * @author chintan.patel
 *
 */
@Component
public class PolicyValidator extends BaseValidator {

	private Policy policy;
	
	@Autowired
	IPolicyDao policyDao;
	
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
	public void validatePolicyParameters(Object target, Errors errors, List<ImportValidationErrors> importErrorList, String moduleName, boolean validateForImport) {
	
		if(validateForImport) {
			this.importErrorList = importErrorList;
		} else {
			this.errors = errors;
		}
		
		policy = (Policy) target;
		
		isValidate(SystemParametersConstant.POLICY_NAME, 
				   policy.getName(), 
				   "name", 
				   moduleName, 
				   policy.getName(), 
				   validateForImport);
		
		isValidate(SystemParametersConstant.POLICY_DESCRIPTION, 
				   policy.getDescription(), 
				   "description", 
				   moduleName, 
				   policy.getName(), 
				   validateForImport);

	}

	public void validatePolicyUniqueName(Policy policy2, Object object, List<ImportValidationErrors> importErrorList2,
			Object object2, boolean validateForImport, int serverId) {
		policy = policy2;
		this.importErrorList = importErrorList2;
		long count = policyDao.getPolicyCountByAlias(policy2.getName(), serverId);
		if(count > 0){
			isValidate(SystemParametersConstant.POLICY_UNIQUE_FAILED, policy2.getName(), "Name", "Policy", "", true);
		}
	}
}
