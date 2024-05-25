/**
 * 
 */
package com.elitecore.sm.iam.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.iam.model.AccessGroup;
import com.elitecore.sm.iam.model.Action;
import com.elitecore.sm.util.Regex;

/**
 * @author Sunil Gulabani
 * Apr 21, 2015
 */
@Component
public class AccessGroupValidator extends BaseValidator{
	
	@Override
	public boolean supports(@SuppressWarnings("rawtypes") Class clazz) {
		return AccessGroup.class.isAssignableFrom(clazz);
	}
	@Override
    public void validate(Object target, Errors errors){
    	this.errors = errors;
    	AccessGroup accessGroup = (AccessGroup) target;
    	
    	if(accessGroup.getId() !=0){
    		logger.debug("Validating for Access Group Update");
    	}else{
    		logger.debug("Validating for Access Group Save");
    	}
    	validateName(accessGroup.getName());
    	validateDescription(accessGroup.getDescription());
    	validateActions(accessGroup.getActions());
    }

    private void validateActions(List<Action> actionList){
    	if(actionList == null || (actionList.isEmpty())){
    		errors.rejectValue("actions", "error.access.group.actions.is.null", getMessage("access.group.actions.is.null"));
    	}
    }

    private void validateName(String name){
    	isValidate(SystemParametersConstant.ACCESSGROUP_NAME,name,"name",null,null,false);
    }
    
	private void validateDescription(String value){
    	
    	isValidate(SystemParametersConstant.ACCESSGROUP_DESCRIPTION,value,"description",null,null,false);
    }
    
    public boolean validateReasonForChange(String value){
    	
    	logger.debug("Regex for change::"+Regex.get(SystemParametersConstant.STAFF_REASON_FOR_CHANGE_REGEX));
		
		if(!StringUtils.isEmpty(value)){
		return match("^[\\p{L} .'-]+{1,255}$", value);
			
		}
		else{
			return true;
		}
    }
}