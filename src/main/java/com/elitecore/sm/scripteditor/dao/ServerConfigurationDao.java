/**
 * 
 */
package com.elitecore.sm.scripteditor.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.scripteditor.model.ServerConfiguration;

/**
 * @author hiral.panchal
 *
 */
public interface ServerConfigurationDao extends GenericDAO<ServerConfiguration>{
	
	
public List<ServerConfiguration> getAllActiveServers();
	
}
