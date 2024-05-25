/**
 * 
 */
package com.elitecore.sm.migration.service;

import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.serverinstance.model.ServerInstance;


/**
 * @author Ranjitsinh Reval
 *
 */
public interface AgentMigrationService {

	
	public ResponseObject getAgentObject(ServerInstance serverInstance) throws MigrationSMException;
}
