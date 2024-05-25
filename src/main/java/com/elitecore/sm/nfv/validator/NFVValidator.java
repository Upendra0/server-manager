/**
 * 
 */
package com.elitecore.sm.nfv.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.nfv.commons.constants.NFVConstants;
import com.elitecore.sm.nfv.commons.constants.NFVResponseCode;
import com.elitecore.sm.nfv.commons.constants.ServerTypeEnum;
import com.elitecore.sm.nfv.model.NFVAddServer;
import com.elitecore.sm.nfv.model.NFVClient;
import com.elitecore.sm.nfv.model.NFVCopyServer;
import com.elitecore.sm.nfv.model.NFVLicense;
import com.elitecore.sm.nfv.model.NFVServiceType;
import com.elitecore.sm.nfv.model.NFVSyncServer;

/**
 * The Class NFVValidator.
 *
 * @author Sagar Shah
 * July 27, 2017
 */
@Component
public class NFVValidator{
	
	/**
	 * Validate login parameters.
	 *
	 * @param httpRequest the http request
	 * @param responseObject the response object
	 * @return true, if successful
	 */
	public boolean validateLoginParams(HttpServletRequest httpRequest, ResponseObject responseObject){
		if(responseObject != null){
			String username = httpRequest.getHeader(BaseConstants.HEADER_USERNAME);
			String password = httpRequest.getHeader(BaseConstants.HEADER_PASS_PARAM);
			//TO CHECK MISSING STRING VVALUE		
			if(!validateStringValue(username, NFVResponseCode.REQUIRED_HEADER_PARAM_MISSING, responseObject)) return false;
			if(!validateStringValue(password, NFVResponseCode.REQUIRED_HEADER_PARAM_MISSING, responseObject)) return false;
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Validate server type.
	 *
	 * @param userValue the user value
	 * @param responseObject the response object
	 * @return true, if successful
	 */
	private boolean validateServerType(int userValue, ResponseObject responseObject){
		if(responseObject != null){
			boolean bValid = false;
			for(ServerTypeEnum serverType : ServerTypeEnum.values()){
				if(serverType.getCode() == userValue){
					bValid = true;
					break;
				}
			}
			if(!bValid){
				responseObject.setSuccess(bValid);
				responseObject.setResponseCodeNFV(NFVResponseCode.INVALID_FIELD_VALUE);
			}
			return bValid;
		}else{
			return false;
		}
	}
	
	/**
	 * Validate add server params.
	 *
	 * @param addServer the add server
	 * @param httpRequest the http request
	 * @param responseObject the response object
	 */
	public void validateAddServerParams(NFVAddServer addServer, HttpServletRequest httpRequest,
			ResponseObject responseObject){
		
		if(responseObject != null){
			//TO CHECK LOGIN PARAMETERES
			if(!validateLoginParams(httpRequest, responseObject)) return;
			
			//TO CHECK MISSING INT VALUE
			if(!validateIntValue(addServer.getServerType(),-1, NFVResponseCode.REQUIRED_PARAM_MISSING, responseObject)) return;
			if(!validateIntValue(addServer.getMinMemoryAllocation(),-1, NFVResponseCode.REQUIRED_PARAM_MISSING, responseObject)) return;
			if(!validateIntValue(addServer.getMaxMemoryAllocation(),-1, NFVResponseCode.REQUIRED_PARAM_MISSING, responseObject)) return;
			//TO CHECK MISSING STRING VALUE		
			if(!validateStringValue(addServer.getIpAddress(), NFVResponseCode.REQUIRED_PARAM_MISSING, responseObject)) return;
			
			//TO CHECK TYPE CHECKED VALUES
			if(!validateServerType(addServer.getServerType(), responseObject)) return;
			
			//TO CHECK IP ADDRESS
			if(!validateIPAddress(addServer.getIpAddress(), responseObject)) return;
			
			responseObject.setSuccess(true);
		}
	}
	
	/**
	 * @author brijesh.soni
	 * @see To validate sync, restart, start and stop server api call
	 * @param ipAddress
	 * @param httpRequest
	 * @param responseObject
	 */
	public void validateSyncServerParams(NFVSyncServer server, HttpServletRequest httpRequest, ResponseObject responseObject) {
		if(responseObject != null){
			//TO CHECK LOGIN PARAMETERES
			if(!validateLoginParams(httpRequest, responseObject)) return;
			//TO CHECK MISSING STRING VALUE		
			if(!validateStringValue(server.getIpAddress(), NFVResponseCode.REQUIRED_PARAM_MISSING, responseObject)) return;
			//TO CHECK IP ADDRESS
			if(!validateIPAddress(server.getIpAddress(), responseObject)) return;
			
			responseObject.setSuccess(true);
		}
	}
	
	/**
	 * @author brijesh.soni
	 * @see To validate add client api call
	 * @param client
	 * @param httpRequest
	 * @param responseObject
	 */
	public void validateClientParams(NFVClient client, HttpServletRequest httpRequest, ResponseObject responseObject) {
		if(responseObject != null){
			//TO CHECK LOGIN PARAMETERES
			if(!validateLoginParams(httpRequest, responseObject)) return;
			
			if(!validateStringValue(client.getClientIpAddress(), NFVResponseCode.REQUIRED_PARAM_MISSING, responseObject)) return;
			if(!validateStringValue(client.getServerIpAddress(), NFVResponseCode.REQUIRED_PARAM_MISSING, responseObject)) return;
			if(!validateEnum(client.getServiceType(), NFVServiceType.class, NFVResponseCode.INVALID_FIELD_VALUE, responseObject)) return;
			
			if(!validateIPAddress(client.getClientIpAddress(), responseObject)) return;
			if(!validateIPAddress(client.getServerIpAddress(), responseObject)) return;
			responseObject.setSuccess(true);
		}
	}
	
	/**
	 * @see to validate copy server api parameters
	 * @param server
	 * @param httpRequest
	 * @param responseObject
	 */
	public void validateCopyServerParams(NFVCopyServer server, HttpServletRequest httpRequest, ResponseObject responseObject) {
		if(responseObject != null){
			//TO CHECK LOGIN PARAMETERES
			if(!validateLoginParams(httpRequest, responseObject)) return;
			//TO CHECK MISSING STRING VALUE		
			if(!validateStringValue(server.getCopyFromIp(), NFVResponseCode.REQUIRED_PARAM_MISSING, responseObject)) return;
			if(!validateStringValue(server.getCopyToIp(), NFVResponseCode.REQUIRED_PARAM_MISSING, responseObject)) return;
			//TO CHECK IP ADDRESS
			if(!validateIPAddress(server.getCopyFromIp(), responseObject)) return;
			if(!validateIPAddress(server.getCopyToIp(), responseObject)) return;
			
			responseObject.setSuccess(true);
		}
	}
	
	/**
	 * Validate license activation params.
	 *
	 * @param licenseData the license data
	 * @param httpRequest the http request
	 * @param responseObject the response object
	 */
	public void validateLicenseActivationParams(NFVLicense licenseData, HttpServletRequest httpRequest,
			ResponseObject responseObject){
		
		if(responseObject != null){
			//TO CHECK LOGIN PARAMETERES
			if(!validateLoginParams(httpRequest, responseObject)) return;
			
			//TO CHECK MISSING STRING VALUE		
			if(!validateStringValue(licenseData.getServerInstanceIP(), NFVResponseCode.REQUIRED_PARAM_MISSING, responseObject)) return;
			if(!validateStringValue(licenseData.getCopyFromIP(), NFVResponseCode.REQUIRED_PARAM_MISSING, responseObject)) return;
			//if(!validateStringValue(licenseData.getHostName(), NFVResponseCode.REQUIRED_PARAM_MISSING, responseObject)) return;
			
			//TO CHECK IP ADDRESS
			if(!validateIPAddress(licenseData.getServerInstanceIP(), responseObject)) return;
			if(!validateIPAddress(licenseData.getCopyFromIP(), responseObject)) return;
			
			responseObject.setSuccess(true);
		}
	}
	
	/**
	 * Validate Integer value.
	 *
	 * @param userValue the user value
	 * @param errorValue the error value
	 * @param responseCode the response code
	 * @param responseObject the response object
	 * @return true, if successful
	 */
	private boolean validateIntValue(int userValue, int errorValue, NFVResponseCode responseCode,
			ResponseObject responseObject){
		if(responseObject != null){
			if(userValue == errorValue){
				responseObject.setSuccess(false);
				responseObject.setResponseCodeNFV(responseCode);
				return false;
			}
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Validate string value.
	 *
	 * @param userValue the user value
	 * @param responseCode the response code
	 * @param responseObject the response object
	 * @return true, if successful
	 */
	private boolean validateStringValue(String userValue, NFVResponseCode responseCode,
			ResponseObject responseObject){
		if(responseObject != null){
			if(StringUtils.isBlank(userValue)){
				responseObject.setSuccess(false);
				responseObject.setResponseCodeNFV(responseCode);
				return false;
			}
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Validate IP address.
	 *
	 * @param ipAddress the ip address
	 * @param responseObject the response object
	 * @return true, if successful
	 */
	private boolean validateIPAddress(String ipAddress, ResponseObject responseObject){
		if(responseObject != null){
		    if(ipAddress!=null && System.getenv(BaseConstants.KUBERNETES_ENV)!=null 
		        && System.getenv(BaseConstants.KUBERNETES_ENV).equalsIgnoreCase("TRUE")){
		            return true;
		    }else if(!isIPv4(ipAddress)){
				responseObject.setSuccess(false);
				responseObject.setResponseCodeNFV(NFVResponseCode.INVALID_IP_ADDRESS);
				return false;
			}
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Checks if is IPv4 Address.
	 *
	 * @param ipAddress the IPAddress
	 * @return true, if is IPv4 address
	 */
	private boolean isIPv4(String ipAddress) {
		if(!StringUtils.isBlank(ipAddress)){
			Pattern ipv4Pattern = Pattern.compile(NFVConstants.IPV4ADDRESSPATTERN);
			Matcher ipv4Matcher = ipv4Pattern.matcher(ipAddress);
			return ipv4Matcher.matches();
		}else{
			return false;
		}
	}
	
	/**
	 * @author brijesh.soni
	 * @see Validate string that it is valid enum or not
	 * @param value
	 * @param enumClass
	 * @param responseCode
	 * @param responseObject
	 * @return boolean
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public boolean validateEnum(final String value, final Class enumClass, NFVResponseCode responseCode,
			ResponseObject responseObject) {
		Enum myEnum = getEnumFromString(value, enumClass);
		if(myEnum == null) {
			responseObject.setSuccess(false);
			responseObject.setResponseCodeNFV(responseCode);
			return false;
		}
		return true;
	}
	
	/**
	 * @author brijesh.soni
	 * @see get enum from string value
	 * @param string value
	 * @param enumClass
	 * @return Enum
	 */
	public <T extends Enum<T>> T getEnumFromString(final String value, final Class<T> enumClass) {
		String myValue = value;
		T[] results = enumClass.getEnumConstants();
		for (T result : results) {
			if(result.toString().equalsIgnoreCase(myValue)) {
				return Enum.valueOf(enumClass, result.toString());
			}
		}
		return null;
	}
	
}