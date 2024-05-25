package com.elitecore.sm.pathlist.validator;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import com.elitecore.sm.aggregationservice.model.AggregationDefinition;
import com.elitecore.sm.aggregationservice.model.AggregationServicePathList;
import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.model.FileActionParamEnum;
import com.elitecore.sm.common.model.FilterActionEnum;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.composer.service.CharRenameOperationService;
import com.elitecore.sm.consolidationservice.model.DataConsolidationMapping;
import com.elitecore.sm.consolidationservice.service.IConsolidationDefinitionService;
import com.elitecore.sm.drivers.model.CollectionDriver;
import com.elitecore.sm.drivers.service.DriversService;
import com.elitecore.sm.fileseqmgmt.dao.FileSequenceMgmtDao;
import com.elitecore.sm.parser.model.Parser;
import com.elitecore.sm.pathlist.model.CharRenameOperation;
import com.elitecore.sm.pathlist.model.CollectionDriverPathList;
import com.elitecore.sm.pathlist.model.CommonPathList;
import com.elitecore.sm.pathlist.model.DataConsolidationPathList;
import com.elitecore.sm.pathlist.model.DistributionDriverPathList;
import com.elitecore.sm.pathlist.model.ParsingPathList;
import com.elitecore.sm.pathlist.model.PathList;
import com.elitecore.sm.pathlist.model.ProcessingPathList;
import com.elitecore.sm.pathlist.service.PathListService;
import com.elitecore.sm.services.model.AggregationService;
import com.elitecore.sm.services.model.DuplicateRecordPolicyTypeEnum;

/**
 * Validator class for pathlist 
 * @author avani.panchal
 *
 */
@Component
public class PathListValidator extends BaseValidator{
	
	private PathList pathList;
	
	private CharRenameOperation charRenameOperation;
	
	@Autowired
	CharRenameOperationService charRenameOperationService;
	
	private static final String REMOTE_FILE_ACTION_PARAM_NAME = "remoteFileActionParamName";
	private static final String FILE_ACTION_ERROR_MESSAGE_KEY = "CollectionDriverPathList.remoteFileActionValue.invalid";
	private static final String REMOTE_FILE_ACTION_PARAM_ERROR_MESSAGE_KEY = "CollectionDriverPathList.remoteFileActionParamName.invalid";
	private static final String WRITE_PATH = "writeFilePath";	
	private static final String READ_FILE_NAME_PATH = "readFilePath";
	private static final String READ_FILE_NAME_SUFFIX = "readFilenameSuffix";
	private static final String DB_READ_FILENAME_EXTRA_SUFFIX = "dbReadFileNameExtraSuffix";
	private static final String END_INDEX = "endIndex";	
	private static final String START_INDEX = "startIndex";
	private static final String SEQ_END_INDEX = "seqEndIndex";
	private static final String SEQ_START_INDEX = "seqStartIndex";
	private static final String LEFT_POSITION = "left";	
	private static final String RIGHT_POSITION = "right";
	private static final String FILESIZECHECKMINVALUE = "fileSizeCheckMinValue";
	private static final String FILESIZECHECKMAXVALUE = "fileSizeCheckMaxValue";
		
	private static final String PARENT_DEVICE = "parentDevice";
	private static final String TIME_INTERVAL = "timeInterval";
	private static final String VALID_FILE_TIME_INTERVAL = "validFileTimeInterval";
	private static final String REFERENCE_DEVICE = "referenceDevice";
	private static final String MIN_VALUE = "missingFileSequenceId.minValue";
	private static final String MAX_VALUE = "missingFileSequenceId.maxValue";
	private static final String REMOTE_FILE_ACTION_PARAM_NAME_TWO = "remoteFileActionParamNameTwo";
	private static final String CIRCLE = "circle";

	
	@Autowired
	DriversService driverServcice;
	
	@Autowired
	PathListService pathListServcice;
	
	@Autowired
	FileSequenceMgmtDao fileSequenceMgmtDao;
	
	@Autowired
	IConsolidationDefinitionService consolidationDefinitionService;
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {

		return PathList.class.isAssignableFrom(clazz);
	}
	
	/**
	 * Validate PathList Parameter
	 * @param target
	 * @param errors
	 * @param importErrorList
	 * @param entityName
	 * @param validateForImport
	 */
	public void validatePathListParams(Object target, Errors errors,List<ImportValidationErrors> importErrorList,String entityName,boolean validateForImport) {
		
		if(validateForImport){
			this.importErrorList=importErrorList;
		}else{
			this.errors = errors;	
		}
		
		pathList = (PathList) target;
		
		isValidate(SystemParametersConstant.PATHLIST_NAME,pathList.getName(),"name",entityName,pathList.getName(),validateForImport);
		isValidate(SystemParametersConstant.PARSER_PARSINGPATHLIST_READFILEPATH,pathList.getReadFilePath(),"readFilePath",entityName,pathList.getName(),validateForImport);
		
		if(pathList instanceof CollectionDriverPathList){
			validationCollectionDriverPathlist(pathList, entityName, validateForImport);
		} else if (pathList instanceof ParsingPathList){
				ParsingPathList parsingPathList = (ParsingPathList)pathList;
				
				if((!validateForImport) && (parsingPathList.getParentDevice() == null || parsingPathList.getParentDevice().getId() == 0)){
					setErrorFieldErrorMessage("" , PARENT_DEVICE, parsingPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.parentDevice.id.invalid", getMessage("CollectionDriverPathList.parentDevice.id.invalid"));
				}
				
				if((!validateForImport) && (parsingPathList.getCircle() == null || parsingPathList.getCircle().getId() == 0)){
					setErrorFieldErrorMessage("" , CIRCLE, parsingPathList.getName(), entityName, validateForImport,  "Parser.circle.id.invalid", getMessage("Parser.circle.id.invalid"));
				}

				if(parsingPathList.getFileGrepDateEnabled()){
					isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_DATEFORMAT,parsingPathList.getDateFormat(),"dateFormat",entityName, parsingPathList.getName(),validateForImport);	
					boolean startIndexValid = isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_STARTINDEX, parsingPathList.getStartIndex(),"startIndex",entityName,parsingPathList.getName(),validateForImport, parsingPathList.getClass().getName());
					boolean endIndexValid = isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_ENDINDEX, parsingPathList.getEndIndex(),END_INDEX,entityName,parsingPathList.getName(),validateForImport, parsingPathList.getClass().getName());
					if(parsingPathList.getStartIndex() == -1){
						setErrorFieldErrorMessage(String.valueOf(parsingPathList.getStartIndex()), START_INDEX, parsingPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.startIndex.invalid", getMessage("CollectionDriverPathList.startIndex.invalid"));
					}
					if(parsingPathList.getEndIndex() == -1){
						setErrorFieldErrorMessage(String.valueOf(parsingPathList.getEndIndex()), END_INDEX, parsingPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.startIndex.invalid", getMessage("CollectionDriverPathList.startIndex.invalid"));
					}
					if(startIndexValid && endIndexValid){
						if(parsingPathList.getEndIndex() != -1 && parsingPathList.getStartIndex() == parsingPathList.getEndIndex()){
							setErrorFieldErrorMessage(String.valueOf(parsingPathList.getEndIndex()) , END_INDEX, parsingPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.endIndex.startIndex.same.invalid", getMessage("CollectionDriverPathList.endIndex.startIndex.same.invalid"));
						}else if(parsingPathList.getPosition()!=null && parsingPathList.getPosition().equalsIgnoreCase(LEFT_POSITION) &&   parsingPathList.getStartIndex() > parsingPathList.getEndIndex()){
							setErrorFieldErrorMessage(String.valueOf(parsingPathList.getEndIndex()) , END_INDEX, parsingPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.endIndex.islesser.invalid", getMessage("CollectionDriverPathList.endIndex.islesser.invalid"));
						}else if(parsingPathList.getPosition()!=null && parsingPathList.getPosition().equalsIgnoreCase(RIGHT_POSITION) && parsingPathList.getStartIndex() < parsingPathList.getEndIndex()){
							setErrorFieldErrorMessage(String.valueOf(parsingPathList.getEndIndex()) , START_INDEX, parsingPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.startIndex.islesser.invalid", getMessage("CollectionDriverPathList.startIndex.islesser.invalid"));
						}	
					}
				}
				
		}else if(pathList instanceof DistributionDriverPathList){ // Validation for distribution driver path list parameters.
			validateDistributionPathlistParams((DistributionDriverPathList) pathList, entityName, validateForImport);
		}else if(pathList instanceof ProcessingPathList){ // Validation for Processing Service path list parameters.
			validateProcessingPathlistParams((ProcessingPathList) pathList, entityName, validateForImport);
		}
	}

	/**
	 * Method will validate collection driver path list parameters.
	 * @param pathList
	 * @param entityName
	 * @param validateForImport
	 */
	private void  validationCollectionDriverPathlist(PathList pathList,String entityName, boolean validateForImport ){
		CollectionDriverPathList collectionDriverPathList = (CollectionDriverPathList)pathList;
		String fullClassName = collectionDriverPathList.getClass().getName();
		String pathlistName = collectionDriverPathList.getName();
		validateCommonPathListParam((CommonPathList)pathList,entityName,validateForImport);
		
		if((!validateForImport) && (collectionDriverPathList.getParentDevice() == null || collectionDriverPathList.getParentDevice().getId() == 0)){
			setErrorFieldErrorMessage("" , PARENT_DEVICE, collectionDriverPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.parentDevice.id.invalid", getMessage("CollectionDriverPathList.parentDevice.id.invalid"));
		}
		isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_MAXFILESCOUNTALERT,collectionDriverPathList.getMaxFilesCountAlert(),BaseConstants.MAX_FILE_COUNT_ALERT,pathlistName, entityName,validateForImport,fullClassName);
		if(!validateForImport){
			isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_REFERENCEDEVICE,collectionDriverPathList.getReferenceDevice(),REFERENCE_DEVICE,pathlistName,entityName,validateForImport);
		}
		if(validateForImport){
			CollectionDriver collectionDriver = (CollectionDriver) collectionDriverPathList.getDriver();
			
			if(collectionDriver.getService().isEnableDBStats() && collectionDriver.getFileGroupingParameter().isEnableForDuplicate()){
				isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_TIMEINTERVAL,String.valueOf(collectionDriverPathList.getTimeInterval()),TIME_INTERVAL,pathlistName, entityName,validateForImport);
			}
		}else{
			CollectionDriver collectionDriver = (CollectionDriver) driverServcice.getDriverById(collectionDriverPathList.getDriver().getId());
			
			if(collectionDriver.getService().isEnableDBStats() && collectionDriver.getFileGroupingParameter().isEnableForDuplicate()){
				isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_TIMEINTERVAL,String.valueOf(collectionDriverPathList.getTimeInterval()),TIME_INTERVAL,pathlistName, entityName,validateForImport);
			}
		}
		
		if(collectionDriverPathList.getFileSeqAlertEnabled()){
			isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_SEQSTARTINDEX,String.valueOf(collectionDriverPathList.getSeqStartIndex()),"seqStartIndex",entityName,collectionDriverPathList.getName(),validateForImport);
			if(isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_SEQENDINDEX,String.valueOf(collectionDriverPathList.getEndIndex()),SEQ_END_INDEX,entityName,collectionDriverPathList.getName(),validateForImport)){
				if(collectionDriverPathList.getSeqStartIndex() > collectionDriverPathList.getSeqEndIndex()){
					setErrorFieldErrorMessage(String.valueOf(collectionDriverPathList.getSeqEndIndex()) , SEQ_END_INDEX, collectionDriverPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.seqEndIndex.islesser.invalid", getMessage("CollectionDriverPathList.seqEndIndex.islesser.invalid"));
				}
				if(collectionDriverPathList.getSeqStartIndex() == 0 ){
					setErrorFieldErrorMessage(String.valueOf(collectionDriverPathList.getSeqStartIndex()) , SEQ_START_INDEX, collectionDriverPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.startIndex.zero.invalid", getMessage("CollectionDriverPathList.startIndex.zero.invalid"));
				}
				if(collectionDriverPathList.getSeqEndIndex() == 0){
					setErrorFieldErrorMessage(String.valueOf(collectionDriverPathList.getSeqStartIndex()) , SEQ_END_INDEX, collectionDriverPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.endIndex.zero.invalid", getMessage("CollectionDriverPathList.endIndex.zero.invalid"));
				}
			}
		
		
			if(collectionDriverPathList.getMissingFileSequenceId() != null){
				isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_MINVALUE,collectionDriverPathList.getMissingFileSequenceId().getMinValue()+"",MIN_VALUE,entityName,collectionDriverPathList.getName(),validateForImport);
				if(isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_MAXVALUE,collectionDriverPathList.getMissingFileSequenceId().getMaxValue()+"",MAX_VALUE,entityName,collectionDriverPathList.getName(),validateForImport)){
					if(collectionDriverPathList.getMissingFileSequenceId().getMinValue() == collectionDriverPathList.getMissingFileSequenceId().getMaxValue()){
						setErrorFieldErrorMessage(String.valueOf(collectionDriverPathList.getMissingFileSequenceId().getMaxValue()) , MAX_VALUE, collectionDriverPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.minValue.maxValue.same.invalid", getMessage("CollectionDriverPathList.minValue.maxValue.same.invalid"));
					}else if(collectionDriverPathList.getMissingFileSequenceId().getMinValue() > collectionDriverPathList.getMissingFileSequenceId().getMaxValue()){
						setErrorFieldErrorMessage(String.valueOf(collectionDriverPathList.getMissingFileSequenceId().getMaxValue()) , MAX_VALUE, collectionDriverPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.maxValue.islesser.invalid", getMessage("CollectionDriverPathList.maxValue.islesser.invalid"));
					}
				}
			}
			
		}
		
		
		if(collectionDriverPathList.getFileGrepDateEnabled()){
			isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_DATEFORMAT,collectionDriverPathList.getDateFormat(),"dateFormat",entityName,pathlistName,validateForImport);	
			boolean startIndexValid = isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_STARTINDEX, collectionDriverPathList.getStartIndex(),"startIndex",entityName,pathlistName,validateForImport, collectionDriverPathList.getClass().getName());
			boolean endIndexValid = isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_ENDINDEX, collectionDriverPathList.getEndIndex(),END_INDEX,entityName,pathlistName,validateForImport, collectionDriverPathList.getClass().getName());
			if(collectionDriverPathList.getStartIndex() == -1){
				setErrorFieldErrorMessage(String.valueOf(collectionDriverPathList.getStartIndex()), START_INDEX, collectionDriverPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.startIndex.invalid", getMessage("CollectionDriverPathList.startIndex.invalid"));
			}
			if(collectionDriverPathList.getEndIndex() == -1){
				setErrorFieldErrorMessage(String.valueOf(collectionDriverPathList.getEndIndex()), END_INDEX, collectionDriverPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.startIndex.invalid", getMessage("CollectionDriverPathList.startIndex.invalid"));
			}
			if(startIndexValid && endIndexValid){
				if(collectionDriverPathList.getEndIndex() != -1 && collectionDriverPathList.getStartIndex() == collectionDriverPathList.getEndIndex()){
					setErrorFieldErrorMessage(String.valueOf(collectionDriverPathList.getEndIndex()) , END_INDEX, pathlistName, entityName, validateForImport,  "CollectionDriverPathList.endIndex.startIndex.same.invalid", getMessage("CollectionDriverPathList.endIndex.startIndex.same.invalid"));
				}else if(collectionDriverPathList.getPosition()!=null && collectionDriverPathList.getPosition().equalsIgnoreCase(LEFT_POSITION) &&  collectionDriverPathList.getStartIndex() > collectionDriverPathList.getEndIndex()){
					setErrorFieldErrorMessage(String.valueOf(collectionDriverPathList.getEndIndex()) , END_INDEX, pathlistName, entityName, validateForImport,  "CollectionDriverPathList.endIndex.islesser.invalid", getMessage("CollectionDriverPathList.endIndex.islesser.invalid"));
				}else if(collectionDriverPathList.getPosition()!=null && collectionDriverPathList.getPosition().equalsIgnoreCase(RIGHT_POSITION) && collectionDriverPathList.getStartIndex() < collectionDriverPathList.getEndIndex()){
					setErrorFieldErrorMessage(String.valueOf(collectionDriverPathList.getEndIndex()) , START_INDEX, pathlistName, entityName, validateForImport,  "CollectionDriverPathList.startIndex.islesser.invalid", getMessage("CollectionDriverPathList.startIndex.islesser.invalid"));
				}
			}
		}
		
		if(FilterActionEnum.MOVE.getValue().equals(collectionDriverPathList.getRemoteFileAction()) && !(FileActionParamEnum.DESTINATIONPATH.getValue().equals(collectionDriverPathList.getRemoteFileActionParamName()))){
			setErrorFieldErrorMessage(String.valueOf(collectionDriverPathList.getRemoteFileActionParamName()) , REMOTE_FILE_ACTION_PARAM_NAME, pathlistName, entityName, validateForImport, FILE_ACTION_ERROR_MESSAGE_KEY, getMessage(REMOTE_FILE_ACTION_PARAM_ERROR_MESSAGE_KEY));
		}
		
		if(FilterActionEnum.RENAME.getValue().equals(collectionDriverPathList.getRemoteFileAction()) && !(FileActionParamEnum.EXTENSION.getValue().equals(collectionDriverPathList.getRemoteFileActionParamName()))){
			setErrorFieldErrorMessage(String.valueOf(collectionDriverPathList.getRemoteFileActionParamName()) , REMOTE_FILE_ACTION_PARAM_NAME, pathlistName, entityName, validateForImport, FILE_ACTION_ERROR_MESSAGE_KEY, getMessage(REMOTE_FILE_ACTION_PARAM_ERROR_MESSAGE_KEY));	
		}
		
		if(FilterActionEnum.NA.getValue().equals(collectionDriverPathList.getRemoteFileAction()) && !(FileActionParamEnum.NA.getValue().equals(collectionDriverPathList.getRemoteFileActionParamName()))){
			setErrorFieldErrorMessage(String.valueOf(collectionDriverPathList.getRemoteFileActionParamName()) , REMOTE_FILE_ACTION_PARAM_NAME, pathlistName, entityName, validateForImport, FILE_ACTION_ERROR_MESSAGE_KEY, getMessage(REMOTE_FILE_ACTION_PARAM_ERROR_MESSAGE_KEY));
		}
		
		if(FilterActionEnum.NA.getValue().equals(collectionDriverPathList.getRemoteFileAction())){
			isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_VALIDFILETIMEINTERVAL,String.valueOf(collectionDriverPathList.getValidFileTimeInterval()),VALID_FILE_TIME_INTERVAL,pathlistName, entityName,validateForImport);	
		}

		if(!(collectionDriverPathList.getRemoteFileAction().equals(FilterActionEnum.NA.getValue()) || collectionDriverPathList.getRemoteFileAction().equals(FilterActionEnum.DELETE.getValue())) && !(collectionDriverPathList.getRemoteFileActionParamName().equals(FileActionParamEnum.NA.getValue()))){
			if(StringUtils.isEmpty( collectionDriverPathList.getRemoteFileActionValue()))
				setErrorFieldErrorMessage(collectionDriverPathList.getRemoteFileActionValue() , "remoteFileActionValue", pathlistName, entityName, validateForImport, FILE_ACTION_ERROR_MESSAGE_KEY,getMessage(FILE_ACTION_ERROR_MESSAGE_KEY));				
			if(StringUtils.isEmpty( collectionDriverPathList.getRemoteFileActionValueTwo()) && FilterActionEnum.MOVEANDRENAME.getValue().equals(collectionDriverPathList.getRemoteFileAction()))
				setErrorFieldErrorMessage(collectionDriverPathList.getRemoteFileActionValueTwo() , "remoteFileActionValueTwo", pathlistName, entityName, validateForImport, FILE_ACTION_ERROR_MESSAGE_KEY,getMessage(FILE_ACTION_ERROR_MESSAGE_KEY));				
		}
		
		if(FilterActionEnum.MOVEANDRENAME.getValue().equals(collectionDriverPathList.getRemoteFileAction()) && !(FileActionParamEnum.DESTINATIONPATH.getValue().equals(collectionDriverPathList.getRemoteFileActionParamName())) && !(FileActionParamEnum.EXTENSION.getValue().equals(collectionDriverPathList.getRemoteFileActionParamNameTwo()))){
			setErrorFieldErrorMessage(String.valueOf(collectionDriverPathList.getRemoteFileActionParamName()) , REMOTE_FILE_ACTION_PARAM_NAME, pathlistName, entityName, validateForImport, FILE_ACTION_ERROR_MESSAGE_KEY, getMessage(REMOTE_FILE_ACTION_PARAM_ERROR_MESSAGE_KEY));
			setErrorFieldErrorMessage(String.valueOf(collectionDriverPathList.getRemoteFileActionValueTwo()) , REMOTE_FILE_ACTION_PARAM_NAME_TWO, pathlistName, entityName, validateForImport, FILE_ACTION_ERROR_MESSAGE_KEY, getMessage(REMOTE_FILE_ACTION_PARAM_ERROR_MESSAGE_KEY));
		}
		
		if(collectionDriverPathList.getFileSizeCheckEnabled()){
				
			boolean fileSizeCheckMinValue = isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_FILESIZECHECKMINVALUE, collectionDriverPathList.getFileSizeCheckMinValue(),FILESIZECHECKMINVALUE,entityName,pathlistName,validateForImport, collectionDriverPathList.getClass().getName());
			boolean fileSizeCheckMaxValue = isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_FILESIZECHECKMAXVALUE, collectionDriverPathList.getFileSizeCheckMaxValue(),FILESIZECHECKMAXVALUE,entityName,pathlistName,validateForImport, collectionDriverPathList.getClass().getName());
			if(!fileSizeCheckMinValue || collectionDriverPathList.getFileSizeCheckMinValue() == -1){
				setErrorFieldErrorMessage(String.valueOf(collectionDriverPathList.getFileSizeCheckMinValue()), FILESIZECHECKMINVALUE, collectionDriverPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.fileSizeCheck.invalid", getMessage("CollectionDriverPathList.fileSizeCheck.invalid"));
			}
			if(!fileSizeCheckMaxValue || collectionDriverPathList.getFileSizeCheckMaxValue() == -1){
				setErrorFieldErrorMessage(String.valueOf(collectionDriverPathList.getFileSizeCheckMaxValue()), FILESIZECHECKMAXVALUE, collectionDriverPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.fileSizeCheck.invalid", getMessage("CollectionDriverPathList.fileSizeCheck.invalid"));
			}
			if(fileSizeCheckMinValue && fileSizeCheckMaxValue && collectionDriverPathList.getFileSizeCheckMinValue() > collectionDriverPathList.getFileSizeCheckMaxValue()){
					setErrorFieldErrorMessage(String.valueOf(collectionDriverPathList.getFileSizeCheckMaxValue()) , FILESIZECHECKMAXVALUE, pathlistName, entityName, validateForImport,  "CollectionDriverPathList.fileSizeCheckMaxValue.islesser.invalid", getMessage("CollectionDriverPathList.fileSizeCheckMaxValue.islesser.invalid"));
			}
		}
		
	}
	
	/**
	 * validate commom path list param
	 * @param commonPathList
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	private void validateCommonPathListParam(CommonPathList commonPathList,String entityName,boolean validateForImport){
		isValidate(SystemParametersConstant.COMMONPATHLIST_WRITEFILEPATH,commonPathList.getWriteFilePath(),WRITE_PATH,entityName,commonPathList.getName(),validateForImport);
		isValidate(SystemParametersConstant.COMMONPATHLIST_READFILENAMEPREFIX,commonPathList.getReadFilenamePrefix(),READ_FILE_NAME_PATH,entityName,commonPathList.getName(),validateForImport);
		if(!(commonPathList instanceof CollectionDriverPathList) || (commonPathList.getReadFilenameSuffix() != null && commonPathList.getReadFilenameSuffix().length() > 0)) {
			isValidate(SystemParametersConstant.COMMONPATHLIST_READFILENAMESUFFIX,commonPathList.getReadFilenameSuffix(),READ_FILE_NAME_SUFFIX,entityName,commonPathList.getName(),validateForImport);
		}
		isValidate(SystemParametersConstant.COMMONPATHLIST_READFILENAMECONTAINS,commonPathList.getReadFilenameContains(),BaseConstants.READ_FILE_NAME_CONTAIN,entityName,commonPathList.getName(),validateForImport);
		isValidate(SystemParametersConstant.COMMONPATHLIST_WRITEFILENAMEPREFIX,commonPathList.getWriteFilenamePrefix(),"writeFilenamePrefix",entityName,commonPathList.getName(),validateForImport);
		
	}
	
	/**
	 * validate collection pathlist param 
	 * @param collectionPathList
	 * @param errors
	 * @param importErrorList
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateAddtionalCollectionPathListParam(CollectionDriverPathList collectionPathList, Errors errors,List<ImportValidationErrors> importErrorList,String entityName,boolean validateForImport){
		if(validateForImport){
			this.importErrorList=importErrorList;
		}else{
			this.errors = errors;
		}
		
		String pathlistName = collectionPathList.getName();
		
		if(collectionPathList.getFileGrepDateEnabled()){
			isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_DATEFORMAT,collectionPathList.getDateFormat(),"dateFormat",entityName,pathlistName,validateForImport);	
			boolean startIndexValid = isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_STARTINDEX, collectionPathList.getStartIndex(),"startIndex",entityName,pathlistName,validateForImport, collectionPathList.getClass().getName());
			boolean endIndexValid = isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_ENDINDEX, collectionPathList.getEndIndex(),END_INDEX,entityName,pathlistName,validateForImport, collectionPathList.getClass().getName());
			if(collectionPathList.getStartIndex() == -1){
				setErrorFieldErrorMessage(String.valueOf(collectionPathList.getStartIndex()), START_INDEX, collectionPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.startIndex.invalid", getMessage("CollectionDriverPathList.startIndex.invalid"));
			}
			if(collectionPathList.getEndIndex() == -1){
				setErrorFieldErrorMessage(String.valueOf(collectionPathList.getEndIndex()), END_INDEX, collectionPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.startIndex.invalid", getMessage("CollectionDriverPathList.startIndex.invalid"));
			}
			if(startIndexValid && endIndexValid){
				if(collectionPathList.getEndIndex() != -1 && collectionPathList.getStartIndex() == collectionPathList.getEndIndex()){
					setErrorFieldErrorMessage(String.valueOf(collectionPathList.getEndIndex()) , END_INDEX, pathlistName, entityName, validateForImport,  "CollectionDriverPathList.endIndex.startIndex.same.invalid", getMessage("CollectionDriverPathList.endIndex.startIndex.same.invalid"));
				}else if(collectionPathList.getPosition()!=null && collectionPathList.getPosition().equalsIgnoreCase(LEFT_POSITION) && collectionPathList.getStartIndex() > collectionPathList.getEndIndex()){
					setErrorFieldErrorMessage(String.valueOf(collectionPathList.getEndIndex()) , END_INDEX, pathlistName, entityName, validateForImport,  "CollectionDriverPathList.endIndex.islesser.invalid", getMessage("CollectionDriverPathList.endIndex.islesser.invalid"));
				}else if(collectionPathList.getPosition()!=null && collectionPathList.getPosition().equalsIgnoreCase(RIGHT_POSITION) && collectionPathList.getStartIndex() < collectionPathList.getEndIndex()){
					setErrorFieldErrorMessage(String.valueOf(collectionPathList.getEndIndex()) , START_INDEX, pathlistName, entityName, validateForImport,  "CollectionDriverPathList.startIndex.islesser.invalid", getMessage("CollectionDriverPathList.startIndex.islesser.invalid"));
				}	

			}
		}
		
		if(collectionPathList.getFileSeqAlertEnabled()){
			isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_SEQSTARTINDEX,String.valueOf(collectionPathList.getSeqStartIndex()),"seqStartIndex",entityName,collectionPathList.getName(),validateForImport);
			if(isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_SEQENDINDEX,String.valueOf(collectionPathList.getEndIndex()),SEQ_END_INDEX,entityName,collectionPathList.getName(),validateForImport)){
				if(collectionPathList.getSeqStartIndex() > collectionPathList.getSeqEndIndex()){
					setErrorFieldErrorMessage(String.valueOf(collectionPathList.getSeqEndIndex()) , SEQ_END_INDEX, pathlistName, entityName, validateForImport,  "CollectionDriverPathList.seqEndIndex.islesser.invalid", getMessage("CollectionDriverPathList.seqEndIndex.islesser.invalid"));
				}
			}
		}
		//isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_MAXCOUNTERLIMIT,String.valueOf(collectionPathList.getMaxCounterLimit()),"maxCounterLimit",entityName,collectionPathList.getName(),validateForImport);
		
	}
	
	/**
	 * Validate Pathlist parameter for import
	 * @param pathList
	 * @param pathListValidationError
	 */
	public void validatePathListForImport(PathList pathList,List<ImportValidationErrors> pathListValidationError){
		
		logger.debug("Validate Import Operation for Entity: "+BaseConstants.PATHLIST + " Name: "+pathList.getName());
		validatePathListParams(pathList,null,pathListValidationError,BaseConstants.PATHLIST,true);
		validateAddtionalCollectionPathListParam((CollectionDriverPathList)pathList,null,pathListValidationError,BaseConstants.PATHLIST,true);
		
	}
	
	/**
	 * Validate IPLog path-list wrapper object
	 * @param parserWrapper
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 * @param pathListValidationError
	 */
	public void validateParserWrapperForIPLogParsing(Parser parser,Errors errors,String entityName,boolean validateForImport,List<ImportValidationErrors> pathListValidationError){
		if(validateForImport){
			this.importErrorList=pathListValidationError;
		}else{
			this.errors = errors;
		}
		isValidate(SystemParametersConstant.PATHLIST_NAME,parser.getParsingPathList().getName(),"parsingPathList.name",entityName,parser.getName(),validateForImport);
		isValidate(SystemParametersConstant.PATHLIST_READFILEPATH,parser.getParsingPathList().getReadFilePath(),"parsingPathList.readFilePath",entityName,parser.getName(),validateForImport);
		isValidate(SystemParametersConstant.PARSER_WRITEFILEPATH,parser.getWriteFilePath(),WRITE_PATH,entityName,parser.getName(),validateForImport);
		isValidate(SystemParametersConstant.PARSER_READFILENAMEPREFIX,parser.getReadFilenamePrefix(),READ_FILE_NAME_PATH,entityName,parser.getName(),validateForImport);
		isValidate(SystemParametersConstant.PARSER_READFILENAMESUFFIX,parser.getReadFilenameSuffix(),READ_FILE_NAME_SUFFIX,entityName,parser.getName(),validateForImport);
		isValidate(SystemParametersConstant.PARSER_READFILENAMEEXCLUDETYPES,parser.getReadFilenameExcludeTypes(),BaseConstants.READ_FILE_NAME_EXCLUDE,entityName,parser.getName(),validateForImport);
		isValidate(SystemParametersConstant.PARSER_WRITEFILENAMEPREFIX,parser.getWriteFilenamePrefix(),"writeFilenamePrefix",entityName,parser.getName(),validateForImport);
		isValidate(SystemParametersConstant.PARSER_NAME,parser.getName(),"name",entityName,parser.getName(),validateForImport);
	}
	
	
	/**
	 * Method will validate all required parameters for the distribution path list.
	 * @param distributionPathList
	 * @param errors
	 * @param importErrorList
	 * @param entityName
	 * @param validateForImport
	 */
	private void validateDistributionPathlistParams(DistributionDriverPathList distributionPathList,  String entityName, boolean validateForImport){
		logger.debug("Validating distribution pathlist parameters.");
		
		isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_MAXFILESCOUNTALERT,((DistributionDriverPathList)pathList).getMaxFilesCountAlert(),BaseConstants.MAX_FILE_COUNT_ALERT,distributionPathList.getName(), entityName,validateForImport,distributionPathList.getClass().getName());
		if(!StringUtils.isEmpty(distributionPathList.getWriteFilePath())){
			isValidate(SystemParametersConstant.DISTRIBUTION_PATHLIST_WRITE_PATH,distributionPathList.getWriteFilePath(),WRITE_PATH,entityName,distributionPathList.getName(),validateForImport);
		}
		if((!validateForImport) && (distributionPathList.getParentDevice() == null || distributionPathList.getParentDevice().getId() == 0)){
			setErrorFieldErrorMessage("" , PARENT_DEVICE, distributionPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.parentDevice.id.invalid", getMessage("CollectionDriverPathList.parentDevice.id.invalid"));
		}
		
		isValidate(SystemParametersConstant.COMMONPATHLIST_READFILENAMEPREFIX,distributionPathList.getReadFilenamePrefix(),READ_FILE_NAME_PATH,entityName,distributionPathList.getName(),validateForImport);
		//isValidate(SystemParametersConstant.COMMONPATHLIST_READFILENAMESUFFIX,distributionPathList.getReadFilenameSuffix(),READ_FILE_NAME_SUFFIX,entityName,distributionPathList.getName(),validateForImport);
		isValidate(SystemParametersConstant.DISTRIBUTIONDRIVERPATHLIST_DBREADFILENAMEEXTRASUFFIX,distributionPathList.getDbReadFileNameExtraSuffix(),DB_READ_FILENAME_EXTRA_SUFFIX,entityName,distributionPathList.getName(),validateForImport);

		isValidate(SystemParametersConstant.COMMONPATHLIST_READFILENAMECONTAINS,distributionPathList.getReadFilenameContains(),BaseConstants.READ_FILE_NAME_CONTAIN,entityName,distributionPathList.getName(),validateForImport);
		isValidate(SystemParametersConstant.PARSER_READFILENAMEEXCLUDETYPES,distributionPathList.getReadFilenameExcludeTypes(),BaseConstants.READ_FILE_NAME_EXCLUDE,entityName,distributionPathList.getName(),validateForImport);
		
		if(distributionPathList.getFileGrepDateEnabled()){
			isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_DATEFORMAT,distributionPathList.getDateFormat(),"dateFormat",entityName, distributionPathList.getName(),validateForImport);	
			boolean startIndexValid = isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_STARTINDEX, distributionPathList.getStartIndex(),"startIndex",entityName,distributionPathList.getName(),validateForImport, distributionPathList.getClass().getName());
			boolean endIndexValid = isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_ENDINDEX, distributionPathList.getEndIndex(),END_INDEX,entityName,distributionPathList.getName(),validateForImport, distributionPathList.getClass().getName());
			if(distributionPathList.getStartIndex() == -1){
				setErrorFieldErrorMessage(String.valueOf(distributionPathList.getStartIndex()), START_INDEX, distributionPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.startIndex.invalid", getMessage("CollectionDriverPathList.startIndex.invalid"));
			}
			if(distributionPathList.getEndIndex() == -1){
				setErrorFieldErrorMessage(String.valueOf(distributionPathList.getEndIndex()), END_INDEX, distributionPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.startIndex.invalid", getMessage("CollectionDriverPathList.startIndex.invalid"));
			}
			
			if(startIndexValid && endIndexValid){
				if(distributionPathList.getEndIndex() != -1 && distributionPathList.getStartIndex() == distributionPathList.getEndIndex()){
					setErrorFieldErrorMessage(String.valueOf(distributionPathList.getEndIndex()) , END_INDEX, distributionPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.endIndex.startIndex.same.invalid", getMessage("CollectionDriverPathList.endIndex.startIndex.same.invalid"));
				}else if(distributionPathList.getPosition()!=null && distributionPathList.getPosition().equalsIgnoreCase(LEFT_POSITION) && distributionPathList.getStartIndex() > distributionPathList.getEndIndex()){
					setErrorFieldErrorMessage(String.valueOf(distributionPathList.getEndIndex()) , END_INDEX, distributionPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.endIndex.islesser.invalid", getMessage("CollectionDriverPathList.endIndex.islesser.invalid"));
				}else if(distributionPathList.getPosition()!=null && distributionPathList.getPosition().equalsIgnoreCase(RIGHT_POSITION) && distributionPathList.getStartIndex() < distributionPathList.getEndIndex()){
					setErrorFieldErrorMessage(String.valueOf(distributionPathList.getEndIndex()) , START_INDEX, distributionPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.startIndex.islesser.invalid", getMessage("CollectionDriverPathList.startIndex.islesser.invalid"));
				}
			}
		}
	}
	
	
	/**
	 * Method will validate all required parameters for the processing service path list.
	 * @param processingPathList
	 * @param errors
	 * @param importErrorList
	 * @param entityName
	 * @param validateForImport
	 */
	private void validateProcessingPathlistParams(ProcessingPathList processingPathList,  String entityName, boolean validateForImport){
		logger.debug("Validating processing pathlist parameters.");
		isValidate(SystemParametersConstant.PROCESSINGPATHLIST_MAXFILESCOUNTALERT,((ProcessingPathList)pathList).getMaxFilesCountAlert(),BaseConstants.MAX_FILE_COUNT_ALERT,processingPathList.getName(), entityName,validateForImport,processingPathList.getClass().getName());
		isValidate(SystemParametersConstant.COMMONPATHLIST_WRITEFILEPATH,processingPathList.getWriteFilePath(),WRITE_PATH,entityName,processingPathList.getName(),validateForImport);
		isValidate(SystemParametersConstant.COMMONPATHLIST_READFILENAMEPREFIX, processingPathList.getReadFilenamePrefix(), READ_FILE_NAME_PATH, entityName, processingPathList.getName(),validateForImport);
		isValidate(SystemParametersConstant.COMMONPATHLIST_READFILENAMESUFFIX, processingPathList.getReadFilenameSuffix(), READ_FILE_NAME_SUFFIX, entityName, processingPathList.getName(),validateForImport);
		isValidate(SystemParametersConstant.COMMONPATHLIST_READFILENAMECONTAINS, processingPathList.getReadFilenameContains(), BaseConstants.READ_FILE_NAME_CONTAIN, entityName, processingPathList.getName(),validateForImport);
		isValidate(SystemParametersConstant.PARSER_READFILENAMEEXCLUDETYPES, processingPathList.getReadFilenameExcludeTypes(), BaseConstants.READ_FILE_NAME_EXCLUDE, entityName, processingPathList.getName(),validateForImport);
		
		if((!validateForImport) && (processingPathList.getParentDevice() == null || processingPathList.getParentDevice().getId() == 0)){
			setErrorFieldErrorMessage("" , PARENT_DEVICE, processingPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.parentDevice.id.invalid", getMessage("CollectionDriverPathList.parentDevice.id.invalid"));
		}
		
		if(processingPathList.getFileGrepDateEnabled()){
			isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_DATEFORMAT,processingPathList.getDateFormat(),"dateFormat",entityName, processingPathList.getName(),validateForImport);	
			boolean startIndexValid = isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_STARTINDEX, processingPathList.getStartIndex(),"startIndex",entityName,processingPathList.getName(),validateForImport, processingPathList.getClass().getName());
			boolean endIndexValid = isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_ENDINDEX, processingPathList.getEndIndex(),END_INDEX,entityName,processingPathList.getName(),validateForImport, processingPathList.getClass().getName());
			if(processingPathList.getStartIndex() == -1){
				setErrorFieldErrorMessage(String.valueOf(processingPathList.getStartIndex()), START_INDEX, processingPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.startIndex.invalid", getMessage("CollectionDriverPathList.startIndex.invalid"));
			}
			if(processingPathList.getEndIndex() == -1){
				setErrorFieldErrorMessage(String.valueOf(processingPathList.getEndIndex()), END_INDEX, processingPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.startIndex.invalid", getMessage("CollectionDriverPathList.startIndex.invalid"));
			}
			if(startIndexValid && endIndexValid){
				if(processingPathList.getEndIndex() != -1 && processingPathList.getStartIndex() == processingPathList.getEndIndex()){
					setErrorFieldErrorMessage(String.valueOf(processingPathList.getEndIndex()) , END_INDEX, processingPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.endIndex.startIndex.same.invalid", getMessage("CollectionDriverPathList.endIndex.startIndex.same.invalid"));
				}else if(processingPathList.getPosition()!=null && processingPathList.getPosition().equalsIgnoreCase(LEFT_POSITION) && processingPathList.getStartIndex() > processingPathList.getEndIndex()){
					setErrorFieldErrorMessage(String.valueOf(processingPathList.getEndIndex()) , END_INDEX, processingPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.endIndex.islesser.invalid", getMessage("CollectionDriverPathList.endIndex.islesser.invalid"));
				}else if(processingPathList.getPosition()!=null && processingPathList.getPosition().equalsIgnoreCase(RIGHT_POSITION) && processingPathList.getStartIndex() < processingPathList.getEndIndex()){
					setErrorFieldErrorMessage(String.valueOf(processingPathList.getEndIndex()) , START_INDEX, processingPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.startIndex.islesser.invalid", getMessage("CollectionDriverPathList.startIndex.islesser.invalid"));
				}	
			}
		}
		
		if(processingPathList.isDuplicateRecordPolicyEnabled()){
			/*isValidate(SystemParametersConstant.PROCESSING_DUPLICATECHECK_ALERTID, processingPathList.getAlertId(), "alertId", entityName, processingPathList.getName(), validateForImport);
			isValidate(SystemParametersConstant.PROCESSING_DUPLICATECHECK_ALERTDESCRIPTION, processingPathList.getAlertDescription(), "alertDescription", entityName, processingPathList.getName(), validateForImport);*/
			isValidate(SystemParametersConstant.PROCESSING_CHECK_UNIFIEDFIELDS, processingPathList.getUnifiedFields(), "unifiedFields", entityName, processingPathList.getName(), validateForImport);
			if(processingPathList.getDuplicateRecordPolicyType().equals(DuplicateRecordPolicyTypeEnum.ACROSS_FILE)){
				isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD, processingPathList.getAcrossFileDuplicateDateField(), "acrossFileDuplicateDateField", entityName, processingPathList.getName(),validateForImport);
				isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_DATEFORMAT, processingPathList.getAcrossFileDuplicateDateFieldFormat(), "acrossFileDuplicateDateFieldFormat", entityName, processingPathList.getName(),validateForImport);
				isValidate(SystemParametersConstant.PROCESSING_RECORD_CACHE_LIMIT, processingPathList.getAcrossFileDuplicateCDRCacheLimit(), "acrossFileDuplicateCDRCacheLimit", entityName, processingPathList.getName(), validateForImport, processingPathList.getClass().getName());
				isValidate(SystemParametersConstant.PROCESSING_CHECK_INTERVAL, processingPathList.getAcrossFileDuplicateDateInterval(), "acrossFileDuplicateDateInterval", entityName, processingPathList.getName(), validateForImport, processingPathList.getClass().getName());
			}
		}
	}
	
	
	public void validateConsolidationPathlistParams(Object target, Errors errors,List<ImportValidationErrors> importErrorList,String entityName,boolean validateForImport){
		logger.debug("Validating consolidation pathlist parameters.");
		if(validateForImport){
			this.importErrorList=importErrorList;
		}else{
			this.errors = errors;	
		}
		DataConsolidationPathList consPathList = (DataConsolidationPathList) target;
		isValidate(SystemParametersConstant.PATHLIST_NAME,consPathList.getName(),"name", entityName, consPathList.getName(), validateForImport);
		isValidate(SystemParametersConstant.PROCESSINGPATHLIST_MAXFILESCOUNTALERT, consPathList.getMaxCounterLimit(), "maxCounterLimit", consPathList.getName(), entityName, validateForImport, consPathList.getClass().getName());
		isValidate(SystemParametersConstant.PATHLIST_READFILEPATH, consPathList.getReadFilePath(), READ_FILE_NAME_PATH, entityName,consPathList.getName(), validateForImport);
		isValidate(SystemParametersConstant.COMMONPATHLIST_READFILENAMESUFFIX, consPathList.getReadFilenameSuffix(), READ_FILE_NAME_SUFFIX, entityName,consPathList.getName(), validateForImport);
		
		if((!validateForImport) && (consPathList.getParentDevice() == null || consPathList.getParentDevice().getId() == 0)){
			setErrorFieldErrorMessage("" , PARENT_DEVICE, consPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.parentDevice.id.invalid", getMessage("CollectionDriverPathList.parentDevice.id.invalid"));
		}
	}
	
	public void validateAggregationPathlistParams(Object target, Errors errors,List<ImportValidationErrors> importErrorList,String entityName,boolean validateForImport,AggregationService service){
		logger.debug("Validating aggregation pathlist parameters.");
		if(validateForImport){
			this.importErrorList=importErrorList;
		}else{
			this.errors = errors;	
		}
		AggregationServicePathList aggPathList = (AggregationServicePathList) target;
		AggregationDefinition aggDefinition = service.getAggregationDefinition();
		logger.debug("Aggreagation Definition :" + aggDefinition);
		if(aggDefinition != null){
			if(aggDefinition.getPartCDRField() != null && !aggDefinition.getPartCDRField().equalsIgnoreCase("NONE")){
				isValidate(SystemParametersConstant.DISTRIBUTION_PATHLIST_WRITE_PATH, aggPathList.getwPathNonAggregate(), "wPathNonAggregate", null, null, validateForImport);
				isValidate(SystemParametersConstant.DISTRIBUTION_PATHLIST_WRITE_PATH, aggPathList.getwPathAggregateError(), "wPathAggregateError", null, null, validateForImport);
				isValidate(SystemParametersConstant.AGGREGATION_PATH_OUTPUTFILENAME,aggPathList.getoFilePathNameForNonAgg(),"oFilePathNameForNonAgg", entityName, aggPathList.getName(), validateForImport);
				isValidate(SystemParametersConstant.AGGREGATION_PATH_OUTPUTFILENAME,aggPathList.getoFilePathNameForError(),"oFilePathNameForError", entityName, aggPathList.getName(), validateForImport);
			}
		}
		
		if((!validateForImport) && (aggPathList.getParentDevice() == null || aggPathList.getParentDevice().getId() == 0)){
			setErrorFieldErrorMessage("" , PARENT_DEVICE, aggPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.parentDevice.id.invalid", getMessage("CollectionDriverPathList.parentDevice.id.invalid"));
		}
		
		isValidate(SystemParametersConstant.PATHLIST_NAME,aggPathList.getName(),"name", entityName, aggPathList.getName(), validateForImport);
		isValidate(SystemParametersConstant.PATHLIST_READFILEPATH, aggPathList.getReadFilePath(), READ_FILE_NAME_PATH, entityName,aggPathList.getName(), validateForImport);
		isValidate(SystemParametersConstant.COMMONPATHLIST_READFILENAMEPREFIX, aggPathList.getReadFilenamePrefix(), READ_FILE_NAME_PATH, entityName, aggPathList.getName(),validateForImport);
		isValidate(SystemParametersConstant.COMMONPATHLIST_READFILENAMESUFFIX, aggPathList.getReadFilenameSuffix(), READ_FILE_NAME_SUFFIX, entityName,aggPathList.getName(), validateForImport);
		isValidate(SystemParametersConstant.PROCESSINGPATHLIST_MAXFILESCOUNTALERT, aggPathList.getMaxFilesCountAlert(), "maxFilesCountAlert",entityName, aggPathList.getName(), validateForImport, aggPathList.getClass().getName());
		isValidate(SystemParametersConstant.DISTRIBUTION_PATHLIST_WRITE_PATH, aggPathList.getWriteFilePath(), "writeFilePath", entityName, aggPathList.getName(), validateForImport);
		isValidate(SystemParametersConstant.AGGREGATION_PATH_OUTPUTFILENAME,aggPathList.getoFilePathName(),"oFilePathName", entityName, aggPathList.getName(), validateForImport);
		
		boolean oMinfileRangeValid=isValidate(SystemParametersConstant.AGGREGATION_PATH_OMINFILERANGE, aggPathList.getoFileMinRange(),"oFileMinRange", entityName, aggPathList.getName(), validateForImport,aggPathList.getName());		
		boolean oMaxFileRangeValid= isValidate(SystemParametersConstant.AGGREGATION_PATH_OMAXFILERANGE, aggPathList.getoFileMaxRange(), "oFileMaxRange", entityName, aggPathList.getName(), validateForImport, aggPathList.getName());
		
		if((oMinfileRangeValid &&  oMaxFileRangeValid ) && ((aggPathList.getoFileMinRange()  == -1  &&  aggPathList.getoFileMaxRange() >= 1) ||  (aggPathList.getoFileMinRange()  >= 1  &&  aggPathList.getoFileMaxRange() == -1))){
			setErrorFieldErrorMessage(String.valueOf(aggPathList.getoFileMaxRange()), "oFileMaxRange", aggPathList.getName(), entityName, validateForImport, "Service.maxFileRange.maxFileRange.both.invalid", getMessage("Service.maxFileRange.maxFileRange.both.invalid"));
		}
		
		if(oMinfileRangeValid &&  oMaxFileRangeValid && aggPathList.getoFileMaxRange() < aggPathList.getoFileMinRange()){
			setErrorFieldErrorMessage(String.valueOf(aggPathList.getoFileMaxRange()), "oFileMaxRange", aggPathList.getName(), entityName, validateForImport, "Service.maxFileRange.isLesser.invalid", getMessage("Service.maxFileRange.isLesser.invalid"));
		}
		
		boolean oMinfileRangeForNonAggValid=isValidate(SystemParametersConstant.AGGREGATION_PATH_OMINFILERANGE, aggPathList.getoFileMinRangeForNonAgg(),"oFileMinRangeForNonAgg", entityName, aggPathList.getName(), validateForImport,aggPathList.getName());		
		boolean oMaxFileRangeForNonAggValid= isValidate(SystemParametersConstant.AGGREGATION_PATH_OMAXFILERANGE, aggPathList.getoFileMaxRangeForNonAgg(), "oFileMaxRangeForNonAgg", entityName, aggPathList.getName(), validateForImport, aggPathList.getName());
		
		if((oMinfileRangeForNonAggValid &&  oMaxFileRangeForNonAggValid ) && ((aggPathList.getoFileMinRangeForNonAgg()  == -1  &&  aggPathList.getoFileMaxRangeForNonAgg() >= 1) ||  (aggPathList.getoFileMinRangeForNonAgg()  >= 1  &&  aggPathList.getoFileMaxRangeForNonAgg() == -1))){
			setErrorFieldErrorMessage(String.valueOf(aggPathList.getoFileMaxRangeForNonAgg()), "oFileMaxRangeForNonAgg", aggPathList.getName(), entityName, validateForImport, "Service.maxFileRange.maxFileRange.both.invalid", getMessage("Service.maxFileRange.maxFileRange.both.invalid"));
		}
		
		if(oMinfileRangeForNonAggValid &&  oMaxFileRangeForNonAggValid && aggPathList.getoFileMaxRangeForNonAgg() < aggPathList.getoFileMinRangeForNonAgg()){
			setErrorFieldErrorMessage(String.valueOf(aggPathList.getoFileMaxRangeForNonAgg()), "oFileMaxRangeForNonAgg", aggPathList.getName(), entityName, validateForImport, "Service.maxFileRange.isLesser.invalid", getMessage("Service.maxFileRange.isLesser.invalid"));
		}
		
		boolean oMinfileRangeForErrValid=isValidate(SystemParametersConstant.AGGREGATION_PATH_OMINFILERANGE, aggPathList.getoFileMinRangeForError(),"oFileMinRangeForError", entityName, aggPathList.getName(), validateForImport,aggPathList.getName());		
		boolean oMaxFileRangeForErrValid= isValidate(SystemParametersConstant.AGGREGATION_PATH_OMAXFILERANGE, aggPathList.getoFileMaxRangeForError(), "oFileMaxRangeForError", entityName, aggPathList.getName(), validateForImport, aggPathList.getName());
		
		if((oMinfileRangeForErrValid &&  oMaxFileRangeForErrValid ) && ((aggPathList.getoFileMinRangeForError()  == -1  &&  aggPathList.getoFileMaxRangeForError() >= 1) ||  (aggPathList.getoFileMinRangeForError()  >= 1  &&  aggPathList.getoFileMaxRangeForError() == -1))){
			setErrorFieldErrorMessage(String.valueOf(aggPathList.getoFileMaxRange()), "oFileMaxRangeForError", aggPathList.getName(), entityName, validateForImport, "Service.maxFileRange.maxFileRange.both.invalid", getMessage("Service.maxFileRange.maxFileRange.both.invalid"));
		}
		
		if(oMinfileRangeForErrValid &&  oMaxFileRangeForErrValid && aggPathList.getoFileMaxRangeForError() < aggPathList.getoFileMinRangeForError()){
			setErrorFieldErrorMessage(String.valueOf(aggPathList.getoFileMaxRangeForError()), "oFileMaxRangeForError", aggPathList.getName(), entityName, validateForImport, "Service.maxFileRange.isLesser.invalid", getMessage("Service.maxFileRange.isLesser.invalid"));
		}
		
		if(aggPathList.getFileGrepDateEnabled()){
			isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_DATEFORMAT,aggPathList.getDateFormat(),"dateFormat",entityName, aggPathList.getName(),validateForImport);	
			boolean startIndexValid = isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_STARTINDEX, aggPathList.getStartIndex(),"startIndex",entityName,aggPathList.getName(),validateForImport, aggPathList.getClass().getName());
			boolean endIndexValid = isValidate(SystemParametersConstant.COLLECTIONDRIVERPATHLIST_ENDINDEX, aggPathList.getEndIndex(),END_INDEX,entityName,aggPathList.getName(),validateForImport, aggPathList.getClass().getName());
			if(aggPathList.getStartIndex() == -1){
				setErrorFieldErrorMessage(String.valueOf(aggPathList.getStartIndex()), START_INDEX, aggPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.startIndex.invalid", getMessage("CollectionDriverPathList.startIndex.invalid"));
			}
			if(aggPathList.getEndIndex() == -1){
				setErrorFieldErrorMessage(String.valueOf(aggPathList.getEndIndex()), END_INDEX, aggPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.startIndex.invalid", getMessage("CollectionDriverPathList.startIndex.invalid"));
			}
			
			if(startIndexValid && endIndexValid){
				if(aggPathList.getEndIndex() != -1 && aggPathList.getStartIndex() == aggPathList.getEndIndex()){
					setErrorFieldErrorMessage(String.valueOf(aggPathList.getEndIndex()) , END_INDEX, aggPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.endIndex.startIndex.same.invalid", getMessage("CollectionDriverPathList.endIndex.startIndex.same.invalid"));
				}else if(aggPathList.getStartIndex() > aggPathList.getEndIndex()){
					setErrorFieldErrorMessage(String.valueOf(aggPathList.getEndIndex()) , END_INDEX, aggPathList.getName(), entityName, validateForImport,  "CollectionDriverPathList.endIndex.islesser.invalid", getMessage("CollectionDriverPathList.endIndex.islesser.invalid"));
				}
			}
		}
	}
	
	public void validateConsolidationDefinationMapping(Object target, Errors errors,List<ImportValidationErrors> importErrorList,String entityName,boolean validateForImport){
		logger.debug("Validating consolidation pathlist parameters.");
		if(validateForImport){
			this.importErrorList=importErrorList;
		}else{
			this.errors = errors;	
		}
		DataConsolidationMapping mapping = (DataConsolidationMapping) target;
		isValidate(SystemParametersConstant.CONSOLIDATION_PATH_MAPPING_NAME, mapping.getMappingName(), "mappingName", entityName, mapping.getMappingName(), validateForImport);
		if(isValidate(SystemParametersConstant.DISTRIBUTION_PATHLIST_WRITE_PATH,mapping.getDestPath(),"destPath", entityName, mapping.getMappingName(), validateForImport)){
			// If destPath not empty then going to check same write path exists with consolidation mapping name
			long totalCount = consolidationDefinitionService.getDataConsolidationMappingCountByMappingNameAndDestPath(mapping.getId(),mapping.getMappingName(),mapping.getDestPath());
			if(totalCount >= 1) 
				errors.rejectValue("destPath", "DistributionDriverPathList.writeFilePath.duplicate", getMessage("DistributionDriverPathList.writeFilePath.duplicate"));
		}
		if(!mapping.getRecordSortingType().contains("NA")) {
			isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,mapping.getRecordSortingField(),"recordSortingField",null,mapping.getRecordSortingField(),validateForImport);
		}
		isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,mapping.getFieldNameForCount(),"fieldNameForCount",null,mapping.getFieldNameForCount(),validateForImport);
		isValidate(SystemParametersConstant.CONSOLIDATION_PATH_MAPPING_PROCESS_RECORD, mapping.getProcessRecordLimit(),"processRecordLimit", entityName, mapping.getMappingName(), validateForImport, mapping.getClass().getName());
		if(mapping.isFileSequence()){
			boolean minFileSeq = isValidate(SystemParametersConstant.CONSOLIDATION_PATH_MAPPING_FILE_MIN_RANGE, mapping.getMinSeqRange(), "minSeqRange", entityName, mapping.getMappingName(), validateForImport, mapping.getClass().getName());
			boolean maxFileSeq = isValidate(SystemParametersConstant.CONSOLIDATION_PATH_MAPPING_FILE_MAX_RANGE, mapping.getMaxSeqRange(), BaseConstants.MAX_SEQ_RANGE, entityName, mapping.getMappingName(), validateForImport, mapping.getClass().getName());
			if(minFileSeq && maxFileSeq){
				if(mapping.getMinSeqRange()  == -1){
					setErrorFieldErrorMessage(String.valueOf(mapping.getMaxSeqRange()), "minSeqRange", mapping.getMappingName(), entityName, validateForImport, "dataconsolidationMapping.minFileRange.both.invalid", getMessage("dataconsolidationMapping.minFileRange.both.invalid"));
				}
				if(mapping.getMaxSeqRange() == -1){
					setErrorFieldErrorMessage(String.valueOf(mapping.getMaxSeqRange()), BaseConstants.MAX_SEQ_RANGE, mapping.getMappingName(), entityName, validateForImport, "dataconsolidationMapping.maxFileRange.both.invalid", getMessage("dataconsolidationMapping.maxFileRange.both.invalid"));
				}
				if(mapping.getMaxSeqRange() < mapping.getMinSeqRange()){
					setErrorFieldErrorMessage(String.valueOf(mapping.getMaxSeqRange()), BaseConstants.MAX_SEQ_RANGE, mapping.getMappingName(), entityName, validateForImport, "dataconsolidationMapping.maxFileRange.isLesser.invalid", getMessage("dataconsolidationMapping.maxFileRange.isLesser.invalid"));
				}
			}
		}
	}

	public void validateChareRenameParameters(Object target, Errors errors,String entityName,boolean validateForImport, List<ImportValidationErrors> importErrorList, boolean isFileRenameAgent) {

		logger.debug("Going to validate Char rename operation parameters.");
		
		charRenameOperation = (CharRenameOperation) target;
		String classFullName = charRenameOperation.getClass().getName();

		setErrorObject(errors, validateForImport, importErrorList);
		
		if(charRenameOperation.getPaddingValue() != null && charRenameOperation.getPaddingValue().length() > 1)
			errors.rejectValue("paddingValue", "CharRenameOperation.paddingValue.invalid", getMessage("CharRenameOperation.paddingValue.invalid"));
		isValidate(SystemParametersConstant.CHAR_OPERATION_QUERY,charRenameOperation.getQuery(),"query",entityName,BaseConstants.COLLECTION_SERVICE,validateForImport);
		isValidate(SystemParametersConstant.CHAR_OPERATION_DEFAULT_VALUE,charRenameOperation.getDefaultValue(),"defaultValue",entityName,BaseConstants.COLLECTION_SERVICE,validateForImport);
		
		boolean isValidStartIndex = isValidate(SystemParametersConstant.CHAR_OPERATION_START_INDEX,charRenameOperation.getStartIndex(),"startIndex",entityName, null, validateForImport,classFullName);

		boolean isValidEndIndex = isValidate(SystemParametersConstant.CHAR_OPERATION_END_INDEX,charRenameOperation.getEndIndex(),"endIndex",entityName, null, validateForImport,classFullName);
		if(isValidStartIndex &&  isValidEndIndex  && ( (charRenameOperation.getStartIndex()  == -1  &&  charRenameOperation.getEndIndex() >= 0) ||  (charRenameOperation.getStartIndex()  >= 0  &&  charRenameOperation.getEndIndex() == -1))){
			setErrorFieldErrorMessage(String.valueOf(charRenameOperation.getEndIndex()), "endIndex", null, entityName, validateForImport, "CharRenameOperation.startIndex.endIndex.both.invalid", getMessage("CharRenameOperation.startIndex.endIndex.both.invalid"));
		}
		
		if(charRenameOperation.getStartIndex()!=-1 && charRenameOperation.getStartIndex()<=0){
			setErrorFieldErrorMessage(String.valueOf(charRenameOperation.getStartIndex()), "startIndex", null, entityName, validateForImport, "CharRenameOperation.startIndex.invalid", getMessage("CharRenameOperation.startIndex.invalid"));
		} 
		if(charRenameOperation.getEndIndex()!=-1 && charRenameOperation.getEndIndex()<=0){
			setErrorFieldErrorMessage(String.valueOf(charRenameOperation.getEndIndex()), "endIndex", null, entityName, validateForImport, "CharRenameOperation.endIndex.invalid", getMessage("CharRenameOperation.endIndex.invalid"));
		}
		
		if (isValidStartIndex &&  isValidEndIndex && charRenameOperation.getStartIndex() > charRenameOperation.getEndIndex() ){ // Min index must be smaller than Max index.
			if(validateForImport){
				ImportValidationErrors errorsList = new ImportValidationErrors(BaseConstants.COLLECTION_SERVICE, BaseConstants.COLLECTION_SERVICE,"endIndex", String.valueOf(charRenameOperation.getEndIndex()), getMessage(SystemParametersConstant.CHAR_OPERATION_END_INDEX+".invalid"));
	    		importErrorList.add(errorsList);
			}else{
				errors.rejectValue("endIndex", "CharRenameOperation.endIndex.islesser.invalid", getMessage("CharRenameOperation.endIndex.islesser.invalid"));	
			}
		}
		
		if ("Other".equals(charRenameOperation.getDateFormat()))
			isValidate(SystemParametersConstant.COLLLECTION_CHAR_RENAME_SRCDATEFORMAT,charRenameOperation.getSrcDateFormat(),"srcDateFormat",entityName,BaseConstants.COLLECTION_SERVICE,validateForImport);
		
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
			if(charRenameOperation.getPathList() != null) {
				long totalCount = charRenameOperationService.getSeqNumberCountByCollectionServicePathListId(charRenameOperation.getSequenceNo(), charRenameOperation.getPathList().getId(), charRenameOperation.getId());
				if(totalCount >= 1) {
					errors.rejectValue("sequenceNo", "sequence.number.duplicate", getMessage("sequence.number.duplicate"));
					return;
				}
			}
		}
		return;
	}
	
}
