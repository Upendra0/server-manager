/**
 * 
 */
package com.elitecore.sm.samples;

import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.iam.model.Action;
import com.elitecore.sm.iam.model.BusinessModel;
import com.elitecore.sm.iam.model.BusinessModule;
import com.elitecore.sm.iam.model.BusinessSubModule;

/**
 * @author Sunil Gulabani
 * Jun 22, 2015
 */
public abstract class BaseBusinessModel {
	
	protected BusinessModule getBusinessModule(String name, String description, String alias, BusinessModel parentBusinessModel) {
		BusinessModule businessModule = new BusinessModule();
		businessModule.setName(name);
		businessModule.setDescription(description);
		businessModule.setAlias(alias);
		businessModule.setParentBusinessModel(parentBusinessModel);
		businessModule.setStatus(StateEnum.ACTIVE);
		return businessModule;
	}
	
	protected BusinessSubModule getBusinessSubModule(String name, String description, String alias, BusinessModule parentBusinessModule) {
		BusinessSubModule businessSubModule = new BusinessSubModule();
		businessSubModule.setName(name);
		businessSubModule.setDescription(description);
		businessSubModule.setAlias(alias);
		businessSubModule.setStatus(StateEnum.ACTIVE);
		businessSubModule.setParentBusinessModule(parentBusinessModule);
		return businessSubModule;
	}
	
	protected Action getAction(BusinessSubModule parentBusinessSubModule, String name, String description, String alias){
		Action action = new Action();
		action.setName(name);
		action.setDescription(description);
		action.setAlias(alias);
		action.setParentBusinessSubModule(parentBusinessSubModule);
		action.setStatus(StateEnum.ACTIVE);
		return action;
	}
}
