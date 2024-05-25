package com.elitecore.sm.roaming.validator;

import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import com.elitecore.sm.common.constants.SystemParametersConstant;
import com.elitecore.sm.common.validator.BaseValidator;
import com.elitecore.sm.roaming.model.FileManagement;
import com.elitecore.sm.roaming.model.FileSequenceManagement;
import com.elitecore.sm.roaming.model.HostConfiguration;
import com.elitecore.sm.roaming.model.RoamingParameter;
import com.elitecore.sm.roaming.model.TestSimManagementData;
@Component
public class RoamingValidator extends BaseValidator{
	
	private HostConfiguration hostConfiguration;
	private RoamingParameter roamingParameter;
	private FileSequenceManagement fileSequenceManagement;
	
	public void validateHostConfigurationDetails(Object target, Errors errors,String moduleName,boolean validateForImport){
		this.errors = errors;
		hostConfiguration = (HostConfiguration)target;
		
		
		isValidate(SystemParametersConstant.HOST_CONFIGURATION_NAME,hostConfiguration.getName(),"name",moduleName,null,validateForImport);
		isValidate(SystemParametersConstant.HOST_CONFIGURATION_PMN_CODE,hostConfiguration.getPmncode(),"pmncode",moduleName,hostConfiguration.getPmncode(),validateForImport);
		isValidate(SystemParametersConstant.HOST_CONFIGURATION_TADIG_CODE,hostConfiguration.getTadigcode(),"tadigcode",moduleName,hostConfiguration.getTadigcode(),validateForImport);
	
	}

	public void validateRoamingParameterDetails(Object target, Errors errors,String moduleName,boolean validateForImport) {
		this.errors = errors;
		
		roamingParameter= (RoamingParameter)target;
		isValidate(SystemParametersConstant.ROAMING_PARAMETER_TAP_IN_FREQUENCY,roamingParameter.getTapinFrequency(),"tapinFrequency" ,moduleName,null,validateForImport);
		isValidate(SystemParametersConstant.ROAMING_PARAMETER_NRTRDE_IN_FREQUENCY,roamingParameter.getNrtrdeInFrequency(),"nrtrdeInFrequency" ,moduleName,null,validateForImport);
		isValidate(SystemParametersConstant.ROAMING_PARAMETER_NRTRDE_OUT_FREQUENCY,roamingParameter.getNrtrdeOutFrequency(),"nrtrdeOutFrequency" ,moduleName,null,validateForImport);
		
		
		
	}

	public void validateFileSequenceFileds(Object target, Errors errors, String moduleName,boolean validateForImport) {
		this.errors = errors;
		fileSequenceManagement = (FileSequenceManagement)target;
		String fullClassName =fileSequenceManagement.getClass().getName();
		isValidate(SystemParametersConstant.FILE_SEQUENCE_TEST_RAP_IN,fileSequenceManagement.getTestRapIn(),"testRapIn",null, moduleName,validateForImport,fullClassName);
		isValidate(SystemParametersConstant.FILE_SEQUENCE_TEST_RAP_OUT,fileSequenceManagement.getTestRapOut(),"testRapOut",null, moduleName,validateForImport,fullClassName);
		isValidate(SystemParametersConstant.FILE_SEQUENCE_TEST_TAP_IN,fileSequenceManagement.getTestTapIn(),"testTapIn",null, moduleName,validateForImport,fullClassName);
		isValidate(SystemParametersConstant.FILE_SEQUENCE_TEST_TAP_OUT,fileSequenceManagement.getTestTapOut(),"testTapOut",null, moduleName,validateForImport,fullClassName);
		isValidate(SystemParametersConstant.FILE_SEQUENCE_NRTRDE_IN,fileSequenceManagement.getNrtrdeIn(),"nrtrdeIn",null, moduleName,validateForImport,fullClassName);
		isValidate(SystemParametersConstant.FILE_SEQUENCE_COMMERCIAL_RAP_IN,fileSequenceManagement.getCommercialRapIn(),"commercialRapIn",null, moduleName,validateForImport,fullClassName);
		isValidate(SystemParametersConstant.FILE_SEQUENCE_COMMERCIAL_RAP_OUT,fileSequenceManagement.getCommercialRapOut(),"commercialRapOut",null, moduleName,validateForImport,fullClassName);
		isValidate(SystemParametersConstant.FILE_SEQUENCE_COMMERCIAL_TAP_IN,fileSequenceManagement.getCommercialTapIn(),"commercialTapIn",null, moduleName,validateForImport,fullClassName);
		isValidate(SystemParametersConstant.FILE_SEQUENCE_COMMERCIAL_TAP_OUT,fileSequenceManagement.getTestRapIn(),"commercialTapOut",null, moduleName,validateForImport,fullClassName);
		isValidate(SystemParametersConstant.FILE_SEQUENCE_NRTRDE_OUT,fileSequenceManagement.getNrtrdeOut(),"nrtrdeOut",null, moduleName,validateForImport,fullClassName);
		
	}

	public void validateTestSimManagementDetails(TestSimManagementData testSimManagementData, Errors errors,String  moduleName, boolean validateForImport) {
		this.errors = errors;
		String fullClassName = testSimManagementData.getClass().getName();
		
		isValidate(SystemParametersConstant.TEST_SIM_PMN_CODE,testSimManagementData.getInBoundPmnCode(),"inBoundPmnCode",null,moduleName, validateForImport, fullClassName);
		isValidate(SystemParametersConstant.TEST_SIM_IMSI,testSimManagementData.getInBoundImsi(),"inBoundImsi",null,moduleName, validateForImport, fullClassName);
		isValidate(SystemParametersConstant.TEST_SIM_MSISDN,testSimManagementData.getInBoundmsisdn(),"inBoundmsisdn",null,moduleName, validateForImport, fullClassName);
		isValidate(SystemParametersConstant.TEST_SIM_PMN_CODE,testSimManagementData.getOutBoundPmnCode(),"outBoundPmnCode",null,moduleName, validateForImport, fullClassName);
		isValidate(SystemParametersConstant.TEST_SIM_IMSI,testSimManagementData.getOutBoundImsi(),"outBoundImsi",null,moduleName, validateForImport, fullClassName);
		isValidate(SystemParametersConstant.TEST_SIM_MSISDN,testSimManagementData.getOutBoundmsisdn(),"outBoundmsisdn",null,moduleName, validateForImport, fullClassName);
		isValidate(SystemParametersConstant.TEST_SIM_SERVICES,testSimManagementData.getInBoundServices(),"inBoundServices",moduleName,null,validateForImport);
		isValidate(SystemParametersConstant.TEST_SIM_SERVICES,testSimManagementData.getOutBoundServices(),"outBoundServices",moduleName,null,validateForImport);
		Date inBoundToDate = testSimManagementData.getInBoundToDate();
		Date inBoundFromDate = testSimManagementData.getInBoundFromDate();
		Date outBoundToDate = testSimManagementData.getOutBoundToDate();
		Date outBoundFromDate = testSimManagementData.getOutBoundFromDate();
		validateOutBoundFromdate(outBoundFromDate);
		validateInBoundFromdate(inBoundFromDate);
		validateInBoundToDate(inBoundToDate,inBoundFromDate);
		validateOutBoundToDate(outBoundToDate,outBoundFromDate);
		
		
	}
	
	private void validateInBoundFromdate(Date inBoundFromDate) {
		
		boolean isValid = true;
		if(inBoundFromDate == null){
			isValid = false;
		}else{
			if(inBoundFromDate.compareTo(new Date()) < 0){
				System.out.println(new Date());
				isValid = false;
			}
		}
		
		if(!isValid){
			String errorMsgValue = getMessage("TestSimManagementData.inBoundFromDate.invalid");
			errors.rejectValue("inBoundFromDate", "error.TestSimManagementData.inBoundFromDate.invalid", errorMsgValue);
		}
	
	
}

	private void validateInBoundToDate(Date inBoundToDate, Date inBoundFromDate) {
		
			boolean isValid = true;
			if(inBoundToDate == null || inBoundFromDate == null ){
				isValid = false;
			}else{
				if(inBoundToDate.compareTo(inBoundFromDate) < 0){
					isValid = false;
				}
			}
			
			if(!isValid){
				String errorMsgValue = getMessage("TestSimManagementData.inBoundToDate.invalid");
				errors.rejectValue("inBoundToDate", "error.TestSimManagementData.inBoundToDate.invalid", errorMsgValue);
			}
		
		
	}
	
	private void validateOutBoundFromdate(Date outBoundFromDate) {
		
		boolean isValid = true;
		if(outBoundFromDate == null){
			isValid = false;
		}else{
			if(outBoundFromDate.compareTo(new Date()) < 0){
				isValid = false;
			}
		}
		
		if(!isValid){
			String errorMsgValue = getMessage("TestSimManagementData.outBoundFromDate.invalid");
			errors.rejectValue("outBoundFromDate", "error.TestSimManagementData.outBoundFromDate.invalid", errorMsgValue);
		}
	
	
}
	
	
	
	
	private void validateOutBoundToDate(Date outBoundToDate, Date outBoundFromDate) {
		
		boolean isValid = true;
		if(outBoundToDate == null ){
			isValid = false;
		}else{
			if(outBoundToDate.compareTo(outBoundFromDate) < 0){
				isValid = false;
			}
		}
		
		if(!isValid){
			String errorMsgValue = getMessage("TestSimManagementData.outBoundToDate.invalid");
			errors.rejectValue("outBoundToDate", "error.TestSimManagementData.outBoundToDate.invalid", errorMsgValue);
		}
	
	
}

	public void validateFileManagement(FileManagement fileManagement, Errors errors,String  moduleName, boolean validateForImport) {
		this.errors = errors;
		String fullClassName = fileManagement.getClass().getName();
		
		isValidate(SystemParametersConstant.MAX_RECORDS_TAP_OUT,Long.parseLong(fileManagement.getTestMaxRecordsInTapOut()),"testMaxRecordsInTapOut",null,moduleName, validateForImport, fullClassName);
		isValidate(SystemParametersConstant.MAX_FILE_SIZE_TAP_OUT,Long.parseLong(fileManagement.getTestMaxFileSizeOfTapOut()),"testMaxFileSizeOfTapOut",null,moduleName, validateForImport, fullClassName);
		isValidate(SystemParametersConstant.MAX_RECORDS_NRTRDE_OUT,Long.parseLong(fileManagement.getTestMaxRecordsInNrtrdeOut()),"testMaxRecordsInNrtrdeOut",null,moduleName, validateForImport, fullClassName);
		isValidate(SystemParametersConstant.MAX_FILE_SIZE_NRTRDE_OUT,Long.parseLong(fileManagement.getTestMaxfileSizeOfnrtrdeOut()),"testMaxfileSizeOfnrtrdeOut",null,moduleName, validateForImport, fullClassName);
		isValidate(SystemParametersConstant.MAX_RECORDS_TAP_OUT,Long.parseLong(fileManagement.getCommercialMaxRecordsInTapOut()),"commercialMaxRecordsInTapOut",null,moduleName, validateForImport, fullClassName);
		isValidate(SystemParametersConstant.MAX_FILE_SIZE_TAP_OUT,Long.parseLong(fileManagement.getCommercialMaxFileSizeOfTapOut()),"commercialMaxFileSizeOfTapOut",null,moduleName, validateForImport, fullClassName);
		isValidate(SystemParametersConstant.MAX_RECORDS_NRTRDE_OUT,Long.parseLong(fileManagement.getCommercialMaxRecordsInNrtrdeOut()),"commercialMaxRecordsInNrtrdeOut",null,moduleName, validateForImport, fullClassName);
		isValidate(SystemParametersConstant.MAX_FILE_SIZE_NRTRDE_OUT,Long.parseLong(fileManagement.getCommercialMaxfileSizeOfnrtrdeOut()),"commercialMaxfileSizeOfnrtrdeOut",null,moduleName, validateForImport, fullClassName);
		
		
	}

}
