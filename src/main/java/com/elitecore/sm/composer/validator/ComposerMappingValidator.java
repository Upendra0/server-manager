package com.elitecore.sm.composer.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.composer.model.ASCIIComposerMapping;
import com.elitecore.sm.composer.model.ASN1ComposerMapping;
import com.elitecore.sm.composer.model.ComposerGroupAttribute;
import com.elitecore.sm.composer.model.ComposerMapping;
import com.elitecore.sm.composer.model.FixedLengthASCIIComposerMapping;
import com.elitecore.sm.composer.model.RoamingComposerMapping;
import com.elitecore.sm.composer.model.XMLComposerMapping;

/**
 * 
 * @author avani.panchal
 *
 */
@Component
public class ComposerMappingValidator extends BaseValidator{
	
	/**
	 * Method will check current validator class
	 *@param  Class<?> clazz
	 *@return boolean 
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ComposerMapping.class.isAssignableFrom(clazz);
	}
	
	/**
	 * Method will validate composer mapping name property.
	 * @param parserMapping
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateComposerMappingName(ComposerMapping composerMapping, Errors errors, String entityName, boolean validateForImport){
		this.errors = errors;
		isValidate(SystemParametersConstant.COMPOSER_MAPPING_NAME,composerMapping.getName(),"name",entityName,composerMapping.getName(),validateForImport);
	}
	
	/**
	 * Method will validate All Parameter for parser configuration , based on parser type
	 * @param target
	 * @param errors
	 */
	public void validateComposerMappingParameter(Object target, Errors errors,String entityName,boolean validateForImport){
		this.errors = errors;
		logger.debug("inside validateComposerMappingParameter");
		
		
		if(target instanceof FixedLengthASCIIComposerMapping){
			FixedLengthASCIIComposerMapping fixedLengthASCIIComposerMapping = (FixedLengthASCIIComposerMapping)target;
			validateFixedLengthAsciiComposerMappingParamater(fixedLengthASCIIComposerMapping,entityName,validateForImport);
		} 
		else if (target instanceof ASCIIComposerMapping) {
			ASCIIComposerMapping asciiComposer = (ASCIIComposerMapping)target;
			validateAsciiComposerMappingParamater(asciiComposer,entityName,validateForImport);
		}
		else if(target instanceof ASN1ComposerMapping){
			ASN1ComposerMapping asn1ComposerMapping = (ASN1ComposerMapping)target;
			validateAsn1ComposerMappingParameter(asn1ComposerMapping,entityName,validateForImport);
		}
		else if(target instanceof RoamingComposerMapping){
			RoamingComposerMapping roamingComposerMapping = (RoamingComposerMapping)target;
			validateRoamingComposerMappingParameter(roamingComposerMapping,entityName,validateForImport);
		}
	}
	
	private void validateAsn1ComposerMappingParameter(ASN1ComposerMapping asn1ComposerMapping, String entityName,
			boolean validateForImport) {
		
		isValidate(SystemParametersConstant.ASN1_REC_MAIN_ATTRIBUTE,asn1ComposerMapping.getRecMainAttribute(),"recMainAttribute",entityName,asn1ComposerMapping.getRecMainAttribute(),validateForImport);
		isValidate(SystemParametersConstant.ASN1_MULTI_CONTAINER_DELIMITER, asn1ComposerMapping.getMultiContainerDelimiter(), "multiContainerDelimiter", entityName,asn1ComposerMapping.getMultiContainerDelimiter(),validateForImport);
	}
	
	private void validateRoamingComposerMappingParameter(RoamingComposerMapping roamingComposerMapping, String entityName,
			boolean validateForImport) {
		
		isValidate(SystemParametersConstant.ASN1_REC_MAIN_ATTRIBUTE,roamingComposerMapping.getRecMainAttribute(),"recMainAttribute",entityName,roamingComposerMapping.getRecMainAttribute(),validateForImport);
		isValidate(SystemParametersConstant.ASN1_MULTI_CONTAINER_DELIMITER, roamingComposerMapping.getMultiContainerDelimiter(), "multiContainerDelimiter", entityName,roamingComposerMapping.getMultiContainerDelimiter(),validateForImport);
	}

	/**
	 * Validate Ascii Composer Mapping Parameter
	 * @param asciiComposer
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
public void validateAsciiComposerMappingParamater(ASCIIComposerMapping asciiComposer,String entityName,boolean validateForImport){
		
		
	
				String fieldseparator=asciiComposer.getFieldSeparator();
				logger.debug("validating the ASCII Composer Fiedl Seperator with value = "+fieldseparator);
				if(fieldseparator.startsWith("s")){
                     fieldseparator=fieldseparator.replaceAll("s"," ");
                     asciiComposer.setFieldSeparator(fieldseparator);
                }
				if(!("_".equals(fieldseparator) || "\\|".equals(fieldseparator))){
					isValidate(SystemParametersConstant.ASCIICOMPOSERMAPPING_FIELDSEPARATOR,fieldseparator,"fieldSeparator",entityName,asciiComposer.getName(),validateForImport);
				}
				
				if(asciiComposer.isFileHeaderSummaryEnable() && (asciiComposer.getFileHeaderSummary() == null || asciiComposer.getFileHeaderSummary().length() == 0)){
					errors.rejectValue("fileHeaderSummary", "ComposerMapping.header.summary.empty.invalid", getMessage("ComposerMapping.header.summary.empty.invalid"));
				}
				
				if(asciiComposer.isFileFooterSummaryEnable() && (asciiComposer.getFileFooterSummary() == null || asciiComposer.getFileFooterSummary().length() == 0)){
					errors.rejectValue("fileFooterSummary", "ComposerMapping.footer.summary.empty.invalid", getMessage("ComposerMapping.footer.summary.empty.invalid"));
				}
	}

	public void validateFixedLengthAsciiComposerMappingParamater(FixedLengthASCIIComposerMapping asciiComposer,String entityName,boolean validateForImport){
	
	if(asciiComposer.isFileHeaderSummaryEnable() && (asciiComposer.getFileHeaderSummary() == null || asciiComposer.getFileHeaderSummary().length() == 0)){
			errors.rejectValue("fileHeaderSummary", "ComposerMapping.header.summary.empty.invalid", getMessage("ComposerMapping.header.summary.empty.invalid"));
		}
		
		if(asciiComposer.isFileFooterSummaryEnable() && (asciiComposer.getFileFooterSummary() == null || asciiComposer.getFileFooterSummary().length() == 0)){
			errors.rejectValue("fileFooterSummary", "ComposerMapping.footer.summary.empty.invalid", getMessage("ComposerMapping.footer.summary.empty.invalid"));
		}
	}
	
	
	/**
	 * Validate Ascii composer basic detail
	 * @param target
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateBasicDetail(Object target, Errors errors,String entityName,boolean validateForImport){
		this.errors = errors;
		ComposerMapping composerMapping = (ComposerMapping)target;
		if("Other".equals(composerMapping.getDateFormatEnum())){
			isValidate(SystemParametersConstant.COMPOSERMAPPING_DESTDATEFORMAT,composerMapping.getDestDateFormat(),"destDateFormat",entityName,composerMapping.getName(),validateForImport);
		}
		isValidate(SystemParametersConstant.COMPOSERMAPPING_DESTFILEEXTENSION,composerMapping.getDestFileExt(),"destFileExt",entityName,composerMapping.getName(),validateForImport);
	}

	
	
	/**
	 * Method will validate imported mapping parameters for all parser plug-ins
	 * @param parserMapping
	 * @param entityName
	 * @param validateForImport
	 * @param importErrorList
	 */
	public void validateImportedComposerMappingParameters(ComposerMapping composerMapping, String entityName, boolean validateForImport, List<ImportValidationErrors> importErrorList){
		setErrorObject(errors, validateForImport, importErrorList);
		
		validateComposerMappingName(composerMapping, errors, entityName, validateForImport); // Method will validate the name for composer mapping.
		
		if (composerMapping instanceof ASCIIComposerMapping) { 
			ASCIIComposerMapping asciiComposer = (ASCIIComposerMapping)composerMapping;
			validateAsciiComposerMappingParamater(asciiComposer,entityName,validateForImport);
		}
	} 
	
	public void validateXMLBasicDetail(Object target, Errors errors,String entityName,boolean validateForImport){
		this.errors = errors;
		XMLComposerMapping xmlComposer = (XMLComposerMapping)target;
		if("Other".equals(xmlComposer.getDateFormatEnum())){
			isValidate(SystemParametersConstant.COMPOSERMAPPING_DESTDATEFORMAT,xmlComposer.getDestDateFormat(),"destDateFormat",entityName,xmlComposer.getName(),validateForImport);
		}
		
		isValidate(SystemParametersConstant.COMPOSERMAPPING_DESTFILEEXTENSION,xmlComposer.getDestFileExt(),"destFileExt",entityName,xmlComposer.getName(),validateForImport);
	}
	
	public void validateComposerGroupAttributeName(ComposerGroupAttribute composerGroupAttribute, Errors errors, String entityName, boolean validateForImport){
		this.errors = errors;
		isValidate(SystemParametersConstant.COMPOSER_MAPPING_NAME,composerGroupAttribute.getName(),"name",entityName,composerGroupAttribute.getName(),validateForImport);
	}
	
}
