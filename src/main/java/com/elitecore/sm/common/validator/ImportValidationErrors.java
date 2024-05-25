package com.elitecore.sm.common.validator;

/**
 * 
 * @author avani.panchal
 * Used for custom validation occurs in import functionality
 *
 */
public class ImportValidationErrors {
	
	private  int sequendNumber;
	private String moduleName;
	private String entityName;
	private String propertyName;
	private String propertyValue;
	private String errorMessage;
	
	public ImportValidationErrors(String moduleName,String entityName,String propertyName,String propertyValue,String errorMessage){
		this.moduleName=moduleName;
		this.entityName=entityName;
		this.propertyName=propertyName;
		this.propertyValue=propertyValue;
		this.errorMessage=errorMessage;
	}
	
	public ImportValidationErrors(String entityName,String propertyName,String propertyValue,String errorMessage, int sequendNumber){
		this.entityName=entityName;
		this.propertyName=propertyName;
		this.propertyValue=propertyValue;
		this.errorMessage=errorMessage;
		this.sequendNumber = sequendNumber;
	}
	
	/**
	 * 
	 * @return entityName
	 */
	public String getEntityName() {
		return entityName;
	}
	
	/**
	 * 
	 * @param entityName
	 */
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	
	/**
	 * 
	 * @return propertyName
	 */
	public String getPropertyName() {
		return propertyName;
	}
	
	/**
	 * 
	 * @param propertyName
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	/**
	 * 
	 * @return propertyValue
	 */
	public String getPropertyValue() {
		return propertyValue;
	}
	
	/**
	 * 
	 * @param propertyValue
	 */
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}
	
	/**
	 * 
	 * @return errorMessage
	 */
	public String getErrorMessage() {
		return errorMessage;
	}
	
	/**
	 * 
	 * @param errorMessage
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	/**
	 * @return the sequendNumber
	 */
	public int getSequendNumber() {
		return sequendNumber;
	}

	/**
	 * @param sequendNumber the sequendNumber to set
	 */
	public void setSequendNumber(int sequendNumber) {
		this.sequendNumber = sequendNumber;
	}
	
	/**
	 * 
	 * @return moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * 
	 * @param moduleName
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
}
