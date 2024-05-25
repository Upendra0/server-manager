/**
 * 
 */
package com.elitecore.sm.composer.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.composer.model.Composer;
import com.elitecore.sm.composer.service.CharRenameOperationService;
import com.elitecore.sm.pathlist.model.CharRenameOperation;

/**
 * @author Ranjitsinh Reval
 *
 */

@Component
public class ComposerValidator extends BaseValidator {
	
	
	private Composer composer;
	
	private CharRenameOperation charRenameOperation;
	
	@Autowired
	CharRenameOperationService charRenameOperationService;
	
	/**
	 * Method will check current validator class
	 *@param  Class<?> clazz
	 *@return boolean 
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return Composer.class.isAssignableFrom(clazz) || CharRenameOperation.class.isAssignableFrom(clazz) ;
	}	
	
	
	/**
	 * Method will validate all required composer plug-in parameters. 
	 * @param target
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	public void validationComposerPluginParameters(Object target, Errors errors,String entityName,boolean validateForImport,List<ImportValidationErrors> importErrorList){
		logger.debug("Going to validate composer plugin parameters.");
		composer = (Composer) target;
		setErrorObject(errors, validateForImport, importErrorList);
		
		isValidate(SystemParametersConstant.COMPOSER_PLUGIN_NAME,composer.getName(),"name",entityName,composer.getName(),validateForImport);
		isValidate(SystemParametersConstant.COMPOSER_WRITE_FILE_PATH,composer.getDestPath(),"destPath",entityName,composer.getName(),validateForImport);
		isValidate(SystemParametersConstant.COMPOSER_WRITE_FILE_PREFIX,composer.getWriteFilenamePrefix(),"writeFilenamePrefix",entityName,composer.getName(),validateForImport);
		isValidate(SystemParametersConstant.COMPOSER_WRITE_FILE_SUFFIX,composer.getWriteFilenameSuffix(),"writeFilenameSuffix",entityName,composer.getName(),validateForImport);
		isValidate(SystemParametersConstant.COMPOSER_FILE_BACKUP_PATH,composer.getFileBackupPath(),"fileBackupPath",entityName,composer.getName(),validateForImport);
		isValidate(SystemParametersConstant.COMPOSER_FILE_EXTENSION,composer.getFileExtension(),"fileExtension",entityName,composer.getName(),validateForImport);
		
	}

	/**
	 * Method will validate all required composer plug-in parameters. 
	 * @param target
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateChareRenameParameters(Object target, Errors errors,String entityName,boolean validateForImport, List<ImportValidationErrors> importErrorList, boolean isFileRenameAgent){
		logger.debug("Going to validate Char rename operation parameters.");
		
		charRenameOperation = (CharRenameOperation) target;
		String classFullName = charRenameOperation.getClass().getName();

		setErrorObject(errors, validateForImport, importErrorList);
		
		if(charRenameOperation.getPaddingValue() != null && charRenameOperation.getPaddingValue().length() > 1)
			errors.rejectValue("paddingValue", "CharRenameOperation.paddingValue.invalid", getMessage("CharRenameOperation.paddingValue.invalid"));
		
		isValidate(SystemParametersConstant.CHAR_OPERATION_QUERY,charRenameOperation.getQuery(),"query",entityName,BaseConstants.DISTRIBUTION_SERVICE,validateForImport);
		isValidate(SystemParametersConstant.CHAR_OPERATION_DEFAULT_VALUE,charRenameOperation.getDefaultValue(),"defaultValue",entityName,BaseConstants.DISTRIBUTION_SERVICE,validateForImport);
		
		/*boolean isValidStartIndex = isValidate(SystemParametersConstant.CHAR_OPERATION_START_INDEX,charRenameOperation.getStartIndex(),"startIndex",entityName, null, validateForImport,classFullName);

		boolean isValidEndIndex = isValidate(SystemParametersConstant.CHAR_OPERATION_END_INDEX,charRenameOperation.getEndIndex(),"endIndex",entityName, null, validateForImport,classFullName);
		if(isValidStartIndex &&  isValidEndIndex  && ( (charRenameOperation.getStartIndex()  == -1  &&  charRenameOperation.getEndIndex() >= 0) ||  (charRenameOperation.getStartIndex()  >= 0  &&  charRenameOperation.getEndIndex() == -1))){
			setErrorFieldErrorMessage(String.valueOf(charRenameOperation.getEndIndex()), "endIndex", null, entityName, validateForImport, "CharRenameOperation.startIndex.endIndex.both.invalid", getMessage("CharRenameOperation.startIndex.endIndex.both.invalid"));
		}
		
		if (isValidStartIndex &&  isValidEndIndex && charRenameOperation.getStartIndex() > charRenameOperation.getEndIndex() ){ // Min index must be smaller than Max index.
			if(validateForImport){
				ImportValidationErrors errorsList = new ImportValidationErrors(BaseConstants.DISTRIBUTION_SERVICE, BaseConstants.DISTRIBUTION_SERVICE,"endIndex", String.valueOf(charRenameOperation.getEndIndex()), getMessage(SystemParametersConstant.CHAR_OPERATION_END_INDEX+".invalid"));
	    		importErrorList.add(errorsList);
			}else{
				errors.rejectValue("endIndex", "CharRenameOperation.endIndex.islesser.invalid", getMessage("CharRenameOperation.endIndex.islesser.invalid"));	
			}
		}*/
		
		validateSequenceNumber(charRenameOperation, errors, isFileRenameAgent);
		
	}
	
	private void validateSequenceNumber(CharRenameOperation charRenameOperation, Errors errors, boolean isFileRenameAgent) {
		if(charRenameOperation.getSequenceNo() <= 0 ){
			errors.rejectValue("sequenceNo", "fixedLengthASCIIParser.sequenceNumber.invalid", getMessage("fixedLengthASCIIParser.sequenceNumber.invalid"));
			return;
		}
		
		if(isFileRenameAgent) {
			if(charRenameOperation.getSvcFileRenConfig() != null) {
				long totalCount = charRenameOperationService.getSeqNumberCountByIdForFileRenameAgent(charRenameOperation.getSequenceNo(), charRenameOperation.getSvcFileRenConfig().getId(), charRenameOperation.getId());
				if(totalCount >= 1) {
					errors.rejectValue("sequenceNo", "sequence.number.duplicate", getMessage("sequence.number.duplicate"));
					return;
				}
			}
		} else {
			if(charRenameOperation.getComposer() != null) {
				long totalCount = charRenameOperationService.getSeqNumberCountByPluginId(charRenameOperation.getSequenceNo(), charRenameOperation.getComposer().getId(), charRenameOperation.getId());
				if(totalCount >= 1) {
					errors.rejectValue("sequenceNo", "sequence.number.duplicate", getMessage("sequence.number.duplicate"));
					return;
				}
			}
		}
		
		return;
	}
	
}
