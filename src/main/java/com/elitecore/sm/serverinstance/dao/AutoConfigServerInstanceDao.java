package com.elitecore.sm.serverinstance.dao;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.serverinstance.model.AutoConfigServerInstance;


/**
 * 
 * @author hardik.loriya
 *
 */
public interface AutoConfigServerInstanceDao extends GenericDAO<AutoConfigServerInstance> {
	
	public AutoConfigServerInstance getAutoConfigSIByIPAndUtilityPortAndSIPort(String ipAddress, int utilityPort, int port);
	
}

