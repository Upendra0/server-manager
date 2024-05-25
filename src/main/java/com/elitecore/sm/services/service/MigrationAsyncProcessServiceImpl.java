/**
 * 
 */
package com.elitecore.sm.services.service;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.exception.MigrationSMException;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.migration.model.MigrationTrackDetails;
import com.elitecore.sm.migration.service.MigrationTrackService;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.services.model.MigrationStatusEnum;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.MigrationUtil;


/**
 * @author Ranjitsinh Reval	
 *
 */
@Service(value="migrationAsyncProcessService")
public class MigrationAsyncProcessServiceImpl implements MigrationAsyncProcessService {

	
	@Autowired
	MigrationTrackService migrationTrackService;

	@Autowired
	private MigrationService migrationService;
	
	@Autowired
	@Qualifier(value="eliteUtilsQualifier")
	protected EliteUtils eliteUtils ;
	
	@Autowired
	private MigrationUtil migrationUtil;
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	@Async
	public ResponseObject doMigration(MigrationStatusEnum migrationStatus)  throws MigrationSMException{
		
		ResponseObject responseObject = migrationTrackService.getAllOpenInstances(migrationStatus);
		MapCache.addConfigObject(BaseConstants.IS_MIGRATION_IN_PROCESS, true);
		if(responseObject.isSuccess()){
			List<MigrationTrackDetails> instanceList  = (List<MigrationTrackDetails>) responseObject.getObject();
			
			for (int i = 0; i < instanceList.size(); i++) {
				
				MigrationTrackDetails migrationTrackDetails = instanceList.get(i);
				try {
					
					migrationTrackDetails.setMigrationStatus(MigrationStatusEnum.INPROGRESS);
					migrationTrackDetails.setMigrationStartDate(new Date());
					
					if(migrationStatus.equals(MigrationStatusEnum.REPROCESS)){
					  int attempNo = migrationTrackDetails.getReprocessAttemptNumber();
					  attempNo++;
					  migrationTrackDetails.setReprocessAttemptNumber(attempNo);
					}
					
					migrationTrackService.updateMigrationStatus(migrationTrackDetails);
					
					
					Server server = migrationTrackDetails.getServer();
					logger.info("migration process started for server ip " + server.getIpAddress() + " port :: " + migrationTrackDetails.getServerInstancePort());
					responseObject = migrationService.readServerConfiguration(server.getIpAddress(), migrationTrackDetails.getServerInstancePrefix(), server.getServerType().getId(), migrationTrackDetails.getServerInstancePort(), 1, migrationTrackDetails.getServerInstanceScriptName(),migrationTrackDetails.getServicesList());
					
					if(responseObject.isSuccess()){
						logger.info("Migration done successfully for ipaddress " + server.getIpAddress() + " and port " + migrationTrackDetails.getServerInstancePort());
						migrationTrackDetails.setMigrationStatus(MigrationStatusEnum.SUCCESS);
						migrationTrackDetails.setMigrationEndDate(new Date());
						migrationTrackDetails.setRemark("Migration done successfully!");
						migrationTrackService.updateMigrationStatus(migrationTrackDetails);
					}else{
						logger.info("Migration failed for ipaddress " + server.getIpAddress() + " and port " + migrationTrackDetails.getServerInstancePort());
						migrationTrackDetails.setMigrationStatus(MigrationStatusEnum.FAILED);
						migrationTrackDetails.setMigrationEndDate(new Date());
						migrationTrackDetails.setRemark(migrationUtil.getFixedLengthJsonObjectFromAjaxResponse(eliteUtils.convertToAjaxResponse(responseObject)).toString());
						logger.info("Remark [Error log for Migration] : "+eliteUtils.convertToAjaxResponse(responseObject));
						migrationTrackService.updateMigrationStatus(migrationTrackDetails);
					}
				} catch (MigrationSMException e) {
					migrationTrackDetails.setMigrationStatus(MigrationStatusEnum.FAILED);
					migrationTrackDetails.setMigrationEndDate(new Date());
					logger.info("Remark [Error log for Migration] : "+eliteUtils.convertToAjaxResponse(e.getResponseObject()));
					migrationTrackDetails.setRemark(migrationUtil.getFixedLengthJsonObjectFromAjaxResponse(eliteUtils.convertToAjaxResponse(e.getResponseObject())).toString());
					migrationTrackService.updateMigrationStatus(migrationTrackDetails);
					logger.error(e);
					responseObject.setSuccess(false);
				}catch(Exception e){
					migrationTrackDetails.setMigrationStatus(MigrationStatusEnum.FAILED);
					migrationTrackDetails.setMigrationEndDate(new Date());
					logger.info("[Error log for Migration] : "+e.getMessage());
					migrationTrackDetails.setRemark(migrationUtil.getFixedLengthString(e.getMessage()));
					migrationTrackService.updateMigrationStatus(migrationTrackDetails);
					logger.error(e);
				}
			} 
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FAIL_GET_OPEN_STATUS_INSTANCES);
		}
		MapCache.addConfigObject(BaseConstants.IS_MIGRATION_IN_PROCESS, false);
		return responseObject;
	}
}
