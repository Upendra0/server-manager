/**
 * 
 */
package com.elitecore.sm.common.validator;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springmodules.validation.util.condition.string.EmailStringCondition;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.config.model.EntityValidationRange;
import com.elitecore.sm.util.MapCache;
import com.elitecore.sm.util.Regex;

/**
 * @author Sunil Gulabani
 * Apr 22, 2015
 */

@Component(value="baseValidator")
public class BaseValidator  implements Validator {

	protected Logger logger = Logger.getLogger(this.getClass().getName());
	
	@Autowired
    private MessageSource messageSource;
	
	private String regexLoggerConstant = "REGEX: ";
	private String errorMsgLoggerConstant = "errorMsgValue: ";
	private String regexConstant = "[REGEX]";
	private String errorConstant = "error.";
	private String invalidConstant = ".invalid";
	
	protected Pattern pattern;
	protected Matcher matcher;
	
	protected Errors errors;
	
	protected List<ImportValidationErrors> importErrorList;
	
	protected Class<?> klass;
	
	public Class<?> getKlass() {
		return klass;
	}

	public void setKlass(Class<?> klass) {
		this.klass = klass;
	}
	
	/**
	 * This method will provide the value for the specified key based on the user's locale.
	 * @param key
	 * @param arguments if placeholders in message
	 * @return
	 */
	public String getMessage(String key, Object[] arguments){
		try {
			return messageSource.getMessage(key, arguments, LocaleContextHolder.getLocale());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}
	
	/**
	 * This method will provide the value for the specified key based on the user's locale.
	 * @param key
	 * @return
	 */
	public String getMessage(String key ){
		try {
			return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		return "";
	}
	
	
	/**
	 * Method will set errors for import and general validation.
	 * @param errors
	 * @param validateForImport
	 * @param importErrorList
	 */
	public void setErrorObject(Errors errors, boolean validateForImport, List<ImportValidationErrors> importErrorList){
		if(validateForImport){
			this.importErrorList = importErrorList;
		}else{
			this.errors = errors;	
		}
	}
	
	/**
	 * This method validates the regex and sets error if it fails. , DONT USE THIS METHOD DIRECTLY
	 * @param regex
	 * @param value
	 * @param errorMsgKey
	 * @param propertyName
	 * @param arguments for placeholder messages
	 * @deprecated Method is deprecated.
	 * @return
	 */
	@Deprecated
	protected boolean isValidate(String regex, String value, String errorMsgKey, String propertyName, Object[]arguments,String moduleName,String entityName,boolean validateForImport){
    	logger.debug( regexLoggerConstant + regex);
		
    	String errorMsgValue ;
    	String  regexVal;
    	
    	if(SystemParametersConstant.STAFF_PASSWORD.equals(regex)){
    		String delims = "~"; // so the delimiters is: ~
    		String[] tokens= (MapCache.getConfigValueAsString(SystemParametersConstant.STAFF_PASSWORD,"")).split(delims);
    		
    		if(tokens!=null){
    		regexVal = tokens[0];
    		}
    		else{
    			regexVal = regex;
    		}
    	}else{
    		regexVal = Regex.get(regex,BaseConstants.REGEX_MAP_CACHE);
    	}
    	
    	if (match(regexVal, value)){
    		errorMsgValue = getMessage(errorMsgKey,arguments);
    		
    		logger.debug(errorMsgLoggerConstant + errorMsgValue);
    		errorMsgValue = errorMsgValue.replace(regexConstant,regexVal);
    		if(validateForImport){
    			ImportValidationErrors importErrors = new ImportValidationErrors(moduleName,entityName, propertyName, value, errorMsgValue);
        		importErrorList.add(importErrors);
    		}else{
    			errors.rejectValue(propertyName, errorConstant + errorMsgKey, errorMsgValue);
    		}
    		return false;
    	}else{
    		logger.info("Regex key is :: " + regex);
    		return true;	
    	}
    }
	
	protected boolean isValidate(String regex, long value, String errorMsgKey, String propertyName,String moduleName, String entityName,boolean validateForImport,String className){
    	logger.debug(regexLoggerConstant + regex);
    	logger.debug("className: " + className);
    	String errorMsgValue ;
    	
    	Object [] argument = null;
    	boolean isObjectFound = true;
    	
    	EntityValidationRange entitiesRegexRange = Regex.getValidationRange(BaseConstants.REGEX_MAP_CACHE, regex,className);
    	if(entitiesRegexRange != null){
    		argument = new Object[]{entitiesRegexRange.getMinRange(),entitiesRegexRange.getMaxRange()};
    	}else{
    		isObjectFound = false;
    	}
    	
    	errorMsgValue = getMessage(errorMsgKey, argument );
		logger.debug(errorMsgLoggerConstant  + errorMsgValue);
		errorMsgValue = errorMsgValue.replace(regexConstant,Regex.get(regex, BaseConstants.REGEX_MAP_CACHE));
    	
	
    	if (match(Regex.get(regex,BaseConstants.REGEX_MAP_CACHE), String.valueOf(value))){ // If REGEX fail then condition will be true and display error message.
    		if(validateForImport){
    			ImportValidationErrors errorList = new ImportValidationErrors(moduleName,entityName, propertyName, String.valueOf(value), errorMsgValue);
        		importErrorList.add(errorList);
    		}else{
    			errors.rejectValue(propertyName, errorConstant + errorMsgKey, errorMsgValue);
    		}
    		return false;
    	}else{
    		logger.info("Going to check bussiness validation for minimum and maximum value.");
    		
    		String loggerComment = "Min-Max range not found for regex key ";
    		
    		if(isObjectFound && entitiesRegexRange.getAdditionalCheckVal() != null && entitiesRegexRange.getAdditionalCheckVal().trim().length() > 0){
    			String[] strValues = entitiesRegexRange.getAdditionalCheckVal().split(",");
    			if(strValues != null){
    				logger.debug( "Additional Check Values :" + entitiesRegexRange.getAdditionalCheckVal());
    				for(String strVal : strValues){
    					int checkVal = Integer.parseInt(strVal);
    					if(checkVal == value){
    						return true;
    					}
    				}
    			}
    		}
    		 
    		if(isObjectFound && (entitiesRegexRange.getMinRange() != -5 && entitiesRegexRange.getMaxRange() != -5)){
    			if( value < entitiesRegexRange.getMinRange() || value > entitiesRegexRange.getMaxRange()){
    				setErrorFieldErrorMessage(String.valueOf(value), propertyName, moduleName, entityName, validateForImport, errorMsgKey, errorMsgValue);
        			return false;
    			}else{
    				logger.debug( loggerComment  +  Regex.get(regex, BaseConstants.REGEX_MAP_CACHE));
    				return true;
    			}
    		}else if(isObjectFound && (entitiesRegexRange.getMinRange() >= -1 && entitiesRegexRange.getMaxRange() == -5)){
    			if( value < entitiesRegexRange.getMinRange()){
    				setErrorFieldErrorMessage(String.valueOf(value), propertyName, moduleName, entityName, validateForImport, errorMsgKey, errorMsgValue);
        			return false;
    			}else{
    				logger.debug(loggerComment + Regex.get(regex, BaseConstants.REGEX_MAP_CACHE));
    				return true;
    			}
    		}else if(isObjectFound && (entitiesRegexRange.getMinRange() == -5 && entitiesRegexRange.getMaxRange() >= 0)){
    			if( value >entitiesRegexRange.getMaxRange()){
    				setErrorFieldErrorMessage(String.valueOf(value), propertyName, moduleName, entityName, validateForImport, errorMsgKey, errorMsgValue);
        			return false;
    			}else{
    				logger.debug(loggerComment  + Regex.get(regex, BaseConstants.REGEX_MAP_CACHE));
    				return true;
    			}
    		}else{
    			logger.debug("Failed to validation min- max validation range conditions.");
    			return true;
    		}
    	}
    }
	
	public void setErrorFieldErrorMessage(String value, String propertyName,String moduleName, String entityName,boolean validateForImport, String errorMsgKey, String errorMsgValue){
		if(validateForImport){
			ImportValidationErrors errorList = new ImportValidationErrors(moduleName,entityName, propertyName, value, errorMsgValue);
    		importErrorList.add(errorList);
		}else{
			errors.rejectValue(propertyName, errorMsgKey+ invalidConstant, errorMsgValue);
		}
	}
	
	public void validateIPAddress(String ipAddrValue, String propertyName,String moduleName, String entityName,boolean validateForImport, String errorMsgKey, String errorMsgValue){
		InetAddressValidator ipValidator = new InetAddressValidator();

		if( ipAddrValue!=null && (!(ipValidator.isValidInet4Address(ipAddrValue) || ipValidator.isValidInet6Address(ipAddrValue)))){
			// it can be balnk or have garbage in either case gets detected in next check
			
				setErrorFieldErrorMessage( ipAddrValue,  propertyName, moduleName,  entityName, validateForImport,  errorMsgKey,  errorMsgValue);	
			
		}	
	}
	
	
	public void setErrorFieldErrorMessage(String value, String propertyName,String moduleName, String entityName,boolean validateForImport, String errorMsgKey, Object[] args,String errorMsgValue){
		if(validateForImport){
			ImportValidationErrors errorList = new ImportValidationErrors(moduleName,entityName, propertyName, value, errorMsgValue);
    		importErrorList.add(errorList);
		}else{
			errors.rejectValue(propertyName, errorMsgKey+ invalidConstant,args, errorMsgValue);
		}
	}
	
	/** Set Error Message For Field
	 * 
	 * @author chetan
	 *
	 * @param value
	 * @param propertyName
	 * @param moduleName
	 * @param entityName
	 * @param validateForImport
	 * @param errorMsgKey
	 * @param args
	 * @param alias
	 * @param rscMsgKey
	 */
	public void setErrorFieldErrorMessage(String value, String propertyName,String moduleName, String entityName,boolean validateForImport, 
			String errorMsgKey, Object[] args , long alias , String rscMsgKey ){
		
		String errorMsgValue = getMessage(rscMsgKey);
		
		if(validateForImport){
			ImportValidationErrors errorList = new ImportValidationErrors(moduleName,entityName, propertyName, value, errorMsgValue);
    		importErrorList.add(errorList);
		}else{
			errors.rejectValue(propertyName, errorMsgKey ,args, errorMsgValue);
		}
	}
	
	// for system param only
	protected boolean validateSystemParam(String regex, String value, String errorMsgKey, String propertyName, Object[]arguments){
    	logger.debug(regexLoggerConstant + regex);
    	
    	String errorMsgValue ;
    	
    	if (match(regex, value)){
    		errorMsgValue = getMessage(errorMsgKey,arguments);
    		errorMsgValue = errorMsgValue.replace(regexConstant,Regex.get(regex));
    		logger.debug(errorMsgLoggerConstant + errorMsgValue);
    		errors.rejectValue(propertyName, errorConstant + errorMsgKey, errorMsgValue);
    		return false;
    	}else{
    		logger.debug(Regex.get(regex) + " regex matches.");
    		return true;
    	}
    }
	
	
	/**
	 * This method validates the regex and sets error if it fails. , DONT USE THIS METHOD DIRECTLY
	 * @param regex
	 * @param value
	 * @param errorMsgKey
	 * @param propertyName
	 * @deprecated use {isValidate with class name params.} instead.  
	 * @return
	 */
	@Deprecated
	protected boolean isValidate(String regex, String value, String errorMsgKey, String propertyName){
    	return isValidate(regex, value, errorMsgKey, propertyName, null,null,null,false);
    }
	
	// new method
	/**
	 * USE THIS METHOD FROM CUSTOM VALIDATOR , FOR VALIDATION
	 * @param regexName
	 * @param value
	 * @param propertyName
	 * @return
	 */
	protected boolean isValidate(String regexName,String value,String propertyName,String moduleName,String entityName,boolean validateForImport){
    	return isValidate(regexName,value,regexName+ invalidConstant, propertyName, null,moduleName,entityName,validateForImport);
    	
    }
	
	protected boolean isValidate(String regexName,String value,String propertyName,Object[] arguments,String moduleName,String entityName,boolean validateForImport){
    	return isValidate(regexName,value,regexName+ invalidConstant, propertyName, arguments,moduleName,entityName,validateForImport);
    }
	
	protected boolean isValidate(String regexName,long value,String propertyName,String moduleName,String entityName,boolean validateForImport,String fullClassName){
		return 	isValidate(regexName, value,regexName+ invalidConstant, propertyName, moduleName, entityName, validateForImport, fullClassName);
    }
	
	
	// for system param only
	protected boolean isValidateSystemParam(String regexName,String value,String errorKey,String propertyName){
    	return validateSystemParam(regexName,value,errorKey, propertyName, null);
    }
	
	/**
	 * @param regex
	 * @param value
	 * @param errorMsgKey
	 * @param propertyName
	 * @deprecated use {isValidate with class name params.} instead.  
	 * @return
	 */
	@Deprecated
	protected boolean isValidated(String regex, String value, String errorMsgKey, String propertyName){
    	return isValidate(regex, value, errorMsgKey, propertyName, null,null,null,false);
    }
	
	/**
	 * It validates the email address
	 * @param value
	 * @param errorMsgKey
	 * @param propertyName
	 * @return
	 */
	protected boolean isEmailValidate(String value, String errorMsgKey, String propertyName,Object[] arguments){
		String errorMsgValue ;
		
		if(!new EmailStringCondition().check(value)){
			errorMsgValue = getMessage(errorMsgKey,arguments);
			logger.debug(errorMsgLoggerConstant + errorMsgValue);
			errors.rejectValue(propertyName, errorConstant + errorMsgKey, errorMsgValue);
			return false;
		}
		
		return true;
	}
	
	/**
	 * It validates the email address
	 * @param value
	 * @param errorMsgKey
	 * @param propertyName
	 * @return
	 */
	protected boolean isEmailValidate(String value, String errorMsgKey, String propertyName){
		return isEmailValidate(value, errorMsgKey, propertyName, null);
	}
	
	/**
	 * It matches the regex 
	 * @param regex
	 * @param value
	 * @return
	 */
	protected boolean match(String regex, String value){
		String newValue = "";
		if(value != null){
			newValue = value;
		}
		//logger.info("value: " + newValue);
		//logger.info(regexLoggerConstant + regex);
    	pattern = Pattern.compile(regex,Pattern.UNICODE_CHARACTER_CLASS);
    	//logger.info("pattern: " + pattern);
    	matcher = pattern.matcher(newValue);
    	return !matcher.matches();
	}
	
	protected boolean validateRegEx(String regEx,String propertyName,String errorMsgKey){
		String errorMsgValue ;
		if(regEx != null && !"".equals(regEx)){
			try {
				Pattern.compile(regEx,Pattern.UNICODE_CHARACTER_CLASS);
				return true;
	        } catch (PatternSyntaxException exception) {
	        	logger.error("Exception Occured: "+exception);
	            errorMsgValue = getMessage(errorMsgKey);
	    		logger.debug(errorMsgLoggerConstant + errorMsgValue);
	    		errors.rejectValue(propertyName, errorMsgKey, errorMsgValue);
	    		return false;
	    	}
	        }else{
	        	logger.debug("RegEx is null");
	        	errorMsgValue = getMessage(errorMsgKey);
	    		
	    		errors.rejectValue(propertyName, errorMsgKey, errorMsgValue);
	        	return false;
	        }
	}
	
	@Override
	public boolean supports(Class<?> clazz) {
		return getKlass().isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object arg0, Errors arg1) {
		// Method is not used.
		
	}

	public boolean validateDateFormat(String dateFormat) {
		 if(dateFormat != null && dateFormat!=""){
		       
	            try{
	                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
	                logger.debug(simpleDateFormat.toPattern());
	                return true;
	            } catch (IllegalArgumentException e){
	            	
	            	logger.debug("Inside catch..So date format is not valid..IllegalArgumentException"+e);
	            	return false;
	            }
	        }
		 else{
	        return true;
		 }
		
	}
	
}