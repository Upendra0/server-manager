package com.elitecore.sm.policy.validator;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.policy.dao.IPolicyActionDao;
import com.elitecore.sm.policy.model.Policy;
import com.elitecore.sm.policy.model.PolicyAction;

/**
 * The Policy Validator class
 * 
 * @author chintan.patel
 *
 */
@Component
public class PolicyActionValidator extends BaseValidator {

	protected PolicyAction policyAction;
	
	@Autowired
	IPolicyActionDao policyActionDao;
	
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
		policyAction = (PolicyAction) target;
		
		isValidate(SystemParametersConstant.POLICY_ACTION_NAME, 
					policyAction.getName(), 	
				   "name", 
				   moduleName, 
				   policyAction.getName(), 
				   validateForImport);
		
		isValidate(SystemParametersConstant.POLICY_ACTION_DESCRIPTION, 
					policyAction.getDescription(), 
				   "description", 
				   moduleName, 
				   policyAction.getDescription(), 
				   validateForImport);
		
		if(policyAction.getType() != null && "expression".equalsIgnoreCase(policyAction.getType())){
			isValidate(SystemParametersConstant.POLICY_ACTION_EXPRESSION, 
					policyAction.getActionExpression(), 
					"actionExpression", 
					moduleName, 
					policyAction.getActionExpression(), 
					validateForImport);
			
			if(policyAction.getActionExpression() != null && !policyAction.getActionExpression().isEmpty()){
				/*if(false) {  //TODO: Here need to call Function which can validate expression and if invalidate execute this block. Refer MED-4190,3945,3923
					setErrorFieldErrorMessage(String.valueOf(policyAction.getActionExpression()), "actionExpression", policyAction.getActionExpression(), "actionExpression", validateForImport,  "Policy.action.expression.wrong", getMessage("Policy.action.expression.wrong"));
				}*/
			}
		}
		if("dynamic".equalsIgnoreCase(policyAction.getType())&&policyAction.getAction().contains("=")){
			String action ;
			int index = policyAction.getAction().indexOf('=');
			if(index != -1){
				action = policyAction.getAction().substring(0, index);
//				if(StringUtils.isBlank(action)){
					isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,action,"unifiedField",moduleName,action,validateForImport);
//					errors.rejectValue("unifiedField", "database.query.unified.field.invalid", getMessage("database.query.unified.field.invalid"));
//				}
				action = policyAction.getAction().substring(index+1);
				if(StringUtils.isBlank(action)){
					errors.rejectValue("value", "database.query.value.invalid", getMessage("database.query.value.invalid"));						
				}
			}
		}
		if(policyAction.getAction().indexOf("Clone")==0){
			String[] nosOfCloneArr = policyAction.getAction().split("#");
				if(nosOfCloneArr.length == 1 || nosOfCloneArr[1] == null || nosOfCloneArr[1].isEmpty() || !StringUtils.isNumeric(nosOfCloneArr[1]))
					errors.rejectValue("action", "policyrule.numberofclone.invalid", getMessage("policyrule.numberofclone.invalid"));
				else if(Integer.parseInt(nosOfCloneArr[1]) < 1)
					errors.rejectValue("action", "policyrule.numberofclone.invalid", getMessage("policyrule.numberofclone.invalid"));
		}
	}


	public void validateNameForUniqueness(PolicyAction policyAction2, Object object,
			List<ImportValidationErrors> importErrorList, Object object2, boolean b, int serverId) {
		policyAction = policyAction2;
		this.importErrorList = importErrorList;
		long count = policyActionDao.getActionByAlies(policyAction2.getName(), serverId);
		if(count > 0){
			isValidate(SystemParametersConstant.POLICY_UNIQUE_FAILED, policyAction2.getName(), "Name", "Policy Action", "", true);
		}
		
	}
}