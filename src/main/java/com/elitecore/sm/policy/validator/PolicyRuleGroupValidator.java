package com.elitecore.sm.policy.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.policy.dao.IPolicyGroupDao;
import com.elitecore.sm.policy.model.PolicyGroup;

/**
 * The Policy Rule Group Validator class
 * 
 * @author chintan.patel
 *
 */
@Component
public class PolicyRuleGroupValidator extends BaseValidator {

	private PolicyGroup policyGroup;
	
	@Autowired
	IPolicyGroupDao policyGroupDao;
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.common.validator.BaseValidator#supports(java.lang.Class)
	 */
	public boolean supports(Class<?> clazz) {
		return PolicyGroup.class.isAssignableFrom(clazz);
	}
	

	/**
	 * Validate Basic Parameter of Service
	 * @param target
	 * @param errors
	 * @param moduleName
	 * @param validateForImport
	 */
	public void validatePolicyRuleGroupParameters(Object target, Errors errors, List<ImportValidationErrors> importErrorList, String moduleName, boolean validateForImport) {
	
		if(validateForImport) {
			this.importErrorList = importErrorList;
		} else {
			this.errors = errors;
		}
		
		policyGroup = (PolicyGroup) target;
		
		isValidate(SystemParametersConstant.POLICY_GROUP_NAME, 
				   policyGroup.getName(), 
				   "name", 
				   moduleName, 
				   policyGroup.getName(), 
				   validateForImport);
		
		isValidate(SystemParametersConstant.POLICY_GROUP_DESCRIPTION, 
				   policyGroup.getDescription(), 
				   "description", 
				   moduleName, 
				   policyGroup.getName(), 
				   validateForImport);

	}


	public void validateNameForUniqueness(PolicyGroup policyGroup2, Object object,
			List<ImportValidationErrors> importErrorList2, Object object2, boolean b, int serverId) {
		policyGroup = policyGroup2;
		this.importErrorList = importErrorList2;
		long count = policyGroupDao.getPolicyGroupCountByAlias(policyGroup2.getAlias(), serverId);
		if(count > 0){
			isValidate(SystemParametersConstant.POLICY_UNIQUE_FAILED, policyGroup2.getAlias(), "Name", "Policy Group", "", true);
		}
	}
}
