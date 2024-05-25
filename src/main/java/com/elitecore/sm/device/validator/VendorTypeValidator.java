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
import com.elitecore.sm.device.model.SearchDeviceMapping;
import com.elitecore.sm.device.model.VendorType;

/**
 * @author Ranjitsinh Reval
 *
 */
@Component
public class VendorTypeValidator extends BaseValidator{

	
	private VendorType vendorType;
	
	/**
	 * Method will check current validator class
	 *@param  Class<?> clazz
	 *@return boolean 
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return VendorType.class.isAssignableFrom(clazz) || SearchDeviceMapping.class.equals(clazz);
	}
	
	/**
	 * Method will validate the all Vendor Type parameter
	 * @param target
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateVendorType(Object target, Errors errors,String entityName,boolean validateForImport, List<ImportValidationErrors> importErrorList){
		logger.debug("Going to validate vendor type parameters.");
		vendorType = (VendorType) target;
		setErrorObject(errors, validateForImport, importErrorList); // Set error object for import and general validation.
		
		isValidate(SystemParametersConstant.VENDOR_TYPE_NAME,vendorType.getName(),"vendorType.name",entityName,vendorType.getName(),validateForImport);
	}
}
