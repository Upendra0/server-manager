/**
 * 
 */
package com.elitecore.sm.iam.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.util.JSTreeBusinessModelData;
import com.elitecore.sm.iam.model.BusinessModel;
import com.elitecore.sm.iam.service.MenuService;

/**
 * @author Sunil Gulabani
 * Apr 6, 2015
 */
@Controller
public class MenuController {
	
	@Autowired(required=true)
	@Qualifier(value="menuService")
	private MenuService menuService;
	
	/**
	 * This method provides the list of Business Model, Business Module, Business Sub-Module and Actions in JS Tree Json Format.
	 * @param actionIdsSelected This is used to keep the default selected nodes. This parameter will be received only in case of the Update Access Group.
	 * @return
	 */
	@RequestMapping(value = ControllerConstants.GET_MENU_TAB_ACTION_LIST, method = RequestMethod.POST)
	@ResponseBody public  String getMenuTabActionList(
    		@RequestParam(value = "actionIdsSelected",required=false) String actionIdsSelected
    		){
		String actionIds[] = null;
		if(actionIdsSelected!= null && !actionIdsSelected.equals("") ){
			actionIds = actionIdsSelected.split(",");
		}
		
		List<BusinessModel> modelList = this.menuService.getActiveModelHierarchy();
		return new JSTreeBusinessModelData(modelList,actionIds,false).toString();
	}

	public MenuService getMenuService() {
		return menuService;
	}

	public void setMenuService(MenuService menuService) {
		this.menuService = menuService;
	}	
}