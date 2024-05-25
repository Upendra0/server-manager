package com.elitecore.sm.netflowclient.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.netflowclient.model.NatFlowProxyClient;
import com.elitecore.sm.netflowclient.service.ProxyClientConfigurationService;
import com.elitecore.sm.netflowclient.validator.NetflowClientValidator;
import com.elitecore.sm.util.EliteUtils;

@Controller
public class ProxyClientConfigurationController extends BaseController{
	
	@Autowired
	ProxyClientConfigurationService proxyClientConfigurationService;
	
	@Autowired
	NetflowClientValidator netFlowClientValidator;
	
	@Autowired
	@Qualifier(value="eliteUtilsQualifier")
	protected EliteUtils eliteUtils ;
	
	@PreAuthorize("hasAnyAuthority('VIEW_COMPOSER')")
	@RequestMapping(value = ControllerConstants.NATFLOW_PROXY_CLIENT_LIST_BY_SERVICEID, method = RequestMethod.POST)
	@ResponseBody public  String getNatFlowClientListByServiceId(
				@RequestParam(value = "serviceId",required = true) int serviceId) {
		//logger.debug(">> getCharRenameOperationById in CharRenameController " + id); 
		ResponseObject responseObject  = this.proxyClientConfigurationService.getAllProxyClientByServiceId(serviceId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	@RequestMapping(value = ControllerConstants.ADD_PROXY_CLIENT, method = RequestMethod.POST)
	@ResponseBody public String addProxyClientParams(@RequestParam(value="blockCount") String blockCount,
			@ModelAttribute(value=FormBeanConstants.NAT_FLOW_CLIENT_CONFIG_FORM_BEAN) NatFlowProxyClient natFlowProxyClient,//NOSONAR
			BindingResult result,
			HttpServletRequest request){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		netFlowClientValidator.validateProxyClient(natFlowProxyClient, null, result, null, null, false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(blockCount+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(blockCount+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);

		}else{
			natFlowProxyClient.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			natFlowProxyClient.setCreatedDate(new Date());
			
			ResponseObject responseObject = this.proxyClientConfigurationService.addProxyClientParams(natFlowProxyClient);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
	}
	
	@RequestMapping(value = ControllerConstants.UPDATE_PROXY_CLIENT, method = RequestMethod.POST)
	public @ResponseBody String updateProxyParams(
			@RequestParam(value="blockCount") int blockCount,
		    @ModelAttribute(value=FormBeanConstants.NETFLOW_COLLECTION_CLIENT_FORM_BEAN) NatFlowProxyClient natFlowProxyClient,//NOSONAR
			BindingResult result,
			HttpServletRequest request){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		netFlowClientValidator.validateProxyClient(natFlowProxyClient, null, result, null, null, false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(blockCount+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(blockCount+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);

		}else{
			natFlowProxyClient.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			ResponseObject responseObject=this.proxyClientConfigurationService.updateProxyClient(natFlowProxyClient);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
	}
	
	@RequestMapping(value = ControllerConstants.DELETE_PROXY_CLIENT, method = RequestMethod.POST)
	public @ResponseBody String deleteProxyClient(
						@RequestParam(value = "id",required=true) int clientId ){
		ResponseObject responseObject = this.proxyClientConfigurationService.deleteProxyClient(clientId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
}
