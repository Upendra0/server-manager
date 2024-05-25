/**
 * 
 */
package com.elitecore.sm.server.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.dictionarymanager.model.DictionaryConfig;
import com.elitecore.sm.dictionarymanager.service.DictionaryConfigService;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.server.model.ServerType;
import com.elitecore.sm.server.service.ServerService;
import com.elitecore.sm.server.validator.ServerValidator;
import com.elitecore.sm.serverinstance.dao.ServerInstanceDao;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.serverinstance.service.ServerInstanceService;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.MapCache;

/**
 * @author Sunil Gulabani
 * Jul 2, 2015
 */
@Controller
public class ServerController extends BaseController{
	
	@Autowired(required=true)
	@Qualifier(value="dictionaryConfigService")
	private DictionaryConfigService dictionaryConfigService;

	@Autowired(required=true)
	@Qualifier(value="serverService")
	private ServerService serverService;
	
	@Autowired(required=true)
	@Qualifier(value="serverInstanceService")
	private ServerInstanceService serverInstanceService;
	
	@Autowired
	private ServerValidator validator;
	
	@Autowired
	private ServerInstanceDao serverInstanceDao;
	
	
	/**
	 * This function will be invoked to convert the Date to specified format.
	 * So we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
	    binder.setValidator(validator);
	}
	
	/**
	 * Initialize server manager page
	 * @param REQUEST_ACTION_TYPE_FROM_FORM
	 * @return Initialize server manager page as response object
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@PreAuthorize("hasAnyAuthority('SERVER_MANAGER_MENU_VIEW')")
	@RequestMapping(value = ControllerConstants.INIT_SERVER_MANAGER, method = RequestMethod.GET)
	public ModelAndView initServerManager(
		@RequestParam(value=BaseConstants.REQUEST_ACTION_TYPE,required=false) String requestActionType){
		ModelAndView model = new ModelAndView(ViewNameConstants.SERVER_MANAGER);
		
		if(requestActionType != null){
			if (requestActionType.equals(BaseConstants.SERVER_MANAGEMENT)){
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.SERVER_MANAGEMENT);
				
			} else if (requestActionType.equals(BaseConstants.CREATE_SERVER)) {
			
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.CREATE_SERVER);
					Map<String,List> serverAndServerInstanceMap = serverInstanceService.getAllServerAndItsInstance();
					if(serverAndServerInstanceMap!=null && serverAndServerInstanceMap.size()>0){
						 for (Map.Entry<String,List> entry : serverAndServerInstanceMap.entrySet()) {
								model.addObject(entry.getKey(), entry.getValue());
					}
					}else{
						logger.info("Server and Server Instance Map is null or zero");
					}
					model.addObject(FormBeanConstants.SERVER_INSTANCE_FORM_BEAN,(ServerInstance) SpringApplicationContext.getBean(ServerInstance.class));
			
			}
		} else {
			// Default Tab Selected called from left menu.
			if(eliteUtils.isAuthorityGranted("SERVER_MANAGEMENT"))
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.SERVER_MANAGEMENT);
			else if(eliteUtils.isAuthorityGranted("CREATE_SERVER_AND_SERVER_INSTANCES")){
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.CREATE_SERVER);
				model.addObject(FormBeanConstants.SERVER_INSTANCE_FORM_BEAN,(ServerInstance) SpringApplicationContext.getBean(ServerInstance.class));
				
				Map<String,List> serverAndServerInstanceMap = serverInstanceService.getAllServerAndItsInstance();
				if(serverAndServerInstanceMap!=null && serverAndServerInstanceMap.size()>0){
					
					 for (Map.Entry<String,List> entry : serverAndServerInstanceMap.entrySet()) {
						model.addObject(entry.getKey(), entry.getValue());
					}
				}else{
					logger.info("Server and Server Instance Map is null or zero");
				}
			}
		}
		List<ServerType> serverType = (List<ServerType>) MapCache.getConfigValueAsObject(SystemParametersConstant.ACTIVE_SERVER_TYPE_LIST);
		model.addObject(BaseConstants.SERVER_TYPE_LIST, serverType);
		model.addObject(BaseConstants.BOOTSTRAP_JS_FLAG,false);
		return model;
	}
	
	/**
	 * Initialize add server screen with defaults
	 * Initialize add server page
	 * @return Initialize add server response as model object
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasAnyAuthority('CREATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.INIT_ADD_SERVER, method = RequestMethod.GET)
	public ModelAndView initCreateServer(){
		ModelAndView model = new ModelAndView(ViewNameConstants.CREATE_SERVER);
		
		List<ServerType> serverType = (List<ServerType>) MapCache.getConfigValueAsObject(SystemParametersConstant.ACTIVE_SERVER_TYPE_LIST);
	
		model.addObject(BaseConstants.SERVER_TYPE_LIST, serverType);
		model.addObject(FormBeanConstants.SERVER_FORM_BEAN,(Server) SpringApplicationContext.getBean(Server.class));
		return model;
	}
	
	/**
	 * Add Server in database.
	 * @param server
	 * @param result
	 * @param status
	 * @param request
	 * @return Add server response as ajax response object
	 */
	@PreAuthorize("hasAnyAuthority('CREATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.ADD_SERVER, method = RequestMethod.POST)
	@ResponseBody public  String addServer(
			@ModelAttribute(value = FormBeanConstants.SERVER_FORM_BEAN) Server server,//NOSONAR
			BindingResult result,HttpServletRequest request){
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		validator.validateServerParameters(server, result, null, false);
		//Check validation errors
	    if (result.hasErrors()) {
	    	ajaxResponse.setResponseCode("500");
			ajaxResponse.setResponseMsg(getMessage("server.add.failed"));
			
			Map<String, String> errorMsgs = new HashMap<>();
			
			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(error.getField(),error.getDefaultMessage());
				}
			}
			
			ajaxResponse.setObject(errorMsgs);
	    }else{
			ResponseObject responseObject = serverService.addServer(server);
			if(responseObject.isSuccess()){
				//Start:MED-10159 : do entry in TBLTFILEINFO from default dictionary data
				List<DictionaryConfig> dictionaryConfigList=(List<DictionaryConfig>)dictionaryConfigService.getDefaultDictionaryConfigObj();
				for(DictionaryConfig dictionaryConfig : dictionaryConfigList) {
					ResponseObject dictResponseObject=dictionaryConfigService.createDictionaryData(dictionaryConfig,server);
					if(dictResponseObject.isSuccess()){
						dictionaryConfig.setId(0);
						dictionaryConfig.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
						dictionaryConfig.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
						logger.debug("INSERT SUCCESS : "+ dictionaryConfig.getIpAddress() + " " + dictionaryConfig.getUtilityPort());
					}else{
						logger.debug("INSERT FAIL : "+ dictionaryConfig.getIpAddress() + " " + dictionaryConfig.getUtilityPort());
					}
				}//END
			}
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
	    }
	    
		return ajaxResponse.toString();
	}
	
	
	@PreAuthorize("hasAnyAuthority('CREATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = "addServerAngular", method = RequestMethod.POST)
	@ResponseBody
	public String addServerAngular(@RequestBody Server server) {//NOSONAR
		ResponseObject responseObject = serverService.addServer(server);
		AjaxResponse ajaxResponse  = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	/**
	 * Initialize Update server page 
	 * @param serverInstanceId
	 * @return Initialize update server response as model object
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.INIT_UPDATE_SERVER, method = RequestMethod.GET)
	public ModelAndView initUpdateServer(
			@RequestParam(value = "id",required=true) String serverInstanceId){
		
		 int iserverInstanceId=Integer.parseInt(serverInstanceId);
		Server server = serverService.getServer(iserverInstanceId);
		List<ServerInstance> serverInstanceList = serverInstanceService.getServerInstanceByServerId(server.getId());
		
		ModelAndView model = new ModelAndView(ViewNameConstants.UPDATE_SERVER);
		model.addObject(FormBeanConstants.SERVER_FORM_BEAN,server);
		model.addObject(BaseConstants.SERVER_TYPE_LIST, serverService.getAllServerTypeList());
		model.addObject("truefalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject(BaseConstants.SERVER_INSTANCE_LIST, serverInstanceList);
		return model;
	}
	
	/**
	 * Update Server in database.
	 * @param server
	 * @param result
	 * @param status
	 * @param request
	 * @return Update server in db response as ajax response object
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.UPDATE_SERVER, method = RequestMethod.POST)
	@ResponseBody public  String updateServer(
			 @ModelAttribute(value = FormBeanConstants.SERVER_FORM_BEAN) Server server,//NOSONAR
			 BindingResult result){
		AjaxResponse ajaxResponse = new AjaxResponse();
		validator.validateServerParameters(server, result, null, false);
		//Check validation errors
	    if (result.hasErrors()) {
	    	ajaxResponse.setResponseCode("500");
			ajaxResponse.setResponseMsg(getMessage("server.update.failed"));
			
			Map<String, String> errorMsgs = new HashMap<>();
			
			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);
	    }else{
			ResponseObject responseObject = serverService.updateServer(server);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
	    }
	    logger.info("server: " + server);
		return ajaxResponse.toString();
	}
	
	/**
	 * Logical Deletes Server in database.
	 * @param server
	 * @param result
	 * @param status
	 * @param request
	 * @return Delete operation response as ajax response
	 */
	@PreAuthorize("hasAnyAuthority('DELETE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.DELETE_SERVER, method = RequestMethod.POST)
	@ResponseBody public  String deleteServer(
			@RequestParam(value="serverId",required=true) String serverId,
			HttpServletRequest request){
		AjaxResponse ajaxResponse ;
		
		int iserverId=Integer.parseInt(serverId);
		Server serverObj=serverService.getServer(Integer.parseInt(serverId));
		ResponseObject responseObject = serverService.deleteServer(iserverId,eliteUtils.getLoggedInStaffId(request));
		
		//MED-10159 : delete entry from TBLTFILEINFO,when server is deleted
		 if(responseObject.isSuccess()){
			 List<DictionaryConfig> dictionaryConfigList=dictionaryConfigService.getDictionaryConfigList(serverObj.getIpAddress(), serverObj.getUtilityPort());
			 for(DictionaryConfig dictionaryConfig : dictionaryConfigList) {
				 dictionaryConfigService.deleteDictionaryData(dictionaryConfig);
			 }
		 }
		//END
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
	   
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('DELETE_SERVER_AND_SERVER_INSTANCES')")
	@RequestMapping(value = ControllerConstants.DELETE_SERVER_CHECK, method = RequestMethod.POST)
	@ResponseBody public  String deleteServerCheck(
			@RequestParam(value="serverId",required=true) String serverId){
		AjaxResponse ajaxResponse ;
		logger.debug("Inside deleteServerCheck method");
		 int iserverId=0;
		 if(!serverId.isEmpty()){
		 iserverId =Integer.parseInt(serverId);
		 }
		ResponseObject responseObject = serverService.deleteServerCheck(iserverId);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
	   
		return ajaxResponse.toString();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "getGroupIdList", method = RequestMethod.GET)
	@ResponseBody 
	public List<String> getGtoupIdList(@RequestParam String tagName) {
		List<String> list=(List<String>) serverService.getListOfGroupIds().getObject();
		return list;
	}
}