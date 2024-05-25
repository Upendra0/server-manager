/**
 * 
 */
package com.elitecore.sm.migration.service;

import java.util.List;

import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.services.model.DistributionService;


/**
 * @author Ranjitsinh Reval
 *
 */
public interface DistributionDriverMigrationService {
	public ResponseObject getDistributionDriverDetailsAndDependents(List<Drivers> finalDriverList, DistributionService distributionService, int staffId, String migrationPrefix) throws MigrationSMException;
}
