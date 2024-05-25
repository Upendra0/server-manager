package com.elitecore.sm.profileconfig.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.iam.model.AccessGroup;
import com.elitecore.sm.iam.model.Action;
import com.elitecore.sm.iam.model.BusinessModule;
import com.elitecore.sm.iam.model.BusinessSubModule;

/**
 * 
 * @author avani.panchal
 *
 */
public interface ProfileConfigurationService{
	
	public ResponseObject updateProfileConfiguration(String selectedActionList,String selectedSubModuleList,String selectedModulesList,int staffId);
	
	public ResponseObject updateActionConfiguration2(Action action);
	
	public ResponseObject updateBusinessSubModuleConfig2(BusinessSubModule businessSubModule);
	
	public ResponseObject updateBusinessModelConfig2(BusinessModule businessModule);
	
	public ResponseObject updateAccessGroupActions2(AccessGroup accessGroup);

}
