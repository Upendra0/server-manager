package com.elitecore.sm.composer.validator;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.model.DataTypeEnum;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.composer.model.ASCIIComposerAttr;
import com.elitecore.sm.composer.model.ASN1ComposerAttribute;
import com.elitecore.sm.composer.model.ComposerAttribute;
import com.elitecore.sm.composer.model.FixedLengthASCIIComposerAttribute;
import com.elitecore.sm.composer.model.NRTRDEComposerAttribute;
import com.elitecore.sm.composer.model.RAPComposerAttribute;
import com.elitecore.sm.composer.model.RoamingComposerAttribute;
import com.elitecore.sm.composer.model.TAPComposerAttribute;
import com.elitecore.sm.composer.model.XMLComposerAttribute;
import com.elitecore.sm.composer.service.ComposerAttributeService;

/**
 * 
 * @author avani.panchal
 *
 */
@Component
public class ComposerAttributeValidator extends BaseValidator{
	
	@Autowired
	ComposerAttributeService composerAttributeService;
	
	/**
	 * Method will check current validator class
	 *@param  Class<?> clazz
	 *@return boolean 
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ComposerAttribute.class.isAssignableFrom(clazz);
	}
	
	/**
	 * Method will validate all basic composer attribute parameter.
	 * @param target
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateComposerAttributeParameter(Object target, Errors errors,String entityName,boolean validateForImport,  List<ImportValidationErrors> importErrorList){
		
		ComposerAttribute composerAttr = (ComposerAttribute)target;
		
		setErrorObject(errors, validateForImport, importErrorList);
		
		isValidate(SystemParametersConstant.COMPOSERATTRIBUTE_DEFAULTVALUE,composerAttr.getDefualtValue(),"defualtValue",entityName,composerAttr.getDefualtValue(),validateForImport);
		//isValidate(SystemParametersConstant.COMPOSERATTRIBUTE_DESCRIPATION,composerAttr.getDescription(),"description",entityName,composerAttr.getDescription(),validateForImport);
		if(!validateForImport && !((target instanceof ASN1ComposerAttribute) || (target instanceof RoamingComposerAttribute))){
			validateSequenceNumber(composerAttr,errors);
		}
		if(DataTypeEnum.DATE.equals(composerAttr.getDataType())&& StringUtils.isNotBlank(composerAttr.getDateFormat())){
			isValidate(SystemParametersConstant.COMPOSERATTRIBUTE_DATEFORMAT,composerAttr.getDateFormat(),"dateFormat",entityName,composerAttr.getDateFormat(),validateForImport);	
		}
		isValidate(SystemParametersConstant.COMPOSERATTRIBUTE_TRIMCHARS,composerAttr.getTrimchars(),"trimchars",entityName,composerAttr.getTrimchars(),validateForImport);
		if(target instanceof FixedLengthASCIIComposerAttribute){
			logger.debug("Fixed Length Ascii Compsoer found");
			FixedLengthASCIIComposerAttribute fixedLengthASCIIComposerAttribute = (FixedLengthASCIIComposerAttribute) target;
			if(fixedLengthASCIIComposerAttribute.getSequenceNumber() <= 0){
				errors.rejectValue("sequenceNumber", "fixedLengthASCIIParser.sequenceNumber.invalid", getMessage("fixedLengthASCIIParser.sequenceNumber.invalid"));
			}
			isValidate(SystemParametersConstant.FIXEDLENGTH_ASCII_COMPOSER_SEQNUMBER,String.valueOf(fixedLengthASCIIComposerAttribute.getSequenceNumber()),"sequenceNumber",entityName,String.valueOf(fixedLengthASCIIComposerAttribute.getSequenceNumber()),validateForImport);
			if(StringUtils.isNotBlank(fixedLengthASCIIComposerAttribute.getPrefix()))
				isValidate(SystemParametersConstant.ASCIICOMPOSERATTRIBUTE_PREFIX,fixedLengthASCIIComposerAttribute.getPrefix(),"prefix",entityName,fixedLengthASCIIComposerAttribute.getPrefix(),validateForImport);
			if(StringUtils.isNotBlank(fixedLengthASCIIComposerAttribute.getSuffix()))
				isValidate(SystemParametersConstant.ASCIICOMPOSERATTRIBUTE_SUFFIX,fixedLengthASCIIComposerAttribute.getSuffix(),"suffix",entityName,fixedLengthASCIIComposerAttribute.getSuffix(),validateForImport);
			if(fixedLengthASCIIComposerAttribute.getFixedLength() <= 0)
				errors.rejectValue("fixedLength", "FixedLengthASCIIComposerAttribute.fixedLength.invalid", getMessage("FixedLengthASCIIComposerAttribute.fixedLength.invalid"));
			else 
				isValidate(SystemParametersConstant.FIXEDLENGTH_ASCII_COMPOSER_LENGTH,String.valueOf(fixedLengthASCIIComposerAttribute.getFixedLength()),"fixedLength",entityName,String.valueOf(fixedLengthASCIIComposerAttribute.getFixedLength()),validateForImport);
			isValidate(SystemParametersConstant.ASCIICOMPOSERATTRIBUTE_PADDINGCHAR,fixedLengthASCIIComposerAttribute.getPaddingChar(),"paddingChar",entityName,fixedLengthASCIIComposerAttribute.getPaddingChar(),validateForImport);
			if(DataTypeEnum.DATE.equals(fixedLengthASCIIComposerAttribute.getDataType())){
				isValidate(SystemParametersConstant.FIXEDLENGTH_ASCII_COMPOSER_DATEFORMAT,fixedLengthASCIIComposerAttribute.getFixedLengthDateFormat(),"fixedLengthDateFormat",entityName,fixedLengthASCIIComposerAttribute.getFixedLengthDateFormat(),validateForImport);
			}
			if(!StringUtils.isEmpty(fixedLengthASCIIComposerAttribute.getUnifiedField()))
				isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,fixedLengthASCIIComposerAttribute.getUnifiedField(),"unifiedField",entityName,fixedLengthASCIIComposerAttribute.getUnifiedField(),validateForImport);
		}
		else if(target  instanceof ASCIIComposerAttr){
			logger.debug("Ascii Composer Attribute Found");
			ASCIIComposerAttr asciiComposerAttr=(ASCIIComposerAttr)target;
			
			isValidate(SystemParametersConstant.ASCIICOMPOSERATTRIBUTE_REPALCECONDITIONLIST,asciiComposerAttr.getReplaceConditionList(),"replaceConditionList",entityName,asciiComposerAttr.getReplaceConditionList(),validateForImport);
			isValidate(SystemParametersConstant.COMPOSERATTRIBUTE_DESTINATIONFIELD,composerAttr.getDestinationField(),"destinationField",entityName,composerAttr.getDestinationField(),validateForImport);
			if(asciiComposerAttr.isPaddingEnable()){
				isValidate(SystemParametersConstant.ASCIICOMPOSERATTRIBUTE_LENGTH,String.valueOf(asciiComposerAttr.getLength()),"length",entityName,String.valueOf(asciiComposerAttr.getLength()),validateForImport);				
				isValidate(SystemParametersConstant.ASCIICOMPOSERATTRIBUTE_PADDINGCHAR,asciiComposerAttr.getPaddingChar(),"paddingChar",entityName,asciiComposerAttr.getPaddingChar(),validateForImport);
				if(StringUtils.isNotBlank(asciiComposerAttr.getPrefix())){
					isValidate(SystemParametersConstant.ASCIICOMPOSERATTRIBUTE_PREFIX,asciiComposerAttr.getPrefix(),"prefix",entityName,asciiComposerAttr.getPrefix(),validateForImport);
				}
				if(StringUtils.isNotBlank(asciiComposerAttr.getSuffix())){
					isValidate(SystemParametersConstant.ASCIICOMPOSERATTRIBUTE_SUFFIX,asciiComposerAttr.getSuffix(),"suffix",entityName,asciiComposerAttr.getSuffix(),validateForImport);
				}
			}
			if(!StringUtils.isEmpty(asciiComposerAttr.getUnifiedField()))
				isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,asciiComposerAttr.getUnifiedField(),"unifiedField",entityName,asciiComposerAttr.getUnifiedField(),validateForImport);
		}
		
		else if(target instanceof ASN1ComposerAttribute){
			logger.debug("ASN1 Composer Attribute found");
			ASN1ComposerAttribute asn1ComposerAttribute = (ASN1ComposerAttribute)target;
			isValidate(SystemParametersConstant.ASN1_COMPOSER_ASN1_DATA_TYPE,asn1ComposerAttribute.getasn1DataType(),"asn1DataType",entityName,asn1ComposerAttribute.getasn1DataType(),validateForImport);
			isValidate(SystemParametersConstant.ASN1_COMPOSER_CHOICE_ID,asn1ComposerAttribute.getChoiceId(), "choiceId", entityName,asn1ComposerAttribute.getChoiceId(),validateForImport);
			isValidate(SystemParametersConstant.ASN1_COMPOSER_CHILD_ATTRIBUTES, asn1ComposerAttribute.getChildAttributes(), "childAttributes", entityName,asn1ComposerAttribute.getChildAttributes(),validateForImport);
			//Added condition for MEDSUP-1711
			if(!StringUtils.isEmpty(asn1ComposerAttribute.getUnifiedField()))
			    isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,asn1ComposerAttribute.getUnifiedField(),"unifiedField",entityName,asn1ComposerAttribute.getUnifiedField(),validateForImport);
		}
		
		else if(target instanceof RoamingComposerAttribute){
			if(target instanceof TAPComposerAttribute){
				logger.debug("TAP Composer Attribute found");
			}else if(target instanceof RAPComposerAttribute){
				logger.debug("RAP Composer Attribute found");
			}else if(target instanceof NRTRDEComposerAttribute){
				logger.debug("NRTRDE Composer Attribute found");
			}
			RoamingComposerAttribute roamingComposerAttribute = (RoamingComposerAttribute)target;
			isValidate(SystemParametersConstant.ASN1_COMPOSER_ASN1_DATA_TYPE,roamingComposerAttribute.getasn1DataType(),"asn1DataType",entityName,roamingComposerAttribute.getasn1DataType(),validateForImport);
			isValidate(SystemParametersConstant.ASN1_COMPOSER_CHOICE_ID,roamingComposerAttribute.getChoiceId(), "choiceId", entityName,roamingComposerAttribute.getChoiceId(),validateForImport);
			isValidate(SystemParametersConstant.ASN1_COMPOSER_CHILD_ATTRIBUTES, roamingComposerAttribute.getChildAttributes(), "childAttributes", entityName,roamingComposerAttribute.getChildAttributes(),validateForImport);
		}
		else if(target instanceof XMLComposerAttribute){
			XMLComposerAttribute xmlComposerAttribute = (XMLComposerAttribute)target;
			if(!StringUtils.isEmpty(xmlComposerAttribute.getUnifiedField()))
				isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,xmlComposerAttribute.getUnifiedField(),"unifiedField",entityName,xmlComposerAttribute.getUnifiedField(),validateForImport);
		}
	}


	private void validateSequenceNumber(ComposerAttribute composerAttr, Errors errors) {
		if(composerAttr.getSequenceNumber() <= 0 ){
			errors.rejectValue("sequenceNumber", "fixedLengthASCIIParser.sequenceNumber.invalid", getMessage("fixedLengthASCIIParser.sequenceNumber.invalid"));
			return;
		}
		List<Integer> composerAttributeSeq = composerAttributeService.getAllSequenceNumbers(composerAttr.getMyComposer().getId(),composerAttr.getId());
		if(composerAttributeSeq!=null && !composerAttributeSeq.isEmpty() && composerAttributeSeq.contains((Integer)composerAttr.getSequenceNumber())){
				errors.rejectValue("sequenceNumber", "sequence.number.duplicate", getMessage("sequence.number.duplicate"));
				return;
			}
		return;
	}
}
	


