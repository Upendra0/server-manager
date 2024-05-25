package com.elitecore.sm.consolidationservice.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.consolidationservice.dao.IConsolidationGroupAttributeDao;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.consolidationservice.model.DataConsolidationGroupAttribute;
import com.elitecore.sm.parser.model.UnifiedFieldEnum;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.util.EliteUtils;

@Service(value = "consolidationGroupAttributeService")
public class ConsolidationGroupAttributeServiceImpl implements IConsolidationGroupAttributeService {
	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	IConsolidationGroupAttributeDao consolidationGroupAttributeDao;
	
	@Autowired
	private ServicesDao servicesDao;
	
	@Transactional
	@Override
	@Auditable(actionType = BaseConstants.CREATE_ACTION, auditActivity = AuditConstants.CREATE_CONSOLIDATION_DEFINITION_GROUP, currentEntity = DataConsolidationGroupAttribute.class, ignorePropList = "")
	public ResponseObject addConsolidationGroupAttributeList(DataConsolidationGroupAttribute dataConsolidationGroupAttribute, int iConsDefId) {
		ResponseObject responseObject = new ResponseObject();
			com.elitecore.sm.services.model.Service service = servicesDao.findByPrimaryKey(com.elitecore.sm.services.model.Service.class, dataConsolidationGroupAttribute.getDataConsolidation().getConsService().getId());
			servicesDao.update(service);
			consolidationGroupAttributeDao.save(dataConsolidationGroupAttribute);
			if (dataConsolidationGroupAttribute.getId() != 0) {
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_GROUP_ADD_SUCCESS);
				responseObject.setObject(dataConsolidationGroupAttribute);
				logger.info("Consolidation Defination Group Attribute List added successfully.");
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_GROUP_ADD_FAIL);
				logger.info("Consolidation Defination Group Attribute List added failed.");
			}
		return responseObject;
	}
	@Transactional
	@Override
	@Auditable(actionType = BaseConstants.UPDATE_ACTION, auditActivity = AuditConstants.UPDATE_CONSOLIDATION_DEFINITION_GROUP, currentEntity = DataConsolidationGroupAttribute.class, ignorePropList = "dataConsolidation")
	public ResponseObject updateConsolidationGroupAttributeList(DataConsolidationGroupAttribute dataConsolidationGroupAttribute, int iConsDefId) {
		ResponseObject responseObject = new ResponseObject();
		
			com.elitecore.sm.services.model.Service service = servicesDao.findByPrimaryKey(com.elitecore.sm.services.model.Service.class, dataConsolidationGroupAttribute.getDataConsolidation().getConsService().getId());
			servicesDao.update(service);
			consolidationGroupAttributeDao.merge(dataConsolidationGroupAttribute);
			if (dataConsolidationGroupAttribute.getId() != 0) {
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_GROUP_UPDATE_SUCCESS);
				responseObject.setObject(dataConsolidationGroupAttribute);
				logger.info("Consolidation Defination Group Attribute List update successfully.");
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_GROUP_UPDATE_FAIL);
				logger.info("Consolidation Defination Group Attribute List update failed.");
			}
		return responseObject;
	}
	
	@Transactional
	@Override
	public ResponseObject getConsolidationGroupById(int id) {
		ResponseObject responseObject = new ResponseObject();
		logger.debug("Get Consolidation Defination Group Attribute List");
		
		DataConsolidationGroupAttribute dataDefGroup = (DataConsolidationGroupAttribute) consolidationGroupAttributeDao.findByPrimaryKey(DataConsolidationGroupAttribute.class, id);
		if (dataDefGroup != null) {
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_GROUP_VIEW_SUCCESS);
			responseObject.setObject(dataDefGroup);
			logger.info("Get Consolidation Defination Group Attribute List Success");
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_GROUP_VIEW_FAIL);
			logger.info("Get Consolidation Defination Group Attribute List Fail");
		}
		return responseObject;
	}

	@Override
	public void importDataConsolidationGroupAttributeUpdateMode(DataConsolidationGroupAttribute dbGroupAttribute, DataConsolidationGroupAttribute exportedGroupAttribute) {
		dbGroupAttribute.setRegExEnable(exportedGroupAttribute.isRegExEnable());
		dbGroupAttribute.setRegExExpression(exportedGroupAttribute.getRegExExpression());
		dbGroupAttribute.setDestinationField(exportedGroupAttribute.getDestinationField());
		dbGroupAttribute.setLookUpEnable(exportedGroupAttribute.isLookUpEnable()); 
		dbGroupAttribute.setLookUpTableName(exportedGroupAttribute.getLookUpTableName());
		dbGroupAttribute.setLookUpTableColumnName(exportedGroupAttribute.getLookUpTableColumnName());
		dbGroupAttribute.setLastUpdatedDate(EliteUtils.getDateForImport(false));
	}
	
	@Override
	public void importDataConsolidationGroupAttributeAddAndKeepBothMode(DataConsolidationGroupAttribute exportedGroupAttribute, DataConsolidation dataConsolidation) {
		exportedGroupAttribute.setId(0);
		exportedGroupAttribute.setCreatedDate(EliteUtils.getDateForImport(false));
		exportedGroupAttribute.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		exportedGroupAttribute.setCreatedByStaffId(dataConsolidation.getCreatedByStaffId());
		exportedGroupAttribute.setLastUpdatedByStaffId(dataConsolidation.getLastUpdatedByStaffId());
		exportedGroupAttribute.setDataConsolidation(dataConsolidation);
	}
	
	@Override
	public DataConsolidationGroupAttribute getDataConsolidationGroupAttributeFromList(List<DataConsolidationGroupAttribute> groupAttributeList, String groupingField) {
		if(!CollectionUtils.isEmpty(groupAttributeList)) {
			int length = groupAttributeList.size();
			for(int i = length-1; i >= 0; i--) {
				DataConsolidationGroupAttribute groupAttribute = groupAttributeList.get(i);
				if(groupAttribute != null && !groupAttribute.getStatus().equals(StateEnum.DELETED)
						&& groupAttribute.getGroupingField().equals(groupingField)) {
					return groupAttributeList.remove(i);
				}
			}
		}
		return null;
	}

	@Transactional
	@Override
	public DataConsolidation getDataConsolidationById(int id) {
		return consolidationGroupAttributeDao.getDataConsolidationById(id);
	}
}