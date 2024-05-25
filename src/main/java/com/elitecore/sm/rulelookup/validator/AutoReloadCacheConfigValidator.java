package com.elitecore.sm.rulelookup.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.rulelookup.model.AutoReloadJobDetail;
import com.elitecore.sm.rulelookup.model.ScheduleTypeEnum;

@Component
public class AutoReloadCacheConfigValidator extends BaseValidator{

	@Override
	public boolean supports(Class<?> clazz) {
		return AutoReloadJobDetail.class.isAssignableFrom(clazz);
	}
	
	public void validateAutoReloadCache(Object target, Errors errors,List<ImportValidationErrors> importErrorList,String entityName,boolean validateForImport) {
		this.errors = errors;
		AutoReloadJobDetail autoReloadCache = (AutoReloadJobDetail) target;
		
		if(autoReloadCache.getRuleLookupTableData() == null || autoReloadCache.getRuleLookupTableData().getId() == -1){
			setErrorFieldErrorMessage("" , "ruleLookupTableData", null, entityName, validateForImport,  "autoReloadCache.tableName.id.invalid", getMessage("autoReloadCache.tableName.id.invalid"));
		}
		
		if(autoReloadCache.getScheduleType().toString().equalsIgnoreCase(ScheduleTypeEnum.Schedule.toString())){
			if(autoReloadCache.getScheduler()==null || autoReloadCache.getScheduler().getTrigger() == null || autoReloadCache.getScheduler().getTrigger().getID() == 0){
				setErrorFieldErrorMessage("" , "scheduler", null, entityName, validateForImport,  "autoReloadCache.scheduler.id.invalid", getMessage("autoReloadCache.scheduler.id.invalid"));
			}
		}
		
		if(autoReloadCache.getServerInstance() == null || autoReloadCache.getServerInstance().getId() == -1){
			setErrorFieldErrorMessage("" , "serverInstance", null, entityName, validateForImport,  "autoReloadCache.serverInstance.id.invalid", getMessage("autoReloadCache.serverInstance.id.invalid"));
		}
		
		if(autoReloadCache.getDatabaseQueryList() == null || autoReloadCache.getDatabaseQueryList().isEmpty()){
			setErrorFieldErrorMessage("" , "databaseQueryList", null, entityName, validateForImport,  "autoReloadCache.dbquery.id.invalid", getMessage("autoReloadCache.dbquery.id.invalid"));
		}
	}
}
