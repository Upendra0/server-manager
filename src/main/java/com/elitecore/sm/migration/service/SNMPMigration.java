package com.elitecore.sm.migration.service;

import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.serverinstance.model.ServerInstance;

public interface SNMPMigration {

	public ResponseObject getSnmpUnmarshalObject(String destinationDir, String migrationPrefix) throws MigrationSMException;

	public ResponseObject getSnmpListnerUnmarshalObject(String destinationDir, String migrationPrefix, ServerInstance serverInstance)
			throws MigrationSMException;

}
