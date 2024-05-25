/**
 * 
 */
package com.elitecore.sm.migration.dao;

import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.migration.model.MigrationTrackDetails;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.services.model.MigrationStatusEnum;


/**
 * @author Ranjitsinh Reval
 *
 */
public interface MigrationTrackDao extends GenericDAO<MigrationTrackDetails>{

	public List<MigrationTrackDetails> getAllInstancesByStatus(MigrationStatusEnum migrationStatus);

	public Map<String, Object> getMigrationTrackPaginatedList();

	public List<MigrationTrackDetails> getMigrationTrackPaginatedList(Class<MigrationTrackDetails> classMigrationTrackDetails, List<Criterion> conditions, Map<String, String> aliases, int offset, int limit, String sortColumn, String sortOrder);
	
	public void clearSession();
	
	public List<MigrationTrackDetails> getMigrationTrackDetailsByIpAndPort(int serverId, int port);
}
