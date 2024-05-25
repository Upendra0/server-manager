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
import com.elitecore.sm.consolidationservice.dao.IConsolidationAttributeDao;
import com.elitecore.sm.consolidationservice.dao.IConsolidationGroupAttributeDao;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.consolidationservice.model.DataConsolidationAttribute;
import com.elitecore.sm.services.dao.ServicesDao;
import com.elitecore.sm.util.EliteUtils;

@Service(value = "consolidationAttributeService")
public class ConsolidationAttributeServiceImpl implements IConsolidationAttributeService {

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Autowired
	IConsolidationAttributeDao consolidationAttributeDao;

	@Autowired
	IConsolidationGroupAttributeDao consolidationGroupAttributeDao;

	@Autowired
	private ServicesDao servicesDao;

	@Transactional
	@Override
	@Auditable(actionType = BaseConstants.CREATE_ACTION, auditActivity = AuditConstants.CREATE_CONSOLIDATION_DEFINITION_ATTRIBUTE, currentEntity = DataConsolidationAttribute.class, ignorePropList = "")
	public ResponseObject addConsolidationAttributeList(DataConsolidationAttribute dataConsolidationAttribute,
			int iConsDefId) {
		ResponseObject responseObject = new ResponseObject();
		com.elitecore.sm.services.model.Service service = servicesDao.findByPrimaryKey(
				com.elitecore.sm.services.model.Service.class,
				dataConsolidationAttribute.getDataConsolidation().getConsService().getId());
		servicesDao.update(service);
		consolidationAttributeDao.save(dataConsolidationAttribute);
		if (dataConsolidationAttribute.getId() != 0) {
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_ATTRIBUTE_ADD_SUCCESS);
			responseObject.setObject(dataConsolidationAttribute);
			logger.info("Consolidation Defination Attribute added successfully.");
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_ATTRIBUTE_ADD_FAIL);
			logger.info("Consolidation Defination Attribute added failed.");
		}
		return responseObject;
	}

	@Transactional
	@Override
	@Auditable(actionType = BaseConstants.UPDATE_ACTION, auditActivity = AuditConstants.UPDATE_CONSOLIDATION_DEFINITION_ATTRIBUTE, currentEntity = DataConsolidationAttribute.class, ignorePropList = "dataConsolidation")
	public ResponseObject updateConsolidationAttributeList(DataConsolidationAttribute dataConsolidationAttribute,
			int iConsDefId) {
		ResponseObject responseObject = new ResponseObject();
		com.elitecore.sm.services.model.Service service = servicesDao.findByPrimaryKey(
				com.elitecore.sm.services.model.Service.class,
				dataConsolidationAttribute.getDataConsolidation().getConsService().getId());
		servicesDao.update(service);
		consolidationAttributeDao.merge(dataConsolidationAttribute);
		if (dataConsolidationAttribute.getId() != 0) {
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_ATTRIBUTE_UPDATE_SUCCESS);
			responseObject.setObject(dataConsolidationAttribute);
			logger.info("Consolidation Defination Attribute Updated Successfully.");
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_ATTRIBUTE_UPDATE_FAIL);
			logger.info("Consolidation Defination Attribute Updated Failed.");
		}
		return responseObject;
	}

	@Transactional
	@Override
	public ResponseObject getConsolidationAttributeById(int id) {
		ResponseObject responseObject = new ResponseObject();
		DataConsolidationAttribute dataConsolidationAttribute = (DataConsolidationAttribute) consolidationAttributeDao
				.findByPrimaryKey(DataConsolidationAttribute.class, id);
		if (dataConsolidationAttribute != null) {
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_ATTRIBUTE_VIEW_SUCCESS);
			responseObject.setObject(dataConsolidationAttribute);
		} else {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.CONSOLIDATION_DEFINITION_ATTRIBUTE_VIEW_FAIL);
		}
		return responseObject;
	}

	@Override
	public void importDataConsolidationAttributeUpdateMode(DataConsolidationAttribute dbAttribute,
			DataConsolidationAttribute exportedAttribute) {
		dbAttribute.setDataType(exportedAttribute.getDataType());
		dbAttribute.setOperation(exportedAttribute.getOperation());
		dbAttribute.setDescription(exportedAttribute.getDescription());
	}

	@Override
	public void importDataConsolidationAttributeAddAndKeepBothMode(DataConsolidationAttribute exportedAttribute,
			DataConsolidation dataConsolidation, int importMode) {
		exportedAttribute.setId(0);
		exportedAttribute.setCreatedDate(EliteUtils.getDateForImport(false));
		exportedAttribute.setLastUpdatedDate(EliteUtils.getDateForImport(false));
		exportedAttribute.setCreatedByStaffId(dataConsolidation.getCreatedByStaffId());
		exportedAttribute.setLastUpdatedByStaffId(dataConsolidation.getLastUpdatedByStaffId());
		if (importMode == BaseConstants.IMPORT_MODE_KEEP_BOTH) {
			exportedAttribute
					.setFieldName(EliteUtils.checkForNames(BaseConstants.IMPORT, exportedAttribute.getFieldName()));
		}
		exportedAttribute.setDataConsolidation(dataConsolidation);
	}

	@Override
	public DataConsolidationAttribute getDataConsolidationAttributeFromList(
			List<DataConsolidationAttribute> consolidationAttributeList, String fieldName) {
		if (!CollectionUtils.isEmpty(consolidationAttributeList)) {
			int length = consolidationAttributeList.size();
			for (int i = length - 1; i >= 0; i--) {
				DataConsolidationAttribute consolidationAttribute = consolidationAttributeList.get(i);
				if (consolidationAttribute != null && !consolidationAttribute.getStatus().equals(StateEnum.DELETED)
						&& consolidationAttribute.getFieldName().equalsIgnoreCase(fieldName)) {
					return consolidationAttributeList.remove(i);
				}
			}
		}
		return null;
	}

	@Override
	@Transactional
	public DataConsolidation getDataConsolidationById(int iConsDefId) {
		DataConsolidation dataConsolidationById = consolidationGroupAttributeDao.getDataConsolidationById(iConsDefId);
		return dataConsolidationById;
	}

}