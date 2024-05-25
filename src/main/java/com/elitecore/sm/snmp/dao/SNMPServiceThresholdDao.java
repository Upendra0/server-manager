package com.elitecore.sm.snmp.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.snmp.model.SNMPServiceThreshold;

public interface SNMPServiceThresholdDao extends GenericDAO<SNMPServiceThreshold> {



	List<SNMPServiceThreshold> getSvcThresholdListByWrapperId(int snmpAlertWrapperId, int svcId);

	List<SNMPServiceThreshold> getSvcThresholdListByWrapperIdAndInstanceId(int snmpAlertWrapperId, int serverInstanceId);

	List<SNMPServiceThreshold> getThresholdListByWrapperId(int snmpAlertWrapperId);
	
	List<SNMPServiceThreshold> getActiveServiceThresodeList(int snmpAlertWrapperId);


}
