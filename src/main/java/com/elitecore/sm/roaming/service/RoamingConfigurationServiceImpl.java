package com.elitecore.sm.roaming.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.pathlist.model.RoamingFileSequenceMgmt;
import com.elitecore.sm.roaming.dao.RoamingConfigurationDao;
import com.elitecore.sm.roaming.model.FileManagementData;
import com.elitecore.sm.roaming.model.HostConfiguration;
import com.elitecore.sm.roaming.model.RoamingConfiguration;
import com.elitecore.sm.roaming.model.RoamingParameter;
import com.elitecore.sm.roaming.model.TestSimManagement;



@Service(value = "roamingConfigurationService")
public class RoamingConfigurationServiceImpl implements RoamingConfigurationService {
	@Autowired
	private RoamingConfigurationDao roamingConfigurationDao;
	
	
	@Override
	@Transactional
	@Auditable(actionType = BaseConstants.UPDATE_ACTION, auditActivity = AuditConstants.UPDATE_ROAMING_PARAMETER, currentEntity = RoamingConfiguration.class, ignorePropList = "")
	public ResponseObject saveRoamingConfigurationDetails(RoamingConfiguration roamingConfiguration) {
		ResponseObject responseObject = new ResponseObject();
		if(roamingConfiguration instanceof HostConfiguration){
			HostConfiguration hostConfiguration =  (HostConfiguration)roamingConfiguration;
		hostConfiguration.setId(1);
		roamingConfigurationDao.merge(hostConfiguration);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.HOST_CONFIGURATION_UPDATE_SUCCESS);
			responseObject.setObject(hostConfiguration);
		}else if (roamingConfiguration instanceof RoamingParameter) {
			RoamingParameter roamingParameter =  (RoamingParameter)roamingConfiguration;
			roamingParameter.setId(1);
			roamingConfigurationDao.merge(roamingParameter);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.ROAMING_PARAMETER_UPDATE_SUCCESS);
			responseObject.setObject(roamingParameter);
		}
		return responseObject;
	}
	@Override
	public ResponseObject loadRoamingConfigurationDetails(String requestActionType) {
		ResponseObject responseObject = new ResponseObject();
		RoamingConfiguration loadData = roamingConfigurationDao.loadData(requestActionType);
		responseObject.setObject(loadData);
			return responseObject;
		
	}
	@Override
	@Transactional
	@Auditable(actionType = BaseConstants.UPDATE_ACTION, auditActivity = AuditConstants.UPDATE_FILE_SEQUENCE_MANAGEMENT, currentEntity = RoamingFileSequenceMgmt.class, ignorePropList = "partner")
	public ResponseObject saveFileSequence( RoamingFileSequenceMgmt missingFileSequenceMgmt) {
		ResponseObject responseObject = new ResponseObject();
		roamingConfigurationDao.merge(missingFileSequenceMgmt);
		responseObject.setObject(missingFileSequenceMgmt);
		responseObject.setSuccess(true);
		return responseObject;	
	}
	@Override
	public List<RoamingFileSequenceMgmt> getFileSequenceDetails(int id){
		List<RoamingFileSequenceMgmt> fileSequenceDetails = roamingConfigurationDao.loadFileSequenceDetails(id);

		return fileSequenceDetails;
		
	}
	@Override
	@Auditable(actionType = BaseConstants.UPDATE_ACTION, auditActivity = AuditConstants.UPDATE_TEST_SIM_MANAGEMNT, currentEntity = TestSimManagement.class, ignorePropList = "partner")
	@Transactional
	public ResponseObject saveOrUpdateTestSimManagementDetails(TestSimManagement testSimManagement) {
		ResponseObject responseObject = new ResponseObject();
		roamingConfigurationDao.merge(testSimManagement);
		responseObject.setObject(testSimManagement);
		responseObject.setSuccess(true);
		return responseObject;
	}
	@Override
	public List<TestSimManagement> getTestSimManagement(int id,String type) {
		List<TestSimManagement> inboudOutboundTestSimData = roamingConfigurationDao.getInboudOutboundTestSimData(id,type);
		
		return inboudOutboundTestSimData;
	} 
	@Override
	public List<TestSimManagement> getTestSimManagement(int id) {
		List<TestSimManagement> inboudOutboundTestSimData = roamingConfigurationDao.getInboudOutboundTestSimData(id);
		return inboudOutboundTestSimData;
	}
	@Override
	@Transactional
	@Auditable(actionType = BaseConstants.UPDATE_ACTION, auditActivity = AuditConstants.UPDATE_FILE_MANAGEMENT, currentEntity = FileManagementData.class, ignorePropList = "partner")
	public ResponseObject saveOrUpdateFileManagementDetails(FileManagementData fileManagementData) {
		ResponseObject responseObject = new ResponseObject();
		roamingConfigurationDao.merge(fileManagementData);
		responseObject.setObject(fileManagementData);;
		responseObject.setSuccess(true);
		return responseObject;
	}
	@Override
	public List<FileManagementData> getFileManagementData(int id, String servicetype) {
	
		List<FileManagementData> testOrCommercialFileManagementData = roamingConfigurationDao.getTestOrCommercialFileManagementData(id,servicetype);
		return testOrCommercialFileManagementData;
	}
	@Override
	public List<FileManagementData> getFileManagementData(int id) {
		List<FileManagementData> testOrCommercialFileManagementData = roamingConfigurationDao.getTestOrCommercialFileManagementData(id);
		return testOrCommercialFileManagementData;
	}

}
