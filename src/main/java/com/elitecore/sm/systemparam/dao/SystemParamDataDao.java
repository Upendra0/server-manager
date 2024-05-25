/**
 * 
 */
package com.elitecore.sm.systemparam.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.systemparam.model.SystemParameterData;
import com.elitecore.sm.systemparam.model.SystemParameterValuePoolData;

/**
 * @author vandana.awatramani
 * 
 */
public interface SystemParamDataDao extends GenericDAO<SystemParameterData> {
	
	public List<SystemParameterValuePoolData> loadSystemParamValuePoolData();
	
	public SystemParameterData getSystemParameterByAlias(String alias);
	
	public List<SystemParameterData> loadSystemParameterList();
	
}
