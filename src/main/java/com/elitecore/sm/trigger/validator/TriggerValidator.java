/**
 * 
 */
package com.elitecore.sm.trigger.validator;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.trigger.model.CrestelSMTrigger;
import com.elitecore.sm.trigger.service.TriggerService;


@Component
public class TriggerValidator extends BaseValidator {
	
	private CrestelSMTrigger trigger;

	@Autowired
	TriggerService triggerService;
	
	/**
	 * Validate Trigger
	 * @param target
	 * @param errors
	 * @param moduleName
	 * @param validate
	 */
	public void validateTrigger(Object target, Errors errors,String moduleName,boolean validate) {
		
		this.errors = errors;
		trigger = (CrestelSMTrigger) target;
		validateTriggerBasicParam(trigger, null, false);
		validateTriggerRecurrencePattern(trigger, BaseConstants.TRIGGER, false);
	}
	
	/**
	 * Validate Basic Parameter of Trigger
	 * @param target
	 * @param moduleName
	 * @param validate
	 */
	private void validateTriggerBasicParam(Object target, String moduleName,boolean validate) {
		
		trigger = (CrestelSMTrigger) target;
		
		isValidate(SystemParametersConstant.TRIGGER_NAME,trigger.getTriggerName(),"triggerName",moduleName,trigger.getTriggerName(),validate);
		isValidate(SystemParametersConstant.TRIGGER_DESCRIPTION,trigger.getDescription(),"description",moduleName,trigger.getDescription(),validate);

	}
	
	private void validateTriggerRecurrencePattern(CrestelSMTrigger trigger, String entityName,boolean validate) {
		String className=trigger.getClass().getName();
		String svcName=trigger.getTriggerName();
		
		this.trigger = (CrestelSMTrigger) trigger;
		String recurrenceType = trigger.getRecurrenceType();
		if(trigger.getAlterationCount()==null){
			trigger.setAlterationCount(0);
		}
		if("Minute".equals(recurrenceType)){
			isValidate(SystemParametersConstant.TRIGGER_ALTERATIONCOUNT_FOR_MINUTE,trigger.getAlterationCount(),"alterationCount",entityName,svcName,validate,className);
		}else if("Hourly".equals(recurrenceType)){
			isValidate(SystemParametersConstant.TRIGGER_ALTERATIONCOUNT_FOR_HOURLY,trigger.getAlterationCount(),"alterationCount",entityName,svcName,validate,className);
		}else if("Daily".equals(recurrenceType)){
			isValidate(SystemParametersConstant.TRIGGER_ALTERATIONCOUNT_FOR_DAILY,trigger.getAlterationCount(),"alterationCount",entityName,svcName,validate,className);
		}else if("Weekly".equals(recurrenceType)){
			
		}else if("Monthly".equals(recurrenceType)){
			isValidate(SystemParametersConstant.TRIGGER_ALTERATIONCOUNT_FOR_MONTHLY,trigger.getAlterationCount(),"alterationCount",entityName,svcName,validate,className);
			if(trigger.getFirstOrLastDayOfMonth()==null || "".equals(trigger.getFirstOrLastDayOfMonth())){
				if(trigger.getDayOfMonth()==null){
					trigger.setDayOfMonth(0);
				}
				isValidate(SystemParametersConstant.TRIGGER_DAYOFMONTH,trigger.getDayOfMonth(),"dayOfMonth",entityName,svcName,validate,className);	
			}
		}
	}
}
