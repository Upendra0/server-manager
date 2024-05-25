/**
 * 
 */
package com.elitecore.sm.server.validator;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.serverinstance.model.ServerInstance;

/**
 * @author Sunil Gulabani
 * Jul 2, 2015
 */
@Component
public class ServerValidator extends BaseValidator  {
	
	private Server server ;

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		boolean returnFlag = false;
		if(Server.class.isAssignableFrom(clazz) || ServerInstance.class.isAssignableFrom(clazz)){
			returnFlag = true;
		}
		return returnFlag;
	}

	/* (non-Javadoc)
	 * @see org.springframework.validation.Validator#validate(java.lang.Object, org.springframework.validation.Errors)
	 */

	public void validateServerParameters(Object target, Errors errors,String entityName,boolean validateForImport) {
		this.errors = errors;
		server = (Server) target;
		
	//	validateIPAddress(server.getIpAddress());
		isValidate(SystemParametersConstant.SERVER_NAME,server.getName(),"name",entityName,server.getName(),validateForImport);
		isValidate(SystemParametersConstant.SERVER_SERVERID,server.getServerId(),"serverId",entityName,server.getName(),validateForImport);
		isValidate(SystemParametersConstant.SERVER_GROUPSERVERID,server.getGroupServerId(),"groupServerId",entityName,server.getGroupServerId(),validateForImport);
		isValidate(SystemParametersConstant.SERVER_DESCRIPTION, server.getDescription(),"description",entityName,server.getName(),validateForImport);
		isValidate(SystemParametersConstant.SERVER_UTILITYPORT,String.valueOf(server.getUtilityPort()),"utilityPort",new Object[]{String.valueOf(BaseConstants.UTILITY_PORT_MIN_LIMIT),String.valueOf(BaseConstants.UTILITY_PORT_MAX_LIMIT)},entityName,server.getName(),validateForImport);
		if("".equals(server.getIpAddress()) || server.getIpAddress() == null){
			errors.rejectValue(BaseConstants.IPADDRESS, "error.Server.ipAddress.invalid", getMessage("Server.ipAddress.invalid"));
		}
	}

	private void validateIPAddress(String ipAddress) {
		InetAddressValidator ipValidator = new InetAddressValidator();
		String errorMsgValue ;
		if( ipAddress!=null && !(ipValidator.isValidInet4Address(ipAddress) || ipValidator.isValidInet6Address(ipAddress)) ){
			errorMsgValue = getMessage("Server.ipAddress.invalid");
			errorMsgValue = errorMsgValue.replace("[IP_ADDRESS]", ipAddress);
			errors.rejectValue(BaseConstants.IPADDRESS, "error.Server.ipAddress.invalid", errorMsgValue);
		}
	}
}