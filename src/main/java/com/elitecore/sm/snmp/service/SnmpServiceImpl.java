package com.elitecore.sm.snmp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.elitecore.core.util.mbean.SNMPSupportedRFCController;
import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.integration.engine.RemoteJMXHelper;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.snmp.dao.SNMPAlertDao;
import com.elitecore.sm.snmp.dao.SNMPAlertWrapperDao;
import com.elitecore.sm.snmp.dao.SNMPServiceThresholdDao;
import com.elitecore.sm.snmp.dao.SnmpDao;
import com.elitecore.sm.snmp.model.SNMPAlert;
import com.elitecore.sm.snmp.model.SNMPAlertType;
import com.elitecore.sm.snmp.model.SNMPAlertTypeEnum;
import com.elitecore.sm.snmp.model.SNMPAlertWrapper;
import com.elitecore.sm.snmp.model.SNMPServerConfig;
import com.elitecore.sm.snmp.model.SNMPServerType;
import com.elitecore.sm.snmp.model.SNMPServiceThreshold;
import com.elitecore.sm.util.EliteUtils;

/**
 * @author Jui Purohit Apr 26, 2016
 */

@org.springframework.stereotype.Service(value = "snmpService")
public class SnmpServiceImpl implements SnmpService {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	ServerInstanceService servInstanceService;

	@Autowired
	SnmpDao snmpDao;

	@Autowired
	SNMPAlertDao snmpAlertDao;

	@Autowired
	ServicesService servicesService;

	@Autowired
	SNMPAlertWrapperDao snmpAlertWrapperDao;

	@Autowired
	ServicesDao servicesDao;

	@Autowired
	SNMPServiceThresholdDao snmpServiceThresholdDao;

	@Autowired
	ServerInstanceDao serverInstanceDao;

	/**
	 * Add Snmp Server into database
	 * 
	 * @param snmpserver
	 * @return
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.CREATE_SNMP_SERVER,actionType = BaseConstants.CREATE_ACTION, currentEntity = SNMPServerConfig.class, ignorePropList= "")
	public ResponseObject addSnmpServerList(SNMPServerConfig snmpserver) {

		ResponseObject responseObject = new ResponseObject();
		int snmpServerCount = snmpDao.getSnmpServerCount(snmpserver.getName());
		if (snmpServerCount > 0) {

			logger.debug("inside addSnmpServerList : duplicate SnmpServer name found:" + snmpserver.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_SNMP_SERVER_NAME);

		} else {
			ServerInstance serverInstance = servInstanceService.getServerInstance(snmpserver.getServerInstance().getId()); 
			snmpserver.setType(SNMPServerType.Self);
			snmpserver.setServerInstance(serverInstance);
			snmpserver.setStatus(StateEnum.ACTIVE);
			
			String[] engineId = new SNMPSupportedRFCController().retriveSNMPV3EngineId(snmpserver.getHostIP(),  Integer.parseInt(snmpserver.getPort())); 
			if(engineId != null && engineId.length > 1){
				snmpserver.setSnmpV3EngineId(engineId[0]);
				snmpserver.setSnmpV3DesEngineId(engineId[1]);
			}
			
			
			snmpDao.save(snmpserver);
			if (snmpserver.getId() != 0) {
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.SNMP_SERVER_ADDED_SUCCESSFULLY);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SNMP_SERVER_ADDED_FAIL);
			}
		}
		return responseObject;

	}

	/**
	 * Method will get total count of snmpo servers
	 * 
	 * @param serverInstanceId
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public long getSnmpServertotalCount(int serverInstanceId) {
		Map<String, Object> snmpServerConditions = snmpDao.getSnmpServerPaginatedList(serverInstanceId);
		return snmpDao.getQueryCount(SNMPServerConfig.class, (List<Criterion>) snmpServerConditions.get(BaseConstants.CONDITIONS),
				(HashMap<String, String>) snmpServerConditions.get(BaseConstants.ALIASES));
	}

	/**
	 * Method will fetch SnmpServerList
	 * 
	 * @param serverInstanceId
	 * @param startIndex
	 * @param limit
	 * @param sidx
	 * @param sord
	 * @return List of SnmpServer
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Map<String, Object>> getSnmpServerPaginatedList(int serverInstanceId, int startIndex, int limit, String sidx, String sord) {
		List<Map<String, Object>> rowList = new ArrayList<>();
		Map<String, Object> row;
		Map<String, Object> conditionsAndAliases = snmpDao.getSnmpServerPaginatedList(serverInstanceId);

		List<SNMPServerConfig> snmpServerList = snmpDao.getSnmpServerPaginatedList(SNMPServerConfig.class,
				(List<Criterion>) conditionsAndAliases.get(BaseConstants.CONDITIONS),
				(HashMap<String, String>) conditionsAndAliases.get(BaseConstants.ALIASES), startIndex, limit, sidx, sord);

		if (snmpServerList != null && !snmpServerList.isEmpty()) {
			for (SNMPServerConfig snmpsever : snmpServerList) {
				row = new HashMap<>();

				row.put("id", snmpsever.getId());
				row.put(BaseConstants.SNMP_SERVER_ID, snmpsever.getId());
				row.put(BaseConstants.SNMP_SERVER_NAME, snmpsever.getName());
				row.put(BaseConstants.SNMP_SERVER_IP, snmpsever.getHostIP());
				row.put(BaseConstants.SNMP_SERVER_PORT, snmpsever.getPort());
				row.put(BaseConstants.SNMP_SERVER_OFFSET, snmpsever.getPortOffset());
				row.put(BaseConstants.SNMP_SERVER_COMMUNITY, snmpsever.getCommunity());
				row.put(BaseConstants.SNMP_SERVER_ENABLE, snmpsever.getStatus().toString().trim());
				String engineId = "-|-"; 
				if(snmpsever.getSnmpV3EngineId() != null && snmpsever.getSnmpV3DesEngineId() != null
						&& snmpsever.getSnmpV3DesEngineId().length() >0 && snmpsever.getSnmpV3EngineId().length() > 0){
					engineId = snmpsever.getSnmpV3EngineId() + " | "+ snmpsever.getSnmpV3DesEngineId();
				}
				row.put(BaseConstants.SNMP_SERVER_V3_ENGINE_ID, engineId);

				row.put(BaseConstants.EDIT, "Edit");
				rowList.add(row);

			}
		}
		return rowList;
	}

	/**
	 * Change SNMP Server status
	 * 
	 * @param SnmpServerId
	 * @param snmpStatus
	 * @param serverInstanceId
	 * @return
	 * @throws CloneNotSupportedException
	 */

	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SNMP_SERVER_STATUS,actionType = BaseConstants.UPDATE_CUSTOM_ACTION, currentEntity = SNMPServerConfig.class, ignorePropList= "configuredAlerts,serverInstance")
	public ResponseObject changeSnmpServerStatus(int snmpServerId, String snmpStatus, int serverInstanceId) throws CloneNotSupportedException {
		ResponseObject responseObject = new ResponseObject();

		int activeSnmpServerCount = snmpDao.getActiveSnmpServerCount(serverInstanceId);
		logger.debug("Snmp server count:" + activeSnmpServerCount);

		if (activeSnmpServerCount == 0 || StateEnum.INACTIVE.name().equals(snmpStatus.trim())) {
			logger.debug("Snmp server count is 0 or status is inactive , so allow change status:" + snmpStatus);

			responseObject = getSnmpServerById(snmpServerId);
			if (responseObject.isSuccess()) {
				SNMPServerConfig snmpServer = (SNMPServerConfig) responseObject.getObject();

				if (snmpServer != null) {

					if (StateEnum.ACTIVE.name().equals(snmpStatus.trim())) {
						snmpServer.setStatus(StateEnum.ACTIVE);
					} else {
						snmpServer.setStatus(StateEnum.INACTIVE);
					}

					snmpDao.merge(snmpServer);

					responseObject.setResponseCode(ResponseCode.SNMP_UPDATE_SUCCESS);
					responseObject.setSuccess(true);

				} else {
					responseObject.setResponseCode(ResponseCode.SNMP_UPDATE_FAIL);
					responseObject.setSuccess(false);
				}

			} else {
				responseObject.setResponseCode(ResponseCode.SNMP_UPDATE_FAIL);
				responseObject.setSuccess(false);
			}

		} else {
			logger.debug("Snmp server count is >0 not allow to change status");
			responseObject.setResponseCode(ResponseCode.SNMP_UPDATE_FAIL_Only_One_Allowed);
			responseObject.setSuccess(false);
		}
		return responseObject;
	}

	/**
	 * Get snmp Server details by its id
	 * 
	 * @param snmpServerId
	 * @return
	 */

	@Transactional(readOnly = true)
	public ResponseObject getSnmpServerById(int snmpServerId) {

		ResponseObject responseObject = new ResponseObject();

		logger.debug("inside getSnmpServerById : Fetch configuration for SNMP Server");
		SNMPServerConfig snmpServer = snmpDao.getSnmpServerById(snmpServerId);
		if (snmpServer != null) {
			responseObject.setSuccess(true);
			responseObject.setObject(snmpServer);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SNMP_SERVER_NOT_FOUND);
		}

		return responseObject;

	}

	/**
	 * Update Snmp Server detail
	 * 
	 * @param snmpServer
	 * @return
	 */
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_SNMP_SERVER_CONFIGURATION,actionType = BaseConstants.UPDATE_ACTION, currentEntity = SNMPServerConfig.class, ignorePropList= "configuredAlerts,serverInstance")
	public ResponseObject updateSnmpServerDetail(SNMPServerConfig snmpServer) {
		ResponseObject responseObject = new ResponseObject();

		if (isSnmpServerNameUniqueForUpdate(snmpServer.getId(), snmpServer.getName())) {
			if (snmpServer.getId() != 0) {
				SNMPServerConfig snmpServerDB = snmpDao.findByPrimaryKey(SNMPServerConfig.class, snmpServer.getId());

				if (snmpServerDB != null) {

					snmpServerDB.setName(snmpServer.getName());
					snmpServerDB.setHostIP(snmpServer.getHostIP());
					snmpServerDB.setPort(snmpServer.getPort());
					snmpServerDB.setPortOffset(snmpServer.getPortOffset());
					snmpServerDB.setCommunity(snmpServer.getCommunity());
					snmpServerDB.setSnmpV3EngineId(snmpServer.getSnmpV3EngineId());
					snmpServerDB.setSnmpV3DesEngineId(snmpServer.getSnmpV3DesEngineId());
					snmpServerDB.setLastUpdatedByStaffId(snmpServer.getLastUpdatedByStaffId());
					snmpServerDB.setLastUpdatedDate(new Date());
					
					ServerInstance serverInstance = snmpServerDB.getServerInstance();
					if(serverInstance == null){
						serverInstance = snmpServer.getServerInstance();
					}
					
					String[] engineId = new SNMPSupportedRFCController().retriveSNMPV3EngineId(snmpServer.getHostIP(), Integer.parseInt(snmpServer.getPort()));
					if(engineId != null && engineId.length > 1){
						snmpServerDB.setSnmpV3EngineId(engineId[0]);
						snmpServerDB.setSnmpV3DesEngineId(engineId[1]);
					}

					snmpDao.merge(snmpServerDB);

					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.SNMP_SERVER_UPDATE_SUCCESS);
				}

			} else {
				logger.debug("RegEx Pattern not found from DB");

				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SNMP_SERVER_UPDATE_FAIL);
			}
		} else {
			logger.debug("inside updateSnmpServernDetail : duplicate snmp server name found:" + snmpServer.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_SNMP_SERVER_NAME);
		}

		return responseObject;
	}

	/**
	 * Check snmp server name is unique or not for update
	 * 
	 * @param snmpServerId
	 * @param snmpServerName
	 * @return
	 */
	@Transactional
	public boolean isSnmpServerNameUniqueForUpdate(int snmpServerId, String snmpServerName) {
		List<SNMPServerConfig> snmpServerList = snmpDao.getSnmpServerListByName(snmpServerName);
		boolean isUnique = false;
		if (snmpServerList != null && !snmpServerList.isEmpty()) {

			for (SNMPServerConfig snmpServer : snmpServerList) {
				// If ID is same , then it is same snmp server object
				if (snmpServerId == (snmpServer.getId())) {
					isUnique = true;
				} else { // It is another snmp server object , but name is same
					isUnique = false;

				}
			}
		} else if (snmpServerList != null && snmpServerList.isEmpty()) { // No snmp Server found with same name
			isUnique = true;
		}

		return isUnique;
	}

	/**
	 * Method will delete snmp server
	 * 
	 * @param snmpServerId
	 * @param staffId
	 * @return ResponseObject
	 * @throws CloneNotSupportedException
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.DELETE_SNMP_SERVER,actionType = BaseConstants.DELETE_ACTION, currentEntity = SNMPServerConfig.class, ignorePropList= "")
	public ResponseObject deleteSnmpServer(int snmpServerId, int staffId) throws CloneNotSupportedException {
		ResponseObject responseObject = new ResponseObject();

		SNMPServerConfig snmpServer = snmpDao.findByPrimaryKey(SNMPServerConfig.class, snmpServerId);
		if (snmpServer != null) {
			snmpServer.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,snmpServer.getName()));
			snmpServer.setStatus(StateEnum.DELETED);
			snmpServer.setLastUpdatedByStaffId(staffId);
			snmpServer.setLastUpdatedDate(new Date());

			snmpDao.merge(snmpServer);

			responseObject.setSuccess(true);
			responseObject.setObject(snmpServer);
			responseObject.setResponseCode(ResponseCode.SNMP_SERVER_DELETE_SUCCESS);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SNMP_SERVER_DELETE_FAIL);
		}
		return responseObject;
	}

	/**
	 * Add Snmp Client to db
	 * 
	 * @param snmpClient
	 * @return ResponseObject
	 */

	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.ADD_SNMP_CLIENT_CONFIGURATION,actionType = BaseConstants.CREATE_ACTION, currentEntity = SNMPServerConfig.class, ignorePropList= "")
	public ResponseObject addSnmpClient(SNMPServerConfig snmpClient) {

		ResponseObject responseObject = new ResponseObject();
		int snmpServerCount = snmpDao.getSnmpServerCount(snmpClient.getName());
		if (snmpServerCount > 0) {

			logger.debug("inside addSnmpClient : duplicate SnmpClient name found:" + snmpClient.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_SNMP_CLIENT_NAME);

		} else {
			snmpClient.setType(SNMPServerType.Listener);
			snmpClient.setPortOffset(0);
			snmpClient.setServerInstance(servInstanceService.getServerInstance(snmpClient.getServerInstance().getId()));
			if(snmpClient.getSnmpV3AuthPassword()!=null){
				snmpClient.setSnmpV3AuthPassword(EliteUtils.encryptData(snmpClient.getSnmpV3AuthPassword()));
			}
			if(snmpClient.getSnmpV3PrivPassword()!=null){
				snmpClient.setSnmpV3PrivPassword(EliteUtils.encryptData(snmpClient.getSnmpV3PrivPassword()));
			}
			snmpDao.save(snmpClient);
			if (snmpClient.getId() != 0) {
				List<String> serviceType = getServiceTypeforServerInstance(snmpClient.getServerInstance().getId());
				List<SNMPAlertType> alertType;
				if (serviceType == null || serviceType.isEmpty()) {

					serviceType = new ArrayList<>();
				}
				serviceType.add(BaseConstants.SERVER_INSTANCE);
				

				alertType = getAlertTypeforServerInstance(serviceType);

				JSONArray jAllAlertArray = getSnmpAlertByAlertType(alertType);
				JSONObject clientId = new JSONObject();
				clientId.put("clientId", snmpClient.getId());
				clientId.put("alertList", jAllAlertArray);

				responseObject.setObject(clientId);
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.SNMP_CLIENT_ADDED_SUCCESSFULLY);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SNMP_CLIENT_ADDED_FAIL);
			}
		}
		return responseObject;

	}

	/**
	 * Fetch configured Service from Database
	 * 
	 * @param serverInstanceId
	 * @return alertTypeList
	 */
	@Transactional(readOnly = true)
	@Override
	public List<String> getServiceTypeforServerInstance(int serverInstanceId) {

		List<Service> serviceList = servicesService.getServicesforServerInstance(serverInstanceId);
		List<String> alertTypeList = new ArrayList<>();
		String serviceType;
		if (serviceList != null && !serviceList.isEmpty()) {
			for (Service configserviceList : serviceList) {
				serviceType = configserviceList.getSvctype().getAlias();
				if (!(alertTypeList.contains(serviceType))) {
					alertTypeList.add(serviceType);
				}
			}
			alertTypeList.add(BaseConstants.GENERIC);
		}

		logger.debug("List of active service alias: " + alertTypeList.toString());

		return alertTypeList;
	}

	/**
	 * Fetch SNMP Alert Type for Configured Services
	 * 
	 * @param serviceType
	 * @return List<SNMPAlertType>
	 */
	@Transactional(readOnly = true)
	@Override
	public List<SNMPAlertType> getAlertTypeforServerInstance(List<String> serviceType) {

		return snmpDao.getSnmpAlertType(serviceType);

	}

	/**
	 * Get alert List in response
	 * 
	 * @param snmpAlertType
	 * @return JSONArray
	 */
	@Transactional(readOnly = true)
	public JSONArray getSnmpAlertByAlertType(List<SNMPAlertType> snmpAlertType) {

		logger.debug("Inside getSnmpAlertByAlertType" + snmpAlertType);

		ResponseObject responseObject = new ResponseObject();
		JSONArray jAllAlertArr = new JSONArray();
		JSONObject jSnmpAletObj;

		List<SNMPAlert> snmpAlertList = snmpDao.getSnmpAlertsByAlertType(snmpAlertType);

		if (snmpAlertList != null) {

			for (SNMPAlert snmpAlert : snmpAlertList) {
				logger.debug("Found Snmp Alert , Name: " + snmpAlert.getName());

				jSnmpAletObj = new JSONObject();
				jSnmpAletObj.put("id", snmpAlert.getId());
				jSnmpAletObj.put("alertId", snmpAlert.getAlertId());
				jSnmpAletObj.put("name", snmpAlert.getName());
				jSnmpAletObj.put("desc", snmpAlert.getDesc());
				jSnmpAletObj.put(BaseConstants.THRESHOLD, snmpAlert.getThreshold());
				jSnmpAletObj.put("alertType", snmpAlert.getAlertType().getName());

				jAllAlertArr.put(jSnmpAletObj);

			}

		} else {
			responseObject.setSuccess(false);
			logger.debug("No Alert mapped with this server Instance");
		}

		return jAllAlertArr;
	}

	/**
	 * Add Snmp Alerts to client in Wrapper table
	 * 
	 * @param clientId
	 * @param alertIds
	 * @param staffId
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.ADD_SNMP_ALERT_LISTENER_CONFIGURATION,actionType = BaseConstants.CREATE_ACTION, currentEntity = SNMPServerConfig.class, ignorePropList= "")
	public ResponseObject addSnmpAlertsToClient(int clientId, String alertIds, int staffId) {
		ResponseObject responseObject = new ResponseObject();

		if (!StringUtils.isEmpty(alertIds)) {
			String[] alertIdList = alertIds.split(",");
			List<SNMPAlertWrapper> snmpAlertWrapperList = new ArrayList<>();
			SNMPAlertWrapper snmpAlertWrapper;
			SNMPServerConfig snmpClient = snmpDao.findByPrimaryKey(SNMPServerConfig.class, clientId);
			for (int i = 0; i < alertIdList.length; i++) {
				snmpAlertWrapper = addAlert(Integer.parseInt(alertIdList[i]), snmpClient, staffId);
			    //MED-4821 : [SMRewamp] SNMP Threshold : SNMP Client Alerts are not displayed in proper format
				snmpAlertWrapper.setServiceThresholdConfigured(true);
				snmpAlertWrapperList.add(snmpAlertWrapper);
			}
			if (!snmpAlertWrapperList.isEmpty()) {
				snmpClient.setLastUpdatedDate(new Date());
				snmpClient.setLastUpdatedByStaffId(staffId);
				//snmpClient.setStatus(StateEnum.ACTIVE);
				snmpClient.setConfiguredAlerts(snmpAlertWrapperList);
				snmpDao.merge(snmpClient);

			}
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SNMP_CLIENT_ADDED_SUCCESSFULLY);
		} else {
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.SNMP_CLIENT_ADDED_FAIL);
		}

		return responseObject;
	}

	
	
	/**
	 * Update Snmp Alerts to client in Wrapper table
	 * @param clientId
	 * @param alertIds
	 * @param staffId
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SNMP_ALERTS_TO_CLIENT,actionType = BaseConstants.UPDATE_CUSTOM_ACTION, currentEntity = SNMPServerConfig.class, ignorePropList= "serverInstance,configuredAlerts")
	public ResponseObject updateSnmpAlertsToClient(int clientId, String alertIds, int staffId) {
		ResponseObject responseObject = new ResponseObject();
		SNMPServerConfig snmpClient = snmpDao.findByPrimaryKey(SNMPServerConfig.class, clientId);
		if (!StringUtils.isEmpty(alertIds)) {
			String[] alertIdList = alertIds.split(",");
			
			List<SNMPAlertWrapper> snmpAlertWrapperList = new ArrayList<>();
			SNMPAlertWrapper snmpAlertWrapper;

			List<SNMPAlertWrapper> alertWrapperForClient = snmpAlertWrapperDao.getWrapperListByClientId(clientId);
			for (SNMPAlertWrapper alertWrapper : alertWrapperForClient) {
				boolean isSelected = false;
				for (int i = 0; i < alertIdList.length; i++) {
					if (alertWrapper.getAlert().getId() == Integer.parseInt(alertIdList[i])) {
						isSelected = true;
					}
				}
				if (!isSelected) {
					alertWrapper.setLastUpdatedDate(new Date());
					alertWrapper.setLastUpdatedByStaffId(staffId);
					alertWrapper.setStatus(StateEnum.DELETED);
					snmpAlertWrapperList.add(alertWrapper);
				}
			}
			for (int i = 0; i < alertIdList.length; i++) {
				snmpAlertWrapper = updateAlert(Integer.parseInt(alertIdList[i]), snmpClient, staffId);
				//MED-4821 : [SMRewamp] SNMP Threshold : SNMP Client Alerts are not displayed in proper format
				snmpAlertWrapper.setServiceThresholdConfigured(true);
				snmpAlertWrapperList.add(snmpAlertWrapper);
			}
			if (!snmpAlertWrapperList.isEmpty()) {
				snmpClient.setLastUpdatedDate(new Date());
				snmpClient.setLastUpdatedByStaffId(staffId);
				//snmpClient.setStatus(StateEnum.ACTIVE);
				snmpClient.setConfiguredAlerts(snmpAlertWrapperList);
				snmpDao.merge(snmpClient);
			}

			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SNMP_CLIENT_UPDATE_SUCCESS);
		} else {
			deleteAllAlertsBindWithClient(snmpClient, clientId, staffId);
			responseObject.setSuccess(true);
			responseObject.setObject(null);
			responseObject.setResponseCode(ResponseCode.SNMP_CLIENT_UPDATE_SUCCESS);
		}

		return responseObject;
	}

	/**
	 * Add alert in db to client in wrapper table
	 * 
	 * @param alertId
	 * @param snmpClient
	 * @param staffId
	 * @return
	 */
	@Transactional
	@Override
	public SNMPAlertWrapper addAlert(int alertId, SNMPServerConfig snmpClient, int staffId) {

		SNMPAlertWrapper snmpalertWrapper = new SNMPAlertWrapper();
		if (alertId > 0) {

			SNMPAlert snmpAlert = snmpAlertDao.findByPrimaryKey(SNMPAlert.class, alertId);

			if (snmpClient != null && snmpAlert != null) {

				snmpalertWrapper.setLastUpdatedDate(new Date());
				snmpalertWrapper.setLastUpdatedByStaffId(staffId);
				snmpalertWrapper.setStatus(StateEnum.ACTIVE);
				snmpalertWrapper.setAlert(snmpAlert);
				snmpalertWrapper.setListener(snmpClient);

				List<SNMPServerConfig> clientList = snmpDao.getClientListByServerInstanceId(snmpClient.getServerInstance().getId());
				for (SNMPServerConfig Client : clientList) {
					List<SNMPAlertWrapper> alertWrapperList = snmpAlertWrapperDao.getAllWrapperListByAlertIdAndClientId(alertId, Client.getId());
					if (alertWrapperList != null && !alertWrapperList.isEmpty()) {
						List<SNMPServiceThreshold> svcThresholdList = snmpServiceThresholdDao.getThresholdListByWrapperId(alertWrapperList.get(0).getId());
						if (svcThresholdList != null && !svcThresholdList.isEmpty()) {
							List<SNMPServiceThreshold> svcThresholdNewList = new ArrayList<>();
							for (SNMPServiceThreshold svcThreshold : svcThresholdList) {
								snmpServiceThresholdDao.evict(svcThreshold);
								svcThreshold.setId(0);
								svcThreshold.setWrapper(snmpalertWrapper);
								svcThresholdNewList.add(svcThreshold);
							}
							snmpalertWrapper.setServiceThreshold(svcThresholdNewList);							
						}
					}
				}
			}

		}
		return snmpalertWrapper;
	}

	/**
	 * Update alert in db to client in wrapper table
	 * 
	 * @param alertId
	 * @param snmpClient
	 * @param staffId
	 * @return SNMPAlertWrapper
	 */
	public SNMPAlertWrapper updateAlert(int alertId, SNMPServerConfig snmpClient, int staffId) {

		SNMPAlertWrapper snmpalertWrapper = null;
		if (snmpClient != null && alertId != 0) {
			List<SNMPAlertWrapper> wrapperList = snmpAlertWrapperDao.getAllWrapperListByAlertIdAndClientId(alertId, snmpClient.getId());
			if (wrapperList != null && !wrapperList.isEmpty()) {
				snmpalertWrapper = wrapperList.get(0);
				snmpalertWrapper.setStatus(StateEnum.ACTIVE);
				snmpalertWrapper.setLastUpdatedDate(new Date());
				snmpalertWrapper.setLastUpdatedByStaffId(staffId);
			} else {
				snmpalertWrapper = new SNMPAlertWrapper();
			}
			if (alertId > 0) {

				SNMPAlert snmpAlert = snmpAlertDao.findByPrimaryKey(SNMPAlert.class, alertId);

				if (snmpAlert != null) {

					snmpalertWrapper.setLastUpdatedDate(new Date());
					snmpalertWrapper.setLastUpdatedByStaffId(staffId);
					snmpalertWrapper.setStatus(StateEnum.ACTIVE);
					snmpalertWrapper.setAlert(snmpAlert);
					snmpalertWrapper.setListener(snmpClient); 

					List<SNMPServerConfig> clientList = snmpDao.getClientListByServerInstanceId(snmpClient.getServerInstance().getId());
					for (SNMPServerConfig Client : clientList) {
						List<SNMPAlertWrapper> alertWrapperList = snmpAlertWrapperDao.getAllWrapperListByAlertIdAndClientId(alertId, Client.getId());
						if (alertWrapperList != null && !alertWrapperList.isEmpty()) {
							List<SNMPServiceThreshold> svcThresholdList = snmpServiceThresholdDao.getThresholdListByWrapperId(alertWrapperList.get(0)
									.getId());
							if (svcThresholdList != null && !svcThresholdList.isEmpty()) {

								List<SNMPServiceThreshold> svcThresholdNewList = new ArrayList<>();
								for (SNMPServiceThreshold svcThreshold : svcThresholdList) {
								    //MED-4825 : [SNMP] SNMP config : Update SNMP Client is not working
									//Getting Hibernate non-unique object exception while persisting threshold value with exisitng serverInstance
									/*snmpServiceThresholdDao.evict(svcThreshold);
									svcThreshold.setId(0);

									svcThreshold.setWrapper(snmpalertWrapper);
									svcThresholdNewList.add(svcThreshold);*/
									//added below code
									snmpServiceThresholdDao.evict(svcThreshold);
									SNMPServiceThreshold threshold = new SNMPServiceThreshold();
									threshold.setId(0);
									threshold.setService(svcThreshold.getService());
									threshold.setServerInstance(svcThreshold.getServerInstance());
									threshold.setWrapper(snmpalertWrapper);
									threshold.setThreshold(svcThreshold.getThreshold());
									svcThresholdNewList.add(threshold);
								}
								snmpalertWrapper.setServiceThreshold(svcThresholdNewList);								
							}
						}
					}
				}
			}
		}
		return snmpalertWrapper;
	}

	/**
	 * Fetch the total count of snmp client
	 * 
	 * @param serverInstanceId
	 * @return SnmpClienttotalCount
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public long getSnmpClienttotalCount(int serverInstanceId) {
		Map<String, Object> snmpClientConditions = snmpDao.getSnmpClientPaginatedList(serverInstanceId);
		return snmpDao.getQueryCount(SNMPServerConfig.class, (List<Criterion>) snmpClientConditions.get(BaseConstants.CONDITIONS),
				(HashMap<String, String>) snmpClientConditions.get(BaseConstants.ALIASES));
	}

	/**
	 * fetch client list by serverInstance Id
	 * 
	 * @param serverInstanceId
	 * @param startIndex
	 * @param limit
	 * @param sidx
	 * @param sord
	 * @return List
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Map<String, Object>> getSnmpClientPaginatedList(int serverInstanceId, int startIndex, int limit, String sidx, String sord) {
		List<Map<String, Object>> rowList = new ArrayList<>();
		Map<String, Object> row;
		Map<String, Object> conditionsAndAliases = snmpDao.getSnmpClientPaginatedList(serverInstanceId);

		List<SNMPServerConfig> snmpClientList = snmpDao.getSnmpClientPaginatedList(SNMPServerConfig.class,
				(List<Criterion>) conditionsAndAliases.get(BaseConstants.CONDITIONS),
				(HashMap<String, String>) conditionsAndAliases.get(BaseConstants.ALIASES), startIndex, limit, sidx, sord);

		if (snmpClientList != null && !snmpClientList.isEmpty()) {
			for (SNMPServerConfig snmpClient : snmpClientList) {
				row = new HashMap<>();

				row.put("id", snmpClient.getId());
				row.put(BaseConstants.SNMP_CLIENT_ID, snmpClient.getId());
				row.put(BaseConstants.SNMP_CLIENT_NAME, snmpClient.getName());
				row.put(BaseConstants.SNMP_CLIENT_IP, snmpClient.getHostIP());
				row.put(BaseConstants.SNMP_CLIENT_PORT, snmpClient.getPort());
				row.put(BaseConstants.SNMP_CLIENT_VERSION, snmpClient.getVersion().name());
				row.put(BaseConstants.SNMP_CLIENT_COMMUNITY, snmpClient.getCommunity());
				row.put(BaseConstants.SNMP_CLIENT_ADVANCE, snmpClient.getAdvance());
				row.put(BaseConstants.ENABLE, snmpClient.getStatus().toString().trim());
				if(snmpClient.getSnmpV3AuthPassword()!=null){
					snmpClient.setSnmpV3AuthPassword(EliteUtils.decryptData(snmpClient.getSnmpV3AuthPassword()));
				}
				if(snmpClient.getSnmpV3PrivPassword()!=null){
					snmpClient.setSnmpV3PrivPassword(EliteUtils.decryptData(snmpClient.getSnmpV3PrivPassword()));
				}
				row.put(BaseConstants.SNMP_CLIENT_V3_AUTH_ALGO, snmpClient.getSnmpV3AuthAlgorithm());
				row.put(BaseConstants.SNMP_CLIENT_V3_AUTH_PASSWORD, snmpClient.getSnmpV3AuthPassword());
				row.put(BaseConstants.SNMP_CLIENT_V3_PRIV_ALGO, snmpClient.getSnmpV3PrivAlgorithm());
				row.put(BaseConstants.SNMP_CLIENT_V3_PRIV_PASSWORD, snmpClient.getSnmpV3PrivPassword());
				

				row.put(BaseConstants.EDIT, "Edit");
				row.put("alertMapping", "alertMapping");

				rowList.add(row);

			}
		}
		return rowList;
	}

	/**
	 * Update Client details in database
	 * 
	 * @param snmpClient
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SNMP_CLIENT_CONFIGURATION,actionType = BaseConstants.UPDATE_ACTION, currentEntity = SNMPServerConfig.class, ignorePropList= "configuredAlerts,serverInstance,portOffset,type")
	public ResponseObject updateSnmpClientDetail(SNMPServerConfig snmpClient) {
		ResponseObject responseObject = new ResponseObject();

		if (isSnmpServerNameUniqueForUpdate(snmpClient.getId(), snmpClient.getName())) {
			if (snmpClient.getId() != 0) {
				SNMPServerConfig snmpServerDB = snmpDao.findByPrimaryKey(SNMPServerConfig.class, snmpClient.getId());

				if (snmpServerDB != null) {

					snmpServerDB.setName(snmpClient.getName());
					snmpServerDB.setHostIP(snmpClient.getHostIP());
					snmpServerDB.setPort(snmpClient.getPort());
					snmpServerDB.setVersion(snmpClient.getVersion());
					snmpServerDB.setAdvance(snmpClient.getAdvance());
					snmpServerDB.setCommunity(snmpClient.getCommunity());
					snmpServerDB.setLastUpdatedByStaffId(snmpClient.getLastUpdatedByStaffId());
					snmpServerDB.setLastUpdatedDate(new Date());
					if(snmpClient.getSnmpV3AuthPassword()!=null){
						snmpClient.setSnmpV3AuthPassword(EliteUtils.encryptData(snmpClient.getSnmpV3AuthPassword()));
					}
					if(snmpClient.getSnmpV3PrivPassword()!=null){
						snmpClient.setSnmpV3PrivPassword(EliteUtils.encryptData(snmpClient.getSnmpV3PrivPassword()));
					}
					snmpServerDB.setSnmpV3AuthAlgorithm(snmpClient.getSnmpV3AuthAlgorithm());
					snmpServerDB.setSnmpV3AuthPassword(snmpClient.getSnmpV3AuthPassword());
					snmpServerDB.setSnmpV3PrivAlgorithm(snmpClient.getSnmpV3PrivAlgorithm());
					snmpServerDB.setSnmpV3PrivPassword(snmpClient.getSnmpV3PrivPassword());

					snmpDao.merge(snmpServerDB);
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.SNMP_CLIENT_UPDATE_SUCCESS);
				}

			} else {
				logger.debug("Snmp Client not found from DB inside updateSnmpClientDetail");
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.SNMP_CLIENT_UPDATE_FAIL);
			}
		} else {
			logger.debug("inside updateSnmpServernDetail : duplicate snmp server name found:" + snmpClient.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_SNMP_CLIENT_NAME);
		}

		return responseObject;
	}

	/**
	 * Get Alerts List related to serviced mapped to service Instance
	 * 
	 * @param clientId
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	public ResponseObject fetchAlertsForUpdateClient(int clientId) {
		ResponseObject responseObject = new ResponseObject();

		SNMPServerConfig snmpClientDB = snmpDao.findByPrimaryKey(SNMPServerConfig.class, clientId);

		if (snmpClientDB != null) {

			List<String> serviceType = getServiceTypeforServerInstance(snmpClientDB.getServerInstance().getId());
			List<SNMPAlertType> alertType;
			if (serviceType == null || serviceType.isEmpty()) {

				serviceType = new ArrayList<>();
			}
			serviceType.add(BaseConstants.SERVER_INSTANCE);
			

			alertType = getAlertTypeforServerInstance(serviceType);

			JSONArray jAllAlertArray = getSnmpAlertByAlertType(alertType);
			JSONArray jConfAlertArray = getClientConfiguredAlerts(clientId);
			JSONObject objAlert = new JSONObject();
			objAlert.put("alertList", jAllAlertArray);
			objAlert.put("configuredAlertList", jConfAlertArray);

			responseObject.setObject(objAlert);
			responseObject.setSuccess(true);

		}

		else {
			logger.debug("Snmp Client not found from DB");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SNMP_CLIENT_UPDATE_FAIL);
		}

		return responseObject;
	}

	/**
	 * Get configured alert list for client
	 * 
	 * @param clientId
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	public ResponseObject getConfiguredAlertsByClientId(int clientId) {
		ResponseObject responseObject = new ResponseObject();
		if (!StringUtils.isEmpty(clientId)) {

			JSONArray jConfAlertArray = getClientConfiguredAlerts(clientId);
			JSONObject objAlert = new JSONObject();
			objAlert.put("configuredAlertList", jConfAlertArray);

			responseObject.setObject(objAlert);
			responseObject.setSuccess(true);

		}

		else {
			logger.debug("Snmp Client not found from DB");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SNMP_CLIENT_UPDATE_FAIL);
		}
		return responseObject;
	}

	/**
	 * Get Alerts configured for specific client
	 * 
	 * @param clientId
	 * @return JSONArray
	 */
	@Transactional
	@Override
	public JSONArray getClientConfiguredAlerts(int clientId) {

		SNMPServerConfig snmpClient = snmpDao.getConfiguredAlertListByClientId(clientId);
		List<SNMPAlertWrapper> alertWrapperList = snmpClient.getConfiguredAlerts();
		List<SNMPAlert> alertList = new ArrayList<>();
		JSONObject jSnmpAletObj;
		JSONArray jAllAlertArr = new JSONArray();
		for (SNMPAlertWrapper snmpalertWrapper : alertWrapperList) {

			SNMPAlert snmpalert = snmpalertWrapper.getAlert();
			alertList.add(snmpalert);
		}

		logger.debug("configured alert List: " + alertList);

		if (!alertList.isEmpty()) {

			for (SNMPAlert snmpAlert : alertList) {
				logger.debug("Found Snmp Alert , Name: " + snmpAlert.getName());

				jSnmpAletObj = new JSONObject();
				jSnmpAletObj.put("id", snmpAlert.getId());
				jSnmpAletObj.put("alertId", snmpAlert.getAlertId());
				jSnmpAletObj.put("name", snmpAlert.getName());
				jSnmpAletObj.put("desc", snmpAlert.getDesc());
				jSnmpAletObj.put(BaseConstants.THRESHOLD, snmpAlert.getThreshold());
				jSnmpAletObj.put("alertType", snmpAlert.getAlertType().getId());

				jAllAlertArr.put(jSnmpAletObj);
			}
		}
		return jAllAlertArr;

	}

	/**
	 * Method gives total count of Alerts In db
	 * 
	 * @return SnmpAlerttotalCount
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	@Override
	public long getSnmpAlerttotalCount() {
		Map<String, Object> snmpAlertConditions = snmpAlertDao.getSnmpAlertPaginatedList();
		return snmpAlertDao.getQueryCount(SNMPAlert.class, (List<Criterion>) snmpAlertConditions.get(BaseConstants.CONDITIONS),
				(HashMap<String, String>) snmpAlertConditions.get(BaseConstants.ALIASES));
	}

	/**
	 * Get Snmp Alert paginated list for alert grid
	 * 
	 * @param serverInstanceId
	 * @param startIndex
	 * @param limit
	 * @param sidx
	 * @param sord
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public ResponseObject getSnmpAlertPaginatedList(int serverInstanceId) {
		Map<String, Object> conditionsAndAliases = snmpAlertDao.getSnmpAlertPaginatedList();
		ResponseObject responseObject = new ResponseObject();
		List<SNMPAlert> snmpAlertList = snmpAlertDao.getSnmpAlertPaginatedList(SNMPAlert.class,
				(List<Criterion>) conditionsAndAliases.get(BaseConstants.CONDITIONS),
				(HashMap<String, String>) conditionsAndAliases.get(BaseConstants.ALIASES));
		JSONObject jSnmpAletObj;
		JSONArray jAllAlertArr = new JSONArray();
		if (!snmpAlertList.isEmpty()) {
			for (SNMPAlert snmpAlert : snmpAlertList) {
				jSnmpAletObj = new JSONObject();

				jSnmpAletObj.put("id", snmpAlert.getId());
				jSnmpAletObj.put(BaseConstants.SNMP_ALERT_ID, snmpAlert.getId());
				jSnmpAletObj.put(BaseConstants.SNMP_ALERT_NAME, snmpAlert.getName());
				jSnmpAletObj.put(BaseConstants.SNMP_ALERT_DESCRIPTION, snmpAlert.getDesc());
				jSnmpAletObj.put(BaseConstants.SNMP_ALERT_SERVICETHRESHOLD, snmpAlert.getThreshold());
				jSnmpAletObj.put(BaseConstants.SNMP_ALERT_TYPE, snmpAlert.getAlertType().getName());
				String serviceThresholdCheck = isServiceThresholdConfiguredCheck(serverInstanceId, snmpAlert.getId());
				jSnmpAletObj.put(BaseConstants.SNMP_ALERT_SERVICETHRESHOLD_LABEL, serviceThresholdCheck);
				jSnmpAletObj.put(BaseConstants.EDIT, "Edit");
				if(BaseConstants.TRUE.equals(serviceThresholdCheck)) {
					jAllAlertArr.put(jSnmpAletObj);
				}
			}
			JSONObject clientId = new JSONObject();
			clientId.put("alertList", jAllAlertArr);

			responseObject.setObject(clientId);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SNMP_CLIENT_ADDED_SUCCESSFULLY);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SNMP_CLIENT_ADDED_FAIL);
		}
		return responseObject;
	}
	

	/**
	 * Get Service threshold for particular alert
	 * 
	 * @param serverInstanceId
	 * @param alertId
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	public ResponseObject getServiceWithServiceThreshold(int serverInstanceId, int alertId) {
		ResponseObject responseObject = new ResponseObject();
		SNMPAlert snmpAlert = snmpAlertDao.findByPrimaryKey(SNMPAlert.class, alertId);
		List<Service> serviceList = servicesService.getServicesforServerInstance(serverInstanceId);
		List<Service> confserviceList = new ArrayList<>();
		JSONArray jAllAlertSvcArr;
		if (snmpAlert != null) {
			if ((BaseConstants.SERVER_INSTANCE).equals(snmpAlert.getAlertType().getAlias())) {
				logger.debug("ServerInstance Type of alert");
				jAllAlertSvcArr = getThresholdForServerInstance(serverInstanceId, alertId, snmpAlert);
				JSONObject objSvc = new JSONObject();
				objSvc.put(BaseConstants.SVCLIST, jAllAlertSvcArr);
				responseObject.setObject(objSvc);
				responseObject.setSuccess(true);

			} else if ((BaseConstants.GENERIC).equals(snmpAlert.getAlertType().getAlias())) {
				logger.debug("Generic Type of alertconfigserviceList");
				jAllAlertSvcArr = getThresholdForGeneric(serverInstanceId, alertId, snmpAlert, serviceList);
				JSONObject objSvc = new JSONObject();
				objSvc.put(BaseConstants.SVCLIST, jAllAlertSvcArr);
				responseObject.setObject(objSvc);
				responseObject.setSuccess(true);
			} else if (serviceList != null && !serviceList.isEmpty()) {
				for (Service configserviceList : serviceList) {

					if ((snmpAlert.getAlertType().getAlias()).equals(configserviceList.getSvctype().getAlias())) {
						confserviceList.add(configserviceList);
					}
				}
				jAllAlertSvcArr = getThresholdForService(confserviceList, serverInstanceId, alertId, snmpAlert);
				JSONObject objSvc = new JSONObject();
				objSvc.put(BaseConstants.SVCLIST, jAllAlertSvcArr);
				responseObject.setObject(objSvc);
				responseObject.setSuccess(true);
			} else {
				logger.debug("No service found for this alert nor it is serverinstance type or generic type of alert.." + snmpAlert);
				responseObject.setSuccess(false);
			}
		} else {
			logger.debug("No alert  object found for this alert in db..");
			responseObject.setSuccess(false);
		}

		return responseObject;

	}

	/**
	 * Check whether service threshold is configured or not
	 * 
	 * @param serverInstanceId
	 * @param alertId
	 * @return isServiceConfiguredCheck
	 */
	public boolean isServiceConfiguredCheck(int serverInstanceId, int alertId) {
		boolean isSvcConfigured = false;
		List<SNMPServerConfig> snmpClientList = snmpDao.getClientListByServerInstanceId(serverInstanceId);

		for (SNMPServerConfig snmpClient : snmpClientList) {
			int snmpClientId = snmpClient.getId();
			List<SNMPAlertWrapper> snmpalertWrapperList = snmpAlertWrapperDao.getWrapperListByAlertIdAndClientId(alertId, snmpClientId);
			if (snmpalertWrapperList != null && !snmpalertWrapperList.isEmpty()) {
				isSvcConfigured = snmpalertWrapperList.get(0).isServiceThresholdConfigured();
			}
		}
		return isSvcConfigured;

	}

	public String isServiceThresholdConfiguredCheck(int serverInstanceId, int alertId) {
		boolean isSvcConfigured ;
		String check = "disabled";
		List<SNMPServerConfig> snmpClientList = snmpDao.getClientListByServerInstanceId(serverInstanceId);

		for (SNMPServerConfig snmpClient : snmpClientList) {
			int snmpClientId = snmpClient.getId();
			List<SNMPAlertWrapper> snmpalertWrapperList = snmpAlertWrapperDao.getWrapperListByAlertIdAndClientId(alertId, snmpClientId);
			if (snmpalertWrapperList != null && !snmpalertWrapperList.isEmpty()) {
				isSvcConfigured = snmpalertWrapperList.get(0).isServiceThresholdConfigured();
				if (isSvcConfigured) {
					check = "true";
				} else {
					check = "false";
				}
				break;
			} else {
				check = "disabled";
			}

		}

		return check;

	}

	/**
	 * Update Status of Snmp Client
	 * 
	 * @param snmpClientId
	 * @param snmpStatus
	 * @return ResponseObject
	 * @throws CloneNotSupportedException
	 */
	@Transactional
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_SNMP_CLIENT_STATUS, actionType = BaseConstants.UPDATE_CUSTOM_ACTION, currentEntity = SNMPServerConfig.class, ignorePropList= "configuredAlerts,serverInstance")
	public ResponseObject changeSnmpClientStatus(int snmpClientId, String snmpStatus) throws CloneNotSupportedException {
		ResponseObject responseObject = new ResponseObject();

		SNMPServerConfig snmpServer = snmpDao.findByPrimaryKey(SNMPServerConfig.class, snmpClientId);
		if (snmpServer != null) {

			if (StateEnum.ACTIVE.name().equals(snmpStatus.trim())) {
				snmpServer.setStatus(StateEnum.ACTIVE);
			} else {
				snmpServer.setStatus(StateEnum.INACTIVE);
			}

			snmpDao.merge(snmpServer);

			responseObject.setResponseCode(ResponseCode.SNMP_CLIENT_UPDATE_SUCCESS);
			responseObject.setSuccess(true);

		} else {
			responseObject.setResponseCode(ResponseCode.SNMP_CLIENT_UPDATE_FAIL);
			responseObject.setSuccess(false);
		}

		return responseObject;
	}

	/**
	 * Delete Snmp Client
	 * 
	 * @param snmpClientId
	 * @param staffId
	 * @return ResponseObject
	 * @throws CloneNotSupportedException
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.DELETE_SNMP_CLIENT_CONFIGURATION,actionType = BaseConstants.DELETE_ACTION , currentEntity = SNMPServerConfig.class, ignorePropList= "")
	public ResponseObject deleteSnmpClient(int snmpClientId, int staffId) throws CloneNotSupportedException {
		ResponseObject responseObject = new ResponseObject();

		SNMPServerConfig snmpClient = snmpDao.findByPrimaryKey(SNMPServerConfig.class, snmpClientId);
		if (snmpClient != null) {
			snmpClient.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,snmpClient.getName()));
			snmpClient.setStatus(StateEnum.DELETED);
			snmpClient.setLastUpdatedByStaffId(staffId);
			snmpClient.setLastUpdatedDate(new Date());
			iterateSnmpAlertWrapper(snmpClient.getServerInstance(), snmpClient, null, false);
			snmpDao.merge(snmpClient);

			responseObject.setSuccess(true);
			responseObject.setObject(snmpClient);
			responseObject.setResponseCode(ResponseCode.SNMP_CLIENT_DELETE_SUCCESS);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SNMP_CLIENT_DELETE_FAIL);
		}
		return responseObject;
	}

	/**
	 * Update Service threshold value in database
	 * 
	 * @param serverInstanceId
	 * @param alertId
	 * @param alertsvcList
	 * @param selectedAlertType
	 * @param configAlertThreshold
	 * @return ResponseObject
	 */
	@Override
	@Transactional
	public ResponseObject updateServiceThreshold(int serverInstanceId, int alertId, String alertsvcList, String selectedAlertType) {
		ResponseObject responseObject = new ResponseObject();

		logger.debug("Alert type::" + selectedAlertType);

		List<SNMPServerConfig> snmpClientList = snmpDao.getClientListByServerInstanceId(serverInstanceId);
		logger.debug("alert type::" + selectedAlertType);

		if (BaseConstants.SERVER_INSTANCE.equals(selectedAlertType)) {
			logger.debug("Inside If in updateServiceThreshold: " + selectedAlertType);
			responseObject = updateServiceThresholdForServerInstance(serverInstanceId, alertId, alertsvcList, snmpClientList);
		} else if (BaseConstants.GENERIC.equals(selectedAlertType)) {
			logger.debug("Inside If in updateServiceThreshold: " + selectedAlertType);
			responseObject = updateServiceThresholdForGeneric(serverInstanceId, alertId, alertsvcList, snmpClientList);
		}

		else if ("SVC".equals(selectedAlertType)) {
			responseObject = updateServiceThresholdForService(serverInstanceId, alertId, alertsvcList, snmpClientList);
		}

		else {
			logger.debug("No service found inside updateServiceThreshold..");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SNMP_SERVICE_THRESHOLD_FAIL);
		}

		return responseObject;
	}

	/**
	 * Get Service threshold value of Service type Alert
	 * 
	 * @param confserviceList
	 * @param serverInstanceId
	 * @param alertId
	 * @param snmpAlert
	 * @return JSONArray
	 */
	public JSONArray getThresholdForService(List<Service> confserviceList, int serverInstanceId, int alertId, SNMPAlert snmpAlert) {

		JSONObject jSvcObj;
		JSONArray jAllAlertSvcArr = new JSONArray();
		if (!confserviceList.isEmpty()) {

			for (Service service : confserviceList) {
				logger.debug("Found Service , Name: " + service.getName());

				jSvcObj = new JSONObject();
				jSvcObj.put("id", service.getId());
				jSvcObj.put(BaseConstants.SERVINSTANCEID, service.getServInstanceId());
				jSvcObj.put("name", service.getName());
				if (!isServiceConfiguredCheck(serverInstanceId, alertId)) {

					jSvcObj.put(BaseConstants.THRESHOLD, snmpAlert.getThreshold());
					jSvcObj.put(BaseConstants.ALERT_NAME, snmpAlert.getName());
					jSvcObj.put(BaseConstants.THRESHOLDID, 0);
					jSvcObj.put(BaseConstants.THRESHOLD_TYPE, "SVC");
				} else {
					List<SNMPServerConfig> clientList = snmpDao.getClientListByServerInstanceId(serverInstanceId);
					for (SNMPServerConfig snmpClient : clientList) {

						List<SNMPAlertWrapper> snmpWrapperList = snmpAlertWrapperDao.getWrapperListByAlertIdAndClientId(alertId, snmpClient.getId());
						if (snmpWrapperList != null && !snmpWrapperList.isEmpty()) {
							List<SNMPServiceThreshold> snmpSvcThresholdList = snmpServiceThresholdDao.getSvcThresholdListByWrapperId(snmpWrapperList
									.get(0).getId(), service.getId());
							if (snmpSvcThresholdList != null && !snmpSvcThresholdList.isEmpty()) {
								jSvcObj.put(BaseConstants.THRESHOLD, snmpSvcThresholdList.get(0).getThreshold());
								jSvcObj.put(BaseConstants.THRESHOLDID, snmpSvcThresholdList.get(0).getId());
								jSvcObj.put(BaseConstants.THRESHOLD_TYPE, "SVC");
								jSvcObj.put(BaseConstants.ALERT_NAME, snmpAlert.getName());
							} else {
								jSvcObj.put(BaseConstants.THRESHOLD, snmpAlert.getThreshold());
								jSvcObj.put(BaseConstants.ALERT_NAME, snmpAlert.getName());
								jSvcObj.put(BaseConstants.THRESHOLDID, 0);
								jSvcObj.put(BaseConstants.THRESHOLD_TYPE, "SVC");
							}
						}
					}
				}
				jAllAlertSvcArr.put(jSvcObj);
			}
		}
		return jAllAlertSvcArr;

	}

	/**
	 * Get Service threshold value of Server Instance type Alert
	 * 
	 * @param serverInstanceId
	 * @param alertId
	 * @param snmpAlert
	 * @return JSONArray
	 */
	public JSONArray getThresholdForServerInstance(int serverInstanceId, int alertId, SNMPAlert snmpAlert) {

		logger.debug("Inside getThresholdForServerInstance");

		ServerInstance serverInstance = serverInstanceDao.findByPrimaryKey(ServerInstance.class, serverInstanceId);

		JSONObject jSvcObj = new JSONObject();
		jSvcObj.put("id", serverInstance.getId());
		jSvcObj.put(BaseConstants.SERVINSTANCEID, serverInstance.getId());
		jSvcObj.put("name", serverInstance.getName());
		JSONArray jAllAlertSvcArr = new JSONArray();

		if (!isServiceConfiguredCheck(serverInstanceId, alertId)) {
			logger.debug("Inside getThresholdForServerInstance: if check not isServiceConfiguredCheck");

			jSvcObj.put(BaseConstants.THRESHOLD, snmpAlert.getThreshold());
			jSvcObj.put(BaseConstants.ALERT_NAME, snmpAlert.getName());
			jSvcObj.put(BaseConstants.THRESHOLDID, 0);
			jSvcObj.put(BaseConstants.THRESHOLD_TYPE, BaseConstants.SERVER_INSTANCE);
		} else {
			logger.debug("Inside getThresholdForServerInstance: else check not isServiceConfiguredCheck");

			List<SNMPServerConfig> clientList = snmpDao.getClientListByServerInstanceId(serverInstanceId);

			for (SNMPServerConfig snmpClient : clientList) {
				List<SNMPAlertWrapper> snmpWrapperList = snmpAlertWrapperDao.getWrapperListByAlertIdAndClientId(alertId, snmpClient.getId());
				if (snmpWrapperList != null && !snmpWrapperList.isEmpty()) {
					List<SNMPServiceThreshold> snmpSvcThresholdList = snmpServiceThresholdDao.getSvcThresholdListByWrapperIdAndInstanceId(
							snmpWrapperList.get(0).getId(), serverInstanceId);
					if (snmpSvcThresholdList != null && !snmpSvcThresholdList.isEmpty()) {
						jSvcObj.put(BaseConstants.THRESHOLD, snmpSvcThresholdList.get(0).getThreshold());
						jSvcObj.put(BaseConstants.THRESHOLDID, snmpSvcThresholdList.get(0).getId());
						jSvcObj.put(BaseConstants.ALERT_NAME, snmpAlert.getName());
					} else {
						jSvcObj.put(BaseConstants.THRESHOLD, snmpAlert.getThreshold());
						jSvcObj.put(BaseConstants.THRESHOLDID, 0);
						jSvcObj.put(BaseConstants.ALERT_NAME, snmpAlert.getName());
					}
				}
				jSvcObj.put(BaseConstants.THRESHOLD_TYPE, BaseConstants.SERVER_INSTANCE);
			}
		}

		jAllAlertSvcArr.put(jSvcObj);
		logger.debug("return message for getThresholdForServerInstance" + jAllAlertSvcArr);
		return jAllAlertSvcArr;

	}

	/**
	 * Get Service threshold value of Generic type Alert
	 * 
	 * @param serverInstanceId
	 * @param alertId
	 * @param snmpAlert
	 * @return JSONArray
	 */
	public JSONArray getThresholdForGeneric(int serverInstanceId, int alertId, SNMPAlert snmpAlert, List<Service> confserviceList) {

		logger.debug("Inside getThresholdForGeneric");

		JSONObject jSvcObj;
		JSONArray jAllAlertSvcArr = new JSONArray();
		if (!confserviceList.isEmpty()) {

			for (Service service : confserviceList) {
				logger.debug("Found Service , Name: " + service.getName());

				jSvcObj = new JSONObject();
				jSvcObj.put("id", service.getId());
				jSvcObj.put(BaseConstants.SERVINSTANCEID, service.getServInstanceId());
				jSvcObj.put("name", service.getName());
				if (!isServiceConfiguredCheck(serverInstanceId, alertId)) {

					jSvcObj.put(BaseConstants.THRESHOLD, snmpAlert.getThreshold());
					jSvcObj.put(BaseConstants.ALERT_NAME, snmpAlert.getName());
					jSvcObj.put(BaseConstants.THRESHOLDID, 0);
					jSvcObj.put(BaseConstants.THRESHOLD_TYPE, BaseConstants.GENERIC_TYPE);
				} else {
					List<SNMPServerConfig> clientList = snmpDao.getClientListByServerInstanceId(serverInstanceId);
					for (SNMPServerConfig snmpClient : clientList) {


						List<SNMPAlertWrapper> snmpWrapperList = snmpAlertWrapperDao.getWrapperListByAlertIdAndClientId(alertId, snmpClient.getId());
						if (snmpWrapperList != null && !snmpWrapperList.isEmpty()) {
							List<SNMPServiceThreshold> snmpSvcThresholdList = snmpServiceThresholdDao.getSvcThresholdListByWrapperId(snmpWrapperList
									.get(0).getId(), service.getId());
							if (snmpSvcThresholdList != null && !snmpSvcThresholdList.isEmpty()) {
								jSvcObj.put(BaseConstants.THRESHOLD, snmpSvcThresholdList.get(0).getThreshold());
								jSvcObj.put(BaseConstants.THRESHOLDID, snmpSvcThresholdList.get(0).getId());
								jSvcObj.put(BaseConstants.THRESHOLD_TYPE, BaseConstants.GENERIC_TYPE);
								jSvcObj.put(BaseConstants.ALERT_NAME, snmpAlert.getName());
							} else {
								jSvcObj.put(BaseConstants.THRESHOLD, snmpAlert.getThreshold());
								jSvcObj.put(BaseConstants.ALERT_NAME, snmpAlert.getName());
								jSvcObj.put(BaseConstants.THRESHOLDID, 0);
								jSvcObj.put(BaseConstants.THRESHOLD_TYPE, BaseConstants.GENERIC_TYPE);
							}

						}
					}
				}
				jAllAlertSvcArr.put(jSvcObj);
			}
		}
		logger.debug("return message for getThresholdForGeneric" + jAllAlertSvcArr);
		return jAllAlertSvcArr;

	}

	/**
	 * Update Service threshold value of Service type Alert
	 * 
	 * @param serverInstanceId
	 * @param alertId
	 * @param alertsvcList
	 * @param snmpClientList
	 * @return responseObject
	 */
	public ResponseObject updateServiceThresholdForService(int serverInstanceId, int alertId, String alertsvcList,
			List<SNMPServerConfig> snmpClientList) {

		logger.debug("Inside updateServiceThresholdForService");
		ResponseObject responseObject = new ResponseObject();
		JSONArray jsvcListArr = new JSONArray(alertsvcList);
		for (int index = 0; index < jsvcListArr.length(); index++) {
			JSONObject jsvcObj = jsvcListArr.getJSONObject(index);
			Service service = servicesDao.findByPrimaryKey(Service.class, jsvcObj.getInt("svcId"));
			if (service != null) {

				for (SNMPServerConfig snmpClient : snmpClientList) {
					int snmpClientId = snmpClient.getId();
					List<SNMPAlertWrapper> snmpAlertWrappers = snmpAlertWrapperDao.getWrapperListByAlertIdAndClientId(alertId, snmpClientId);
					List<SNMPServiceThreshold> snmpSvcThresholdList = new ArrayList<>();
					if (snmpAlertWrappers != null && !snmpAlertWrappers.isEmpty()) {
						SNMPAlertWrapper snmpAlertWrapper = snmpAlertWrappers.get(0);
						SNMPServiceThreshold snmpSvcThgresholdobj = null;
						if (jsvcObj.getInt(BaseConstants.THRESHOLDID) > 0) {
							snmpSvcThgresholdobj = snmpServiceThresholdDao.findByPrimaryKey(SNMPServiceThreshold.class,
									jsvcObj.getInt(BaseConstants.THRESHOLDID));
						}
						if (snmpSvcThgresholdobj != null) {
							snmpSvcThgresholdobj.setThreshold(jsvcObj.getInt(BaseConstants.THRESHOLD));
							snmpSvcThresholdList.add(snmpSvcThgresholdobj);
							snmpAlertWrapper.setServiceThreshold(snmpSvcThresholdList);
							snmpAlertWrapper.setServiceThresholdConfigured(true);
							snmpAlertWrapperDao.merge(snmpAlertWrapper);
						}

						else {
							SNMPServiceThreshold snmpSvcThreshold = new SNMPServiceThreshold();

							snmpSvcThreshold.setThreshold(jsvcObj.getInt(BaseConstants.THRESHOLD));
							snmpSvcThreshold.setServerInstance(snmpAlertWrapper.getListener().getServerInstance());
							snmpSvcThreshold.setService(service);
							snmpSvcThreshold.setWrapper(snmpAlertWrapper);

							snmpSvcThresholdList.add(snmpSvcThreshold);
							snmpAlertWrapper.setServiceThreshold(snmpSvcThresholdList);
							snmpAlertWrapper.setServiceThresholdConfigured(true);
							snmpAlertWrapperDao.merge(snmpAlertWrapper);
						}
					} else {
						logger.debug("No wrapper found for alert and client inside updateServiceThreshold.....");
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.SNMP_SERVICE_THRESHOLD_FAIL);
					}
				}
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.SNMP_SERVICE_THRESHOLD_SUCCESS);
			}
		}
		return responseObject;

	}

	/**
	 * Update Service threshold value of Server Instance type Alert
	 * 
	 * @param serverInstanceId
	 * @param alertId
	 * @param alertsvcList
	 * @param snmpClientList
	 * @return responseObject
	 */
	public ResponseObject updateServiceThresholdForServerInstance(int serverInstanceId, int alertId, String alertsvcList,
			List<SNMPServerConfig> snmpClientList) {

		logger.debug("Inside updateServiceThresholdForServerInstance");

		ResponseObject responseObject = new ResponseObject();
		JSONArray jsvcListArr = new JSONArray(alertsvcList);

		JSONObject jsvcObj = jsvcListArr.getJSONObject(0);
		ServerInstance serverInstance = serverInstanceDao.findByPrimaryKey(ServerInstance.class, serverInstanceId);
		if (serverInstance != null) {

			for (SNMPServerConfig snmpClient : snmpClientList) {
				int snmpClientId = snmpClient.getId();
				List<SNMPAlertWrapper> snmpAlertWrappers = snmpAlertWrapperDao.getWrapperListByAlertIdAndClientId(alertId, snmpClientId);
				List<SNMPServiceThreshold> snmpSvcThresholdList = new ArrayList<>();
				if (snmpAlertWrappers != null && !snmpAlertWrappers.isEmpty()) {
					SNMPAlertWrapper snmpAlertWrapper = snmpAlertWrappers.get(0);
					SNMPServiceThreshold snmpSvcThgresholdobj = null;
					if (jsvcObj.getInt(BaseConstants.THRESHOLDID) > 0) {
						snmpSvcThgresholdobj = snmpServiceThresholdDao.findByPrimaryKey(SNMPServiceThreshold.class,
								jsvcObj.getInt(BaseConstants.THRESHOLDID));
					}
					if (snmpSvcThgresholdobj != null) {
						snmpSvcThgresholdobj.setThreshold(jsvcObj.getInt(BaseConstants.THRESHOLD));
						snmpAlertWrapper.setServiceThreshold(snmpSvcThresholdList);
						snmpAlertWrapper.setServiceThresholdConfigured(true);
						snmpAlertWrapperDao.merge(snmpAlertWrapper);
					}

					else {
						SNMPServiceThreshold snmpSvcThreshold = new SNMPServiceThreshold();

						snmpSvcThreshold.setThreshold(jsvcObj.getInt(BaseConstants.THRESHOLD));

						snmpSvcThreshold.setServerInstance(serverInstance);
						;
						snmpSvcThreshold.setWrapper(snmpAlertWrapper);

						snmpSvcThresholdList.add(snmpSvcThreshold);
						snmpAlertWrapper.setServiceThreshold(snmpSvcThresholdList);
						snmpAlertWrapper.setServiceThresholdConfigured(true);
						snmpAlertWrapperDao.merge(snmpAlertWrapper);
					}
				} else {
					logger.debug("No wrapper found for alert and client inside updateServiceThreshold..");
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.SNMP_SERVICE_THRESHOLD_FAIL);
				}
			}
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SNMP_SERVICE_THRESHOLD_SUCCESS);
		}

		return responseObject;

	}

	/**
	 * Update Service threshold value of Generic type Alert
	 * 
	 * @param serverInstanceId
	 * @param alertId
	 * @param alertsvcList
	 * @param snmpClientList
	 * @param configAlertThreshold
	 * @return responseObject
	 */
	public ResponseObject updateServiceThresholdForGeneric(int serverInstanceId, int alertId, String alertsvcList,
			List<SNMPServerConfig> snmpClientList) {

		logger.debug("Inside updateServiceThresholdForGeneric");

		ResponseObject responseObject = new ResponseObject();
		JSONArray jsvcListArr = new JSONArray(alertsvcList);
		for (int index = 0; index < jsvcListArr.length(); index++) {
			JSONObject jsvcObj = jsvcListArr.getJSONObject(index);
			Service service = servicesDao.findByPrimaryKey(Service.class, jsvcObj.getInt("svcId"));
			if (service != null) {

				for (SNMPServerConfig snmpClient : snmpClientList) {
					int snmpClientId = snmpClient.getId();
					List<SNMPAlertWrapper> snmpAlertWrappers = snmpAlertWrapperDao.getWrapperListByAlertIdAndClientId(alertId, snmpClientId);
					List<SNMPServiceThreshold> snmpSvcThresholdList = new ArrayList<>();
					if (snmpAlertWrappers != null && !snmpAlertWrappers.isEmpty()) {
						SNMPAlertWrapper snmpAlertWrapper = snmpAlertWrappers.get(0);
						SNMPServiceThreshold snmpSvcThgresholdobj = null;
						if (jsvcObj.getInt(BaseConstants.THRESHOLDID) > 0) {
							snmpSvcThgresholdobj = snmpServiceThresholdDao.findByPrimaryKey(SNMPServiceThreshold.class,
									jsvcObj.getInt(BaseConstants.THRESHOLDID));
						}
						if (snmpSvcThgresholdobj != null) {
							snmpSvcThgresholdobj.setThreshold(jsvcObj.getInt(BaseConstants.THRESHOLD));
							snmpAlertWrapper.setServiceThreshold(snmpSvcThresholdList);
							snmpAlertWrapper.setServiceThresholdConfigured(true);
							snmpAlertWrapperDao.merge(snmpAlertWrapper);
						}

						else {
							SNMPServiceThreshold snmpSvcThreshold = new SNMPServiceThreshold();

							snmpSvcThreshold.setThreshold(jsvcObj.getInt(BaseConstants.THRESHOLD));
							snmpSvcThreshold.setServerInstance(snmpAlertWrapper.getListener().getServerInstance());
							snmpSvcThreshold.setService(service);
							snmpSvcThreshold.setWrapper(snmpAlertWrapper);

							snmpSvcThresholdList.add(snmpSvcThreshold);
							snmpAlertWrapper.setServiceThreshold(snmpSvcThresholdList);
							snmpAlertWrapper.setServiceThresholdConfigured(true);
							snmpAlertWrapperDao.merge(snmpAlertWrapper);
						}
					} else {
						logger.debug("No wrapper found for alert and client inside updateServiceThreshold.....");
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.SNMP_SERVICE_THRESHOLD_FAIL);
					}
				}
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.SNMP_SERVICE_THRESHOLD_SUCCESS);
			}
		}
		return responseObject;

	}

	/**
	 * Update Snmp Alert Details
	 * 
	 * @param alertDetails
	 * @return responseObject
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_SNMP_ALERT_LIST,actionType = BaseConstants.UPDATE_ACTION , currentEntity = SNMPAlert.class, ignorePropList= "threshold")
	public ResponseObject updateSnmpAlertDetails(SNMPAlert snmpAlert) {
		ResponseObject responseObject = new ResponseObject();

		SNMPAlert alert = snmpAlertDao.findByPrimaryKey(SNMPAlert.class, snmpAlert.getId());

		alert.setDesc(snmpAlert.getDesc());
		alert.setLastUpdatedByStaffId(snmpAlert.getLastUpdatedByStaffId());
		alert.setLastUpdatedDate(new Date());
		snmpAlertDao.merge(alert);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.SNMP_ALERT_UPDATE_SUCCESS);

		return responseObject;
	}

	private void deleteAllAlertsBindWithClient(SNMPServerConfig snmpClient, int clientId, int staffId) {
		List<SNMPAlertWrapper> snmpAlertWrapperList = new ArrayList<>();
		List<SNMPAlertWrapper> alertWrapperList = snmpAlertWrapperDao.getWrapperListByClientId(clientId);
		for (SNMPAlertWrapper alertWrapper : alertWrapperList) {
			alertWrapper.setLastUpdatedDate(new Date());
			alertWrapper.setLastUpdatedByStaffId(staffId);
			alertWrapper.setStatus(StateEnum.DELETED);
			snmpAlertWrapperList.add(alertWrapper);
		}
		if (!snmpAlertWrapperList.isEmpty()) {
			snmpClient.setLastUpdatedDate(new Date());
			snmpClient.setLastUpdatedByStaffId(staffId);
			snmpClient.setStatus(StateEnum.ACTIVE);
			snmpClient.setConfiguredAlerts(snmpAlertWrapperList);
			snmpDao.merge(snmpClient);

		}
	}

	@Transactional(readOnly = true,rollbackFor=SMException.class)
	@Override
	public void iterateSnmpClient(ServerInstance serverInstance, SNMPServerConfig snmpClient, Map<Integer, Integer> svcId, boolean isImport) {

		if (isImport) { // import call

			snmpClient.setId(0);
			snmpClient.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,snmpClient.getName()));
			snmpClient.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			snmpClient.setCreatedDate(new Date());
			snmpClient.setServerInstance(serverInstance);
			snmpClient.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());

		} else { // delete call
			snmpClient.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,snmpClient.getName()));
			snmpClient.setStatus(StateEnum.DELETED);
			snmpClient.setLastUpdatedDate(new Date());

		}

		snmpClient.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		snmpClient.setLastUpdatedDate(new Date());

		// Iterate over list of wrapper of client

		List<SNMPAlertWrapper> alertWrapperList = iterateSnmpAlertWrapper(serverInstance, snmpClient, svcId, isImport);
		if (alertWrapperList != null && !alertWrapperList.isEmpty()) {
			snmpClient.setConfiguredAlerts(alertWrapperList);
		}

	}

	@Transactional(readOnly = true,rollbackFor=SMException.class)
	@Override
	public void iterateSnmpServer(ServerInstance serverInstance, SNMPServerConfig snmpServer, boolean isImport) {

		if (isImport) { // import call

			logger.debug("Import SNMP Server :: " + snmpServer.getName());
			snmpServer.setId(0);
			snmpServer.setHostIP(serverInstance.getServer().getIpAddress());
			snmpServer.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,snmpServer.getName()));
			snmpServer.setStatus(StateEnum.INACTIVE);
			snmpServer.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			snmpServer.setCreatedDate(new Date());
			snmpServer.setServerInstance(serverInstance);
			snmpServer.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());

		} else { // delete call
			logger.debug("Delete SNMP Server :: " + snmpServer.getName());
			snmpServer.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,snmpServer.getName()));
			snmpServer.setStatus(StateEnum.DELETED);
			snmpServer.setLastUpdatedDate(new Date());

		}

		snmpServer.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		snmpServer.setLastUpdatedDate(new Date());

	}


	@Transactional(readOnly = true,rollbackFor=SMException.class)
	@Override
	public List<SNMPAlertWrapper> iterateSnmpAlertWrapper(ServerInstance serverInstance, SNMPServerConfig snmpClient,Map<Integer, Integer> svcId, boolean isImport) {

		List<SNMPAlertWrapper> snmpWrapperList = snmpClient.getConfiguredAlerts();
		List<SNMPAlertWrapper> snmpAlertWrapperList = new ArrayList<>();
		
		if(!CollectionUtils.isEmpty(snmpWrapperList)) {
			int length = snmpWrapperList.size();
			for(int i = length-1; i >= 0; i--) {
				SNMPAlertWrapper snmpWrapper = snmpWrapperList.get(i);
				if(snmpWrapper != null && !snmpWrapper.getStatus().equals(StateEnum.DELETED)) {
					importSnmpAlertWrapper(snmpWrapper, snmpClient, serverInstance, svcId, isImport);
					snmpAlertWrapperList.add(snmpWrapper);
				}
			}
		}
	
		return snmpAlertWrapperList;
	}

	@Transactional(readOnly = true,rollbackFor=SMException.class)
	@Override

	public List<SNMPServiceThreshold> iterateSnmpAlertThresholdList(ServerInstance serverInstance, SNMPAlertWrapper alertWrapper,
			Map<Integer, Integer> svcId, boolean isImport) {

		List<SNMPServiceThreshold> snmpAlertSvcThresholdList = alertWrapper.getServiceThreshold();
		List<SNMPServiceThreshold> snmpAlertSvcThreshold = new ArrayList<>();
		if (snmpAlertSvcThresholdList != null && !snmpAlertSvcThresholdList.isEmpty()) {
			for (int i = 0; i < snmpAlertSvcThresholdList.size(); i++) {
				SNMPServiceThreshold svcThresholdObj = snmpAlertSvcThresholdList.get(i);

				if (isImport) { // import call

					svcThresholdObj.setId(0);
					logger.debug("Service obj" + svcThresholdObj.getService());
					logger.debug("Service Map" + svcId);
					if (svcThresholdObj.getService() != null) {
						Service svcObj;
						if(svcId != null) {
							Integer newSvcId = svcId.get(svcThresholdObj.getService().getId());
							if(newSvcId != null){
								svcObj = servicesDao.getServiceWithServerInstanceById(newSvcId);
								if (svcObj != null) {
									svcThresholdObj.setService(svcObj);
								}
							}
						} else {
							svcObj = servInstanceService.getServiceFromServerInstance(serverInstance, svcThresholdObj.getService().getName());
							if (svcObj != null) {
								svcThresholdObj.setService(svcObj);
							}
						}
					} else {
						logger.debug("Inside else service id not found");
						svcThresholdObj.setServerInstance(serverInstance);
					}
					svcThresholdObj.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
					svcThresholdObj.setCreatedDate(new Date());
					svcThresholdObj.setServerInstance(serverInstance);
					svcThresholdObj.setWrapper(alertWrapper);
					svcThresholdObj.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());


				} else { // delete call

					svcThresholdObj.setStatus(StateEnum.DELETED);
					svcThresholdObj.setLastUpdatedDate(new Date());

				}


				svcThresholdObj.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
				svcThresholdObj.setLastUpdatedDate(new Date());

				snmpAlertSvcThreshold.add(svcThresholdObj);

			}
		}
		return snmpAlertSvcThreshold;
	}
	
	/**
	 * Iterate DB SNMP Client and set service threshold from imported file 
	 */
	@Transactional(readOnly = true,rollbackFor=SMException.class)
	@Override
	public void iterateDBSnmpClientAndAddThreshold(ServerInstance serverInstance, SNMPServerConfig snmpClient) {
		logger.debug("Inside iterateDBSnmpClientAndAddThreshold , add threshold into db client from imported file");
		List<SNMPAlertWrapper> importedWrapperList = snmpClient.getConfiguredAlerts();
		for (SNMPAlertWrapper importedSnmpWrapper : importedWrapperList) {
			iterateClientAndThresholdForWrapper(importedSnmpWrapper, serverInstance);
		} // end of imported wrapper loop
	}
	
	public void iterateClientAndThresholdForWrapper(SNMPAlertWrapper importedSnmpWrapper, ServerInstance serverInstance) {
		List<SNMPServiceThreshold> importedServiceThresholdList = importedSnmpWrapper.getServiceThreshold();
		if (!CollectionUtils.isEmpty(importedServiceThresholdList)) {
			logger.debug("SNMP Service Threshold list size for Imported snmp wrapper is  ::" + importedServiceThresholdList.size());
			List<SNMPServerConfig> snmpClientList = snmpDao.getClientListByServerInstanceId(serverInstance.getId());
			if(!CollectionUtils.isEmpty(snmpClientList)) {
				int clientLength = snmpClientList.size();
				for(int i = clientLength-1; i >= 0; i--) {
					SNMPServerConfig snmpClientObj = snmpClientList.get(i);
					logger.debug("SNMP Client Found From DB Object is ::" + snmpClientObj.getId() + " now fetch all db wrapper");
		
					List<SNMPAlertWrapper> dbAlertWrapperList = snmpClientObj.getConfiguredAlerts();
					if(!CollectionUtils.isEmpty(dbAlertWrapperList)) {
						int wrapperLength = dbAlertWrapperList.size();
						for(int j = wrapperLength-1; j >= 0; j--) {
							SNMPAlertWrapper dbSnmpWrapper = dbAlertWrapperList.get(j);

							if (dbSnmpWrapper.getAlert().getId() == importedSnmpWrapper.getAlert().getId()) {
								List<SNMPServiceThreshold> newSNMPThresholdList=new ArrayList<>();
								logger.debug("SNMP Wrapper Found for above client from DB object is ::" + dbSnmpWrapper.getId());
			
								List<SNMPServiceThreshold> dbServiceThresholdList =dbSnmpWrapper.getServiceThreshold();
								if (!CollectionUtils.isEmpty(dbServiceThresholdList)) {
									logger.debug("SNMP Service Threshold list size for DB snmp wrapper is  ::" + dbServiceThresholdList.size());
									newSNMPThresholdList.addAll(dbServiceThresholdList);
									int thresholdLength = importedServiceThresholdList.size();
									for (int k = thresholdLength-1; k >= 0; k--) {
										boolean isFound=false;
										SNMPServiceThreshold importedServiceThreshold = importedServiceThresholdList.get(k);
										int dbThresholdLength = dbServiceThresholdList.size();
										for (int l = dbThresholdLength-1; l >= 0; l--) {
											SNMPServiceThreshold dbServiceThreshold = dbServiceThresholdList.get(l);
											if (importedServiceThreshold.getService() != null && dbServiceThreshold.getService() != null && importedServiceThreshold.getService().getId() == dbServiceThreshold.getService().getId()) {
												
													logger.debug("Service Id found from DB threshold List ::" + dbServiceThreshold.getService().getId());
													isFound=true;
													break;
												
											}
										} // end of db service threshold list
										
										if(!isFound){
											logger.debug("Service Id add in DB threshold List ::" + importedServiceThreshold.getService().getId());
											SNMPServiceThreshold tempThreshold=new SNMPServiceThreshold();
											tempThreshold.setWrapper(dbSnmpWrapper);
											tempThreshold.setService(importedServiceThreshold.getService());
											tempThreshold.setThreshold(importedServiceThreshold.getThreshold());
											newSNMPThresholdList.add(tempThreshold);
										}
									} // end of imported service threshold list
								}
								logger.debug("Final SNMP Service Threshold list  for DB snmp wrapper is  ::" + newSNMPThresholdList);
								dbSnmpWrapper.setServiceThreshold(newSNMPThresholdList);
								dbSnmpWrapper.setServiceThresholdConfigured(true);
							}
							snmpAlertWrapperDao.merge(dbSnmpWrapper);
						}
					}
				}
			}
		}
	}

	@Override
	@Transactional(readOnly = true,rollbackFor=SMException.class)
	public List<SNMPAlert> getAlertsByCategory(SNMPAlertTypeEnum alertCategory) {
		return snmpAlertDao.getAlertsByCategory(alertCategory);
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.snmp.service.SnmpService#getAllSnmpAlerts()
	 */
	@Transactional(readOnly = true)
	public List<SNMPAlert> getAllSnmpAlerts() {
		return snmpAlertDao.getSnmpAlertList();
	}

	/*
	 * (non-Javadoc)
	 * @see com.elitecore.sm.snmp.service.SnmpService#getSnmpAlertById(int)
	 */
	@Transactional(readOnly = true)
	public SNMPAlert getSnmpAlertById(int alertId) {
		return snmpAlertDao.findByPrimaryKey(SNMPAlert.class, alertId);
	}
	
	@Override
	@Transactional
	public ResponseObject getSnmpSererByName(String snmpServerName){
		ResponseObject responseObject = new ResponseObject();
		SNMPServerConfig snmpServerConfig =  snmpDao.getSnmpServerConfigByName(snmpServerName);
		if(snmpServerConfig != null){
			responseObject.setSuccess(true);
			responseObject.setObject(snmpServerConfig);
		}else{
			responseObject.setSuccess(false);
		}
		return responseObject;
		 
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseObject updateMigSnmpServer(SNMPServerConfig snmpServer) {
		ResponseObject responseObject = new ResponseObject();
		
		if(snmpServer!=null) {
			SNMPServerConfig snmpServerConfigObj;
			snmpDao.save(snmpServer);
			logger.debug("Snmp Client added::"+snmpServer.getId()+"Type::"+snmpServer.getType().name());
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SNMP_UPDATE_SUCCESS);
			if(snmpServer.getType()==SNMPServerType.Self){
				snmpServerConfigObj=snmpDao.getSnmpServerConfigByName(snmpServer.getName());
			}
			else{
				snmpServerConfigObj=snmpDao.getSnmpClientConfigByName(snmpServer.getName());
			}
			responseObject.setObject(snmpServerConfigObj);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SNMP_UPDATE_FAIL);
		}

		return responseObject;
	}
	
	@Override
	public ResponseObject getSnmpAlertByAlertId(String alertId) {
		ResponseObject responseObject =  new ResponseObject();
	SNMPAlert snmpAlert=snmpAlertDao.getSnmpAlertByAlertId(alertId);
	if(snmpAlert!=null){
		responseObject.setSuccess(true);
		responseObject.setObject(snmpAlert);
	}
	else{
		responseObject.setSuccess(false);
	}
	return responseObject;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseObject updateMigSnmpClientWIthAlerts(SNMPServerConfig snmpClient) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Inside updateMigSnmpClientWIthAlerts");
		if(snmpClient!=null) {
			snmpDao.merge(snmpClient);
			logger.debug("Snmp Client added::"+snmpClient.getId()+"Name::"+snmpClient.getName());
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SNMP_UPDATE_SUCCESS);
			
			responseObject.setObject(snmpClient);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SNMP_UPDATE_FAIL);
		}

		return responseObject;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseObject updateMigAlertWrapperWithThreshold(SNMPAlertWrapper alertWrapper) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Inside updateMigSnmpClientWIthAlerts");
		if(alertWrapper!=null) {
			snmpAlertWrapperDao.merge(alertWrapper);
			logger.debug("Snmp AlertWrapper updated::"+alertWrapper.getId());
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.SNMP_UPDATE_SUCCESS);
			
			responseObject.setObject(alertWrapper);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.SNMP_UPDATE_FAIL);
		}

		return responseObject;
	}
	
	@Override
	public void importSnmpServer(ServerInstance dbServerInstance, ServerInstance exportedServerInstance, int importMode) {
		logger.debug("going to import snmp server for import mode : "+importMode);
		List<SNMPServerConfig> dbServerConfigList = dbServerInstance.getSelfSNMPServerConfig();
		List<SNMPServerConfig> exportedServerConfigList = exportedServerInstance.getSelfSNMPServerConfig();
		if(!CollectionUtils.isEmpty(exportedServerConfigList)) {
			int length = exportedServerConfigList.size();
			for(int i = length-1; i >= 0; i--) {
				SNMPServerConfig exportedServerConfig = exportedServerConfigList.get(i);
				if(exportedServerConfig != null && !exportedServerConfig.getStatus().equals(StateEnum.DELETED)) {
					SNMPServerConfig dbSelfSnmpServerConfig = getSelfSNMPServerConfigFromList(dbServerConfigList);
					if(dbSelfSnmpServerConfig == null) {
						logger.debug("going to add exported snmp server : "+exportedServerConfig+" : for import mode : "+importMode);
						importSnmpServerAddAndKeepBothMode(exportedServerConfig, dbServerInstance, importMode);
						dbServerConfigList.add(exportedServerConfig);
					} else if(importMode == BaseConstants.IMPORT_MODE_UPDATE) {
						logger.debug("going to update db snmp server : "+dbSelfSnmpServerConfig);
						importSnmpServerUpdateMode(dbSelfSnmpServerConfig, exportedServerConfig);
						dbServerConfigList.add(dbSelfSnmpServerConfig);
					}
				}
			}
		}
	}
	
	@Override
	public void importSnmpClient(ServerInstance dbServerInstance, ServerInstance exportedServerInstance, int importMode) {
		logger.debug("going to import snmp client for import mode : "+importMode);
		List<SNMPServerConfig> dbServerConfigList = dbServerInstance.getSnmpListeners();
		List<SNMPServerConfig> exportedServerConfigList = exportedServerInstance.getSnmpListeners();
		if(!CollectionUtils.isEmpty(exportedServerConfigList)) {
			int length = exportedServerConfigList.size();
			for(int i = length-1; i >= 0; i--) {
				SNMPServerConfig exportedServerConfig = exportedServerConfigList.get(i);
				if(exportedServerConfig != null && !exportedServerConfig.getStatus().equals(StateEnum.DELETED)) {
					SNMPServerConfig dbServerConfig = getSNMPServerConfigFromList(dbServerConfigList, exportedServerConfig.getName());
					if(importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH || dbServerConfig == null) {
						logger.debug("going to add exported snmp client : "+exportedServerConfig+ " for import mode : "+importMode);
						importSnmpClientAddAndKeepBothMode(exportedServerConfig, dbServerInstance, importMode);
						dbServerConfigList.add(exportedServerConfig);
					} else if(importMode == BaseConstants.IMPORT_MODE_UPDATE) {
						logger.debug("going to update snmp client : "+dbServerConfig);
						importSnmpClientUpdateMode(dbServerConfig, exportedServerConfig);
						dbServerConfigList.add(dbServerConfig);
					} else if(importMode == BaseConstants.IMPORT_MODE_ADD) {
						logger.debug("going to Add new snmp client : "+dbServerConfig);
						importSnmpClientAddMode(dbServerConfig, exportedServerConfig);
						dbServerConfigList.add(dbServerConfig);
					}
				}
			}
		}
	}
	
	public void importSnmpClientAddAndKeepBothMode(SNMPServerConfig exportedSnmpClient, ServerInstance serverInstance, int importMode) {
		exportedSnmpClient.setId(0);
		exportedSnmpClient.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		exportedSnmpClient.setCreatedDate(EliteUtils.getDateForImport(false));
		exportedSnmpClient.setCreatedByStaffId(serverInstance.getCreatedByStaffId());
		exportedSnmpClient.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		if(importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
			exportedSnmpClient.setName(EliteUtils.checkForNames(BaseConstants.IMPORT, exportedSnmpClient.getName()));
		}
		exportedSnmpClient.setServerInstance(serverInstance);
		String snmpV3AuthPass = exportedSnmpClient.getSnmpV3AuthPassword();
		if(!StringUtils.isEmpty(snmpV3AuthPass)) {
			boolean isHexadecimal = EliteUtils.isHexadecimal(snmpV3AuthPass);
			if(!isHexadecimal) {
				exportedSnmpClient.setSnmpV3AuthPassword(EliteUtils.encryptData(snmpV3AuthPass));
			}
		}
		String snmpV3PrivPass = exportedSnmpClient.getSnmpV3PrivPassword();
		if(!StringUtils.isEmpty(snmpV3PrivPass)) {
			boolean isHexadecimal = EliteUtils.isHexadecimal(snmpV3PrivPass);
			if(!isHexadecimal) {
				exportedSnmpClient.setSnmpV3PrivPassword(EliteUtils.encryptData(snmpV3PrivPass));
			}
		}
		List<SNMPAlertWrapper> alertWrapperList = iterateSnmpAlertWrapper(serverInstance, exportedSnmpClient, null, true);//NOSONAR
		if (!CollectionUtils.isEmpty(alertWrapperList)) {
			exportedSnmpClient.setConfiguredAlerts(alertWrapperList);
		}
	}
	
	public void importSnmpClientUpdateMode(SNMPServerConfig dbSnmpClient, SNMPServerConfig exportedSnmpClient) {
		updateBasicSnmpParamForImport(dbSnmpClient, exportedSnmpClient);
		
		List<SNMPAlertWrapper> dbAlertWrapperList = snmpAlertWrapperDao.getAllWrapperListByClientId(dbSnmpClient.getId());
		List<SNMPAlertWrapper> exportedAlertWrapperList = exportedSnmpClient.getConfiguredAlerts();
		
		if(!CollectionUtils.isEmpty(exportedAlertWrapperList)) {
			int length = exportedAlertWrapperList.size();
			for(int i = length-1; i >= 0; i--) {
				SNMPAlertWrapper exportedWrapper = exportedAlertWrapperList.get(i);
				if(exportedWrapper != null && !exportedWrapper.getStatus().equals(StateEnum.DELETED)) {
					SNMPAlertWrapper dbWrapper = getSNMPAlertWrapperFromList(dbAlertWrapperList, exportedWrapper.getAlert() != null ? exportedWrapper.getAlert().getName() : null);
					if(dbWrapper != null) {
						logger.debug("going to update db snmp wrapper : "+dbWrapper);
						importSnmpAlertWrapperUpdateMode(dbWrapper, exportedWrapper, dbSnmpClient.getServerInstance());
						dbAlertWrapperList.add(dbWrapper);
					} else {
						logger.debug("going to add exported snmp wrapper : "+exportedWrapper);
						importSnmpAlertWrapper(exportedWrapper, dbSnmpClient, dbSnmpClient.getServerInstance(), null, true);
						dbAlertWrapperList.add(exportedWrapper);
					}
				}
			}
		}
		dbSnmpClient.getConfiguredAlerts().clear();
		dbSnmpClient.setConfiguredAlerts(dbAlertWrapperList);
	}
	
	public void importSnmpClientAddMode(SNMPServerConfig dbSnmpClient, SNMPServerConfig exportedSnmpClient) {
		updateBasicSnmpParamForImport(dbSnmpClient, exportedSnmpClient);
		
		List<SNMPAlertWrapper> dbAlertWrapperList = snmpAlertWrapperDao.getAllWrapperListByClientId(dbSnmpClient.getId());
		List<SNMPAlertWrapper> exportedAlertWrapperList = exportedSnmpClient.getConfiguredAlerts();
		
		if(!CollectionUtils.isEmpty(exportedAlertWrapperList)) {
			int length = exportedAlertWrapperList.size();
			for(int i = length-1; i >= 0; i--) {
				SNMPAlertWrapper exportedWrapper = exportedAlertWrapperList.get(i);
				if(exportedWrapper != null && !exportedWrapper.getStatus().equals(StateEnum.DELETED)) {
					SNMPAlertWrapper dbWrapper = getSNMPAlertWrapperFromList(dbAlertWrapperList, exportedWrapper.getAlert() != null ? exportedWrapper.getAlert().getName() : null);
					if(dbWrapper != null) {
						logger.debug("going to update db snmp wrapper : "+dbWrapper);
						importSnmpAlertWrapperAddMode(dbWrapper, exportedWrapper, dbSnmpClient.getServerInstance());
						dbAlertWrapperList.add(dbWrapper);
					} else {
						logger.debug("going to add exported snmp wrapper : "+exportedWrapper);
						importSnmpAlertWrapper(exportedWrapper, dbSnmpClient, dbSnmpClient.getServerInstance(), null, true);
						dbAlertWrapperList.add(exportedWrapper);
					}
				}
			}
		}
		dbSnmpClient.getConfiguredAlerts().clear();
		dbSnmpClient.setConfiguredAlerts(dbAlertWrapperList);
	}
	
	public void importSnmpServerAddAndKeepBothMode(SNMPServerConfig exportedSnmpServer, ServerInstance serverInstance, int importMode) {
		exportedSnmpServer.setId(0);
		exportedSnmpServer.setHostIP(serverInstance.getServer().getIpAddress());
		if(importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
			exportedSnmpServer.setName(EliteUtils.checkForNames(BaseConstants.IMPORT,exportedSnmpServer.getName()));
		}
		exportedSnmpServer.setStatus(StateEnum.INACTIVE);
		exportedSnmpServer.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		exportedSnmpServer.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		exportedSnmpServer.setCreatedDate(EliteUtils.getDateForImport(false));
		exportedSnmpServer.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		String snmpV3AuthPass = exportedSnmpServer.getSnmpV3AuthPassword();
		if(!StringUtils.isEmpty(snmpV3AuthPass)) {
			boolean isHexadecimal = EliteUtils.isHexadecimal(snmpV3AuthPass);
			if(!isHexadecimal) {
				exportedSnmpServer.setSnmpV3AuthPassword(EliteUtils.encryptData(snmpV3AuthPass));
			}
		}
		String snmpV3PrivPass = exportedSnmpServer.getSnmpV3PrivPassword();
		if(!StringUtils.isEmpty(snmpV3PrivPass)) {
			boolean isHexadecimal = EliteUtils.isHexadecimal(snmpV3PrivPass);
			if(!isHexadecimal) {
				exportedSnmpServer.setSnmpV3PrivPassword(EliteUtils.encryptData(snmpV3PrivPass));
			}
		}
		exportedSnmpServer.setServerInstance(serverInstance);
	}
	
	public void importSnmpServerUpdateMode(SNMPServerConfig dbSnmpServer, SNMPServerConfig exportedSnmpServer) {
		updateBasicSnmpParamForImport(dbSnmpServer, exportedSnmpServer);
	}
	
	public void updateBasicSnmpParamForImport(SNMPServerConfig dbSnmpServerConfig, SNMPServerConfig exportedSnmpServerConfig) {
		dbSnmpServerConfig.setPortOffset(exportedSnmpServerConfig.getPortOffset());
		dbSnmpServerConfig.setCommunity(exportedSnmpServerConfig.getCommunity());
		dbSnmpServerConfig.setVersion(exportedSnmpServerConfig.getVersion());
		dbSnmpServerConfig.setAdvance(exportedSnmpServerConfig.getAdvance());
	}
	
	public void importSnmpAlertWrapperUpdateMode(SNMPAlertWrapper dbWrapper, SNMPAlertWrapper exportedWrapper, ServerInstance serverInstance) {
		dbWrapper.setStatus(StateEnum.ACTIVE);
		dbWrapper.setServiceThresholdConfigured(exportedWrapper.isServiceThresholdConfigured());
		List<SNMPServiceThreshold> dbServiceThresholdList = dbWrapper.getServiceThreshold();
		List<SNMPServiceThreshold> exportedServiceThresholdList = exportedWrapper.getServiceThreshold();
		if(!CollectionUtils.isEmpty(exportedServiceThresholdList)) {
			int length = exportedServiceThresholdList.size();
			for(int i = length-1; i >= 0; i--) {
				SNMPServiceThreshold exportedThreshold = exportedServiceThresholdList.get(i);
				if(exportedThreshold != null && !exportedThreshold.getStatus().equals(StateEnum.DELETED)) {
					SNMPServiceThreshold dbThreshold = getSNMPServiceThresholdFromList(dbServiceThresholdList, exportedThreshold.getService().getName());
					if(dbThreshold != null) {
						logger.debug("going to update db threshold : "+dbThreshold);
						importSnmpServiceThresholdUpdateMode(dbThreshold, exportedThreshold);
						dbServiceThresholdList.add(dbThreshold);
					} else {
						logger.debug("going to add exported threshold : "+exportedThreshold);
						importSnmpServiceThresholdAddAndKeepBothMode(exportedThreshold, dbWrapper, serverInstance);
						dbServiceThresholdList.add(exportedThreshold);
					}
				}
			}
		}
	}
	
	public void importSnmpAlertWrapperAddMode(SNMPAlertWrapper dbWrapper, SNMPAlertWrapper exportedWrapper, ServerInstance serverInstance) {
		dbWrapper.setStatus(StateEnum.ACTIVE);
		dbWrapper.setServiceThresholdConfigured(exportedWrapper.isServiceThresholdConfigured());
		List<SNMPServiceThreshold> dbServiceThresholdList = dbWrapper.getServiceThreshold();
		List<SNMPServiceThreshold> exportedServiceThresholdList = exportedWrapper.getServiceThreshold();
		if(!CollectionUtils.isEmpty(exportedServiceThresholdList)) {
			int length = exportedServiceThresholdList.size();
			for(int i = length-1; i >= 0; i--) {
				SNMPServiceThreshold exportedThreshold = exportedServiceThresholdList.get(i);
				if(exportedThreshold != null && !exportedThreshold.getStatus().equals(StateEnum.DELETED)) {
					SNMPServiceThreshold dbThreshold = getSNMPServiceThresholdFromList(dbServiceThresholdList, exportedThreshold.getService().getName());
					if(dbThreshold == null) {	//Add alert wrapper which are not in db					
						logger.debug("going to add exported threshold : "+exportedThreshold);
						importSnmpServiceThresholdAddAndKeepBothMode(exportedThreshold, dbWrapper, serverInstance);
						dbServiceThresholdList.add(exportedThreshold);
					}
				}
			}
		}
	}
	
	public void importSnmpAlertWrapper(SNMPAlertWrapper snmpWrapper, SNMPServerConfig snmpClient, ServerInstance serverInstance, Map<Integer, Integer> svcId, boolean isImport) {
		Date date = new Date();
		if (isImport) { // import call
			snmpWrapper.setId(0);
			snmpWrapper.setCreatedByStaffId(serverInstance.getLastUpdatedByStaffId());
			snmpWrapper.setCreatedDate(date);
			snmpWrapper.setListener(snmpClient);
			snmpWrapper.setAlert(snmpAlertDao.getSnmpAlertByAlertId(snmpWrapper.getAlert().getAlertId()));
			snmpWrapper.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		} else { // delete call
			snmpWrapper.setStatus(StateEnum.DELETED);
			snmpWrapper.setLastUpdatedDate(date);
		}

		snmpWrapper.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		snmpWrapper.setLastUpdatedDate(date);
		
		List<SNMPServiceThreshold> svcThresholdList= iterateSnmpAlertThresholdList(serverInstance, snmpWrapper,svcId, isImport);//NOSONAR
		if(!CollectionUtils.isEmpty(svcThresholdList)){
			if(isImport){
				int alertId = snmpWrapper.getAlert().getId();
				List<SNMPServerConfig> snmpClientList = snmpDao.getClientListByServerInstanceId(serverInstance.getId());
				
				if(!CollectionUtils.isEmpty(snmpClientList)){
					int length = snmpClientList.size();
					
					for(int i = length-1; i >= 0; i--) {
						SNMPServerConfig snmpClientObj = snmpClientList.get(i);
						if(snmpClientObj != null && !snmpClientObj.getStatus().equals(StateEnum.DELETED)) {
							logger.debug("SNMP Client Found From DB Object is ::"+snmpClientObj.getId());
							
							List<SNMPAlertWrapper> dbAlertWrapperList=snmpAlertWrapperDao.getWrapperListByAlertIdAndClientId(alertId, snmpClientObj.getId());
							if(!CollectionUtils.isEmpty(dbAlertWrapperList)){
								
								logger.debug("SNMP Wrapper Found for above client from DB object is ::"+dbAlertWrapperList.get(0).getId());
								List<SNMPServiceThreshold> dbServiceThresholdList=snmpServiceThresholdDao.getThresholdListByWrapperId(dbAlertWrapperList.get(0).getId());
								
								if(!CollectionUtils.isEmpty(dbServiceThresholdList)){
									int thresholdLength = dbServiceThresholdList.size();
									
									for(int j = thresholdLength-1; j >= 0; j--) {
										SNMPServiceThreshold serviceThreshold = dbServiceThresholdList.get(j);
										
										if(serviceThreshold != null && !serviceThreshold.getStatus().equals(StateEnum.DELETED)) {
											logger.debug("SNMP Service Threshold is : "+serviceThreshold.getThreshold());
											/*snmpServiceThresholdDao.evict(serviceThreshold);
											serviceThreshold.setId(0);
											serviceThreshold.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
											serviceThreshold.setLastUpdatedDate(date);
											serviceThreshold.setWrapper(snmpWrapper);
											svcThresholdList.add(serviceThreshold);*/
										}
									} // end of service threshold loop
									logger.debug("SNMP Service Threshold list size for above snmp wrapper is  ::"+dbServiceThresholdList.size());
								}
							}
							break;
						}
					}  // end of snmp client loop
				}
			}
			logger.debug("Inside iterateSnmpAlertWrapper:: Final svcthreshold List for imported file wrapper is ::"+svcThresholdList);
			snmpWrapper.setServiceThreshold(svcThresholdList);
			snmpWrapper.setServiceThresholdConfigured(true);
		}
	}
	
	public void importSnmpServiceThresholdUpdateMode(SNMPServiceThreshold dbThreshold, SNMPServiceThreshold exportedThreshold) {
		dbThreshold.setThreshold(exportedThreshold.getThreshold());
	}
	
	public void importSnmpServiceThresholdAddAndKeepBothMode(SNMPServiceThreshold exportedThreshold, SNMPAlertWrapper wrapper, ServerInstance serverInstance) {
		exportedThreshold.setId(0);
		exportedThreshold.setCreatedDate(EliteUtils.getDateForImport(false));
		exportedThreshold.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		exportedThreshold.setCreatedByStaffId(serverInstance.getCreatedByStaffId());
		exportedThreshold.setLastUpdatedByStaffId(serverInstance.getLastUpdatedByStaffId());
		Service service = servInstanceService.getServiceFromServerInstance(serverInstance, exportedThreshold.getService().getName());
		if(service != null) {
			exportedThreshold.setService(service);
		}
		exportedThreshold.setServerInstance(serverInstance);
		exportedThreshold.setWrapper(wrapper);
	}
	
	public SNMPServerConfig getSNMPServerConfigFromList(List<SNMPServerConfig> serverConfigList, String snmpServerName) {
		if(!CollectionUtils.isEmpty(serverConfigList)) {
			int length = serverConfigList.size();
			for(int i = length-1; i >= 0; i--) {
				SNMPServerConfig config = serverConfigList.get(i);
				if(config != null && !config.getStatus().equals(StateEnum.DELETED)
						&& config.getName().equalsIgnoreCase(snmpServerName)) {
					return serverConfigList.remove(i);
				}
			}
		}
		return null;
	}
	
	public SNMPServerConfig getSelfSNMPServerConfigFromList(List<SNMPServerConfig> serverConfigList) {
		if(!CollectionUtils.isEmpty(serverConfigList)) {
			int length = serverConfigList.size();
			for(int i = length-1; i >= 0; i--) {
				SNMPServerConfig config = serverConfigList.get(i);
				if(config != null && !config.getStatus().equals(StateEnum.DELETED)
						&& config.getType().equals(SNMPServerType.Self)) {
					return config;
				}
			}
		}
		return null;
	}
	
	public SNMPAlertWrapper getSNMPAlertWrapperFromList(List<SNMPAlertWrapper> wrapperList, String alertName) {
		if(!CollectionUtils.isEmpty(wrapperList)) {
			int length = wrapperList.size();
			for(int i = length-1; i >= 0; i--) {
				SNMPAlertWrapper wrapper = wrapperList.get(i);
				if(isSnmpWrapperUnique(wrapper, alertName)) {
					return wrapperList.remove(i);
				}
			}
		}
		return null;
	}
	
	public boolean isSnmpWrapperUnique(SNMPAlertWrapper wrapper, String alertName) {
		boolean isAlertUnique = false;
		if(wrapper != null) {
			SNMPAlert alert = wrapper.getAlert();
			if(alert == null && alertName == null) {
				isAlertUnique = true;
			} else if(alert != null && alert.getName().equalsIgnoreCase(alertName)) {
				isAlertUnique = true;
			}
		}
		return isAlertUnique ? true : false;
	}
	
	public SNMPServiceThreshold getSNMPServiceThresholdFromList(List<SNMPServiceThreshold> serviceThresholdList, String serviceName) {
		if(!CollectionUtils.isEmpty(serviceThresholdList)) {
			int length = serviceThresholdList.size();
			for(int i = length-1; i >= 0; i--) {
				SNMPServiceThreshold threshold = serviceThresholdList.get(i);
				if(threshold != null && !threshold.getStatus().equals(StateEnum.DELETED)
						&& threshold.getService().getName().equalsIgnoreCase(serviceName)) {
					return serviceThresholdList.remove(i);
				}
			}
		}
		return null;
	}
	
	@Transactional
	@Override
	public List<SNMPServerConfig> getServerConfigList() {
		List<SNMPServerConfig> snmpServerConfigList = snmpDao.getServerConfigList();
		return snmpServerConfigList;
	}
	
	@Transactional
	@Override
	public void migrateSnmpClientPassword() {
		List<SNMPServerConfig> snmpClientConfigs = this.getServerConfigList();
		for(SNMPServerConfig snmpClient : snmpClientConfigs){
			if(!StringUtils.isEmpty(snmpClient.getSnmpV3AuthPassword())){
				snmpClient.setSnmpV3AuthPassword(EliteUtils.encryptData(snmpClient.getSnmpV3AuthPassword()));
			}
			if(!StringUtils.isEmpty(snmpClient.getSnmpV3PrivPassword())){
				snmpClient.setSnmpV3PrivPassword(EliteUtils.encryptData(snmpClient.getSnmpV3PrivPassword()));
			}
			snmpDao.merge(snmpClient);
		}
	}
	
}