/**
 * 
 */
package com.elitecore.sm.systemparam.validator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.systemparam.model.SystemParameterData;
import com.elitecore.sm.systemparam.model.SystemParameterFormWrapper;
import com.elitecore.sm.util.Regex;

/**
 * @author vandana.awatramani
 * 
 */
@Component
public class SystemParameterValidator extends BaseValidator {
	private SystemParameterFormWrapper systemParamFormWrapper;
	private String requestActionType = null;
	private String smallLogoContentType = null;
	private String largeLogoContentType = null;
	private String footerImageContentType = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {

		return SystemParameterFormWrapper.class.isAssignableFrom(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	public void validate(Object target, Errors errors,String passwordType,String policyName) {

		this.errors = errors;
		systemParamFormWrapper = (SystemParameterFormWrapper) target;
		String requestType;
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		requestType = request.getParameter(BaseConstants.REQUEST_ACTION_TYPE);

		if (requestType == null) {
			logger.debug("requestActionType is not found in request");
			requestType = getRequestActionType();
		}

		if (SystemParametersConstant.EDIT_GEN_SYSTEM_PARAM.equals(requestType)) {
			validateGeneralParameter(systemParamFormWrapper);
		} else if (SystemParametersConstant.EDIT_PWD_SYSTEM_PARAM.equals(requestType)) {
			validatePasswordParameters(systemParamFormWrapper,passwordType,policyName);
		} else if (SystemParametersConstant.EDIT_CUST_SYSTEM_PARAM.equals(requestType)) {
			validateCustomerParameters(systemParamFormWrapper);
			setRequestActionType(null);
		} else if (SystemParametersConstant.EDIT_FILE_REPROCESSING_PARAM.equals(requestType)) {
			validateFileReprocessingParameter(systemParamFormWrapper);
		}

	}

	/**
	 * Validate General Parameters
	 * 
	 * @param systemParamFormWrapper
	 * @param errors
	 */

	public void validateGeneralParameter(SystemParameterFormWrapper systemParamFormWrapper) {
		int i = 0;
		for (SystemParameterData systemParam : systemParamFormWrapper.getGenParamList()) {

			logger.debug("checking General param " + systemParam.getName() + " for input general param value " + systemParam.getValue()
					+ " against general param regex " + systemParam.getRegularExpression());
			isValidateSystemParam(systemParam.getRegularExpression(), systemParam.getValue(), systemParam.getErrorMessage(), "genParamList["+i+BaseConstants.SYSTEMPARAM_GEN_LIST_VALUE);
			if (SystemParametersConstant.DATE_TIME_FORMAT.equals(systemParam.getAlias()) || SystemParametersConstant.DATE_FORMAT.equals(systemParam.getAlias())) {
				logger.debug("For Date Format Check::");
				if (!validateDateFormat(systemParam.getValue())) {
					logger.debug("Date Format Check Invalid::");
					String errorMsgValue = getMessage("systemParameter.dateFormat.error");
					errors.rejectValue("genParamList[" + i+ BaseConstants.SYSTEMPARAM_GEN_LIST_VALUE, "systemParameter.dateFormat.error", errorMsgValue);
				}

			}
			i++;
		}

	}
	
	public void validateFileReprocessingParameter(SystemParameterFormWrapper systemParamFormWrapper) {
		int i = 0;
		for (SystemParameterData systemParam : systemParamFormWrapper.getFileReprocessingParamList()) {
			logger.debug("checking File Reprocessing param " + systemParam.getName() + " for input file reprocessing param value " + systemParam.getValue()
					+ " against file reprocessing param regex " + systemParam.getRegularExpression());
			isValidateSystemParam(systemParam.getRegularExpression(), systemParam.getValue(), systemParam.getErrorMessage(), "fileReprocessingParamList[" + i
					+ BaseConstants.SYSTEMPARAM_GEN_LIST_VALUE);

			i++;
		}
	}

	/**
	 * Validate Password Parameters
	 * 
	 * @param systemParamFormWrapper
	 * @param errors
	 */
	public void validatePasswordParameters(SystemParameterFormWrapper systemParamFormWrapper,String passwordType,String policyName) {
		int i = 0;
		for (SystemParameterData systemParam : systemParamFormWrapper.getPwdParamList()) {

			logger.debug("checking password param " + systemParam.getName() + " for input password param value " + systemParam.getValue()
					+ " against password param  regex " + systemParam.getRegularExpression());
			if (SystemParametersConstant.STAFF_PASSWORD.equals(systemParam.getAlias())) {
				validateRegularExpression(systemParam.getValue(), BaseConstants.PD_PARAMLIST_STRING+i+BaseConstants.SYSTEMPARAM_GEN_LIST_VALUE, systemParam.getErrorMessage());
			} else if (!SystemParametersConstant.PASSWORD_TYPE.equals(systemParam.getAlias())) {
			
				isValidateSystemParam(systemParam.getRegularExpression(), systemParam.getValue(), systemParam.getErrorMessage(), BaseConstants.PD_PARAMLIST_STRING + i
						+ BaseConstants.SYSTEMPARAM_GEN_LIST_VALUE);
			}
			if(("Staff.password").equalsIgnoreCase(systemParam.getAlias())&&("Regular Expression").equalsIgnoreCase(passwordType)){
				isValidateSystemParam(Regex.get("SystemParameter.PasswordDescription",BaseConstants.REGEX_MAP_CACHE), policyName, "systemParameter.PwdPolicy.error", BaseConstants.PD_PARAMLIST_STRING + i
						+ "].passwordDescription");
				
				isValidateSystemParam(Regex.get("SystemParameter.PasswordRegularExpression",BaseConstants.REGEX_MAP_CACHE), systemParam.getValue(), "systemParameter.PwdRegex.error", BaseConstants.PD_PARAMLIST_STRING + (i)
						+ "].value");
			}
			i++;
		}

	}

	/**
	 * Validate Customer Parameters
	 * 
	 * @param systemParamFormWrapper
	 * @param errors
	 */
	public void validateCustomerParameters(SystemParameterFormWrapper systemParamFormWrapper) {
		int i = 0;

		for (SystemParameterData systemParam : systemParamFormWrapper.getCustParamList()) {

			logger.debug("checking customer param " + systemParam.getName() + " for input customer  param value " + systemParam.getValue()
					+ " against customer param regex " + systemParam.getRegularExpression());

			isValidateSystemParam(systemParam.getRegularExpression(), systemParam.getValue(), systemParam.getErrorMessage(), "custParamList[" + i
					+ BaseConstants.SYSTEMPARAM_GEN_LIST_VALUE);

			i++;
		}
	}

	/**
	 * Validate Customer Logo Parameters
	 * 
	 * @param systemParamFormWrapper
	 * @param errors
	 */
	public void validateCustomerSmallLogoParameters(SystemParameterFormWrapper systemParamFormWrapper, Errors errors) {
		this.errors = errors;
		int i = 0;
		String errorMsgValue;
		smallLogoContentType = getSmallLogoContentType();

		for (SystemParameterData systemParam : systemParamFormWrapper.getCustLogoParamList()) {

			if (SystemParametersConstant.CUSTOMER_LOGO.equals(systemParam.getAlias())) {
				logger.debug("checking param " + systemParam.getName() + " for input value " + systemParam.getValue() + " against regex "
						+ systemParam.getRegularExpression());
				if (smallLogoContentType != null && !(smallLogoContentType.startsWith(SystemParametersConstant.IMAGE_CONTENT_TYPE))) {

					errorMsgValue = getMessage(BaseConstants.CUSTOMER_LOGO_ERROR );
					logger.debug("errorMsgValue: " + errorMsgValue);
					errors.rejectValue("custLogoParamList[" + i + BaseConstants.SYSTEMPARAM_GEN_LIST_VALUE, BaseConstants.CUSTOMER_LOGO_ERROR , errorMsgValue);
					setSmallLogoContentType(null);

				}
			}

			i++;
		}
	}

	/**
	 * Validate Customer Logo Parameters
	 * 
	 * @param systemParamFormWrapper
	 * @param errors
	 */
	public void validateCustomerLargeLogoParameters(SystemParameterFormWrapper systemParamFormWrapper, Errors errors) {
		int i = 0;
		String errorMsgValue;
		largeLogoContentType = getLargeLogoContentType();
		for (SystemParameterData systemParam : systemParamFormWrapper.getCustLogoParamList()) {

			logger.debug("checking param " + systemParam.getName() + " for input value " + systemParam.getValue() + " against regex "
					+ systemParam.getRegularExpression());

			if (SystemParametersConstant.CUSTOMER_LOGO_LARGE.equals(systemParam.getAlias()) && largeLogoContentType != null && !(largeLogoContentType.startsWith(SystemParametersConstant.IMAGE_CONTENT_TYPE))) {
				

					errorMsgValue = getMessage(BaseConstants.CUSTOMER_LOGO_ERROR );
					logger.debug("errorMsgValue: " + errorMsgValue);
					errors.rejectValue("custLogoParamList[" + i + BaseConstants.SYSTEMPARAM_GEN_LIST_VALUE, BaseConstants.CUSTOMER_LOGO_ERROR , errorMsgValue);
					setLargeLogoContentType(null);

				
			}

			i++;
		}
	}
	
	/**
	 * Validate Customer Logo Parameters
	 * 
	 * @param systemParamFormWrapper
	 * @param errors
	 */
	public void validateEmailFooterImageParameters(SystemParameterFormWrapper systemParamFormWrapper, Errors errors) {
		int i = 0;
		String errorMsgValue;
		footerImageContentType = getLargeLogoContentType();
		for (SystemParameterData systemParam : systemParamFormWrapper.getEmailFooterLogoList()) {

			logger.debug("checking param " + systemParam.getName() + " for input value " + systemParam.getValue() + " against regex "
					+ systemParam.getRegularExpression());

			if (SystemParametersConstant.FOOTER_IMAGE.equals(systemParam.getAlias()) && footerImageContentType != null && !(footerImageContentType.startsWith(SystemParametersConstant.IMAGE_CONTENT_TYPE))) {
					errorMsgValue = getMessage(BaseConstants.CUSTOMER_LOGO_ERROR );
					logger.debug("errorMsgValue: " + errorMsgValue);
					errors.rejectValue("emailFooterLogoList[" + i + BaseConstants.SYSTEMPARAM_GEN_LIST_VALUE, BaseConstants.CUSTOMER_LOGO_ERROR , errorMsgValue);
					setFooterImageContentType(null);
			}
			i++;
		}
	}
	


	/**
	 * Validate Regular Expression
	 * 
	 * @param sysParamValue
	 * @param propertyName
	 * @param errorMsgKey
	 */
	public void validateRegularExpression(String sysParamValue, String propertyName, String errorMsgKey) {
		validateRegEx(sysParamValue, propertyName, errorMsgKey);
	}

	/**
	 * 
	 * @return RequestActionType
	 */
	public String getRequestActionType() {
		return requestActionType;
	}

	/**
	 * 
	 * @param requestActionType
	 */
	public void setRequestActionType(String requestActionType) {
		this.requestActionType = requestActionType;
	}

	/**
	 * 
	 * @return
	 */
	public String getSmallLogoContentType() {
		return smallLogoContentType;
	}

	/**
	 * 
	 * @param smallLogoContentType
	 */
	public void setSmallLogoContentType(String smallLogoContentType) {
		this.smallLogoContentType = smallLogoContentType;
	}

	/**
	 * 
	 * @return
	 */
	public String getLargeLogoContentType() {
		return largeLogoContentType;
	}

	/**
	 * 
	 * @param largeLogoContentType
	 */
	public void setLargeLogoContentType(String largeLogoContentType) {
		this.largeLogoContentType = largeLogoContentType;
	}

	public String getFooterImageContentType() {
		return footerImageContentType;
	}

	public void setFooterImageContentType(String footerImageContentType) {
		this.footerImageContentType = footerImageContentType;
	}
	

}
