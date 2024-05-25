package com.elitecore.sm.drivers.validator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.validator.routines.InetAddressValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.constants.EngineConstants;
import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.common.validator.ImportValidationErrors;
import com.elitecore.sm.drivers.model.CollectionDriver;
import com.elitecore.sm.drivers.model.ConnectionParameter;
import com.elitecore.sm.drivers.model.DatabaseDistributionDriver;
import com.elitecore.sm.drivers.model.DatabaseDistributionDriverAttribute;
import com.elitecore.sm.drivers.model.DistributionDriver;
import com.elitecore.sm.drivers.model.Drivers;
import com.elitecore.sm.drivers.model.FTPCollectionDriver;
import com.elitecore.sm.drivers.model.FTPDistributionDriver;
import com.elitecore.sm.drivers.model.LocalCollectionDriver;
import com.elitecore.sm.drivers.model.LocalDistributionDriver;
import com.elitecore.sm.drivers.model.SFTPCollectionDriver;
import com.elitecore.sm.drivers.service.DriversService;
import com.elitecore.sm.pathlist.model.FileGroupingParameterCollection;

/**
 * Validation class for driver
 * @author avani.panchal
 *
 */
@Component
public class DriverValidator  extends BaseValidator{
	
private Drivers driver;

@Autowired
@Qualifier(value="driversService")
DriversService driversService;

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {

		return Drivers.class.isAssignableFrom(clazz);
	}

	/**
	 * Validate Driver Parameter
	 * @param target
	 * @param errors
	 * @param importErrorList
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateDriverParameter(Object target, Errors errors,List<ImportValidationErrors> importErrorList,String entityName,boolean validateForImport) {
		
		if(validateForImport){
			this.importErrorList=importErrorList;
		}else{
			this.errors = errors;			
		}

		driver = (Drivers) target;
		
		if(driver != null && driver.getId() > 0){
			Drivers driverDBVal = driversService.getDriverById(driver.getId());
			if(driverDBVal != null && driverDBVal.getId() > 0 && driverDBVal.getDriverType() !=null && driverDBVal.getDriverType().getAlias() != null){
				if((driverDBVal.getDriverType().getAlias().equalsIgnoreCase(EngineConstants.LOCAL_COLLECTION_DRIVER)) &&
						(driver.getDriverType().getAlias().equalsIgnoreCase(EngineConstants.SFTP_COLLECTION_DRIVER)
								|| driver.getDriverType().getAlias().equalsIgnoreCase(EngineConstants.FTP_COLLECTION_DRIVER))
						){
					errors.rejectValue("driverType", "Drivers.type.invalid", getMessage("Drivers.type.invalid"));
					
				}else if((driver.getDriverType().getAlias().equalsIgnoreCase(EngineConstants.LOCAL_COLLECTION_DRIVER)) &&
						(driverDBVal.getDriverType().getAlias().equalsIgnoreCase(EngineConstants.SFTP_COLLECTION_DRIVER)
								|| driverDBVal.getDriverType().getAlias().equalsIgnoreCase(EngineConstants.FTP_COLLECTION_DRIVER))
						){
					errors.rejectValue("driverType", "Drivers.type.invalid", getMessage("Drivers.type.invalid"));
				}else if((driverDBVal.getDriverType().getAlias().equalsIgnoreCase(EngineConstants.LOCAL_DISTRIBUTION_DRIVER) || 
						driverDBVal.getDriverType().getAlias().equalsIgnoreCase(EngineConstants.DATABASE_DISTRIBUTION_DRIVER)) &&
						(driver.getDriverType().getAlias().equalsIgnoreCase(EngineConstants.SFTP_DISTRIBUTION_DRIVER)
								|| driver.getDriverType().getAlias().equalsIgnoreCase(EngineConstants.FTP_DISTRIBUTION_DRIVER))
						){
					errors.rejectValue("driverType", "Drivers.type.invalid", getMessage("Drivers.type.invalid"));
				}else if((driver.getDriverType().getAlias().equalsIgnoreCase(EngineConstants.LOCAL_DISTRIBUTION_DRIVER) ||
						driver.getDriverType().getAlias().equalsIgnoreCase(EngineConstants.DATABASE_DISTRIBUTION_DRIVER)) &&
						(driverDBVal.getDriverType().getAlias().equalsIgnoreCase(EngineConstants.SFTP_DISTRIBUTION_DRIVER)
								|| driverDBVal.getDriverType().getAlias().equalsIgnoreCase(EngineConstants.FTP_DISTRIBUTION_DRIVER))
						){
					errors.rejectValue("driverType", "Drivers.type.invalid", getMessage("Drivers.type.invalid"));
				}
			}
		}
		
		if(driver!=null && driver.getName()!=null) {
			String driverName = driver.getName();
			isValidate(SystemParametersConstant.DRIVERS_NAME,driverName,"name",entityName,driverName,validateForImport);
		}
	}
	
	/**
	 * Validate Driver Configuration parameter
	 * @param target
	 * @param errors
	 * @param importErrorList
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateDriverConfiguration(Object target, Errors errors,List<ImportValidationErrors> importErrorList,String entityName,boolean validateForImport){
		
		if(validateForImport){
			this.importErrorList=importErrorList;
		}else{
			this.errors = errors;
		}
		
		if(target instanceof CollectionDriver){
			driver = (CollectionDriver) target;
		}else {
			driver = (DistributionDriver) target;
		}
		
		
		isValidate(SystemParametersConstant.DRIVERS_NAME,driver.getName(),"name",entityName,driver.getName(),validateForImport);
		
		if(driver instanceof FTPCollectionDriver){
			FTPCollectionDriver ftpCollectionDriver = (FTPCollectionDriver) driver;
			if(org.apache.commons.lang3.StringUtils.equalsIgnoreCase(ftpCollectionDriver.getDriverType().getType(),"SFTP DISTRIBUTION DRIVER")){
				boolean flag = validatePasswordKeyFileLocation(ftpCollectionDriver.getFtpConnectionParams().getPassword(),ftpCollectionDriver.getFtpConnectionParams().getKeyFileLocation());
				if(!flag){
					String driverName = ftpCollectionDriver.getName();
					String errorMsgValue = getMessage(SystemParametersConstant.KEY_FILE_OR_PASS+".invalid");
					setErrorFieldErrorMessage(ftpCollectionDriver.getFtpConnectionParams().getKeyFileLocation() , "ftpConnectionParams.keyFileLocation", driverName, entityName, validateForImport, "key.file.location.or.password.invalid", errorMsgValue);
					setErrorFieldErrorMessage(ftpCollectionDriver.getFtpConnectionParams().getKeyFileLocation() , "ftpConnectionParams.password", driverName, entityName, validateForImport, "key.file.location.or.password.invalid", errorMsgValue);
				}
			}
			validateDriverConnectionParams(ftpCollectionDriver.getFtpConnectionParams(), BaseConstants.FTP_COLLECTION_DRIVER, validateForImport,driver.getName());
			if(ftpCollectionDriver.getMyFileFetchParams().isFileFetchRuleEnabled()){
				isValidate(SystemParametersConstant.FTPDRIVER_FILE_FETCH_INTERVAL, ftpCollectionDriver.getMyFileFetchParams().getFileFetchIntervalMin(), "myFileFetchParams.FileFetchIntervalMin" ,
						ftpCollectionDriver.getName(), entityName, validateForImport, BaseConstants.DRIVER_FULL_CLASS_NAME);
			}
		}else if(driver instanceof FTPDistributionDriver){
			FTPDistributionDriver ftpDistributionDriver = (FTPDistributionDriver) driver;
			if(org.apache.commons.lang3.StringUtils.equalsIgnoreCase(ftpDistributionDriver.getDriverType().getType(),"SFTP DISTRIBUTION DRIVER")){
				boolean flag = validatePasswordKeyFileLocation(ftpDistributionDriver.getFtpConnectionParams().getPassword(),ftpDistributionDriver.getFtpConnectionParams().getKeyFileLocation());
				if(!flag){
					String driverName = ftpDistributionDriver.getName();
					String errorMsgValue = getMessage(SystemParametersConstant.KEY_FILE_OR_PASS+".invalid");
					setErrorFieldErrorMessage(ftpDistributionDriver.getFtpConnectionParams().getKeyFileLocation() , "ftpConnectionParams.keyFileLocation", driverName, entityName, validateForImport, "key.file.location.or.password.invalid", errorMsgValue);
					setErrorFieldErrorMessage(ftpDistributionDriver.getFtpConnectionParams().getKeyFileLocation() , "ftpConnectionParams.password", driverName, entityName, validateForImport, "key.file.location.or.password.invalid", errorMsgValue);
				}
			}
			
			validateDriverConnectionParams(ftpDistributionDriver.getFtpConnectionParams(),  BaseConstants.FTP_DISTRIBUTION_DRIVER, validateForImport,driver.getName());
			validateControlFileParameter((DistributionDriver)driver,entityName,validateForImport);
			
		}else if(driver instanceof DatabaseDistributionDriver){
			DatabaseDistributionDriver databaseDistributionDriver = (DatabaseDistributionDriver) driver;
			isValidate(SystemParametersConstant.DATABASE_DIS_DRIVER_TABLE_NAME,databaseDistributionDriver.getTableName(),"tableName",entityName,databaseDistributionDriver.getTableName(),validateForImport);
		}
		if(driver instanceof FTPCollectionDriver || driver instanceof SFTPCollectionDriver || driver instanceof LocalCollectionDriver){
			FileGroupingParameterCollection collectionDriver = ((CollectionDriver)driver).getFileGroupingParameter();
			if(collectionDriver != null && collectionDriver.isFileGroupEnable()){
				isValidate(SystemParametersConstant.COLLECTIONDRIVER_FILEGROUPINGPARAMETER_DUPLICATEPATH,collectionDriver.getDuplicateDirPath(),
						"fileGroupingParameter.duplicateDirPath",entityName,driver.getName(),validateForImport);
			}
		}
		if (driver instanceof LocalDistributionDriver)
		{
			validateControlFileParameter((DistributionDriver)driver,entityName,validateForImport);
		}
		validateOperationalParameter(driver,entityName,validateForImport);
		
	}
	
	
	/**
	 * Validate Control File Parameters
	 * @param driver
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	private void validateControlFileParameter(DistributionDriver driver,String entityName,boolean validateForImport){

		/*driver = (DistributionDriver) driver;*/
		String driverName = driver.getName();
		if (driver.getDriverControlFileParams().isControlFileEnabled())
		{ 
			isValidate(SystemParametersConstant.DRIVERS_CONTROLFILELOCATION,driver.getDriverControlFileParams().getControlFileLocation(),"driverControlFileParams.controlFileLocation",driverName, entityName,validateForImport);
			isValidate(SystemParametersConstant.DRIVERS_ROLLINGTIME,driver.getDriverControlFileParams().getFileRollingDuration(),"driverControlFileParams.fileRollingDuration",driverName, entityName,validateForImport,BaseConstants.DRIVER_FULL_CLASS_NAME);
			isValidate(SystemParametersConstant.DRIVERS_CONTROLFILEATTRIBUTES,driver.getDriverControlFileParams().getAttributes(),"driverControlFileParams.attributes",driverName, entityName,validateForImport);
			
			boolean startTime = isValidate(SystemParametersConstant.DRIVERS_ROLLINGSTARTTIME,driver.getDriverControlFileParams().getFileRollingStartTime(),"driverControlFileParams.fileRollingStartTime",driverName, entityName,validateForImport);
			if (startTime) {
				if (!validateRollingStartTime(driver.getDriverControlFileParams().getFileRollingStartTime())) {
					setErrorFieldErrorMessage(driver.getDriverControlFileParams().getFileRollingStartTime(), "driverControlFileParams.fileRollingStartTime", driverName, entityName, validateForImport,
							"error.Drivers.rollingStartTime.invalid", getMessage("Drivers.rollingStartTime.invalid"));
				}
			}

			isValidate(SystemParametersConstant.DRIVERS_CONTROLFILENAME,driver.getDriverControlFileParams().getControlFileName(),"driverControlFileParams.controlFileName",driverName, entityName,validateForImport);
			
			if (driver.getDriverControlFileParams().isFileSeqEnable())
			{
				boolean minFileRange = isValidate(SystemParametersConstant.DRIVERS_CONTROLFILEMINSEQ,driver.getControlFileSeq().getStartRange(),"controlFileSeq.startRange",driverName, entityName,validateForImport,BaseConstants.DRIVER_FULL_CLASS_NAME);
				boolean maxFileRange = isValidate(SystemParametersConstant.DRIVERS_CONTROLFILEMAXSEQ,driver.getControlFileSeq().getEndRange(),"controlFileSeq.endRange",driverName, entityName,validateForImport,BaseConstants.DRIVER_FULL_CLASS_NAME);
				
			 	if (minFileRange && maxFileRange && (driver.getControlFileSeq().getStartRange() > driver.getControlFileSeq().getEndRange())){
				 setErrorFieldErrorMessage(String.valueOf(driver.getControlFileSeq().getEndRange()) , "controlFileSeq.endRange", driverName, entityName, validateForImport, "Drivers.controlFileMaxSeq.invalid.min.max", getMessage("Drivers.controlFileMaxSeq.invalid.min.max"));
				}
			}
		}
	}
	
	private boolean validatePasswordKeyFileLocation(String password, String keyFileLocation) {
		if(org.apache.commons.lang3.StringUtils.isBlank(password)&&org.apache.commons.lang3.StringUtils.isBlank(keyFileLocation))
			return false;
		return true;
	}

	/**
	 * Method will validate Driver connection parameters.
	 * @param driver
	 * @param entityName
	 * @param validateForImport
	 */
	private void validateDriverConnectionParams(ConnectionParameter connectionParams,String entityName, boolean validateForImport,String driverName){
		if(connectionParams != null){
			
			String fullClassName = connectionParams.getClass().getName();
			
			
			isValidate(SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_TIMEOUT,connectionParams.getTimeout(),"ftpConnectionParams.timeout",driver.getName(),entityName,validateForImport,fullClassName);
			isValidate(SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_FILESEPARATOR,connectionParams.getFileSeparator(),"ftpConnectionParams.fileSeparator",entityName,driver.getName(),validateForImport);
			
			if(validateForImport){
				if(!StringUtils.isEmpty(connectionParams.getiPAddressHost()) || !StringUtils.isEmpty(connectionParams.getUsername()) || !StringUtils.isEmpty(connectionParams.getPassword())){
					if(connectionParams.getiPAddressHostList()!=null){
							List<String> ipAddressList = new ArrayList<>();
							for (int i = 0; i < connectionParams.getiPAddressHostList().size(); i++) {
								ipAddressList.add(connectionParams.getiPAddressHostList().get(i).getiPAddressHost());
							}
							String ipAddress = String.join(",", ipAddressList);
							connectionParams.setiPAddressHost(ipAddress);
						validateIPAddress(connectionParams.getiPAddressHost(),validateForImport,entityName,driverName);
					}
					isValidate(SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_USERNAME,connectionParams.getUsername(),"ftpConnectionParams.username",entityName,driver.getName(),validateForImport);
					if(org.apache.commons.lang3.StringUtils.isBlank(connectionParams.getKeyFileLocation())){
						isValidate(SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_PASSWORD,connectionParams.getPassword(),"ftpConnectionParams.password",entityName,driver.getName(),validateForImport);
					}
				}
				
			}else{
				validateIPAddress(connectionParams.getiPAddressHost(),validateForImport,entityName,driverName);
				isValidate(SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_USERNAME,connectionParams.getUsername(),"ftpConnectionParams.username",entityName,driver.getName(),validateForImport);
				if(org.apache.commons.lang3.StringUtils.isBlank(connectionParams.getKeyFileLocation())){
					isValidate(SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_PASSWORD,connectionParams.getPassword(),"ftpConnectionParams.password",entityName,driver.getName(),validateForImport);
				}
			}
			
			isValidate(SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_PORT,connectionParams.getPort(), "ftpConnectionParams.port",driver.getName(),entityName,validateForImport,fullClassName);
			
		}
	}
	
	/**
	 * Validate Operational Parameter
	 * @param driver
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	private void validateOperationalParameter(Drivers driver,String entityName,boolean validateForImport){
		
		if(driver instanceof CollectionDriver){
			driver = (CollectionDriver) driver;
		}else{
			driver = (DistributionDriver) driver;
		}
		
		String driverName = driver.getName();
		
			isValidate(SystemParametersConstant.COLLECTIONDRIVER_NOFILEALERT,driver.getNoFileAlert(),"noFileAlert",driverName, entityName,validateForImport,BaseConstants.DRIVER_FULL_CLASS_NAME);
			boolean minFileRange = isValidate(SystemParametersConstant.DRIVERS_MINFILERANGE,driver.getMinFileRange(),"minFileRange",driverName, entityName,validateForImport,BaseConstants.DRIVER_FULL_CLASS_NAME);
			boolean maxFileRange = isValidate(SystemParametersConstant.DRIVERS_MAXFILERANGE,driver.getMaxFileRange(),"maxFileRange",driverName, entityName,validateForImport,BaseConstants.DRIVER_FULL_CLASS_NAME);
			
		 	if (minFileRange && maxFileRange && (driver.getMinFileRange() > driver.getMaxFileRange() )){
			 setErrorFieldErrorMessage(String.valueOf(driver.getMaxFileRange()) , "maxFileRange", driverName, entityName, validateForImport, "Drivers.maxFileRange.islesser.invalid", getMessage("Drivers.maxFileRange.islesser.invalid"));
			}
		 	isValidate(SystemParametersConstant.DRIVERS_MAXRETRYCOUNT,driver.getMaxRetrycount(),"maxRetrycount",driverName, entityName,validateForImport,BaseConstants.DRIVER_FULL_CLASS_NAME); 
	}
	
	
	/**
	 * validate host Ip address
	 * @param ipAddress
	 * @param validateForImport
	 */
	private void validateIPAddress(String ipAddress,boolean validateForImport,String entityName,String driverName) {
		InetAddressValidator ipValidator = new InetAddressValidator();
		String errorMsgValue ;
		if( ipAddress!=null && !StringUtils.isEmpty(ipAddress)){
			String delims = ","; // so the delimiters is: ,
			 String[] ipAddressArray= ipAddress.split(delims);
			 
			if(!findDuplicateElementFromArray(ipAddressArray).isEmpty()) {
				errorMsgValue = getMessage(SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_IPADDRESSHOST+".duplicate");
				setErrorFieldErrorMessage(ipAddress , SystemParametersConstant.FTPCONNECTIONPARAMS_IPADDRESSHOST, driverName, entityName, validateForImport, SystemParametersConstant.FTPCONNECTIONPARAMS_IPADDRESSHOST_INVALID, errorMsgValue);
	
			}else {			 
				for(int i=0;i<ipAddressArray.length;i++){
					if(ipAddressArray[i]!=null && !StringUtils.isEmpty(ipAddressArray[i])){
					if(!(ipValidator.isValidInet4Address(ipAddressArray[i]) || ipValidator.isValidInet6Address(ipAddressArray[i])) ){
						errorMsgValue = getMessage(SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_IPADDRESSHOST+".invalid");
						setErrorFieldErrorMessage(ipAddressArray[i] , SystemParametersConstant.FTPCONNECTIONPARAMS_IPADDRESSHOST, driverName, entityName, validateForImport, SystemParametersConstant.FTPCONNECTIONPARAMS_IPADDRESSHOST_INVALID, errorMsgValue);
					}
					}
					else{
						errorMsgValue = getMessage(SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_IPADDRESSHOST+".invalid");
						setErrorFieldErrorMessage(ipAddress , SystemParametersConstant.FTPCONNECTIONPARAMS_IPADDRESSHOST, driverName, entityName, validateForImport, SystemParametersConstant.FTPCONNECTIONPARAMS_IPADDRESSHOST_INVALID, errorMsgValue);
						return;
					}
				}
			}
		}else if(StringUtils.isEmpty(ipAddress)){
			errorMsgValue = getMessage(SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_IPADDRESSHOST+".invalid");
			setErrorFieldErrorMessage(ipAddress , SystemParametersConstant.FTPCONNECTIONPARAMS_IPADDRESSHOST, driverName, entityName, validateForImport, SystemParametersConstant.FTPCONNECTIONPARAMS_IPADDRESSHOST_INVALID, errorMsgValue);
		}
	}
	
	
	/**
	 * 
	 * Validate Driver parameter for import
	 * @param driver
	 * @param driverImportErrorList
	 * @return
	 */
	public void validateDriverForImport(Drivers driver,List<ImportValidationErrors> importErrorList){
		logger.debug("Validate Import Operation for Entity: "+BaseConstants.DRIVER + " Name: "+driver.getName()) ;
		validateDriverParameter(driver,null,importErrorList,BaseConstants.DRIVER,true);
		validateDriverConfiguration(driver,null,importErrorList,BaseConstants.DRIVER,true);
		
	}
	
	/**
	 * Method will validate all basic driver attribute parameter.
	 * @param target
	 * @param errors
	 * @param entityName
	 * @param validateForImport
	 */
	public void validateDriverAttributeParameter(Object target, Errors errors,String entityName,boolean validateForImport,  List<ImportValidationErrors> importErrorList){
		
		if(validateForImport){
			this.importErrorList=importErrorList;
		}else{
			this.errors = errors;			
		}
		DatabaseDistributionDriverAttribute driverAttr = (DatabaseDistributionDriverAttribute)target;
		setErrorObject(errors, validateForImport, importErrorList);
		isValidate(SystemParametersConstant.DATABASE_DIS_DRIVER_ATTRIBUTE_DATABASEFIELDNAME,driverAttr.getDatabaseFieldName(),"databaseFieldName",entityName,driverAttr.getDatabaseFieldName(),validateForImport);
		isValidate(SystemParametersConstant.DATABASE_DIS_DRIVER_ATTRIBUTE_DATATYPE,driverAttr.getDataType(),"dataType",entityName,driverAttr.getDataType(),validateForImport);
		//MEDSUP-2004
		//isValidate(SystemParametersConstant.ATTR_UNIFIED_FIELD,driverAttr.getUnifiedFieldName(),"unifiedFieldName",entityName,driverAttr.getUnifiedFieldName(),validateForImport);
		//MEDSUP-2029
		if(driverAttr.isPaddingEnable()){	
			isValidate(SystemParametersConstant.DATABASE_DIS_DRIVER_ATTRIBUTE_LENGTH,String.valueOf(driverAttr.getLength()),"length",entityName,String.valueOf(driverAttr.getLength()),validateForImport);					
			isValidate(SystemParametersConstant.DATABASE_DIS_DRIVER_ATTRIBUTE_PADDINGCHAR,driverAttr.getPaddingChar(),"paddingChar",entityName,driverAttr.getPaddingChar(),validateForImport);	
			if(org.apache.commons.lang3.StringUtils.isNotBlank(driverAttr.getPrefix())){	
				isValidate(SystemParametersConstant.DATABASE_DIS_DRIVER_ATTRIBUTE_PREFIX,driverAttr.getPrefix(),"prefix",entityName,driverAttr.getPrefix(),validateForImport);	
			}	
			if(org.apache.commons.lang3.StringUtils.isNotBlank(driverAttr.getSuffix())){	
				isValidate(SystemParametersConstant.DATABASE_DIS_DRIVER_ATTRIBUTE_SUFFIX,driverAttr.getSuffix(),"suffix",entityName,driverAttr.getSuffix(),validateForImport);	
			}	
		}
	}
	
	public boolean validateRollingStartTime(String startTime) {
		 if(startTime != null && startTime != ""){
	            try{
	                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
	                simpleDateFormat.setLenient(false);
	                logger.debug(simpleDateFormat.parse(startTime));
	                return true;
	            } catch (IllegalArgumentException | ParseException e){
	            	
	            	logger.debug("Inside catch..So date format is not valid..IllegalArgumentException"+e);
	            	return false;
	            }
	        }
		 else{
	        return true;
		 }
	}
	
	public void validateConnectionParameter(ConnectionParameter target, Errors errors,List<ImportValidationErrors> importErrorList,String entityName,boolean validateForImport,String driverName){
		if(validateForImport){
			this.importErrorList=importErrorList;
		}else{
			this.errors = errors;
		}
		validateIPAddress(target.getiPAddressHost(),validateForImport,entityName,driverName);
		isValidate(SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_USERNAME,target.getUsername(),"ftpConnectionParams.username",entityName,driverName,validateForImport);
		if(org.apache.commons.lang3.StringUtils.isBlank(target.getKeyFileLocation())){
				isValidate(SystemParametersConstant.FTPCOLLECTIONDRIVER_FTPCONNECTIONPARAMS_PASSWORD,target.getPassword(),"ftpConnectionParams.password",entityName,driverName,validateForImport);
		}
	}
	
	public static Set<String> findDuplicateElementFromArray(String[] ipAddress) { 
		return Stream.of(ipAddress) 
			.collect(Collectors.groupingBy(Function.identity(),Collectors.counting())) 
			.entrySet() 
			.stream() 
			.filter(m -> m.getValue() > 1) 
			.map(Map.Entry::getKey) 
            .collect(Collectors.toSet());  
	} 


}
