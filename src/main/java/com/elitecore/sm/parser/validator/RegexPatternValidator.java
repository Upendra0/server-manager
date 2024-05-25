package com.elitecore.sm.parser.validator;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.parser.model.RegExPattern;

@Component
public class RegexPatternValidator extends BaseValidator{

	@Override
	public boolean supports(Class<?> clazz) {
		return RegExPattern.class.isAssignableFrom(clazz);
	}
	
	/**
	 * Validate regex pattern parameter
	 * @param target
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateRegExPatternParameter(Object target, Errors errors,String entityName,boolean validateForImport){
		this.errors = errors;
		RegExPattern regExPattern=(RegExPattern)target;
		isValidate(SystemParametersConstant.REGEXPATTERN_PATTERNREGEXNAME,regExPattern.getPatternRegExName(),"patternRegExName",entityName,regExPattern.getPatternRegExName(),validateForImport);
		
		if(StringUtils.isEmpty(regExPattern.getPatternRegExId())){
			if(validateForImport){
				ImportValidationErrors error=new ImportValidationErrors(BaseConstants.PARSER,regExPattern.getPatternRegExName(), "patternRegExId", regExPattern.getPatternRegExId(),getMessage(SystemParametersConstant.REGEXPATTERN_PATTERNREGEX+".invalid"));
	    		importErrorList.add(error);
			}else{
				errors.rejectValue("patternRegExId", "RegExPattern.patternRegExId.invalid", getMessage(SystemParametersConstant.REGEXPATTERN_PATTERNREGEXID+".invalid"));
			}
		}
		validateRegEx(regExPattern.getPatternRegEx(),"patternRegEx",SystemParametersConstant.REGEXPATTERN_PATTERNREGEX+".invalid");
	}
}
