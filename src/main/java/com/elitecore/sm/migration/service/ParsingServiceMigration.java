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
 * @author vandana.awatramani
 *
 */
public interface ParsingServiceMigration {
	
	/**
	 * This method reads parsing service xml and its dependent entities
	 * 
	 * @param serviceInstanceList list of services in the parent server instance
	 * @param position position of this service in the list
	 * @param service actual service object which is super and needs to be filled with details of PArsing service
	 * @param folderDirPath the temp path from where XML config is to be read.
	 * @param serverInstance SI to which this service belongs.
	 * @param staffId Staff id doing this activity
	 * @param migrationPrefix prefix for naming algorithm
	 * @return ResponseObject with details filled from XML.
	 */
	ResponseObject getParsingServiceAndDependents(
			List<Service> serviceInstanceList, int position, Service service,
			String folderDirPath, ServerInstance serverInstance, int staffId,
			String migrationPrefix) throws MigrationSMException;

}
