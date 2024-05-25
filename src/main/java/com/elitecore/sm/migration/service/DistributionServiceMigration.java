/**
 * 
 */
package com.elitecore.sm.migration.service;

import java.util.List;

import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.services.model.Service;


/**
 * @author Ranjitsinh Reval
 *
 */
public interface DistributionServiceMigration {

	public ResponseObject getDistributionServiceAndDependents(List<Service> serviceInstanceList,int position, Service service,ServerInstance serverInstance,int staffId, String migrationPrefix) throws MigrationSMException;
}
