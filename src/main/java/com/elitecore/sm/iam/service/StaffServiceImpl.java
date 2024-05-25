/**
 * 
 */
package com.elitecore.sm.iam.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Blob;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.hibernate.criterion.Criterion;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.LockedException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


import com.elitecore.passwordutil.NoSuchEncryptionException;
import com.elitecore.passwordutil.PasswordEncryption;
import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.model.SystemBackUpPathOptionEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.iam.dao.AccessGroupDAO;
import com.elitecore.sm.iam.dao.StaffDAO;
import com.elitecore.sm.iam.exceptions.StaffNotFoundException;
import com.elitecore.sm.iam.model.AccessGroup;
import com.elitecore.sm.iam.model.PasswordHistory;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.systemparam.service.SystemParameterService;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.PasswordEncoderConfiguration;
import com.elitecore.sm.util.PasswordProcessor;


/**
 * @author Sunil Gulabani
 * Apr 6, 2015
 */
@org.springframework.stereotype.Service(value = "staffService")
public class StaffServiceImpl implements StaffService {
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	private static final String STFSVCBEAN ="staffService";

	@Autowired
	private StaffDAO staffDAO;
	
	@Autowired
	private AccessGroupDAO accessGroupDao;
	
	@Autowired
	@Qualifier(value="systemParameterService")
	private SystemParameterService systemParamService;
	
	/**
	 * Add Staff in database.
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.CREATE_STAFF,actionType = BaseConstants.CREATE_ACTION, currentEntity= Staff.class, ignorePropList= "")
	public ResponseObject addStaff(Staff staff){
		ResponseObject responseObject = new ResponseObject();
		/* check username is available or not with case in-sensitive */
		Staff dbStaff = staffDAO.getStaffDetails(staff.getUsername(),false);
		if(dbStaff!=null){
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_STAFF);
		}else{
			dbStaff = staffDAO.getStaffDetailsByEmailId(staff.getEmailId());
			if(dbStaff!=null){
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.DUPLICATE_STAFF);
			}else{
				PasswordHistory passwordHistory = new PasswordHistory();
				passwordHistory.setPassword(staff.getPassword());
				passwordHistory.setModifiedDate(new Date());
				passwordHistory.setStaff(staff);
				
				List<PasswordHistory> passwordHistoryList = new ArrayList<>();
				passwordHistoryList.add(passwordHistory);
				staff.setPasswordHistoryList(passwordHistoryList);
				
				this.staffDAO.save(staff);
				
				if(staff.getId() != 0){
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.STAFF_INSERT_SUCCESS);
				}else{
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.STAFF_INSERT_FAIL);
				}
			}
		}
		return responseObject;
	}

	/**
	 * Updates the Staff Details.
	 * @throws CloneNotSupportedException 
	 */
	@Override
	@Transactional
	public ResponseObject updateStaffDetails(Staff staff) throws CloneNotSupportedException{
		ResponseObject responseObject = new ResponseObject();
		int dbStaffId = staffDAO.getStaffIdByEmailId(staff.getEmailId());

		if(dbStaffId!=0 && dbStaffId != (staff.getId())){
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.DUPLICATE_STAFF_EMAIL_ID);
		}else{
			StaffService staffServiceImpl = (StaffService) SpringApplicationContext.getBean(STFSVCBEAN);
			Staff staffClone = (Staff) staff.clone();
			
			responseObject  = staffServiceImpl.updateStaffDetailsForLoginHistory(staffClone);
			
		}
		return responseObject;
	}
	
	
	
	@Override
	@Auditable(auditActivity = AuditConstants.UPDATE_STAFF_DETAILS,actionType = BaseConstants.UPDATE_ACTION , currentEntity= Staff.class, ignorePropList= "")
	public ResponseObject updateStaffDetailsForLoginHistory(Staff staff) {
		ResponseObject responseObject = new ResponseObject();
		this.staffDAO.merge(staff);
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.STAFF_UPDATE_SUCCESS);
		return responseObject;
	}
	
	
	
	/**
	 * Updates the Staff Details
	 */
	@Override
	@Transactional
	public void updateStaff(Staff staff){
		this.staffDAO.merge(staff);
	}
	
	/**
	 * Provides the Staff details based on username.
	 */
	@Override
	@Transactional(readOnly = true)
	public Staff getStaffDetails(String username){
		return staffDAO.getStaffDetails(username,true);
	}
	
	/**
	 * Provides the Staff details based on username and staff type.
	 */
	@Override
	@Transactional(readOnly = true)
	public boolean isLDAPStaff(String username){
		Staff staff = staffDAO.getStaffDetailsByType(username, BaseConstants.LDAP_STAFF);
		if(staff==null)
			return true;
		else if(BaseConstants.LDAP_STAFF.equals(staff.getStafftype()))
			return true;
		else
			return false;
	}
	
	/**
	 * Provides the Staff details based on staff id.
	 */
	@Override
	@Transactional(readOnly = true)
	public Staff getStaffDetailsById(int staffId){	
		return staffDAO.getStaffDetailsById(staffId);
	}
	
	/**
	 * Provides the Staff profile pic as blob based on staff id.
	 */
	@Override
	@Transactional(readOnly = true)
	public Blob getStaffProfilePicAsBlob(int staffId){
		return staffDAO.getStaffProfilePicAsBlob(staffId);
	}

	/**
	 * Provides the full staff details based on username
	 */
	@Override
	@Transactional(readOnly = true)
	public Staff getFullStaffDetails(String username){
		return staffDAO.getFullStaffDetails(username);
	}
	
	/**
	 * Provides the full staff details based on id
	 */
	@Override
	@Transactional(readOnly = true)
	public Staff getFullStaffDetailsById(int staffId){
		return staffDAO.getFullStaffDetailsById(staffId);
	}
	
	/**
	 * Provides the full staff details based on email id
	 */
	@Override
	@Transactional(readOnly = true)
	public Staff getFullStaffDetailsByEmailId(String emailId){
		return staffDAO.getFullStaffDetailsByEmailId(emailId);
	}
	
	/**
	 * Provides the staff id and username.
	 */
	@Override
	@Transactional(readOnly = true)
	public Map<String,String> getAllStaffIdAndUsername(){
		return staffDAO.getAllStaffIdAndUsername();
	}
	
	/**
	 * Verifies the user for Forgot Password
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseObject verifyUserForForgotPassword(String username, String emailId){
		Map<String,Object> staffDetail =  staffDAO.verifyStaffUserByCredential(username, emailId);
		ResponseObject responseObject = new ResponseObject();
		
		if(staffDetail != null && staffDetail.size()>0){
			logger.info("staff Detail avaliable");
			
			if(staffDetail.containsKey("QUESTION_LIST")){
				responseObject.setSuccess(true);
				responseObject.setResponseCode(ResponseCode.FORGOT_PASSWORD_USER_FOUND);
				responseObject.setObject(staffDetail);	
			} else if(staffDetail.containsKey(BaseConstants.STAFF_TYPE)){
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FORGOT_PASSWORD_LDAP_USER);
			} else {
				responseObject.setSuccess(false);
				responseObject.setResponseCode(ResponseCode.FORGOT_PASSWORD_FIRST_LOGIN);
			}
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FORGOT_PASSWORD_WRONG_USER);
		}
		logger.info("responseObject: " + responseObject);
		return responseObject;
	}
	
	/**
	 * Verifies the detail for Forgot Password
	 */
	@Override
	@Transactional(readOnly = true)
	public ResponseObject verifyDetailsForForgotPassword(String username, String emailId,String question,String answer){
		Map<String,JSONObject> staffDetail =  staffDAO.getStaffUserByCredential(username, emailId);
		ResponseObject responseObject = new ResponseObject();
		
		if(staffDetail != null && staffDetail.size()>0){
			logger.info("staff Detail avaliable");
			for(String key : staffDetail.keySet()){
				if(staffDetail.get(key).has(question)){
					if(staffDetail.get(key).get(question).equals(answer)){
						responseObject.setSuccess(true);
						responseObject.setResponseCode(ResponseCode.FORGOT_PASSWORD_USER_FOUND);
						responseObject.setObject(staffDetail);
					}else{
						responseObject.setSuccess(false);
						responseObject.setResponseCode(ResponseCode.FORGOT_PASSWORD_WRONG_SECURITY_QUESTION);
						responseObject.setObject(staffDetail);
					}
				}
				else{
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.FORGOT_PASSWORD_WRONG_SECURITY_QUESTION);
				}
			}
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FORGOT_PASSWORD_WRONG_USER);
		}
		logger.info("responseObject: " + responseObject);
		return responseObject;
	}
	
	/**
	 * Updates Fail Attempts when password is wrong entered.
	 * @throws CloneNotSupportedException 
	 */
	@Override
	@Transactional(noRollbackFor=LockedException.class)
	public ResponseObject updateFailAttempts(String username) throws CloneNotSupportedException,LockedException {
		ResponseObject responseObject = new ResponseObject();
		Staff user ;
		user = getStaffDetails(username);
		logger.info("getUserDetails: " + user);
		if (user != null) {
			
			logger.info("update attempts count +1");
			
			StaffService staffServiceImpl = (StaffService) SpringApplicationContext.getBean(STFSVCBEAN);
			Staff staffClone = (Staff) user.clone();
			staffClone.setWrongAttempts(user.getWrongAttempts() + 1);
			staffClone.setLastWrongAttemptsDate(new Date());
			responseObject = staffServiceImpl.updateStaffDetailsForLoginHistory(staffClone);
			if (user.getWrongAttempts() + 1 > MapCache.getConfigValueAsInteger(SystemParametersConstant.MAX_WRONG_PASSWORD_ATTEMPTS, 3)) {
				if(user.getUsername().equals(BaseConstants.ADMIN_USERNAME)
					 && !MapCache.getConfigValueAsBoolean(SystemParametersConstant.LOCK_ADMIN_ON_WRONG_ATTEMPTS, false)){
					// do nothing
					logger.info(user.getUsername() + " - by passing from locking admin...");
					responseObject.setSuccess(true);
				}else{
					// locked user
					staffClone = (Staff) user.clone();
					staffClone.setAccountLocked(true);
					logger.info("locking user: " + user);
					responseObject = staffServiceImpl.updateStaffDetailsForLoginHistory(staffClone);
					
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.USER_ACCOUNT_LOCKED);
				}
			}
		}
		return responseObject;
	}
	
	/**
	 * Reset's the Password in database
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_STAFF_PASSWORD,actionType = BaseConstants.SM_ACTION , currentEntity= Staff.class, ignorePropList= "")
	public ResponseObject resetPassword(String username,String newPassword, int lastUpdatedByStaffId){
		ResponseObject responseObject = new ResponseObject();
		
		Staff staff = staffDAO.getStaffDetails(username,true);
		if(staff!=null){
			int passwordLastNCheck = MapCache.getConfigValueAsInteger(SystemParametersConstant.NEW_PASSWORD_LAST_N_CHECK, 3) -1;
			logger.info("passwordLastNCheck: " + passwordLastNCheck);
			String newPasswordEncrypted = PasswordProcessor.encryptPassword(newPassword, BaseConstants.DEFAULT_ENCRYPTION_TYPE);
			List<PasswordHistory> passwordHistoryList = new ArrayList<>();
			if(passwordLastNCheck > 0){
				passwordHistoryList = staff.getPasswordHistoryList();
				
				if(passwordHistoryList!=null && !passwordHistoryList.isEmpty()){
					TreeMap<Date, String> passwordHistorySorted = new TreeMap<>();
					for(PasswordHistory passwordHistory : passwordHistoryList){
						passwordHistorySorted.put(passwordHistory.getModifiedDate(),passwordHistory.getPassword());
					}
	
					NavigableMap<Date, String> passwordHistorySortedDesc = passwordHistorySorted.descendingMap();
					logger.info("passwordHistorySortedDesc: " + passwordHistorySortedDesc);
					int index = 0;
					for(Date key : passwordHistorySortedDesc.keySet()){
						if(passwordHistorySortedDesc.get(key).equals(newPasswordEncrypted)){
							responseObject.setSuccess(false);
							responseObject.setResponseCode(ResponseCode.STAFF_OLD_PASSWORD_REUSED);
							
							return responseObject;
						}
						logger.info("index : " + index + ",passwordLastNCheck: " + passwordLastNCheck);
						if(index == passwordLastNCheck)
							break;
	
						index++;
					}
				}
			}
			staff.setPassword(newPasswordEncrypted);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DATE, MapCache.getConfigValueAsInteger(SystemParametersConstant.PASSWORD_EXPIRY_DAYS, 1));
			staff.setPasswordExpiryDate(calendar.getTime());

			PasswordHistory passwordHistory = new PasswordHistory();
			passwordHistory.setPassword(newPasswordEncrypted);
			passwordHistory.setModifiedDate(new Date());
			passwordHistory.setStaff(staff);
			
			if(passwordHistoryList!=null){
				passwordHistoryList.add(passwordHistory);		
			}
			
			staff.setPasswordHistoryList(passwordHistoryList);
			staff.setLastUpdatedDate(new Date());
			
			// if account is locked by invalid login attempt not by admin than release his lock so he can login
			if(staff.getWrongAttempts()>0){
				staff.setAccountLocked(false);
			}
			
			staff.setWrongAttempts(0);
			staff.setLastWrongAttemptsDate(null);
			
			if(lastUpdatedByStaffId!=0)
				staff.setLastUpdatedByStaffId(lastUpdatedByStaffId);
			else
				staff.setLastUpdatedByStaffId(staff.getId());

			
			this.updateStaff(staff);
			responseObject.setObject(staff);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.STAFF_PASSWORD_RESET_SUCCESS);
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.STAFF_NOT_FOUND);
		}
		return responseObject;
	}
	
	/**
	 * Changes the Password in database
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_STAFF_PASSWORD ,actionType = BaseConstants.SM_ACTION, currentEntity= Staff.class, ignorePropList= "")
	public ResponseObject changePassword(String username,String oldPassword, String newPassword, String question1, String answer1, String question2, String answer2){
		ResponseObject responseObject = new ResponseObject();
		Staff staff = staffDAO.getStaffDetails(username,true);
		if(staff!=null && 
				new PasswordEncoderConfiguration().matches(oldPassword, staff.getPassword())
			)		
		{
			int passwordLastNCheck = MapCache.getConfigValueAsInteger(SystemParametersConstant.NEW_PASSWORD_LAST_N_CHECK, 3)-1;
			
			String newPasswordEncrypted = PasswordProcessor.encryptPassword(newPassword, BaseConstants.DEFAULT_ENCRYPTION_TYPE);
			List<PasswordHistory> passwordHistoryList = new ArrayList<>();
			if(passwordLastNCheck > 0){
				passwordHistoryList = staff.getPasswordHistoryList();
				
				if(passwordHistoryList!=null && !passwordHistoryList.isEmpty()){
					TreeMap<Date, String> passwordHistorySorted = new TreeMap<>();
					for(PasswordHistory passwordHistory : passwordHistoryList){
						passwordHistorySorted.put(passwordHistory.getModifiedDate(),passwordHistory.getPassword());
					}
	
					NavigableMap<Date, String> passwordHistorySortedDesc = passwordHistorySorted.descendingMap();
					
					int index = 0;
					for(Date key : passwordHistorySortedDesc.keySet()){
						if(passwordHistorySortedDesc.get(key).equals(newPasswordEncrypted)){
							responseObject.setSuccess(false);
							responseObject.setResponseCode(ResponseCode.STAFF_OLD_PASSWORD_REUSED);
							return responseObject;
						}
	
						if(index == passwordLastNCheck)
							break;
	
						index++;
					}
				}
			}

			staff.setPassword(newPasswordEncrypted);

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.add(Calendar.DATE, MapCache.getConfigValueAsInteger(SystemParametersConstant.PASSWORD_EXPIRY_DAYS, 1));
			staff.setPasswordExpiryDate(calendar.getTime());

			PasswordHistory passwordHistory = new PasswordHistory();
			passwordHistory.setPassword(newPasswordEncrypted);
			passwordHistory.setModifiedDate(new Date());
			passwordHistory.setStaff(staff);

			if(passwordHistoryList!=null){
				passwordHistoryList.add(passwordHistory);		
			}
			
			staff.setPasswordHistoryList(passwordHistoryList);
			
			if(
					!StringUtils.isEmpty(question1) && !StringUtils.isEmpty(answer1) && 
					!StringUtils.isEmpty(question2) && !StringUtils.isEmpty(answer2)
					){
				staff.setSecurityQuestion1(question1);
				staff.setSecurityAnswer1(answer1);
				staff.setSecurityQuestion2(question2);
				staff.setSecurityAnswer2(answer2);
				staff.setFirstTimeLogin(false);
			}

			this.updateStaff(staff);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.STAFF_PASSWORD_CHANGE_SUCCESS);
			responseObject.setObject(staff);
			
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.STAFF_OLD_PASSWORD_INVALID);
			
		}
		return responseObject;
	}

	/**
	 * Reset Fail Attempts for user-name provided.
	 * @throws CloneNotSupportedException 
	 */
	@Override
	@Transactional
	public ResponseObject resetFailAttempts(Staff staff) throws CloneNotSupportedException {
		ResponseObject responseObject = new ResponseObject();
		
		if(staff != null){
			logger.info("resetting fail attempts for username: " + staff.getUsername());
			Staff staffClone = (Staff) staff.clone();
			staffClone.setWrongAttempts(0);
			staffClone.setAccountLocked(false);
			staffClone.setLastWrongAttemptsDate(null);
			responseObject  = updateStaffDetailsForLoginHistory(staffClone);
		}else{
			responseObject.setSuccess(false);
			logger.debug("Failed to reset staff details as staff ref is null "); 
			 
		}
		return responseObject;
	}
	
	/**
	 * Releases the All Staff Lock State
	 */
	@Override
	@Transactional
	public int releaseStaffLockAgent(){
		return this.staffDAO.releaseStaffLock();
	}
	
	/**
	 * Release the Staff Lock for specified Staff.
	 */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_STAFF_DETAILS,actionType = BaseConstants.UPDATE_ACTION, currentEntity= Staff.class, ignorePropList= "")
	public ResponseObject releaseStaffLock(Staff staff){
		
		logger.debug("Going to release staff lock.");
		ResponseObject responseObject = new ResponseObject();
		if(staff != null){
			staff.setWrongAttempts(0);
			staff.setAccountLocked(false);
			staff.setLastWrongAttemptsDate(null);
			this.staffDAO.merge(staff);
			responseObject.setSuccess(true);
			responseObject.setObject(staff);
			logger.info("Staff lock release successfully.");
		}else{
			logger.info("Failed to realese staff lock");
			responseObject.setSuccess(false);
			responseObject.setObject(null);
			
		}
		return responseObject;
	}

	/**
	 * Provide the total staff count based on input.
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public long getTotalStaffCount(int searchCreatedByStaffId, String searchUsername, String searchFirstName, String searchLastName,
			String searchEmployeeId, Date startDate, Date endDate,
			String searchAccountState, String searchEmailId,String searchAccessGroupId,String searchLockStatus, 
			String excludeStaffUsername) {
		Map<String,Object> conditionsAndAliases = staffDAO.createCriteriaConditions(
																searchCreatedByStaffId,
																searchUsername,
																searchFirstName, searchLastName,
																searchEmployeeId,
																startDate, 
																endDate,
																searchAccountState,
																searchEmailId,
																searchAccessGroupId,
																searchLockStatus, excludeStaffUsername
															);
		return staffDAO.getQueryCount(
				Staff.class,
				(List<Criterion>)conditionsAndAliases.get("conditions"),
				(HashMap<String,String>)conditionsAndAliases.get("aliases")
			);
	}

	/**
	 * Provides the List of Staff based on input.
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional(readOnly = true)
	public List<Staff> getPaginatedList(int searchCreatedByStaffId, String searchUsername,
			String searchFirstName, String searchLastName, String searchEmployeeId, Date startDate,
			Date endDate, String searchAccountState,
			String searchEmailId,String searchAccessGroupId,String searchLockStatus,
			int startIndex, int limit,
			String sidx, String sord, String excludeStaffUsername) {
		Map<String,Object> conditionsAndAliases = staffDAO.createCriteriaConditions(
																searchCreatedByStaffId,
																searchUsername,
																searchFirstName, searchLastName,
																searchEmployeeId,
																startDate, 
																endDate,
																searchAccountState, 
																searchEmailId,
																searchAccessGroupId,
																searchLockStatus, excludeStaffUsername);
		// Need to change as Paginated Code is being done by Native SQL.
		List<Staff> staffList = staffDAO.getPaginatedList(
											Staff.class,
													(List<Criterion>)conditionsAndAliases.get("conditions"),
													(HashMap<String,String>)conditionsAndAliases.get("aliases"),
											startIndex, 
											limit, 
											sidx, 
											sord);
		
		if(staffList!=null){
			for(Staff staff: staffList){
				if(staff.getAccessGroupList()!=null &&  !staff.getAccessGroupList().isEmpty()){
					for(AccessGroup accessGroup : staff.getAccessGroupList()){
						accessGroup.getName(); // Just not to get the Lazy Loading Exception
					}
				}
			}
		}
		
		return staffList;
	}

	/**
	 * Deletes the Staff.
	 */
	@Override
	@Transactional(rollbackFor=StaffNotFoundException.class)
	public ResponseObject deleteStaff(String[] staffIds,int userId) throws StaffNotFoundException{
		ResponseObject responseObject = new ResponseObject();

		for(String staffId : staffIds){
			int istaffId = 0;
			if(!StringUtils.isEmpty(staffId)){
				istaffId=Integer.parseInt(staffId);	
			}
			
			StaffService staffService = (StaffService) SpringApplicationContext.getBean(STFSVCBEAN); // getting spring bean context for aop to call method from another.
			responseObject = staffService.deleteStaff(istaffId, userId);
			
		}
		return responseObject;
	}
	
	@Override
	@Auditable(auditActivity = AuditConstants.DELETE_STAFF,actionType = BaseConstants.DELETE_ACTION, currentEntity= Staff.class, ignorePropList= "")
	public ResponseObject deleteStaff(int staffId, int userId) throws StaffNotFoundException{
		ResponseObject responseObject = new ResponseObject();
		Staff staff = staffDAO.findByPrimaryKey(Staff.class, staffId);
		
		if(staff == null){
			responseObject.setResponseCode(ResponseCode.STAFF_DELETE_FAIL);
			responseObject.setSuccess(false);

			throw new StaffNotFoundException(staffId,BaseConstants.DELETE_STAFF_STATE);
		}else{
			staff.setUsername(staff.getUsername() + BaseConstants.DELETED_MODEL_SUFFIX+ "_" + new Date().getTime());
			staff.setEmailId(staff.getEmailId() + BaseConstants.DELETED_MODEL_SUFFIX+ "_" + new Date().getTime());
			staff.setAccountState(StateEnum.DELETED);
			staff.setLastUpdatedDate(new Date());
			staff.setLastUpdatedByStaffId(userId);
			staffDAO.merge(staff);
			
			responseObject.setResponseCode(ResponseCode.STAFF_DELETE_SUCCESS);
			responseObject.setSuccess(true);
		}
		return responseObject;
	}
	
	
	/**
	 * Changes the Lock-Unlock state of Staff.
	 */
	@Override
	@Transactional(rollbackFor=StaffNotFoundException.class)
	@Auditable(auditActivity = AuditConstants.UPDATE_STAFF_DETAILS,actionType = BaseConstants.UPDATE_CUSTOM_ACTION, currentEntity= Staff.class, ignorePropList= "")
	public ResponseObject lockUnlockStaff(int staffId,int userId, boolean accountLocked) throws StaffNotFoundException{
		ResponseObject responseObject = new ResponseObject();
		
		logger.debug("Going to change staff lock ");
		Staff staff = staffDAO.findByPrimaryKey(Staff.class,staffId);
		if(staff != null ){
			staff.setAccountLocked(accountLocked);
			if(!accountLocked)
				{staff.setWrongAttempts(0);}
			staff.setLastUpdatedDate(new Date());
			staff.setLastUpdatedByStaffId(userId);
			staffDAO.merge(staff);
			if(accountLocked){
				logger.info("Staff has been locked successfully.");
				responseObject.setResponseCode(ResponseCode.STAFF_LOCK_UPDATE_SUCCESS);	
			}else{
				logger.info("Staff has been unlocked successfully.");
				responseObject.setResponseCode(ResponseCode.STAFF_UNLOCK_UPDATE_SUCCESS);	
			}
			responseObject.setSuccess(true);
		}else{
			responseObject.setSuccess(false);
			if(accountLocked){
				logger.info("Failed to lock staff.");
				responseObject.setResponseCode(ResponseCode.STAFF_LOCK_UPDATE_FAIL);	
			}else{
				logger.info("Failed to unlock staff.");
				responseObject.setResponseCode(ResponseCode.STAFF_UNLOCK_UPDATE_FAIL);	
			}
		}
		
		return responseObject;
	}
	
	/**
	 * Changes the Staff State.
	 */
	@Override
	@Transactional(rollbackFor=StaffNotFoundException.class)
	@Auditable(auditActivity = AuditConstants.UPDATE_STAFF_STATUS,actionType = BaseConstants.UPDATE_CUSTOM_ACTION, currentEntity= Staff.class, ignorePropList= "")
	public ResponseObject changeStaffState(int staffId, StateEnum state, int loggedInStaffId) throws StaffNotFoundException{
		
		logger.debug("Going to update staff status.");
		ResponseObject responseObject = new ResponseObject();
		
		Staff staff = staffDAO.findByPrimaryKey(Staff.class,staffId);
		if(staff != null){
			staff.setAccountState(state);
			staff.setLastUpdatedDate(new Date());
			staff.setLastUpdatedByStaffId(loggedInStaffId);
			staffDAO.merge(staff);
			responseObject.setResponseCode(ResponseCode.STAFF_STATUS_UPDATE_SUCCESS);
			responseObject.setSuccess(true);
			logger.info("Staff status has been udpated successfully.");
		}else{
			logger.info("Failed to update staff status.");
			responseObject.setResponseCode(ResponseCode.STAFF_STATUS_UPDATE_FAIL);
			responseObject.setSuccess(false);
		}
		
		return responseObject;
	}
	
	/**
	 * Update Staff access group
	 * @param accessGroupList
	 * @param staffId
	 * @return
	 */
	@Override
	@Transactional
	public ResponseObject updateStaffAccessGroup(List<AccessGroup> accessGroupList,int staffId){
		ResponseObject responseObject = new ResponseObject();
		Staff staff = staffDAO.findByPrimaryKey(Staff.class, staffId);

		if(staff!=null){
			staff.setAccessGroupList(accessGroupList);
			this.staffDAO.merge(staff);
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.STAFF_ACCESSGROPUP_ASSIGN_SUCCESS);
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.STAFF_UPDATE_FAIL);
		}
		return responseObject;
	}
	
	@SuppressWarnings("deprecation")
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_STAFF_DETAILS,actionType = BaseConstants.UPDATE_ACTION , currentEntity= Staff.class, ignorePropList= "")
	public ResponseObject updateStaffProfilePic(int staffId,String profilePicName,int loggedInStaffId,HttpServletRequest request) throws SMException{
		ResponseObject responseObject = new ResponseObject();
		Staff staff = staffDAO.findByPrimaryKey(Staff.class, staffId);

		if(staff!=null){
			staff.setProfilePic(profilePicName);
			this.staffDAO.merge(staff);
			if(staffId == loggedInStaffId ){
				MapCache.addConfigObject(BaseConstants.STAFF_LOGO,getStaffProfilePicPath(staffId));	
				request.getSession().setAttribute(BaseConstants.STAFF_LOGO,getStaffProfilePicPath(staffId));
				
			}
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.STAFF_PROFILE_PIC_CHANGE_SUCCESS);
		}else{
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.STAFF_UPDATE_FAIL);
		}
		return responseObject;
	}
	
	@SuppressWarnings("deprecation")
	@Transactional(readOnly=true)
	@Override
	public String getStaffProfilePicPath(int staffId) throws SMException{
		
		Staff staff = staffDAO.findByPrimaryKey(Staff.class, staffId);
		String staffLogoPath = null;
		if(staff !=null){
			staffLogoPath=staff.getProfilePic();
			if(staffLogoPath.startsWith(String.valueOf(staffId))){
				logger.debug("Found Custom Staff Profile Pic , so fetch from file system ");
				ResponseObject responseObject = getStaffProfilePicFile(staffId);	
				
				if(responseObject.isSuccess()){
					File sampleFile=(File)responseObject.getObject();
					
					staffLogoPath=BaseConstants.IMAGE_BYTE_CONSTANT+setStaffProfilePicFile(sampleFile);
					}else{
						logger.debug("Staff logo not found at specified location ");
					}
			}else{
				logger.debug("Found Default staff  profile pic. "+staffLogoPath);
			}
			MapCache.addConfigObject(BaseConstants.STAFF_PROFILE_PIC, staffLogoPath);
		}else{
			logger.debug("Staff data is null ");
		}
		
		return staffLogoPath;
	}
	
	
	
	/**
	 * Fetch customer logo file from backup location
	 */
	@Override
	public ResponseObject getStaffProfilePicFile(int staffId){
	
		ResponseObject responseObject=systemParamService.getLocationOfSystemBackUpPath(SystemBackUpPathOptionEnum.STAFFIMAGE);
		File sampleFile;
		if(responseObject.isSuccess()){
			
			String backupLocation=responseObject.getObject().toString();
			logger.debug("System Backup Location :: "+backupLocation);
			
			File directory = new File(backupLocation);
			
			String[] fileList=directory.list(new FilenameFilter() {
				
				@Override
				public boolean accept(File directory, String name) {
					return name.startsWith(staffId+"_");
				}
			});
			
			if (fileList != null && fileList.length>0) {
				
				 String filename = fileList[0];
		         logger.debug("Sample data file found : "+filename);
		         sampleFile=new File(backupLocation+File.separator+filename);
		         responseObject.setSuccess(true);
		         responseObject.setObject(sampleFile);
		      }  else {
		    	  logger.debug("Either dir does not exist or is not a directory");
		    	  responseObject.setSuccess(false);
			      responseObject.setResponseCode(ResponseCode.REGEX_PARSER_NO_SAMPLE_FILE_FOUND);
		      } 
		}
		else{
			logger.debug("System Backup path is not valid");
		}
		
		return responseObject;
	}
	
	@Override
	public String setStaffProfilePicFile(File staffLogoFile) throws SMException{
		
		try(FileInputStream inputStream = new FileInputStream(staffLogoFile) ; ByteArrayOutputStream bos=new ByteArrayOutputStream() ) {

			int b;
			byte[] buffer = new byte[(int) staffLogoFile.length()];
	
			while((b=inputStream.read(buffer))!=-1){
			   bos.write(buffer,0,b);
			}
			bos.flush();
			return new String(Base64.encodeBase64(bos.toByteArray()));
			
		} catch (IOException e) {
			throw new SMException(e); 
		}
	}
	
	public AccessGroupDAO getAccessGroupDao() {
		return accessGroupDao;
	}

	public void setAccessGroupDao(AccessGroupDAO accessGroupDao) {
		this.accessGroupDao = accessGroupDao;
	}

	public StaffDAO getStaffDAO() {
		return staffDAO;
	}

	public void setStaffDAO(StaffDAO userDAO) {
		this.staffDAO = userDAO;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseObject createLdapStaff(Staff staff,String groupRoleAttributeName) {
		logger.debug("Inside createLdapStaff method");
		ResponseObject responseObject =  new ResponseObject();
		if(staff!=null) {
			logger.debug("setting LDAP default attributes");
			staff = setLDAPStaffDefaultAttributes(staff);	
			logger.debug("Going to add/update Staff");
			responseObject = this.addStaff(staff);
			if(responseObject.isSuccess()) {
				logger.debug("Staff is successfully added");
				updateLdapUserAccessGroup(staff, groupRoleAttributeName);
				logger.debug("LDAP access group is assigned to the staff");
			} else { 				
				Staff dbStaff = staffDAO.getStaffDetailsByType(staff.getUsername(),BaseConstants.LDAP_STAFF);
				if(dbStaff==null) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.DUPLICATE_STAFF);
				}else {
					logger.debug("Staff is already created need to update it with new accessgroup : "+groupRoleAttributeName);
					staff = dbStaff;
					updateLdapUserAccessGroup(dbStaff, groupRoleAttributeName);					
					logger.debug("Access group Updated for login staff");
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.STAFF_UPDATE_SUCCESS);
				}
			}
		}
		return responseObject;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public AccessGroup getAccessGroupForLDAPStaff(String groupRoleAttributeName) {	
		logger.debug("Inside getAccessGroupForLDAPStaff method");
		return accessGroupDao.getAccessGroupForLDAPStaff(groupRoleAttributeName);				
	}
	
	
	
	private Staff setLDAPStaffDefaultAttributes(Staff staff) {				
		if(staff!=null) {
			String DEFAULT = "DEFAULT";
			Date date = new Date();
			staff.setId(0);					
			String[] args = getFirstAndLastName(staff.getUsername());
			staff.setFirstName((args!=null && args.length>0)?args[0]:DEFAULT);
			staff.setLastName((args!=null && args.length==2)?args[1]:DEFAULT);
			staff.setPassword(DEFAULT);
			staff.setCreatedByStaffId(1);
			staff.setCreatedDate(date);
			staff.setLastUpdatedByStaffId(1);
			staff.setLastUpdatedDate(date);
			staff.setAddress(DEFAULT);
			staff.setCity(DEFAULT);
			staff.setEmailId(staff.getUsername()+"@"+(String)MapCache.getConfigValueAsString(SystemParametersConstant.DOMAIN_NAME,"DEFAULT"));			
			staff.setPincode("000000");
			staff.setStaffCode("0000");
			staff.setMobileNo("0000000000");
			staff.setFirstTimeLogin(false);
			staff.setProfilePic("img/staff_default_profile_pic.png");
			staff.setStafftype(BaseConstants.LDAP_STAFF);
			staff.setCountry(DEFAULT);
			staff.setCity(DEFAULT);
			staff.setPasswordExpiryDate(Timestamp.valueOf("2999-07-12 10:17:13.0000"));		
		}
		return staff;
	}
	
	private void updateLdapUserAccessGroup(Staff staff, String groupRoleAttributeName) {	
		logger.debug("Inside updateLdapUserAccessGroup method");
		AccessGroup accessGroup = accessGroupDao.getAccessGroupForLDAPStaff(groupRoleAttributeName);
		List<AccessGroup> accessGrpList = new ArrayList<>();
		if(accessGroup != null) {
			logger.debug("LDAP Access group found in Database");
			if(accessGroup.getAccessGroupState().equals(StateEnum.ACTIVE))
				accessGrpList.add(accessGroup);				
		} else {
			String defaultAccessGroup = (String)MapCache.getConfigValueAsString(SystemParametersConstant.DEFAULT_ACCESS_GROUP,"");
			logger.debug("LDAP Access group not found in Database, so assigned default access group : "+defaultAccessGroup);
			if(!("").equalsIgnoreCase(defaultAccessGroup)) {
				accessGroup = accessGroupDao.getAccessGroup((String)MapCache.getConfigValueAsString(SystemParametersConstant.DEFAULT_ACCESS_GROUP,""));				
				if(accessGroup!=null && accessGroup.getAccessGroupState().equals(StateEnum.ACTIVE)) {
					logger.debug("Default access group found in database");
					accessGrpList.add(accessGroup);
				}					
			}
		}
		staff.setAccessGroupList(accessGrpList);
		updateStaffAccessGroup(accessGrpList,staff.getId());
		logger.debug("Ldap Access group has been assigned to login staff");
	}
	
	private String[] getFirstAndLastName(String userName) {
		return userName.split("\\.");				
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseObject getStaffByUsernameOrEmail(String username) {
		ResponseObject responseObject = new ResponseObject();
		Staff staff = staffDAO.getStaffByUsernameOrEmail(username);
		if(staff != null && staff.getStafftype().equals(BaseConstants.LDAP_STAFF)) {
			responseObject.setSuccess(false);
			responseObject.setResponseCode(ResponseCode.FORGOT_PASSWORD_LDAP_USER);
		}else {
			responseObject.setObject(staff);
			responseObject.setSuccess(true);
		}
		return responseObject;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseObject getAllStaffDetails() {
		ResponseObject responseObject = new ResponseObject();
		List<Staff> staffList = staffDAO.getAllStaffDetails();
		if(staffList != null && !staffList.isEmpty()) {
			responseObject.setObject(staffList);
			responseObject.setSuccess(true);
		}
		return responseObject;
	}
	
	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public ResponseObject createSSOStaff(Staff staff,List<String> groupRoleAttributeName) throws SMException {
		logger.debug("Inside createKeycloakStaff method");
		ResponseObject responseObject =  new ResponseObject();
		if(staff!=null && !staff.getName().equals("admin")) {
			logger.debug("setting Keycloak default attributes");
			staff = setSSOStaffDefaultAttributes(staff);	
			logger.debug("Going to add/update Staff");
			responseObject = this.addStaff(staff);
			if(responseObject.isSuccess()) {
				logger.debug("Staff is successfully added");
				
				updateSSOUserAccessGroup(staff, groupRoleAttributeName);
				logger.debug("Keycloak access group is assigned to the staff");
				responseObject.setResponseCode(ResponseCode.STAFF_INSERT_SUCCESS);	
			} else { 				
				Staff dbStaff = staffDAO.getStaffDetailsBySSOType(staff.getUsername(),BaseConstants.SSO_STAFF);
				if(dbStaff==null) {
					responseObject.setSuccess(false);
					responseObject.setResponseCode(ResponseCode.DUPLICATE_STAFF);
				}else {
					logger.debug("Staff is already created need to update it with new accessgroup : "+groupRoleAttributeName);
					staff = dbStaff;
					updateSSOUserAccessGroup(dbStaff, groupRoleAttributeName);					
					logger.debug("Access group Updated for login staff");
					responseObject.setSuccess(true);
					responseObject.setResponseCode(ResponseCode.STAFF_UPDATE_SUCCESS);
				}
			}
		}
		return responseObject;
	}
	private void updateSSOUserAccessGroup(Staff staff, List<String> groupRoleAttributeName) {	
		logger.debug("Inside updateLdapUserAccessGroup method");
		List<AccessGroup> accessGrpList = new ArrayList<>();
		for(String roleName:groupRoleAttributeName){
			AccessGroup accessGroup = accessGroupDao.getAccessGroupForSSOStaff(roleName);
			
			if(accessGroup != null) {
				logger.debug("Keycloak Access group found in Database");
				if(accessGroup.getAccessGroupState().equals(StateEnum.ACTIVE))
					accessGrpList.add(accessGroup);				
			}
		}
		if(accessGrpList.isEmpty())
		{
			String defaultAccessGroup = (String)MapCache.getConfigValueAsString(SystemParametersConstant.DEFAULT_SSO_ACCESS_GROUP,"");
			logger.debug("Keycloak Access group not found in Database, so assigned default access group : "+defaultAccessGroup);
			if(!("").equalsIgnoreCase(defaultAccessGroup)) {
				AccessGroup accessGroup = accessGroupDao.getAccessGroup((String)MapCache.getConfigValueAsString(SystemParametersConstant.DEFAULT_SSO_ACCESS_GROUP,""));				
				if(accessGroup!=null && accessGroup.getAccessGroupState().equals(StateEnum.ACTIVE)) {
					logger.debug("Default access group found in database");
					accessGrpList.add(accessGroup);
				}	
			}						
				
		}
		staff.setAccessGroupList(accessGrpList);
		updateStaffAccessGroup(accessGrpList,staff.getId());
		logger.debug("Keycloak Access group has been assigned to login staff");
	}
	
	private Staff setSSOStaffDefaultAttributes(Staff staff) {				
		if(staff!=null) {
			String DEFAULT = "DEFAULT";
			Date date = new Date();
			staff.setId(0);					
			String[] args = getFirstAndLastName(staff.getUsername());
			staff.setFirstName((args!=null && args.length>0)?args[0]:DEFAULT);
			staff.setLastName((args!=null && args.length==2)?args[1]:DEFAULT);
			if(staff.getPassword()!=null){
				staff.setPassword(staff.getPassword());
			}else{
				try {
					staff.setPassword(PasswordEncryption.crypt("DEFAULT",2));
				} catch (NoSuchEncryptionException e) {
					logger.error(e);
				}
				
			}
			staff.setCreatedByStaffId(1);
			staff.setCreatedDate(date);
			staff.setLastUpdatedByStaffId(1);
			staff.setLastUpdatedDate(date);
			staff.setAddress(DEFAULT);
			staff.setCity(DEFAULT);
			if(staff.getEmailId()!=null){
				staff.setEmailId(staff.getEmailId());
			}else{
				staff.setEmailId(staff.getUsername()+"@"+(String)MapCache.getConfigValueAsString(SystemParametersConstant.DOMAIN_NAME,"DEFAULT"));			
			}
			staff.setPincode("000000");
			staff.setStaffCode("0000");
			staff.setMobileNo("0000000000");
			staff.setFirstTimeLogin(false);
			staff.setProfilePic("img/staff_default_profile_pic.png");
			staff.setStafftype(BaseConstants.SSO_STAFF);
			staff.setCountry(DEFAULT);
			staff.setCity(DEFAULT);
			staff.setPasswordExpiryDate(Timestamp.valueOf("2999-07-12 10:17:13.0000"));		
		}
		return staff;
	}

	@Override
	@Transactional(propagation=Propagation.REQUIRED)
	public AccessGroup getAccessGroupForSSOStaff(String groupRoleAttributeName) {	
		logger.debug("Inside getAccessGroupForLDAPStaff method");
		return accessGroupDao.getAccessGroupForLDAPStaff(groupRoleAttributeName);				
	}


}