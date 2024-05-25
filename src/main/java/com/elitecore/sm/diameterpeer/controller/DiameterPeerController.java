package com.elitecore.sm.diameterpeer.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.model.TrueFalseEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.diameterpeer.model.DiameterPeer;
import com.elitecore.sm.diameterpeer.service.DiameterPeerService;
import com.elitecore.sm.diameterpeer.validator.DiameterPeerValidator;
import com.elitecore.sm.services.model.Service;
import com.elitecore.sm.services.service.ServicesService;
import com.elitecore.sm.util.DateFormatter;

@Controller
public class DiameterPeerController extends BaseController {
	
	@Autowired
	@Qualifier(value="diameterPeerService")
	private DiameterPeerService peerService;
	
	@Autowired
	private ServicesService servicesService;
	
	@Autowired
	private DiameterPeerValidator validator;
	
	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.registerCustomEditor(String.class, new StringTrimmerEditor(false));
	}
	
	@PreAuthorize("hasAnyAuthority('VIEW_COLLECTION_CLIENT')")
	@RequestMapping(value = ControllerConstants.GET_DIAMETER_COLLECTION_PEER_LIST, method = RequestMethod.GET)
	public @ResponseBody String getDiameterPeerListSummary(
			@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx") String sidx,
			@RequestParam(value = "sord") String sord,
			@RequestParam(value = BaseConstants.SERVICE_ID,required=true) String serviceId,
			HttpServletRequest request) {
		
		int iserviceId=Integer.parseInt(serviceId);
		long count = this.peerService.getTotalPeerCount(iserviceId);

		logger.info("count: " + count);
		List<DiameterPeer> resultList = new ArrayList<>();
		if (count > 0)
			resultList = this.peerService.getPaginatedList(eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord,iserviceId);

		Map<String, Object> row = null;

		List<Map<String, Object>> rowList = new ArrayList<>();
		count = 0;
		if (resultList != null) {
			logger.info("resultList size: " + resultList.size());
			for (DiameterPeer peer : resultList) {
				row = new HashMap<>();
				
				count++;
				row.put("id",peer.getId());
				row.put("peerName", peer.getName());
				row.put("peerIdentity", peer.getIdentity());
				row.put("fileLoc", peer.getOutFileLocation());
				row.put("status", String.valueOf(peer.getStatus()));
				rowList.add(row);
			}
		}
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}
	
	/**
	 * Return collection peer list for service
	 * @param serviceId
	 * @param request
	 * @return peer list response as model object
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_COLLECTION_CLIENT')")
	@RequestMapping(value = ControllerConstants.GET_DIAMETER_PEER_FOR_SERVICE,method= RequestMethod.POST)
	public ModelAndView getDiameterPeerList(
			@RequestParam(value=BaseConstants.SERVICE_ID,required=true) String serviceId,
			@RequestParam(value = BaseConstants.SERVICE_INST_ID, required=false) String serviceInstanceId,
			HttpServletRequest request) {
		ModelAndView model = new ModelAndView(ViewNameConstants.DIAMETER_COLLECTION_SERVICE_MANAGER);
		
		int iserviceId=Integer.parseInt(serviceId);
		
		ResponseObject responseObject=peerService.getPeerListForService(iserviceId);
		model.addObject("peerList",responseObject.getObject());
		
		Service service = servicesService.getServiceandServerinstance(iserviceId);

		model.addObject("lastUpdateTime",DateFormatter.formatDate(service.getLastUpdatedDate()));
		model.addObject(BaseConstants.SERVICE_ID, serviceId);
		model.addObject(BaseConstants.SERVICE_TYPE, service.getSvctype().getAlias());
		model.addObject(BaseConstants.SERVICE_NAME, service.getName());
		model.addObject("truefalseEnum", java.util.Arrays.asList(TrueFalseEnum.values()));
		model.addObject(BaseConstants.STATE_ENUM, java.util.Arrays.asList(StateEnum.values()));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE,BaseConstants.DIAMETER_COLLECTION_PEER_CONFIGURATION);
		model.addObject(BaseConstants.INSTANCE_ID,service.getServerInstance().getId());
		model.addObject(BaseConstants.SERVICE_INST_ID, serviceInstanceId);
			
		return model;		
	}
	
	@PreAuthorize("hasAnyAuthority('CREATE_COLLECTION_CLIENT')")
	@RequestMapping(value = ControllerConstants.CREATE_DIAMETER_COLLECTION_PEER, method = RequestMethod.POST)
	public @ResponseBody String createDiameterCollectionPeer(
			@RequestParam(value="peerCount") String peerCount,
			@RequestParam(value=BaseConstants.SERVICE_TYPE) String serviceType,
			@ModelAttribute(value=FormBeanConstants.DIAMETER_COLLECTION_PEER_FORM_BEAN) DiameterPeer peer,//NOSONAR
			BindingResult result,
			HttpServletRequest request){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		validator.validateDiameterPeer(peer, serviceType, result, null, null, false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(peerCount+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(peerCount+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);

		}else{
			peer.setCreatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			peer.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			peer.setCreatedDate(new Date());
			ResponseObject responseObject=peerService.addCollectionPeer(peer);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('UPDATE_COLLECTION_CLIENT')")
	@RequestMapping(value = ControllerConstants.UPDATE_DIAMETER_COLLECTION_PEER, method = RequestMethod.POST)
	public @ResponseBody String updateCollectionPeer(
			@RequestParam(value="peerCount") String peerCount,
			@RequestParam(value=BaseConstants.SERVICE_TYPE) String serviceType,
		    @ModelAttribute(value=FormBeanConstants.DIAMETER_COLLECTION_PEER_FORM_BEAN) DiameterPeer peer,//NOSONAR
			BindingResult result,
			HttpServletRequest request){
	
		AjaxResponse ajaxResponse=new AjaxResponse();
		validator.validateDiameterPeer(peer, serviceType, result, null, null, false);
		
		if(result.hasErrors()){
			ajaxResponse.setResponseCode(BaseConstants.AJAX_RESPONSE_FAIL);

			Map<String, String> errorMsgs = new HashMap<>();

			for(FieldError error:result.getFieldErrors()){
				if(error.getCode().equalsIgnoreCase(BaseConstants.TYPEMISMATCH_ERROR)){
					errorMsgs.put(peerCount+"_"+error.getField(),getMessage(error.getCode()+"."+error.getField()));
				}else{
					errorMsgs.put(peerCount+"_"+error.getField(),error.getDefaultMessage());
				}
			}
			ajaxResponse.setObject(errorMsgs);

		}else{
			peer.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			peer.setLastUpdatedDate(new Date());
			ResponseObject responseObject=peerService.updateDiameterPeer(peer);
			ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		}
		
		return ajaxResponse.toString();
	}
	
	@PreAuthorize("hasAnyAuthority('DELETE_COLLECTION_CLIENT')")
	@RequestMapping(value = ControllerConstants.DELETE_DIAMETER_COLLECTION_PEER, method = RequestMethod.POST)
	public @ResponseBody String deleteDiameterPeer(
						@RequestParam(value = "peerId",required=true) String peerId
						){
		
		int iPeerId=Integer.parseInt(peerId);
		ResponseObject responseObject = peerService.deleteDiameterPeer(iPeerId);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		  
		return ajaxResponse.toString();
	}
	
	/**
	 * Update diameter peer status in database
	 * @param peerId
	 * @param serviceId
	 * @return AJAX response Obj
	 */
	@PreAuthorize("hasAnyAuthority('UPDATE_COLLECTION_CLIENT')")
	@RequestMapping(value = ControllerConstants.UPDATE_DIAMETER_PEER_STATUS, method = RequestMethod.POST)
	public @ResponseBody String updateDiameterePeerStatus(
						@RequestParam(value = "peerId",required=true) String peerId,
						@RequestParam(value = "status",required=true) String status
						){
		
		int iPeerId=Integer.parseInt(peerId);
		ResponseObject responseObject = peerService.updatePeerStatus(iPeerId,status);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		  
		return ajaxResponse.toString();
	}
	
}
