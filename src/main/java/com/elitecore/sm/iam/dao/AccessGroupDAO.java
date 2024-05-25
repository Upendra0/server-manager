/**
 * 
 */
package com.elitecore.sm.iam.dao;

import java.util.List;
import java.util.Map;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.iam.model.AccessGroup;

/**
 * @author Sunil Gulabani
 * Apr 7, 2015
 */
public interface AccessGroupDAO extends GenericDAO<AccessGroup> {
	public Map<String,Object> createCriteriaConditionsForPagination(int searchCreatedByStaffId, String searchAccessGroupName, String searchStatus, String searchActiveInactiveStatus);
	
	public List<Integer> getAccessGroupStaffRelUniqueIds(int accessGroupId);
	
	public AccessGroup getAccessGroup(String accessGroupName);
	
	public int getAccessGroupIdByName(String accessGroupName);
	
	public String getDynamicNativeQueryForAccessGroupSearchCount(int searchCreatedByStaffId);
	
	public String getDynamicNativeQueryForAccessGroupSearchResult(int searchCreatedByStaffId);
	
	public List<AccessGroup> getAllAccessGroup();
	
	public List<String> getAllAccessGroupName();

	public AccessGroup getAccessGroupForLDAPStaff(String accessGroupName);
	
	public AccessGroup getAccessGroupForSSOStaff(String accessGroupName);
	
	public List<String> getAllAccessGroupByStaffType(String staffType);
}