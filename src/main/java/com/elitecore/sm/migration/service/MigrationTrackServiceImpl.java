/**
 * 
 */
package com.elitecore.sm.migration.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.migration.dao.MigrationTrackDao;
import com.elitecore.sm.migration.model.MigrationTrackDetails;
import com.elitecore.sm.server.dao.ServerDao;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.services.model.MigrationStatusEnum;
import com.elitecore.sm.services.service.MigrationService;
import com.elitecore.sm.services.service.MigrationServiceImpl;
import com.elitecore.sm.util.MapCache;



/**
 * @author Ranjitsinh Reval
 *
 */
@org.springframework.stereotype.Service(value = "migrationTrackService")
public class MigrationTrackServiceImpl implements MigrationTrackService {


	private static Logger logger = Logger.getLogger(MigrationServiceImpl.class);
	
	@Autowired
	private MigrationTrackDao migrationTrackDao;
	
	@Autowired
	private ServerDao serverDao;
	
	@Autowired
	private MigrationService migrationService;
	
	
	/**
	 * Method will get all open instances for migration process. 
	 * 
	 */
	@Override
	@Transactional()
	public ResponseObject getAllOpenInstances(MigrationStatusEnum migrationStatus) {
		logger.debug("Fetching all open status instances for migration process for status " + migrationStatus);
		ResponseObject responseObject  = new ResponseObject();
		
		List<MigrationTrackDetails> instanceList = migrationTrackDao.getAllInstancesByStatus(migrationStatus);
		
		if(instanceList != null && !instanceList.isEmpty()){
			responseObject.setSuccess(true);
			responseObject.setObject(instanceList);
			logger.info("Instances list found successfully for migration.");
		}else{
			logger.info("Failed to get instances list for migration.");
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_OPEN_STATUS_INSTANCES);
		}
		
		return responseObject;
	}


	/**
	 * Method will update Migration track details. 
	 * 
	 */
	@Override
	@Transactional()
	public ResponseObject updateMigrationStatus(MigrationTrackDetails migrationObj) {
		logger.debug("Going to update migration details.");
		ResponseObject responseObject  = new ResponseObject();
		migrationTrackDao.merge(migrationObj);
		responseObject.setSuccess(true);
		return responseObject;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public void clearSession() {
		migrationTrackDao.clearSession();
	}


	@Override
	@Transactional
	public ResponseObject saveMigrationTrackDetail(MigrationTrackDetails migrationTrackDetails) throws SMException {
		Server server = serverDao.findByPrimaryKey(Server.class, migrationTrackDetails.getServer().getId());
		migrationTrackDetails.setServer(server);
		ResponseObject responseObject = migrationService.checkServerInstanceDetails(
				migrationTrackDetails.getServer().getIpAddress(),
				migrationTrackDetails.getServer().getId(),
				migrationTrackDetails.getServer().getUtilityPort(),
				migrationTrackDetails.getServerInstancePort(),
				migrationTrackDetails.getServerInstanceScriptName());
		if(responseObject.isSuccess()) {
			if(migrationTrackDetails.getId() > 0) {
				MigrationTrackDetails migrationTrackReference = migrationTrackDao.findByPrimaryKey(MigrationTrackDetails.class, migrationTrackDetails.getId());
				migrationTrackDetails.setMigrationStartDate(migrationTrackReference.getMigrationStartDate());
				migrationTrackDao.merge(migrationTrackDetails);
			} else {
				List<MigrationTrackDetails> migrationTrackList = migrationTrackDao.getMigrationTrackDetailsByIpAndPort( migrationTrackDetails.getServer().getId(), migrationTrackDetails.getServerInstancePort());
				if(migrationTrackList != null && !migrationTrackList.isEmpty()) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.MIGRATION_TRACK_DETAIL_DUPLICATE_FOUND);
					return responseObject;
				} else {
					migrationTrackDao.save(migrationTrackDetails);
				}
			}
			
			if(migrationTrackDetails.getId() > 0) {
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.MIGRATION_TRACK_DETAIL_ADD_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.MIGRATION_TRACK_DETAIL_ADD_FAIL);
			}
			responseObject.setObject(migrationTrackDetails);
		} 
		
		return responseObject;
	}


	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<Map<String, Object>> getMigrationTrackDetailsPaginatedList(int migrationTrackId, int startIndex, int limit, String sidx, String sord) {
		List<Map<String, Object>> rowList = new ArrayList<>();
		Map<String, Object> row;
		Map<String, Object> conditionsAndAliases = migrationTrackDao.getMigrationTrackPaginatedList();

		List<MigrationTrackDetails> migrationTrackDetailList = migrationTrackDao.getMigrationTrackPaginatedList(MigrationTrackDetails.class,
				(List<Criterion>) conditionsAndAliases.get(BaseConstants.CONDITIONS),
				(HashMap<String, String>) conditionsAndAliases.get(BaseConstants.ALIASES), startIndex, limit, sidx, sord);

		if (migrationTrackDetailList != null && !migrationTrackDetailList.isEmpty()) {
			for (MigrationTrackDetails migrationTrackDetails : migrationTrackDetailList) {
				row = new HashMap<>();

				row.put("id", migrationTrackDetails.getId());
				row.put("ServerId", migrationTrackDetails.getServer().getId());
				row.put("ServerTypeId", migrationTrackDetails.getServer().getServerType().getId());
				row.put("ServerIPAndPort", migrationTrackDetails.getServer().getIpAddress()+":"+migrationTrackDetails.getServer().getUtilityPort());
				row.put("ServerInstancePort", migrationTrackDetails.getServerInstancePort());
				row.put("MigrationPrefix", migrationTrackDetails.getServerInstancePrefix());
				row.put("SIScriptName", migrationTrackDetails.getServerInstanceScriptName());
				row.put("MigrationStatus", migrationTrackDetails.getMigrationStatus().name());
				row.put("Reprocess", migrationTrackDetails.getMigrationStatus().name());
				row.put("Attempts", migrationTrackDetails.getReprocessAttemptNumber());
				row.put("MigrationDetail", migrationTrackDetails.getRemark());
				row.put("ServicesList", migrationTrackDetails.getServicesList());
				row.put(BaseConstants.EDIT, "Edit");
				rowList.add(row);

			}
		}
		return rowList;
	}


	@Transactional(readOnly = true)
	@Override
	public long getMigrationTrackTotalCount() {
		List<Criterion> conditions = new ArrayList<>();
		conditions.add(Restrictions.ne(BaseConstants.STATUS, StateEnum.DELETED));
		return migrationTrackDao.getQueryCount(MigrationTrackDetails.class,	conditions,	null);
	}


	@Override
	@Transactional
	public ResponseObject updateFailedMigrationStatus(int migrationTrackDetailId) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		MigrationTrackDetails migrationTrackDetails = migrationTrackDao.findByPrimaryKey(MigrationTrackDetails.class, migrationTrackDetailId);
		if(migrationTrackDetails != null) {
			migrationTrackDetails.setMigrationStatus(MigrationStatusEnum.REPROCESS);
			migrationTrackDao.merge(migrationTrackDetails);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.MIGRATION_TRACK_DETAIL_UPDATE_SUCCESS);
		} else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.MIGRATION_TRACK_DETAIL_UPDATE_FAIL);
		}
		responseObject.setObject(migrationTrackDetails);
		return responseObject;
	}
	
	@Override
	public ResponseObject isMigrationInProcess() throws SMException{
		ResponseObject responseObject = new ResponseObject();
		responseObject.setSuccess(true);
		responseObject.setObject((Boolean) MapCache.getConfigValueAsObject(BaseConstants.IS_MIGRATION_IN_PROCESS));
		return responseObject;
	}

	@Override
	@Transactional
	public ResponseObject getMigrationTrackDetailById(int migrationTrackDetailId) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		MigrationTrackDetails migrationTrackDetails = migrationTrackDao.findByPrimaryKey(MigrationTrackDetails.class, migrationTrackDetailId);
		if(migrationTrackDetails != null) {
			responseObject.setSuccess(true);
		} else{
			responseObject.setSuccess(false);
		}
		responseObject.setObject(migrationTrackDetails);
		return responseObject;
	}


	@Override
	@Transactional
	public ResponseObject deleteMigrationTrackDetailByIds(Integer[] migrationTrackIds, int staffId) throws SMException {
		ResponseObject responseObject = new ResponseObject();
		if(migrationTrackIds != null && migrationTrackIds.length > 0) {
			Date date = new Date();
			int length = migrationTrackIds.length;
			for(int i = length-1; i >= 0 ;i--) {
				MigrationTrackDetails migrationTrackDetails = migrationTrackDao.findByPrimaryKey(MigrationTrackDetails.class, migrationTrackIds[i]);
				migrationTrackDetails.setLastUpdatedDate(date);
				migrationTrackDetails.setLastUpdatedByStaffId(staffId);
				migrationTrackDetails.setStatus(StateEnum.DELETED);
				migrationTrackDao.merge(migrationTrackDetails);
			}
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.MIGRATION_TRACK_DETAIL_DELETE_SUCCESS);
		}
		return responseObject;
	}
}
