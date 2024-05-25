/**
 * 
 */
package com.elitecore.sm.samples;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.iam.model.AccessGroup;
import com.elitecore.sm.iam.model.Action;
import com.elitecore.sm.iam.model.Staff;
import com.elitecore.sm.iam.service.AccessGroupService;
import com.elitecore.sm.iam.service.StaffService;
import com.elitecore.sm.util.PasswordProcessor;

/**
 * @author Sunil Gulabani
 * Apr 17, 2015
 */
public class StaffSamples {
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public void createAdminStaffAndAssignAccessGroup(
			StaffService staffService,
			AccessGroupService accessGroupService,
			MediationBusinessModel mediationBusinessModel) {
		
/*		Date passwordExpiryDate = new Date();
		calendar.setTime(passwordExpiryDate);
		calendar.add(Calendar.DATE, 60); // add 60 days
		passwordExpiryDate = calendar.getTime();*/
		
		String password = PasswordProcessor.encryptPassword(BaseConstants.ADMIN_USERNAME ,BaseConstants.DEFAULT_ENCRYPTION_TYPE);
		Staff staff = createUser(
								staffService,
								BaseConstants.ADMIN_USERNAME , 	//username, 
								password, 	//password, 
								"2183", 	//staffCode, 
								"John", 	//firstName,
								"a", // middleName
								"Doe", 	//lastName,
								"sunil.gulabani@sterlite.com ", //emailId, 
								"9898989898", //mobileNo,
								"Thaltej", //address,
								"Address 2", //address2, 
								"Ahmedabad", //city, 
								"380054", //pincode, 
								"Gujarat", //state, 
								"India", //country, 
								StateEnum.ACTIVE, //accountState, 
								false, //accountLocked, 
								null, //accessGroupList,
								0, //wrongAttempts, 
								null, //lastWrongAttemptsDate, 
								null, //passwordExpiryDate, 
								null, //lastLoginTime, 
								new Date(), //createdDate, 
								new Date(), //lastUpdatedDate, 
								null, //createdByStaff, 
								null, //lastUpdatedByStaff,
								new Date() // birth date,
							);
		
		List<AccessGroup> accessGroupList = addAccessGroup(accessGroupService, staff, mediationBusinessModel);
		staff.setAccessGroupList(accessGroupList);
		
		staffService.updateStaff(staff);
	}
	
	private List<AccessGroup> addAccessGroup(AccessGroupService accessGroupService,  Staff staff,MediationBusinessModel mediationBusinessModel){
		List<AccessGroup> accessGroupList = new ArrayList<>();
		
		AccessGroup staffAccessGroup = createStaffAccessGroup(accessGroupService,staff, mediationBusinessModel);
		AccessGroup changePasswordAccessGroup = createChangePasswordAccessGroup(accessGroupService,staff, mediationBusinessModel);
		AccessGroup systemParameterAccessGroup= createSystemParameterAccessGroup(accessGroupService, staff, mediationBusinessModel);
		
		AccessGroup serverManagerAccessGroup= createServerManagerAccessGroup(accessGroupService, staff, mediationBusinessModel);
		AccessGroup serviceManagerAccessGroup= createServiceManagerAccessGroup(accessGroupService, staff, mediationBusinessModel);
		
		accessGroupList.add(staffAccessGroup);
		accessGroupList.add(changePasswordAccessGroup);
		accessGroupList.add(systemParameterAccessGroup);
		accessGroupList.add(serverManagerAccessGroup);
		accessGroupList.add(serviceManagerAccessGroup);

		return accessGroupList; 
	}
	
	private AccessGroup createStaffAccessGroup(AccessGroupService accessGroupService, Staff staff,MediationBusinessModel mediationBusinessModel){
		AccessGroup staffAccessGroup = new AccessGroup();
		staffAccessGroup.setName("Staff Access Group");
		staffAccessGroup.setDescription("Staff Access Group");
		staffAccessGroup.setAccessGroupState(StateEnum.ACTIVE);
		
		List<Action> staffActionList = new ArrayList<>(); 
		staffActionList.addAll(mediationBusinessModel.getStaffBusinessModel().getStaffAccessManagerActions());
		staffActionList.addAll(mediationBusinessModel.getStaffBusinessModel().getStaffManagerActions());
		staffActionList.addAll(mediationBusinessModel.getStaffBusinessModel().getStaffAuditManagerActions());
		logger.info("staffActionList: " + staffActionList);
		staffAccessGroup.setActions(staffActionList);
		
		staffAccessGroup.setCreateDate(new Date());
		staffAccessGroup.setCreatedByStaffId(staff.getId());
		
		staffAccessGroup.setLastUpdateDate(new Date());
		staffAccessGroup.setLastUpdateByStaffId(staff.getId());

		accessGroupService.save(staffAccessGroup);
		
		return staffAccessGroup;
	}

	private AccessGroup createChangePasswordAccessGroup(AccessGroupService accessGroupService, Staff staff,MediationBusinessModel mediationBusinessModel){
		AccessGroup accessGroup = new AccessGroup();
		accessGroup.setName("Change Password Access Group");
		accessGroup.setDescription("Change Password Access Group");
		accessGroup.setAccessGroupState(StateEnum.ACTIVE);

		List<Action> changePasswordActionList = new ArrayList<>(); 
		changePasswordActionList.addAll(mediationBusinessModel.getChangePasswordBusinessModel().getChangePasswordActions());
		logger.info("changePasswordActionList: " + changePasswordActionList);
		accessGroup.setActions(changePasswordActionList);

		accessGroup.setCreateDate(new Date());
		accessGroup.setCreatedByStaffId(staff.getId());
		
		accessGroup.setLastUpdateDate(new Date());
		accessGroup.setLastUpdateByStaffId(staff.getId());

		accessGroupService.save(accessGroup);
		
		return accessGroup;
	}

	private AccessGroup createSystemParameterAccessGroup(AccessGroupService accessGroupService, Staff staff,MediationBusinessModel mediationBusinessModel){
		AccessGroup accessGroup = new AccessGroup();
		accessGroup.setName("System Parameter Access Group");
		accessGroup.setDescription("System Parameter Access Group");
		accessGroup.setAccessGroupState(StateEnum.ACTIVE);

		List<Action> systemParameterActionList = new ArrayList<>(); 
		systemParameterActionList.addAll(mediationBusinessModel.getSystemParamBusinessModel().getSystemParamterActions());
		logger.info("systemParameterActionList: " + systemParameterActionList);
		accessGroup.setActions(systemParameterActionList);

		accessGroup.setCreateDate(new Date());
		accessGroup.setCreatedByStaffId(staff.getId());
		
		accessGroup.setLastUpdateDate(new Date());
		accessGroup.setLastUpdateByStaffId(staff.getId());

		accessGroupService.save(accessGroup);
		
		return accessGroup;
	}
	
	private AccessGroup createServerManagerAccessGroup(AccessGroupService accessGroupService, Staff staff,MediationBusinessModel mediationBusinessModel){
		AccessGroup accessGroup = new AccessGroup();
		accessGroup.setName("Server Manager Access Group");
		accessGroup.setDescription("Server Manager Access Group");
		accessGroup.setAccessGroupState(StateEnum.ACTIVE);

		List<Action> serverManagerActionList = new ArrayList<>(); 
		serverManagerActionList.addAll(mediationBusinessModel.getServerManagerBusinessModel().getServerMgmtActions());
		serverManagerActionList.addAll(mediationBusinessModel.getServerManagerBusinessModel().getCreateServerActions());
		logger.info("serverManagerActionList: " + serverManagerActionList);
		accessGroup.setActions(serverManagerActionList);

		accessGroup.setCreateDate(new Date());
		accessGroup.setCreatedByStaffId(staff.getId());
		
		accessGroup.setLastUpdateDate(new Date());
		accessGroup.setLastUpdateByStaffId(staff.getId());

		accessGroupService.save(accessGroup);
		
		return accessGroup;
	}
	
	private AccessGroup createServiceManagerAccessGroup(AccessGroupService accessGroupService, Staff staff,MediationBusinessModel mediationBusinessModel){
		AccessGroup accessGroup = new AccessGroup();
		accessGroup.setName("Service Manager Access Group");
		accessGroup.setDescription("Service Manager Access Group");
		accessGroup.setAccessGroupState(StateEnum.ACTIVE);

		List<Action> serviceManagerActionList = new ArrayList<>(); 
		serviceManagerActionList.addAll(mediationBusinessModel.getServiceManagerBusinessModel().getserviceMgmtActions());
		serviceManagerActionList.addAll(mediationBusinessModel.getServiceManagerBusinessModel().getcreateServiceActions());
		logger.info("serviceManagerActionList: " + serviceManagerActionList);
		accessGroup.setActions(serviceManagerActionList);

		accessGroup.setCreateDate(new Date());
		accessGroup.setCreatedByStaffId(staff.getId());
		
		accessGroup.setLastUpdateDate(new Date());
		accessGroup.setLastUpdateByStaffId(staff.getId());

		accessGroupService.save(accessGroup);
		
		return accessGroup;
	}
	
	private Staff createUser(
			StaffService staffService, 
			String username, String password,
			String staffCode, 
			String firstName, String middleName, String lastName,
			String emailId, 
			String mobileNo, 
			String address,  String address2,
			String city, String pincode,
			String state, String country,
			StateEnum accountState, boolean accountLocked,
			List<AccessGroup> accessGroupList,
			int wrongAttempts, Date lastWrongAttemptsDate,
			Date passwordExpiryDate, Date lastLoginTime,
			Date createdDate, Date lastUpdatedDate,
			Staff createdByStaff, Staff lastUpdatedByStaff,
			Date birthDate
		){
		Staff staff = new Staff();
		staff.setUsername(username);
		staff.setPassword(password);
		staff.setStaffCode(staffCode);
		staff.setFirstName(firstName);
		staff.setMiddleName(middleName);
		staff.setLastName(lastName);
		staff.setEmailId(emailId);
		staff.setMobileNo(mobileNo);
		staff.setAddress(address);
		staff.setAddress2(address2);
		staff.setCity(city);
		staff.setPincode(pincode);
		staff.setState(state);
		staff.setCountry(country);
		staff.setAccountState(accountState);
		staff.setAccountLocked(accountLocked);
		staff.setAccessGroupList(accessGroupList);
		staff.setWrongAttempts(wrongAttempts);
		staff.setLastWrongAttemptsDate(lastWrongAttemptsDate);
		staff.setPasswordExpiryDate(passwordExpiryDate);
		staff.setLastLoginTime(lastLoginTime);
		staff.setCreatedDate(createdDate);
		staff.setLastUpdatedDate(lastUpdatedDate);
		staff.setBirthDate(birthDate);
		if(createdByStaff!=null){
			staff.setCreatedByStaffId(createdByStaff.getId());
		}
		if(lastUpdatedByStaff!=null){
			staff.setLastUpdatedByStaffId(lastUpdatedByStaff.getId());
		}

		staffService.addStaff(staff);
		
		return staff;
	}
}
