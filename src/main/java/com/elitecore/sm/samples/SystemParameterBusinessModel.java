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
public class SystemParameterBusinessModel extends BaseBusinessModel{
	
	private List<Action> systemParamterActions=null;
	
	public BusinessModule getSystemParameterBusinessModule(BusinessModel parentBusinessModel){
		BusinessModule businessModule = getBusinessModule("System Parameter", "System Parameter", "SYSTEM_PARAMETER_MENU_VIEW", parentBusinessModel);

		Set<BusinessSubModule> subModuleSet = new HashSet<>();
		subModuleSet.add(getSystemParameterBusinessSubModule(businessModule));
		businessModule.setSubModuleList(subModuleSet);
		return businessModule;
	}
	
	private BusinessSubModule getSystemParameterBusinessSubModule(BusinessModule parentBusinessModule){
		BusinessSubModule businessSubModule = getBusinessSubModule(
												"System Parameter Management", 
												"System Parameter Management", 
												"SYSTEM_PARAMETER_MANAGEMENT", 
												parentBusinessModule);
		
		systemParamterActions = new ArrayList<>();
		systemParamterActions.add(getAction(businessSubModule, "View System Parameter", "View System Parameter", "VIEW_SYSTEM_PARAMETER"));
		systemParamterActions.add(getAction(businessSubModule, "Modify System Parameter", "Modify System Parameter", "MODIFY_SYSTEM_PARAMETER"));
		businessSubModule.setActionList(systemParamterActions);
		return businessSubModule;
	}
	
	/**
	 * @return the systemParamterActions
	 */
	public List<Action> getSystemParamterActions() {
		return systemParamterActions;
	}

	/**
	 * @param systemParamterActions the systemParamterActions to set
	 */
	public void setSystemParamterActions(List<Action> systemParamterActions) {
		this.systemParamterActions = systemParamterActions;
	}

}
