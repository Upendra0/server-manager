/**
 * 
 */
package com.elitecore.sm.device.validator;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.parser.model.ASN1ParserAttribute;
import com.elitecore.sm.parser.model.NatFlowParserAttribute;
import com.elitecore.sm.parser.model.AsciiParserAttribute;
import com.elitecore.sm.parser.model.FixedLengthASCIIParserAttribute;
import com.elitecore.sm.parser.model.FixedLengthBinaryParserAttribute;
import com.elitecore.sm.parser.model.HtmlParserAttribute;
import com.elitecore.sm.parser.model.MTSiemensParserAttribute;
import com.elitecore.sm.parser.model.NRTRDEParserAttribute;
import com.elitecore.sm.parser.model.PDFParserAttribute;
import com.elitecore.sm.parser.model.ParserAttribute;
import com.elitecore.sm.parser.model.RAPParserAttribute;
import com.elitecore.sm.parser.model.RegexParserAttribute;
import com.elitecore.sm.parser.model.TAPParserAttribute;
import com.elitecore.sm.parser.model.VarLengthAsciiParserAttribute;
import com.elitecore.sm.parser.model.VarLengthBinaryParserAttribute;
import com.elitecore.sm.parser.model.XMLParserAttribute;
import com.elitecore.sm.parser.model.XlsParserAttribute;
import com.elitecore.sm.common.model.DataTypeEnum;

/**
 * @author Ranjitsinh Reval
 *
 */
@Component
public class ParserAttributeValidator extends BaseValidator {

	
	/**
	 * Method will check current validator class
	 *@param  Class<?> clazz
	 *@return boolean 
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return ParserAttribute.class.isAssignableFrom(clazz);
	}
	
	/**
	 * Method will validate all basic parser attribute parameter.
	 * @param target
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateParserAttributeParameter(Object target, Errors errors,String entityName,boolean validateForImport, List<ImportValidationErrors> importErrorList){
		ParserAttribute parserAttr = (ParserAttribute)target;
	
		setErrorObject(errors, validateForImport, importErrorList);
		
		if(!(target  instanceof RegexParserAttribute)&&!(target  instanceof FixedLengthASCIIParserAttribute)&&!(target instanceof FixedLengthBinaryParserAttribute)&&!(target  instanceof AsciiParserAttribute) &&!(target  instanceof HtmlParserAttribute) &&	!(target  instanceof PDFParserAttribute) &&
				!(target  instanceof XlsParserAttribute) && !(target  instanceof VarLengthAsciiParserAttribute) &&
				!(target  instanceof VarLengthBinaryParserAttribute)&&!(target  instanceof XMLParserAttribute)){
			isValidate(SystemParametersConstant.ATTR_SOURCE_FIELD,parserAttr.getSourceField(),"sourceField",entityName,parserAttr.getSourceField(),validateForImport);	
		}
		
		// To allow 200 characters for Source field in ASCII Parser and other validations
		if(target  instanceof AsciiParserAttribute){
			logger.debug("ASCII Parser Attribute Found");
			try{
				isValidate(SystemParametersConstant.ATTR_ASCII_SOURCE_FIELD,parserAttr.getSourceField(),"sourceField",entityName,parserAttr.getSourceField(),validateForImport);
				isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,parserAttr.getUnifiedField(),"unifiedField",entityName,parserAttr.getUnifiedField(),validateForImport);
				AsciiParserAttribute asciiParserAttribute = (AsciiParserAttribute)parserAttr;
				if(!StringUtils.isEmpty(asciiParserAttribute.getPortUnifiedField())){
					isValidate(SystemParametersConstant.ATTR_ASCII_PORT_UNIFIED_FIELD,asciiParserAttribute.getPortUnifiedField(),"portUnifiedField",entityName,parserAttr.getUnifiedField(),validateForImport);
					if(asciiParserAttribute.getPortUnifiedField().equalsIgnoreCase(asciiParserAttribute.getUnifiedField())){
						setErrorFieldErrorMessage(asciiParserAttribute.getPortUnifiedField() ,"portUnifiedField", asciiParserAttribute.getPortUnifiedField(), entityName, validateForImport,  "AsciiParserAttribute.unifiedField.ipPortUnifiedField.same.invalid", getMessage("AsciiParserAttribute.unifiedField.ipPortUnifiedField.same.invalid"));
					}
					isValidate(SystemParametersConstant.ATTR_ASCII_IP_PORT_SEPERATOR,asciiParserAttribute.getIpPortSeperator(),"ipPortSeperator",entityName,asciiParserAttribute.getIpPortSeperator(),validateForImport);
				} else if(StringUtils.isEmpty(asciiParserAttribute.getPortUnifiedField()) && !StringUtils.isEmpty(asciiParserAttribute.getIpPortSeperator())){
					setErrorFieldErrorMessage(asciiParserAttribute.getIpPortSeperator() ,"ipPortSeperator", asciiParserAttribute.getIpPortSeperator(), entityName, validateForImport,  "AsciiParserAttribute.empty.ipPortUnifiedfield.ipPortSeperator.invalid", getMessage("AsciiParserAttribute.empty.ipPortUnifiedfield.ipPortSeperator.invalid"));
				}
			} catch(ClassCastException castException){}//NOSONAR
		}
		if(!(target  instanceof AsciiParserAttribute ||
				target  instanceof ASN1ParserAttribute ||
				target  instanceof XMLParserAttribute ||
				target  instanceof FixedLengthASCIIParserAttribute ||
				target  instanceof FixedLengthBinaryParserAttribute ||
				target  instanceof RAPParserAttribute ||
				target  instanceof TAPParserAttribute ||
				target  instanceof NRTRDEParserAttribute ||
				target  instanceof HtmlParserAttribute ||
				target  instanceof PDFParserAttribute ||
				target  instanceof XlsParserAttribute ||
				target  instanceof VarLengthAsciiParserAttribute ||
				target  instanceof VarLengthBinaryParserAttribute ||
				target  instanceof RegexParserAttribute) || (target  instanceof FixedLengthASCIIParserAttribute) || (target instanceof FixedLengthBinaryParserAttribute)|| (target instanceof XMLParserAttribute)|| (target instanceof RegexParserAttribute)|| (target instanceof MTSiemensParserAttribute)){
			isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,parserAttr.getUnifiedField(),"unifiedField",entityName,parserAttr.getUnifiedField(),validateForImport);
			
		}
		
		if(!((target  instanceof AsciiParserAttribute) ||
				(target  instanceof ASN1ParserAttribute) ||
				(target  instanceof XMLParserAttribute) ||
				(target  instanceof FixedLengthASCIIParserAttribute) ||
				(target  instanceof FixedLengthBinaryParserAttribute) ||
				(target  instanceof RAPParserAttribute) ||
				(target  instanceof TAPParserAttribute) ||
				(target  instanceof NRTRDEParserAttribute) ||
				(target  instanceof HtmlParserAttribute) ||
				(target  instanceof PDFParserAttribute) ||
				(target  instanceof XlsParserAttribute) ||
				(target  instanceof VarLengthAsciiParserAttribute) ||
				(target  instanceof VarLengthBinaryParserAttribute) ||
				(target  instanceof RegexParserAttribute) ||
				(target  instanceof MTSiemensParserAttribute))){
			isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,parserAttr.getUnifiedField(),"unifiedField",entityName,parserAttr.getUnifiedField(),validateForImport);
		}
		
		// MED-7836 Removing validation for TrimCharacter
		//isValidate(SystemParametersConstant.ATTR_TRIM_CHARS,parserAttr.getTrimChars(),"trimChars",entityName,parserAttr.getTrimChars(),validateForImport);
		if(parserAttr.getTrimChars() != null && parserAttr.getTrimChars().trim().isEmpty())
				parserAttr.setTrimChars("");
		if(!(target  instanceof HtmlParserAttribute) &&	!(target  instanceof PDFParserAttribute) &&
				!(target  instanceof XlsParserAttribute) && !(target  instanceof VarLengthAsciiParserAttribute) &&
				!(target  instanceof VarLengthBinaryParserAttribute)) {
			isValidate(SystemParametersConstant.ATTR_DEFAULT_VALUE,parserAttr.getDefaultValue(),"defaultValue",entityName,parserAttr.getDefaultValue(),validateForImport);
			isValidate(SystemParametersConstant.ATTR_DESCRIPTION,parserAttr.getDescription(),"description",entityName,parserAttr.getDescription(),validateForImport);
			if(!StringUtils.isEmpty(parserAttr.getSourceFieldFormat())){
				logger.debug("Source Field Format Found");
				isValidate(SystemParametersConstant.PARSERATTRIBUTE_SOURCEFIELDFORMAT,parserAttr.getSourceFieldFormat(),"sourceFieldFormat",entityName,parserAttr.getSourceFieldFormat(),validateForImport);
			}
		}
		if(target  instanceof RegexParserAttribute){
			logger.debug("RegEx Parser Attribute Found");
			RegexParserAttribute regExParserAttribute=(RegexParserAttribute)target;
			if(!StringUtils.isEmpty(regExParserAttribute.getRegex())){
				validateRegEx(regExParserAttribute.getRegex(),"regex",SystemParametersConstant.REGEXPARSERATTRIBUTE_REGEX+".invalid");
			}
			logger.debug("After validate regex");
		}
		if(target instanceof FixedLengthASCIIParserAttribute){
			logger.debug("Fixed Length ASCII parser found");
			if(parserAttr.getUnifiedField() == null || "".equalsIgnoreCase(parserAttr.getUnifiedField())) {
				errors.rejectValue("unifiedField","fixedLengthASCIIParser.unifiedField.invalid",getMessage("fixedLengthASCIIParser.unifiedField.invalid"));
			}
			
			FixedLengthASCIIParserAttribute fixedLengthASCIIParserAttribute = (FixedLengthASCIIParserAttribute)target;
			String className = fixedLengthASCIIParserAttribute.getClass().getName();
			
			int startLength = fixedLengthASCIIParserAttribute.getStartLength();
			int endLength = fixedLengthASCIIParserAttribute.getEndLength();
			
			boolean startLenthValid = isValidate(SystemParametersConstant.FIXEDLENGTHASCIIPARSERATTRIBUTE_START_LENGTH,startLength,"startLength",fixedLengthASCIIParserAttribute.getUnifiedField(),entityName,validateForImport,className);
			boolean endLenthValid = isValidate(SystemParametersConstant.FIXEDLENGTHASCIIPARSERATTRIBUTE_END_LENGTH,endLength,"endLength",fixedLengthASCIIParserAttribute.getUnifiedField(),entityName,validateForImport,className);
			isValidate(SystemParametersConstant.FIXEDLENGTHASCIIPARSERATTRIBUTE_LENGTH,fixedLengthASCIIParserAttribute.getLength(),"length",entityName,fixedLengthASCIIParserAttribute.getLength(),validateForImport);
			// business validation	
			if (startLenthValid && endLenthValid && (startLength > 0 && endLength > 0) &&  (startLength > endLength )){
				setErrorFieldErrorMessage(String.valueOf(fixedLengthASCIIParserAttribute.getStartLength()), "startLength", fixedLengthASCIIParserAttribute.getUnifiedField(), entityName, validateForImport, "FixedLengthASCIIParserAttribute.startLength.greater.invalid", getMessage("FixedLengthASCIIParserAttribute.startLength.greater.invalid"));		
			}	
			
		}if(target instanceof FixedLengthBinaryParserAttribute){
			logger.debug("Fixed Length Binary parser found");
			
			if(parserAttr.getUnifiedField() == null || "".equalsIgnoreCase(parserAttr.getUnifiedField())) {
				errors.rejectValue("unifiedField","fixedLengthBinaryParser.unifiedField.invalid",getMessage("fixedLengthBinaryParser.unifiedField.invalid"));
			}
			FixedLengthBinaryParserAttribute fixedLengthBinaryParserAttribute=(FixedLengthBinaryParserAttribute)target;
			String className=fixedLengthBinaryParserAttribute.getClass().getName();
			
			int startLength=fixedLengthBinaryParserAttribute.getStartLength();
			int endLength=fixedLengthBinaryParserAttribute.getEndLength();
			
			boolean startLenthValid =false,endLenthValid=false;
			if(startLength>-1 || endLength>-1) {
				startLenthValid=isValidate(SystemParametersConstant.FIXEDLENGTHBINARYPARSERATTRIBUTE_START_LENGTH,String.valueOf(startLength),"startLength",entityName,className,validateForImport);
				endLenthValid=isValidate(SystemParametersConstant.FIXEDLENGTHBINARYPARSERATTRIBUTE_END_LENGTH,String.valueOf(endLength),"endLength",entityName,className,validateForImport);
			//isValidate(SystemParametersConstant.FIXEDLENGTHBINARYPARSERATTRIBUTE_LENGTH,String.valueOf(fixedLengthBinaryParserAttribute.getLength()),"length",entityName,className,validateForImport);
			}
			if (startLenthValid && endLenthValid && (startLength >= -1 && endLength >= -1) &&  (startLength > endLength )){
				setErrorFieldErrorMessage(String.valueOf(fixedLengthBinaryParserAttribute.getStartLength()), "startLength", fixedLengthBinaryParserAttribute.getUnifiedField(), entityName, validateForImport, "FixedLengthBinaryParserAttribute.startLength.greater.invalid", getMessage("FixedLengthBinaryParserAttribute.startLength.greater.invalid"));		
			}
			
			boolean readAsBitsFlag = fixedLengthBinaryParserAttribute.isReadAsBits();
			
			if(readAsBitsFlag){
				int bitStartLength=fixedLengthBinaryParserAttribute.getBitStartLength();
				int bitEndLength=fixedLengthBinaryParserAttribute.getBitEndLength();
				boolean bitStartLenthValid =isValidate(SystemParametersConstant.FIXEDLENGTHBINARYPARSERATTRIBUTE_START_LENGTH,String.valueOf(bitStartLength),"bitStartLength",entityName,className,validateForImport);
				boolean bitEndLenthValid=isValidate(SystemParametersConstant.FIXEDLENGTHBINARYPARSERATTRIBUTE_END_LENGTH,String.valueOf(bitEndLength),"bitEndLength",entityName,className,validateForImport);
				if (bitStartLenthValid && bitEndLenthValid && (bitStartLength >= 0 && bitEndLength >= 0) &&  (bitStartLength > bitEndLength )){
					setErrorFieldErrorMessage(String.valueOf(fixedLengthBinaryParserAttribute.getBitStartLength()), "bitStartLength", fixedLengthBinaryParserAttribute.getBitStartLength()+"", entityName, validateForImport, "FixedLengthBinaryParserAttribute.startLength.greater.invalid", getMessage("FixedLengthBinaryParserAttribute.startLength.greater.invalid"));		
				}
			}
		}
		if(target  instanceof ASN1ParserAttribute){
			logger.debug("ASN1 Parser Attribute Found");
			ASN1ParserAttribute asn1ParserAttribute=(ASN1ParserAttribute)target;
			if(!StringUtils.isEmpty(asn1ParserAttribute.getUnifiedField()))
				isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,asn1ParserAttribute.getUnifiedField(),"unifiedField",entityName,asn1ParserAttribute.getUnifiedField(),validateForImport);
			if(!StringUtils.isEmpty(asn1ParserAttribute.getUnifiedFieldHoldsChoiceId()))
				isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,asn1ParserAttribute.getUnifiedFieldHoldsChoiceId(),"unifiedFieldHoldsChoiceId",entityName,asn1ParserAttribute.getUnifiedFieldHoldsChoiceId(),validateForImport);
			logger.debug("After validate regex");
		}
		if(target  instanceof TAPParserAttribute){
			logger.debug("TAP Parser Attribute Found");
			TAPParserAttribute tapParserAttribute=(TAPParserAttribute)target;
			if(!StringUtils.isEmpty(tapParserAttribute.getUnifiedField()))
				isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,tapParserAttribute.getUnifiedField(),"unifiedField",entityName,tapParserAttribute.getUnifiedField(),validateForImport);
			if(!StringUtils.isEmpty(tapParserAttribute.getUnifiedFieldHoldsChoiceId()))
				isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,tapParserAttribute.getUnifiedFieldHoldsChoiceId(),"unifiedFieldHoldsChoiceId",entityName,tapParserAttribute.getUnifiedFieldHoldsChoiceId(),validateForImport);
			logger.debug("After validate regex");
		}
		if(target  instanceof RAPParserAttribute){
			logger.debug("RAP Parser Attribute Found");
			RAPParserAttribute rapParserAttribute=(RAPParserAttribute)target;
			if(!StringUtils.isEmpty(rapParserAttribute.getUnifiedField()))
				isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,rapParserAttribute.getUnifiedField(),"unifiedField",entityName,rapParserAttribute.getUnifiedField(),validateForImport);
			if(!StringUtils.isEmpty(rapParserAttribute.getUnifiedFieldHoldsChoiceId()))
				isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,rapParserAttribute.getUnifiedFieldHoldsChoiceId(),"unifiedFieldHoldsChoiceId",entityName,rapParserAttribute.getUnifiedFieldHoldsChoiceId(),validateForImport);
			logger.debug("After validate regex");
		}
		if(target  instanceof NRTRDEParserAttribute){
			logger.debug("NRTRDE Parser Attribute Found");
			NRTRDEParserAttribute nrtrdParserAttribute=(NRTRDEParserAttribute)target;
			if(!StringUtils.isEmpty(nrtrdParserAttribute.getUnifiedField()))
				isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,nrtrdParserAttribute.getUnifiedField(),"unifiedField",entityName,nrtrdParserAttribute.getUnifiedField(),validateForImport);
			if(!StringUtils.isEmpty(nrtrdParserAttribute.getUnifiedFieldHoldsChoiceId()))
				isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,nrtrdParserAttribute.getUnifiedFieldHoldsChoiceId(),"unifiedFieldHoldsChoiceId",entityName,nrtrdParserAttribute.getUnifiedFieldHoldsChoiceId(),validateForImport);
			logger.debug("After validate regex");
		}
		
		// NatFLow Parser Plugin
		if(target  instanceof NatFlowParserAttribute){
			logger.debug("validting the NatFLow attributes.");			
			NatFlowParserAttribute natFlowparserAttr = (NatFlowParserAttribute)target;
			if(natFlowparserAttr != null) {
				if(!StringUtils.isEmpty(parserAttr.getSourceFieldFormat())){
					logger.debug("Source Field Format Found");
					isValidate(SystemParametersConstant.PARSERATTRIBUTE_SOURCEFIELDFORMAT,natFlowparserAttr.getSourceFieldFormat(),"sourceFieldFormat",entityName,natFlowparserAttr.getSourceFieldFormat(),validateForImport);
					if(DataTypeEnum.DATE.equals(natFlowparserAttr.getSourceFieldFormat())&& !StringUtils.isEmpty(natFlowparserAttr.getDestDateFormat())){
						isValidate(SystemParametersConstant.PARSERATTRIBUTE_DESTDATEFORMAT,natFlowparserAttr.getDestDateFormat(),"destDateFormat",entityName,natFlowparserAttr.getDestDateFormat(),validateForImport);	
					}
				}				
			}
		}
		
	}
	
}
