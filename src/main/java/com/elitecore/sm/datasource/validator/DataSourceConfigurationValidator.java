package com.elitecore.sm.datasource.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.datasource.model.DataSourceConfig;

@Component
public class DataSourceConfigurationValidator extends BaseValidator{
	
	private DataSourceConfig datasourceconfig;
	
	@Override
	public boolean supports(Class<?> clazz) {

		return DataSourceConfig.class.isAssignableFrom(clazz);
	}

	
	public void validateDataSourceConfigurationParameters(Object target, Errors errors,List<ImportValidationErrors> importErrorList,String entityName,boolean validateForImport)
	{
		
		if(validateForImport){
			this.importErrorList=importErrorList;
		}else{
			this.errors = errors;			
		}
		datasourceconfig= (DataSourceConfig) target;
		
		validateDataSourceConfiguration(datasourceconfig, importErrorList, entityName, validateForImport);
		
	}
	
	/**
	 * @param datasourceconfig
	 */
	private void validateDataSourceConfiguration(DataSourceConfig datasourceconfig,List<ImportValidationErrors> importErrorList,String entityName,boolean validateForImport){
		
		if(validateForImport){
			this.importErrorList=importErrorList;
		}
		
		String datasourceName= datasourceconfig.getName();
		String datasourceConnectionUrl=datasourceconfig.getConnURL();
		String datasourceUserName=datasourceconfig.getUsername();
		String datasourcePassword=datasourceconfig.getPassword();
		
		String fullClassName = datasourceconfig.getClass().getName();
		
		isValidate(SystemParametersConstant.DATASOURCECONFIG_NAME_REGEX,datasourceName,"name",entityName,datasourceName,validateForImport);
		isValidate(SystemParametersConstant.DATASOURCECONFIG_CONNURL_REGEX,datasourceConnectionUrl,"connURL",entityName,datasourceName,validateForImport);
		isValidate(SystemParametersConstant.DATASOURCECONFIG_USERNAME_REGEX,datasourceUserName,"username",entityName,datasourceName,validateForImport);
		isValidate(SystemParametersConstant.DATASOURCECONFIG_PASSWORD_REGEX,datasourcePassword,"password",entityName,datasourceName,validateForImport);
		boolean minPoolValid = isValidate(SystemParametersConstant.DATASOURCECONFIG_MINPOOLSIZE_REGEX,datasourceconfig.getMinPoolSize(),"minPoolSize",datasourceconfig.getName(), entityName,validateForImport,fullClassName);
		boolean maxPoolValid = isValidate(SystemParametersConstant.DATASOURCECONFIG_MAXPOOLSIZE_REGEX,datasourceconfig.getMaxPoolsize(),"maxPoolsize",datasourceconfig.getName(), entityName,validateForImport,fullClassName);
		if(datasourceconfig.getFailTimeout() != null && datasourceconfig.getFailTimeout().trim().length() > 0){
			isValidate(SystemParametersConstant.DATASOURCECONFIG_FAIL_TIMEOUT,Integer.parseInt(datasourceconfig.getFailTimeout()),"failTimeout",datasourceconfig.getName(), entityName,validateForImport,fullClassName);	
		}
		
		if (minPoolValid && maxPoolValid && (datasourceconfig.getMinPoolSize() > datasourceconfig.getMaxPoolsize() )){
			setErrorFieldErrorMessage(String.valueOf(datasourceconfig.getMaxPoolsize()), "maxPoolsize", datasourceconfig.getName(), entityName, validateForImport, "fullClassName", getMessage("DataSourceConfig.maxPoolsize.islesser.invalid"));		
		}	
	}
	
	/**
	 * @param target
	 * @param importErrorList
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateDataSourceConfigForMigration(Object target,List<ImportValidationErrors> importErrorList,String entityName, boolean validateForImport){
		this.importErrorList=importErrorList;
		datasourceconfig= (DataSourceConfig) target;
		validateDataSourceConfiguration(datasourceconfig, importErrorList, entityName, validateForImport);
	}
		
}
