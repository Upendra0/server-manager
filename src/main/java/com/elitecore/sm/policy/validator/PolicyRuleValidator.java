package com.elitecore.sm.policy.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.policy.dao.IPolicyRuleDao;
import com.elitecore.sm.policy.model.PolicyRule;
import com.elitecore.sm.policy.service.IPolicyService;

/**
 * The Policy Validator class
 * 
 * @author chintan.patel
 *
 */
@Component
public class PolicyRuleValidator extends BaseValidator {

	private PolicyRule policyRule;
	
	@Autowired
	IPolicyService policyService;
	
	@Autowired
	IPolicyRuleDao policyRuleDao;
	
	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.common.validator.BaseValidator#supports(java.lang.Class)
	 */
	public boolean supports(Class<?> clazz) {
		return PolicyRule.class.isAssignableFrom(clazz);
	}
	

	/**
	 * Validate Basic Parameter of Service
	 * @param target
	 * @param errors
	 * @param moduleName
	 * @param validateForImport
	 */
	public void validatePolicyRuleParameters(Object target, Errors errors, List<ImportValidationErrors> importErrorList, String moduleName, 
			                                 boolean validateForImport , int serverId , String userAction ) {
	
		if(validateForImport) {
			this.importErrorList = importErrorList;
		} else {
			this.errors = errors;
		}
		
		policyRule = (PolicyRule) target;
		
		if(isValidate(SystemParametersConstant.POLICY_RULE_NAME, 
				   policyRule.getName(), 
				   "name", 
				   moduleName, 
				   policyRule.getName(), 
				   validateForImport)) { 
			
			validateNameForUniqueRule(target, errors, importErrorList, moduleName, validateForImport, serverId , userAction );
		} 
				  
		
		isValidate(SystemParametersConstant.POLICY_RULE_DESCRIPTION, 
				   policyRule.getDescription(), 
				   "description", 
				   moduleName, 
				   policyRule.getName(), 
				   validateForImport);
		
		isValidate(SystemParametersConstant.POLICY_RULE_ALERT_DESCRIPTION, 
				   policyRule.getAlertDescription(),
				   "alertDescription", 
				   moduleName, 
				   policyRule.getAlertDescription(), 
				   validateForImport);
		
		isValidate(SystemParametersConstant.POLICY_RULE_GLOBAL_SEQUENCE_RULE, 
				   policyRule.getGlobalSequenceRuleId(), 
				   "globalSequenceRuleId", 
				   moduleName, 
				   policyRule.getGlobalSequenceRuleId(), 
				   validateForImport);
		isValidate(SystemParametersConstant.POLICY_RULE_ERROR_CODE, 
				   policyRule.getErrorCode(), 
				   "errorCode", 
				   moduleName, 
				   policyRule.getErrorCode(), 
				   validateForImport);
		
		
	}


	public void validateNameForUniqueness(PolicyRule policyRule2, Object object,
			List<ImportValidationErrors> importErrorList2, Object object2, boolean b, int serverId) {
		policyRule = policyRule2;
		this.importErrorList = importErrorList2;
		long count = policyRuleDao.getPolicyRuleCountByAlias(policyRule2.getName(), serverId);
		if(count > 0){
			 isValidate(SystemParametersConstant.POLICY_UNIQUE_FAILED, policyRule2.getName(), "Name", "Policy Rule", "", true);
		}
	}
	
	/**
	 *  Validate Name For Unique Rule
	 *  
	 * @author chetan
	 * 
	 */
	public void validateNameForUniqueRule(Object target, Errors errors, List<ImportValidationErrors> importErrorList, String moduleName,
			boolean validateForImport , int serverId , String userAction ) {		
		 
		if(validateForImport) {
			this.importErrorList = importErrorList;
		} else {
			this.errors = errors;
		}
		
		policyRule = (PolicyRule) target;
		
		long noOFAliases = policyService.getPolicyRuleCountByAlias( policyRule , serverId );
		
		if( "createRule".equalsIgnoreCase(userAction)){
			if( noOFAliases > 0 ){
			    setErrorFieldErrorMessage( policyRule.getName() , "name", moduleName, moduleName , validateForImport, SystemParametersConstant.POLICY_RULE_NAME , 
			    		null , noOFAliases ,"policyrule.name.duplicate");
			}
		}		
		else if("updateRule".equalsIgnoreCase(userAction) ){
			PolicyRule policyRuleByID = policyService.getPolicyRuleById( policyRule.getId() );			 
			
			if(!(policyRuleByID.getName().equals(policyRule.getName()))){
		    	if( noOFAliases > 0 ){
			    setErrorFieldErrorMessage( policyRule.getName() , "name", moduleName, moduleName , validateForImport, SystemParametersConstant.POLICY_RULE_NAME , 
			    		null , noOFAliases ,"policyrule.name.duplicate");
			 }
		   }
		}
		
	}
	
}
