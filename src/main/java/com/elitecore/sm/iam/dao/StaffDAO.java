/**
 * 
 */
package com.elitecore.sm.iam.dao;

import java.sql.Blob;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.hibernate.criterion.Criterion;
import org.json.JSONObject;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.iam.model.Staff;

/**
 * @author Sunil Gulabani
 * Apr 6, 2015
 */
public interface StaffDAO extends GenericDAO<Staff> {
	
	public Staff getStaffDetails(String username, boolean iscasesensitive);
	public Staff getStaffDetailsById(int staffId);
	public Staff getStaffDetailsByEmailId(String emailId);
	
	public Staff getFullStaffDetails(String username);
	public Staff getFullStaffDetailsById(int staffId);
	public Staff getFullStaffDetailsByEmailId(String emailId) ;
	
	public int getStaffIdByEmailId(String emailId);
	
	public Map<String,String> getAllStaffIdAndUsername();
	
	public int releaseStaffLock();

	public Map<String,Object> createCriteriaConditions(int searchCreatedByStaffId, String searchUsername,
			String searchFirstName, String searchLastName, String searchEmployeeId, Date startDate,
			Date endDate, String searchAccountState, String searchEmailId,String searchAccessGroupId,String searchLockStatus, String excludeStaffUsername);
	
	
	public Blob getStaffProfilePicAsBlob(int staffId);
	
	public Map<String, JSONObject> getStaffUserByCredential(String username, String emailId);
	public Map<String,Object> verifyStaffUserByCredential(String username,String emailId);
	
	public List<Staff> getPaginatedList(Class<Staff> klass,List<Criterion> conditions, Map<String,String> aliases, int start, int limit,String sidx, String sord);
	
	public Staff getStaffDetailsByType(String username, String type);
	
	public Staff getStaffByUsernameOrEmail(String username);
	
	public List<Staff> getAllStaffDetails();
	
	public Staff getStaffDetailsBySSOType(String username, String type);

}
