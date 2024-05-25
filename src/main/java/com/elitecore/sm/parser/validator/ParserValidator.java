/**
 * 
 */
package com.elitecore.sm.parser.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.parser.model.NATFlowParserMapping;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.RegExPattern;
import com.elitecore.sm.parser.model.RegexParserMapping;

/**
 * @author Ranjitsinh Reval
 *
 */
@Component
public class ParserValidator extends BaseValidator{
	
	/**
	 * Method will check current validator class
	 *@param  Class<?> clazz
	 *@return boolean 
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ParserMapping.class.isAssignableFrom(clazz) || Device.class.isAssignableFrom(clazz) || RegExPattern.class.isAssignableFrom(clazz);
	}
	
	/**
	 * Method will check current validator class
	 *@param  Class<?> clazz
	 *@return boolean 
	 */
	@Override
	public void validate(Object target, Errors errors) {
		this.errors = errors;
	}
	
	/**
	 * Method will validate All Parameter for parser configuration , based on parser type
	 * @param target
	 * @param errors
	 */
	public void validateParserConfigurationParameter(Object target, Errors errors,String entityName,boolean validateForImport){
		this.errors = errors;
		
		if (target instanceof NATFlowParserMapping){
			NATFlowParserMapping natflowParser = (NATFlowParserMapping)target;
			if(natflowParser.getSkipAttributeForValidation() != null && !natflowParser.getSkipAttributeForValidation().isEmpty()){
				natflowParser.getSkipAttributeForValidation().replace(" ", "");
				isValidate(SystemParametersConstant.ASN1_PARSER_RECORD_START_IDS,natflowParser.getSkipAttributeForValidation(),"skipAttributeForValidation",entityName,natflowParser.getName(),validateForImport);
			}
			validationOptionTemplateParams(natflowParser, errors,entityName,validateForImport); // Method will validate the option template parameters.
			
			// Validate netflow filter params
			validationFilterParams(natflowParser, errors, entityName, validateForImport);
		}else if (target instanceof RegexParserMapping) {
			RegexParserMapping regExParser = (RegexParserMapping)target;
			validateRegExAttributeBasicDetail(regExParser,entityName,validateForImport);
		}
	}
	
	/**
	 * Method will validate the option template parameters for parser.
	 * @param natflowParser
	 * @param errors
	 */
	private void validationOptionTemplateParams(NATFlowParserMapping natflowParser,Errors errors,String entityName,boolean validateForImport){
		
		isValidate(SystemParametersConstant.NATFLOW_PARSER_OPTION_TEMPLATE_ID,natflowParser.getOptionTemplateId(),"optionTemplateId",entityName,natflowParser.getName(),validateForImport);
		isValidate(SystemParametersConstant.NATFLOW_PARSER_OPTION_TEMPLATE_KEY,natflowParser.getOptionTemplateKey(),"optionTemplateKey",entityName,natflowParser.getName(),validateForImport);
		isValidate(SystemParametersConstant.NATFLOW_PARSER_OPTION_TEMPLATE_VALUE,natflowParser.getOptionTemplateValue(),"optionTemplateValue",entityName,natflowParser.getName(),validateForImport);
		isValidate(SystemParametersConstant.NATFLOW_PARSER_OPTION_COPY_TO_TEMPLATE_ID,natflowParser.getOptionCopytoTemplateId(),"optionCopytoTemplateId",entityName,natflowParser.getName(),validateForImport);
		isValidate(SystemParametersConstant.NATFLOW_PARSER_OPTION_COPY_TO_FIELD,natflowParser.getOptionCopyTofield(),"optionCopyTofield",entityName,natflowParser.getName(),validateForImport);
		
		if(natflowParser.isOptionTemplateEnable()){
			if(natflowParser.getOptionTemplateId().length() == 0)
				errors.rejectValue("optionTemplateId", "natflow.parser.mapping.optionTemplateId.invalid", getMessage("natflow.parser.mapping.optionTemplateId.invalid"));
			if(natflowParser.getOptionTemplateKey().length() == 0)
				errors.rejectValue("optionTemplateKey", "natflow.parser.mapping.optionTemplateKey.invalid", getMessage("natflow.parser.mapping.optionTemplateKey.invalid"));
			if(natflowParser.getOptionTemplateValue().length() == 0)
				errors.rejectValue("optionTemplateValue", "natflow.parser.mapping.optionTemplateValue.invalid", getMessage("natflow.parser.mapping.optionTemplateValue.invalid"));
			if(natflowParser.getOptionCopytoTemplateId().length() == 0)
				errors.rejectValue("optionCopytoTemplateId", "natflow.parser.mapping.optionCopytoTemplateId.invalid", getMessage("natflow.parser.mapping.optionCopytoTemplateId.invalid"));
			if(natflowParser.getOptionCopyTofield().length() == 0)
				errors.rejectValue("optionCopyTofield", "natflow.parser.mapping.optionCopyTofield.invalid", getMessage("natflow.parser.mapping.optionCopyTofield.invalid"));
		}
	}
	
	private void validationFilterParams(NATFlowParserMapping natflowParser,Errors errors,String entityName,boolean validateForImport){
		
		isValidate(SystemParametersConstant.NATFLOW_PARSER_FILTER_PROTOCOL,natflowParser.getFilterProtocol(),"filterProtocol",entityName,natflowParser.getName(),validateForImport);
		isValidate(SystemParametersConstant.NATFLOW_PARSER_FILTER_TRANSPORT,natflowParser.getFilterTransport(),"filterTransport",entityName,natflowParser.getName(),validateForImport);
		isValidate(SystemParametersConstant.NATFLOW_PARSER_FILTER_PORT,natflowParser.getFilterPort(),"filterPort",entityName,natflowParser.getName(),validateForImport);
		
		if(natflowParser.isFilterEnable()){
			if(natflowParser.getFilterProtocol().length() == 0)
				errors.rejectValue("filterProtocol", "natflow.parser.mapping.filterProtocol.invalid", getMessage("natflow.parser.mapping.filterProtocol.invalid"));
			if(natflowParser.getFilterTransport().length() == 0)
				errors.rejectValue("filterTransport", "natflow.parser.mapping.filterTransport.invalid", getMessage("natflow.parser.mapping.filterTransport.invalid"));
			if(natflowParser.getFilterPort().length() == 0)
				errors.rejectValue("filterPort", "natflow.parser.mapping.filterPort.invalid", getMessage("natflow.parser.mapping.filterPort.invalid"));
		}
	}
	
	/**
	 * Validate parsing service parser object
	 * @param parser
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 * @param pathListValidationError
	 */
	public void validateParserForParsing(Parser parser,Errors errors,String entityName,boolean validateForImport,List<ImportValidationErrors> pathListValidationError){
		if(validateForImport){
			this.importErrorList=pathListValidationError;
		}else{
			this.errors = errors;
		}
		
		isValidate(SystemParametersConstant.PARSER_WRITEFILEPATH,parser.getWriteFilePath(),"writeFilePath",entityName,parser.getName(),validateForImport);
		isValidate(SystemParametersConstant.PARSER_READFILENAMEPREFIX,parser.getReadFilenamePrefix(),"readFilenamePrefix",entityName,parser.getName(),validateForImport);
		//isValidate(SystemParametersConstant.PARSER_READFILENAMESUFFIX,parser.getReadFilenameSuffix(),"readFilenameSuffix",entityName,parser.getName(),validateForImport);
		isValidate(SystemParametersConstant.PARSER_READFILENAMECONTAINS,parser.getReadFilenameContains(),"readFilenameContains",entityName,parser.getName(),validateForImport);
		isValidate(SystemParametersConstant.PARSER_READFILENAMEEXCLUDETYPES,parser.getReadFilenameExcludeTypes(),"readFilenameExcludeTypes",entityName,parser.getName(),validateForImport);
		isValidate(SystemParametersConstant.PARSER_WRITEFILENAMEPREFIX,parser.getWriteFilenamePrefix(),"writeFilenamePrefix",entityName,parser.getName(),validateForImport);
		isValidate(SystemParametersConstant.PARSER_NAME,parser.getName(),"name",entityName,parser.getName(),validateForImport);
		isValidate(SystemParametersConstant.PARSER_MAXFILECOUNTALERT, parser.getMaxFileCountAlert(), "maxFileCountAlert",
				parser.getName(), entityName, validateForImport, parser.getClass().getName());
	}
	
	/**
	 * Method will validate Regex Attribute Basic Detais.
	 * @param regExParser
	 * @param entityName
	 * @param validateForImport
	 */
	private void validateRegExAttributeBasicDetail(RegexParserMapping regExParser,String entityName,boolean validateForImport){
		isValidate(SystemParametersConstant.PARSERMAPPING_SRCDATEFORMAT,regExParser.getSrcDateFormat(),"srcDateFormat",entityName,regExParser.getName(),validateForImport);
		validateRegEx(regExParser.getLogPatternRegex(),"logPatternRegex",SystemParametersConstant.REGEXPARSERMAPPING_LOGPATTERNREGEX+".invalid");
		
	}
	
}
