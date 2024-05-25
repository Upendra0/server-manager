package com.elitecore.sm.netflowclient.validator;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.model.SecurityTypeEnum;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.netflowclient.model.NatFlowProxyClient;
import com.elitecore.sm.netflowclient.model.NetflowClient;
import com.elitecore.sm.netflowclient.service.ProxyClientConfigurationService;
import com.elitecore.sm.services.model.GTPPrimeCollectionService;
import com.elitecore.sm.services.model.NetflowBinaryCollectionService;
import com.elitecore.sm.services.model.NetflowCollectionService;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.model.SysLogCollectionService;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.Utilities;

/**
 * @author vishal.lakhyani
 *
 */
@Component
public class NetflowClientValidator extends BaseValidator {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	ServicesService servicesService;
	
	@Autowired
	private ProxyClientConfigurationService proxyClientConfigurationService;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		boolean retunStatement = false;
		if ((NetflowClient.class.isAssignableFrom(clazz)) || (NetflowCollectionService.class.isAssignableFrom(clazz))) {
			retunStatement = true;
		}

		return retunStatement;
	}

	/**
	 * Validate Ip address
	 * 
	 * @param ipAddress
	 */
	private void validateIPAddress(String ipAddress,boolean validateForImport,String clientName,String prop,boolean checkIPV6) {
		InetAddressValidator ipValidator = new InetAddressValidator();
		String errorMsgValue;
		if (ipAddress != null && !(ipValidator.isValidInet4Address(ipAddress) || ipValidator.isValidInet6Address(ipAddress))) {
			errorMsgValue = getMessage(BaseConstants.NETFLOW_CLIENT_IP_INVALID);
			if("redirectionIp".equals(prop))
				errorMsgValue = getMessage(BaseConstants.NETFLOW_REDIRECTION_IP_INVALID);
			if("proxyServerIp".equals(prop))
				errorMsgValue = getMessage(BaseConstants.NETFLOW_PROXY_SERVER_IP_INVALID);
			
			if (validateForImport) {
				ImportValidationErrors errors = new ImportValidationErrors(BaseConstants.NETFLOW_CLIENT, clientName, prop, ipAddress, getMessage(BaseConstants.NETFLOW_CLIENT_IP_INVALID));
				importErrorList.add(errors);
			} else if("proxyServerIp".equals(prop)) {
				errors.rejectValue(prop, "error.NetflowClient.proxyServerIp.invalid", errorMsgValue);
			} else {
				errors.rejectValue(prop, "error.NetflowClient.clientIpAddress.invalid", errorMsgValue);
			}
		}
	}

	/**
	 * Validate Netflow client parameter
	 * 
	 * @param client
	 * @param serviceType
	 * @param result
	 * @param importErrorList
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateNetflowClient(NetflowClient client, String serviceType, BindingResult result, List<ImportValidationErrors> importErrorList,String entityName, boolean validateForImport) { //NOSONAR
		if (validateForImport) {
			this.importErrorList = importErrorList;
		} else {
			this.errors = result;
		}
		String clienName = client.getName();
	    String classFullName = client.getClass().getName();
	    boolean minFileSeqValue=false;
	    boolean maxFileSeqValue=false;
		//validateIPAddress(client.getClientIpAddress(), validateForImport, client.getName(),"clientIpAddress",true);
		if(!validateForImport && serviceType.equals(EngineConstants.GTPPRIME_COLLECTION_SERVICE) && client.getRedirectionIp() != null && client.getRedirectionIp().length() > 0)
			validateIPAddress(client.getRedirectionIp(),validateForImport,client.getName(),"redirectionIp",true);
		if(!validateForImport ) {
			isValidate(SystemParametersConstant.NETFLOWCLIENT_NAME, client.getName(), "name", entityName, clienName, validateForImport);
			if(client.getClientPort() != -1 && !serviceType.equals(EngineConstants.MQTT_COLLECTION_SERVICE)){
				isValidate(SystemParametersConstant.NETFLOWCLIENT_CLIENTPORT, client.getClientPort(), "clientPort", clienName, entityName, validateForImport,
						classFullName);
			}
		}
		isValidate(SystemParametersConstant.NETFLOWCLIENT_FILENAMEFORMAT, client.getFileNameFormat(), BaseConstants.FILE_NAME_FORMAT, entityName,
				clienName, validateForImport);
		/*boolean minFileSeqValue = isValidate(SystemParametersConstant.NETFLOWCLIENT_MINFILESEQVALUE, String.valueOf(client.getMinFileSeqValue()),
				"minFileSeqValue", entityName, clienName, validateForImport);
		boolean maxFileSeqValue = isValidate(SystemParametersConstant.NETFLOWCLIENT_MAXFILESEQVALUE, String.valueOf(client.getMaxFileSeqValue()),
				"maxFileSeqValue", entityName, clienName, validateForImport);*/
		if (client.isAppendFileSequenceInFileName()){
		 minFileSeqValue = isValidate(SystemParametersConstant.NETFLOWCLIENT_MINFILESEQVALUE, client.getMinFileSeqValue(),
				"minFileSeqValue",clienName, entityName, validateForImport, classFullName);
		 maxFileSeqValue = isValidate(SystemParametersConstant.NETFLOWCLIENT_MAXFILESEQVALUE, client.getMaxFileSeqValue(),
				"maxFileSeqValue",clienName, entityName, validateForImport, classFullName);
		}
		if(isValidate(SystemParametersConstant.NETFLOWCLIENT_OUTFILELOCATION, String.valueOf(client.getOutFileLocation()), "outFileLocation",
				entityName, clienName, validateForImport) && isDuplicateNetFlowOutFileLocation(client.getOutFileLocation())){
			errors.rejectValue("outFileLocation", "NetflowClient.outFileLocation.invalid", getMessage("NetflowClient.outFileLocation.invalid"));
		}

		isValidate(SystemParametersConstant.NETFLOWCLIENT_VOLLOGROLLINGUNIT, client.getVolLogRollingUnit(), "volLogRollingUnit", clienName,
				entityName, validateForImport, classFullName);
		isValidate(SystemParametersConstant.NETFLOWCLIENT_TIMELOGROLLINGUNIT, client.getTimeLogRollingUnit(), "timeLogRollingUnit", clienName,
				entityName, validateForImport, classFullName);

		if (!validateForImport && serviceType.equals(EngineConstants.NATFLOW_COLLECTION_SERVICE)) {
			logger.debug("service Type = Netflow coll service");
			isValidate(SystemParametersConstant.NETFLOWCLIENT_BKPBINARYFILELOCATION, String.valueOf(client.getBkpBinaryfileLocation()),
					"bkpBinaryfileLocation", entityName, clienName, validateForImport);
		}
		if (!validateForImport && serviceType.equals(EngineConstants.MQTT_COLLECTION_SERVICE)) 
			isValidate(SystemParametersConstant.NETFLOWCLIENT_TOPICNAME, client.getTopicName(), "topicName", entityName, clienName, validateForImport);


		if (("".equals(client.getClientIpAddress()) || client.getClientIpAddress() == null) && !serviceType.equals(EngineConstants.MQTT_COLLECTION_SERVICE)) {
			setErrorFieldErrorMessage(client.getClientIpAddress(), BaseConstants.CLIENT_IPADDRESS, clienName, entityName, validateForImport,
					"error.NetflowClient.clientIpAddress.invalid", getMessage(BaseConstants.NETFLOW_CLIENT_IP_INVALID));
		}

		if (client.isAppendFileSequenceInFileName() && minFileSeqValue && maxFileSeqValue && (client.getMinFileSeqValue() > client.getMaxFileSeqValue())) {
			setErrorFieldErrorMessage(String.valueOf(client.getMaxFileSeqValue()), "maxFileSeqValue", clienName, entityName, validateForImport,
					"error.NetflowClient.maxFileSeqValue.invalid", getMessage("NetflowClient.maxFileSeqValue.invalid.min.max"));
		}

		if (!StringUtils.isEmpty(client.getFileNameFormat())) {
			if (validateDateFormat(parseFileName(client.getFileNameFormat()))) {
				if (client.getFileNameFormat().indexOf(BaseConstants.FULL_STOP) == -1
						|| client.getFileNameFormat().substring(client.getFileNameFormat().indexOf(BaseConstants.FULL_STOP) + 1).length() == 0) {
					setErrorFieldErrorMessage(client.getFileNameFormat(), BaseConstants.FILE_NAME_FORMAT, clienName, entityName, validateForImport,
							"error.NetflowClient.fileNameFormat.invalid", getMessage("NetflowClient.fileNameFormat.invalid.extension"));
				}
			} else {
				setErrorFieldErrorMessage(client.getFileNameFormat(), BaseConstants.FILE_NAME_FORMAT, clienName, entityName, validateForImport,
						"error.NetflowClient.fileNameFormat.invalid", getMessage("NetflowClient.fileNameFormat.invalid.dateFormat"));
			}
		}

		if (client.isSnmpAlertEnable()) {
			isValidate(SystemParametersConstant.NETFLOWCLIENT_ALERT_INTERVAL, client.getAlertInterval(), "alertInterval", clienName, entityName,
					validateForImport, classFullName);
		}
		
		if(!validateForImport && serviceType.equals(EngineConstants.RADIUS_COLLECTION_SERVICE) && (client.getSharedSecretKey() == null || client.getSharedSecretKey().trim().isEmpty())) {
			errors.rejectValue("sharedSecretKey", "NetflowClient.sharedSecretKey.invalid", getMessage("NetflowClient.sharedSecretKey.invalid"));
		}
		
		if(!validateForImport && !serviceType.equals(EngineConstants.MQTT_COLLECTION_SERVICE) && !serviceType.equals(EngineConstants.COAP_COLLECTION_SERVICE)){
			boolean isUnique = validateClientIpAndPortForUniqueness(client);
			if(!isUnique){
				errors.rejectValue("clientIpAddress", "client.ip.unique", getMessage("client.ip.unique"));
				errors.rejectValue("clientPort", "client.port.unique", getMessage("client.port.unique"));
			}
		}
		
		if(!validateForImport && serviceType.equals(EngineConstants.COAP_COLLECTION_SERVICE)) {
			this.validateCoapClient(client, serviceType, result, importErrorList, entityName, validateForImport);
		}
	}
	
	public void validateCoapClient(NetflowClient client, String serviceType, BindingResult result, List<ImportValidationErrors> importErrorList,String entityName, boolean validateForImport) {
			
			String clienName = client.getName();
			String classFullName = client.getClass().getName();
	    
			isValidate(SystemParametersConstant.NETFLOWCLIENT_RESOURCESNAME, client.getResourcesName(), "resourcesName", entityName, clienName, validateForImport);
			if(client.isRegisterObserver()) {
				isValidate(SystemParametersConstant.NETFLOWCLIENT_OBSERVERTIMEOUT, client.getObserverTimeout(), "observerTimeout", clienName, entityName, validateForImport,classFullName);
			} else {
				isValidate(SystemParametersConstant.NETFLOWCLIENT_REQEXECUTIONINTERVAL, client.getReqExecutionInterval(), "reqExecutionInterval", clienName, entityName, validateForImport,classFullName);
				isValidate(SystemParametersConstant.NETFLOWCLIENT_REQEXECUTIONFREQ, client.getReqExecutionFreq(), "reqExecutionFreq", clienName, entityName, validateForImport,classFullName);
			}
			
			isValidate(SystemParametersConstant.NETFLOWCLIENT_REQUESTTIMEOUT, client.getRequestTimeout(), "requestTimeout", clienName, entityName, validateForImport,classFullName);
			isValidate(SystemParametersConstant.NETFLOWCLIENT_REQUESTRETRYCOUNT, client.getRequestRetryCount(), "requestRetryCount", clienName, entityName, validateForImport,classFullName);
			
			
			if(client.isEnableSecurity()) {
				if(SecurityTypeEnum.PSK.toString().equalsIgnoreCase(client.getSecurityType().toString())) {
					isValidate(SystemParametersConstant.NETFLOWCLIENT_SECURITYIDENTITY, client.getSecurityIdentity(), "securityIdentity", entityName, clienName, validateForImport);
					isValidate(SystemParametersConstant.NETFLOWCLIENT_SECURITYKEY, client.getSecurityKey(), "securityKey", entityName,  clienName, validateForImport);
				} else {
					isValidate(SystemParametersConstant.NETFLOWCLIENT_SECCERLOCATION, String.valueOf(client.getSecCerLocation()), "secCerLocation",entityName, clienName, validateForImport);
					isValidate(SystemParametersConstant.NETFLOWCLIENT_SECCERPASSWD, client.getSecCerPasswd(), "secCerPasswd", entityName, clienName, validateForImport);
				}
			}
			if(client.isEnableProxy()) {
				isValidate(SystemParametersConstant.NETFLOWCLIENT_PROXYRESOURCES, client.getProxyResources(), "proxyResources", entityName, clienName, validateForImport);
				validateIPAddress(client.getProxyServerIp(),validateForImport,client.getName(),"proxyServerIp",true);
				if(client.getProxyServerPort()!=-1) {
					isValidate(SystemParametersConstant.NETFLOWCLIENT_PROXYSERVERPORT, client.getProxyServerPort(), "proxyServerPort",  clienName, entityName, validateForImport,classFullName);
				}
			}
			
			if(!validateForImport && client.isEnableKafka() && client.getKafkaDataSourceConfig()==null) {
				errors.rejectValue("kafkaDataSourceConfig", "netflow.client.kafka.data.source.invalid", getMessage("netflow.client.kafka.data.source.invalid"));
			}
			if(!validateForImport && client.isEnableKafka()) {
				isValidate(SystemParametersConstant.NETFLOWCLIENT_TOPICNAME, client.getTopicName(), "topicName", entityName, clienName, validateForImport);
			}
	}
	
	public void validateProxyClient(NatFlowProxyClient client, String serviceType, BindingResult result, List<ImportValidationErrors> importErrorList,String entityName, boolean validateForImport) { //NOSONAR
		if (validateForImport) {
			this.importErrorList = importErrorList;
		} else {
			this.errors = result;
		}
		if(!validateForImport){
			
			boolean isClientForCreate = client.getId() == 0;
			
			boolean isUnique = validateProxyClientIpAndPortForUniqueness(client);
			if(isClientForCreate && !isUnique){
				errors.rejectValue("proxyIp", "proxyclient.ip.port.unique.failed", getMessage("proxyclient.ip.port.unique.failed"));
				errors.rejectValue("proxyPort", "proxyclient.ip.port.unique.failed", getMessage("proxyclient.ip.port.unique.failed"));
			} else {
				if(!Utilities.isIPv4(client.getProxyIp()))
					errors.rejectValue("proxyIp", "proxyclient.ip.invalid", getMessage("proxyclient.ip.invalid"));
				if(client.getProxyPort() < 1024 || client.getProxyPort() >65535 )
					errors.rejectValue("proxyPort", "proxyclient.port.invalid", getMessage("proxyclient.port.invalid"));
			}
		}
	}

	private boolean validateProxyClientIpAndPortForUniqueness(NatFlowProxyClient client) {
		long count= proxyClientConfigurationService.getProxyClientCount(client);
		return count == 0;
	}	

	private boolean isDuplicateNetFlowOutFileLocation(String outFileLocation) {
		String[] pathArray = outFileLocation.split(",");
		HashMap<String,Integer> duplicatePathMap = new HashMap<String,Integer>();
		for(String path : pathArray){
			if(path.startsWith(" ") || path.endsWith(" ") || path.isEmpty())
				return true;
			if(!path.startsWith(File.separator))
				path = File.separator + path;
			if(!path.endsWith(File.separator))
				path += File.separator;
			if(duplicatePathMap.containsKey(path))
				return true;
			else 
				duplicatePathMap.put(path, new Integer(1));
		}
		return false;
	}

	public boolean validateClientIpAndPortForUniqueness(NetflowClient client) {
		Service service = servicesService.getServiceById(client.getService().getId());
		List<NetflowClient> clientList = new ArrayList<>();
		List<Service> serviceList = servicesService.getServiceListToValidate(service.getServerInstance().getId());
		for(Service data : serviceList){
			if(org.apache.commons.lang3.StringUtils.equalsIgnoreCase("NATFLOW_COLLECTION_SERVICE", data.getSvctype().getAlias())){
				NetflowCollectionService netflowCollectionService = (NetflowCollectionService) data;
				clientList.addAll(netflowCollectionService.getNetFLowClientList());
			}else if(org.apache.commons.lang3.StringUtils.equalsIgnoreCase("NATFLOWBINARY_COLLECTION_SERVICE", data.getSvctype().getAlias())){
				NetflowBinaryCollectionService netflowBinaryCollectionService = (NetflowBinaryCollectionService) data;
				clientList.addAll(netflowBinaryCollectionService.getNetFLowClientList());
			}else if(org.apache.commons.lang3.StringUtils.equalsIgnoreCase("SYSLOG_COLLECTION_SERVICE", data.getSvctype().getAlias())){
				SysLogCollectionService sysLogCollectionService = (SysLogCollectionService) data;
				clientList.addAll(sysLogCollectionService.getNetFLowClientList());
			}else if(org.apache.commons.lang3.StringUtils.equalsIgnoreCase("GTPPRIME_COLLECTION_SERVICE", data.getSvctype().getAlias())){
				GTPPrimeCollectionService gtpPrimeCollectionService = (GTPPrimeCollectionService)data;
				clientList.addAll(gtpPrimeCollectionService.getNetFLowClientList());
			}
		}
		
		for(NetflowClient netflowClient : clientList){
			if(netflowClient.getStatus() == StateEnum.ACTIVE && netflowClient.getId() != client.getId()&&org.apache.commons.lang3.StringUtils.equalsIgnoreCase(client.getClientIpAddress(), netflowClient.getClientIpAddress()) && client.getClientPort() == netflowClient.getClientPort()){
				return false;
			}
		}
		return true;
	}

	/**
	 * To get the date Format String from file name
	 * 
	 * @param fileName
	 * @return dateFormat String
	 */
	public String parseFileName(String fileName) {
		String timestampFormat = "";

		char[] fileNameArray = fileName.toCharArray();
		String fileTimestampFormat = null;
		try {
			for (int i = 0; i < fileNameArray.length; i++) {
				if (fileNameArray[i] == '\\') {
					if (fileNameArray.length > i + 1) {
						i++; //NOSONAR

					}
				} else if (fileNameArray[i] == '{') {
					boolean hasClosingBrace = false;
					i++; //NOSONAR
					for (; i < fileNameArray.length; i++) { //NOSONAR
						if (fileNameArray[i] != '}') {
							if (fileNameArray[i] != '\\') { //NOSONAR
								timestampFormat += Character.toString(fileNameArray[i]);
							}
						} else {
							hasClosingBrace = true;
							break;
						}
					}
					if (hasClosingBrace) {
						fileTimestampFormat = timestampFormat;
					}

				}
			}

		} catch (Exception e) {
			logger.debug("Inside catch..So date format is not valid.." + e);
		}

		return fileTimestampFormat;

	}

}
