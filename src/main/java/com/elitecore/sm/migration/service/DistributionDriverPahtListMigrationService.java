/**
 * 
 */
package com.elitecore.sm.migration.service;

import java.util.List;

import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.drivers.model.DistributionDriver;


/**
 * @author Ranjitsinh Reval
 *
 */
public interface DistributionDriverPahtListMigrationService {

	public ResponseObject getDistributionDriverPathListDetails(List<?> jaxbPathList, DistributionDriver distributionDriver, int staffId, String migrationPrefix) throws MigrationSMException;
}
