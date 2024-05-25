package com.elitecore.sm.consolidationservice.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.consolidationservice.model.DataConsolidationAttribute;
import com.elitecore.sm.services.model.DataConsolidationService;

@Component(value = "consolidationAttributeValidator")
public class ConsolidationAttributeValidator extends BaseValidator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		boolean retunStatement = false;
		if ((DataConsolidationAttribute.class.isAssignableFrom(clazz))
				|| (DataConsolidationService.class.isAssignableFrom(clazz))) {
			retunStatement = true;
		}

		return retunStatement;
	}

	
	/**
	 * Method will set error message 
	 * @param dataConsolidation
	 * @param importErrorList
	 */
	public void setDataConsolidationValidationMsg(DataConsolidation dataConsolidation, List<ImportValidationErrors> importErrorList){
		ImportValidationErrors importErrors = new ImportValidationErrors(dataConsolidation.getConsName(),dataConsolidation.getConsName(), "consName", dataConsolidation.getConsName(), getMessage("fail.import.dataconsolidation.consName"));
		importErrorList.add(importErrors);
	}
	
	/**
	 * Validate consolidation definition Parameters for consolidation Attribute
	 * @param target
	 * @param errors
	 * @param importErrorList
	 * @param validateForImport
	 */
	public void validateConsolidationDefinitionAttributes(Object target, Errors errors,List<ImportValidationErrors> importErrorList,boolean validateForImport){
		if(validateForImport){
			this.importErrorList=importErrorList;
		}else{
			this.errors = errors;	
		}
		DataConsolidationAttribute consolidationAttribute = (DataConsolidationAttribute)target;
		if(target instanceof DataConsolidationAttribute){
			isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,consolidationAttribute.getFieldName(),"fieldName",null,consolidationAttribute.getFieldName(),validateForImport);
		}
	}
	

}
