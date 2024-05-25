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
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.device.model.SearchDeviceMapping;


/**
 * @author Ranjitsinh Reval
 *
 */
@Component
public class DeviceValidator extends BaseValidator {

	private Device device;
	
	/**
	 * Method will check current validator class
	 *@param  Class<?> clazz
	 *@return boolean 
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return Device.class.isAssignableFrom(clazz) || SearchDeviceMapping.class.equals(clazz);
	}
	
	/**
	 * Method will validate the all device parameters
	 * @param target
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateDevice(Object target, Errors errors,String entityName,boolean validateForImport, List<ImportValidationErrors> importErrorList){
		logger.debug("Going to validate device parameters.");
		
		setErrorObject(errors, validateForImport, importErrorList);
		
		device = (Device) target;
		isValidate(SystemParametersConstant.DEVICE_NAME,device.getName(),"name",entityName,device.getName(),validateForImport);
	}
	
}
