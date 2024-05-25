package com.elitecore.sm.snmp.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.snmp.model.SNMPAlertWrapper;

public interface SNMPAlertWrapperDao extends GenericDAO<SNMPAlertWrapper> {

	List<SNMPAlertWrapper> getWrapperListByAlertId(int alertId);

	List<SNMPAlertWrapper> getWrapperListByAlertIdAndClientId(int alertId,int clientId);

	List<SNMPAlertWrapper> getWrapperListByClientId(int clientId);

	List<SNMPAlertWrapper> getAllWrapperListByAlertIdAndClientId(int alertId, int clientId);

	void irerateOverSnmpWrapper(SNMPAlertWrapper alertWrapper);
	
	List<SNMPAlertWrapper>  getAllActiveSNMPAlertWrapperByServerConfig(int serverConfigId);
	
	public List<SNMPAlertWrapper> getAllWrapperListByClientId(int clientId);
	
	


}
