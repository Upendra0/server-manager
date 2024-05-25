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
public class ChangePasswordBusinessModel extends BaseBusinessModel{
	
	private List<Action> changePasswordActions = null;
	
	public BusinessModule getChangePasswordBusinessModule(BusinessModel parentBusinessModel){
		BusinessModule businessModule = getBusinessModule(
												"Change Password", 
												"Change Password", 
												"CHANGE_PASSWORD_MENU_VIEW", 
												parentBusinessModel);
		
		Set<BusinessSubModule> subModuleSet = new HashSet<>();
		subModuleSet.add(getChangePasswordBusinessSubModule(businessModule));
		businessModule.setSubModuleList(subModuleSet);
		return businessModule;
	}
	
	private BusinessSubModule getChangePasswordBusinessSubModule(BusinessModule parentBusinessModule){
		BusinessSubModule businessSubModule = getBusinessSubModule(
													"Change Password Management", 
													"Change Password Management", 
													"CHANGE_PASSWORD_MANAGEMENT", 
													parentBusinessModule);
		
		changePasswordActions = new ArrayList<>();
		changePasswordActions.add(getAction(businessSubModule, "Change Password", "Change Password", "CHANGE_PASSWORD"));
		businessSubModule.setActionList(changePasswordActions);
		return businessSubModule;
	}
	
	public List<Action> getChangePasswordActions() {
		return changePasswordActions;
	}

	public void setChangePasswordActions(List<Action> changePasswordActions) {
		this.changePasswordActions = changePasswordActions;
	}
}