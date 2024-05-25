/**
 * 
 */
package com.elitecore.sm.dictionary.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Ranjitsinh Reval
 *
 */
@XmlRootElement(name="field")
public class DictionaryField {

	private String elementId;
    private String dataType;
    private String status;
    private String name;
    private String applicability;
    private String group;
    private String dataTypeSemantics;
	
    
    /**
	 * @return the elementId
	 */
    @XmlAttribute
	public String getElementId() {
		return elementId;
	}
	/**
	 * @param elementId the elementId to set
	 */
	public void setElementId(String elementId) {
		this.elementId = elementId;
	}
	/**
	 * @return the dataType
	 */
	@XmlAttribute
	public String getDataType() {
		return dataType;
	}
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	/**
	 * @return the status
	 */
	@XmlAttribute
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	/**
	 * @return the name
	 */
	@XmlAttribute
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the applicability
	 */
	@XmlAttribute
	public String getApplicability() {
		return applicability;
	}
	/**
	 * @param applicability the applicability to set
	 */
	public void setApplicability(String applicability) {
		this.applicability = applicability;
	}
	/**
	 * @return the group
	 */
	@XmlAttribute
	public String getGroup() {
		return group;
	}
	/**
	 * @param group the group to set
	 */
	public void setGroup(String group) {
		this.group = group;
	}
	/**
	 * @return the dataTypeSemantics
	 */
	@XmlAttribute
	public String getDataTypeSemantics() {
		return dataTypeSemantics;
	}
	/**
	 * @param dataTypeSemantics the dataTypeSemantics to set
	 */
	public void setDataTypeSemantics(String dataTypeSemantics) {
		this.dataTypeSemantics = dataTypeSemantics;
	}
    
    
    
}
