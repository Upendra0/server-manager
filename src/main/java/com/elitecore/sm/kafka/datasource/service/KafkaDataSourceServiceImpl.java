package com.elitecore.sm.kafka.datasource.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.kafka.datasource.dao.KafkaDataSourceDao;
import com.elitecore.sm.kafka.datasource.model.KafkaDataSourceConfig;
import com.elitecore.sm.netflowclient.dao.NetflowClientDao;
import com.elitecore.sm.netflowclient.model.NetflowClient;
import com.elitecore.sm.services.model.NetflowBinaryCollectionService;
import com.elitecore.sm.util.EliteUtils;

@Service(value="kafkaDataSourceService")
public class KafkaDataSourceServiceImpl implements KafkaDataSourceService {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	private KafkaDataSourceDao kafkaDataSourceDao;
	
	@Autowired
	private NetflowClientDao netflowClientDao;
	
	/**
	 * Fetch KafkaDataSource Details for synchronization
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseObject getKafkaDataSourceConfigList() {
		
		ResponseObject responseObject = new ResponseObject();
		
		List<KafkaDataSourceConfig> kafkaDataSourceConfigList = kafkaDataSourceDao.getKafkaDataSourceConfigList();
		
		if (kafkaDataSourceConfigList != null){
			responseObject.setSuccess(true);
			responseObject.setObject(kafkaDataSourceConfigList);
		} else {
			responseObject.setObject(new ArrayList<KafkaDataSourceConfig>());
			responseObject.setSuccess(false);
		}
		
		return responseObject;
	}

	/**
	 * Add KafkaDataSource into database while creating new KafkaDataSource explicitly from GUI
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.CREATE_KAFKA_DATASOURCE, actionType = BaseConstants.CREATE_ACTION, currentEntity = KafkaDataSourceConfig.class, ignorePropList= "")	
	public ResponseObject addKafkaDataSourceConfig(KafkaDataSourceConfig defaultKafkaDataSource) {
		ResponseObject responseObject = new ResponseObject();
		//check if same KafkaDataSource Name already exits in DB
		if(checkForDuplicateKafkaDSName(defaultKafkaDataSource.getName())){
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.KAFKA_DATASOURCE_ADD_FAIL_DUPLICATE_KAKFA_DATASOURCE_NAME);
			return responseObject;
		}
		List<KafkaDataSourceConfig> kafkaDataSourceConfigList = kafkaDataSourceDao.getKafkaDSConfigByIPandPort(defaultKafkaDataSource.getKafkaServerIpAddress(), defaultKafkaDataSource.getKafkaServerPort());
		boolean dsConfigExists = false;
		if (kafkaDataSourceConfigList != null && !kafkaDataSourceConfigList.isEmpty()){
			//Kafka Data Source with same server IP and server Port already exist in DB
			dsConfigExists= true;
			
		}else{
			kafkaDataSourceDao.save(defaultKafkaDataSource);
		}
		if(defaultKafkaDataSource.getId() !=0) {
			responseObject.setSuccess(true);
			responseObject.setObject(defaultKafkaDataSource);
			responseObject.setResponseCode(ResponseCode.KAFKA_DATASOURCE_INSERT_SUCCESS);
		} else if(!dsConfigExists) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.KAFKA_DATASOURCE_INSERT_FAIL);
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.KAFKADATASOURCE_ADD_FAIL_DUPLICATE_SERVER_IP_PORT);
		}
		return responseObject;
	}
	
	private boolean checkForDuplicateKafkaDSName(String dataSourceName) {
		boolean duplicateExists = false;
		List<KafkaDataSourceConfig> kafkaDataSourceConfigList = kafkaDataSourceDao.getKafkaDataSourceByName(dataSourceName);
		if(kafkaDataSourceConfigList!=null && !kafkaDataSourceConfigList.isEmpty())
			duplicateExists = true;
		
		return duplicateExists;
	}
	
	/**
	 * Update KafkaDataSource Configuration Details  
	 * 
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_KAFKA_DATASOURCE, actionType = BaseConstants.UPDATE_ACTION, currentEntity = KafkaDataSourceConfig.class, ignorePropList= "")
	public ResponseObject updateKafkaDataSourceConfig(KafkaDataSourceConfig kafkaDataSourceConfig) {

		ResponseObject responseObject = new ResponseObject();
	
		if(isKafkaDataSourceUniqueForUpdate(kafkaDataSourceConfig.getId(), kafkaDataSourceConfig.getName())) {	
			
			List<KafkaDataSourceConfig> kafkaDataSourceConfigList = kafkaDataSourceDao.getKafkaDSConfigByIPandPort(kafkaDataSourceConfig.getKafkaServerIpAddress(), kafkaDataSourceConfig.getKafkaServerPort());
			boolean dsConfigExists = false;
			if (kafkaDataSourceConfigList != null && !kafkaDataSourceConfigList.isEmpty()){
				//KafkaDataSource with same server IP and Port already exist in DB
				if(kafkaDataSourceConfigList.size()==1 && kafkaDataSourceConfigList.get(0).getId()==kafkaDataSourceConfig.getId()){
					dsConfigExists= false;
				} else {
					dsConfigExists= true;
				}
			}
			if(!dsConfigExists){
				kafkaDataSourceConfig.setLastUpdatedByStaffId(kafkaDataSourceConfig.getLastUpdatedByStaffId());
				kafkaDataSourceConfig.setLastUpdatedDate(new Date());
				kafkaDataSourceDao.merge(kafkaDataSourceConfig);			
				logger.info("KafkaDataSource Configuration  details updated successfully.");	
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.KAFKA_DATASOURCE_UPDATE_SUCCESS);
				responseObject.setObject(kafkaDataSourceConfig);
			} else{
				logger.debug("inside updateKafkaDataSourceConfig : duplicate KafkaDataSource Server IP and Port combination found in update:" + kafkaDataSourceConfig.getName());
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.KAFKADATASOURCE_UPDATE_FAIL_DUPLICATE_SERVER_IP_PORT);
			}
			
		} else {
			logger.debug("inside updateKafkaDataSourceConfig : duplicate kafkaDataSourceConfiguration name found in update:" + kafkaDataSourceConfig.getName());
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_KAFKA_DATASOURCE_NAME);
			
		}

		
		return responseObject;
	}
	
	/**
	 * Check KafkaDataSource name is unique in case of update 
	 * @param kafkaDataSourceConfigId
	 * @param kafkaDataSourceName
	 * @return ResponseObject
	 */
	@Transactional
	public boolean  isKafkaDataSourceUniqueForUpdate(int kafkaDataSourceConfigId,String kafkaDataSourceName){
		List<KafkaDataSourceConfig> kafkaDataSourceConfigList = kafkaDataSourceDao.getKafkaDataSourceByName(kafkaDataSourceName);
		boolean isUnique=false;
		if(kafkaDataSourceConfigList!=null && !kafkaDataSourceConfigList.isEmpty()){
			for(KafkaDataSourceConfig kafkaDataSourceConfig:kafkaDataSourceConfigList){
				//If ID is same , then it is same KafkaDataSourceConfig object
				if(kafkaDataSourceConfigId == (kafkaDataSourceConfig.getId())){
					isUnique=true;
				}else{ // It is another KafkaDataSourceConfig object , but name is same
					isUnique=false;
				}
			}
		}else if(kafkaDataSourceConfigList!=null && kafkaDataSourceConfigList.isEmpty()){ // No KafkaDataSourceConfig found with same name 
			isUnique=true;
		}
		
		return isUnique;
	}

	/**
	 * 	Delete KakfaDataSource Configuration Details 
	 *  
	 */
	
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.DELETE_KAFKA_DATASOURCE, actionType = BaseConstants.DELETE_ACTION, currentEntity = KafkaDataSourceConfig.class, ignorePropList= "")
	public ResponseObject deleteKafkaDataSourceConfig(int kafkaDataSourceId) {
		ResponseObject responseObject = new ResponseObject();
		KafkaDataSourceConfig kafkaDataSourceConfigList = kafkaDataSourceDao.findByPrimaryKey(KafkaDataSourceConfig.class, kafkaDataSourceId);
		if(kafkaDataSourceConfigList !=null){
			//mark the status for the KafkaDataSource as DELETED
			kafkaDataSourceConfigList.setName(EliteUtils.checkForNames(BaseConstants.ACTION_DELETE,kafkaDataSourceConfigList.getName()));
			kafkaDataSourceConfigList.setStatus(StateEnum.DELETED);
			kafkaDataSourceConfigList.setLastUpdatedByStaffId(kafkaDataSourceConfigList.getLastUpdatedByStaffId());
			kafkaDataSourceConfigList.setLastUpdatedDate(new Date());

			kafkaDataSourceDao.merge(kafkaDataSourceConfigList);

			responseObject.setSuccess(true);
			responseObject.setObject(kafkaDataSourceConfigList);
			responseObject.setResponseCode(ResponseCode.KAFKA_DATASOURCE_DELETE_SUCCESS);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.KAFKA_DATASOURCE_DELETE_FAIL);
		}
		return responseObject;
	}

	@Override
	@Transactional
	public List<NetflowClient> getClientListByKafkaDSId(int kafkaDSId, int startIndex, int limit) {
		List<NetflowClient> clientList = netflowClientDao.getClientListByKafkaDSId(kafkaDSId, startIndex, limit);
		for(NetflowClient client : clientList) {
			Hibernate.initialize(client.getService());
			Hibernate.initialize(client.getService().getServerInstance());
		}
		return clientList;
	}

	@Override
	@Transactional
	public int getClientCountByKafkaDSId(int kafkaDSId) {
		int count = 0;
		try {
			count = netflowClientDao.getClientCountByKafkaDSId(kafkaDSId);
		} catch(Exception e) {
			logger.error("Problem while getting associated client count.", e);
		}
		
		return count;
	}

	@Override
	@Transactional(readOnly=true)
	public KafkaDataSourceConfig getKafkaDataSourceConfigById(Integer kafkaDSId) {
		return kafkaDataSourceDao.getKafkaDataSourceConfigById(kafkaDSId);
	}

	/**
	 * Method will check if kafka datasource with same config exists or not, if not will create new Kafka DS
	 * @param exportedDataSource
	 * @param serverInstanceDB
	 * @return ResponseObject
	 */
	@Override
	@Transactional
	public ResponseObject createKafkaDataSourceForImport(KafkaDataSourceConfig exportedKafkaDataSource,NetflowBinaryCollectionService exportedService) {
		ResponseObject responseObject = new ResponseObject();
		List<KafkaDataSourceConfig> kafkaDataSourceConfigList = kafkaDataSourceDao.getKafkaDSConfigByIPandPort(exportedKafkaDataSource.getKafkaServerIpAddress(), exportedKafkaDataSource.getKafkaServerPort());
		if (kafkaDataSourceConfigList != null && !kafkaDataSourceConfigList.isEmpty()){
			responseObject.setObject(kafkaDataSourceConfigList.get(0));
			responseObject.setSuccess(true);
		}
		else{
			exportedKafkaDataSource.setId(0);
			exportedKafkaDataSource.setName(EliteUtils.checkForNames(BaseConstants.ACTION_IMPORT,exportedKafkaDataSource.getName()));
			exportedKafkaDataSource.setLastUpdatedByStaffId(exportedService.getCreatedByStaffId());
			exportedKafkaDataSource.setCreatedByStaffId(exportedService.getCreatedByStaffId());
			exportedKafkaDataSource.setCreatedDate(new Date());
			kafkaDataSourceDao.save(exportedKafkaDataSource);
			responseObject.setObject(exportedKafkaDataSource);
			responseObject.setSuccess(true);
		}
		return responseObject;
	}
}
