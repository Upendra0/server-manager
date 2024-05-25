package com.elitecore.sm.migration.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.MigrationConstants;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.migration.model.SNMPAlertsEntity;
import com.elitecore.sm.migration.model.SnmpListenersEntity;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.snmp.model.SNMPAlert;
import com.elitecore.sm.snmp.model.SNMPAlertTypeEnum;
import com.elitecore.sm.snmp.model.SNMPAlertWrapper;
import com.elitecore.sm.snmp.model.SNMPServerConfig;
import com.elitecore.sm.snmp.model.SNMPServerType;
import com.elitecore.sm.snmp.model.SNMPServiceThreshold;
import com.elitecore.sm.snmp.service.SnmpService;
import com.elitecore.sm.snmp.validator.SnmpValidator;
import com.elitecore.sm.util.MigrationUtil;

/**
 * 
 * @author Jui Purohit
 *
 */
@org.springframework.stereotype.Service(value = "snmpMigration")
public class SNMPMigrationImpl implements SNMPMigration {

	private static Logger logger = Logger.getLogger(DataSourceMigrationImpl.class);

	@Autowired
	private MigrationUtil migrationUtil;

	@Autowired
	private SnmpValidator validator;

	@Autowired
	private SnmpService snmpService;

	@Autowired
	private ServicesService servicesService;

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseObject getSnmpUnmarshalObject(String destinationDir, String migrationPrefix) throws MigrationSMException {
		logger.debug("Unmarshalling snmp config object");

		ResponseObject responseObject;
		Map<String, byte[]> fileContent = migrationUtil.getEntityFileContent();

		responseObject = migrationUtil.getSMAndJaxbObjectFromXml(fileContent.get(EngineConstants.SNMP_CONFIGURATION),
				MigrationConstants.SNMP_CONFIG_XSD, MigrationConstants.SNMP_SERVER_CONFIG_XML);
		if (responseObject.isSuccess()) {
			logger.info("JAXB unamarshalling done successfully for snmp config object.");
			Map<String, Object> returnObjectsMap = (Map<String, Object>) responseObject.getObject();

			SNMPServerConfig snmpServerConfig = (SNMPServerConfig) returnObjectsMap.get(MigrationConstants.MAP_SM_CLASS_KEY);
			if (snmpServerConfig != null && (snmpServerConfig.getHostIP()!=null && !("0".equals(snmpServerConfig.getPort())) && snmpServerConfig.getPortOffset()!=0)) {
				snmpServerConfig.setType(SNMPServerType.Self);

				snmpServerConfig.setName(migrationUtil.getRandomName("snmpServer"));
				responseObject = valiateSnmpServerConfigParams(snmpServerConfig); // Validating
																					// all
																					// SNMP
																					// parameters.
				
			}
			else{
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.SNMP_NOT_FOUND);
				logger.info("No SNMP Server configured for the server instance ");
			}
		}// For failure case response object will set messaged from calling
			// method.
		return responseObject;

	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseObject getSnmpListnerUnmarshalObject(String destinationDir, String migrationPrefix, ServerInstance serverInstance)
			throws MigrationSMException {
		logger.debug("Unmarshalling snmp listner config object");

		ResponseObject responseObject;
		Map<String, byte[]> fileContent = migrationUtil.getEntityFileContent();

		Map<String, Class<?>> mapOfClasses = migrationUtil.getClasses(MigrationConstants.SNMP_ALERT_LISTENERS_XML);

		responseObject = migrationUtil.unmarshalObjectFromFile(fileContent.get(EngineConstants.SNMP_ALERT_LISTENER_CONFIGURATION),
				mapOfClasses.get(MigrationConstants.MAP_JAXB_CLASS_KEY), MigrationConstants.SNMP_ALERT_LISTENERS_XSD, MigrationConstants.SNMP_ALERT_LISTENERS_XML);
		if (responseObject.isSuccess()) {
			logger.info("JAXB unamarshalling done successfully for snmp listener config object.");

			SnmpListenersEntity snmpClientConfigEntity = (SnmpListenersEntity) responseObject.getObject();

			responseObject = convertToSnmpListerFromJAXBClass(snmpClientConfigEntity, serverInstance);
		if (responseObject.isSuccess() && responseObject.getObject()!=null) {
		List<SNMPServerConfig> snmpClientList = (List<SNMPServerConfig>) responseObject.getObject();
		for(int i=0;i<snmpClientList.size();i++){
			SNMPServerConfig snmpClient=snmpClientList.get(i);
			if(snmpClient!=null){
			responseObject=snmpService.updateMigSnmpClientWIthAlerts(snmpClient);
			
			if(responseObject.isSuccess()){
				SNMPServerConfig snmpClientdb=(SNMPServerConfig)responseObject.getObject();
				if(snmpClientdb!=null && snmpClientdb.getConfiguredAlerts()!=null && !snmpClientdb.getConfiguredAlerts().isEmpty()){
					responseObject=addServiceThresholdFromJAXBClass(snmpClientdb.getConfiguredAlerts());
					if(responseObject.isSuccess()){
						List<SNMPAlertWrapper> finalAlertWrapperList=(List<SNMPAlertWrapper>)responseObject.getObject();
						if(finalAlertWrapperList!=null && !finalAlertWrapperList.isEmpty()){
							for(int j=0;j<finalAlertWrapperList.size();j++){
								SNMPAlertWrapper alertWrapperObj=finalAlertWrapperList.get(j);
								responseObject=snmpService.updateMigAlertWrapperWithThreshold(alertWrapperObj);
							}
						}
					}
				}
			}
			
			}
			
			
		}
		}
	}// For failure case response object will set messaged from calling
	// method.
		return responseObject;

	}

	private ResponseObject valiateSnmpServerConfigParams(SNMPServerConfig snmpServerConfig) {

		logger.debug("Validating snmp server configuration parameters.");
		ResponseObject responseObject = new ResponseObject();

		List<ImportValidationErrors> importErrorList = new ArrayList<>();
		validator.validateSnmpServerConfigParam(snmpServerConfig, null, importErrorList, snmpServerConfig.getName(), true);
		if (!importErrorList.isEmpty()) {
			logger.debug("Validation Fail for imported file");
			JSONArray finaljArray = new JSONArray();

			for (ImportValidationErrors errors : importErrorList) {

				JSONArray jArray = new JSONArray();
				jArray.put(errors.getModuleName());
				jArray.put(errors.getEntityName());
				jArray.put(errors.getPropertyName());
				jArray.put(errors.getPropertyValue());
				jArray.put(errors.getErrorMessage());
				finaljArray.put(jArray);
			}
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SNMP_VALIDATION_FAIL);
			responseObject.setObject(finaljArray);

		} else {
			responseObject.setSuccess(true);
			responseObject.setObject(snmpServerConfig);
		}

		return responseObject;
	}

	private ResponseObject valiateSnmpClientConfigParams(SNMPServerConfig snmpServerConfig) {

		logger.debug("Validating snmp server configuration parameters.");
		ResponseObject responseObject = new ResponseObject();

		List<ImportValidationErrors> importErrorList = new ArrayList<>();
		validator.validateSnmpClientConfigParam(snmpServerConfig, null, importErrorList, snmpServerConfig.getName(), true);
		if (!importErrorList.isEmpty()) {
			logger.debug("Validation Fail for imported file");
			JSONArray finaljArray = new JSONArray();

			for (ImportValidationErrors errors : importErrorList) {

				JSONArray jArray = new JSONArray();
				jArray.put(errors.getModuleName());
				jArray.put(errors.getEntityName());
				jArray.put(errors.getPropertyName());
				jArray.put(errors.getPropertyValue());
				jArray.put(errors.getErrorMessage());
				finaljArray.put(jArray);
			}
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SNMP_VALIDATION_FAIL);
			responseObject.setObject(finaljArray);

		} else {
			responseObject.setSuccess(true);
			responseObject.setObject(snmpServerConfig);
		}

		return responseObject;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public ResponseObject convertToSnmpListerFromJAXBClass(SnmpListenersEntity snmpListnerJAXBObj, ServerInstance serverInstance) throws MigrationSMException {
		ResponseObject responseObject = new ResponseObject();
		List<SNMPAlertWrapper> snmpalertWrapperList = new ArrayList<>();
		List<SNMPServerConfig> snmpClientConfigListFinal = new ArrayList<>();
		SNMPServerConfig snmpClientConfigdb;
		if (snmpListnerJAXBObj != null) {
			List<SnmpListenersEntity.Listener> listnerList = snmpListnerJAXBObj.getListener();
			if (listnerList != null && !listnerList.isEmpty()) {
				for (int i = 0; i < listnerList.size(); i++) {
					SNMPServerConfig snmpClientConfig;
						responseObject = migrationUtil.convertJaxbToSMObject(listnerList.get(i), SNMPServerConfig.class);
						if (responseObject.isSuccess()) {
							snmpClientConfig = (SNMPServerConfig) responseObject.getObject();
							snmpClientConfig.setServerInstance(serverInstance);
							snmpClientConfig.setName(migrationUtil.getRandomName(snmpClientConfig.getName()));
							snmpClientConfig.setType(SNMPServerType.Listener);
							responseObject = valiateSnmpClientConfigParams(snmpClientConfig);
							logger.debug("Response Object:--"+responseObject.isSuccess());
							if (responseObject.isSuccess()) {
								responseObject = snmpService.updateMigSnmpServer(snmpClientConfig);
								if (responseObject.isSuccess()) {

									snmpClientConfigdb = (SNMPServerConfig) responseObject.getObject();
									if (snmpClientConfigdb != null) {
										List<String> alertList = listnerList.get(i).getAlerts().getId();
										if (alertList != null) {
											ResponseObject responseObj;
											for (int j = 0; j < alertList.size(); j++) {
												responseObj = snmpService.getSnmpAlertByAlertId(alertList.get(j));
												if (responseObj.isSuccess()) {
													SNMPAlert snmpAlertdb = (SNMPAlert) responseObj.getObject();
													SNMPAlertWrapper snmpAlertWrapper = new SNMPAlertWrapper();
													snmpAlertWrapper.setAlert(snmpAlertdb);
													snmpAlertWrapper.setListener(snmpClientConfigdb);
													snmpalertWrapperList.add(snmpAlertWrapper);
												}
											}
										}

										snmpClientConfigdb.setConfiguredAlerts(snmpalertWrapperList);
										snmpClientConfigListFinal.add(snmpClientConfigdb);
									}
								}
								responseObject.setObject(snmpClientConfigListFinal);
								responseObject.setSuccess(true);
							}
							else{
								break;
							}

						} else {
							break;
						}
					
				}
				
			}
			else{
				responseObject.setSuccess(true);
				logger.info("No SNMP Client configured for the server instance ");
			}
		}
		else{
			responseObject.setSuccess(true);
			logger.info("No SNMP Client configured for the server instance ");
		}
		return responseObject;
	}

	private ResponseObject addServiceThresholdFromJAXBClass(List<SNMPAlertWrapper> alertWrapperList) throws MigrationSMException {
		ResponseObject responseObject;
		Map<String, byte[]> fileContent = migrationUtil.getEntityFileContent();

		Map<String, Class<?>> mapOfClasses = migrationUtil.getClasses(MigrationConstants.ALERT_CONF_XML);

		responseObject = migrationUtil.unmarshalObjectFromFile(fileContent.get(EngineConstants.ALERT_CONFIGURATION),
				mapOfClasses.get(MigrationConstants.MAP_JAXB_CLASS_KEY), MigrationConstants.ALERT_CONF_XSD,MigrationConstants.ALERT_CONF_XML);
		if (responseObject.isSuccess()) {
			logger.info("JAXB unamarshalling done successfully for snmp listener config object.");

			SNMPAlertsEntity snmpAlertEntity = (SNMPAlertsEntity) responseObject.getObject();
			List<SNMPServiceThreshold> svcThresholdObjList = new ArrayList<>();
			List<SNMPAlertWrapper> alertWrapperListFinal = new ArrayList<>();
			List<SNMPAlertsEntity.Alert> alertJAXBList = snmpAlertEntity.getAlert();
			if (alertJAXBList != null && !alertJAXBList.isEmpty()) {
				if (alertWrapperList != null && !alertWrapperList.isEmpty()) {
					for (int i = 0; i < alertWrapperList.size(); i++) {
						SNMPAlertWrapper alertWrapper = alertWrapperList.get(i);
						if (alertWrapper != null) {
							for (int j = 0; j < alertJAXBList.size(); j++) {
 								SNMPAlertsEntity.Alert alertJAXB = alertJAXBList.get(j);
 								logger.debug("ALERTWRAPPER Alert id is:"+alertWrapper.getAlert().getAlertId()+"#############"+"  "+"ALERTJAXB obj id is::"+alertJAXB.getId());
								if (alertWrapper.getAlert().getAlertId().equals(alertJAXB.getId())) {
									
									SNMPAlertsEntity.Alert.ServiceThresholdList svcThresholdListJAXB = alertJAXB.getServiceThresholdList();
									if (svcThresholdListJAXB != null) {
										logger.debug("Threshold details are:::" + svcThresholdListJAXB.getServiceThresholdObjList());
										List<SNMPAlertsEntity.Alert.ServiceThresholdList.ServiceThreshold> svcAlertThresholdListJAXB = svcThresholdListJAXB.getServiceThresholdObjList();
										if (svcAlertThresholdListJAXB != null && !svcAlertThresholdListJAXB.isEmpty()) {
											for (int k = 0; k < svcAlertThresholdListJAXB.size(); k++) {
												SNMPAlertsEntity.Alert.ServiceThresholdList.ServiceThreshold svcThresholdJAXBObj =  svcAlertThresholdListJAXB
														.get(k);
												if (svcThresholdJAXBObj != null && svcThresholdJAXBObj.getThreshold()!=0 && svcThresholdJAXBObj.getServiceId()!=null) {
													SNMPServiceThreshold snmpServiceThreshold = new SNMPServiceThreshold();
													snmpServiceThreshold.setWrapper(alertWrapper);
													if (alertWrapper.getAlert().getAlertType().getCategory() != SNMPAlertTypeEnum.SERVER_INSTANCE) {
														String[] parts = svcThresholdJAXBObj.getServiceId().split("-");
														if (parts[0] != null && parts[1] != null) {
															Service service = servicesService.getServiceListByIDAndTypeAlias(parts[0],
																	parts[1], alertWrapper.getListener().getServerInstance()
																			.getId());
															if (service != null) {
																snmpServiceThreshold.setService(service);
																
															}
															else{
																continue;
															}
														}

													} else {
														snmpServiceThreshold.setServerInstance(alertWrapper.getListener().getServerInstance());
													}

													snmpServiceThreshold.setThreshold(svcThresholdJAXBObj.getThreshold());
													svcThresholdObjList.add(snmpServiceThreshold);
													alertWrapper.setServiceThresholdConfigured(true);
												}
											}
											alertWrapper.setServiceThreshold(svcThresholdObjList);
										}
									}

									alertWrapperListFinal.add(alertWrapper);
								}
							}
						}
					}
					responseObject.setObject(alertWrapperListFinal);
				} else {
					logger.debug("SNMPAlertWrapper List is null");
				}

			}

		}
		return responseObject;
	}
}
