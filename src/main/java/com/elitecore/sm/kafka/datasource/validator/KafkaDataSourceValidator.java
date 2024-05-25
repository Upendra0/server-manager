package com.elitecore.sm.kafka.datasource.validator;

import java.util.List;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.kafka.datasource.model.KafkaDataSourceConfig;

@Component
public class KafkaDataSourceValidator extends BaseValidator {

	private KafkaDataSourceConfig kafkaDatasourceconfig;
	
	@Override
	public boolean supports(Class<?> clazz) {

		return KafkaDataSourceConfig.class.isAssignableFrom(clazz);
	}
	
	public void validateKafkaDataSourceConfigParameters(Object target, Errors errors,List<ImportValidationErrors> importErrorList,String entityName,boolean validateForImport) {
		
		if(validateForImport){
			this.importErrorList=importErrorList;
		}else{
			this.errors = errors;			
		}
		kafkaDatasourceconfig= (KafkaDataSourceConfig) target;
		
		validateKafkaDataSourceConfiguration(kafkaDatasourceconfig, importErrorList, entityName, validateForImport);
		
	}
	
	/**
	 * @param kafkaDataSourceConfig
	 */
	private void validateKafkaDataSourceConfiguration(KafkaDataSourceConfig kafkaDataSourceConfig,List<ImportValidationErrors> importErrorList,String entityName,boolean validateForImport){
		
		if(validateForImport){
			this.importErrorList=importErrorList;
		}
		
		String kafkaDataSourceName= kafkaDataSourceConfig.getName();
		String classFullName = kafkaDataSourceConfig.getClass().getName();
		
		isValidate(SystemParametersConstant.KAFKADATASOURCECONFIG_NAME_REGEX,kafkaDataSourceName,"name",entityName,kafkaDataSourceName,validateForImport);
		validateIPAddress(kafkaDataSourceConfig.getKafkaServerIpAddress(),validateForImport,kafkaDataSourceConfig.getName(),"kafkaServerIpAddress",true);
		
		isValidate(SystemParametersConstant.KAFKADATASOURCECONFIG_KAFKASERVERPORT_REGEX, kafkaDataSourceConfig.getKafkaServerPort(), "kafkaServerPort",  kafkaDataSourceName, entityName, validateForImport,classFullName);
		isValidate(SystemParametersConstant.KAFKADATASOURCECONFIG_MAXRETRYCOUNT_REGEX, kafkaDataSourceConfig.getMaxRetryCount(), "maxRetryCount", kafkaDataSourceName, entityName, validateForImport,classFullName);
		isValidate(SystemParametersConstant.KAFKADATASOURCECONFIG_MAXRESPONSEWAIT_REGEX, kafkaDataSourceConfig.getMaxResponseWait(), "maxResponseWait", kafkaDataSourceName, entityName, validateForImport,classFullName);
		isValidate(SystemParametersConstant.KAFKADATASOURCECONFIG_KAFKAPRODUCERRETRYCOUNT_REGEX, kafkaDataSourceConfig.getKafkaProducerRetryCount(), "kafkaProducerRetryCount", kafkaDataSourceName, entityName, validateForImport,classFullName);
		isValidate(SystemParametersConstant.KAFKADATASOURCECONFIG_KAFKAPRODUCERREQUESTTIMEOUT_REGEX, kafkaDataSourceConfig.getKafkaProducerRequestTimeout(), "kafkaProducerRequestTimeout", kafkaDataSourceName, entityName, validateForImport,classFullName);
		isValidate(SystemParametersConstant.KAFKADATASOURCECONFIG_KAFKAPRODUCERRETRYBACKOFF_REGEX, kafkaDataSourceConfig.getKafkaProducerRetryBackoff(), "kafkaProducerRetryBackoff", kafkaDataSourceName, entityName, validateForImport,classFullName);
		isValidate(SystemParametersConstant.KAFKADATASOURCECONFIG_KAFKAPRODUCERDELIVERYTIMEOUT_REGEX, kafkaDataSourceConfig.getKafkaProducerDeliveryTimeout(), "kafkaProducerDeliveryTimeout", kafkaDataSourceName, entityName, validateForImport,classFullName);
		
	}
	
	/**
	 * Validate Ip address
	 * 
	 * @param ipAddress
	 */
	private void validateIPAddress(String ipAddress,boolean validateForImport,String kafkaDataSourceName,String prop,boolean checkIPV6) {
		InetAddressValidator ipValidator = new InetAddressValidator();
		String errorMsgValue;
		if (ipAddress != null && !(ipValidator.isValidInet4Address(ipAddress) || ipValidator.isValidInet6Address(ipAddress))) {
			errorMsgValue = getMessage(BaseConstants.KAFKA_SERVER_IP_INVALID);
			if (validateForImport) {
				ImportValidationErrors errors = new ImportValidationErrors(BaseConstants.KAFKADATASOURCECONFIG, kafkaDataSourceName, prop, ipAddress, getMessage(BaseConstants.KAFKA_SERVER_IP_INVALID));
				importErrorList.add(errors);
			} else {
				errors.rejectValue(prop, "error.KafkaDataSourceConfig.kafkaServerIpAddress.invalid", errorMsgValue);
			}
		}
	}
}
