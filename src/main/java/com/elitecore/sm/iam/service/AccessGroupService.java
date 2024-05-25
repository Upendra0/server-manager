/**
 * 
 */
package com.elitecore.sm.iam.service;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.iam.exceptions.AccessGroupNotFoundException;
import com.elitecore.sm.iam.exceptions.AccessGroupUniqueContraintException;
import com.elitecore.sm.iam.model.AccessGroup;

/**
 * @author Sunil Gulabani
 * Apr 7, 2015
 */
public interface AccessGroupService {
	
	public AccessGroup getAccessGroup(int id);
	
	public ResponseObject save(AccessGroup accessGroup) throws DataAccessException ;
	
	public ResponseObject update(AccessGroup accessGroup) throws DataAccessException, AccessGroupUniqueContraintException ;

	public ResponseObject deleteAccessGroups(String accessGroupIds[],int userId) throws AccessGroupNotFoundException;

	public List<Integer> getAccessGroupStaffRelUniqueIds(int accessGroupId);
	
	public List<Map<String,Object>> getPaginatedList(int searchCreatedByStaffId, String searchAccessGroupName, String searchStatus, String searchActiveInactiveStatus, int start, int limit,String sidx, String sord);
	
	public Long getPaginatedListTotalCount(int searchCreatedByStaffId, String searchAccessGroupName, String searchStatus, String searchActiveInactiveStatus);

	public ResponseObject changeAccessGroupState(int accessGroupId, StateEnum state, int staffId) throws AccessGroupNotFoundException;
	
	public ResponseObject deleteGroup(int accessGroupId, int userId) throws AccessGroupNotFoundException;
	
	public AccessGroup getAccessGroup(String accessGroupName);
	
	public List<String> getAllAccessGroupName();
	
	public List<String> getAllAccessGroupByStaffType(String staffType);
	
}