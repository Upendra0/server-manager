/**
 * 
 */
package com.elitecore.sm.common.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONValue;

import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.iam.model.Action;
import com.elitecore.sm.iam.model.BusinessModel;
import com.elitecore.sm.iam.model.BusinessModule;
import com.elitecore.sm.iam.model.BusinessSubModule;

/**
 * @author Sunil Gulabani
 * Apr 14, 2015
 */
public class JSTreeBusinessModelData {
	private List<BusinessModel> businessModelList ;
	private String actionIdsToBeSeleted[];
	private boolean isProfileAdmin;
	
	public JSTreeBusinessModelData() {
		//  Auto-generated constructor stub
	}
	
	public JSTreeBusinessModelData(List<BusinessModel> businessModelList, String actionIdsToBeSeleted[],boolean isProfileAdmin) {
		this.businessModelList = businessModelList;
		this.actionIdsToBeSeleted = actionIdsToBeSeleted;
		this.isProfileAdmin=isProfileAdmin;
	}
	@Override
	public String toString(){
		List<Map<String, Object>> root = new ArrayList<>(); // sonar issue  Line-36 
		
		if(businessModelList!=null){
			
			Map<String, Object> modelMap = null;
			
			for(BusinessModel model : businessModelList){
				modelMap = new HashMap<>(); // sonar issue line _43
				modelMap.put("id", "MODEL_" + model.getId());
				modelMap.put("text", model.getName());
				modelMap.put("icon", "images/model.png");
				
				if(model.getBusinessModuleList()!=null && !model.getBusinessModuleList().isEmpty()){
					List<Map<String, Object>> moduleChildren = new ArrayList<>(); // sonar issue Line_49
					Map<String, Object> moduleMap = null;
					
					for(BusinessModule module : model.getBusinessModuleList()){
						moduleMap = new HashMap<>(); // sonar issue Line _53
						moduleMap.put("id", "MODULE_" + module.getId());
						moduleMap.put("text", module.getName());
						moduleMap.put("icon", "images/menu.png");
						
						if(module.getSubModuleList()!=null && !module.getSubModuleList().isEmpty()){
							List<Map<String, Object>> subModuleChildren = new ArrayList<>(); //Sonar issue Line_59
							Map<String, Object> subModuleMap = null;
							
							
							for(BusinessSubModule subModule : module.getSubModuleList()){
								subModuleMap = new HashMap<>(); //Line_64
								subModuleMap.put("id", "SUB_MODULE_" + subModule.getId());
								subModuleMap.put("text", subModule.getName());
								subModuleMap.put("icon", "images/tabs.png");
								
								if(subModule.getActionList()!=null && !subModule.getActionList().isEmpty()){
									List<Map<String, Object>> actionChildren = new ArrayList<>(); // Line_70
									Map<String, Object> actionMap = null;
									for(Action action : subModule.getActionList()){
										actionMap = new HashMap<>(); //line 73
										actionMap.put("id", "ACTION_" + action.getId());
										actionMap.put("text", action.getName());
										actionMap.put("icon", "images/action.png");
										
										if(actionIdsToBeSeleted!=null){
											Map<String,Object> actionStateMap = null;
											actionStateMap = new HashMap<>(); // Line 80
											for(String actionIdSelected : actionIdsToBeSeleted){
												if(actionIdSelected.equals(String.valueOf(action.getId()))){
													actionStateMap.put("selected", true);
													break;
												}
											}
											actionMap.put("state", actionStateMap);
										}else{
											if(isProfileAdmin){
												if (StateEnum.ACTIVE.equals(action.getStatus())) {
													Map<String,Object> actionStateMap = new HashMap<>();
													actionStateMap.put("selected", true);

													actionMap.put("state", actionStateMap);
												}
											}
										}
										
										actionChildren.add(actionMap);
									}
									
									if(!actionChildren.isEmpty())
										subModuleMap.put("children", actionChildren);
									
								}
								subModuleChildren.add(subModuleMap);
							}
							if(!subModuleChildren.isEmpty())
								moduleMap.put("children", subModuleChildren);
						}
						moduleChildren.add(moduleMap);
					}
					
					if(!moduleChildren.isEmpty())
						modelMap.put("children", moduleChildren);
				}
				root.add(modelMap);
			}
		}
		return JSONValue.toJSONString(root);
	}
}