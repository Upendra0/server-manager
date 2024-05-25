package com.elitecore.sm.samples;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.elitecore.sm.common.constants.BusinessModelConstants;
import com.elitecore.sm.iam.model.Action;
import com.elitecore.sm.iam.model.BusinessModel;
import com.elitecore.sm.iam.model.BusinessModule;
import com.elitecore.sm.iam.model.BusinessSubModule;

/**
 * 
 * @author avani.panchal
 * 08 Oct , 2015
 */
public class ServiceManagerBusinessModel extends BaseBusinessModel{
	
	private List<Action> serviceMgmtActions = null;
	private List<Action> createServiceActions = null;

	public BusinessModule getServiceManagerBusinessModule(BusinessModel parentBusinessModel){
		BusinessModule businessModule = getBusinessModule(BusinessModelConstants.SERVICE_MANAGER_MENU_NAME, 
																								   BusinessModelConstants.SERVICE_MANAGER_MENU_NAME, 
																								   BusinessModelConstants.SERVICE_MANAGER_MENU_ALIAS, 
																								   parentBusinessModel);

		Set<BusinessSubModule> subModuleSet = new HashSet<>();
		subModuleSet.add(createServiceBusinessSubModule(businessModule));
		subModuleSet.add(getServiceMgmtBusinessSubModule(businessModule));
		businessModule.setSubModuleList(subModuleSet);
		return businessModule;
	}
	
	private BusinessSubModule createServiceBusinessSubModule(BusinessModule parentBusinessModule){
		BusinessSubModule businessSubModule = getBusinessSubModule(
														BusinessModelConstants.CREATE_SERVICE_TAB_NAME,
														BusinessModelConstants.CREATE_SERVICE_TAB_NAME,
														BusinessModelConstants.CREATE_SERVICE_TAB_ALIAS,
														parentBusinessModule);
		
		createServiceActions = new ArrayList<>();
		createServiceActions.add(getAction(businessSubModule, BusinessModelConstants.CREATE_SERVICE_ACCESS_RIGHTS_NAME, 
																									 BusinessModelConstants.CREATE_SERVICE_ACCESS_RIGHTS_NAME, 
																									 BusinessModelConstants.CREATE_SERVICE_ACCESS_RIGHTS_ALIAS));
		createServiceActions.add(getAction(businessSubModule, BusinessModelConstants.DELETE_SERVICE_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.DELETE_SERVICE_ACCESS_RIGHTS_NAME, 
																									 BusinessModelConstants.DELETE_SERVICE_ACCESS_RIGHTS_ALIAS));
		createServiceActions.add(getAction(businessSubModule, BusinessModelConstants.UPDATE_SERVICE_ACCESS_RIGHTS_NAME, 
				 																					 BusinessModelConstants.UPDATE_SERVICE_ACCESS_RIGHTS_NAME, 
				 																					 BusinessModelConstants.UPDATE_SERVICE_ACCESS_RIGHTS_ALIAS));
		createServiceActions.add(getAction(businessSubModule, BusinessModelConstants.VIEW_SERVICE_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.VIEW_SERVICE_ACCESS_RIGHTS_NAME, 
																									 BusinessModelConstants.VIEW_SERVICE_ACCESS_RIGHTS_ALIAS));
		createServiceActions.add(getAction(businessSubModule, BusinessModelConstants.CREATE_COLLECTION_DRIVER_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.CREATE_COLLECTION_DRIVER_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.CREATE_COLLECTION_DRIVER_ACCESS_RIGHTS_ALIAS));
		createServiceActions.add(getAction(businessSubModule, BusinessModelConstants.CONFIGURE_COLLECTION_DRIVER_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.CONFIGURE_COLLECTION_DRIVER_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.CONFIGURE_COLLECTION_DRIVER_ACCESS_RIGHTS_ALIAS));
		createServiceActions.add(getAction(businessSubModule, BusinessModelConstants.DELETE_COLLECTION_DRIVER_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.DELETE_COLLECTION_DRIVER_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.DELETE_COLLECTION_DRIVER_ACCESS_RIGHTS_ALIAS));
		createServiceActions.add(getAction(businessSubModule, BusinessModelConstants.UPDATE_COLLECTION_DRIVER_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.UPDATE_COLLECTION_DRIVER_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.UPDATE_COLLECTION_DRIVER_ACCESS_RIGHTS_ALIAS));
		createServiceActions.add(getAction(businessSubModule, BusinessModelConstants.VIEW_COLLECTION_DRIVER_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.VIEW_COLLECTION_DRIVER_ACCESS_RIGHTS_NAME, 
																									 BusinessModelConstants.VIEW_COLLECTION_DRIVER_ACCESS_RIGHTS_ALIAS));
		createServiceActions.add(getAction(businessSubModule, BusinessModelConstants.CREATE_DISTRIBUTION_DRIVER_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.CREATE_DISTRIBUTION_DRIVER_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.CREATE_DISTRIBUTION_DRIVER_ACCESS_RIGHTS_ALIAS));
		createServiceActions.add(getAction(businessSubModule, BusinessModelConstants.CONFIGURE_DISTRIBUTION_DRIVER_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.CONFIGURE_DISTRIBUTION_DRIVER_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.CONFIGURE_DISTRIBUTION_DRIVER_ACCESS_RIGHTS_ALIAS));
		createServiceActions.add(getAction(businessSubModule, BusinessModelConstants.DELETE_DISTRIBUTION_DRIVER_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.DELETE_DISTRIBUTION_DRIVER_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.DELETE_DISTRIBUTION_DRIVER_ACCESS_RIGHTS_ALIAS));
		createServiceActions.add(getAction(businessSubModule, BusinessModelConstants.UPDATE_DISTRIBUTION_DRIVER_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.UPDATE_DISTRIBUTION_DRIVER_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.UPDATE_DISTRIBUTION_DRIVER_ACCESS_RIGHTS_ALIAS));
		createServiceActions.add(getAction(businessSubModule, BusinessModelConstants.VIEW_DISTRIBUTION_DRIVER_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.VIEW_DISTRIBUTION_DRIVER_ACCESS_RIGHTS_NAME,
																									 BusinessModelConstants.VIEW_DISTRIBUTION_DRIVER_ACCESS_RIGHTS_NAME));
		
		createServiceActions.add(getAction(businessSubModule, BusinessModelConstants.CREATE_COLLECTION_CLIENT_ACCESS_RIGHTS_NAME,
				 																					BusinessModelConstants.CREATE_COLLECTION_CLIENT_ACCESS_RIGHTS_NAME,
				 																					BusinessModelConstants.CREATE_COLLECTION_CLIENT_ACCESS_RIGHTS_ALIAS));
		createServiceActions.add(getAction(businessSubModule, BusinessModelConstants.CONFIGURE_COLLECTION_CLIENT_ACCESS_RIGHTS_NAME,
						 																			BusinessModelConstants.CONFIGURE_COLLECTION_CLIENT_ACCESS_RIGHTS_NAME,
						 																			BusinessModelConstants.CONFIGURE_COLLECTION_CLIENT_ACCESS_RIGHTS_ALIAS));
		createServiceActions.add(getAction(businessSubModule, BusinessModelConstants.DELETE_COLLECTION_CLIENT_ACCESS_RIGHTS_NAME,
						 																			BusinessModelConstants.DELETE_COLLECTION_CLIENT_ACCESS_RIGHTS_NAME,
						 																			BusinessModelConstants.DELETE_COLLECTION_CLIENT_ACCESS_RIGHTS_ALIAS));
		createServiceActions.add(getAction(businessSubModule, BusinessModelConstants.UPDATE_COLLECTION_CLIENT_ACCESS_RIGHTS_NAME,
						 																			BusinessModelConstants.UPDATE_COLLECTION_CLIENT_ACCESS_RIGHTS_NAME,
						 																			BusinessModelConstants.UPDATE_COLLECTION_CLIENT_ACCESS_RIGHTS_ALIAS));
		createServiceActions.add(getAction(businessSubModule, BusinessModelConstants.VIEW_COLLECTION_CLIENT_ACCESS_RIGHTS_NAME,
						 																			BusinessModelConstants.VIEW_COLLECTION_CLIENT_ACCESS_RIGHTS_NAME,
						 																			BusinessModelConstants.VIEW_COLLECTION_CLIENT_ACCESS_RIGHTS_ALIAS));
		
		businessSubModule.setActionList(createServiceActions);
		return businessSubModule;
	}

	private BusinessSubModule getServiceMgmtBusinessSubModule(BusinessModule parentBusinessModule){
		BusinessSubModule businessSubModule = getBusinessSubModule(
												BusinessModelConstants.SERVICE_MANAGEMENT_TAB_NAME,
												BusinessModelConstants.SERVICE_MANAGEMENT_TAB_NAME,
												BusinessModelConstants.SERVICE_MANAGEMENT_TAB_ALIAS,
												parentBusinessModule);
		serviceMgmtActions = new ArrayList<>();
		serviceMgmtActions.add(getAction(businessSubModule,
																BusinessModelConstants.SEARCH_SERVICE_ACCESS_RIGHTS_NAME, 
																BusinessModelConstants.SEARCH_SERVICE_ACCESS_RIGHTS_NAME, 
																BusinessModelConstants.SEARCH_SERVICE_ACCESS_RIGHTS_ALIAS));
		serviceMgmtActions.add(getAction(businessSubModule,
																  BusinessModelConstants.SYNC_SERVICE_ACCESS_RIGHTS_NAME,
																  BusinessModelConstants.SYNC_SERVICE_ACCESS_RIGHTS_NAME,
																  BusinessModelConstants.SYNC_SERVICE_ACCESS_RIGHTS_ALIAS));
		serviceMgmtActions.add(getAction(businessSubModule, 
																 BusinessModelConstants.IMPORT_SERVICE_ACCESS_RIGHTS_NAME,
																 BusinessModelConstants.IMPORT_SERVICE_ACCESS_RIGHTS_NAME, 
																 BusinessModelConstants.IMPORT_SERVICE_ACCESS_RIGHTS_ALIAS));
		serviceMgmtActions.add(getAction(businessSubModule, 
																 BusinessModelConstants.EXPORT_SERVICE_ACCESS_RIGHTS_NAME,
																 BusinessModelConstants.EXPORT_SERVICE_ACCESS_RIGHTS_NAME,
																 BusinessModelConstants.EXPORT_SERVICE_ACCESS_RIGHTS_ALIAS));
		serviceMgmtActions.add(getAction(businessSubModule, 
																 BusinessModelConstants.START_SERVICE_ACCESS_RIGHTS_NAME, 
																 BusinessModelConstants.START_SERVICE_ACCESS_RIGHTS_NAME,
																 BusinessModelConstants.START_SERVICE_ACCESS_RIGHTS_ALIAS));
		serviceMgmtActions.add(getAction(businessSubModule, 
																 BusinessModelConstants.STOP_SERVICE_ACCESS_RIGHTS_NAME, 
																 BusinessModelConstants.STOP_SERVICE_ACCESS_RIGHTS_NAME, 
																 BusinessModelConstants.STOP_SERVICE_ACCESS_RIGHTS_ALIAS));
		
		businessSubModule.setActionList(serviceMgmtActions);
		return businessSubModule;
	}

	public List<Action> getserviceMgmtActions() {
		return serviceMgmtActions;
	}

	public void setserviceMgmtActions(List<Action> serviceMgmtActions) {
		this.serviceMgmtActions = serviceMgmtActions;
	}

	public List<Action> getcreateServiceActions() {
		return createServiceActions;
	}

	public void setcreateServiceActions(List<Action> createServiceActions) {
		this.createServiceActions = createServiceActions;
	}

}
