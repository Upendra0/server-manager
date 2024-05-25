package com.elitecore.sm.services.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.services.model.PartitionFieldEnum;
import com.elitecore.sm.services.model.PartitionParam;

@Component
public class PartitionParamValidator extends BaseValidator {
	private PartitionParam param;
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return PartitionParam.class.isAssignableFrom(clazz);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	
	public void validatePartitionParam(Object target, Errors errors,String entityName,boolean validateForImport,List<ImportValidationErrors> importErrorList) {
		
		if(validateForImport){
			this.importErrorList=importErrorList;
		}else{
			this.errors = errors;	
		}
		param = (PartitionParam) target;
		
		isValidate(SystemParametersConstant.PARTITIONPARAM_PARTITIONRANGE,param.getPartitionRange(),"partitionRange",entityName,param.getPartitionRange(),validateForImport);
		
		if(isValidate(SystemParametersConstant.PARTITIONPARAM_PARTITIONRANGE,param.getPartitionRange(),"partitionRange",entityName,param.getPartitionRange(),validateForImport)){
			
			
			if(param.getPartitionField() != PartitionFieldEnum.Date && (Integer.parseInt(param.getPartitionRange())< 1 || Integer.parseInt(param.getPartitionRange())>100)){
				if(validateForImport){
					ImportValidationErrors error=new ImportValidationErrors(BaseConstants.IPLOG_SERVICE_HASH_CONFIGURATION,param.getPartitionRange(),"Iplog.HashBasedConf.partitionRange", param.getPartitionRange(), getMessage(SystemParametersConstant.IPLOG_HASHBASEDCONF_PARTITIONRANGE+".invalid"));
		    		importErrorList.add(error);
				}else{
					errors.rejectValue("partitionRange", "Iplog.HashBasedConf.partitionRange.invalid", getMessage("Iplog.HashBasedConf.partitionRange.invalid"));	
				}
			}
		}
		
		isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,param.getUnifiedField(),"unifiedField",entityName,param.getUnifiedField(),validateForImport);
		
		
		if(param.getBaseUnifiedField() != null &&  !"NA".equals(param.getBaseUnifiedField()) && !"".equals(param.getBaseUnifiedField())){
			isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,param.getBaseUnifiedField(),"baseUnifiedField",entityName,param.getBaseUnifiedField(),validateForImport);
			String paramClassName = param.getClass().getName();
			isValidate(SystemParametersConstant.PARTITIONPARAM_NETMASK,param.getNetMask(),"netMask","PartitionParam", entityName,validateForImport,paramClassName);
		}
		
		
	}
}