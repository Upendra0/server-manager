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
 * Jul 6, 2015
 */
public class ServerManagerBusinessModel extends BaseBusinessModel{
	
	private List<Action> serverMgmtActions = null;
	private List<Action> createServerActions = null;

	public BusinessModule getServerManagerBusinessModule(BusinessModel parentBusinessModel){
		BusinessModule businessModule = getBusinessModule("Server Manager", "Server Manager", "SERVER_MANAGER_MENU_VIEW", parentBusinessModel);

		Set<BusinessSubModule> subModuleSet = new HashSet<>();
		subModuleSet.add(createServerBusinessSubModule(businessModule));
		subModuleSet.add(getServerMgmtBusinessSubModule(businessModule));
		businessModule.setSubModuleList(subModuleSet);
		return businessModule;
	}
	
	private BusinessSubModule createServerBusinessSubModule(BusinessModule parentBusinessModule){
		BusinessSubModule businessSubModule = getBusinessSubModule(
														"Create Server",
														"Create Server",
														"CREATE_SERVER",
														parentBusinessModule);
		
		createServerActions = new ArrayList<>();
		createServerActions.add(getAction(businessSubModule, "View Server And Server Instances", "View Server And Server Instances", "VIEW_SERVER_AND_SERVER_INSTANCES"));
		createServerActions.add(getAction(businessSubModule, "Create Server And Server Instances", "Create Server And Server Instances", "CREATE_SERVER_AND_SERVER_INSTANCES"));
		createServerActions.add(getAction(businessSubModule, "Update Server And Server Instances", "Update Server And Server Instances", "UPDATE_SERVER_AND_SERVER_INSTANCES"));
		createServerActions.add(getAction(businessSubModule, "Delete Server And Server Instances", "Delete Server And Server Instances", "DELETE_SERVER_AND_SERVER_INSTANCES"));
		
		
		businessSubModule.setActionList(createServerActions);
		return businessSubModule;
	}

	private BusinessSubModule getServerMgmtBusinessSubModule(BusinessModule parentBusinessModule){
		BusinessSubModule businessSubModule = getBusinessSubModule(
												"Server Management",
												"Server Management",
												"SERVER_MANAGEMENT",
												parentBusinessModule);
		serverMgmtActions = new ArrayList<>();
		serverMgmtActions.add(getAction(businessSubModule, "Server Instance Synchronization", "Server Instance Synchronization", "SERVER_INSTANCE_SYNCHRONIZATION"));
		serverMgmtActions.add(getAction(businessSubModule, "Server Instance Import Config", "Server Instance Import Config", "SERVER_INSTANCE_IMPORT_CONFIG"));
		serverMgmtActions.add(getAction(businessSubModule, "Server Instance Export Config", "Server Instance Export Config", "SERVER_INSTANCE_EXPORT_CONFIG"));
		serverMgmtActions.add(getAction(businessSubModule, "Server Instance Copy Config", "Server Instance Copy Config", "SERVER_INSTANCE_COPY_CONFIG"));
		serverMgmtActions.add(getAction(businessSubModule, "Server Instance Soft Restart", "Server Instance Soft Restart", "SERVER_INSTANCE_SOFT_RESTART"));
		serverMgmtActions.add(getAction(businessSubModule, "Server Instance Reload Config", "Server Instance Reload Config", "SERVER_INSTANCE_RELOAD_CONFIG"));
		serverMgmtActions.add(getAction(businessSubModule, "Server Instance Reload Cache", "Server Instance Reload Cache", "SERVER_INSTANCE_RELOAD_CACHE"));
		
		//serverMgmtActions.add(getAction(businessSubModule, "Server Instance Search", "Search Server Instance", "SERVER_INSTANCE_SEARCH"));
		serverMgmtActions.add(getAction(businessSubModule, "Server Instance Start", "Server Instance Start", "SERVER_INSTANCE_START"));
		serverMgmtActions.add(getAction(businessSubModule, "Server Instance Stop", "Server Instance Stop", "SERVER_INSTANCE_STOP"));
		serverMgmtActions.add(getAction(businessSubModule, "Server Instance Upload License", "Server Instance Upload License", "SERVER_INSTANCE_UPLOAD_LICENSE"));
		
		serverMgmtActions.add(getAction(businessSubModule, "View NMS Config", "Configure NMS Config","CONFIGURE_NMS_CONFIG"));
		
		serverMgmtActions.add(getAction(businessSubModule, "View SNMP Alert Config", "View SNMP Alert Config","VIEW_SNMP_ALERT_CONFIG"));
		serverMgmtActions.add(getAction(businessSubModule, "Add SNMP Alert", "Add SNMP Alert","ADD_SNMP_ALERT"));
		serverMgmtActions.add(getAction(businessSubModule, "Update SNMP Alert", "Update SNMP Alert","UPDATE_SNMP_ALERT"));
		serverMgmtActions.add(getAction(businessSubModule, "Delete SNMP Alert", "Delete SNMP Alert","DELETE_SNMP_ALERT"));
		
		serverMgmtActions.add(getAction(businessSubModule, "View Consolidation Statistic", "View Consolidation Statistic","VIEW_CONSOLIDATION_STATISTIC"));
		serverMgmtActions.add(getAction(businessSubModule, "Add Consolidation Statistic", "Add Consolidation Statistic","ADD_CONSOLIDATION_STATISTIC"));
		serverMgmtActions.add(getAction(businessSubModule, "Update Consolidation Statistic", "Update Consolidation Statistic","UPDATE_CONSOLIDATION_STATISTIC"));
		serverMgmtActions.add(getAction(businessSubModule, "Delete Consolidation Statistic", "Delete Consolidation Statistic","DELETE_CONSOLIDATION_STATISTIC"));
		
		serverMgmtActions.add(getAction(businessSubModule, "View Webservice Detail", "View Webservice Detail","VIEW_WEBSERVICE_DETAIL"));
		serverMgmtActions.add(getAction(businessSubModule, "Add Webservice Detail", "Add Webservice Detail","ADD_WEBSERVICE_DETAIL"));
		serverMgmtActions.add(getAction(businessSubModule, "Update Webservice Detail", "Update Webservice Detail","UPDATE_WEBSERVICE_DETAIL"));
		serverMgmtActions.add(getAction(businessSubModule, "Delete Webservice Detail", "Delete Webservice Detail","DELETE_WEBSERVICE_DETAIL"));
		
		businessSubModule.setActionList(serverMgmtActions);
		return businessSubModule;
	}

	public List<Action> getServerMgmtActions() {
		return serverMgmtActions;
	}

	public void setServerMgmtActions(List<Action> serverMgmtActions) {
		this.serverMgmtActions = serverMgmtActions;
	}

	public List<Action> getCreateServerActions() {
		return createServerActions;
	}

	public void setCreateServerActions(List<Action> createServerActions) {
		this.createServerActions = createServerActions;
	}
}