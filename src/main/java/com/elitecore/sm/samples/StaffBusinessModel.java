/**
 * 
 */
package com.elitecore.sm.samples;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.elitecore.sm.iam.model.Action;
import com.elitecore.sm.iam.model.BusinessModel;
import com.elitecore.sm.iam.model.BusinessModule;
import com.elitecore.sm.iam.model.BusinessSubModule;

/**
 * @author Sunil Gulabani
 * Jun 22, 2015
 */
public class StaffBusinessModel extends BaseBusinessModel{
	
	private List<Action> staffManagerActions = null;
	private List<Action> staffAccessManagerActions = null;
	private List<Action> staffAuditManagerActions = null;

	public BusinessModule getStaffBusinessModule(BusinessModel parentBusinessModel){
		BusinessModule businessModule = getBusinessModule(
												"Staff Manager", 
												"Staff Manager", 
												"STAFF_MANAGER_MENU_VIEW", 
												parentBusinessModel);
		
		Set<BusinessSubModule> subModuleSet = new HashSet<>();
		subModuleSet.add(getStaffManagementBusinessSubModule(businessModule));
		subModuleSet.add(getStaffAccessGroupManagementBusinessSubModule(businessModule));
		subModuleSet.add(setStaffAuditManagementBusinessSubModule(businessModule));
		
		
		businessModule.setSubModuleList(subModuleSet);
		return businessModule;
	}

	private BusinessSubModule getStaffManagementBusinessSubModule(BusinessModule parentBusinessModule){
		BusinessSubModule businessSubModule = getBusinessSubModule(
														"Staff Management", 
														"Staff Management", 
														"STAFF_MANAGEMENT", 
														parentBusinessModule);

		staffManagerActions = new ArrayList<>();
		
		staffManagerActions.add(getAction(businessSubModule, "View Staff", "View Staff", "VIEW_STAFF"));
		staffManagerActions.add(getAction(businessSubModule, "Add Staff", "Add Staff", "ADD_STAFF"));
		staffManagerActions.add(getAction(businessSubModule, "Edit Staff", "Edit Staff", "EDIT_STAFF"));
		staffManagerActions.add(getAction(businessSubModule, "Delete Staff", "Delete Staff", "DELETE_STAFF"));
		staffManagerActions.add(getAction(businessSubModule, "Lock Unlock Staff", "Lock Unlock Staff", "LOCK_UNLOCK_STAFF"));

		businessSubModule.setActionList(staffManagerActions);
		return businessSubModule;
	}
	
	private BusinessSubModule getStaffAccessGroupManagementBusinessSubModule(BusinessModule parentBusinessModule){
		BusinessSubModule businessSubModule = getBusinessSubModule(
													"Access Group Management", 
													"Access Group Management", 
													"ACCESS_GROUP_MANAGEMENT", 
													parentBusinessModule);
		
		staffAccessManagerActions = new ArrayList<>();
		staffAccessManagerActions.add(getAction(businessSubModule, "View Access Group", "View Access Group", "VIEW_ACCESS_GROUP"));
		staffAccessManagerActions.add(getAction(businessSubModule, "Add Access Group", "Add Access Group", "ADD_ACCESS_GROUP"));
		staffAccessManagerActions.add(getAction(businessSubModule, "Edit Access Group", "Edit Access Group", "EDIT_ACCESS_GROUP"));
		staffAccessManagerActions.add(getAction(businessSubModule, "Delete Access Group", "Delete Access Group", "DELETE_ACCESS_GROUP"));
		businessSubModule.setActionList(staffAccessManagerActions);
		return businessSubModule;
	}
	
	
	private BusinessSubModule setStaffAuditManagementBusinessSubModule(BusinessModule parentBusinessModule){
		BusinessSubModule businessSubModule = getBusinessSubModule(
													"Staff Audit Management", 
													"Staff Audit Management", 
													"STAFF_AUDIT_MANAGEMENT", 
													parentBusinessModule);
		
		staffAuditManagerActions = new ArrayList<>();
		staffAuditManagerActions.add(getAction(businessSubModule, "View Audit", "View Audit", "VIEW_AUDIT"));
		businessSubModule.setActionList(staffAuditManagerActions);
		
		return businessSubModule;
	}
	
	
	public List<Action> getStaffAccessManagerActions() {
		return staffAccessManagerActions;
	}

	public void setStaffAccessManagerActions(
			List<Action> staffAccessManagerActions) {
		this.staffAccessManagerActions = staffAccessManagerActions;
	}

	public List<Action> getStaffManagerActions() {
		return staffManagerActions;
	}

	public void setStaffManagerActions(List<Action> staffManagerActions) {
		this.staffManagerActions = staffManagerActions;
	}

	/**
	 * @return the staffAuditManagerActions
	 */
	public List<Action> getStaffAuditManagerActions() {
		return staffAuditManagerActions;
	}

	/**
	 * @param staffAuditManagerActions the staffAuditManagerActions to set
	 */
	public void setStaffAuditManagerActions(List<Action> staffAuditManagerActions) {
		this.staffAuditManagerActions = staffAuditManagerActions;
	}
}
