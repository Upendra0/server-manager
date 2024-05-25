/**
 * 
 */
package com.elitecore.sm.parser.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.parser.model.ASN1ParserMapping;
import com.elitecore.sm.parser.model.AsciiParserMapping;
import com.elitecore.sm.parser.model.FixedLengthASCIIParserMapping;
import com.elitecore.sm.parser.model.FixedLengthBinaryParserMapping;
import com.elitecore.sm.parser.model.NATFlowParserMapping;
import com.elitecore.sm.parser.model.NRTRDEParserMapping;
import com.elitecore.sm.parser.model.NatflowASN1ParserMapping;
import com.elitecore.sm.parser.model.PDFParserMapping;
import com.elitecore.sm.parser.model.ParserGroupAttribute;
import com.elitecore.sm.parser.model.ParserMapping;
import com.elitecore.sm.parser.model.RapParserMapping;
import com.elitecore.sm.parser.model.RegexParserMapping;
import com.elitecore.sm.parser.model.TapParserMapping;
import com.elitecore.sm.parser.model.VarLengthAsciiParserMapping;

/**
 * @author Ranjitsinh Reval
 *
 */
@Component
public class ParserMappingValidator extends BaseValidator {

	/**
	 * Method will check current validator class
	 *
	 * @param Class
	 *            <?> clazz
	 * @return boolean
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ParserMapping.class.isAssignableFrom(clazz) || NATFlowParserMapping.class.isAssignableFrom(clazz);
	}

	/**
	 * Method will validate All Parameter for parser configuration , based on
	 * parser type
	 * 
	 * @param target
	 * @param errors
	 */
	public void validateParserMappingParameter(Object target, Errors errors, String entityName, boolean validateForImport,
			List<ImportValidationErrors> importErrorList) {
		setErrorObject(errors, validateForImport, importErrorList);

		if (target instanceof NATFlowParserMapping) {
			NATFlowParserMapping natflowParser = (NATFlowParserMapping) target;
			if(natflowParser.getSkipAttributeForValidation() != null && !natflowParser.getSkipAttributeForValidation().isEmpty()){
				isValidate(SystemParametersConstant.ASN1_PARSER_RECORD_START_IDS,natflowParser.getSkipAttributeForValidation(),"skipAttributeForValidation",entityName,natflowParser.getName(),validateForImport);
			}
			validationOptionTemplateParams(natflowParser, errors, entityName, validateForImport, importErrorList); // Method
			validationFilterParams(natflowParser, errors, entityName, validateForImport, importErrorList);

		} else if (target instanceof NatflowASN1ParserMapping) {
			NatflowASN1ParserMapping natflowParser = (NatflowASN1ParserMapping) target;
			validationOptionTemplateParams(natflowParser, errors, entityName, validateForImport, importErrorList); // Method
																													// will
		} else if (target instanceof AsciiParserMapping) {
			AsciiParserMapping asciiParser = (AsciiParserMapping) target;
			validateAsciiParserMappingParamater(asciiParser, errors, entityName, validateForImport, importErrorList);
		
		} else if (target instanceof RegexParserMapping) {
			RegexParserMapping regExParser = (RegexParserMapping) target;
			validateRegExAttributeBasicDetail(regExParser, errors, entityName, validateForImport, importErrorList);
		
		} else if (target instanceof ASN1ParserMapping) {
			ASN1ParserMapping asn1ParserMapping = (ASN1ParserMapping) target;
			validateAsn1AttributeBasicDetail(asn1ParserMapping, errors, entityName, validateForImport, importErrorList);
		} else if(target instanceof FixedLengthASCIIParserMapping){
			FixedLengthASCIIParserMapping fixedLengthASCIIParserMapping = (FixedLengthASCIIParserMapping) target;
			validateFixedLengthASCIIParserBasicDetail(fixedLengthASCIIParserMapping, errors, entityName, validateForImport,importErrorList);
		}else if(target instanceof RapParserMapping ){
			RapParserMapping rapParserMapping = (RapParserMapping) target;
			validateRapAttributeBasicDetail(rapParserMapping, errors, entityName, validateForImport,importErrorList);
		}else if(target instanceof TapParserMapping ){
			TapParserMapping tapParserMapping = (TapParserMapping) target;
			validateTapAttributeBasicDetail(tapParserMapping, errors, entityName, validateForImport,importErrorList);
		}else if(target instanceof NRTRDEParserMapping ){
			NRTRDEParserMapping nrtrdeParserMapping = (NRTRDEParserMapping) target;
			validateNrtrdeAttributeBasicDetail(nrtrdeParserMapping, errors, entityName, validateForImport,importErrorList);
		}else if(target instanceof FixedLengthBinaryParserMapping){
			FixedLengthBinaryParserMapping fixedLengthBinaryParserMapping=(FixedLengthBinaryParserMapping)target;
			validateFixedLengthBinaryParserBasicDetail(fixedLengthBinaryParserMapping, errors, entityName, validateForImport, importErrorList);
		}else if(target instanceof PDFParserMapping){
			PDFParserMapping pdfParserMapping=(PDFParserMapping)target;
			validatePDFParserBasicDetail(pdfParserMapping, errors, entityName, validateForImport, importErrorList);
		} else if (target instanceof VarLengthAsciiParserMapping) {
			VarLengthAsciiParserMapping varLengthAsciiParser = (VarLengthAsciiParserMapping) target;
			validateVarLengthAsciiParserMappingParamater(varLengthAsciiParser, errors, entityName, validateForImport, importErrorList);
		}	
		
	}

	/**
	 * This method will validate fixed length ASCII parser parameters
	 * @param importErrorList 
	 * @param validateForImport 
	 * @param entityName 
	 * @param errors 
	 * @param fixedLengthASCIIParserMapping 
	 */
	public void validateFixedLengthASCIIParserBasicDetail(FixedLengthASCIIParserMapping fixedLengthASCIIParserMapping, Errors errors, String entityName, boolean validateForImport, List<ImportValidationErrors> importErrorList){
		setErrorObject(errors, validateForImport, importErrorList);
	}
	
	/**
	 * This method will validate fixed length Binary parser parameters
	 * @param importErrorList 
	 * @param validateForImport 
	 * @param entityName 
	 * @param errors 
	 * @param fixedLengthBinaryParserMapping 
	 */
	public void validateFixedLengthBinaryParserBasicDetail(FixedLengthBinaryParserMapping fixedLengthBinaryParserMapping, Errors errors, String entityName, boolean validateForImport, List<ImportValidationErrors> importErrorList){
		setErrorObject(errors, validateForImport, importErrorList);
	}
	
	/**
	 * This method will validate PDF parser parameters
	 * @param importErrorList 
	 * @param validateForImport 
	 * @param entityName 
	 * @param errors 
	 * @param pdfParserMapping 
	 */
	public void validatePDFParserBasicDetail(PDFParserMapping pdfParserMapping, Errors errors, String entityName, boolean validateForImport, List<ImportValidationErrors> importErrorList){
		setErrorObject(errors, validateForImport, importErrorList);
	}
	
	/**
	 * Method will validate parser mapping name property.
	 * 
	 * @param parserMapping
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateParserMappingName(ParserMapping parserMapping, Errors errors, String entityName, boolean validateForImport,
			List<ImportValidationErrors> importErrorList) {
		setErrorObject(errors, validateForImport, importErrorList);
		isValidate(SystemParametersConstant.PARSER_MAPPING_NAME, parserMapping.getName(), "name", entityName, parserMapping.getName(),
				validateForImport);
	}

	/**
	 * Method will validate the option template parameters for parser.
	 * 
	 * @param natflowParser
	 * @param errors
	 */
	private void validationOptionTemplateParams(NATFlowParserMapping natflowParser, Errors errors, String entityName, boolean validateForImport,
			List<ImportValidationErrors> importErrorList) {

		setErrorObject(errors, validateForImport, importErrorList);
		isValidate(SystemParametersConstant.SERVICE_OPTIONTEMPLATEID,natflowParser.getOptionTemplateId(),"optionTemplateId",entityName,natflowParser.getName(),validateForImport);
		isValidate(SystemParametersConstant.SERVICE_OPTIONTEMPLATEKEY,natflowParser.getOptionTemplateKey(),"optionTemplateKey",entityName,natflowParser.getName(),validateForImport);
		isValidate(SystemParametersConstant.SERVICE_OPTIONTEMPLATEVALUE,natflowParser.getOptionTemplateValue(),"optionTemplateValue",entityName,natflowParser.getName(),validateForImport);
		isValidate(SystemParametersConstant.SERVICE_OPTIONCOPYTOTEMPLATEID,natflowParser.getOptionCopytoTemplateId(),"optionCopytoTemplateId",entityName,natflowParser.getName(),validateForImport);
		isValidate(SystemParametersConstant.SERVICE_OPTIONCOPYTOFIELD,natflowParser.getOptionCopyTofield(),"optionCopyTofield",entityName,natflowParser.getName(),validateForImport);
		
		if(natflowParser.isOptionTemplateEnable()){
			if(StringUtils.isEmpty(natflowParser.getOptionTemplateId())){
				setErrorFieldErrorMessage(natflowParser.getOptionTemplateId(), "optionTemplateId",  entityName,natflowParser.getName(), validateForImport, "Service.optionTemplateId.invalid", getMessage("Service.optionTemplateId.invalid"));
			}
			
			if(StringUtils.isEmpty(natflowParser.getOptionTemplateKey())){
				setErrorFieldErrorMessage(natflowParser.getOptionTemplateKey(), "optionTemplateKey",  entityName,natflowParser.getName(), validateForImport, "Service.optionTemplateKey.invalid", getMessage("Service.optionTemplateKey.invalid"));
			}
				
			if(StringUtils.isEmpty(natflowParser.getOptionTemplateValue())){
				setErrorFieldErrorMessage(natflowParser.getOptionTemplateValue(), "optionTemplateValue",  entityName,natflowParser.getName(), validateForImport, "Service.optionTemplateValue.invalid", getMessage("Service.optionTemplateValue.invalid"));
			}
				
			if(StringUtils.isEmpty(natflowParser.getOptionCopytoTemplateId())){
				setErrorFieldErrorMessage(natflowParser.getOptionCopytoTemplateId(), "optionCopytoTemplateId",  entityName,natflowParser.getName(), validateForImport, "Service.optionCopytoTemplateId.invalid", getMessage("Service.optionCopytoTemplateId.invalid"));
			}
			if(StringUtils.isEmpty(natflowParser.getOptionCopyTofield())){
				setErrorFieldErrorMessage(natflowParser.getOptionCopyTofield(), "optionCopyTofield",  entityName,natflowParser.getName(), validateForImport, "Service.optionCopyTofield.invalid", getMessage("Service.optionCopyTofield.invalid"));
			}
		}
		
	}
	
	private void validationFilterParams(NATFlowParserMapping natflowParser, Errors errors, String entityName, boolean validateForImport,
			List<ImportValidationErrors> importErrorList) {

		setErrorObject(errors, validateForImport, importErrorList);
		isValidate(SystemParametersConstant.NATFLOW_PARSER_FILTER_PROTOCOL,natflowParser.getFilterProtocol(),"filterProtocol",entityName,natflowParser.getName(),validateForImport);
		isValidate(SystemParametersConstant.NATFLOW_PARSER_FILTER_TRANSPORT,natflowParser.getFilterTransport(),"filterTransport",entityName,natflowParser.getName(),validateForImport);
		isValidate(SystemParametersConstant.NATFLOW_PARSER_FILTER_PORT,natflowParser.getFilterPort(),"filterPort",entityName,natflowParser.getName(),validateForImport);		
		if(natflowParser.isFilterEnable()){
			if(!StringUtils.hasText(natflowParser.getFilterProtocol())){
				setErrorFieldErrorMessage(natflowParser.getFilterProtocol(), "filterProtocol",  entityName,natflowParser.getName(), validateForImport, "Service.filterProtocol.invalid", getMessage("Service.filterProtocol.invalid"));
			}
			
			if(!StringUtils.hasText(natflowParser.getFilterTransport())){
				setErrorFieldErrorMessage(natflowParser.getFilterTransport(), "filterTransport",  entityName,natflowParser.getName(), validateForImport, "Service.filterTransport.invalid", getMessage("Service.filterTransport.invalid"));
			}
				
			if(!StringUtils.hasText(natflowParser.getFilterPort())){
				setErrorFieldErrorMessage(natflowParser.getFilterPort(), "filterPort",  entityName,natflowParser.getName(), validateForImport, "Service.filterPort.invalid", getMessage("Service.filterPort.invalid"));
			}
		}
		
	}

	/**
	 * validate regex parser basic detail
	 * 
	 * @param regExParser
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	private void validateRegExAttributeBasicDetail(RegexParserMapping regExParser, Errors errors, String entityName, boolean validateForImport,
			List<ImportValidationErrors> importErrorList) {
		setErrorObject(errors, validateForImport, importErrorList);
		if ("Other".equals(regExParser.getDateFormat())) {
			isValidate(SystemParametersConstant.PARSERMAPPING_SRCDATEFORMAT, regExParser.getSrcDateFormat(), "srcDateFormat", entityName,
					regExParser.getName(), validateForImport);
			if (!validateDateFormat(regExParser.getSrcDateFormat())) {
				errors.rejectValue("srcDateFormat", "RegexParserMapping.srcDateFormat.invalid", getMessage("systemParameter.dateFormat.error"));
			}
		}
		validateRegEx(regExParser.getLogPatternRegex(), "logPatternRegex", SystemParametersConstant.REGEXPARSERMAPPING_LOGPATTERNREGEX + ".invalid");
	}

	private void validateAsn1AttributeBasicDetail(ASN1ParserMapping asn1ParserMapping, Errors errors, String entityName, boolean validateForImport,
			List<ImportValidationErrors> importErrorList) {
		setErrorObject(errors, validateForImport, importErrorList);
		if (asn1ParserMapping.getRecMainAttribute() == null || asn1ParserMapping.getRecMainAttribute().trim().length() == 0) {
			errors.rejectValue("recMainAttribute", "Asn1ParserMapping.recordMainAttribute.empty.invalid", getMessage("Asn1ParserMapping.recordMainAttribute.empty.invalid"));
		}else{
			isValidate(SystemParametersConstant.ASN1_PARSER_REC_MAIN_ATTRIBUTE,asn1ParserMapping.getRecMainAttribute(),"recMainAttribute",
				entityName,asn1ParserMapping.getRecMainAttribute(),false);
		}
		if (asn1ParserMapping.isRemoveAddByte() == true)
		{
			String className = asn1ParserMapping.getClass().getName();
			isValidate(SystemParametersConstant.ASN1_PARSER_HEADER_OFFSET_LENGTH,String.valueOf(asn1ParserMapping.getHeaderOffset()),"headerOffset",entityName,className,validateForImport);
			isValidate(SystemParametersConstant.ASN1_PARSER_RECORD_OFFSET_LENGTH,String.valueOf(asn1ParserMapping.getRecOffset()),"recOffset",entityName,className,validateForImport);
		}
		String className = asn1ParserMapping.getClass().getName();
		if (asn1ParserMapping.getRemoveFillers())
		{
			
			if(asn1ParserMapping.getRecordStartIds() == null || asn1ParserMapping.getRecordStartIds().isEmpty()){
				errors.rejectValue("recordStartIds", "ASN1ParserMapping.recordStartIds.invalid", getMessage("ASN1ParserMapping.recordStartIds.invalid"));
			}else{
				asn1ParserMapping.getRecordStartIds().replace(" ", "");
				isValidate(SystemParametersConstant.ASN1_PARSER_RECORD_START_IDS,asn1ParserMapping.getRecordStartIds(),"recordStartIds",entityName,className,validateForImport);
			}
		}
		
		if(asn1ParserMapping.getBufferSize() < SystemParametersConstant.ASN1_PARSER_DEFAULT_BUFFER_SIZE || asn1ParserMapping.getBufferSize() > Integer.MAX_VALUE ) {
			errors.rejectValue("bufferSize", "ASN1ParserMapping.bufferSize.invalid", getMessage("ASN1ParserMapping.bufferSize.invalid"));
		}
		if (asn1ParserMapping.isSkipAttributeMapping()){
			isValidate(SystemParametersConstant.ASN1_PARSER_ROOT_NODE_NAME,asn1ParserMapping.getRootNodeName(),"rootNodeName",entityName,className,validateForImport);
		}
	}
	
	private void validateRapAttributeBasicDetail(RapParserMapping rapParserMapping, Errors errors, String entityName, boolean validateForImport,
			List<ImportValidationErrors> importErrorList) {
		setErrorObject(errors, validateForImport, importErrorList);
		if (rapParserMapping.getRecMainAttribute() == null || rapParserMapping.getRecMainAttribute().trim().length() == 0) {
			errors.rejectValue("recMainAttribute", "RapParserMapping.recMainAttribute.invalid", getMessage("RapParserMapping.recordMainAttribute.invalid"));
		}else{
			isValidate(SystemParametersConstant.ASN1_PARSER_REC_MAIN_ATTRIBUTE,rapParserMapping.getRecMainAttribute(),"recMainAttribute",
				entityName,rapParserMapping.getRecMainAttribute(),false);
		}
		if (rapParserMapping.isRemoveAddByte() == true)
		{
			String className = rapParserMapping.getClass().getName();
			isValidate(SystemParametersConstant.ASN1_PARSER_HEADER_OFFSET_LENGTH,String.valueOf(rapParserMapping.getHeaderOffset()),"headerOffset",entityName,className,validateForImport);
			isValidate(SystemParametersConstant.ASN1_PARSER_RECORD_OFFSET_LENGTH,String.valueOf(rapParserMapping.getRecOffset()),"recOffset",entityName,className,validateForImport);
		}
		String className = rapParserMapping.getClass().getName();
		if (rapParserMapping.getRemoveFillers())
		{
			
			if(rapParserMapping.getRecordStartIds() == null || rapParserMapping.getRecordStartIds().isEmpty()){
				errors.rejectValue("recordStartIds", "ASN1ParserMapping.recordStartIds.invalid", getMessage("ASN1ParserMapping.recordStartIds.invalid"));
			}else{
				rapParserMapping.getRecordStartIds().replace(" ", "");
				isValidate(SystemParametersConstant.ASN1_PARSER_RECORD_START_IDS,rapParserMapping.getRecordStartIds(),"recordStartIds",entityName,className,validateForImport);
			}
		}
		
		if(rapParserMapping.getBufferSize() < SystemParametersConstant.ASN1_PARSER_DEFAULT_BUFFER_SIZE || rapParserMapping.getBufferSize() > Integer.MAX_VALUE ) {
			errors.rejectValue("bufferSize", "ASN1ParserMapping.bufferSize.invalid", getMessage("ASN1ParserMapping.bufferSize.invalid"));
		}
		if (rapParserMapping.isSkipAttributeMapping()){
			isValidate(SystemParametersConstant.ASN1_PARSER_ROOT_NODE_NAME,rapParserMapping.getRootNodeName(),"rootNodeName",entityName,className,validateForImport);
		}
	}
		private void validateTapAttributeBasicDetail(TapParserMapping tapParserMapping, Errors errors, String entityName, boolean validateForImport,
				List<ImportValidationErrors> importErrorList) {
			setErrorObject(errors, validateForImport, importErrorList);
			if (tapParserMapping.getRecMainAttribute() == null || tapParserMapping.getRecMainAttribute().trim().length() == 0) {
				errors.rejectValue("recMainAttribute", "TapParserMapping.recMainAttribute.invalid", getMessage("TapParserMapping.recordMainAttribute.invalid"));
			}else{
				isValidate(SystemParametersConstant.ASN1_PARSER_REC_MAIN_ATTRIBUTE,tapParserMapping.getRecMainAttribute(),"recMainAttribute",
					entityName,tapParserMapping.getRecMainAttribute(),false);
			}
			if (tapParserMapping.isRemoveAddByte() == true)
			{
				String className = tapParserMapping.getClass().getName();
				isValidate(SystemParametersConstant.ASN1_PARSER_HEADER_OFFSET_LENGTH,String.valueOf(tapParserMapping.getHeaderOffset()),"headerOffset",entityName,className,validateForImport);
				isValidate(SystemParametersConstant.ASN1_PARSER_RECORD_OFFSET_LENGTH,String.valueOf(tapParserMapping.getRecOffset()),"recOffset",entityName,className,validateForImport);
			}
			String className = tapParserMapping.getClass().getName();
			if (tapParserMapping.getRemoveFillers())
			{
				
				if(tapParserMapping.getRecordStartIds() == null || tapParserMapping.getRecordStartIds().isEmpty()){
					errors.rejectValue("recordStartIds", "ASN1ParserMapping.recordStartIds.invalid", getMessage("ASN1ParserMapping.recordStartIds.invalid"));
				}else{
					tapParserMapping.getRecordStartIds().replace(" ", "");
					isValidate(SystemParametersConstant.ASN1_PARSER_RECORD_START_IDS,tapParserMapping.getRecordStartIds(),"recordStartIds",entityName,className,validateForImport);
				}
			}
			
			if(tapParserMapping.getBufferSize() < SystemParametersConstant.ASN1_PARSER_DEFAULT_BUFFER_SIZE || tapParserMapping.getBufferSize() > Integer.MAX_VALUE ) {
				errors.rejectValue("bufferSize", "ASN1ParserMapping.bufferSize.invalid", getMessage("ASN1ParserMapping.bufferSize.invalid"));
			}
			if (tapParserMapping.isSkipAttributeMapping()){
				isValidate(SystemParametersConstant.ASN1_PARSER_ROOT_NODE_NAME,tapParserMapping.getRootNodeName(),"rootNodeName",entityName,className,validateForImport);
			}
	}
		private void validateNrtrdeAttributeBasicDetail(NRTRDEParserMapping nrtrdeParserMapping, Errors errors, String entityName, boolean validateForImport,
				List<ImportValidationErrors> importErrorList) {
			setErrorObject(errors, validateForImport, importErrorList);
			if (nrtrdeParserMapping.getRecMainAttribute() == null || nrtrdeParserMapping.getRecMainAttribute().trim().length() == 0) {
				errors.rejectValue("recMainAttribute", "NRTRDEParserMapping.recMainAttribute.invalid", getMessage("NRTRDEParserMapping.recordMainAttribute.invalid"));
			}else{
				isValidate(SystemParametersConstant.ASN1_PARSER_REC_MAIN_ATTRIBUTE,nrtrdeParserMapping.getRecMainAttribute(),"recMainAttribute",
					entityName,nrtrdeParserMapping.getRecMainAttribute(),false);
			}
			if (nrtrdeParserMapping.isRemoveAddByte() == true)
			{
				String className = nrtrdeParserMapping.getClass().getName();
				isValidate(SystemParametersConstant.ASN1_PARSER_HEADER_OFFSET_LENGTH,String.valueOf(nrtrdeParserMapping.getHeaderOffset()),"headerOffset",entityName,className,validateForImport);
				isValidate(SystemParametersConstant.ASN1_PARSER_RECORD_OFFSET_LENGTH,String.valueOf(nrtrdeParserMapping.getRecOffset()),"recOffset",entityName,className,validateForImport);
			}
			String className = nrtrdeParserMapping.getClass().getName();
			if (nrtrdeParserMapping.getRemoveFillers())
			{
				
				if(nrtrdeParserMapping.getRecordStartIds() == null || nrtrdeParserMapping.getRecordStartIds().isEmpty()){
					errors.rejectValue("recordStartIds", "ASN1ParserMapping.recordStartIds.invalid", getMessage("ASN1ParserMapping.recordStartIds.invalid"));
				}else{
					nrtrdeParserMapping.getRecordStartIds().replace(" ", "");
					isValidate(SystemParametersConstant.ASN1_PARSER_RECORD_START_IDS,nrtrdeParserMapping.getRecordStartIds(),"recordStartIds",entityName,className,validateForImport);
				}
			}
			
			if(nrtrdeParserMapping.getBufferSize() < SystemParametersConstant.ASN1_PARSER_DEFAULT_BUFFER_SIZE || nrtrdeParserMapping.getBufferSize() > Integer.MAX_VALUE ) {
				errors.rejectValue("bufferSize", "ASN1ParserMapping.bufferSize.invalid", getMessage("ASN1ParserMapping.bufferSize.invalid"));
			}
			if (nrtrdeParserMapping.isSkipAttributeMapping()){
				isValidate(SystemParametersConstant.ASN1_PARSER_ROOT_NODE_NAME,nrtrdeParserMapping.getRootNodeName(),"rootNodeName",entityName,className,validateForImport);
			}
	}
	/**
	 * Validate Ascii Parser Source Date Format
	 * 
	 * @param target
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateSrcDateFormat(Object target, Errors errors, String entityName, boolean validateForImport,
			List<ImportValidationErrors> importErrorList) {
		ParserMapping parser = (ParserMapping) target;
		if ("Other".equals(parser.getDateFormat())) {
			if(!EngineConstants.UNIX_EPOCH_TIME.equals(parser.getSrcDateFormat())) {
				isValidate(SystemParametersConstant.PARSERMAPPING_SRCDATEFORMAT, parser.getSrcDateFormat(), "srcDateFormat", entityName,
						parser.getName(), validateForImport);
				
				if(parser.getSrcDateFormat()==null || parser.getSrcDateFormat().trim().equals("") ){
							errors.rejectValue("srcDateFormat", "ASN1ParserMapping.srcDateFormat", getMessage("systemParameter.dateFormat.empty"));
				   }
				else{	
				     if(!validateDateFormat(parser.getSrcDateFormat())) {
					     errors.rejectValue("srcDateFormat", "AsciiParserMapping.srcDateFormat.invalid", getMessage("systemParameter.dateFormat.error"));
				    }
				}
			}
		}
	}
	
	/**
	 * Validate Fixed Length Binary Parameters
	 * 
	 * @param target
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateFixedLengthBinaryMappingParamater(FixedLengthBinaryParserMapping fixedLengthBinaryParserMapping, Errors errors, String entityName, boolean validateForImport,
			List<ImportValidationErrors> importErrorList) {
		if(fixedLengthBinaryParserMapping.getRecordLength() == 0 || fixedLengthBinaryParserMapping.getRecordLength() > 99999){
			errors.rejectValue("recordLength", "FixedLengthBinaryParserAttribute.recordLength.invalid", getMessage("FixedLengthBinaryParserAttribute.recordLength.invalid"));
		}
	}
	
	/**
	 * Validate PDF Parameters
	 * 
	 * @param target
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	public void validatePDFMappingParamater(PDFParserMapping pdfParserMapping, Errors errors, String entityName, boolean validateForImport,
			List<ImportValidationErrors> importErrorList) {
	}
	
	/**
	 * Validate Var Length Ascii Parser
	 * 
	 * @param varLengthAsciiParser
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 * @param importErrorList
	 */
	public void validateVarLengthAsciiParserMappingParamater(VarLengthAsciiParserMapping varLengthAsciiParser, Errors errors, String entityName, boolean validateForImport,
			List<ImportValidationErrors> importErrorList) {

		setErrorObject(errors, validateForImport, importErrorList);
		String mappingName = varLengthAsciiParser.getName();
		if (varLengthAsciiParser.getFileTypeEnum().name().equals(BaseConstants.KEY_VALUE_RECORD)) {
			String keyseparator = varLengthAsciiParser.getKeyValueSeparator();
			String fieldseparator = varLengthAsciiParser.getFieldSeparator();
			if ("s".equals(varLengthAsciiParser.getKeyValueSeparator())) {
				keyseparator = varLengthAsciiParser.getKeyValueSeparator().replace("s", " ");
				varLengthAsciiParser.setKeyValueSeparator(keyseparator);
			}
			if ("s".equals(varLengthAsciiParser.getFieldSeparator())) {
				fieldseparator = varLengthAsciiParser.getFieldSeparator().replace("s", " ");
				varLengthAsciiParser.setFieldSeparator(fieldseparator);
			}
			if (!("_".equals(keyseparator))) {
				isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_FIELDSEPARATOR, keyseparator, "keyValueSeparator", entityName, mappingName,
						validateForImport);
			}
			if (!("_".equals(fieldseparator))) {
				isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_FIELDSEPARATOR, fieldseparator, BaseConstants.FIELD_SEPARATOR, entityName, mappingName,
						validateForImport);
			}
		} else if (varLengthAsciiParser.getFileTypeEnum().name().equals(BaseConstants.RECORD_HEADER)) {
			String recordseparator = varLengthAsciiParser.getRecordHeaderSeparator();

			if ("s".equals(varLengthAsciiParser.getRecordHeaderSeparator())) {
				recordseparator = varLengthAsciiParser.getRecordHeaderSeparator().replace("s", " ");
				varLengthAsciiParser.setRecordHeaderSeparator(recordseparator);
			}
			if (!("_".equals(recordseparator))) {
				isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_FIELDSEPARATOR, recordseparator, "recordHeaderSeparator", entityName,
						mappingName, validateForImport);
			}

			boolean isValidRecordHeaderLength = isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_RECORDHEADERLENGTH,
					varLengthAsciiParser.getRecordHeaderLength(), "recordHeaderLength", entityName, mappingName, validateForImport);
			if (isValidRecordHeaderLength
					&& (!(StringUtils.isEmpty(varLengthAsciiParser.getRecordHeaderLength())) && Integer.parseInt(varLengthAsciiParser.getRecordHeaderLength()) > 10000)) {
				setErrorFieldErrorMessage(varLengthAsciiParser.getRecordHeaderLength(), "recordHeaderLength", mappingName, entityName,
						validateForImport, "AsciiParserMapping.recordHeaderLength.invalid",
						getMessage("AsciiParserMapping.recordHeaderLength.invalid"));
			}

		} else if (varLengthAsciiParser.getFileTypeEnum().name().equals(BaseConstants.FILE_HEADER_FOOTER) || varLengthAsciiParser.getFileTypeEnum().name().equals(BaseConstants.DELIMITER)) {
			
				String fieldseparator = varLengthAsciiParser.getFieldSeparator();
				if ("s".equals(varLengthAsciiParser.getFieldSeparator())) {
					fieldseparator = varLengthAsciiParser.getFieldSeparator().replace("s", " ");
					varLengthAsciiParser.setFieldSeparator(fieldseparator);
				}
				if (!("_".equals(fieldseparator))) {
					isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_FIELDSEPARATOR, fieldseparator, BaseConstants.FIELD_SEPARATOR, entityName, mappingName,
							validateForImport);
				}
			
				if (varLengthAsciiParser.getFileFooterEnable()) {
				isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_FILEFOOTERCONTAINS, varLengthAsciiParser.getFileFooterContains(), "fileFooterContains",
						entityName, mappingName, validateForImport);
				}
		} else if(varLengthAsciiParser.getFileTypeEnum().name().equals(BaseConstants.LINEAR_KEY_VALUE_RECORD)){
			
			String keyseparator = varLengthAsciiParser.getKeyValueSeparator();
			if ("s".equals(varLengthAsciiParser.getKeyValueSeparator())) {
				keyseparator = varLengthAsciiParser.getKeyValueSeparator().replace("s", " ");
				varLengthAsciiParser.setKeyValueSeparator(keyseparator);
			}
			if (!("_".equals(keyseparator))) {
				isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_FIELDSEPARATOR, keyseparator, "keyValueSeparator", entityName, mappingName,
						validateForImport);
			}

			isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_RECORDHEADERIDENTIFIER, varLengthAsciiParser.getRecordHeaderIdentifier(), "recordHeaderIdentifier", entityName,mappingName, validateForImport);
			isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_EXCLUDELINESSTART, varLengthAsciiParser.getExcludeLinesStart(), "excludeLinesStart", entityName,mappingName, validateForImport);
			isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_EXCLUDEMINCHARACTERS, varLengthAsciiParser.getExcludeCharactersMin(), "excludeCharactersMin",entityName,mappingName, validateForImport, varLengthAsciiParser.getClass().getName());
			if(isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_EXCLUDEMAXCHARACTERS, varLengthAsciiParser.getExcludeCharactersMax(), "excludeCharactersMax",entityName,mappingName, validateForImport, varLengthAsciiParser.getClass().getName())){
				if(varLengthAsciiParser.getExcludeCharactersMax()<varLengthAsciiParser.getExcludeCharactersMin()){
				setErrorFieldErrorMessage( String.valueOf(varLengthAsciiParser.getExcludeCharactersMax()), "excludeCharactersMax",  entityName,mappingName, validateForImport,  "AsciiParserMapping.excludeMaxCharactersValue.invalid", getMessage("AsciiParserMapping.excludeMaxCharactersValue.invalid"));
				}
			}
		}
		if (isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_FIND, varLengthAsciiParser.getFind(), "find", entityName, mappingName, validateForImport)) {
			boolean isValidParam = isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_REPLACE, varLengthAsciiParser.getReplace(), "replace", entityName,
					mappingName, validateForImport);
			if (isValidParam && (!StringUtils.isEmpty(varLengthAsciiParser.getFind()) && StringUtils.isEmpty(varLengthAsciiParser.getReplace()))) {
				setErrorFieldErrorMessage(varLengthAsciiParser.getReplace(), "replace", mappingName, entityName, validateForImport,
						"AsciiParsermapping.require.replace.string", getMessage("AsciiParsermapping.require.replace.string"));
			}
		}
	}


	/**
	 * Validate Ascii Parser Mapping Parameter
	 * 
	 * @param asciiParser
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateAsciiParserMappingParamater(AsciiParserMapping asciiParser, Errors errors, String entityName, boolean validateForImport,
			List<ImportValidationErrors> importErrorList) {

		setErrorObject(errors, validateForImport, importErrorList);
		String mappingName = asciiParser.getName();
		if (asciiParser.getFileTypeEnum().name().equals(BaseConstants.KEY_VALUE_RECORD)) {
			String keyseparator = asciiParser.getKeyValueSeparator();
			String fieldseparator = asciiParser.getFieldSeparator();
			if ("s".equals(asciiParser.getKeyValueSeparator())) {
				keyseparator = asciiParser.getKeyValueSeparator().replace("s", " ");
				asciiParser.setKeyValueSeparator(keyseparator);
			}
			if ("s".equals(asciiParser.getFieldSeparator())) {
				fieldseparator = asciiParser.getFieldSeparator().replace("s", " ");
				asciiParser.setFieldSeparator(fieldseparator);
			}
			if (!("_".equals(keyseparator))) {
				isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_FIELDSEPARATOR, keyseparator, "keyValueSeparator", entityName, mappingName,
						validateForImport);
			}
			if (!("_".equals(fieldseparator))) {
				isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_FIELDSEPARATOR, fieldseparator, BaseConstants.FIELD_SEPARATOR, entityName, mappingName,
						validateForImport);
			}
		} else if (asciiParser.getFileTypeEnum().name().equals(BaseConstants.RECORD_HEADER)) {
			String recordseparator = asciiParser.getRecordHeaderSeparator();

			if ("s".equals(asciiParser.getRecordHeaderSeparator())) {
				recordseparator = asciiParser.getRecordHeaderSeparator().replace("s", " ");
				asciiParser.setRecordHeaderSeparator(recordseparator);
			}
			if (!("_".equals(recordseparator))) {
				isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_FIELDSEPARATOR, recordseparator, "recordHeaderSeparator", entityName,
						mappingName, validateForImport);
			}

			boolean isValidRecordHeaderLength = isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_RECORDHEADERLENGTH,
					asciiParser.getRecordHeaderLength(), "recordHeaderLength", entityName, mappingName, validateForImport);
			if (isValidRecordHeaderLength
					&& (!(StringUtils.isEmpty(asciiParser.getRecordHeaderLength())) && Integer.parseInt(asciiParser.getRecordHeaderLength()) > 10000)) {
				setErrorFieldErrorMessage(asciiParser.getRecordHeaderLength(), "recordHeaderLength", mappingName, entityName,
						validateForImport, "AsciiParserMapping.recordHeaderLength.invalid",
						getMessage("AsciiParserMapping.recordHeaderLength.invalid"));
			}

		} else if (asciiParser.getFileTypeEnum().name().equals(BaseConstants.FILE_HEADER_FOOTER) || asciiParser.getFileTypeEnum().name().equals(BaseConstants.DELIMITER)) {
			
				String fieldseparator = asciiParser.getFieldSeparator();
				if ("s".equals(asciiParser.getFieldSeparator())) {
					fieldseparator = asciiParser.getFieldSeparator().replace("s", " ");
					asciiParser.setFieldSeparator(fieldseparator);
				}
				if (!("_".equals(fieldseparator))) {
					isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_FIELDSEPARATOR, fieldseparator, BaseConstants.FIELD_SEPARATOR, entityName, mappingName,
							validateForImport);
				}
			
				if (asciiParser.getFileFooterEnable()) {
				isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_FILEFOOTERCONTAINS, asciiParser.getFileFooterContains(), "fileFooterContains",
						entityName, mappingName, validateForImport);
				}
		} else if(asciiParser.getFileTypeEnum().name().equals(BaseConstants.LINEAR_KEY_VALUE_RECORD)){
			
			String keyseparator = asciiParser.getKeyValueSeparator();
			if ("s".equals(asciiParser.getKeyValueSeparator())) {
				keyseparator = asciiParser.getKeyValueSeparator().replace("s", " ");
				asciiParser.setKeyValueSeparator(keyseparator);
			}
			if (!("_".equals(keyseparator))) {
				isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_FIELDSEPARATOR, keyseparator, "keyValueSeparator", entityName, mappingName,
						validateForImport);
			}

			isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_RECORDHEADERIDENTIFIER, asciiParser.getRecordHeaderIdentifier(), "recordHeaderIdentifier", entityName,mappingName, validateForImport);
			isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_EXCLUDELINESSTART, asciiParser.getExcludeLinesStart(), "excludeLinesStart", entityName,mappingName, validateForImport);
			isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_EXCLUDEMINCHARACTERS, asciiParser.getExcludeCharactersMin(), "excludeCharactersMin",entityName,mappingName, validateForImport, asciiParser.getClass().getName());
			if(isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_EXCLUDEMAXCHARACTERS, asciiParser.getExcludeCharactersMax(), "excludeCharactersMax",entityName,mappingName, validateForImport, asciiParser.getClass().getName())){
				if(asciiParser.getExcludeCharactersMax()<asciiParser.getExcludeCharactersMin()){
				setErrorFieldErrorMessage( String.valueOf(asciiParser.getExcludeCharactersMax()), "excludeCharactersMax",  entityName,mappingName, validateForImport,  "AsciiParserMapping.excludeMaxCharactersValue.invalid", getMessage("AsciiParserMapping.excludeMaxCharactersValue.invalid"));
				}
			}
		}
		if (isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_FIND, asciiParser.getFind(), "find", entityName, mappingName, validateForImport)) {
			boolean isValidParam = isValidate(SystemParametersConstant.ASCIIPARSERMAPPING_REPLACE, asciiParser.getReplace(), "replace", entityName,
					mappingName, validateForImport);
			if (isValidParam && (!StringUtils.isEmpty(asciiParser.getFind()) && StringUtils.isEmpty(asciiParser.getReplace()))) {
				setErrorFieldErrorMessage(asciiParser.getReplace(), "replace", mappingName, entityName, validateForImport,
						"AsciiParsermapping.require.replace.string", getMessage("AsciiParsermapping.require.replace.string"));
			}
		}
	}

	
	/**
	 * Method will validate imported mapping parameters for all parser plug-ins
	 * 
	 * @param parserMapping
	 * @param entityName
	 * @param validateForImport
	 * @param importErrorList
	 */
	public void validateImportedMappingParameters(ParserMapping parserMapping, String entityName, boolean validateForImport,
			List<ImportValidationErrors> importErrorList) {

		validateParserMappingName(parserMapping, errors, entityName, validateForImport, importErrorList);

		if (parserMapping instanceof NATFlowParserMapping) {
			NATFlowParserMapping natflowParser = (NATFlowParserMapping) parserMapping;
			if(natflowParser.getSkipAttributeForValidation() != null && !natflowParser.getSkipAttributeForValidation().isEmpty()){
				isValidate(SystemParametersConstant.ASN1_PARSER_RECORD_START_IDS,natflowParser.getSkipAttributeForValidation().trim(),"skipAttributeForValidation",entityName,natflowParser.getName(),validateForImport);
			}
			validationOptionTemplateParams(natflowParser, errors, entityName, validateForImport, importErrorList); 
			validationFilterParams(natflowParser, errors, entityName, validateForImport, importErrorList);
		} else if (parserMapping instanceof NatflowASN1ParserMapping) {
			NatflowASN1ParserMapping natflowParser = (NatflowASN1ParserMapping) parserMapping;
			validationOptionTemplateParams(natflowParser, errors, entityName, validateForImport, importErrorList); // Method
																													// parameters.
		} else if (parserMapping instanceof AsciiParserMapping) {
			AsciiParserMapping asciiParser = (AsciiParserMapping) parserMapping;
			validateAsciiParserMappingParamater(asciiParser, errors, entityName, validateForImport, importErrorList);
		
		} else if (parserMapping instanceof RegexParserMapping) {
			RegexParserMapping regExParser = (RegexParserMapping) parserMapping;
			validateRegExAttributeBasicDetail(regExParser, errors, entityName, validateForImport, importErrorList);
		}

	}
	public void validateParserGroupAttributeName(ParserGroupAttribute parserGroupAttribute,Errors errors,String entityName,boolean validateForImport){
		this.errors=errors;
		isValidate(SystemParametersConstant.GROUP_ATTRIBUTE_NAME,parserGroupAttribute.getName(),"name",entityName,parserGroupAttribute.getName(),validateForImport);
	}

}
