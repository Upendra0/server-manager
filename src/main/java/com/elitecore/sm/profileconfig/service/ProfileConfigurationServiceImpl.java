package com.elitecore.sm.profileconfig.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.elitecore.sm.common.aspect.Auditable;
import com.elitecore.sm.common.constants.AuditConstants;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.iam.dao.AccessGroupDAO;
import com.elitecore.sm.iam.model.AccessGroup;
import com.elitecore.sm.iam.model.Action;
import com.elitecore.sm.iam.model.BusinessModule;
import com.elitecore.sm.iam.model.BusinessSubModule;
import com.elitecore.sm.iam.service.MenuService;
import com.elitecore.sm.productconfig.model.ProfileEntity;

/**
 * 
 * @author avani.panchal
 *
 */
@Service(value="profileConfigurationService")
public class ProfileConfigurationServiceImpl implements ProfileConfigurationService{
	
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
	MenuService menuService;
	
	@Autowired
	AccessGroupDAO accessGroupDao;
	
	/**
	 * Update  profile information
	 * @param selectedActionList
	 * @param selectedSubModuleList
	 * @param selectedModulesList
	 * @param staffId
	 * @return ResponseObject
	 */
	@Transactional
	@Override
	public ResponseObject updateProfileConfiguration(String selectedActionList,String selectedSubModuleList,String selectedModulesList,int staffId){

		ResponseObject responseObject=new ResponseObject();
		
		updateActionConfiguration(selectedActionList,staffId);
		
		updateBusinessSubModuleConfig(selectedSubModuleList,staffId);
		
		updateBusinessModelConfig(selectedModulesList,staffId);
		
		updateAccessGroupActions(selectedActionList);
		
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.PROFILE_CONFIGURATION_UPDATE_SUCCESS);
		
		return responseObject;
		
	}
	
	private void updateActionConfiguration(String selectedActionList,int staffId){
		logger.debug("inside updateProfileConfiguration selectedActions:: " +selectedActionList );
		
		boolean isActive=false;
		if(!StringUtils.isEmpty(selectedActionList)){
			
			List<Action> actionList=menuService.getMenuDAO().getAllActions();
			
			JSONArray selectedEntitiesArr=new JSONArray(selectedActionList);
			if(actionList !=null && !actionList.isEmpty()){
				
				ProfileConfigurationService profileConfigurationService = (ProfileConfigurationService) SpringApplicationContext.getBean("profileConfigurationService");
				
				for(int i=0,size=actionList.size();i<size;i++){
					Action action=actionList.get(i);
				
					for(int j =0 , size1 = selectedEntitiesArr.length(); j <size1 ; j++ ){
						if(action.getId() == selectedEntitiesArr.getInt(j)){
							isActive=true;
							break;
						}else{
							isActive=false;
							action.setStatus(StateEnum.INACTIVE);
						}
				}
				
					if(isActive){
						logger.debug("Make Profile Entity Active for entity id " + action.getId());
						action.setStatus(StateEnum.ACTIVE);
					}else{
						logger.debug("Make Profile Entity Inactive for entity id " + action.getId());
						action.setStatus(StateEnum.INACTIVE);
					}
					action.setLastUpdatedDate(new Date());
					action.setLastUpdatedByStaffId(staffId);
					
					profileConfigurationService.updateActionConfiguration2(action);

			}
		}else{
			logger.debug("Fail to fetch Actions" );
		}
			
		}
	}

	/** 
	 * Following method is added for audit purpose
	 * */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_PROFILE_CONFIGURATION,actionType = BaseConstants.UPDATE_ACTION, currentEntity = ProfileEntity.class, ignorePropList= "")
	public ResponseObject updateActionConfiguration2(Action action){
		ResponseObject responseObject = new ResponseObject();
		menuService.getMenuDAO().updateAction(action);
		return responseObject;
	}
	
	private void updateBusinessSubModuleConfig(String selectedSubModuleList,int staffId){
		logger.debug("inside updateProfileConfiguration selectedSubModuleList:: " +selectedSubModuleList );
		
		boolean isActive=false;
			
			List<BusinessSubModule> subModuleList=menuService.getMenuDAO().getAllBusinessSubModule();
			
			JSONArray selectedEntitiesArr=new JSONArray(selectedSubModuleList);
			if(subModuleList !=null && !subModuleList.isEmpty()){
				
				ProfileConfigurationService profileConfigurationService = (ProfileConfigurationService) SpringApplicationContext.getBean("profileConfigurationService");
				
				for(int i=0,size=subModuleList.size();i<size;i++){
					BusinessSubModule businessSubModule=subModuleList.get(i);
				
					for(int j =0 , size1 = selectedEntitiesArr.length(); j <size1 ; j++ ){
						if(businessSubModule.getId() == selectedEntitiesArr.getInt(j)){
							isActive=true;
							break;
						}else{
							isActive=false;
							businessSubModule.setStatus(StateEnum.INACTIVE);
						}
				}
				
					if(isActive){
						logger.debug("Make Business Sub module Active for entity id " + businessSubModule.getId());
						businessSubModule.setStatus(StateEnum.ACTIVE);
					}else{
						logger.debug("Make Business Sub module Inactive for entity id " + businessSubModule.getId());
						businessSubModule.setStatus(StateEnum.INACTIVE);
					}
					businessSubModule.setLastUpdatedDate(new Date());
					businessSubModule.setLastUpdatedByStaffId(staffId);
					
					profileConfigurationService.updateBusinessSubModuleConfig2(businessSubModule);
					
			}
		}else{
			logger.debug("Fail to fetch Actions" );
		}

		
	}
	
	/** 
	 * Following method is added for audit purpose
	 * */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_PROFILE_BUSINESS_SUB_CONFIGURATION,actionType = BaseConstants.UPDATE_ACTION, currentEntity = ProfileEntity.class, ignorePropList= "")
	public ResponseObject updateBusinessSubModuleConfig2(BusinessSubModule businessSubModule){
		ResponseObject responseObject = new ResponseObject();
		menuService.getMenuDAO().updateBusinessSubModule(businessSubModule);
		return responseObject;
	}
	
	private void updateBusinessModelConfig(String selectedModulesList,int staffId){
		logger.debug("inside updateProfileConfiguration selectedModulesList:: " +selectedModulesList );
		
		boolean isActive=false;
	
			List<BusinessModule> moduleList=menuService.getMenuDAO().getAllBusinessModule();
			
			JSONArray selectedEntitiesArr=new JSONArray(selectedModulesList);
			if(moduleList !=null && !moduleList.isEmpty()){
				
				ProfileConfigurationService profileConfigurationService = (ProfileConfigurationService) SpringApplicationContext.getBean("profileConfigurationService");
				
				for(int i=0,size=moduleList.size();i<size;i++){
					BusinessModule businessModule=moduleList.get(i);
				
					for(int j =0 , size1 = selectedEntitiesArr.length(); j <size1 ; j++ ){
						if(businessModule.getId() == selectedEntitiesArr.getInt(j)){
							isActive=true;
							break;
						}else{
							isActive=false;
							businessModule.setStatus(StateEnum.INACTIVE);
						}
				}
				
					if(isActive){
						logger.debug("Make Profile Entity Active for entity id " + businessModule.getId());
						businessModule.setStatus(StateEnum.ACTIVE);
					}else{
						logger.debug("Make Profile Entity Inactive for entity id " + businessModule.getId());
						businessModule.setStatus(StateEnum.INACTIVE);
					}
					businessModule.setLastUpdatedDate(new Date());
					businessModule.setLastUpdatedByStaffId(staffId);
					
					profileConfigurationService.updateBusinessModelConfig2(businessModule);
			}
		}else{
			logger.debug("Fail to fetch Actions" );
		}
	
	}
	
	/** 
	 * Following method is added for audit purpose
	 * */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_PROFILE_BUSINESS_CONFIGURATION,actionType = BaseConstants.UPDATE_ACTION, currentEntity = ProfileEntity.class, ignorePropList= "")
	public ResponseObject updateBusinessModelConfig2(BusinessModule businessModule){
		ResponseObject responseObject = new ResponseObject();
		menuService.getMenuDAO().updateBusinessModule(businessModule);
		return responseObject;	
	}
	
	/**
	 * Update action bind with access group
	 * @param selectedActionList
	 * @param staffId
	 */
	private void updateAccessGroupActions(String selectedActionList){
		
		logger.debug("inside updateAccessGroupActions selectedActions:: " +selectedActionList );
		
		boolean isActive=false;
		if(!StringUtils.isEmpty(selectedActionList)){

			List<AccessGroup> accessGroupList=accessGroupDao.getAllAccessGroup();
			
			JSONArray selectedEntitiesArr=new JSONArray(selectedActionList);
			if(accessGroupList !=null && !accessGroupList.isEmpty()){
				
				ProfileConfigurationService profileConfigurationService = (ProfileConfigurationService) SpringApplicationContext.getBean("profileConfigurationService");
				
				for(int i=0,size=accessGroupList.size();i<size;i++){
					AccessGroup accessGroup=accessGroupList.get(i);
					logger.debug("Update Access Group Action for :: " +accessGroup.getName() );
					List<Action> actionList=accessGroup.getActions();
					if(actionList !=null && !actionList.isEmpty()){
						Iterator<Action> itr=actionList.listIterator();
						while(itr.hasNext()){
							
							Action action=itr.next();
							
							for(int j =0 , size1 = selectedEntitiesArr.length(); j <size1 ; j++ ){
								if(action.getId() == selectedEntitiesArr.getInt(j)){
									isActive=true;
									break;
								}else{
									isActive=false;
								}
						}
							
							if(isActive){
								logger.debug("Active Action " + action.getId());
							}else{
								logger.debug("Inactive action , so unbind it from access group " + action.getId());
								itr.remove();
							}
						}
						accessGroup.setActions(actionList);
						profileConfigurationService.updateAccessGroupActions2(accessGroup);
						
					}
			}
		}else{
			logger.debug("Fail to fetch Actions" );
		}
			
		}
		
	}
	
	/** 
	 * Following method is added for audit purpose
	 * */
	@Override
	@Transactional
	@Auditable(auditActivity = AuditConstants.UPDATE_PROFILE_ACCESSGROUP_CONFIGURATION,actionType = BaseConstants.UPDATE_ACTION, currentEntity = ProfileEntity.class, ignorePropList= "")
	public ResponseObject updateAccessGroupActions2(AccessGroup accessGroup){
		ResponseObject responseObject = new ResponseObject();
		accessGroupDao.merge(accessGroup);		
		return responseObject;
	}

}
