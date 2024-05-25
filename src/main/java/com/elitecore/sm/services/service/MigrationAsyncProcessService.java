package com.elitecore.sm.services.service;

import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.services.model.MigrationStatusEnum;


public interface MigrationAsyncProcessService {

	public ResponseObject doMigration(MigrationStatusEnum migrationStatus) throws MigrationSMException;
	
}
