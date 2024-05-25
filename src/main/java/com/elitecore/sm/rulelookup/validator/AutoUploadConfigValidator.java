package com.elitecore.sm.rulelookup.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.rulelookup.model.AutoUploadJobDetail;


@Component
public class AutoUploadConfigValidator extends BaseValidator{

	
	@Override
	public boolean supports(Class<?> clazz) {
		return AutoUploadJobDetail.class.isAssignableFrom(clazz);
	}
	
	public void validateAutoUpload(Object target, Errors errors,List<ImportValidationErrors> importErrorList,String entityName,boolean validateForImport) {
		this.errors = errors;
		AutoUploadJobDetail autoUpload = (AutoUploadJobDetail) target;
		
		setErrorObject(errors, false, importErrorList);
		
		if(autoUpload.getRuleLookupTableData() == null || autoUpload.getRuleLookupTableData().getId() == -1){
			setErrorFieldErrorMessage("" , "ruleLookupTableData", null, entityName, validateForImport,  "autoUpload.tableName.id.invalid", 
					getMessage("autoUpload.tableName.id.invalid"));
		}
		if( autoUpload.getSourceDirectory() == null || "".equals(autoUpload.getSourceDirectory()) ){
			setErrorFieldErrorMessage("" , "sourceDirectory", null, entityName, validateForImport,  "autoUpload.sourceDirectory.invalid", 
					getMessage("autoUpload.sourceDirectory.invalid"));
		}else{
			isValidate(SystemParametersConstant.AUTOUPLOADJOBDETAIL_SOURCEDIRECTORY, autoUpload.getSourceDirectory(),  "sourceDirectory"  , "sourceDirectory", entityName, false);
		}
		if( autoUpload.getAction() == null || "".equals(autoUpload.getAction()) ){
			setErrorFieldErrorMessage("" , "action", null, entityName, validateForImport,  "autoUpload.action.invalid", 
					getMessage("autoUpload.action.invalid"));
		}
		/** if( autoUpload.getFilePrefix() == null || "".equals(autoUpload.getFilePrefix()) ){
			setErrorFieldErrorMessage("" , "filePrefix", null, entityName, validateForImport,  "autoUpload.filePrefix.invalid", 
					getMessage("autoUpload.filePrefix.invalid"));  
		}
		if( autoUpload.getFileContains() == null || "".equals(autoUpload.getFileContains()) ){
		  	setErrorFieldErrorMessage("" , "fileContains", null, entityName, validateForImport,  "autoUpload.fileContains.invalid", 
					getMessage("autoUpload.fileContains.invalid")); 
		} **/
		if(autoUpload.getScheduler().getTrigger() == null || autoUpload.getScheduler().getTrigger().getID() == 0){
			setErrorFieldErrorMessage("" , "scheduler", null, entityName, validateForImport,  "autoUpload.job.invalid", getMessage("autoUpload.job.invalid"));
		}
		
		
	}
}
