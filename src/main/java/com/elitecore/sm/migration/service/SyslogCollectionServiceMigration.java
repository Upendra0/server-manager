package com.elitecore.sm.migration.service;

import java.util.List;

import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.Service;


/**
 * 
 * @author Harsh.Patel
 *
 */
public interface SyslogCollectionServiceMigration{

	public ResponseObject iterateAndSetSyslogCollectionServiceDetails(
			List<Service> serviceList, int position, Service service,
			String directoryPath, ServerInstance serverInstance, String migrationPrefix) throws MigrationSMException;
}
