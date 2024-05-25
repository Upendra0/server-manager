/**
 * 
 */
package com.elitecore.sm.migration.service;

import com.elitecore.core.util.mbean.data.config.CrestelNetConfigurationData;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseObject;


/**
 * @author Ranjitsinh Reval
 *
 */
public interface ServerInstanceMigrationService {

	public ResponseObject readServerConfiguration(CrestelNetConfigurationData crestelNetConfigData, String ipAddres,String utilityPort, String serverType, int instancePort, int staffId, String migrationPrefix, String scriptFileName) throws MigrationSMException; 
}
