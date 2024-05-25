package com.elitecore.sm.consolidationservice.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.consolidationservice.model.DataConsolidationGroupAttribute;
import com.elitecore.sm.services.model.DataConsolidationService;

@Component(value = "consolidationGroupAttributeValidator")
public class ConsolidationGroupAttributeValidator extends BaseValidator {

	@Override
	public boolean supports(Class<?> clazz) {
		boolean retunStatement = false;
		if ((DataConsolidationGroupAttribute.class.isAssignableFrom(clazz))
				|| (DataConsolidationService.class.isAssignableFrom(clazz))) {
			retunStatement = true;
		}

		return retunStatement;
	}
	
	/**
	 * Validate consolidation definition Parameters
	 * @param target
	 * @param errors
	 * @param importErrorList
	 * @param validateForImport
	 */
	public void validateConsolidationDefinitionGroupAttributes(Object target, Errors errors,List<ImportValidationErrors> importErrorList,boolean validateForImport){
		
		logger.info("Target class is : " + target.getClass());
		
		if(validateForImport){
			this.importErrorList=importErrorList;
		}else{
			this.errors = errors;	
		}
		
		if(target instanceof DataConsolidationGroupAttribute){
			DataConsolidationGroupAttribute groupAttribute = (DataConsolidationGroupAttribute)target;
			isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,groupAttribute.getGroupingField(),"groupingField",null,groupAttribute.getGroupingField(),validateForImport);
			if(groupAttribute.getDestinationField()!="")
				isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,groupAttribute.getDestinationField(),"destinationField",null,groupAttribute.getDestinationField(),validateForImport);
			if(groupAttribute.isRegExEnable()){
				isValidate(SystemParametersConstant.CONSOLIDATION_DEF_GROUP_REGEX_EXP, groupAttribute.getRegExExpression(),"regExExpression", null, "", validateForImport);
			}
			if(groupAttribute.isLookUpEnable()){
				isValidate(SystemParametersConstant.CONSOLIDATION_DEF_LOOKUP_TABLE_NAME, groupAttribute.getLookUpTableName(),"lookUpTableName", null, "", validateForImport);
				isValidate(SystemParametersConstant.CONSOLIDATION_DEF_LOOKUP_TABLE_COLNAME, groupAttribute.getLookUpTableColumnName(),"lookUpTableColumnName", null, "", validateForImport);
			}
		}
	}
}
