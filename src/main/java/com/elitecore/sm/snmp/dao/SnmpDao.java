package com.elitecore.sm.snmp.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.snmp.model.SNMPAlert;
import com.elitecore.sm.snmp.model.SNMPAlertType;
import com.elitecore.sm.snmp.model.SNMPServerConfig;

public interface SnmpDao extends GenericDAO<SNMPServerConfig> {

	public Map<String, Object> getSnmpServerPaginatedList(int serviceInstanceId);

	public SNMPServerConfig getSnmpServerById(int snmpServerId);

	public int getActiveSnmpServerCount(int serverInstanceId);

	public int getSnmpServerCount(String snmpServerName);

	public List<SNMPServerConfig> getSnmpServerListByName(String snmpServerName);

	public List<SNMPAlertType> getSnmpAlertType(List<String> serviceAliasList);

	public List<SNMPAlertType> getCommonSnmpAlertType();

	public List<SNMPAlert> getSnmpAlertsByAlertType(List<SNMPAlertType> snmpAlertType);

	public Map<String, Object> getSnmpClientPaginatedList(int serverInstanceId);

	public List<SNMPServerConfig> getSnmpClientPaginatedList(Class<SNMPServerConfig> classInstance, List<Criterion> conditions,
			Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder);

	public SNMPServerConfig getConfiguredAlertListByClientId(int clientId);

	public List<SNMPServerConfig> getClientListByServerInstanceId(int serverInstanceId);

	public List<SNMPServerConfig> getSnmpServerPaginatedList(Class<SNMPServerConfig> classInstance, List<Criterion> conditions,
			Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder);

	public SNMPServerConfig getSnmpClientfullHierarchyWithoutMarshlling(int clientId);

	public List<SNMPServerConfig> getServerListByServerInstanceId(int serverInstanceId);

	public List<Object> getAlertListWithThreshold(ServerInstance serverInstance, String jaxbXmlPath, String xsltFilePath, String engineSampleXmlPath);

	public SNMPServerConfig getSnmpServerConfigByName(String snmpName);

	public SNMPServerConfig getSnmpClientConfigByName(String snmpName);
	
	public List<SNMPServerConfig> getServerConfigList();

}
