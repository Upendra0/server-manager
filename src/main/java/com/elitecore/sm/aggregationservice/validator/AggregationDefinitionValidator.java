package com.elitecore.sm.aggregationservice.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.aggregationservice.model.AggregationDefinition;
import com.elitecore.sm.aggregationservice.service.IAggregationDefinitionService;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.services.model.AggregationService;

@Component(value = "aggregationDefinitionValidator")
public class AggregationDefinitionValidator extends BaseValidator {

	@Autowired
	IAggregationDefinitionService aggregationDefinitionService;
	
	@Override
	public boolean supports(Class<?> clazz) {
		boolean retunStatement = false;
		if ((AggregationDefinition.class.isAssignableFrom(clazz))
				|| (AggregationService.class.isAssignableFrom(clazz))) {
			retunStatement = true;
		}

		return retunStatement;
	}
	
	/**
	 * Validate Aggregation Definition Parameters
	 * @param target
	 * @param errors
	 * @param importErrorList
	 * @param validateForImport
	 */
	public void validateAggregationDefinitionParams(Object target, Errors errors,List<ImportValidationErrors> importErrorList,boolean validateForImport){
		logger.info("Target class is : " + target.getClass());
		
		if(validateForImport){
			this.importErrorList=importErrorList;
		}else{
			this.errors = errors;	
		}
		String className = AggregationDefinition.class.getName();
		AggregationDefinition aggDefinition = (AggregationDefinition)target;
		
		isValidate(SystemParametersConstant.AGGREGATION_DEFINITION_NAME, aggDefinition.getAggDefName(), "aggDefName", null, null, validateForImport);
		if(validateForImport == false){
			if(aggDefinition.getId() != 0){
				validateNameForUniqueness(aggDefinition,errors);
			}
		}
		isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD, aggDefinition.getUnifiedDateFiled(),"unifiedDateFiled",null, aggDefinition.getUnifiedDateFiled(),validateForImport);
		if(aggDefinition.getPartCDRField()!="")
			isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD, aggDefinition.getPartCDRField(),"partCDRField",null, aggDefinition.getPartCDRField(),validateForImport);
	
		isValidate(SystemParametersConstant.AGGREGATION_DEFINITION_NOOFPARTITION,aggDefinition.getNoOfPartition(),"AggregationDefinition.noOfPartition.invalid","noOfPartition", null, null,validateForImport,className);
		
		//business validations
		logger.debug("PartCDRField Value : " + aggDefinition.getPartCDRField());
		if(!aggDefinition.getPartCDRField().equalsIgnoreCase("NONE")){
			isValidate(SystemParametersConstant.AGGREGATION_DEFINITION_AGGINTERVAL,aggDefinition.getAggInterval(),"AggregationDefinition.aggInterval.invalid","aggInterval", null, null,validateForImport,className);
			if(aggDefinition.getfLegVal() != null && (aggDefinition.getfLegVal().length() < 0 || aggDefinition.getfLegVal().length() > 30)){
				errors.rejectValue("fLegVal", "AggregationDefinition.fLegVal.length.invalid", getMessage("AggregationDefinition.fLegVal.length.invalid"));
			}
			if(aggDefinition.getlLegVal() != null && (aggDefinition.getlLegVal().length() < 0 || aggDefinition.getlLegVal().length() > 30)){
				errors.rejectValue("lLegVal", "AggregationDefinition.lLegVal.length.invalid", getMessage("AggregationDefinition.lLegVal.length.invalid"));
			}
			isValidate(SystemParametersConstant.AGGREGATION_DEFINITION_FLEG, aggDefinition.getfLegVal(), "fLegVal", null, null, validateForImport);
			isValidate(SystemParametersConstant.AGGREGATION_DEFINITION_LLEG, aggDefinition.getlLegVal(), "lLegVal", null, null, validateForImport);
			
		}		
	}
	
	public void validateNameForUniqueness(AggregationDefinition aggDefinition, Errors errors) {
		boolean isUnique = aggregationDefinitionService.isUniqueDefinitionName(aggDefinition.getId(), aggDefinition.getAggDefName());
		if(!isUnique){
			errors.rejectValue("aggDefName", "aggregation.definition.name.already.exists", getMessage("aggregation.definition.name.already.exists"));;
		}
	}
}
