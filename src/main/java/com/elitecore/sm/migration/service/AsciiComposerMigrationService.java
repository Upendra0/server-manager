/**
 * 
 */
package com.elitecore.sm.migration.service;

import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseObject;


/**
 * @author Ranjitsinh Reval
 *
 */
public interface AsciiComposerMigrationService {

	
	public ResponseObject getAsciiComposerPluginList(Class<?> destinationClass, String migrationPrefix, int staffId) throws MigrationSMException;
	
}
