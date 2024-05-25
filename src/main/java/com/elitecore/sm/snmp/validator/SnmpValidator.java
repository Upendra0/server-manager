package com.elitecore.sm.snmp.validator;

import java.util.List;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import com.elitecore.core.commons.alert.AuthAlgorithm;
import com.elitecore.core.commons.alert.PrivacyAlgorithm;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.snmp.model.SNMPAlert;
import com.elitecore.sm.snmp.model.SNMPServerConfig;
import com.elitecore.sm.snmp.model.SNMPServiceThreshold;
import com.elitecore.sm.snmp.model.SNMPVersionType;

/**
 * Validation class for snmp
 * 
 * @author jui.purohit
 *
 */
@Component
public class SnmpValidator extends BaseValidator {

	SNMPServerConfig snmpServerConfig;
	SNMPServiceThreshold snmpServiceThreshold;

	@Override
	public boolean supports(Class<?> clazz) {

		return SNMPServerConfig.class.equals(clazz) || SNMPAlert.class.equals(clazz);
	}

	/**
	 * Validate Basic Parameter of SnmpServerList
	 * 
	 * @param target
	 * @param errors
	 * @param moduleName
	 * @param validateForImport
	 */
	public void validateSnmpServerConfigParam(Object target, Errors errors, List<ImportValidationErrors> importErrorList, String moduleName,
			boolean validateForImport) {

		if (validateForImport) {
			this.importErrorList = importErrorList;
		} else {
			this.errors = errors;
		}

		snmpServerConfig = (SNMPServerConfig) target;

		isValidate(SystemParametersConstant.SNMPSERVERLIST_NAME, snmpServerConfig.getName(), "name", moduleName, snmpServerConfig.getName(),
				validateForImport);
		
		if(snmpServerConfig.getCommunity() == null || StringUtils.isEmpty(snmpServerConfig.getCommunity().trim()))
			errors.rejectValue("community", BaseConstants.SNMP_COMMUNITY_REQUIRED, getMessage(BaseConstants.SNMP_COMMUNITY_REQUIRED));
		
		isValidate(SystemParametersConstant.SNMPSERVERLIST_PORT, snmpServerConfig.getPort(), "port", moduleName, snmpServerConfig.getName(),
				validateForImport);

		validatePortAndOffSet(snmpServerConfig, errors, importErrorList, moduleName, validateForImport);
		
	//		validateIPAddress(snmpServerConfig.getHostIP(), validateForImport, snmpServerConfig.getName());
		

	}

	/**
	 * Validate client parameters
	 * 
	 * @param target
	 * @param errors
	 * @param importErrorList
	 * @param moduleName
	 * @param validateForImport
	 */
	public void validateSnmpClientConfigParam(Object target, Errors errors, List<ImportValidationErrors> importErrorList, String moduleName,
			boolean validateForImport) {

		if (validateForImport) {
			this.importErrorList = importErrorList;
		} else {
			this.errors = errors;
		}

		snmpServerConfig = (SNMPServerConfig) target;

		isValidate(SystemParametersConstant.SNMPSERVERLIST_NAME, snmpServerConfig.getName(), "name", moduleName, snmpServerConfig.getName(),
				validateForImport);

		if(snmpServerConfig.getCommunity() == null || StringUtils.isEmpty(snmpServerConfig.getCommunity().trim()))
			errors.rejectValue("community", BaseConstants.SNMP_COMMUNITY_REQUIRED, getMessage(BaseConstants.SNMP_COMMUNITY_REQUIRED));
		
		if (StringUtils.isEmpty(snmpServerConfig.getPort())) {
			if (validateForImport) {

				ImportValidationErrors importErrors = new ImportValidationErrors(BaseConstants.SNMP_CONFIG, snmpServerConfig.getName(), "port",
						snmpServerConfig.getPort(), getMessage(BaseConstants.CLIENT_PORT_REQUIRED));

				importErrorList.add(importErrors);
			} else {
				errors.rejectValue("port", BaseConstants.CLIENT_PORT_REQUIRED, getMessage(BaseConstants.CLIENT_PORT_REQUIRED));
			}
		} else {

			isValidate(SystemParametersConstant.SNMPSERVERLIST_PORT, snmpServerConfig.getPort(), "port", moduleName, snmpServerConfig.getName(),
					validateForImport);
		}
		validateIPAddress(snmpServerConfig.getHostIP(), validateForImport, snmpServerConfig.getName());
		
		if(snmpServerConfig.getVersion() == SNMPVersionType.V3){
			if(!snmpServerConfig.getSnmpV3AuthAlgorithm().equalsIgnoreCase(AuthAlgorithm.NOAUTH.getValue())){
				boolean validAuthpass = isValidate(SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_PASSWORD,snmpServerConfig.getSnmpV3AuthPassword(),"snmpV3AuthPassword",moduleName, snmpServerConfig.getName(),validateForImport);
				if(snmpServerConfig.getSnmpV3AuthPassword().length() < 8 || snmpServerConfig.getSnmpV3AuthPassword().length() > 20 || !validAuthpass){
					setErrorFieldErrorMessage(snmpServerConfig.getSnmpV3AuthPassword() , "snmpV3AuthPassword", snmpServerConfig.getName(), moduleName, validateForImport,  "snmpV3AuthPassword.invalid", getMessage("snmpV3AuthPassword.invalid"));
				}
				if(!snmpServerConfig.getSnmpV3PrivAlgorithm().equalsIgnoreCase(PrivacyAlgorithm.NOPRIV.getValue())){
					boolean validPrivpass = isValidate(SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_PASSWORD,snmpServerConfig.getSnmpV3PrivPassword(),"snmpV3PrivPassword",moduleName, snmpServerConfig.getName(),validateForImport);
					if(snmpServerConfig.getSnmpV3PrivPassword().length() < 8 || snmpServerConfig.getSnmpV3PrivPassword().length() > 20 || !validPrivpass){
						setErrorFieldErrorMessage(snmpServerConfig.getSnmpV3PrivPassword() , "snmpV3PrivPassword", snmpServerConfig.getName(), moduleName, validateForImport,  "snmpV3PrivPassword.invalid", getMessage("snmpV3PrivPassword.invalid"));
					}
				}
			}
		}
	}

	/**
	 * Validate port and offset value
	 * 
	 * @param snmpServerConfig
	 * @param errors
	 * @param importErrorList
	 * @param moduleName
	 * @param validateForImport
	 */
	private void validatePortAndOffSet(SNMPServerConfig snmpServerConfig, Errors errors, List<ImportValidationErrors> importErrorList,
			String moduleName, boolean validateForImport) {
		
		if ((StringUtils.isEmpty(snmpServerConfig.getPort()) || "0".equalsIgnoreCase(snmpServerConfig.getPort()) || Integer.parseInt(snmpServerConfig.getPort()) ==0) && snmpServerConfig.getPortOffset() == 0 ) {
			logger.debug("Inside if:" + snmpServerConfig.getPort() + snmpServerConfig.getPortOffset());
			if (validateForImport) {

				ImportValidationErrors importErrors = new ImportValidationErrors(BaseConstants.SNMP_CONFIG, snmpServerConfig.getName(), "port",
						snmpServerConfig.getPort(), getMessage(BaseConstants.PORT_OR_OFFSET_REQUIRED));
				ImportValidationErrors importErrorsportOffset = new ImportValidationErrors(BaseConstants.SNMP_CONFIG, snmpServerConfig.getName(),
						"portOffset", String.valueOf(snmpServerConfig.getPortOffset()), getMessage(BaseConstants.PORT_OR_OFFSET_REQUIRED));

				importErrorList.add(importErrors);
				importErrorList.add(importErrorsportOffset);
			} else {
				errors.rejectValue("port", BaseConstants.PORT_OR_OFFSET_REQUIRED, getMessage(BaseConstants.PORT_OR_OFFSET_REQUIRED));
				errors.rejectValue(BaseConstants.PORTOFFSET, BaseConstants.PORT_OR_OFFSET_REQUIRED, getMessage(BaseConstants.PORT_OR_OFFSET_REQUIRED));
			}

		} else {
			logger.debug("Inside else:" + snmpServerConfig.getPort() + snmpServerConfig.getPortOffset());
			if (!StringUtils.isEmpty(snmpServerConfig.getPort()) ) {
				if(validateForImport){
					if( !("0".equalsIgnoreCase(snmpServerConfig.getPort())) && Integer.parseInt(snmpServerConfig.getPort())!=0){
					isValidate(SystemParametersConstant.SNMPSERVERLIST_PORT, snmpServerConfig.getPort(), "port", moduleName, snmpServerConfig.getName(),
							validateForImport);
					}
				}
				else{
					isValidate(SystemParametersConstant.SNMPSERVERLIST_PORT, snmpServerConfig.getPort(), "port", moduleName, snmpServerConfig.getName(),
							validateForImport);
				}
			}
			if (snmpServerConfig.getPortOffset() != 0) {		
			
				isValidate(SystemParametersConstant.SNMPSERVERLIST_OFFSET, snmpServerConfig.getPortOffset(),
						BaseConstants.PORTOFFSET, snmpServerConfig.getName(), moduleName, validateForImport, snmpServerConfig.getClass().getName());
			
			
			}
		}
	}

	/**
	 * Method for validation of Ip Address validate host IP address
	 * 
	 * @param ipAddress
	 * @param validateForImport
	 */
	private void validateIPAddress(String ipAddress, boolean validateForImport, String snmpName) {
		logger.debug("Inside validateIPAddress");
		InetAddressValidator ipValidator = new InetAddressValidator();
		String errorMsgValue;
		if (ipAddress != null) {
			logger.debug("Inside validateIPAddress if not null");
			if (!(ipValidator.isValidInet4Address(ipAddress) || ipValidator.isValidInet6Address(ipAddress))) {
				errorMsgValue = getMessage(SystemParametersConstant.SNMPSERVERLIST_IP + ".invalid");
				if (validateForImport) {
					ImportValidationErrors errors = new ImportValidationErrors(BaseConstants.SNMP_CONFIG, snmpName,
							SystemParametersConstant.SNMPSERVERLIST_IP, ipAddress, errorMsgValue);
					importErrorList.add(errors);
				} else {
					errors.rejectValue("hostIP", "error.Server.ipAddress.invalid", errorMsgValue);
				}
			}
		} else if (StringUtils.isEmpty(ipAddress)) {
			logger.debug("Inside validateIPAddress if null");
			errorMsgValue = getMessage(SystemParametersConstant.SNMPSERVERLIST_IP + ".invalid");
			if (validateForImport) {
				ImportValidationErrors errors = new ImportValidationErrors(BaseConstants.SNMP_CONFIG, snmpName,
						SystemParametersConstant.SNMPSERVERLIST_IP, ipAddress, errorMsgValue);
				importErrorList.add(errors);
			} else {
				errors.rejectValue("hostIP", "error.Server.ipAddress.invalid", errorMsgValue);
			}
		}
	}

	/**
	 * Validate service threshold for alert
	 * 
	 * @param importErrorList
	 * @param alertsvcList
	 * @param selectedAlertType
	 * @param configAlertThreshold
	 */
	public void validateServiceThresholdParam(List<ImportValidationErrors> importErrorList, String alertsvcList, String selectedAlertType) {
		JSONArray jsvcListArr = new JSONArray(alertsvcList);
		for (int index = 0; index < jsvcListArr.length(); index++) {
			JSONObject jsvcObj = jsvcListArr.getJSONObject(index);
			double svcThreshold = jsvcObj.getDouble("threshold");
			if (svcThreshold < 1 || svcThreshold > 10000) {
				ImportValidationErrors importErrors = new ImportValidationErrors(BaseConstants.SNMP_CONFIG, BaseConstants.SNMP_CONFIG, jsvcObj.getString("servInstanceId")+"_"+jsvcObj.getString("svcName")
						+ "_threshold", String.valueOf(svcThreshold), getMessage("SNMPserviceThreshold.value.fail"));
				importErrorList.add(importErrors);
			}
		}
	}

	/**
	 * Validate Alert details
	 * 
	 * @param target
	 * @param errors
	 * @param importErrorList
	 * @param moduleName
	 * @param validateForImport
	 */
	public void validateAlertDetailsParam(Object target, Errors errors, List<ImportValidationErrors> importErrorList, String moduleName,
			boolean validateForImport) {

		SNMPAlert alert = (SNMPAlert) target;
		if (validateForImport) {
			this.importErrorList = importErrorList;
		} else {
			this.errors = errors;
		}

		isValidate(SystemParametersConstant.SNMPALERT_DESCRIPTION, alert.getDesc(), "desc", moduleName, alert.getName(), validateForImport);

	}

	public void validateSnmpClientConfigParamForImport(Object target, List<ImportValidationErrors> importErrorList, String moduleName,
			boolean validateForImport) {
		validateSnmpClientConfigParam(target, null, importErrorList, moduleName, validateForImport);
	}
}
