package com.elitecore.sm.policy.controller;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.policy.model.Policy;
import com.elitecore.sm.policy.model.PolicyGroupRel;
import com.elitecore.sm.policy.model.SearchPolicy;
import com.elitecore.sm.policy.service.IPolicyImportExportService;
import com.elitecore.sm.policy.service.IPolicyService;
import com.elitecore.sm.policy.validator.PolicyValidator;
import com.elitecore.sm.serverinstance.model.ServerInstance;
import com.elitecore.sm.util.DateFormatter;

/**
 * Policy Controller class
 * 
 * @author chintan.patel
 *
 */
@Controller
public class PolicyController extends BaseController {
	
	@Autowired
	IPolicyService policyService;

	@Autowired
	PolicyValidator policyValidator;
	
	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	IPolicyImportExportService policyImportExportService;
	
	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));

	}

	@PreAuthorize("hasAnyAuthority('VIEW_POLICY_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.GET_POLICY_LIST, method = RequestMethod.GET)
	@ResponseBody
	public String getPolicyList(
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx" , required=false) String sidx,
			@RequestParam(value = "sord" , required=false) String sord,
			@RequestParam(value = BaseConstants.POLICY_NAME, required = false) String searchPolicyName,
			@RequestParam(value = BaseConstants.DESCRIPTION, required = false) String searchPolicyDescription,
			@RequestParam(value = BaseConstants.ASSOCIATION_STATUS, required = false) String searchAssociationStatus,
			@RequestParam(value = BaseConstants.SERVER_INSTANCES_ID) int serverInstanceId) {

		SearchPolicy searchPolicy = new SearchPolicy(searchPolicyName, 
													 searchPolicyDescription, 
													 searchAssociationStatus, 
													 null, 
													 serverInstanceId,null,null,null);
		
		long count =  policyService.getTotalPolicyCount(searchPolicy);
		
		List<Policy> resultList = new ArrayList<>();
		if(count > 0){
			resultList = policyService.getPaginatedList(searchPolicy, 
														eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)),
														limit, sidx,sord);
		}
		
		Map<String, Object> row ;
		List<Map<String, Object>> rowList = new ArrayList<>();
		
		if (resultList != null) {
			for (Policy policy : resultList) {
				
				row = new HashMap<>();
				
				row.put(BaseConstants.ID, policy.getId());
				row.put(BaseConstants.POLICY_NAME, policy.getName());
				row.put(BaseConstants.DESCRIPTION, policy.getDescription());
				StringBuilder sb = new StringBuilder();
				for(PolicyGroupRel policyGroupRel : policy.getPolicyGroupRelSet()) {
					sb.append(policyGroupRel.getGroup().getName());
					sb.append(", ");
				}
				if(sb.indexOf(",") != -1) {
					sb.delete(sb.lastIndexOf(","), sb.length());
				}
				row.put(BaseConstants.GROUPS, sb.toString());
				row.put(BaseConstants.ASSOCIATION_STATUS, policy.getAssociationStatus());
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage,
													(int) count, rowList).getJsonString();
	}

	@PreAuthorize("hasAnyAuthority('CREATE_POLICY_CONFIGURATION')")
	@RequestMapping(value=ControllerConstants.INIT_CREATE_POLICY, method=RequestMethod.POST)
	public ModelAndView initCreatePolicy(
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_NAME_PARAM , required=false) String instanceName,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_HOST_PARAM , required=false) String host,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_PORT_PARAM , required=false) String port,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_ID_PARAM, required=false) String instanceId,
			@RequestParam(value=BaseConstants.POLICY_ID_PARAM , required=false) String policyId,
			@RequestParam(value=BaseConstants.POLICY_NAME_PARAM , required=false) String policyName,
			@RequestParam(value=BaseConstants.POLICY_DESCRIPTION_PARAM , required=false) String policyDescription,
			@RequestParam(value=BaseConstants.SERVICE_ID , required=false) String serviceId,
			@RequestParam(value=BaseConstants.SERVICE_TYPE , required=false) String serviceType,
			@RequestParam(value=BaseConstants.SERVICE_INST_ID , required=false) String serviceInstanceId,
			@RequestParam(value=BaseConstants.SERVICE_NAME , required=false) String serviceName){

		ModelAndView model = new ModelAndView(ViewNameConstants.CREATE_POLICY);
		model.addObject(BaseConstants.INSTANCE_NAME, instanceName);
		model.addObject(BaseConstants.HOST, host);
		model.addObject(BaseConstants.PORT, port);
		model.addObject(BaseConstants.INSTANCE_ID, instanceId);
		model.addObject(BaseConstants.POLICY_ID, policyId);
		model.addObject(BaseConstants.POLICY_NAME, policyName);
		model.addObject(BaseConstants.POLICY_DESCRIPTION, policyDescription);
		model.addObject(BaseConstants.SERVICE_ID, serviceId);
		model.addObject(BaseConstants.SERVICE_INST_ID,serviceInstanceId);
		model.addObject(BaseConstants.SERVICE_TYPE, serviceType);
		model.addObject(BaseConstants.SERVICE_NAME, serviceName);
		int id = 0;
		try {
			id = Integer.parseInt(policyId);
		} catch (NumberFormatException e) {
			// Invalid / Null policy id
		}
		Policy policy = new Policy();
		if (id != 0) {
			policy = policyService.getPolicyById(id);
		}
		model.addObject(FormBeanConstants.POLICY_FORM_BEAN, policy);
		return model;

	}

	@PreAuthorize("hasAnyAuthority('CREATE_POLICY_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.CREATE_POLICY, method = RequestMethod.POST)
	public ModelAndView createPolicy(
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_NAME_PARAM , required=false) String instanceName,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_HOST_PARAM , required=false) String host,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_PORT_PARAM , required=false) String port,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_ID_PARAM, required=false) int instanceId,
			@RequestParam(value=BaseConstants.SERVICE_ID , required=false) String serviceId,
			@RequestParam(value=BaseConstants.SERVICE_TYPE , required=false) String serviceType,
			@RequestParam(value=BaseConstants.SERVICE_INST_ID , required=false) String serviceInstanceId,
			@RequestParam(value=BaseConstants.SERVICE_NAME , required=false) String serviceName,
			@RequestParam(value = "policy-groups" , required=false) String policyGroups,
			@ModelAttribute(value = FormBeanConstants.POLICY_FORM_BEAN) Policy policy,//NOSONAR
			HttpServletRequest request,
			BindingResult result) {
		ModelAndView model = new ModelAndView(ViewNameConstants.BUSINESS_POLICY_MANAGEMENT);
		policyValidator.validatePolicyParameters(policy, result, null, null, false);
		
		if(result.hasErrors()) {
			model = new ModelAndView(ViewNameConstants.CREATE_POLICY);
			model.addObject(FormBeanConstants.POLICY_FORM_BEAN, policy);
		} else {
			policy.setAlias(policy.getName());
			if(policy.getServer() == null) {
				policy.setServer(new ServerInstance());
			}
			policy.getServer().setId(instanceId);
			if(policy.getDescription() == null) {
				policy.setDescription(StringUtils.EMPTY);
			}
			ResponseObject responseObject = policyService.savePolicy(policy, policyGroups, eliteUtils.getLoggedInStaffId(request), instanceId);
			if(!responseObject.isSuccess()) {
				model.setViewName(ViewNameConstants.CREATE_POLICY);
			}
			model.addObject(BaseConstants.RESPONSE_MSG, getMessage(responseObject.getResponseCode().toString()));
		}
		model.addObject(BaseConstants.INSTANCE_NAME, instanceName);
		model.addObject(BaseConstants.HOST, host);
		model.addObject(BaseConstants.PORT, port);
		model.addObject(BaseConstants.INSTANCE_ID, instanceId);
		model.addObject(BaseConstants.SERVICE_ID, serviceId);
		model.addObject(BaseConstants.SERVICE_INST_ID,serviceInstanceId);
		model.addObject(BaseConstants.SERVICE_TYPE, serviceType);
		model.addObject(BaseConstants.SERVICE_NAME, serviceName);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.POLICY);
		return model;
	}

	@PreAuthorize("hasAnyAuthority('UPDATE_POLICY_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.UPDATE_POLICY, method = RequestMethod.POST)
	public ModelAndView updatePolicy(
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_NAME_PARAM , required=false) String instanceName,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_HOST_PARAM , required=false) String host,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_PORT_PARAM , required=false) String port,
			@RequestParam(value=BaseConstants.SERVER_INSTANCE_ID_PARAM, required=false) int instanceId,
			@RequestParam(value=BaseConstants.SERVICE_ID , required=false) String serviceId,
			@RequestParam(value=BaseConstants.SERVICE_TYPE , required=false) String serviceType,
			@RequestParam(value=BaseConstants.SERVICE_INST_ID , required=false) String serviceInstanceId,
			@RequestParam(value=BaseConstants.SERVICE_NAME , required=false) String serviceName,
			@RequestParam(value = "policy-groups" , required=false) String policyGroups,
			@ModelAttribute(value = FormBeanConstants.POLICY_FORM_BEAN) Policy policy,//NOSONAR
			HttpServletRequest request,
			BindingResult result) {
		
		ModelAndView model;
		policyValidator.validatePolicyParameters(policy, result, null, null, false);
		
		if(result.hasErrors()) {
			model = new ModelAndView(ViewNameConstants.CREATE_POLICY);
			model.addObject(FormBeanConstants.POLICY_FORM_BEAN, policy);
		} else {
			model = new ModelAndView(ViewNameConstants.BUSINESS_POLICY_MANAGEMENT);
			policy.setAlias(policy.getName());
			if(policy.getServer() == null) {
				policy.setServer(new ServerInstance());
			}
			policy.getServer().setId(instanceId);
			ResponseObject responseObject = policyService.updatePolicy(policy, policyGroups, eliteUtils.getLoggedInStaffId(request), instanceId);
			model.addObject(BaseConstants.RESPONSE_MSG, getMessage(responseObject.getResponseCode().toString()));
		}
		
		model.addObject(BaseConstants.INSTANCE_NAME, instanceName);
		model.addObject(BaseConstants.HOST, host);
		model.addObject(BaseConstants.PORT, port);
		model.addObject(BaseConstants.INSTANCE_ID, instanceId);
		model.addObject(BaseConstants.SERVICE_ID, serviceId);
		model.addObject(BaseConstants.SERVICE_INST_ID,serviceInstanceId);
		model.addObject(BaseConstants.SERVICE_TYPE, serviceType);
		model.addObject(BaseConstants.SERVICE_NAME, serviceName);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.POLICY);

		return model;
	}

	@PreAuthorize("hasAnyAuthority('VIEW_POLICY_CONFIGURATION')")
	@RequestMapping(value = ControllerConstants.GET_POLICY_GROUP_LIST, method = RequestMethod.GET)
	@ResponseBody
	public String getPolicyGroupList(@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx" , required=false) String sidx,
			@RequestParam(value = "sord" , required=false) String sord,
			@RequestParam("policyId") int policyId) {
		
		long count =  policyService.getTotalPolicyGroupCount(policyId);
		List<PolicyGroupRel> resultList = new ArrayList<>();
		if(count > 0){
			resultList = policyService.getPolicyGroupPaginatedListByPolicy(policyId, eliteUtils.getStartIndex(limit, currentPage, 
					eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord);
		}
		
		Map<String, Object> row ;
		List<Map<String, Object>> rowList = new ArrayList<>();
		
		if (resultList != null) {
			for (PolicyGroupRel policyGroupRel : resultList) {
				
				row = new HashMap<>();
				
				row.put(BaseConstants.ID, policyGroupRel.getGroup().getId());
				row.put(BaseConstants.NAME, policyGroupRel.getGroup().getName());
				row.put(BaseConstants.DESCRIPTION, policyGroupRel.getGroup().getDescription());
				row.put(BaseConstants.APPLICATION_ORDER, policyGroupRel.getApplicationOrder());
				row.put("relId", policyGroupRel.getId());
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage,
													(int) count, rowList).getJsonString();
	}
	
	@RequestMapping(value = ControllerConstants.REMOVE_POLICY, method = RequestMethod.POST)
	@ResponseBody
	public String removePolicy(@RequestParam("policyId") int policyId) {
		AjaxResponse ajaxResponse;
		
		ResponseObject responseObject = policyService.removePolicy(policyId);
		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		
		return ajaxResponse.toString();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = ControllerConstants.EXPORT_POLICY_CONFIG, method = RequestMethod.POST)
	public ModelAndView exportPolicyConfig(@RequestParam(value = "exportPolicyId", required = true) String strPolicyId,
							@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = true) String requestActionType, 
							@RequestParam(value = "isExportForDelete",required = true) boolean isExportForDelete, 
							@RequestParam(value="exportPath" , required=false) String exportFilePath,
							HttpServletResponse response) throws SMException{
		
		ModelAndView model = new ModelAndView();
		File exportXml = null;
		boolean isSuccess = false;
		int policyId = 0;
		
		if(!StringUtils.isEmpty(strPolicyId)){
			policyId = Integer.parseInt(strPolicyId);		
		}
		
		if (StringUtils.isEmpty(exportFilePath)) {
			logger.debug("Call Simple Export Policy functionality");
			String tempPathForExport = servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT);
			ResponseObject responseObject = policyImportExportService.getPolicyFullHierarchy(policyId,isExportForDelete,tempPathForExport);
			if (responseObject.isSuccess() && responseObject.getObject() != null) {
				Map<String, Object> serverInstanceJAXB = (Map<String, Object>) responseObject.getObject();
				exportXml = (File) serverInstanceJAXB.get(BaseConstants.POLICY_EXPORT_FILE);
				isSuccess = true;
			}
		}else{
			logger.debug("Call Download Export File in Delete Policy");
			exportXml=new File(exportFilePath);
			isSuccess=true;
		}
		
		if(isSuccess){
			downloadExportedPolicyfile(exportXml,response);
		}else {
			model.addObject(BaseConstants.ERROR_MSG, getMessage("serverMgmt.export.config.fail"));
		}
		
		model.setViewName(ViewNameConstants.BUSINESS_POLICY_MANAGEMENT);
		//model.setViewName(ViewNameConstants.SERVER_MANAGER);
		
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
		return model;
	}
	
	@RequestMapping(value = ControllerConstants.IMPORT_POLICY_CONFIG, method = RequestMethod.POST)
	@ResponseBody 
	public String importPolicyConfig(@RequestParam(value = "importInstanceId", required = true) String strServerInstanceId,
			@RequestParam(value = "importPolicyId", required = true) String strPolicyId,
			@RequestParam(value = "configFile", required = true) MultipartFile file,
			@RequestParam(value = "importMode", required = true) int importMode,
			HttpServletRequest request) throws SMException {
		
		AjaxResponse ajaxResponse = new AjaxResponse();
		
		if(strPolicyId != null && strServerInstanceId != null){
			int importPolicyId = Integer.parseInt(strPolicyId);
			int importServerInstanceId = Integer.parseInt(strServerInstanceId);
			
			try {
				if (!file.isEmpty()) {
					// check for content type
					if (BaseConstants.IMPORT_FILE_CONTENT_TYPE.equals(file.getContentType())) {
						logger.debug("Valid file found for import , process import functionality");
						String tempPath = servletContext.getRealPath(BaseConstants.TEMP_PATH_FOR_EXPORT)+File.separator;
						File importFile = new File(tempPath+file.getOriginalFilename());
						
						file.transferTo(importFile);
							
						String jaxbXmlPath = servletContext.getRealPath(BaseConstants.JAXB_XML_PATH);
						
						ResponseObject responseObject = policyImportExportService.importPolicyConfig(importServerInstanceId, importPolicyId, importFile, eliteUtils.getLoggedInStaffId(request), importMode, jaxbXmlPath, false);
						
						if(responseObject != null && responseObject.getResponseCode() == ResponseCode.SERVERiNSTANCE_UNMARSHAL_FAIL) {
							ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
							ajaxResponse.setResponseMsg(getMessage("policy.import.failed",new Object[]{""})+" : "+responseObject.getObject());
							
						} else {
							if(responseObject != null) {
								responseObject.setArgs(new Object[]{""});
								ajaxResponse=eliteUtils.convertToAjaxResponse(responseObject);
							}
						}
					} else {
						logger.debug("InValid file found for import");
						ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
						ajaxResponse.setResponseMsg(getMessage("policy.import.wrong.file.select",new Object[]{""}));
					}
				} else {
					logger.debug("Blank file found for import");
					ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
					ajaxResponse.setResponseMsg(getMessage("policy.import.no.file.select"));
				}
			} catch (Exception e) {
				logger.error("Exception Occured in Import policy config:"+e);
				throw new  SMException(e.getMessage());
			} 
		} else {
			logger.debug("Serverinstance id or policy id not availabal in request");
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);
			ajaxResponse.setResponseMsg(getMessage("policy.import.failed",new Object[]{""}));
		}
		return ajaxResponse.toString();

	}
	
	/**
	 * write Exported file in http response
	 * @param exportXml
	 * @param response
	 * @throws SMException
	 */
	public void downloadExportedPolicyfile(File exportXml, HttpServletResponse response) throws SMException{
		try(FileInputStream inputStream = new FileInputStream(exportXml);ServletOutputStream outStream = response.getOutputStream();){
			response.reset();
			response.setContentType(BaseConstants.CONTENT_TYPE_FOR_EXPORT_CONFIG);
			response.setHeader("Content-Disposition", "attachment; filename=\"" +exportXml.getName());
	        byte[] buffer = new byte[(int) exportXml.length()];
	        int bytesRead ;
	        
	        // write bytes read from the input stream into the output stream
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            outStream.write(buffer, 0, bytesRead);
	        }

	        outStream.flush();
		}catch(Exception e){
			logger.error("Exception Occured:"+e);
			throw new SMException(e.getMessage());
		}
	}
	
}
