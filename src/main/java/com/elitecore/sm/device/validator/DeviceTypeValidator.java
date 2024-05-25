/**
 * 
 */
package com.elitecore.sm.device.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.device.model.DeviceType;
import com.elitecore.sm.device.model.SearchDeviceMapping;

/**
 * @author Ranjitsinh Reval
 *
 */
@Component
public class DeviceTypeValidator extends BaseValidator {

	
	private DeviceType deviceType;
	
	/**
	 * Method will check current validator class
	 *@param  Class<?> clazz
	 *@return boolean 
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return DeviceType.class.isAssignableFrom(clazz) || SearchDeviceMapping.class.equals(clazz);
	}
	
	/**
	 * Method will validate the all Device Type parameter
	 * @param target
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateDeviceType(Object target, Errors errors,String entityName,boolean validateForImport, List<ImportValidationErrors> importErrorList){
		logger.debug("Going to validate Device Type parameters.");
		deviceType = (DeviceType) target;
		setErrorObject(errors, validateForImport, importErrorList);
		isValidate(SystemParametersConstant.DEVICE_TYPE_NAME,deviceType.getName(),"deviceType.name",entityName,deviceType.getName(),validateForImport);
	}
	
}
