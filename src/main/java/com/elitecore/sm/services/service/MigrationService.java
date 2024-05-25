package com.elitecore.sm.services.service;

import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.migration.model.MigrationTrackDetails;

public interface MigrationService {
	
	public ResponseObject checkServerAvailability(String ipaddress, int serverTypeId, int utilityPort);

	public ResponseObject checkServerInstanceDetails(String ipaddress, int serverTypeId, int utilityPort, int port, String scriptFileName);

	public ResponseObject readServerConfiguration(String ipAddress, String migrationPrefix, int serverType, int instancePort, int staffId,
			String scriptFileName, String servicesList) throws MigrationSMException;
	
	public ResponseObject getAllServicesList(MigrationTrackDetails migrationTrackDetails);

	public ResponseObject getAllServicesListForUpdate(int serverId, int port);
	
}
