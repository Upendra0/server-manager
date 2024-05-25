package com.elitecore.sm.snmp.service;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.snmp.model.SNMPAlert;
import com.elitecore.sm.snmp.model.SNMPAlertType;
import com.elitecore.sm.snmp.model.SNMPAlertTypeEnum;
import com.elitecore.sm.snmp.model.SNMPAlertWrapper;
import com.elitecore.sm.snmp.model.SNMPServerConfig;
import com.elitecore.sm.snmp.model.SNMPServiceThreshold;

public interface SnmpService {

	public ResponseObject addSnmpServerList(SNMPServerConfig snmpserver);

	public long getSnmpServertotalCount(int serverInstanceId);

	public List<Map<String, Object>> getSnmpServerPaginatedList(int serverInstanceId, int startIndex, int limit, String sidx, String sord);

	public ResponseObject changeSnmpServerStatus(int snmpServerId, String snmpStatus, int serverInstanceId) throws CloneNotSupportedException;

	public ResponseObject getSnmpServerById(int snmpServerId);

	public boolean isSnmpServerNameUniqueForUpdate(int snmpServerId, String snmpServerName);

	public ResponseObject updateSnmpServerDetail(SNMPServerConfig snmpServer);

	public ResponseObject deleteSnmpServer(int snmpServerId, int staffId) throws CloneNotSupportedException;

	public ResponseObject addSnmpClient(SNMPServerConfig snmpClient);

	public List<String> getServiceTypeforServerInstance(int serverInstanceId);

	public List<SNMPAlertType> getAlertTypeforServerInstance(List<String> serviceType);

	public JSONArray getSnmpAlertByAlertType(List<SNMPAlertType> snmpAlertType);

	ResponseObject addSnmpAlertsToClient(int clientId, String alertIds, int staffId);

	SNMPAlertWrapper addAlert(int alertId, SNMPServerConfig snmpClient, int staffId);

	long getSnmpClienttotalCount(int serverInstanceId);

	List<Map<String, Object>> getSnmpClientPaginatedList(int serverInstanceId, int startIndex, int limit, String sidx, String sord);

	ResponseObject updateSnmpClientDetail(SNMPServerConfig snmpClient);

	ResponseObject fetchAlertsForUpdateClient(int clientId);

	JSONArray getClientConfiguredAlerts(int clientId);

	long getSnmpAlerttotalCount();

	ResponseObject getServiceWithServiceThreshold(int serverInstanceId, int alertId);

	ResponseObject changeSnmpClientStatus(int snmpClientId, String snmpStatus) throws CloneNotSupportedException;

	ResponseObject deleteSnmpClient(int snmpClientId, int staffId) throws CloneNotSupportedException;

	ResponseObject getConfiguredAlertsByClientId(int clientId);

	ResponseObject updateSnmpAlertsToClient(int clientId, String alertIds, int staffId);

	ResponseObject getSnmpAlertPaginatedList(int serverInstanceId);

	ResponseObject updateSnmpAlertDetails(SNMPAlert snmpAlert);

	void iterateSnmpServer(ServerInstance serverInstance, SNMPServerConfig snmpServer, boolean isImport);

	List<SNMPServiceThreshold> iterateSnmpAlertThresholdList(ServerInstance serverInstance, SNMPAlertWrapper alertWrapper,
			Map<Integer, Integer> svcId, boolean isImport);

	List<SNMPAlertWrapper> iterateSnmpAlertWrapper(ServerInstance serverInstance, SNMPServerConfig snmpClient, Map<Integer, Integer> svcId,
			boolean isImport);

	void iterateSnmpClient(ServerInstance serverInstance, SNMPServerConfig snmpClient, Map<Integer, Integer> svcId, boolean isImport);

	ResponseObject updateServiceThreshold(int serverInstanceId, int alertId, String alertsvcList, String selectedAlertType);

	public void iterateDBSnmpClientAndAddThreshold(ServerInstance serverInstance, SNMPServerConfig snmpClient);

	public List<SNMPAlert> getAlertsByCategory(SNMPAlertTypeEnum alertCategory);

	/**
	 * Get All SNMP Alerts
	 * 
	 * @return All SNMP Alerts
	 */
	public List<SNMPAlert> getAllSnmpAlerts();

	/**
	 * Get SNMP ALert by Id
	 * 
	 * @param alertId
	 *            the snmp alert id
	 * @return
	 */
	public SNMPAlert getSnmpAlertById(int alertId);

	public ResponseObject getSnmpSererByName(String snmpServerName);

	public ResponseObject updateMigSnmpServer(SNMPServerConfig snmpServer);

	public ResponseObject getSnmpAlertByAlertId(String alertId);

	public ResponseObject updateMigSnmpClientWIthAlerts(SNMPServerConfig snmpClient);

	public ResponseObject updateMigAlertWrapperWithThreshold(SNMPAlertWrapper alertWrapper);
	
	public void importSnmpClient(ServerInstance dbServerInstance, ServerInstance exportedServerInstance, int importMode);
	
	public void importSnmpServer(ServerInstance dbServerInstance, ServerInstance exportedServerInstance, int importMode);
	
	public List<SNMPServerConfig> getServerConfigList();
	
	public void migrateSnmpClientPassword();
	
}
