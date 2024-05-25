package com.elitecore.sm.service.filerenameconfig.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.agent.model.ServiceFileRenameConfig;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;

@Component
public class ServiceFileRenameConfigValidator extends BaseValidator {

	ServiceFileRenameConfig serviceFileRenameConfig;
	
	@Override
	public boolean supports(Class<?> clazz) {

		return ServiceFileRenameConfig.class.isAssignableFrom(clazz);
	}
	
	/**
	 * Validate Agent Parameter
	 * @param target
	 * @param errors
	 * @param importErrorList
	 * @param moduleName
	 * @param validateForImport
	 */
	public void validateServiceFileRenameAgentParam(Object target, Errors errors, List<ImportValidationErrors> importErrorList, String moduleName,
			boolean validateForImport) {
		
		if (validateForImport) {
			this.importErrorList = importErrorList;
		} else {
			this.errors = errors;
		}

		if (target instanceof ServiceFileRenameConfig) {
			serviceFileRenameConfig = (ServiceFileRenameConfig) target;
				
			logger.info("Going to validate serviceFileRenameConfig parameters :");
			
			isValidate(SystemParametersConstant.FILE_RENAME_AGENT_FILE_EXTENTION_LIST,serviceFileRenameConfig.getFileExtensitonList(),"fileExtensitonList",
					moduleName,serviceFileRenameConfig.getFileExtensitonList(),validateForImport);			
		}
	}
}
