package com.elitecore.sm.rulelookup.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.rulelookup.model.LookupFieldDetailData;

@Component
public class RuleLookupFieldValidator extends BaseValidator{
	
	@Override
	public boolean supports(Class<?> clazz) {
		return LookupFieldDetailData.class.isAssignableFrom(clazz);
	}
	
	public void validateRuleLookupFields(Object target, Errors errors,String entityName){
		try{
			LookupFieldDetailData lookupFieldDetailData = (LookupFieldDetailData)target;
			isValidate(SystemParametersConstant.RULE_LOOKUP_FIELD_NAME,lookupFieldDetailData.getFieldName(),"fieldName",entityName,lookupFieldDetailData.getFieldName(),false);
			isValidate(SystemParametersConstant.RULE_LOOKUP_DISPLAY_NAME,lookupFieldDetailData.getDisplayName(),"displayName",entityName,lookupFieldDetailData.getDisplayName(),false);
		}catch(Exception e){
			errors.reject("Could not add without fields");
			logger.debug("Exception occurred while validation");
			logger.trace(e);
		}
		
	}
}
