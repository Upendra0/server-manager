package com.elitecore.sm.snmp.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.snmp.model.SNMPAlert;
import com.elitecore.sm.snmp.model.SNMPAlertTypeEnum;

public interface SNMPAlertDao extends GenericDAO<SNMPAlert> {

	public Map<String, Object> getSnmpAlertPaginatedList();

	List<SNMPAlert> getSnmpAlertPaginatedList(
			Class<SNMPAlert> classInstance, List<Criterion> conditions,
			HashMap<String, String> aliases);
	
	public List<SNMPAlert> getAlertsByCategory(SNMPAlertTypeEnum alertCategory);
	
	/**
	 * Get All SNMP Alert List
	 * @return the SNMP Alert List
	 */
	public List<SNMPAlert> getSnmpAlertList();

	public SNMPAlert getSnmpAlertByAlertId(String alertId);
}
