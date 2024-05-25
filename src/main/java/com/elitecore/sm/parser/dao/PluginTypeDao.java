/**
 * 
 */
package com.elitecore.sm.parser.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.parser.model.PluginTypeMaster;

/**
 * @author vandana.awatramani
 *
 */
public interface PluginTypeDao extends GenericDAO<PluginTypeMaster> {

	public List<PluginTypeMaster> getEnablePluginTypeList(String pluginCategory);
	
	public PluginTypeMaster getPluginByType(String type);
	
}
