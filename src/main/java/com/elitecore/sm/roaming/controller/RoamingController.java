package com.elitecore.sm.roaming.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
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
import org.springframework.web.servlet.ModelAndView;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.JqGridData;
import com.elitecore.sm.common.util.ResponseCode;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.common.util.SpringApplicationContext;
import com.elitecore.sm.pathlist.model.RoamingFileSequenceMgmt;
import com.elitecore.sm.roaming.model.EnableDisableEnum;
import com.elitecore.sm.roaming.model.FileManagement;
import com.elitecore.sm.roaming.model.FileManagementData;
import com.elitecore.sm.roaming.model.FileSequenceEnum;
import com.elitecore.sm.roaming.model.FileSequenceManagement;
import com.elitecore.sm.roaming.model.HostConfiguration;
import com.elitecore.sm.roaming.model.Partner;
import com.elitecore.sm.roaming.model.RoamingParameter;
import com.elitecore.sm.roaming.model.RoamingServicesENUM;
import com.elitecore.sm.roaming.model.TestSimManagement;
import com.elitecore.sm.roaming.model.TestSimManagementData;
import com.elitecore.sm.roaming.model.TimeZoneEnum;
import com.elitecore.sm.roaming.service.PartnerService;
import com.elitecore.sm.roaming.service.RoamingConfigurationService;
import com.elitecore.sm.roaming.validator.RoamingValidator;
import com.elitecore.sm.util.DateFormatter;

@Controller
public class RoamingController extends BaseController {

	@Autowired
	private RoamingValidator validator;
	@Autowired
	private RoamingConfigurationService roamingService;
	@Autowired
	private PartnerService partnerService;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
	}

	@PreAuthorize("hasAnyAuthority('ROAMING_CONFIGURATION_MENU_VIEW')")
	@RequestMapping(value = ControllerConstants.INIT_ROAMING_CONFIGURATION)//NOSONAR
	public ModelAndView initRoamingConfiguration(
			@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = false) String requestActionType,
			HttpServletRequest request, @RequestParam(value = "partner", required = false) String partner,
			@RequestParam(value = "lob", required = false) String lob)
					throws SMException {
		ModelAndView model = new ModelAndView(ViewNameConstants.ROAMING_CONFIGURATION);
		if (requestActionType != null) {
			if (requestActionType.equals(BaseConstants.HOST_CONFIGURATION)) {
				ResponseObject roamingConfigurationDetails = roamingService
						.loadRoamingConfigurationDetails(requestActionType);
				HostConfiguration hostConfiguration = (HostConfiguration) roamingConfigurationDetails.getObject();
				model.addObject("timeZoneEnum", java.util.Arrays.asList(TimeZoneEnum.values()));
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
				model.addObject(FormBeanConstants.HOST_CONFIGURATION_FORM_BEAN, hostConfiguration);
			} else if (requestActionType.equals(BaseConstants.ROAMING_PARAMETER)) {
				ResponseObject roamingConfigurationDetails = roamingService
						.loadRoamingConfigurationDetails(requestActionType);
				RoamingParameter roamingParameter = (RoamingParameter) roamingConfigurationDetails.getObject();
				model.addObject("enalbeDisableEnum", java.util.Arrays.asList(EnableDisableEnum.values()));
				model.addObject(FormBeanConstants.ROAMING_PARAMETER_FORM_BEAN, roamingParameter);
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
			} else if (requestActionType.equals(BaseConstants.RAP_TAP_CONFIGURATION)) {
				FileManagement fileManagement = new FileManagement();
				
				Cookie[] cookies = request.getCookies();
				String partnerName = "";
				String lobValue = "";
				String name = "";
				String primaryTadigValue = "";
				String secondaryTadigValue = "";
				if(cookies.length > 0){
					for (Cookie cookie : cookies) {
						name = cookie.getName();
						if(name.equals("partnerName")){
							partnerName = cookie.getValue();
						}else if(name.equals("lob")){
							lobValue = cookie.getValue();
						}else if(name.equals("primaryTadig")){
							primaryTadigValue = cookie.getValue();
						}else if(name.equals("secondaryTadig")){
							secondaryTadigValue = cookie.getValue();
						}
					}
					List<Partner> loadPartnerByNameandLob = partnerService.loadPartnerByNameandLob(partnerName,
							lobValue);
					if(loadPartnerByNameandLob.size()>0){
						List<FileManagementData> loadFileManagementData = roamingService
								.getFileManagementData(loadPartnerByNameandLob.get(0).getId());
					if (loadFileManagementData.size() > 0) {
						for (FileManagementData fileManagementData : loadFileManagementData) {
							if (fileManagementData.getServiceType().equals(SystemParametersConstant.TEST_SERVICE)) {
								fileManagement.setTestTapInVersion(fileManagementData.getTapInVersion());
								fileManagement.setTestTapOutVersion(fileManagementData.getTapOutVersion());
								fileManagement.setTestMaxRecordsInTapOut(fileManagementData.getMaxRecordsInTapOut());
								fileManagement.setTestMaxFileSizeOfTapOut(fileManagementData.getMaxFileSizeOfTapOut());
								fileManagement.setTestSendTapNotification(fileManagementData.getSendTapNotification());
								fileManagement.setTestRegeneratedTapOutFileSequence(
										fileManagementData.getRegeneratedTapOutFileSequence());
								fileManagement.setTestFileValidation(fileManagementData.getFileValidation());
								fileManagement.setTestRapInVersion(fileManagementData.getRapInVersion());
								fileManagement.setTestRapOutVersion(fileManagementData.getRapOutVersion());
								fileManagement.setTestSendNrtrde(fileManagementData.getSendNrtrde());
								fileManagement.setTestNrtrdeInVersion(fileManagementData.getNrtrdeInVersion());
								fileManagement.setTestNrtrdeOutVersion(fileManagementData.getNrtrdeOutVersion());
								fileManagement.setTestMaxRecordsInNrtrdeOut(fileManagementData.getMaxRecordsInNrtrdeOut());
								fileManagement.setTestMaxfileSizeOfnrtrdeOut(fileManagementData.getMaxfileSizeOfnrtrdeOut());
							} else {
								fileManagement.setCommercialTapInVersion(fileManagementData.getTapInVersion());
								fileManagement.setCommercialTapOutVersion(fileManagementData.getTapOutVersion());
								fileManagement.setCommercialMaxRecordsInTapOut(fileManagementData.getMaxRecordsInTapOut());
								fileManagement.setCommercialMaxFileSizeOfTapOut(fileManagementData.getMaxFileSizeOfTapOut());
								fileManagement.setCommercialSendTapNotification(fileManagementData.getSendTapNotification());
								fileManagement.setCommercialRegeneratedTapOutFileSequence(
										fileManagementData.getRegeneratedTapOutFileSequence());
								fileManagement.setCommercialFileValidation(fileManagementData.getFileValidation());
								fileManagement.setCommercialRapInVersion(fileManagementData.getRapInVersion());
								fileManagement.setCommercialRapOutVersion(fileManagementData.getRapOutVersion());
								fileManagement.setCommercialSendNrtrde(fileManagementData.getSendNrtrde());
								fileManagement.setCommercialNrtrdeInVersion(fileManagementData.getNrtrdeInVersion());
								fileManagement.setCommercialNrtrdeOutVersion(fileManagementData.getNrtrdeOutVersion());
								fileManagement
										.setCommercialMaxRecordsInNrtrdeOut(fileManagementData.getMaxRecordsInNrtrdeOut());
								fileManagement
										.setCommercialMaxfileSizeOfnrtrdeOut(fileManagementData.getMaxfileSizeOfnrtrdeOut());

							}
						}
						model.addObject(FormBeanConstants.FILE_MANAGEMENT_FORM_BEAN, fileManagement);
						model.addObject("partner", partnerName);
						model.addObject("lob", lobValue);
						model.addObject("primaryTadig", primaryTadigValue);
						model.addObject("secondaryTadig", secondaryTadigValue);
					}else{
						model.addObject(FormBeanConstants.FILE_MANAGEMENT_FORM_BEAN, new FileManagement());
						model.addObject("partner", fileManagement.getPartnerName());
						model.addObject("lob", fileManagement.getLob());
						model.addObject("primaryTadig", primaryTadigValue);
						model.addObject("secondaryTadig", secondaryTadigValue);
					}
					}
					else{
						model.addObject(FormBeanConstants.FILE_MANAGEMENT_FORM_BEAN, new FileManagement());
						
					}
					
					
				}else{
					model.addObject(FormBeanConstants.FILE_MANAGEMENT_FORM_BEAN, new FileManagement());
				}
		 
				model.addObject("enalbeDisableEnum", java.util.Arrays.asList(EnableDisableEnum.values()));
				model.addObject("fileSequenceEnum", java.util.Arrays.asList(FileSequenceEnum.values()));
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
			} else if (requestActionType.equals(BaseConstants.TEST_SIM_MANAGEMENT)) {

				TestSimManagementData testSimManagementdata = new TestSimManagementData();
				Cookie[] cookies = request.getCookies();
				String partnerName = "";
				String lobValue = "";
				String name = "";
				String primaryTadigValue = "";
				String secondaryTadigValue = "";
				if(cookies.length > 0){
					for (Cookie cookie : cookies) {
						name = cookie.getName();
						if(name.equals("partnerName")){
							partnerName = cookie.getValue();
						}else if(name.equals("lob")){
							lobValue = cookie.getValue();
						}else if(name.equals("primaryTadig")){
							primaryTadigValue = cookie.getValue();
						}else if(name.equals("secondaryTadig")){
							secondaryTadigValue = cookie.getValue();
						}
					}
					List<Partner> loadPartnerByNameandLob = partnerService.loadPartnerByNameandLob(partnerName,
							lobValue);
					if(loadPartnerByNameandLob.size()>0){
					List<TestSimManagement> loadTestSimData = roamingService
							.getTestSimManagement(loadPartnerByNameandLob.get(0).getId());
					if (loadTestSimData.size() > 0) {
						for (TestSimManagement testSimManagement : loadTestSimData) {
							if (testSimManagement.getType().equals(SystemParametersConstant.INBOUND)) {
								testSimManagementdata.setInBoundPmnCode(testSimManagement.getPmnCode());
								testSimManagementdata.setInBoundImsi(testSimManagement.getImsi());
								testSimManagementdata.setInBoundmsisdn(testSimManagement.getMsisdn());
								testSimManagementdata.setInBoundToDate(testSimManagement.getToDate());
								testSimManagementdata.setInBoundFromDate(testSimManagement.getFromDate());
								testSimManagementdata.setInBoundServices(testSimManagement.getServices());
							} else {
								testSimManagementdata.setOutBoundPmnCode(testSimManagement.getPmnCode());
								testSimManagementdata.setOutBoundImsi(testSimManagement.getImsi());
								testSimManagementdata.setOutBoundmsisdn(testSimManagement.getMsisdn());
								testSimManagementdata.setOutBoundToDate(testSimManagement.getToDate());
								testSimManagementdata.setOutBoundFromDate(testSimManagement.getFromDate());
								testSimManagementdata.setOutBoundServices(testSimManagement.getServices());
							}

						}
						model.addObject(FormBeanConstants.TEST_SIM_MANAGEMNT_DATA, testSimManagementdata);
						model.addObject("partner", partnerName);
						model.addObject("lob", lobValue);
						model.addObject("primaryTadig", primaryTadigValue);
						model.addObject("secondaryTadig", secondaryTadigValue);
					}else{
						model.addObject(FormBeanConstants.TEST_SIM_MANAGEMNT_DATA, new TestSimManagementData());
						model.addObject("partner", partnerName);
						model.addObject("lob", lobValue);
						model.addObject("primaryTadig", primaryTadigValue);
						model.addObject("secondaryTadig", secondaryTadigValue);
					}
					}
					else{
						model.addObject(FormBeanConstants.TEST_SIM_MANAGEMNT_DATA, new TestSimManagementData());
					}
					
					
				}else{
					model.addObject(FormBeanConstants.TEST_SIM_MANAGEMNT_DATA, new TestSimManagementData());
				}
				model.addObject("servicesEnum", java.util.Arrays.asList(RoamingServicesENUM.values()));
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);

			} else if (requestActionType.equals(BaseConstants.FILE_SEQUENCE_MANAGEMENT)) {
				FileSequenceManagement filesequence = new FileSequenceManagement();
				Cookie[] cookies = request.getCookies();
				String partnerName = "";
				String lobValue = "";
				String name = "";
				String primaryTadigValue = "";
				String secondaryTadigValue = "";
				if(cookies.length > 0){
					for (Cookie cookie : cookies) {
						name = cookie.getName();
						if(name.equals("partnerName")){
							partnerName = cookie.getValue();
						}else if(name.equals("lob")){
							lobValue = cookie.getValue();
						}else if(name.equals("primaryTadig")){
							primaryTadigValue = cookie.getValue();
						}else if(name.equals("secondaryTadig")){
							secondaryTadigValue = cookie.getValue();
						}
					}
					List<Partner> loadPartnerByNameandLob = partnerService.loadPartnerByNameandLob(partnerName,
							lobValue);
					if(loadPartnerByNameandLob.size()>0){
						List<RoamingFileSequenceMgmt> fileSequenceDetails = roamingService
								.getFileSequenceDetails(loadPartnerByNameandLob.get(0).getId());
					if (fileSequenceDetails.size() > 0) {
						for (RoamingFileSequenceMgmt missingFileSequenceMgmt : fileSequenceDetails) {
							int maxValue = missingFileSequenceMgmt.getMaxValue();
							if(missingFileSequenceMgmt.getElementType().equalsIgnoreCase(SystemParametersConstant.TEST_RAP_IN)
									&& missingFileSequenceMgmt.getFileType().equalsIgnoreCase(SystemParametersConstant.RAP_IN))
								filesequence.setTestRapIn(maxValue);
							else if(missingFileSequenceMgmt.getElementType().equalsIgnoreCase(SystemParametersConstant.TEST_RAP_OUT)
									&& missingFileSequenceMgmt.getFileType().equalsIgnoreCase(SystemParametersConstant.RAP_OUT))
								filesequence.setTestRapOut(maxValue);
							else if(missingFileSequenceMgmt.getElementType().equalsIgnoreCase(SystemParametersConstant.TEST_TAP_IN)
									&& missingFileSequenceMgmt.getFileType().equalsIgnoreCase(SystemParametersConstant.TAP_IN))
								filesequence.setTestTapIn(maxValue);
							else if(missingFileSequenceMgmt.getElementType().equalsIgnoreCase(SystemParametersConstant.TEST_TAP_OUT)
									&& missingFileSequenceMgmt.getFileType().equalsIgnoreCase(SystemParametersConstant.TAP_OUT))
								filesequence.setTestTapOut(maxValue);
							else if(missingFileSequenceMgmt.getElementType().equalsIgnoreCase(SystemParametersConstant.COMMERICAL_RAP_IN)
									&& missingFileSequenceMgmt.getFileType().equalsIgnoreCase(SystemParametersConstant.RAP_IN))
								filesequence.setCommercialRapIn(maxValue);
							else if(missingFileSequenceMgmt.getElementType().equalsIgnoreCase(SystemParametersConstant.COMMERICAL_RAP_OUT)
									&& missingFileSequenceMgmt.getFileType().equalsIgnoreCase(SystemParametersConstant.RAP_OUT))
								filesequence.setCommercialRapOut(maxValue);
							else if(missingFileSequenceMgmt.getElementType().equalsIgnoreCase(SystemParametersConstant.COMMERICAL_TAP_IN)
									&& missingFileSequenceMgmt.getFileType().equalsIgnoreCase(SystemParametersConstant.TAP_IN))
								filesequence.setCommercialTapIn(maxValue);
							else if(missingFileSequenceMgmt.getElementType().equalsIgnoreCase(SystemParametersConstant.COMMERICAL_TAP_OUT)
									&& missingFileSequenceMgmt.getFileType().equalsIgnoreCase(SystemParametersConstant.TAP_OUT))
								filesequence.setCommercialTapOut(maxValue);
							else if(missingFileSequenceMgmt.getElementType().equalsIgnoreCase(SystemParametersConstant.COMMERICAL_NRTRDE_IN)
									&& missingFileSequenceMgmt.getFileType().equalsIgnoreCase(SystemParametersConstant.NRTRDE_IN))
								filesequence.setNrtrdeIn(maxValue);
							else if(missingFileSequenceMgmt.getElementType().equalsIgnoreCase(SystemParametersConstant.COMMERICAL_NRTRDE_OUT)
									&& missingFileSequenceMgmt.getFileType().equalsIgnoreCase(SystemParametersConstant.NRTRDE_OUT))
								filesequence.setNrtrdeOut(maxValue);
							
						}
						model.addObject(FormBeanConstants.FILE_SEQUENCE_MANAGEMENT, filesequence);
						model.addObject("partner", partnerName);
						model.addObject("lob", lobValue);
						model.addObject("primaryTadig", primaryTadigValue);
						model.addObject("secondaryTadig", secondaryTadigValue);
					}else{
						model.addObject(FormBeanConstants.FILE_SEQUENCE_MANAGEMENT, new FileSequenceManagement());
						model.addObject("partner", partnerName);
						model.addObject("lob", lobValue);
						model.addObject("primaryTadig", primaryTadigValue);
						model.addObject("secondaryTadig", secondaryTadigValue);
					}
					}
					else{
						model.addObject(FormBeanConstants.FILE_SEQUENCE_MANAGEMENT, new FileSequenceManagement());
					}	
				}
				}else{
					model.addObject(FormBeanConstants.FILE_SEQUENCE_MANAGEMENT, new FileSequenceManagement());
				}
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
			

		} else {
			ResponseObject roamingConfigurationDetails = roamingService
					.loadRoamingConfigurationDetails(BaseConstants.ROAMING_PARAMETER);
			RoamingParameter roamingParameter = (RoamingParameter) roamingConfigurationDetails.getObject();
			//model.addObject("enalbeDisableEnum", java.util.Arrays.asList(EnableDisableEnum.values()));
			model.addObject(FormBeanConstants.ROAMING_PARAMETER_FORM_BEAN, roamingParameter);
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.ROAMING_PARAMETER);
		}
		return model;
	}

	@RequestMapping(value = ControllerConstants.MODIFY_HOST_CONFIGURATION, method = RequestMethod.POST)
	public ModelAndView editHostConfiguration(
			@ModelAttribute(FormBeanConstants.HOST_CONFIGURATION_FORM_BEAN) HostConfiguration hostConfiguration,//NOSONAR
			HttpServletRequest request, BindingResult result) throws SMException {
		ModelAndView model = new ModelAndView();
		validator.validateHostConfigurationDetails(hostConfiguration, result, null, false);
		if (result.hasErrors()) {
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.HOST_CONFIGURATION);
		} else {
			hostConfiguration.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			hostConfiguration.setLastUpdatedDate(new Date());
			ResponseObject responseObject = roamingService.saveRoamingConfigurationDetails(hostConfiguration);
			if (responseObject != null && responseObject.isSuccess()) {
				model.addObject(BaseConstants.RESPONSE_MSG, getMessage("host.configuration.update.success"));
			}
		}
		model.addObject("timeZoneEnum", java.util.Arrays.asList(TimeZoneEnum.values()));
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.HOST_CONFIGURATION);
		model.addObject(FormBeanConstants.HOST_CONFIGURATION_FORM_BEAN, hostConfiguration);
		model.setViewName(ViewNameConstants.ROAMING_CONFIGURATION);
		return model;
	}

	@RequestMapping(value = ControllerConstants.MODIFY_ROAMING_PARAMETER, method = RequestMethod.POST)
	public ModelAndView editRoamingParameter(
			@ModelAttribute(FormBeanConstants.ROAMING_PARAMETER_FORM_BEAN) RoamingParameter roamingParameter,//NOSONAR
			HttpServletRequest request, BindingResult result) throws SMException {
		ModelAndView model = new ModelAndView();
		validator.validateRoamingParameterDetails(roamingParameter, result, null, false);
		if (result.hasErrors()) {
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.ROAMING_PARAMETER);
		} else {
			roamingParameter.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			roamingParameter.setLastUpdatedDate(new Date());
			ResponseObject responseObject = roamingService.saveRoamingConfigurationDetails(roamingParameter);
			if (responseObject != null && responseObject.isSuccess()) {
				model.addObject(BaseConstants.RESPONSE_MSG, getMessage("roaming.configuration.update.success"));
			}
		}
		//model.addObject("enalbeDisableEnum", java.util.Arrays.asList(EnableDisableEnum.values()));
		model.setViewName(ViewNameConstants.ROAMING_CONFIGURATION);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.ROAMING_PARAMETER);
		model.addObject(FormBeanConstants.ROAMING_PARAMETER_FORM_BEAN, roamingParameter);
		return model;
	}

	@RequestMapping(value = ControllerConstants.MODIFY_FILE_SEQUENCE_MANAGEMENT, method = RequestMethod.POST)
	public ModelAndView editFileSequenceManagement(
			@ModelAttribute(FormBeanConstants.FILE_SEQUENCE_MANAGEMENT) FileSequenceManagement fileSequence,
			HttpServletRequest request, BindingResult result, HttpServletResponse response) throws SMException {
		ModelAndView model = new ModelAndView();
		Cookie partnerCookie = new Cookie("partnerName",fileSequence.getPartnerName());
		 response.addCookie(partnerCookie);
		 Cookie lobCookie = new Cookie("lob",fileSequence.getLob());
		 response.addCookie(lobCookie);
		 Cookie primaryTadigCookie = new Cookie("primaryTadig",fileSequence.getPrimaryTadig());
		 response.addCookie(primaryTadigCookie);
		 Cookie secondaryTadigCookie = new Cookie("secondaryTadig",fileSequence.getSecondaryTadig());
		 response.addCookie(secondaryTadigCookie);
		 
		 
		 
		validator.validateFileSequenceFileds(fileSequence, result, null, false);
		if (result.hasErrors()) {
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.FILE_SEQUENCE_MANAGEMENT);
			model.addObject("partner", fileSequence.getPartnerName());
			model.addObject("lob", fileSequence.getLob());
			model.addObject("primaryTadig", fileSequence.getPrimaryTadig());
			model.addObject("secondaryTadig", fileSequence.getSecondaryTadig());
		} else {
			fileSequence.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			fileSequence.setLastUpdatedDate(new Date());

			String partnerName = fileSequence.getPartnerName();
			String lob = fileSequence.getLob();
			if (partnerName == null || partnerName.trim().equals("")) {
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.FILE_SEQUENCE_MANAGEMENT);

				model.addObject(BaseConstants.ERROR_MSG, getMessage("please.enter.partner.name"));
			} else {
				model.addObject("partner", fileSequence.getPartnerName());
				model.addObject("lob", fileSequence.getLob());
				model.addObject("primaryTadig", fileSequence.getPrimaryTadig());
				model.addObject("secondaryTadig", fileSequence.getSecondaryTadig());
				List<Partner> loadPartnerByNameandLob = partnerService.loadPartnerByNameandLob(partnerName, lob);
				if (loadPartnerByNameandLob != null && loadPartnerByNameandLob.size() > 0) {

					Partner partnerByNameandLob = loadPartnerByNameandLob.get(0);

					RoamingFileSequenceMgmt mgmt1 = new RoamingFileSequenceMgmt(SystemParametersConstant.TEST_RAP_IN,SystemParametersConstant.RAP_IN,
							fileSequence.getTestRapIn(),  fileSequence.getLastUpdatedDate(),
							fileSequence.getLastUpdatedByStaffId(), partnerByNameandLob);
					RoamingFileSequenceMgmt mgmt2 = new RoamingFileSequenceMgmt(SystemParametersConstant.TEST_RAP_OUT,SystemParametersConstant.RAP_OUT,
							fileSequence.getTestRapOut(),  fileSequence.getLastUpdatedDate(),
							fileSequence.getLastUpdatedByStaffId(),partnerByNameandLob);
					RoamingFileSequenceMgmt mgmt3 = new RoamingFileSequenceMgmt(SystemParametersConstant.TEST_TAP_IN,SystemParametersConstant.TAP_IN,
							fileSequence.getTestTapIn(),  fileSequence.getLastUpdatedDate(),
							fileSequence.getLastUpdatedByStaffId(),partnerByNameandLob);
					RoamingFileSequenceMgmt mgmt4 = new RoamingFileSequenceMgmt(SystemParametersConstant.TEST_TAP_OUT,SystemParametersConstant.TAP_OUT,
							fileSequence.getTestTapOut(),  fileSequence.getLastUpdatedDate(),
							fileSequence.getLastUpdatedByStaffId(),  partnerByNameandLob);
					RoamingFileSequenceMgmt mgmt5 = new RoamingFileSequenceMgmt(SystemParametersConstant.COMMERICAL_NRTRDE_IN,SystemParametersConstant.NRTRDE_IN,
							fileSequence.getNrtrdeIn(),  fileSequence.getLastUpdatedDate(),
							fileSequence.getLastUpdatedByStaffId(), partnerByNameandLob);
					RoamingFileSequenceMgmt mgmt6 = new RoamingFileSequenceMgmt(
							SystemParametersConstant.COMMERICAL_RAP_IN, SystemParametersConstant.RAP_IN,fileSequence.getCommercialRapIn(), 
							fileSequence.getLastUpdatedDate(), fileSequence.getLastUpdatedByStaffId(), 
							partnerByNameandLob);
					RoamingFileSequenceMgmt mgmt7 = new RoamingFileSequenceMgmt(
							SystemParametersConstant.COMMERICAL_RAP_OUT, SystemParametersConstant.RAP_OUT, fileSequence.getCommercialRapOut(), 
							fileSequence.getLastUpdatedDate(), fileSequence.getLastUpdatedByStaffId(), 
							partnerByNameandLob);
					RoamingFileSequenceMgmt mgmt8 = new RoamingFileSequenceMgmt(
							SystemParametersConstant.COMMERICAL_TAP_IN, SystemParametersConstant.TAP_IN,fileSequence.getCommercialTapIn(), 
							fileSequence.getLastUpdatedDate(), fileSequence.getLastUpdatedByStaffId(), 
							partnerByNameandLob);
					RoamingFileSequenceMgmt mgmt9 = new RoamingFileSequenceMgmt(
							SystemParametersConstant.COMMERICAL_TAP_OUT, SystemParametersConstant.TAP_OUT,fileSequence.getCommercialTapOut(), 
							fileSequence.getLastUpdatedDate(), fileSequence.getLastUpdatedByStaffId(), 
							partnerByNameandLob);
					RoamingFileSequenceMgmt mgmt10 = new RoamingFileSequenceMgmt(SystemParametersConstant.COMMERICAL_NRTRDE_OUT,SystemParametersConstant.NRTRDE_OUT,
							fileSequence.getNrtrdeOut(),  fileSequence.getLastUpdatedDate(),
							fileSequence.getLastUpdatedByStaffId(), partnerByNameandLob);

					ArrayList<RoamingFileSequenceMgmt> arrayList = new ArrayList<>();
					arrayList.add(mgmt1);
					arrayList.add(mgmt2);
					arrayList.add(mgmt3);
					arrayList.add(mgmt4);
					arrayList.add(mgmt5);
					arrayList.add(mgmt6);
					arrayList.add(mgmt7);
					arrayList.add(mgmt8);
					arrayList.add(mgmt9);
					arrayList.add(mgmt10);
					ResponseObject responseObject = saveFileSequenceDetails(arrayList);
					if (responseObject != null && responseObject.isSuccess()) {
						model.addObject(BaseConstants.RESPONSE_MSG,
								getMessage("file.sequence.management.update.success"));

					}
				} else {
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.FILE_SEQUENCE_MANAGEMENT);
					model.addObject(BaseConstants.ERROR_MSG, getMessage("invalid.partner.name"));
				}
			}
		}
		model.setViewName(ViewNameConstants.ROAMING_CONFIGURATION);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.FILE_SEQUENCE_MANAGEMENT);
		model.addObject(FormBeanConstants.FILE_SEQUENCE_MANAGEMENT, fileSequence);

		return model;
	}

	@RequestMapping(value = ControllerConstants.VIEW_FILE_SEQUENCE_MANAGEMENT, method = RequestMethod.POST)
	@ResponseBody
	public String viewFileSequenceData(@RequestParam(value = "partner", required = true) String partner,
			@RequestParam(value = "lob", required = true) String lob) throws SMException {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject = new ResponseObject();
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		List<Partner> loadPartnerByNameandLob = partnerService.loadPartnerByNameandLob(partner, lob);

		if (loadPartnerByNameandLob != null && loadPartnerByNameandLob.size() > 0) {
			Partner partnerByNameandLob = loadPartnerByNameandLob.get(0);

			List<RoamingFileSequenceMgmt> fileSequenceDetails = roamingService
					.getFileSequenceDetails(partnerByNameandLob.getId());
			for (RoamingFileSequenceMgmt missingRoamingFileSequenceMgmt : fileSequenceDetails) {
				JSONObject fieldObject = new JSONObject();
				fieldObject.put(missingRoamingFileSequenceMgmt.getElementType()+missingRoamingFileSequenceMgmt.getFileType(), missingRoamingFileSequenceMgmt.getMaxValue());
				jsonArray.put(fieldObject);
			}
		}
		jsonObject.put("fileSequenceList", jsonArray);
		responseObject.setSuccess(true);
		responseObject.setObject(jsonObject);

		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);

		return ajaxResponse.toString();
	}

	@RequestMapping(value = ControllerConstants.MODIFY_TEST_SIM_MANAGEMENT, method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView modifyTestSimManagement(
			@ModelAttribute(FormBeanConstants.TEST_SIM_MANAGEMNT_DATA) TestSimManagementData testSimManagement,
			HttpServletRequest request, HttpServletResponse response, BindingResult result) throws SMException {

		ModelAndView model = new ModelAndView();
		ResponseObject responseObject = new ResponseObject();
		 Cookie ck = new Cookie("partnerName",testSimManagement.getPartnerName());
		 response.addCookie(ck);
		 Cookie lobCookie = new Cookie("lob",testSimManagement.getLob());
		 response.addCookie(lobCookie);
		 Cookie primaryTadigCookie = new Cookie("primaryTadig",testSimManagement.getPrimaryTadig());
		 response.addCookie(primaryTadigCookie);
		 Cookie secondaryTadigCookie = new Cookie("secondaryTadig",testSimManagement.getSecondaryTadig());
		 response.addCookie(secondaryTadigCookie);
		 
		 
		validator.validateTestSimManagementDetails(testSimManagement, result, null, false);
		if (result.hasErrors()) {
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.TEST_SIM_MANAGEMENT);
			model.addObject("partner", testSimManagement.getPartnerName());
			model.addObject("lob", testSimManagement.getLob());
			model.addObject("primaryTadig", testSimManagement.getPrimaryTadig());
			model.addObject("secondaryTadig", testSimManagement.getSecondaryTadig());
		} else {
			String partnerName = testSimManagement.getPartnerName();
			String lob = testSimManagement.getLob();
			if (partnerName == null || partnerName.trim().equals("")) {
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.TEST_SIM_MANAGEMENT);
				model.addObject(BaseConstants.ERROR_MSG, getMessage("please.enter.partner.name"));
			} else {
				model.addObject("partner", testSimManagement.getPartnerName());
				model.addObject("lob", testSimManagement.getLob());
				model.addObject("primaryTadig", testSimManagement.getPrimaryTadig());
				model.addObject("secondaryTadig", testSimManagement.getSecondaryTadig());
				TestSimManagement inboundtestSimManagement = new TestSimManagement();
				TestSimManagement outboundtestSimManagement = new TestSimManagement();
				inboundtestSimManagement.setPmnCode(testSimManagement.getInBoundPmnCode());
				inboundtestSimManagement.setImsi(testSimManagement.getInBoundImsi());
				inboundtestSimManagement.setMsisdn(testSimManagement.getInBoundmsisdn());
				inboundtestSimManagement.setToDate(testSimManagement.getInBoundToDate());
				inboundtestSimManagement.setFromDate(testSimManagement.getInBoundFromDate());
				inboundtestSimManagement.setServices(testSimManagement.getInBoundServices());
				inboundtestSimManagement.setType(SystemParametersConstant.INBOUND);
				outboundtestSimManagement.setPmnCode(testSimManagement.getOutBoundPmnCode());
				outboundtestSimManagement.setImsi(testSimManagement.getOutBoundImsi());
				outboundtestSimManagement.setMsisdn(testSimManagement.getOutBoundmsisdn());
				outboundtestSimManagement.setToDate(testSimManagement.getOutBoundToDate());
				outboundtestSimManagement.setFromDate(testSimManagement.getOutBoundFromDate());
				outboundtestSimManagement.setServices(testSimManagement.getOutBoundServices());
				outboundtestSimManagement.setType(SystemParametersConstant.OUTBOUND);
				List<Partner> loadPartnerByNameandLob = partnerService.loadPartnerByNameandLob(partnerName, lob);
				if (loadPartnerByNameandLob.size() > 0) {
					Partner partner = loadPartnerByNameandLob.get(0);
					outboundtestSimManagement.setPartner(partner);
					inboundtestSimManagement.setPartner(partner);
					outboundtestSimManagement.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
					outboundtestSimManagement.setLastUpdatedDate(new Date());
					inboundtestSimManagement.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
					inboundtestSimManagement.setLastUpdatedDate(new Date());
					ArrayList<TestSimManagement> inboundOutBoundList = new ArrayList<>();
					inboundOutBoundList.add(inboundtestSimManagement);
					inboundOutBoundList.add(outboundtestSimManagement);
					responseObject = saveOrUpdateTestSimManagement(inboundOutBoundList);
					model.addObject("servicesEnum", java.util.Arrays.asList(RoamingServicesENUM.values()));
					if (responseObject != null && responseObject.isSuccess()) {
						model.addObject(BaseConstants.RESPONSE_MSG, getMessage("test.sim.management.update.success"));

					}
					if(responseObject!=null)
						responseObject.setSuccess(true);
					model.addObject(responseObject);
				} else {
					model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.TEST_SIM_MANAGEMENT);
					model.addObject(BaseConstants.ERROR_MSG, getMessage("invalid.partner.name"));

				}
			}

		}
		model.addObject("servicesEnum", java.util.Arrays.asList(RoamingServicesENUM.values()));
		model.setViewName(ViewNameConstants.ROAMING_CONFIGURATION);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.TEST_SIM_MANAGEMENT);
		model.addObject(FormBeanConstants.TEST_SIM_MANAGEMNT_DATA, testSimManagement);

		return model;
	}

	@RequestMapping(value = ControllerConstants.VIEW_TEST_SIM_MANAGEMENT, method = RequestMethod.POST)
	@ResponseBody
	public String viewTestSimData(@RequestParam(value = "partner", required = true) String partner,
			@RequestParam(value = "lob", required = true) String lob) throws SMException {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject = new ResponseObject();
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();

		List<Partner> loadPartnerByNameandLob = partnerService.loadPartnerByNameandLob(partner, lob);

		if (loadPartnerByNameandLob != null && loadPartnerByNameandLob.size() > 0) {
			Partner partnerByNameandLob = loadPartnerByNameandLob.get(0);
			List<TestSimManagement> testSimManagement = roamingService
					.getTestSimManagement(partnerByNameandLob.getId());

			for (TestSimManagement inboundOutboundData : testSimManagement) {
				JSONObject fieldObject = new JSONObject();
				if (inboundOutboundData.getType().equalsIgnoreCase(SystemParametersConstant.INBOUND)) {
					fieldObject.put(SystemParametersConstant.INBOUND_PMNCODE, inboundOutboundData.getPmnCode());
					fieldObject.put(SystemParametersConstant.INBOUND_IMSI, inboundOutboundData.getImsi());
					fieldObject.put(SystemParametersConstant.INBOUND_MSISDN, inboundOutboundData.getMsisdn());
					fieldObject.put(SystemParametersConstant.INBOUND_FROMDATE,
							new SimpleDateFormat("MM/dd/yyyy").format(inboundOutboundData.getFromDate()));
					fieldObject.put(SystemParametersConstant.INBOUND_TODATE,
							new SimpleDateFormat("MM/dd/yyyy").format(inboundOutboundData.getToDate()));
					fieldObject.put(SystemParametersConstant.INBOUND_SERVICES, inboundOutboundData.getServices());
				} else {
					fieldObject.put(SystemParametersConstant.OUTBOUND_PMNCODE, inboundOutboundData.getPmnCode());
					fieldObject.put(SystemParametersConstant.OUTBOUND_IMSI, inboundOutboundData.getImsi());
					fieldObject.put(SystemParametersConstant.OUTBOUND_MSISDN, inboundOutboundData.getMsisdn());
					fieldObject.put(SystemParametersConstant.OUTBOUND_FROMDATE,
							new SimpleDateFormat("MM/dd/yyyy").format(inboundOutboundData.getFromDate()));
					fieldObject.put(SystemParametersConstant.OUTBOUND_SERVICES, inboundOutboundData.getServices());
					fieldObject.put(SystemParametersConstant.OUTBOUND_TODATE,
							new SimpleDateFormat("MM/dd/yyyy").format(inboundOutboundData.getToDate()));
				}
				jsonArray.put(fieldObject);
			}
		}
		jsonObject.put("testsimList", jsonArray);
		responseObject.setSuccess(true);
		responseObject.setObject(jsonObject);

		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);

		return ajaxResponse.toString();
	}

	@RequestMapping(value = ControllerConstants.MODIFY_FILE_MANAGEMENT, method = RequestMethod.POST)
	@ResponseBody
	public ModelAndView saveOrUpdateFileManagement(
			@ModelAttribute(FormBeanConstants.FILE_MANAGEMENT_FORM_BEAN) FileManagement fileManagement,
			HttpServletRequest request, BindingResult result,HttpServletResponse response) throws SMException {
		ModelAndView model = new ModelAndView();
		ResponseObject responseObject = new ResponseObject();
		Cookie partnerCookie = new Cookie("partnerName",fileManagement.getPartnerName());
		 response.addCookie(partnerCookie);
		 Cookie lobCookie = new Cookie("lob",fileManagement.getLob());
		 response.addCookie(lobCookie);
		 Cookie primaryTadigCookie = new Cookie("primaryTadig",fileManagement.getPrimaryTadig());
		 response.addCookie(primaryTadigCookie);
		 Cookie secondaryTadigCookie = new Cookie("secondaryTadig",fileManagement.getSecondaryTadig());
		 response.addCookie(secondaryTadigCookie);
		validator.validateFileManagement(fileManagement, result, null, false);
		if (result.hasErrors()) {
			model.addObject("partner", fileManagement.getPartnerName());
			model.addObject("lob", fileManagement.getLob());
			model.addObject("primaryTadig", fileManagement.getPrimaryTadig());
			model.addObject("secondaryTadig", fileManagement.getSecondaryTadig());

		} else {

			String partnerName = fileManagement.getPartnerName();
			String lob = fileManagement.getLob();
			if (partnerName == null || partnerName.trim().equals("")) {
				model.addObject(BaseConstants.ERROR_MSG, getMessage("please.enter.partner.name"));
			} else {
				List<Partner> loadPartnerByNameandLob = partnerService.loadPartnerByNameandLob(partnerName, lob);
				if (loadPartnerByNameandLob.size() > 0) {
					model.addObject("partner", fileManagement.getPartnerName());
					model.addObject("lob", fileManagement.getLob());
					model.addObject("primaryTadig", fileManagement.getPrimaryTadig());
					model.addObject("secondaryTadig", fileManagement.getSecondaryTadig());
					
					Partner partner = loadPartnerByNameandLob.get(0);
					FileManagementData testFileManagementData = new FileManagementData();
					FileManagementData commercialFileManagementdata = new FileManagementData();
					testFileManagementData.setTapInVersion(fileManagement.getTestTapInVersion());
					testFileManagementData.setTapOutVersion(fileManagement.getTestTapOutVersion());
					testFileManagementData.setMaxRecordsInTapOut(fileManagement.getTestMaxRecordsInTapOut());
					testFileManagementData.setMaxFileSizeOfTapOut(fileManagement.getTestMaxFileSizeOfTapOut());
					testFileManagementData.setSendTapNotification(fileManagement.getTestSendTapNotification());
					testFileManagementData
							.setRegeneratedTapOutFileSequence(fileManagement.getTestRegeneratedTapOutFileSequence());
					testFileManagementData.setFileValidation(fileManagement.getTestFileValidation());
					testFileManagementData.setRapInVersion(fileManagement.getTestRapInVersion());
					testFileManagementData.setRapOutVersion(fileManagement.getTestRapOutVersion());
					testFileManagementData.setSendNrtrde(fileManagement.getTestSendNrtrde());
					testFileManagementData.setNrtrdeInVersion(fileManagement.getTestNrtrdeInVersion());
					testFileManagementData.setNrtrdeOutVersion(fileManagement.getTestNrtrdeOutVersion());
					testFileManagementData.setMaxRecordsInNrtrdeOut(fileManagement.getTestMaxRecordsInNrtrdeOut());
					testFileManagementData.setMaxfileSizeOfnrtrdeOut(fileManagement.getTestMaxfileSizeOfnrtrdeOut());
					testFileManagementData.setPartner(partner);
					testFileManagementData.setServiceType(SystemParametersConstant.TEST_SERVICE);
					testFileManagementData.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
					testFileManagementData.setLastUpdatedDate(new Date());

					commercialFileManagementdata.setTapInVersion(fileManagement.getCommercialTapInVersion());
					commercialFileManagementdata.setTapOutVersion(fileManagement.getCommercialTapOutVersion());
					commercialFileManagementdata
							.setMaxRecordsInTapOut(fileManagement.getCommercialMaxRecordsInTapOut());
					commercialFileManagementdata
							.setMaxFileSizeOfTapOut(fileManagement.getCommercialMaxFileSizeOfTapOut());
					commercialFileManagementdata
							.setSendTapNotification(fileManagement.getCommercialSendTapNotification());
					commercialFileManagementdata.setRegeneratedTapOutFileSequence(
							fileManagement.getCommercialRegeneratedTapOutFileSequence());
					commercialFileManagementdata.setFileValidation(fileManagement.getCommercialFileValidation());
					commercialFileManagementdata.setRapInVersion(fileManagement.getCommercialRapInVersion());
					commercialFileManagementdata.setRapOutVersion(fileManagement.getCommercialRapOutVersion());
					commercialFileManagementdata.setSendNrtrde(fileManagement.getCommercialSendNrtrde());
					commercialFileManagementdata.setNrtrdeInVersion(fileManagement.getCommercialNrtrdeInVersion());
					commercialFileManagementdata.setNrtrdeOutVersion(fileManagement.getCommercialNrtrdeOutVersion());
					commercialFileManagementdata
							.setMaxRecordsInNrtrdeOut(fileManagement.getCommercialMaxRecordsInNrtrdeOut());
					commercialFileManagementdata
							.setMaxfileSizeOfnrtrdeOut(fileManagement.getCommercialMaxfileSizeOfnrtrdeOut());
					commercialFileManagementdata.setPartner(partner);
					commercialFileManagementdata.setServiceType(SystemParametersConstant.COMMERCIAL_SERVICE);
					commercialFileManagementdata.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
					commercialFileManagementdata.setLastUpdatedDate(new Date());

					ArrayList<FileManagementData> fileManagementList = new ArrayList<>();
					fileManagementList.add(testFileManagementData);
					fileManagementList.add(commercialFileManagementdata);
					responseObject = saveOrUpdateFileManagement(fileManagementList);
					if (responseObject != null & responseObject.isSuccess()) {//NOSONAR
						model.addObject(BaseConstants.RESPONSE_MSG, getMessage("file.management.update.success"));

					}
					responseObject.setSuccess(true);
					model.addObject(responseObject);

				} else {
					model.addObject(BaseConstants.ERROR_MSG, getMessage("invalid.partner.name"));
				}

			}

		}
		model.addObject("enalbeDisableEnum", java.util.Arrays.asList(EnableDisableEnum.values()));
		model.addObject("fileSequenceEnum", java.util.Arrays.asList(FileSequenceEnum.values()));
		model.setViewName(ViewNameConstants.ROAMING_CONFIGURATION);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, BaseConstants.RAP_TAP_CONFIGURATION);
		model.addObject(FormBeanConstants.FILE_MANAGEMENT_FORM_BEAN, fileManagement);
		return model;

	}

	@RequestMapping(value = ControllerConstants.VIEW_FILE_MANAGEMENT, method = RequestMethod.POST)
	@ResponseBody
	public String viewFileManagement(@RequestParam(value = "partner", required = true) String partner,
			@RequestParam(value = "lob", required = true) String lob) throws SMException {
		AjaxResponse ajaxResponse = new AjaxResponse();
		ResponseObject responseObject = new ResponseObject();
		JSONObject jsonObject = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		List<Partner> loadPartnerByNameandLob = partnerService.loadPartnerByNameandLob(partner, lob);

		if (loadPartnerByNameandLob.size() > 0) {
			Partner partnerDetails = loadPartnerByNameandLob.get(0);
			List<FileManagementData> fileManagementDataFromDb = roamingService
					.getFileManagementData(partnerDetails.getId());
			for (FileManagementData fileManagementData : fileManagementDataFromDb) {
				JSONObject fieldObject = new JSONObject();
				if (fileManagementData.getServiceType().equalsIgnoreCase(SystemParametersConstant.TEST_SERVICE)) {
					fieldObject.put(SystemParametersConstant.TEST_TAPIN_VERSION, fileManagementData.getTapInVersion());
					fieldObject.put(SystemParametersConstant.TEST_TAPOUT_VERSION,
							fileManagementData.getTapOutVersion());
					fieldObject.put(SystemParametersConstant.TEST_MAX_RECORDS_TAPOUT,
							fileManagementData.getMaxRecordsInTapOut());
					fieldObject.put(SystemParametersConstant.TEST_MAXFILE_TAPOUT,
							fileManagementData.getMaxFileSizeOfTapOut());
					fieldObject.put(SystemParametersConstant.TEST_SEND_NOTIFICATION,
							fileManagementData.getSendTapNotification());
					fieldObject.put(SystemParametersConstant.TEST_GENERAT_FILESEQUENCE,
							fileManagementData.getRegeneratedTapOutFileSequence());
					fieldObject.put(SystemParametersConstant.TEST_FILE_VALIDATION,
							fileManagementData.getFileValidation());
					fieldObject.put(SystemParametersConstant.TEST_RAPIN_VERSION, fileManagementData.getRapInVersion());
					fieldObject.put(SystemParametersConstant.TEST_RAPOUT_VERSION,
							fileManagementData.getRapOutVersion());
					fieldObject.put(SystemParametersConstant.TEST_NRTRDE_IN_VERSION,
							fileManagementData.getNrtrdeInVersion());
					fieldObject.put(SystemParametersConstant.TEST_NRTRDE_OUT_VERSION,
							fileManagementData.getNrtrdeOutVersion());
					fieldObject.put(SystemParametersConstant.TEST_MAX_RECORD_NRTRDE,
							fileManagementData.getMaxRecordsInNrtrdeOut());
					fieldObject.put(SystemParametersConstant.TEST_MAX_FILESIZE_NRTRDE,
							fileManagementData.getMaxfileSizeOfnrtrdeOut());
				} else {
					fieldObject.put(SystemParametersConstant.COMMERCIAL_TAPIN_VERSION,
							fileManagementData.getTapInVersion());
					fieldObject.put(SystemParametersConstant.COMMERCIAL_TAPOUT_VERSION,
							fileManagementData.getTapOutVersion());
					fieldObject.put(SystemParametersConstant.COMMERCIAL_MAX_RECORDS_TAPOUT,
							fileManagementData.getMaxRecordsInTapOut());
					fieldObject.put(SystemParametersConstant.COMMERCIAL_MAX_FILESIZE_TAPOUT,
							fileManagementData.getMaxFileSizeOfTapOut());
					fieldObject.put(SystemParametersConstant.COMMERCIAL_TAP_NOTIFICATION,
							fileManagementData.getSendTapNotification());
					fieldObject.put(SystemParametersConstant.COMMERCIAL_GENERATE_FILESEQUENCE,
							fileManagementData.getRegeneratedTapOutFileSequence());
					fieldObject.put(SystemParametersConstant.COMMERCIAL_FILE_VALIDATION,
							fileManagementData.getFileValidation());
					fieldObject.put(SystemParametersConstant.COMMERCIAL_RAPIN_VERSION,
							fileManagementData.getRapInVersion());
					fieldObject.put(SystemParametersConstant.COMMERCIAL_RAPOUT_VERSION,
							fileManagementData.getRapOutVersion());
					fieldObject.put(SystemParametersConstant.COMMERCIAL_NRTRDE_IN_VERSION,
							fileManagementData.getNrtrdeInVersion());
					fieldObject.put(SystemParametersConstant.COMMERCIAL_NRTRDE_OUT_VERSION,
							fileManagementData.getNrtrdeOutVersion());
					fieldObject.put(SystemParametersConstant.COMMERCIAL_MAX_RECORDS,
							fileManagementData.getMaxRecordsInNrtrdeOut());
					fieldObject.put(SystemParametersConstant.COMMERCIAL_MAX_FILESIZE_NRTRDE,
							fileManagementData.getMaxfileSizeOfnrtrdeOut());

				}
				jsonArray.put(fieldObject);
			}
		}
		jsonObject.put("fileManagement", jsonArray);
		responseObject.setSuccess(true);
		responseObject.setObject(jsonObject);

		ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();

	}
	
	@RequestMapping(value = ControllerConstants.GET_PARTNER_LIST_BY_NAME, method = RequestMethod.POST)
	@ResponseBody
	public String selectPartnerDetails(@RequestParam(value = "rows", defaultValue = "10") int limit,
			@RequestParam(value = "page", defaultValue = "1") int currentPage,
			@RequestParam(value = "sidx", required = true) String sidx, 
			@RequestParam(value = "sord", required = true) String sord,
			@RequestParam(value = "partner",required = true) String partnerName,
			Partner partner){//NOSONAR
		long count = partnerService.getTotalPartnerCountByName(partner,partnerName);
		logger.debug("partner count found " + count);
		List<Partner> resultList = new ArrayList<>(); 
		if (count > 0){
			resultList = this.partnerService.getPaginatedListForPartner(partner,partnerName,eliteUtils.getStartIndex(limit, currentPage, eliteUtils.getTotalPagesCount(count, limit)), limit, sidx, sord);
		}
		Map<String, Object> row ;
		List<Map<String, Object>> rowList = new ArrayList<>();
		
		if (resultList != null) {
			for (Partner partnerList : resultList) {
				
				row = new HashMap<>();
				
				row.put("id",partnerList.getId());
				row.put("partner",partnerList.getName());
				row.put("lob",partnerList.getLob());
				row.put("primaryTadig",partnerList.getPrimaryTadig());
				row.put("secondaryTadig",partnerList.getSecondaryTadig());
				rowList.add(row);
			}
		}

		logger.debug("<< getDeviceListByDecodeType in DeviceController "); 
		return new JqGridData<Map<String, Object>>(eliteUtils.getTotalPagesCount(count, limit), currentPage, (int) count, rowList).getJsonString();
	}
	public ResponseObject saveFileSequenceDetails(ArrayList<RoamingFileSequenceMgmt> arrayList) {
		ResponseObject responseObject = new ResponseObject();
		
		RoamingFileSequenceMgmt missingFileSequenceMgmt2 = arrayList.get(0);
		Partner partner = missingFileSequenceMgmt2.getPartner();
		int id = partner.getId();
		List<RoamingFileSequenceMgmt> fileSequenceList = roamingService.getFileSequenceDetails(id);
		if(fileSequenceList.size()>0){
			for (int j = 0; j < arrayList.size(); j++) {
				int maxValue = arrayList.get(j).getMaxValue();
				String type = arrayList.get(j).getElementType()+arrayList.get(j).getFileType();
				for(int k = 0;k < fileSequenceList.size();k++){
					RoamingFileSequenceMgmt missingFileSequenceMgmt = fileSequenceList.get(k);
					if (type.equalsIgnoreCase(missingFileSequenceMgmt.getElementType() + missingFileSequenceMgmt.getFileType())) {
						if (missingFileSequenceMgmt.getMaxValue() != maxValue) {
							missingFileSequenceMgmt.setMaxValue(maxValue);
							missingFileSequenceMgmt.setLastUpdatedDate(new Date());
							roamingService.saveFileSequence(missingFileSequenceMgmt);
						}
						break;
					}
				}
			}
			responseObject.setResponseCode(ResponseCode.FILE_SEQUENCE_MANAGEMENT_INSERT_SUCCESS);
		}else{
			for (RoamingFileSequenceMgmt missingFileSequenceMgmt : arrayList) {
				roamingService.saveFileSequence(missingFileSequenceMgmt);
			}
			responseObject.setSuccess(true);
			responseObject.setResponseCode(ResponseCode.FILE_SEQUENCE_MANAGEMENT_UPDATE_SUCCESS);
		}
responseObject.setSuccess(true);
		
		return responseObject;
	}
	
	public ResponseObject saveOrUpdateTestSimManagement(ArrayList<TestSimManagement> inboundOutboundList) {
		ResponseObject responseObject = new ResponseObject();
		for (TestSimManagement testSimManagement : inboundOutboundList) {
			RoamingConfigurationService roamingConfigurationService = (RoamingConfigurationService) SpringApplicationContext.getBean("roamingConfigurationService");
			List<TestSimManagement> inboundOutboundData = roamingService.getTestSimManagement(testSimManagement.getPartner().getId(),testSimManagement.getType());
			if(inboundOutboundData.size()>0){
				TestSimManagement testSimManagementDb= inboundOutboundData.get(0);
				testSimManagementDb.setPmnCode(testSimManagement.getPmnCode());
				testSimManagementDb.setImsi(testSimManagement.getImsi());
				testSimManagementDb.setMsisdn(testSimManagement.getMsisdn());
				testSimManagementDb.setFromDate(testSimManagement.getFromDate());
				testSimManagementDb.setToDate(testSimManagement.getToDate());
				testSimManagementDb.setServices(testSimManagement.getServices());
				testSimManagementDb.setLastUpdatedByStaffId(testSimManagement.getLastUpdatedByStaffId());
				testSimManagementDb.setLastUpdatedDate(new Date());
				
				roamingConfigurationService.saveOrUpdateTestSimManagementDetails(testSimManagementDb);
			}
			else {
				roamingConfigurationService.saveOrUpdateTestSimManagementDetails(testSimManagement);
			}
		}
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.TEST_SIM_MANAGEMENT);
		
		return responseObject;
	}	
	
	public ResponseObject saveOrUpdateFileManagement(ArrayList<FileManagementData> fileManagementList) {
		ResponseObject responseObject = new ResponseObject();
		RoamingConfigurationService roamingConfigurationService = (RoamingConfigurationService) SpringApplicationContext.getBean("roamingConfigurationService");
		for (FileManagementData fileManagementData : fileManagementList) {
			List<FileManagementData> testOrCommercialFileManagement = roamingService.getFileManagementData(fileManagementData.getPartner().getId(),fileManagementData.getServiceType());
			if(testOrCommercialFileManagement.size()>0){
				FileManagementData fileManagementUpdateData = testOrCommercialFileManagement.get(0);
				fileManagementUpdateData.setTapInVersion(fileManagementData.getTapInVersion());
				fileManagementUpdateData.setTapOutVersion(fileManagementData.getTapOutVersion());
				fileManagementUpdateData.setMaxRecordsInTapOut(fileManagementData.getMaxRecordsInTapOut());
				fileManagementUpdateData.setMaxFileSizeOfTapOut(fileManagementData.getMaxFileSizeOfTapOut());
				fileManagementUpdateData.setSendTapNotification(fileManagementData.getSendTapNotification());
				fileManagementUpdateData.setRegeneratedTapOutFileSequence(fileManagementData.getRegeneratedTapOutFileSequence());
				fileManagementUpdateData.setFileValidation(fileManagementData.getFileValidation());
				fileManagementUpdateData.setRapInVersion(fileManagementData.getRapInVersion());
				fileManagementUpdateData.setRapOutVersion(fileManagementData.getRapOutVersion());
				fileManagementUpdateData.setSendNrtrde(fileManagementData.getSendNrtrde());
				fileManagementUpdateData.setNrtrdeInVersion(fileManagementData.getNrtrdeInVersion());
				fileManagementUpdateData.setNrtrdeOutVersion(fileManagementData.getNrtrdeOutVersion());
				fileManagementUpdateData.setMaxRecordsInNrtrdeOut(fileManagementData.getMaxRecordsInNrtrdeOut());
				fileManagementUpdateData.setMaxfileSizeOfnrtrdeOut(fileManagementData.getMaxfileSizeOfnrtrdeOut());
				fileManagementUpdateData.setLastUpdatedByStaffId(fileManagementData.getLastUpdatedByStaffId());
				fileManagementUpdateData.setLastUpdatedDate(new Date());
				roamingConfigurationService.saveOrUpdateFileManagementDetails(fileManagementUpdateData);
			}else{
				roamingConfigurationService.saveOrUpdateFileManagementDetails(fileManagementData);
			}
			responseObject.setObject(fileManagementData);;
		}
		
		responseObject.setSuccess(true);
		responseObject.setResponseCode(ResponseCode.FILE_MANAGEMENT);
		
		return responseObject;
	}	
}
