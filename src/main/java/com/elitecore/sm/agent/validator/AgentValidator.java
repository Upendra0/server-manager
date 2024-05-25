package com.elitecore.sm.agent.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import com.elitecore.sm.agent.model.Agent;
import com.elitecore.sm.agent.model.FileRenameAgent;
import com.elitecore.sm.agent.model.PacketStatisticsAgent;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;

@Component
public class AgentValidator extends BaseValidator {

	PacketStatisticsAgent packetStatAgent;

	FileRenameAgent fileRenameAgent;

	@Override
	public boolean supports(Class<?> clazz) {

		return Agent.class.isAssignableFrom(clazz);
	}

	/**
	 * Validate Agent Parameter
	 * 
	 * @param target
	 * @param errors
	 * @param importErrorList
	 * @param moduleName
	 * @param validateForImport
	 */
	public void validateAgentParam(Object target, Errors errors,
			List<ImportValidationErrors> importErrorList, String moduleName,
			boolean validateForImport) {

		if (validateForImport) {
			this.importErrorList = importErrorList;
		} else {
			this.errors = errors;
		}

		if (target instanceof PacketStatisticsAgent) {
			packetStatAgent = (PacketStatisticsAgent) target;

			if (validateForImport) {
				if (!StringUtils.isEmpty(packetStatAgent.getStorageLocation())) {
					isValidate(
							SystemParametersConstant.PACKETSTATASTICAGENT_FILESTORAGEPATH,
							packetStatAgent.getStorageLocation(),
							"storageLocation", moduleName, packetStatAgent
									.getAgentType().getAlias(),
							validateForImport);
				}
			} else {
				isValidate(
						SystemParametersConstant.PACKETSTATASTICAGENT_FILESTORAGEPATH,
						packetStatAgent.getStorageLocation(),
						"storageLocation", moduleName,
						packetStatAgent.getStorageLocation(), validateForImport);
			}

			isValidate(
					SystemParametersConstant.PACKETSTATASTICAGENT_EXECUTIONINTERVAL,
					packetStatAgent.getExecutionInterval(),
					BaseConstants.EXECUTION_INTERVAL, BaseConstants.AGENT, moduleName,
					validateForImport, packetStatAgent.getClass().getName());

		}else if (target instanceof FileRenameAgent) {
			
			logger.info("Validating File rename agent details ::");
			
			fileRenameAgent = (FileRenameAgent) target;

				isValidate(SystemParametersConstant.FILE_RENAME_AGENT_INITIAL_DELAY,fileRenameAgent.getInitialDelay(), "initialDelay", BaseConstants.AGENT,
					moduleName, validateForImport, fileRenameAgent.getClass().getName());

				isValidate(SystemParametersConstant.PACKETSTATASTICAGENT_EXECUTIONINTERVAL,fileRenameAgent.getExecutionInterval(),BaseConstants.EXECUTION_INTERVAL, BaseConstants.AGENT, 
						moduleName,  validateForImport, fileRenameAgent.getClass().getName());
		}
	}

	/**
	 * Validate File Rename Agent Parameter
	 * 
	 * @param target
	 * @param errors
	 * @param importErrorList
	 * @param moduleName
	 * @param validateForImport
	 */
	public void validateFileRenameAgentParam(Object target, Errors errors,List<ImportValidationErrors> importErrorList, String moduleName,
			boolean validateForImport) {
		
		if (validateForImport) {
			this.importErrorList = importErrorList;
		} else {
			this.errors = errors;
		}
		
		if (target instanceof FileRenameAgent) {
			
			logger.info("Validating File rename agent details ::");
			
			fileRenameAgent = (FileRenameAgent) target;

				isValidate(SystemParametersConstant.FILE_RENAME_AGENT_INITIAL_DELAY,fileRenameAgent.getInitialDelay(), "initialDelay", BaseConstants.AGENT,
					moduleName, validateForImport, fileRenameAgent.getClass().getName());

				isValidate(SystemParametersConstant.PACKETSTATASTICAGENT_EXECUTIONINTERVAL,fileRenameAgent.getExecutionInterval(),BaseConstants.EXECUTION_INTERVAL, BaseConstants.AGENT, 
						moduleName,  validateForImport, fileRenameAgent.getClass().getName());
		}
	}
}
