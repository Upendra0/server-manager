/**
 * 
 */
package com.elitecore.sm.iam.service;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.iam.dao.AccessGroupDAO;
import com.elitecore.sm.iam.dao.StaffDAO;
import com.elitecore.sm.iam.exceptions.AccessGroupNotFoundException;
import com.elitecore.sm.iam.exceptions.AccessGroupUniqueContraintException;
import com.elitecore.sm.iam.model.AccessGroup;
import com.elitecore.sm.iam.model.Action;
import com.elitecore.sm.systemparam.dao.SystemParamDataDao;
import com.elitecore.sm.systemparam.model.SystemParameterData;
import com.elitecore.sm.util.EliteUtils;

/**
 * @author Sunil Gulabani Apr 7, 2015
 */
@Service(value = "accessGroupService")
public class AccessGroupServiceImpl implements AccessGroupService {

	@Autowired
	private AccessGroupDAO accessGroupDAO;

	@Autowired
	@Qualifier(value = "staffDAO")
	private StaffDAO userDAO;
		
	@Autowired
	private SystemParamDataDao systemParamDataDao;

	public AccessGroupServiceImpl() {
		// default no-arg constructor
	}

	public AccessGroupServiceImpl(AccessGroupDAO accessGroupDAO) {
		this.accessGroupDAO = accessGroupDAO;
	}

	/**
	 * Add access group in database.
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.CREATE_ACCESS_GROUP, actionType = BaseConstants.CREATE_ACTION, currentEntity = AccessGroup.class, ignorePropList= "")
	public ResponseObject save(AccessGroup accessGroup) {
		ResponseObject responseObject = new ResponseObject();

		int accessGroupId = accessGroupDAO.getAccessGroupIdByName(accessGroup.getName());
		if (accessGroupId != 0 && accessGroupId != accessGroup.getId()) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_ACCESS_GROUP);
		} else {
			accessGroupDAO.save(accessGroup);

			if (accessGroup.getId() != 0) {
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.ACCESS_GROUP_INSERT_SUCCESS);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.ACCESS_GROUP_INSERT_FAIL);
			}
		}
		return responseObject;
	}

	/**
	 * Updates Access Group in database.
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_ACCESS_GROUP, actionType = BaseConstants.UPDATE_LIST_ACTION, currentEntity = AccessGroup.class, ignorePropList= "")
	public ResponseObject update(AccessGroup accessGroup) throws AccessGroupUniqueContraintException {
		ResponseObject responseObject = new ResponseObject();
		int accessGroupId = accessGroupDAO.getAccessGroupIdByName(accessGroup.getName());

		if (accessGroupId != 0 && accessGroupId != accessGroup.getId()) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_ACCESS_GROUP);
		} else {

			accessGroupDAO.merge(accessGroup);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.ACCESS_GROUP_INSERT_SUCCESS);
		}
		return responseObject;
	}

	/**
	 * Provides the total count of access group based on input.
	 */
	@Override
	@Transactional(readOnly = true)
	public Long getPaginatedListTotalCount(int searchCreatedByStaffId, String searchAccessGroupName, String searchStatus, String searchActiveInactiveStatus) {
		return accessGroupDAO.getTotalCountUsingSQL(accessGroupDAO.getDynamicNativeQueryForAccessGroupSearchCount(searchCreatedByStaffId), accessGroupDAO.createCriteriaConditionsForPagination(searchCreatedByStaffId, searchAccessGroupName, searchStatus, searchActiveInactiveStatus));
	}

	/**
	 * Provides the List of Access Group based on input.
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Map<String, Object>> getPaginatedList(int searchCreatedByStaffId, String searchAccessGroupName, String searchStatus, String searchActiveInactiveStatus, int start, int limit, String sidx, String sord) {
		return accessGroupDAO.getListUsingSQL(accessGroupDAO.getDynamicNativeQueryForAccessGroupSearchResult(searchCreatedByStaffId)
				+ " order by " + sidx + " " + sord, accessGroupDAO.createCriteriaConditionsForPagination(searchCreatedByStaffId, searchAccessGroupName, searchStatus, searchActiveInactiveStatus), start, limit);
	}

	/**
	 * Logical Deletes the Access Group
	 */
	@Override
	@Transactional(rollbackFor = AccessGroupNotFoundException.class)
	public ResponseObject deleteAccessGroups(String[] accessGroupIds, int userId) throws AccessGroupNotFoundException {
		ResponseObject responseObject = new ResponseObject();
		SystemParameterData systemParameterData =  systemParamDataDao.getSystemParameterByAlias(SystemParametersConstant.DEFAULT_ACCESS_GROUP);
		AccessGroup accessGroup = accessGroupDAO.getAccessGroup(systemParameterData.getValue());
		for (String accessGroupId : accessGroupIds) {
			int iaccessGroupId = 0;
			if (!StringUtils.isEmpty(accessGroupId)) {
				iaccessGroupId = Integer.parseInt(accessGroupId);
			}
			if(accessGroup!=null && accessGroup.getId() == iaccessGroupId) {
				systemParameterData.setValue("");
				systemParamDataDao.update(systemParameterData);
			}
			AccessGroupService accessGroupSI = (AccessGroupService) SpringApplicationContext.getBean("accessGroupService"); // getting spring bean context for aop to call method from another.
			accessGroupSI.deleteGroup(iaccessGroupId, userId);
		}

		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.ACCESS_GROUP_DELETE_SUCCESS);

		return responseObject;
	}

	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_ACCESS_GROUP, actionType = BaseConstants.DELETE_ACTION, currentEntity = AccessGroup.class, ignorePropList= "")
	public ResponseObject deleteGroup(int accessGroupId,  int userId) throws AccessGroupNotFoundException {
		ResponseObject responseObject = new ResponseObject();
		AccessGroup accessGroup = accessGroupDAO.findByPrimaryKey(AccessGroup.class, accessGroupId);
		if (accessGroup == null)
			throw new AccessGroupNotFoundException(accessGroupId);
		// Modified below setName for [MED-4702] [SMRevamp] Staff Mgmt : Access Group is not deleted
		accessGroup.setName(EliteUtils.checkForNames(BaseConstants.DELETE_MODE, accessGroup.getName()));
		accessGroup.setAccessGroupState(StateEnum.DELETED);
		accessGroup.setLastUpdateDate(new Date());
		accessGroup.setLastUpdateByStaffId(userId);

		responseObject.setSuccess(true);
		accessGroupDAO.merge(accessGroup);
		return responseObject;
	}

	/**
	 * Changes the access group state in database.
	 */
	@Override
	@Transactional(rollbackFor = AccessGroupNotFoundException.class)
	@Auditable(auditActivity = AuditConstants.UPDATE_ACCESS_GROUP, actionType = BaseConstants.UPDATE_CUSTOM_ACTION, currentEntity = AccessGroup.class, ignorePropList= "")
	public ResponseObject changeAccessGroupState(int accessGroupId, StateEnum state, int staffId) throws AccessGroupNotFoundException {
		AccessGroup accessGroup;
		ResponseObject responseObject = new ResponseObject();

		accessGroup = accessGroupDAO.findByPrimaryKey(AccessGroup.class, accessGroupId);
		if (accessGroup == null) {
			responseObject.setSuccess(false);
			throw new AccessGroupNotFoundException(accessGroupId);
		}

		accessGroup.setAccessGroupState(state);
		accessGroup.setLastUpdateDate(new Date());
		accessGroup.setLastUpdateByStaffId(staffId);
		accessGroupDAO.merge(accessGroup);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.ACCESS_GROUP_UPDATE_SUCCESS);
		return responseObject;
	}

	/**
	 * Provides the Access Group based on id.
	 */
	@Override
	@Transactional(readOnly = true)
	public AccessGroup getAccessGroup(int id) {
		AccessGroup accessGroup = accessGroupDAO.findByPrimaryKey(AccessGroup.class, id);
		if (accessGroup != null && accessGroup.getActions() != null) {
			for (Action action : accessGroup.getActions()) {
				action.getId();
				action.getName();
				action.getAlias();
			}
		}
		return accessGroup;
	}

	/**
	 * Provides the access group staff rel associated with access group id,
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Integer> getAccessGroupStaffRelUniqueIds(int accessGroupId) {
		return accessGroupDAO.getAccessGroupStaffRelUniqueIds(accessGroupId);
	}

	public AccessGroupDAO getAccessGroupDAO() {
		return accessGroupDAO;
	}

	public void setAccessGroupDAO(AccessGroupDAO accessGroupDAO) {
		this.accessGroupDAO = accessGroupDAO;
	}
	
	@Override
	@Transactional(readOnly = true)
	public AccessGroup getAccessGroup(String accessGroupName) {		
		return accessGroupDAO.getAccessGroup(accessGroupName);		
	}
	
	@Override
	@Transactional(readOnly = true)
	public List<String> getAllAccessGroupName() {
		return accessGroupDAO.getAllAccessGroupName();
	}

	@Override
	@Transactional(readOnly = true)
	public List<String> getAllAccessGroupByStaffType(String staffType) {
		return accessGroupDAO.getAllAccessGroupByStaffType(staffType);
	}

}