/**
 * 
 */
package com.elitecore.sm.iam.service;

import java.io.File;
import java.sql.Blob;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.iam.exceptions.StaffNotFoundException;
import com.elitecore.sm.iam.model.AccessGroup;
import com.elitecore.sm.iam.model.Staff;

/**
 * @author Sunil Gulabani Apr 6, 2015
 */
public interface StaffService {
	public Staff getStaffDetails(String username);

	public Staff getStaffDetailsById(int staffId);

	public Staff getFullStaffDetails(String username);

	public Staff getFullStaffDetailsById(int staffId);

	public Staff getFullStaffDetailsByEmailId(String emailId);

	public Map<String, String> getAllStaffIdAndUsername();

	public ResponseObject updateFailAttempts(String username) throws CloneNotSupportedException;

	public ResponseObject resetFailAttempts(Staff staff) throws CloneNotSupportedException;

	public ResponseObject addStaff(Staff staff);

	public void updateStaff(Staff staff);

	public ResponseObject updateStaffDetails(Staff staff) throws CloneNotSupportedException;

	public ResponseObject updateStaffDetailsForLoginHistory(Staff staff);

	public ResponseObject changePassword(String username, String oldPassword, String newPassword, String question1, String answer1, String question2,
			String answer2);

	public ResponseObject resetPassword(String username, String newPassword, int lastUpdatedByStaffId);

	public int releaseStaffLockAgent();

	public ResponseObject releaseStaffLock(Staff staff);

	public long getTotalStaffCount(int searchCreatedByStaffId, String searchUsername, String searchFirstName, String searchLastName,
			String searchEmployeeId, Date startDate, Date endDate, String searchAccountState, String searchEmailId, String searchAccessGroupId,
			String searchLockStatus, String excludeStaffUsernames);

	public List<Staff> getPaginatedList(int searchCreatedByStaffId, String searchUsername, String searchFirstName, String searchLastName,String searchEmployeeId, Date startDate, Date endDate, String searchAccountState, String searchEmailId, String searchAccessGroupId,String searchLockStatus, int startIndex, int limit, String sidx, String sord, String excludeStaffUsernames); //NOSONAR 

	public ResponseObject verifyUserForForgotPassword(String username, String emailId);

	public ResponseObject verifyDetailsForForgotPassword(String username, String emailId, String question, String answer);

	public ResponseObject deleteStaff(String staffIds[], int userId) throws StaffNotFoundException;

	public ResponseObject deleteStaff(int staffId, int userId) throws StaffNotFoundException;

	public ResponseObject lockUnlockStaff(int staffId, int userId, boolean accountLocked) throws StaffNotFoundException;

	public ResponseObject changeStaffState(int staffId, StateEnum state, int loggedInStaffId) throws StaffNotFoundException;

	public Blob getStaffProfilePicAsBlob(int staffId);

	public ResponseObject updateStaffAccessGroup(List<AccessGroup> accessGroupList, int staffId);

	public ResponseObject getStaffProfilePicFile(int staffId);

	public String setStaffProfilePicFile(File staffLogoFile) throws SMException;

	public String getStaffProfilePicPath(int staffId) throws SMException;

	public ResponseObject updateStaffProfilePic(int staffId, String profilePicName, int loggedInStaffId, HttpServletRequest request)
			throws SMException;
	
	public ResponseObject createLdapStaff(Staff staff, String groupRoleAttributeName) throws SMException;
	
	public boolean isLDAPStaff(String username);
	
	public ResponseObject getStaffByUsernameOrEmail(String username);
	
	public AccessGroup getAccessGroupForLDAPStaff(String groupRoleAttributeName);
	
	public ResponseObject getAllStaffDetails();
	
	public ResponseObject createSSOStaff(Staff staff, List<String> groupRoleAttributeNames) throws SMException;
	
	public AccessGroup getAccessGroupForSSOStaff(String groupRoleAttributeName);
}
