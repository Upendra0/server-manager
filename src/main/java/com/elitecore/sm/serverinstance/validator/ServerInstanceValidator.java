/**
 * 
 */
package com.elitecore.sm.serverinstance.validator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.serverinstance.model.ServerInstance;

/**
 * @author Sunil Gulabani Jul 2, 2015
 */

@Component
public class ServerInstanceValidator extends BaseValidator {

	private ServerInstance serverInstance;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ServerInstance.class.isAssignableFrom(clazz);
	}

	/**
	 * Validate Server Instance Parameter
	 * 
	 * @param target
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateServerInstanceParameter(Object target, Errors errors, String entityName, boolean validateForImport) {
		this.errors = errors;
		serverInstance = (ServerInstance) target;
		boolean minMemoryValid = false;
		boolean maxMemoryValid = false;
		isValidate(SystemParametersConstant.SERVERINSTANCE_NAME, serverInstance.getName(), "name", entityName, serverInstance.getName(),
				validateForImport);
		isValidate(SystemParametersConstant.SERVERINSTANCE_DESCRIPTION, serverInstance.getDescription(), "description", entityName,
				serverInstance.getName(), validateForImport);
		isValidate(SystemParametersConstant.SERVERINSTANCE_PORT, String.valueOf(serverInstance.getPort()), "port", entityName,
				serverInstance.getName(), validateForImport);
		if (isValidate(SystemParametersConstant.SERVERINSTANCE_MAXMEMORYALLOCATION, serverInstance.getMaxMemoryAllocation(), "maxMemoryAllocation",
				serverInstance.getName(), entityName, validateForImport, serverInstance.getClass().getName())) {
			maxMemoryValid = true;
		}
		
		if (isValidate(SystemParametersConstant.SERVERINSTANCE_MINMEMORYALLOCATION,serverInstance.getMinMemoryAllocation(),"minMemoryAllocation",serverInstance.getName(), entityName,validateForImport,serverInstance.getClass().getName())) {
			minMemoryValid = true;
		}
		isValidate(SystemParametersConstant.SERVERINSTANCE_MAXCONNECTIONRETRY, serverInstance.getMaxConnectionRetry(), BaseConstants.MAXCONNECTIONRETRY ,
				serverInstance.getName(), entityName, validateForImport, serverInstance.getClass().getName());
		isValidate(SystemParametersConstant.SERVERINSTANCE_RETRYINTERVAL, serverInstance.getRetryInterval(), BaseConstants.RETRY_INTERVAL,
				serverInstance.getName(), entityName, validateForImport, serverInstance.getClass().getName());
		isValidate(SystemParametersConstant.SERVERINSTANCE_CONNECTIONTIMEOUT, serverInstance.getConnectionTimeout(), BaseConstants.CONNECTION_TIMEOUT ,
				serverInstance.getName(), entityName, validateForImport, serverInstance.getClass().getName());

		if (serverInstance.getScriptName() != null && !"".equals(serverInstance.getScriptName())) {
			if (serverInstance.getScriptName().toLowerCase().indexOf(BaseConstants.SCRIPT_FILE_SH_EXT) == -1
					&& serverInstance.getScriptName().toLowerCase().indexOf(BaseConstants.SCRIPT_FILE_BAT_EXT) == -1) {
				errors.rejectValue(BaseConstants.SCRIPT_NAME, BaseConstants.SCRIPT_NAME_INVALID , getMessage("ServerInstance.scriptName.ext.invalid"));
			} else {
				//this code block added to fix MED-4606
				isValidate(SystemParametersConstant.SERVERINSTANCE_SCRIPTNAME, serverInstance.getScriptName(), BaseConstants.SCRIPT_NAME, entityName,
						serverInstance.getName(), validateForImport);
			}		
		} else {
			errors.rejectValue(BaseConstants.SCRIPT_NAME, BaseConstants.SCRIPT_NAME_INVALID , getMessage("ServerInstance.scriptName.invalid"));
		}

		if (minMemoryValid && maxMemoryValid && serverInstance.getMaxMemoryAllocation() < serverInstance.getMinMemoryAllocation()) {
			errors.rejectValue("maxMemoryAllocation", "ServerInstance.maxMemoryAllocation.invalid", getMessage("ServerInstance.maxMemory.lesser"));
		}

	}

	/**
	 * validate Advance config tab parameter
	 * 
	 * @param serverInstance
	 * @param result
	 * @param entityName
	 * @param validateForImport
	 */
	public void getValidateAdvanceConfigTab(ServerInstance serverInstance, BindingResult result, String entityName, boolean validateForImport) {

		this.errors = result;
		boolean minMemoryValid = false;
		boolean maxMemoryValid = false;
		isValidate(SystemParametersConstant.SERVERINSTANCE_NAME, String.valueOf(serverInstance.getName()), "name", entityName,
				serverInstance.getName(), validateForImport);
		isValidate(SystemParametersConstant.SERVERINSTANCE_DESCRIPTION, String.valueOf(serverInstance.getDescription()), "description", entityName,
				serverInstance.getName(), validateForImport);
		isValidate(SystemParametersConstant.SERVERINSTANCE_MAXCONNECTIONRETRY, serverInstance.getMaxConnectionRetry(), BaseConstants.MAXCONNECTIONRETRY ,
				serverInstance.getName(), entityName, validateForImport, serverInstance.getClass().getName());
		isValidate(SystemParametersConstant.SERVERINSTANCE_RETRYINTERVAL, serverInstance.getRetryInterval(), BaseConstants.RETRY_INTERVAL,
				serverInstance.getName(), entityName, validateForImport, serverInstance.getClass().getName());
		isValidate(SystemParametersConstant.SERVERINSTANCE_CONNECTIONTIMEOUT, serverInstance.getConnectionTimeout(), BaseConstants.CONNECTION_TIMEOUT ,
				serverInstance.getName(), entityName, validateForImport, serverInstance.getClass().getName());
		if (isValidate(SystemParametersConstant.SERVERINSTANCE_MAXMEMORYALLOCATION, serverInstance.getMaxMemoryAllocation(), "maxMemoryAllocation",
				serverInstance.getName(), entityName, validateForImport, serverInstance.getClass().getName())) {
			maxMemoryValid = true;
		}
		if (isValidate(SystemParametersConstant.SERVERINSTANCE_MINMEMORYALLOCATION,serverInstance.getMinMemoryAllocation(),"minMemoryAllocation",serverInstance.getName(), entityName,validateForImport,serverInstance.getClass().getName())) {
			minMemoryValid = true;
		}		
		isValidate(SystemParametersConstant.SERVERINSTANCE_SCRIPTNAME, serverInstance.getScriptName(), BaseConstants.SCRIPT_NAME, entityName,
				serverInstance.getName(), validateForImport);
		isValidate(SystemParametersConstant.SERVERINSTANCE_MINDISKSPACE, serverInstance.getMinDiskSpace(), "minDiskSpace", entityName,
				serverInstance.getName(), validateForImport);
		isValidate(SystemParametersConstant.SERVERINSTANCE_MEDIATIONROOT, serverInstance.getMediationRoot(), "mediationRoot", entityName,
				serverInstance.getName(), validateForImport);
		isValidate(SystemParametersConstant.SERVERINSTANCE_REPROCESSING_BACKUP_PATH, serverInstance.getReprocessingBackupPath(), 
				"reprocessingBackupPath", entityName, serverInstance.getName(), validateForImport);

		if (serverInstance.getScriptName() != null && !"".equals(serverInstance.getScriptName())) {
			if (serverInstance.getScriptName().toLowerCase().indexOf(BaseConstants.SCRIPT_FILE_SH_EXT) == -1
					&& serverInstance.getScriptName().toLowerCase().indexOf(BaseConstants.SCRIPT_FILE_BAT_EXT) == -1) {
				errors.rejectValue(BaseConstants.SCRIPT_NAME, BaseConstants.SCRIPT_NAME_INVALID , getMessage("ServerInstance.scriptName.ext.invalid"));
			}
		} else {
			errors.rejectValue(BaseConstants.SCRIPT_NAME, BaseConstants.SCRIPT_NAME_INVALID , getMessage("ServerInstance.scriptName.invalid"));
		}
		if (Integer.parseInt(serverInstance.getMinDiskSpace()) < -1 || Integer.parseInt(serverInstance.getMinDiskSpace()) == 0) {
			errors.rejectValue("minDiskSpace", "ServerInstance.minDiskSpace.invalid", getMessage("ServerInstance.minDiskSpace.invalid"));
		}
		if (serverInstance.getMaxConnectionRetry() <= 0) {
			errors.rejectValue(BaseConstants.MAXCONNECTIONRETRY , "ServerInstance.maxConnectionRetry.invalid",
					getMessage("ServerInstance.maxConnectionRetry.invalid"));
		}
		if (serverInstance.getRetryInterval() <= 0) {
			errors.rejectValue(BaseConstants.RETRY_INTERVAL, "ServerInstance.retryInterval.invalid", getMessage("ServerInstance.retryInterval.invalid"));
		}
		if (serverInstance.getConnectionTimeout() <= 0) {
			errors.rejectValue(BaseConstants.CONNECTION_TIMEOUT , "ServerInstance.connectionTimeout.invalid",
					getMessage("ServerInstance.connectionTimeout.invalid"));
		}
		if (serverInstance.getServer().getServerType().getId()==3 && serverInstance.isDatabaseInit()
				&& serverInstance.getServerManagerDatasourceConfig().getId() == serverInstance.getIploggerDatasourceConfig().getId()) {
			errors.rejectValue("serverManagerDatasourceConfig.name", "ServerInstance.serverManagerDatasourceConfig.name.invalid",
					getMessage("ServerInstance.datasourceconfig.name.invalid"));
			errors.rejectValue("iploggerDatasourceConfig.name", "ServerInstance.iploggerDatasourceConfig.name.invalid",
					getMessage("ServerInstance.datasourceconfig.name.invalid"));
		}
		if (minMemoryValid && maxMemoryValid && serverInstance.getMaxMemoryAllocation() < serverInstance.getMinMemoryAllocation()) {
			errors.rejectValue("maxMemoryAllocation", "ServerInstance.maxMemoryAllocation.invalid", new Object[] {serverInstance.getMinMemoryAllocation()}, getMessage("ServerInstance.maxMemory.lesser"));
		}
	}

	/**
	 * validate System log management tab parameter
	 * 
	 * @param instance
	 * @param result
	 * @param entityName
	 * @param validateForImport
	 */
	public void getValidateSystemLogTab(ServerInstance instance, BindingResult result, String entityName, boolean validateForImport) {

		this.errors = result;

		String fullClassName = instance.getClass().getName();
		String instanceName = instance.getName();

		isValidate(SystemParametersConstant.SERVERINSTANCE_LOGSDETAIL_MAXROLLINGUNIT, instance.getLogsDetail().getMaxRollingUnit(),
				"logsDetail.maxRollingUnit", instanceName, entityName, validateForImport, fullClassName);


		if (instance.isThresholdSysAlertEnable()) {

			
			isValidate(SystemParametersConstant.SERVERINSTANCE_THRESHOLDMEMORY, instance.getThresholdMemory(), "thresholdMemory", instanceName,
					entityName, validateForImport, fullClassName);
			isValidate(SystemParametersConstant.SERVERINSTANCE_THRESHOLDTIMEINTERVAL, instance.getThresholdTimeInterval(), "thresholdTimeInterval",
					instanceName, entityName, validateForImport, fullClassName);
			isValidate(SystemParametersConstant.SERVERINSTANCE_LOADAVERAGE, instance.getLoadAverage(), "loadAverage", instanceName, entityName,
					validateForImport, fullClassName);
		}


			isValidate(SystemParametersConstant.SERVERINSTANCE_LOGSDETAIL_ROLLINGVALUE, instance.getLogsDetail().getRollingValue(),
					"logsDetail.rollingValue", instanceName, entityName, validateForImport, instance.getLogsDetail().getRollingType());
	}

	/**
	 * validate statistic management tab parameter
	 * 
	 * @param instance
	 * @param result
	 * @param entityName
	 * @param validateForImport
	 */
	public void getValidateStatisticTab(ServerInstance instance, BindingResult result, String entityName, boolean validateForImport) {
		this.errors = result;
		if (instance.isFileStatInDBEnable()) {
			isValidate(SystemParametersConstant.SERVERINSTANCE_FILESTORAGELOCATION, instance.getFileStorageLocation(), "fileStorageLocation",
					entityName, instance.getName(), validateForImport);
		}
	}

	/**
	 * validate server instance parameter for import
	 * 
	 * @param instance
	 * @return
	 */
	public List<ImportValidationErrors> validateServerInstanceForImport(ServerInstance instance) {
		this.importErrorList = new ArrayList<ImportValidationErrors>();

		logger.debug("Validate Field for Import Operation for Entity: " + BaseConstants.SERVERINSTANCE);

		isValidate(SystemParametersConstant.SERVERINSTANCE_THRESHOLDMEMORY, String.valueOf(instance.getThresholdMemory()), "thresholdMemory",
				BaseConstants.SERVERINSTANCE, instance.getName(), true);
		isValidate(SystemParametersConstant.SERVERINSTANCE_THRESHOLDTIMEINTERVAL, String.valueOf(instance.getThresholdTimeInterval()),
				"thresholdTimeInterval", BaseConstants.SERVERINSTANCE, instance.getName(), true);
		isValidate(SystemParametersConstant.SERVERINSTANCE_LOADAVERAGE, String.valueOf(instance.getLoadAverage()), "loadAverage",
				BaseConstants.SERVERINSTANCE, instance.getName(), true);

		return importErrorList;

	}
}