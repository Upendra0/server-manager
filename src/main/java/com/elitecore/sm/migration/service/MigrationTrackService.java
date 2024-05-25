/**
 * 
 */
package com.elitecore.sm.migration.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.migration.model.MigrationTrackDetails;
import com.elitecore.sm.services.model.MigrationStatusEnum;


/**
 * @author Ranjitsinh Reval
 *
 */
public interface MigrationTrackService {

	public ResponseObject getAllOpenInstances(MigrationStatusEnum migrationStatus);
	
	public ResponseObject updateMigrationStatus(MigrationTrackDetails migrationObj);
	
	public ResponseObject saveMigrationTrackDetail(MigrationTrackDetails migrationTrackDetails) throws SMException;
	
	public List<Map<String, Object>> getMigrationTrackDetailsPaginatedList(int migrationTrackId, int startIndex, int limit, String sidx, String sord);
	
	public long getMigrationTrackTotalCount();

	public ResponseObject updateFailedMigrationStatus(int migrationTrackDetailId) throws SMException;

	public ResponseObject getMigrationTrackDetailById(int migrationTrackDetailId) throws SMException;
	
	public ResponseObject deleteMigrationTrackDetailByIds(Integer[] migrationTrackIds, int staffId) throws SMException;
	
	public ResponseObject isMigrationInProcess() throws SMException;
	
	public void clearSession();
	
}
