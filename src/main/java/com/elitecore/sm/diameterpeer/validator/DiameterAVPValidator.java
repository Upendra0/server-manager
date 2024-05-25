package com.elitecore.sm.diameterpeer.validator;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.diameterpeer.model.DiameterAVP;
import com.elitecore.sm.services.service.ServicesService;

@Component
public class DiameterAVPValidator extends BaseValidator {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	ServicesService servicesService;
	
	
	public void validateAVPforPeer(DiameterAVP avp,Errors errors,String entityName,boolean validateForImport,List<ImportValidationErrors> avpValidationError){
		if(validateForImport){
			this.importErrorList=avpValidationError;
		}else{
			this.errors = errors;
		}
		String moduleName = "DiameterAVP";
		isValidate(SystemParametersConstant.DIAMETERAVP_ATTRIBUTEVALUE, avp.getValue(), "value", entityName, moduleName, validateForImport);
	}
}
