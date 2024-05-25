/**
 * 
 */
package com.elitecore.sm.systemparam.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.ControllerConstants;
import com.elitecore.sm.common.constants.FormBeanConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.constants.ViewNameConstants;
import com.elitecore.sm.common.controller.BaseController;
import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.model.SystemBackUpPathOptionEnum;
import com.elitecore.sm.common.util.AjaxResponse;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.iam.service.AccessGroupService;
import com.elitecore.sm.systemparam.model.SystemParameterData;
import com.elitecore.sm.systemparam.model.SystemParameterFormWrapper;
import com.elitecore.sm.systemparam.model.SystemParameterValuePoolData;
import com.elitecore.sm.systemparam.service.SystemParameterService;
import com.elitecore.sm.systemparam.validator.SystemParameterValidator;
import com.elitecore.sm.util.DateFormatter;
import com.elitecore.sm.util.EliteUtils;
import com.elitecore.sm.util.MapCache;

/**
 * @author Vandana Awatramani
 *
 */
@Controller
public class SystemParameterController extends BaseController {

	@Autowired(required = true)
	@Qualifier(value = "systemParameterService")
	private SystemParameterService systemParamService;

	@Autowired
	private SystemParameterValidator validator;
	
	@Autowired
	private AccessGroupService accessGroupService;
	
	private static final String SELECT_ACCESS_GROUP_OPTION = "Select Access Group";

	/**
	 * This function will be invoked to convert the Date to specified format. So
	 * we don't need to convert the date manually.
	 * 
	 * @param binder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Date.class, new CustomDateEditor(DateFormatter.getShortDataFormat(), true));
		binder.setValidator(validator);
	}

	/**
	 * Load All System Parameter form Database
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@PreAuthorize("hasAnyAuthority('VIEW_SYSTEM_PARAMETER')")
	@RequestMapping(value = ControllerConstants.INIT_ALL_SYSTEM_PARAMETER)//NOSONAR
	public ModelAndView initSystemParameter() throws SMException {
		ModelAndView model = new ModelAndView();
		String passowrdPolicy = "";
		boolean isSSOEnabled = false;
		SystemParameterFormWrapper formWrapper = systemParamService.loadAllSystemParameter();
		for (int i = 0; i < formWrapper.getPwdParamList().size(); i++) {
			if (formWrapper.getPwdParamList().get(i).getAlias().equalsIgnoreCase(BaseConstants.STAFF_PD)) {
				passowrdPolicy = systemParamService.parsePasswordType(formWrapper.getPwdParamList().get(i).getValue())[1];
				formWrapper.getPwdParamList().get(i)
						.setValue(systemParamService.parsePasswordType(formWrapper.getPwdParamList().get(i).getValue())[0]);

			}			
		}
		
		isSSOEnabled = isSSOParameterEnabled(formWrapper.getSsoParamList());

		model.addObject(FormBeanConstants.SYSTEM_PARAM_FORM_BEAN, formWrapper);
		model.addObject(BaseConstants.PD_POLICY_DESCRIPTIONDB , passowrdPolicy);
		model.addObject(BaseConstants.REQUEST_ACTION_TYPE, SystemParametersConstant.GENERAL_PARAMETERS);
		model.addObject(SystemParametersConstant.CUSTOMER_LOGO, formWrapper.getSmallLogoPath());
		model.addObject(SystemParametersConstant.CUSTOMER_LOGO_LARGE, formWrapper.getLargeLogoPath());
		model.addObject(SystemParametersConstant.FOOTER_IMAGE, formWrapper.getEmailFooterImagePath());
		model.addObject(SystemParametersConstant.EMAIL_PARAM, formWrapper.getEmailParamList());
		model.addObject(SystemParametersConstant.LOGIN_PARAM, formWrapper.getLdapParamList());
		model.addObject(SystemParametersConstant.SSO_PARAM, formWrapper.getSsoParamList());
		model.addObject(BaseConstants.IS_SSO_ENABLED, isSSOEnabled);
		model.setViewName(ViewNameConstants.MODIFY_SYSTEM_PARAM);

		return model;
	}

	/**
	 * Update General Parameters and Password Parameters
	 * 
	 * @param systemParamFormWrapper
	 * @param result
	 * @param status
	 * @param requestActionType
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('MODIFY_SYSTEM_PARAMETER')")
	@RequestMapping(value = ControllerConstants.EDIT_SYSTEM_PARAMETER, method = RequestMethod.POST)
	public ModelAndView editSytemParam(@ModelAttribute(FormBeanConstants.SYSTEM_PARAM_FORM_BEAN) SystemParameterFormWrapper systemParamFormWrapper,
			BindingResult result, @RequestParam(value = "passwordType", required = false) String ipasswordType, @RequestParam(
					value = "passwordPolicyNameCustom", required = false) String ipwdPolicyDescription, @RequestParam(value = "GpasswordType",
					required = false) String gPasswordType,
			@RequestParam(value = "GpasswordPolicyNameCustom", required = false) String gPwdPolicyDescription, @RequestParam(
					value = BaseConstants.REQUEST_ACTION_TYPE, required = true) String requestActionType, HttpServletRequest request,
			RedirectAttributes redirectAttributes) throws SMException {
			String passwordType=ipasswordType;
			String pwdPolicyDescription=ipwdPolicyDescription;
		if (SystemParametersConstant.EDIT_GEN_SYSTEM_PARAM.equals(requestActionType)) {
			logger.debug("gPasswordType::" + gPasswordType);
			logger.debug("gPwdPolicyDescription::" + gPwdPolicyDescription);
			passwordType = gPasswordType;
			pwdPolicyDescription = gPwdPolicyDescription;
		}

		validator.setRequestActionType(requestActionType);
		validator.validate(systemParamFormWrapper, result, passwordType, pwdPolicyDescription);
		logger.debug("passwordType::" + passwordType);
		ModelAndView model = new ModelAndView();
		logger.debug("pwdPolicyDescription::" + pwdPolicyDescription);
		String passowrdPolicy = "";
		logger.info("***********************************************");
		logger.info("Error object is  :: " + result.getAllErrors());
		logger.info("***********************************************");
		if (result.hasErrors()) {
			setAllParamList(requestActionType, systemParamFormWrapper, model);
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
			redirectAttributes.addFlashAttribute(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
		}
		else {
			if (passwordType != "" && passwordType != null && !("Regular Expression").equalsIgnoreCase(passwordType)) {
				saveSystemParamByGroup(requestActionType, systemParamFormWrapper, model, request, null, null, passwordType,null);
			} else {
				saveSystemParamByGroup(requestActionType, systemParamFormWrapper, model, request, null, null, pwdPolicyDescription,null);
			}
		}
		model.addObject(SystemParametersConstant.CUSTOMER_LOGO, systemParamService.getCustomerLogoPath(SystemParametersConstant.CUSTOMER_LOGO));
		model.addObject(SystemParametersConstant.CUSTOMER_LOGO_LARGE,
				systemParamService.getCustomerLogoPath(SystemParametersConstant.CUSTOMER_LOGO_LARGE));
		for (int i = 0; i < systemParamFormWrapper.getPwdParamList().size(); i++) {
			if (systemParamFormWrapper.getPwdParamList().get(i).getAlias().equalsIgnoreCase(BaseConstants.STAFF_PD)) {
				if (SystemParametersConstant.EDIT_GEN_SYSTEM_PARAM.equals(requestActionType)) {
					systemParamFormWrapper.getPwdParamList().get(i)
							.setValue(systemParamService.parsePasswordType(systemParamFormWrapper.getPwdParamList().get(i).getValue())[0]);
					passowrdPolicy = gPwdPolicyDescription;
				} else {
						String parsePasswordType=systemParamFormWrapper.getPwdParamList().get(i).getValue();
						if(parsePasswordType!=null && !StringUtils.isEmpty(parsePasswordType) && systemParamService.parsePasswordType(parsePasswordType).length >= 2){
							passowrdPolicy = systemParamService.parsePasswordType(systemParamFormWrapper.getPwdParamList().get(i).getValue())[1];
							systemParamFormWrapper.getPwdParamList().get(i)
									.setValue(systemParamService.parsePasswordType(systemParamFormWrapper.getPwdParamList().get(i).getValue())[0]);
						}else if(pwdPolicyDescription!=null){
							passowrdPolicy = pwdPolicyDescription;
						}else if(parsePasswordType!=null){
							systemParamFormWrapper.getPwdParamList().get(i)
							.setValue(systemParamService.parsePasswordType(systemParamFormWrapper.getPwdParamList().get(i).getValue())[0]);
						}
				}
			}
		}
		model.addObject(FormBeanConstants.SYSTEM_PARAM_FORM_BEAN, systemParamFormWrapper);
		logger.debug("passowrdPolicy in controller::" + passowrdPolicy);
		model.addObject(BaseConstants.PD_POLICY_DESCRIPTIONDB , (passowrdPolicy==null?pwdPolicyDescription:passowrdPolicy));
		model.setViewName(ViewNameConstants.MODIFY_SYSTEM_PARAM);

		return model;
	}

	/**
	 * Update Customer Parameters
	 * 
	 * @param systemParamFormWrapper
	 * @param result
	 * @param status
	 * @param file
	 * @param requestActionType
	 * @param request
	 * @return
	 */
	@PreAuthorize("hasAnyAuthority('MODIFY_SYSTEM_PARAMETER')")
	@RequestMapping(value = ControllerConstants.EDIT_CUSTOMER_SYSTEM_PARAMETER, method = RequestMethod.POST)
	public ModelAndView editCustomerSytemParam(
			@ModelAttribute(FormBeanConstants.SYSTEM_PARAM_FORM_BEAN) SystemParameterFormWrapper systemParamFormWrapper, BindingResult result,
			@RequestParam(value = "CpasswordType", required = false) String cPasswordType, @RequestParam(value = "CpasswordPolicyNameCustom",
					required = false) String cPwdPolicyDescription,
			@RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE, required = true) String requestActionType, HttpServletRequest request)
			throws SMException {
		ModelAndView model = new ModelAndView();		
		validator.setRequestActionType(requestActionType);
		validator.validate(systemParamFormWrapper, result);
		String passowrdPolicy = "";
		if (result.hasErrors()) {
			setAllParamList(requestActionType, systemParamFormWrapper, model);
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);
		} else {
			saveSystemParamByGroup(requestActionType, systemParamFormWrapper, model, request, null, null, null,null);
		}

		model.addObject(SystemParametersConstant.CUSTOMER_LOGO, systemParamService.getCustomerLogoPath(SystemParametersConstant.CUSTOMER_LOGO));
		model.addObject(SystemParametersConstant.CUSTOMER_LOGO_LARGE, systemParamService.getCustomerLogoPath(SystemParametersConstant.CUSTOMER_LOGO_LARGE));
		for (int i = 0; i < systemParamFormWrapper.getPwdParamList().size(); i++) {
			if (systemParamFormWrapper.getPwdParamList().get(i).getAlias().equalsIgnoreCase(BaseConstants.STAFF_PD)) {
				systemParamFormWrapper.getPwdParamList().get(i)
						.setValue(systemParamService.parsePasswordType(systemParamFormWrapper.getPwdParamList().get(i).getValue())[0]);
				passowrdPolicy = cPwdPolicyDescription;

			}
		}
		model.addObject(FormBeanConstants.SYSTEM_PARAM_FORM_BEAN, systemParamFormWrapper);
		logger.debug("passowrdPolicy in controller::" + passowrdPolicy);
		model.addObject(BaseConstants.PD_POLICY_DESCRIPTIONDB , passowrdPolicy);
		model.setViewName(ViewNameConstants.MODIFY_SYSTEM_PARAM);

		return model;
	}

	/**
	 * Set all system parameter value , when some error occur in validation
	 * 
	 * @param requestActionType
	 * @param systemParamFormWrapper
	 * @param model
	 * @param uploadedImageId
	 */
	private void setAllParamList(String requestActionType, SystemParameterFormWrapper systemParamFormWrapper, ModelAndView model) throws SMException {
		List<SystemParameterValuePoolData> sysParamValuePoolList = MapCache
				.getSystemParameterValuePoolList(SystemParametersConstant.SYSTEM_PARAM_VALUE_POOL);
		List<SystemParameterData> dbValueList = new ArrayList<>();

		if (SystemParametersConstant.EDIT_GEN_SYSTEM_PARAM.equals(requestActionType)) {

			systemParamFormWrapper.setGenParamList(systemParamService.setValuePoolDetail(systemParamFormWrapper.getGenParamList(),
					sysParamValuePoolList));
			systemParamFormWrapper.setPwdParamList(MapCache.getSystemParameterList(SystemParametersConstant.PASSWORD_PARAMETERS));
			systemParamFormWrapper.setCustParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_PARAMETERS));
			systemParamFormWrapper.setCustLogoParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_LOGO_PARAMETERS));
			systemParamFormWrapper.setFileReprocessingParamList(MapCache.getSystemParameterList(SystemParametersConstant.FILE_REPROCESSING_PARAMETERS));
			dbValueList = MapCache.getSystemParameterList(SystemParametersConstant.GENERAL_PARAMETERS);

		} else if (SystemParametersConstant.EDIT_PWD_SYSTEM_PARAM.equals(requestActionType)) {

			systemParamFormWrapper.setPwdParamList(systemParamService.setValuePoolDetail(systemParamFormWrapper.getPwdParamList(),
					sysParamValuePoolList));
			systemParamFormWrapper.setGenParamList(MapCache.getSystemParameterList(SystemParametersConstant.GENERAL_PARAMETERS));
			systemParamFormWrapper.setCustParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_PARAMETERS));
			systemParamFormWrapper.setCustLogoParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_LOGO_PARAMETERS));
			systemParamFormWrapper.setFileReprocessingParamList(MapCache.getSystemParameterList(SystemParametersConstant.FILE_REPROCESSING_PARAMETERS));
			dbValueList = MapCache.getSystemParameterList(SystemParametersConstant.PASSWORD_PARAMETERS);

		} else if (SystemParametersConstant.EDIT_CUST_SYSTEM_PARAM.equals(requestActionType)) {

			systemParamFormWrapper.setCustParamList(systemParamService.setValuePoolDetail(systemParamFormWrapper.getCustParamList(),
					sysParamValuePoolList));
			systemParamFormWrapper.setGenParamList(MapCache.getSystemParameterList(SystemParametersConstant.GENERAL_PARAMETERS));
			systemParamFormWrapper.setPwdParamList(MapCache.getSystemParameterList(SystemParametersConstant.PASSWORD_PARAMETERS));
			systemParamFormWrapper.setCustLogoParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_LOGO_PARAMETERS));
			systemParamFormWrapper.setFileReprocessingParamList(MapCache.getSystemParameterList(SystemParametersConstant.FILE_REPROCESSING_PARAMETERS));
			dbValueList = MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_PARAMETERS);

		} else if (SystemParametersConstant.EDIT_CUST_LOGO_SYSTEM_PARAM.equals(requestActionType)) {

			systemParamFormWrapper.setGenParamList(MapCache.getSystemParameterList(SystemParametersConstant.GENERAL_PARAMETERS));
			systemParamFormWrapper.setPwdParamList(MapCache.getSystemParameterList(SystemParametersConstant.PASSWORD_PARAMETERS));
			systemParamFormWrapper.setCustParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_PARAMETERS));
			systemParamFormWrapper.setCustLogoParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_LOGO_PARAMETERS));
			systemParamFormWrapper.setFileReprocessingParamList(MapCache.getSystemParameterList(SystemParametersConstant.FILE_REPROCESSING_PARAMETERS));

		} else if (SystemParametersConstant.EDIT_FILE_REPROCESSING_PARAM.equals(requestActionType)) {
			
			systemParamFormWrapper.setFileReprocessingParamList(systemParamService.setValuePoolDetail(systemParamFormWrapper.getFileReprocessingParamList(),
					sysParamValuePoolList));
			systemParamFormWrapper.setGenParamList(MapCache.getSystemParameterList(SystemParametersConstant.GENERAL_PARAMETERS));
			systemParamFormWrapper.setPwdParamList(MapCache.getSystemParameterList(SystemParametersConstant.PASSWORD_PARAMETERS));
			systemParamFormWrapper.setCustParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_PARAMETERS));
			systemParamFormWrapper.setCustLogoParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_LOGO_PARAMETERS));
			dbValueList = MapCache.getSystemParameterList(SystemParametersConstant.FILE_REPROCESSING_PARAMETERS);
		}

		model.addObject(SystemParametersConstant.SYSTEM_PARAM_DB_WRAPPER, dbValueList);
	}

	/**
	 * Update System Parameter and refresh cache
	 * 
	 * @param requestActionType
	 * @param systemParamFormWrapper
	 * @param model
	 * @param request
	 * @param uploadedImageId
	 */
	@SuppressWarnings("deprecation")
	private void saveSystemParamByGroup(String requestActionType, SystemParameterFormWrapper systemParamFormWrapper, ModelAndView model,
			HttpServletRequest request, String uploadedImageName, String logoType, String passwordType,byte [] customer_logo) {
		
		List<SystemParameterValuePoolData> sysParamValuePoolList = MapCache.getSystemParameterValuePoolList(SystemParametersConstant.SYSTEM_PARAM_VALUE_POOL);
		List<SystemParameterData> sysParamlist;
		ResponseObject responseObject = null;

		if (SystemParametersConstant.EDIT_GEN_SYSTEM_PARAM.equals(requestActionType)) {
			sysParamlist = systemParamFormWrapper.getGenParamList();
			List<SystemParameterValuePoolData> systemParameterValuePoolDatas = new ArrayList<SystemParameterValuePoolData>();
			Integer index = -1;
			Integer defaultAccessGroupIndex = 0;
			for (SystemParameterData sysparamData : sysParamlist) {
				++index;
				if(sysparamData.getAlias().equalsIgnoreCase("DEFAULT_ACCESS_GROUP")) {
					List<String> accessGroups = new ArrayList<>();
					accessGroups.add(SELECT_ACCESS_GROUP_OPTION);
					List<String> accessGroups1 = accessGroupService.getAllAccessGroupByStaffType(BaseConstants.LDAP_STAFF);
					Integer count = 0;
					defaultAccessGroupIndex = index;
					if(accessGroups1 != null && !accessGroups1.isEmpty())
						accessGroups.addAll(accessGroups1);
					for(String accessGroup : accessGroups) {
						SystemParameterValuePoolData systemParameterValuePoolData = new SystemParameterValuePoolData();
						systemParameterValuePoolData.setId(++count);
						systemParameterValuePoolData.setName(accessGroup);
						systemParameterValuePoolData.setValue(accessGroup);
						systemParameterValuePoolData.setParentSystemParameter(sysparamData);
						systemParameterValuePoolDatas.add(systemParameterValuePoolData);
					}
				}
				sysparamData.setLastUpdatedDate(new Date());
				sysparamData.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			}
			responseObject = systemParamService.updateSystemParameters(sysParamlist);
			sysParamlist = systemParamService.setValuePoolDetail(sysParamlist, sysParamValuePoolList);
			systemParamService.addAllSystemParameterInCache(SystemParametersConstant.GENERAL_PARAMETERS, sysParamlist);
			SystemParameterData systemParameterData = sysParamlist.get(defaultAccessGroupIndex);
			systemParameterData.setParameterDetail(systemParameterValuePoolDatas);
			sysParamlist.set(defaultAccessGroupIndex, systemParameterData);
			if (sysParamlist != null) {
				for (SystemParameterData systemParameter : sysParamlist) {
					MapCache.addConfigObject(systemParameter.getAlias(), systemParameter.getValue());
				}
			}

			systemParamFormWrapper.setGenParamList(sysParamlist);
			systemParamFormWrapper.setPwdParamList(MapCache.getSystemParameterList(SystemParametersConstant.PASSWORD_PARAMETERS));
			systemParamFormWrapper.setCustParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_PARAMETERS));
			systemParamFormWrapper.setCustLogoParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_LOGO_PARAMETERS));
			systemParamFormWrapper.setFileReprocessingParamList(MapCache.getSystemParameterList(SystemParametersConstant.FILE_REPROCESSING_PARAMETERS));
			systemParamFormWrapper.setEmailParamList(MapCache.getSystemParameterList(SystemParametersConstant.EMAIL_PARAM));
			systemParamFormWrapper.setLdapParamList(MapCache.getSystemParameterList(SystemParametersConstant.LOGIN_PARAM));
			systemParamFormWrapper.setSsoParamList(MapCache.getSystemParameterList(SystemParametersConstant.SSO_PARAM));
			systemParamFormWrapper.setEmailFooterLogoList(MapCache.getSystemParameterList(SystemParametersConstant.EMAIL_FOOTER_IMAGE_PARAM));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, SystemParametersConstant.GENERAL_PARAMETERS);
		}

		else if (SystemParametersConstant.EDIT_PWD_SYSTEM_PARAM.equals(requestActionType)) {
			sysParamlist = systemParamFormWrapper.getPwdParamList();
			for (SystemParameterData sysparamData : sysParamlist) {
				sysparamData.setLastUpdatedDate(new Date());
				sysparamData.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				if (sysparamData.getAlias().equalsIgnoreCase(BaseConstants.STAFF_PD)) {

					sysparamData.setValue(sysparamData.getValue() + "~" + passwordType);

					logger.debug("sysparamData Value for password:::" + sysparamData.getValue());
				}
			}
			responseObject = systemParamService.updateSystemParameters(sysParamlist);
			sysParamlist = systemParamService.setValuePoolDetail(sysParamlist, sysParamValuePoolList);
			systemParamService.addAllSystemParameterInCache(SystemParametersConstant.PASSWORD_PARAMETERS, sysParamlist);

			if (sysParamlist != null) {
				for (SystemParameterData systemParameter : sysParamlist) {
					MapCache.addConfigObject(systemParameter.getAlias(), systemParameter.getValue());
				}
			}

			systemParamFormWrapper.setPwdParamList(sysParamlist);
			systemParamFormWrapper.setGenParamList(MapCache.getSystemParameterList(SystemParametersConstant.GENERAL_PARAMETERS));
			systemParamFormWrapper.setCustParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_PARAMETERS));
			systemParamFormWrapper.setCustLogoParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_LOGO_PARAMETERS));
			systemParamFormWrapper.setFileReprocessingParamList(MapCache.getSystemParameterList(SystemParametersConstant.FILE_REPROCESSING_PARAMETERS));
			systemParamFormWrapper.setEmailParamList(MapCache.getSystemParameterList(SystemParametersConstant.EMAIL_PARAM));
			systemParamFormWrapper.setLdapParamList(MapCache.getSystemParameterList(SystemParametersConstant.LOGIN_PARAM));
			systemParamFormWrapper.setSsoParamList(MapCache.getSystemParameterList(SystemParametersConstant.SSO_PARAM));
			systemParamFormWrapper.setEmailFooterLogoList(MapCache.getSystemParameterList(SystemParametersConstant.EMAIL_FOOTER_IMAGE_PARAM));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, SystemParametersConstant.PASSWORD_PARAMETERS);
		}

		else if (SystemParametersConstant.EDIT_CUST_SYSTEM_PARAM.equals(requestActionType)) {
			sysParamlist = systemParamFormWrapper.getCustParamList();
			for (SystemParameterData sysparamData : sysParamlist) {
				sysparamData.setLastUpdatedDate(new Date());
				sysparamData.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			}
			responseObject = systemParamService.updateSystemParameters(sysParamlist);
			sysParamlist = systemParamService.setValuePoolDetail(sysParamlist, sysParamValuePoolList);
			systemParamService.addAllSystemParameterInCache(SystemParametersConstant.CUSTOMER_PARAMETERS, sysParamlist);

			if (sysParamlist != null) {
				for (SystemParameterData systemParameter : sysParamlist) {
					MapCache.addConfigObject(systemParameter.getAlias(), systemParameter.getValue());
				}
			}

			systemParamFormWrapper.setGenParamList(MapCache.getSystemParameterList(SystemParametersConstant.GENERAL_PARAMETERS));
			systemParamFormWrapper.setPwdParamList(MapCache.getSystemParameterList(SystemParametersConstant.PASSWORD_PARAMETERS));
			systemParamFormWrapper.setCustParamList(sysParamlist);
			systemParamFormWrapper.setCustLogoParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_LOGO_PARAMETERS));
			systemParamFormWrapper.setFileReprocessingParamList(MapCache.getSystemParameterList(SystemParametersConstant.FILE_REPROCESSING_PARAMETERS));
			systemParamFormWrapper.setEmailParamList(MapCache.getSystemParameterList(SystemParametersConstant.EMAIL_PARAM));
			systemParamFormWrapper.setLdapParamList(MapCache.getSystemParameterList(SystemParametersConstant.LOGIN_PARAM));
			systemParamFormWrapper.setSsoParamList(MapCache.getSystemParameterList(SystemParametersConstant.SSO_PARAM));
			systemParamFormWrapper.setEmailFooterLogoList(MapCache.getSystemParameterList(SystemParametersConstant.EMAIL_FOOTER_IMAGE_PARAM));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, SystemParametersConstant.CUSTOMER_PARAMETERS);
		}

		else if (SystemParametersConstant.EDIT_CUST_LOGO_SYSTEM_PARAM.equals(requestActionType)) {
			sysParamlist = systemParamFormWrapper.getCustLogoParamList();
			for (SystemParameterData sysparamData : sysParamlist) {
				if (SystemParametersConstant.CUSTOMER_LOGO.equals(sysparamData.getAlias())
						&& BaseConstants.SMALL_LOGO_TYPE.equalsIgnoreCase(logoType)) {
					sysparamData.setValue(uploadedImageName);
					sysparamData.setImage(customer_logo);
				} else if (SystemParametersConstant.CUSTOMER_LOGO_LARGE.equals(sysparamData.getAlias())
						&& BaseConstants.LARGE_LOGO_TYPE.equalsIgnoreCase(logoType)) {
					sysparamData.setValue(uploadedImageName);
					sysparamData.setImage(customer_logo);
				}

				sysparamData.setLastUpdatedDate(new Date());
				sysparamData.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			}
			responseObject = systemParamService.updateSystemParameters(sysParamlist);
			sysParamlist = systemParamService.setValuePoolDetail(sysParamlist, sysParamValuePoolList);
			systemParamService.addAllSystemParameterInCache(SystemParametersConstant.CUSTOMER_LOGO_PARAMETERS, sysParamlist);

			if (sysParamlist != null) {
				for (SystemParameterData systemParameter : sysParamlist) {
					MapCache.addConfigObject(systemParameter.getAlias(), systemParameter.getValue());
				}
			}

			systemParamFormWrapper.setGenParamList(MapCache.getSystemParameterList(SystemParametersConstant.GENERAL_PARAMETERS));
			systemParamFormWrapper.setPwdParamList(MapCache.getSystemParameterList(SystemParametersConstant.PASSWORD_PARAMETERS));
			systemParamFormWrapper.setCustParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_PARAMETERS));
			systemParamFormWrapper.setEmailParamList(MapCache.getSystemParameterList(SystemParametersConstant.EMAIL_PARAM));
			systemParamFormWrapper.setLdapParamList(MapCache.getSystemParameterList(SystemParametersConstant.LOGIN_PARAM));
			systemParamFormWrapper.setSsoParamList(MapCache.getSystemParameterList(SystemParametersConstant.SSO_PARAM));
			systemParamFormWrapper.setCustLogoParamList(sysParamlist);
			systemParamFormWrapper.setFileReprocessingParamList(MapCache.getSystemParameterList(SystemParametersConstant.FILE_REPROCESSING_PARAMETERS));
			systemParamFormWrapper.setEmailFooterLogoList(MapCache.getSystemParameterList(SystemParametersConstant.EMAIL_FOOTER_IMAGE_PARAM));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, SystemParametersConstant.CUSTOMER_LOGO_PARAMETERS);
		}
		
		else if (SystemParametersConstant.EDIT_FILE_REPROCESSING_PARAM.equals(requestActionType)) {
			sysParamlist = systemParamFormWrapper.getFileReprocessingParamList();
			for (SystemParameterData sysparamData : sysParamlist) {
				sysparamData.setLastUpdatedDate(new Date());
				sysparamData.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			}
			responseObject = systemParamService.updateSystemParameters(sysParamlist);
			sysParamlist = systemParamService.setValuePoolDetail(sysParamlist, sysParamValuePoolList);
			systemParamService.addAllSystemParameterInCache(SystemParametersConstant.CUSTOMER_PARAMETERS, sysParamlist);

			if (sysParamlist != null) {
				for (SystemParameterData systemParameter : sysParamlist) {
					MapCache.addConfigObject(systemParameter.getAlias(), systemParameter.getValue());
				}
			}
			
			systemParamFormWrapper.setGenParamList(MapCache.getSystemParameterList(SystemParametersConstant.GENERAL_PARAMETERS));
			systemParamFormWrapper.setPwdParamList(MapCache.getSystemParameterList(SystemParametersConstant.PASSWORD_PARAMETERS));
			systemParamFormWrapper.setCustParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_PARAMETERS));
			systemParamFormWrapper.setCustLogoParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_LOGO_PARAMETERS));
			systemParamFormWrapper.setEmailParamList(MapCache.getSystemParameterList(SystemParametersConstant.EMAIL_PARAM));
			systemParamFormWrapper.setLdapParamList(MapCache.getSystemParameterList(SystemParametersConstant.LOGIN_PARAM));
			systemParamFormWrapper.setSsoParamList(MapCache.getSystemParameterList(SystemParametersConstant.SSO_PARAM));
			systemParamFormWrapper.setFileReprocessingParamList(sysParamlist);
			systemParamFormWrapper.setEmailFooterLogoList(MapCache.getSystemParameterList(SystemParametersConstant.EMAIL_FOOTER_IMAGE_PARAM));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, SystemParametersConstant.FILE_REPROCESSING_PARAMETERS);
		} else if (SystemParametersConstant.EDIT_EMAIL_PARAMETER.equals(requestActionType)) {
			sysParamlist = systemParamFormWrapper.getEmailParamList();
			SystemParameterData sysparamDataFromEmailPassword = null;
			for (SystemParameterData sysparamData : sysParamlist) {
				if(SystemParametersConstant.FROM_EMAIL_PASSWORD.equals(sysparamData.getAlias()) && sysparamData.getValue()!=null){
					sysparamData.setValue(EliteUtils.encryptData(sysparamData.getValue()));
					sysparamDataFromEmailPassword = sysparamData;
				}
				sysparamData.setLastUpdatedDate(new Date());
				sysparamData.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			}
			responseObject = systemParamService.updateSystemParameters(sysParamlist);			
			if(sysparamDataFromEmailPassword!=null && SystemParametersConstant.FROM_EMAIL_PASSWORD.equals(sysparamDataFromEmailPassword.getAlias()) && sysparamDataFromEmailPassword.getValue()!=null){
				sysparamDataFromEmailPassword.setValue(EliteUtils.decryptData(sysparamDataFromEmailPassword.getValue()));
			}
			sysParamlist = systemParamService.setValuePoolDetail(sysParamlist, sysParamValuePoolList);
			systemParamService.addAllSystemParameterInCache(SystemParametersConstant.EMAIL_PARAM, sysParamlist);
			
			systemParamFormWrapper.setGenParamList(MapCache.getSystemParameterList(SystemParametersConstant.GENERAL_PARAMETERS));
			systemParamFormWrapper.setPwdParamList(MapCache.getSystemParameterList(SystemParametersConstant.PASSWORD_PARAMETERS));
			systemParamFormWrapper.setCustParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_PARAMETERS));
			systemParamFormWrapper.setCustLogoParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_LOGO_PARAMETERS));
			systemParamFormWrapper.setFileReprocessingParamList(MapCache.getSystemParameterList(SystemParametersConstant.FILE_REPROCESSING_PARAMETERS));
			systemParamFormWrapper.setLdapParamList(MapCache.getSystemParameterList(SystemParametersConstant.LOGIN_PARAM));
			systemParamFormWrapper.setSsoParamList(MapCache.getSystemParameterList(SystemParametersConstant.SSO_PARAM));
			systemParamFormWrapper.setEmailParamList(sysParamlist);
			systemParamFormWrapper.setEmailFooterLogoList(MapCache.getSystemParameterList(SystemParametersConstant.EMAIL_FOOTER_IMAGE_PARAM));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, SystemParametersConstant.EMAIL_PARAM);
		}else if (SystemParametersConstant.EDIT_SSO_PARAMETER.equals(requestActionType)) {
				sysParamlist = systemParamFormWrapper.getSsoParamList();
				
				List<SystemParameterValuePoolData> systemParameterValuePoolDatas = new ArrayList<SystemParameterValuePoolData>();
				Integer index = -1;
				Integer defaultAccessGroupIndex = 0;
				SystemParameterData sysparamDataFromSSOPassword = null;
				for (SystemParameterData sysparamData : sysParamlist) {
					++index;
					
					if(sysparamData.getAlias().equalsIgnoreCase("DEFAULT_SSO_ACCESS_GROUP")) {
						List<String> accessGroups = new ArrayList<>();
						accessGroups.add(SELECT_ACCESS_GROUP_OPTION);
						List<String> accessGroups1 = accessGroupService.getAllAccessGroupByStaffType(BaseConstants.SSO_STAFF);
						Integer count = 0;
						defaultAccessGroupIndex = index;
						if(accessGroups1 != null && !accessGroups1.isEmpty())
							accessGroups.addAll(accessGroups1);
						for(String accessGroup : accessGroups) {
							SystemParameterValuePoolData systemParameterValuePoolData = new SystemParameterValuePoolData();
							systemParameterValuePoolData.setId(++count);
							systemParameterValuePoolData.setName(accessGroup);
							systemParameterValuePoolData.setValue(accessGroup);
							systemParameterValuePoolData.setParentSystemParameter(sysparamData);
							systemParameterValuePoolDatas.add(systemParameterValuePoolData);
						}
						}else if(SystemParametersConstant.SSO_ADMIN_PASSWORD.equals(sysparamData.getAlias()) && sysparamData.getValue()!=null){
						sysparamData.setValue(EliteUtils.encryptData(sysparamData.getValue()));
						sysparamDataFromSSOPassword = sysparamData;
						}
						
					if(sysparamData.getAlias().equalsIgnoreCase("SSO_ENABLE")) {
						if(sysparamData.getValue()==null) {
							sysparamData.setValue("true");
						}
					}
					sysparamData.setLastUpdatedDate(new Date());
					sysparamData.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
				}
				responseObject = systemParamService.updateSystemParameters(sysParamlist);
//				if(sysparamDataFromSSOPassword!=null && SystemParametersConstant.SSO_ADMIN_PASSWORD.equals(sysparamDataFromSSOPassword.getAlias()) && sysparamDataFromSSOPassword.getValue()!=null){
//					sysparamDataFromSSOPassword.setValue(EliteUtils.decryptData(sysparamDataFromSSOPassword.getValue()));
//				}
				
				sysParamlist = systemParamService.setValuePoolDetail(sysParamlist, sysParamValuePoolList);
				systemParamService.addAllSystemParameterInCache(SystemParametersConstant.SSO_PARAM, sysParamlist);
				if (sysParamlist != null) {
					for (SystemParameterData systemParameter : sysParamlist) {
						MapCache.addConfigObject(systemParameter.getAlias(), systemParameter.getValue());
					}
				}
				SystemParameterData systemParameterData = sysParamlist.get(defaultAccessGroupIndex);
				systemParameterData.setParameterDetail(systemParameterValuePoolDatas);
				sysParamlist.set(defaultAccessGroupIndex, systemParameterData);														
				
				systemParamFormWrapper.setGenParamList(MapCache.getSystemParameterList(SystemParametersConstant.GENERAL_PARAMETERS));
				systemParamFormWrapper.setPwdParamList(MapCache.getSystemParameterList(SystemParametersConstant.PASSWORD_PARAMETERS));
				systemParamFormWrapper.setCustParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_PARAMETERS));
				systemParamFormWrapper.setCustLogoParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_LOGO_PARAMETERS));
				systemParamFormWrapper.setFileReprocessingParamList(MapCache.getSystemParameterList(SystemParametersConstant.FILE_REPROCESSING_PARAMETERS));
				systemParamFormWrapper.setEmailParamList(MapCache.getSystemParameterList(SystemParametersConstant.EMAIL_PARAM));
				systemParamFormWrapper.setSsoParamList(sysParamlist);
				systemParamFormWrapper.setLdapParamList(MapCache.getSystemParameterList(SystemParametersConstant.LOGIN_PARAM));
				systemParamFormWrapper.setEmailFooterLogoList(MapCache.getSystemParameterList(SystemParametersConstant.EMAIL_FOOTER_IMAGE_PARAM));
				model.addObject(BaseConstants.REQUEST_ACTION_TYPE, SystemParametersConstant.SSO_PARAM);
			
		} else if (SystemParametersConstant.EDIT_LOGIN_PARAMETER.equals(requestActionType)) {
			sysParamlist = systemParamFormWrapper.getLdapParamList();
			List<SystemParameterValuePoolData> systemParameterValuePoolDatas = new ArrayList<SystemParameterValuePoolData>();
			List<SystemParameterValuePoolData> systemParameterValuePoolDatasAuth = new ArrayList<SystemParameterValuePoolData>();
			Integer index = -1;
			Integer defaultAccessGroupIndex = 0;
			Integer ldapAuthEnableIndex = 0;
			SystemParameterData sysparamDataFromSSOPassword = null;
			for (SystemParameterData sysparamData : sysParamlist) {
				++index;
				if(sysparamData.getAlias().equalsIgnoreCase("DEFAULT_ACCESS_GROUP")) {
					List<String> accessGroups = new ArrayList<>();
					accessGroups.add(SELECT_ACCESS_GROUP_OPTION);
					List<String> accessGroups1 = accessGroupService.getAllAccessGroupByStaffType(BaseConstants.LDAP_STAFF);
					Integer count = 0;
					defaultAccessGroupIndex = index;
					if(accessGroups1 != null && !accessGroups1.isEmpty())
						accessGroups.addAll(accessGroups1);
					for(String accessGroup : accessGroups) {
						SystemParameterValuePoolData systemParameterValuePoolData = new SystemParameterValuePoolData();
						systemParameterValuePoolData.setId(++count);
						systemParameterValuePoolData.setName(accessGroup);
						systemParameterValuePoolData.setValue(accessGroup);
						systemParameterValuePoolData.setParentSystemParameter(sysparamData);
						systemParameterValuePoolDatas.add(systemParameterValuePoolData);
					}
				} else if(sysparamData.getAlias().equalsIgnoreCase("LDAP_AUTH_ENABLE")) {
					String[] stringArr = new String[] {"True","False"};
					ldapAuthEnableIndex = index;
					Integer count = 0;
					for(String ldapAuthEnable : stringArr) {
						SystemParameterValuePoolData systemParameterValuePoolData = new SystemParameterValuePoolData();
						systemParameterValuePoolData.setId(++count);
						systemParameterValuePoolData.setName(ldapAuthEnable);
						systemParameterValuePoolData.setValue(ldapAuthEnable);
						systemParameterValuePoolData.setParentSystemParameter(sysparamData);
						systemParameterValuePoolDatasAuth.add(systemParameterValuePoolData);
					}
				} else if(SystemParametersConstant.SSO_ADMIN_PASSWORD.equals(sysparamData.getAlias()) && sysparamData.getValue()!=null){
					sysparamData.setValue(EliteUtils.encryptData(sysparamData.getValue()));
					sysparamDataFromSSOPassword = sysparamData;
				}
				sysparamData.setLastUpdatedDate(new Date());
				sysparamData.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			}
			responseObject = systemParamService.updateSystemParameters(sysParamlist);
			
			sysParamlist = systemParamService.setValuePoolDetail(sysParamlist, sysParamValuePoolList);
			systemParamService.addAllSystemParameterInCache(SystemParametersConstant.LOGIN_PARAM, sysParamlist);
			if (sysParamlist != null) {
				for (SystemParameterData systemParameter : sysParamlist) {
					MapCache.addConfigObject(systemParameter.getAlias(), systemParameter.getValue());
				}
			}
			
//			if(sysparamDataFromSSOPassword!=null && SystemParametersConstant.SSO_ADMIN_PASSWORD.equals(sysparamDataFromSSOPassword.getAlias()) && sysparamDataFromSSOPassword.getValue()!=null){
//				sysparamDataFromSSOPassword.setValue(EliteUtils.decryptData(sysparamDataFromSSOPassword.getValue()));
//			}
			SystemParameterData systemParameterData = sysParamlist.get(defaultAccessGroupIndex);
			systemParameterData.setParameterDetail(systemParameterValuePoolDatas);
			sysParamlist.set(defaultAccessGroupIndex, systemParameterData);
			systemParameterData = sysParamlist.get(ldapAuthEnableIndex);
			systemParameterData.setParameterDetail(systemParameterValuePoolDatasAuth);
			sysParamlist.set(ldapAuthEnableIndex, systemParameterData);
									
			systemParamFormWrapper.setGenParamList(MapCache.getSystemParameterList(SystemParametersConstant.GENERAL_PARAMETERS));
			systemParamFormWrapper.setPwdParamList(MapCache.getSystemParameterList(SystemParametersConstant.PASSWORD_PARAMETERS));
			systemParamFormWrapper.setCustParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_PARAMETERS));
			systemParamFormWrapper.setCustLogoParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_LOGO_PARAMETERS));
			systemParamFormWrapper.setFileReprocessingParamList(MapCache.getSystemParameterList(SystemParametersConstant.FILE_REPROCESSING_PARAMETERS));
			systemParamFormWrapper.setEmailParamList(MapCache.getSystemParameterList(SystemParametersConstant.EMAIL_PARAM));
			systemParamFormWrapper.setLdapParamList(sysParamlist);
			systemParamFormWrapper.setSsoParamList(MapCache.getSystemParameterList(SystemParametersConstant.SSO_PARAM));
			systemParamFormWrapper.setEmailFooterLogoList(MapCache.getSystemParameterList(SystemParametersConstant.EMAIL_FOOTER_IMAGE_PARAM));
			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, SystemParametersConstant.LOGIN_PARAM);
		} else if(SystemParametersConstant.EDIT_EMAIL_FOOTER_IMAGE.equals(requestActionType)) {
			sysParamlist = systemParamFormWrapper.getEmailFooterLogoList();
			for (SystemParameterData sysparamData : sysParamlist) {
				if (SystemParametersConstant.FOOTER_IMAGE.equals(sysparamData.getAlias())) {
					sysparamData.setValue(uploadedImageName);
				}
				sysparamData.setLastUpdatedDate(new Date());
				sysparamData.setLastUpdatedByStaffId(eliteUtils.getLoggedInStaffId(request));
			}
			responseObject = systemParamService.updateSystemParameters(sysParamlist);
			sysParamlist = systemParamService.setValuePoolDetail(sysParamlist, sysParamValuePoolList);
			systemParamService.addAllSystemParameterInCache(SystemParametersConstant.EMAIL_FOOTER_IMAGE_PARAM, sysParamlist);

			if (sysParamlist != null) {
				for (SystemParameterData systemParameter : sysParamlist) {
					MapCache.addConfigObject(systemParameter.getAlias(), systemParameter.getValue());
				}
			}

			systemParamFormWrapper.setGenParamList(MapCache.getSystemParameterList(SystemParametersConstant.GENERAL_PARAMETERS));
			systemParamFormWrapper.setPwdParamList(MapCache.getSystemParameterList(SystemParametersConstant.PASSWORD_PARAMETERS));
			systemParamFormWrapper.setCustParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_PARAMETERS));
			systemParamFormWrapper.setEmailParamList(MapCache.getSystemParameterList(SystemParametersConstant.EMAIL_PARAM));
			systemParamFormWrapper.setLdapParamList(MapCache.getSystemParameterList(SystemParametersConstant.LOGIN_PARAM));
			systemParamFormWrapper.setSsoParamList(MapCache.getSystemParameterList(SystemParametersConstant.SSO_PARAM));
			systemParamFormWrapper.setEmailFooterLogoList(sysParamlist);
			systemParamFormWrapper.setCustLogoParamList(MapCache.getSystemParameterList(SystemParametersConstant.CUSTOMER_LOGO_PARAMETERS));
			systemParamFormWrapper.setFileReprocessingParamList(MapCache.getSystemParameterList(SystemParametersConstant.FILE_REPROCESSING_PARAMETERS));

			model.addObject(BaseConstants.REQUEST_ACTION_TYPE, SystemParametersConstant.EMAIL_PARAM);
		}
		
		if (responseObject != null && responseObject.isSuccess()) {					
			model.addObject(BaseConstants.IS_SSO_ENABLED, isSSOParameterEnabled(systemParamFormWrapper.getSsoParamList()));
			model.addObject(BaseConstants.RESPONSE_MSG, getMessage("systemParameter.updated.success"));
		}
	}

	/**
	 * Edit customer logo
	 * 
	 * @param systemParamFormWrapper
	 * @param result
	 * @param file
	 * @param largeLogo
	 * @param custLogoId
	 * @param requestActionType
	 * @param logoType
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @throws SMException
	 */
	@PreAuthorize("hasAnyAuthority('MODIFY_SYSTEM_PARAMETER')")
	@RequestMapping(value = ControllerConstants.EDIT_CUSTOMER_LOGO, method = RequestMethod.POST)
	public ModelAndView editCustomerLogo(@ModelAttribute(FormBeanConstants.SYSTEM_PARAM_FORM_BEAN) SystemParameterFormWrapper systemParamFormWrapper,
			BindingResult result, @RequestParam("custLogofile") MultipartFile file, @RequestParam("custLargeLogofile") MultipartFile largeLogo,
			@RequestParam(value = "custLogoId", required = true) String custLogoId, @RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE,
					required = true) String requestActionType, @RequestParam(value = "CLpasswordType", required = false) String cPasswordType,
			@RequestParam(value = "CLpasswordPolicyNameCustom", required = false) String cPwdPolicyDescription, @RequestParam(value = "logoType",
					required = false) String logoType, HttpServletRequest request) throws SMException {
		ModelAndView model = new ModelAndView();
		logger.debug("cPasswordType::" + cPasswordType);
		logger.debug("cPwdPolicyDescription::" + cPwdPolicyDescription);
		String passowrdPolicy = "";
		try {

			if (!file.isEmpty() || !largeLogo.isEmpty()) {

				if ("small".equalsIgnoreCase(logoType)) {
					validator.setSmallLogoContentType(file.getContentType());
					validator.validateCustomerSmallLogoParameters(systemParamFormWrapper, result);
				} else {
					validator.setLargeLogoContentType(largeLogo.getContentType());
					validator.validateCustomerLargeLogoParameters(systemParamFormWrapper, result);
				}

				if (result.hasErrors()) {

					setAllParamList(requestActionType, systemParamFormWrapper, model);

					model.addObject(BaseConstants.REQUEST_ACTION_TYPE, SystemParametersConstant.EDIT_CUST_LOGO_SYSTEM_PARAM);

				} else {

					ResponseObject responseObject = systemParamService.getLocationOfSystemBackUpPath(SystemBackUpPathOptionEnum.SYSTEMPARAMIMAGE);
					if (responseObject.isSuccess()) {
						String samplePath = (String) responseObject.getObject();
						logger.debug("Going to store customer small logo  file at location: " + samplePath);
						responseObject = systemParamService.getCustomerLogoFile(custLogoId);
						if (responseObject.isSuccess()) {
							logger.debug("If file exists then remove it");
							File uploadedFile = (File) responseObject.getObject();
							uploadedFile.delete();
						}
						File uploadedFile;
						String fileName;
						byte [] byteArr;
						if ("small".equalsIgnoreCase(logoType)) {
							fileName = file.getOriginalFilename();
							uploadedFile = new File(samplePath + File.separator + custLogoId + "_" + fileName);
							byteArr=file.getBytes();
							file.transferTo(uploadedFile);
						} else {
							fileName = largeLogo.getOriginalFilename();
							uploadedFile = new File(samplePath + File.separator + custLogoId + "_" + fileName);
							byteArr=largeLogo.getBytes();
							largeLogo.transferTo(uploadedFile);
						}

						if (uploadedFile.exists()) {
							logger.debug("Sample File saved successfully , now save basic detail of regex parser mapping");
							String logoFileName = custLogoId + "_" + fileName;
							saveSystemParamByGroup(requestActionType, systemParamFormWrapper, model, request, logoFileName, logoType, null,byteArr);

						}
					}
				}

				model.addObject(SystemParametersConstant.CUSTOMER_LOGO,
						systemParamService.getCustomerLogoPath(SystemParametersConstant.CUSTOMER_LOGO));
				model.addObject(SystemParametersConstant.CUSTOMER_LOGO_LARGE,
						systemParamService.getCustomerLogoPath(SystemParametersConstant.CUSTOMER_LOGO_LARGE));
				for (int i = 0; i < systemParamFormWrapper.getPwdParamList().size(); i++) {
					if (systemParamFormWrapper.getPwdParamList().get(i).getAlias().equalsIgnoreCase(BaseConstants.STAFF_PD)) {
						systemParamFormWrapper.getPwdParamList().get(i)
								.setValue(systemParamService.parsePasswordType(systemParamFormWrapper.getPwdParamList().get(i).getValue())[0]);
						passowrdPolicy = cPwdPolicyDescription;

					}
				}
				model.addObject(FormBeanConstants.SYSTEM_PARAM_FORM_BEAN, systemParamFormWrapper);
				model.addObject(BaseConstants.PD_POLICY_DESCRIPTIONDB , passowrdPolicy);
				model.setViewName(ViewNameConstants.MODIFY_SYSTEM_PARAM);
			} else {
				logger.debug("Uploaded file is empty");
			}

		} catch (Exception e) {
			logger.error("Exception Occured:" + e);
			throw new SMException(e);
		}
		return model;
	}
	
	/**
	 * Edit customer logo
	 * 
	 * @param systemParamFormWrapper
	 * @param result
	 * @param file
	 * @param largeLogo
	 * @param custLogoId
	 * @param requestActionType
	 * @param logoType
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @throws SMException
	 */
	@PreAuthorize("hasAnyAuthority('MODIFY_SYSTEM_PARAMETER')")
	@RequestMapping(value = ControllerConstants.EDIT_EMAIL_FOOTER_IMAGE, method = RequestMethod.POST)
	public ModelAndView editEmailFooterImage(@ModelAttribute(FormBeanConstants.SYSTEM_PARAM_FORM_BEAN) SystemParameterFormWrapper systemParamFormWrapper,
			BindingResult result, @RequestParam("emailFooterFile") MultipartFile file,
			@RequestParam(value = "emailFooterImageId", required = true) String custLogoId, @RequestParam(value = BaseConstants.REQUEST_ACTION_TYPE,
					required = true) String requestActionType, @RequestParam(value = "CLpasswordType", required = false) String cPasswordType,
			@RequestParam(value = "CLpasswordPolicyNameCustom", required = false) String cPwdPolicyDescription, @RequestParam(value = "logoType",
					required = false) String logoType, HttpServletRequest request) throws SMException {
		ModelAndView model = new ModelAndView();
		logger.debug("cPasswordType::" + cPasswordType);
		logger.debug("cPwdPolicyDescription::" + cPwdPolicyDescription);
		String passowrdPolicy = "";
		try {

			if (!file.isEmpty()) {

				if ("footerImage".equalsIgnoreCase(logoType)) {
					validator.setFooterImageContentType(file.getContentType());
					validator.validateEmailFooterImageParameters(systemParamFormWrapper, result);
				} 

				if (result.hasErrors()) {

					setAllParamList(requestActionType, systemParamFormWrapper, model);

					model.addObject(BaseConstants.REQUEST_ACTION_TYPE, requestActionType);

				} else {

					ResponseObject responseObject = systemParamService.getLocationOfSystemBackUpPath(SystemBackUpPathOptionEnum.SYSTEMPARAMIMAGE);
					if (responseObject.isSuccess()) {
						String samplePath = (String) responseObject.getObject();
						logger.debug("Going to store customer small logo  file at location: " + samplePath);
						responseObject = systemParamService.getCustomerLogoFile(custLogoId);
						if (responseObject.isSuccess()) {
							logger.debug("If file exists then remove it");
							File uploadedFile = (File) responseObject.getObject();
							uploadedFile.delete();
						}
						File uploadedFile;
						String fileName;
						if ("footerImage".equalsIgnoreCase(logoType)) {
							fileName = file.getOriginalFilename();
							uploadedFile = new File(samplePath + File.separator + custLogoId + "_" + fileName);
							file.transferTo(uploadedFile);
						} else {
							fileName = file.getOriginalFilename();
							uploadedFile = new File(samplePath + File.separator + custLogoId + "_" + fileName);
							file.transferTo(uploadedFile);
						}

						if (uploadedFile.exists()) {
							logger.debug("Sample File saved successfully , now save basic detail of regex parser mapping");
							String logoFileName = custLogoId + "_" + fileName;
							saveSystemParamByGroup(requestActionType, systemParamFormWrapper, model, request, logoFileName, logoType, null,null);

						}
					}
				}

				model.addObject(SystemParametersConstant.FOOTER_IMAGE,
						systemParamService.getCustomerLogoPath(SystemParametersConstant.FOOTER_IMAGE));
				for (int i = 0; i < systemParamFormWrapper.getPwdParamList().size(); i++) {
					if (systemParamFormWrapper.getPwdParamList().get(i).getAlias().equalsIgnoreCase(BaseConstants.STAFF_PD)) {
						systemParamFormWrapper.getPwdParamList().get(i)
								.setValue(systemParamService.parsePasswordType(systemParamFormWrapper.getPwdParamList().get(i).getValue())[0]);
						passowrdPolicy = cPwdPolicyDescription;

					}
				}
				model.addObject(FormBeanConstants.SYSTEM_PARAM_FORM_BEAN, systemParamFormWrapper);
				model.addObject(BaseConstants.PD_POLICY_DESCRIPTIONDB , passowrdPolicy);
				model.setViewName(ViewNameConstants.MODIFY_SYSTEM_PARAM);
			} else {
				logger.debug("Uploaded file is empty");
			}

		} catch (Exception e) {
			logger.error("Exception Occured:" + e);
			throw new SMException(e);
		}
		return model;
	}
	
	/**
	 * Edit customer logo
	 * 
	 * @param systemParamFormWrapper
	 * @param result
	 * @param file
	 * @param largeLogo
	 * @param custLogoId
	 * @param requestActionType
	 * @param logoType
	 * @param request
	 * @param redirectAttributes
	 * @return
	 * @throws SMException
	 * @throws IOException 
	 * @throws IllegalStateException 
	 */
	@SuppressWarnings("deprecation")
	@PreAuthorize("hasAnyAuthority('MODIFY_SYSTEM_PARAMETER')")
	@RequestMapping(value = "editEmailFooterImage2", method = RequestMethod.POST)
	@ResponseBody
	public String editEmailFooterImage2( @RequestParam("emailFooterFile") MultipartFile file,
			@RequestParam(value = "emailFooterImageId", required = true) String custLogoId, @RequestParam(value = "CLpasswordType", required = false) String cPasswordType,
			@RequestParam(value = "CLpasswordPolicyNameCustom", required = false) String cPwdPolicyDescription, @RequestParam(value = "logoType",
					required = false) String logoType, HttpServletRequest request) throws SMException, IllegalStateException, IOException {
		ResponseObject responseObject = new ResponseObject();
		
		if(!file.isEmpty()) {
			List<SystemParameterData> systemParameterDatas = MapCache.getSystemParameterList(SystemParametersConstant.EMAIL_FOOTER_IMAGE_PARAM);
			SystemParameterData systemParameterData = null;
			if(systemParameterDatas != null && !systemParameterDatas.isEmpty())
				systemParameterData = systemParameterDatas.get(0);
			responseObject = systemParamService.getLocationOfSystemBackUpPath(SystemBackUpPathOptionEnum.SYSTEMPARAMIMAGE);
			if(responseObject.isSuccess()) {
				String samplePath = (String) responseObject.getObject();
				logger.debug("Going to store customer small logo  file at location: " + samplePath);
				responseObject = systemParamService.getCustomerLogoFile(Integer.toString(systemParameterData.getId()));//NOSONAR
				if (responseObject.isSuccess()) {
					logger.debug("If file exists then remove it");
					File uploadedFile = (File) responseObject.getObject();
					uploadedFile.delete();
				}
				File uploadedFile;
				String 	fileName = file.getOriginalFilename();
				uploadedFile = new File(samplePath + File.separator + systemParameterData.getId()  + "_" + fileName);
				file.transferTo(uploadedFile);

				if (uploadedFile.exists()) {
					logger.debug("Sample File saved successfully , now save basic detail of regex parser mapping");
					String logoFileName = systemParameterData.getId() + "_" + fileName;
					systemParameterData.setValue(logoFileName);
					List<SystemParameterData> syetDatas = new ArrayList<>();
					syetDatas.add(systemParameterData);
					systemParamService.updateSystemParameters(syetDatas);
					systemParamService.addAllSystemParameterInCache(SystemParametersConstant.EMAIL_FOOTER_IMAGE_PARAM, syetDatas);
					MapCache.addConfigObject(SystemParametersConstant.FOOTER_IMAGE, BaseConstants.IMAGE_BYTE_CONSTANT+setCustomerLogoFile(uploadedFile));
				}
			}
			
		}else {
			responseObject.setSuccess(false);
		
		}
		responseObject.setSuccess(true);
		responseObject.setObject(null);
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	private String setCustomerLogoFile(File custLogoFile) throws SMException{
		
		try(FileInputStream inputStream = new FileInputStream(custLogoFile) ; ByteArrayOutputStream bos=new ByteArrayOutputStream() ) {

			int b;
			byte[] buffer = new byte[(int) custLogoFile.length()];
	
			while((b=inputStream.read(buffer))!=-1){
			   bos.write(buffer,0,b);
			}
			bos.flush();
			return new String(Base64.encodeBase64(bos.toByteArray()));
			
		} catch (IOException e) {
			throw new SMException(e); 
		}
	}
	
	/**
	 * @return the systemParamService
	 */
	public SystemParameterService getSystemParamService() {
		return systemParamService;
	}

	/**
	 * @param systemParamService
	 *            the systemParamService to set
	 */
	public void setSystemParamService(SystemParameterService systemParamService) {
		this.systemParamService = systemParamService;
	}

	/**
	 * @return the validator
	 */
	public SystemParameterValidator getValidator() {
		return validator;
	}

	/**
	 * @param validator
	 *            the validator to set
	 */
	public void setValidator(SystemParameterValidator validator) {
		this.validator = validator;
	}
	
	@PreAuthorize("hasAnyAuthority('MODIFY_SSO_SYSTEM_PARAMETER')")
	@RequestMapping(value = ControllerConstants.MIGRATE_STAFF_TO_KEYCLOAK, method = RequestMethod.GET)
	@ResponseBody
	public String migrateKeycloakStaff() {
		ResponseObject responseObject = systemParamService.migrateStaffToKeycloak();
		AjaxResponse ajaxResponse = eliteUtils.convertToAjaxResponse(responseObject);
		return ajaxResponse.toString();
	}
	
	private boolean isSSOParameterEnabled(List<SystemParameterData> list) {
		
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).getAlias().equalsIgnoreCase(SystemParametersConstant.SSO_ENABLE)) {
				String isSSOStr = list.get(i).getValue();
				if(isSSOStr!=null) {
					return Boolean.parseBoolean(isSSOStr.toLowerCase());
				}
			}
		}
		return false;
	}
	
}