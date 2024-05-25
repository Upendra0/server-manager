package com.elitecore.sm.migration.validator;

import java.util.List;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.migration.model.MigrationTrackDetails;
import com.elitecore.sm.util.Regex;

@Component
public class MigrationValidator extends BaseValidator {
	
	private MigrationTrackDetails migrationTrackDetails;
	
	/**
	 * Method will check current validator class
	 *@param  Class<?> clazz
	 *@return boolean 
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return MigrationTrackDetails.class.isAssignableFrom(clazz);
	}
	
	public void validateMigrationTrackDetails(Object target, Errors errors, List<ImportValidationErrors> importErrorList, String moduleName,
			boolean validateForImport) {
		
		logger.debug("Going to validate migration track detail.");
		
		setErrorObject(errors, validateForImport, importErrorList);
		
		migrationTrackDetails = (MigrationTrackDetails) target;
		
		isValidate("Server.utilityPort", migrationTrackDetails.getServerInstancePort(), "serverInstancePort" ,
				String.valueOf(migrationTrackDetails.getServerInstancePort()), moduleName, validateForImport, migrationTrackDetails.getClass().getName());
		
		isValidate("ServerInstance.scriptName", migrationTrackDetails.getServerInstanceScriptName(), "serverInstanceScriptName" ,
				moduleName, migrationTrackDetails.getClass().getName(), validateForImport);
		
		if (match(Regex.get(SystemParametersConstant.SERVER_NAME, BaseConstants.REGEX_MAP_CACHE), migrationTrackDetails.getServerInstancePrefix())) {
			errors.rejectValue("serverInstancePrefix", "Server.utilityPort.invalid", getMessage("serverInstance.namingPrefix.invalid"));
		}
		
		if(migrationTrackDetails.getServer() == null || migrationTrackDetails.getServer().getId() <= 0){
			String errorMsgValue = getMessage("migration.server.invalid");
			setErrorFieldErrorMessage(String.valueOf(migrationTrackDetails.getServer().getId()) , "server.id", migrationTrackDetails.getServer().getName(), migrationTrackDetails.getClass().getName(), validateForImport, "migration.server", errorMsgValue);
		}
	}

	/**
	 * Validate Server details
	 * @param importErrorList
	 * @param ipAddress
	 * @param utilityport
	 */
	public void validateServerDetails(List<ImportValidationErrors> importErrorList, String ipAddress, int utilityport) {

		if (!validateIPAddress(ipAddress)) {

			ImportValidationErrors importErrors = new ImportValidationErrors(BaseConstants.MIGRATION, BaseConstants.MIGRATION, "serverIp", ipAddress,
					getMessage("Server.migration.ipAddress.invalid"));
			importErrorList.add(importErrors);
		}

		if (match(Regex.get(SystemParametersConstant.SERVER_UTILITYPORT, BaseConstants.REGEX_MAP_CACHE), String.valueOf(utilityport))) {

			ImportValidationErrors importErrors = new ImportValidationErrors(BaseConstants.MIGRATION, BaseConstants.MIGRATION, "utilityPort",
					String.valueOf(utilityport), getMessage("Server.utilityPort.invalid"));
			importErrorList.add(importErrors);

		}

	}

	/**
	 * Validate Server Instance details
	 * @param importErrorList
	 * @param prefix
	 * @param port
	 * @param fileName
	 * @param counter
	 */
	public void validateServerInstanceDetails(List<ImportValidationErrors> importErrorList, String prefix, int port, String fileName, int counter) {

		if (match(Regex.get(SystemParametersConstant.SERVER_NAME, BaseConstants.REGEX_MAP_CACHE), prefix)) {

			ImportValidationErrors importErrors = new ImportValidationErrors(BaseConstants.MIGRATION, BaseConstants.MIGRATION, counter
					+ "_namingPrefix", prefix, getMessage("serverInstance.namingPrefix.invalid"));
			importErrorList.add(importErrors);

		}

		if (match(Regex.get(SystemParametersConstant.SERVER_NAME, BaseConstants.REGEX_MAP_CACHE), fileName)) {

			ImportValidationErrors importErrors = new ImportValidationErrors(BaseConstants.MIGRATION, BaseConstants.MIGRATION, counter
					+ "_scriptName", fileName, getMessage("serverInstance.fileName.invalid"));
			importErrorList.add(importErrors);

		}

		if (match(Regex.get(SystemParametersConstant.SERVERINSTANCE_PORT, BaseConstants.REGEX_MAP_CACHE), String.valueOf(port))) {

			ImportValidationErrors importErrors = new ImportValidationErrors(BaseConstants.MIGRATION, BaseConstants.MIGRATION, counter + "_port",
					String.valueOf(port), getMessage("ServerInstance.port.invalid"));
			importErrorList.add(importErrors);
		}

	}

	/**
	 * Validate Ip Address
	 * 
	 * @param ipAddress
	 * @return boolean
	 */
	private boolean validateIPAddress(String ipAddress) {
		logger.debug("Inside validateIPAddress");
		InetAddressValidator ipValidator = new InetAddressValidator();
		boolean validateIPAddress = true;
		if (ipAddress != null) {
			logger.debug("Inside validateIPAddress if not null");
			if (!(ipValidator.isValidInet4Address(ipAddress) || ipValidator.isValidInet6Address(ipAddress))) {

				validateIPAddress = false;

			}
		} else if (StringUtils.isEmpty(ipAddress)) {
			logger.debug("Inside validateIPAddress if null");

			validateIPAddress = false;

		}
		return validateIPAddress;
	}

}
