/**
 * 
 */
package com.elitecore.sm.parser.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.EngineConstants;

/**
 * @author vandana.awatramani
 *
 */
@Component(value="natFlowParserMapping")
@Entity()
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="parserMappingCache")
@XmlType(propOrder = { "readTemplatesInitially", "optionTemplateEnable","optionTemplateId","optionTemplateKey","optionTemplateValue","optionCopytoTemplateId","optionCopyTofield", "skipAttributeForValidation", "overrideTemplate", "defaultTemplate", "filterEnable", "filterProtocol", "filterTransport", "filterPort"})
@DiscriminatorValue(EngineConstants.NATFLOW_PARSING_PLUGIN)
public class NATFlowParserMapping extends ParserMapping {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8644378306256675508L;
	private boolean readTemplatesInitially;
	
	private boolean optionTemplateEnable=false;
	private String optionTemplateId="262";
	private String optionTemplateKey="234";
	private String optionTemplateValue="236";
	private String optionCopytoTemplateId="271,272";
	private String optionCopyTofield="234,235";
	private String skipAttributeForValidation = null;
	private boolean overrideTemplate = false;
	private boolean defaultTemplate = false;
	
	private boolean filterEnable=false;
	private String filterProtocol;
	private String filterTransport;
	private String filterPort;
	
	/**
	 * @return the readTemplatesInitially
	 */
	@XmlElement
	@Column(name = "READTEMPLATESINITIALLY", nullable = true)
	@org.hibernate.annotations.Type(type = "yes_no")	
	public boolean isReadTemplatesInitially() {
		return readTemplatesInitially;
	}
	/**
	 * @return the optionTemplateEnable
	 */
	@XmlElement
	@Column(name = "OPTIONTEMPLATEENABLE", nullable = true)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isOptionTemplateEnable() {
		return optionTemplateEnable;
	}
	/**
	 * @return the optionTemplateId
	 */
	@XmlElement(nillable=true)
	@Column(name = "OPTTMPID", nullable = true,length=50)
	public String getOptionTemplateId() {
		return optionTemplateId;
	}
	/**
	 * @return the optionTemplateKey
	 */
	
	@XmlElement(nillable=true)
	@Column(name = "OPTTMPKEY", nullable = true,length=50)
	public String getOptionTemplateKey() {
		return optionTemplateKey;
	}
	/**
	 * @return the optionTemplateValue
	 */
	@XmlElement(nillable=true)
	@Column(name = "OPTTMPVAL", nullable = true,length=50)
	public String getOptionTemplateValue() {
		return optionTemplateValue;
	}
	/**
	 * @return the optionCopytoTemplateId
	 */
	@XmlElement(nillable=true)
	@Column(name = "CPYTOTMPID", nullable = true,length=50)
	public String getOptionCopytoTemplateId() {
		return optionCopytoTemplateId;
	}
	/**
	 * @return the optionCopyTofield
	 */
	@XmlElement(nillable=true)
	@Column(name = "CPYTOFIELD", nullable = true,length=50)
	public String getOptionCopyTofield() {
		return optionCopyTofield;
	}
	/**
	 * @return the skipAttributeForValidation
	 */
	@XmlElement(nillable=true)
	@Column(name = "SKIPATTRIBUTEFORVALIDATION", nullable = true, length=300)
	public String getSkipAttributeForValidation() {
		return skipAttributeForValidation;
	}
	/**
	 * @return the overrideTemplate
	 */
	@XmlElement
	@Column(name = "OVERRIDETEMPLATE", nullable = true)
	@org.hibernate.annotations.Type(type = "yes_no")	
	public boolean isOverrideTemplate() {
		return overrideTemplate;
	}
	/**
	 * @param readTemplatesInitially the readTemplatesInitially to set
	 */
	public void setReadTemplatesInitially(boolean readTemplatesInitially) {
		this.readTemplatesInitially = readTemplatesInitially;
	}
	/**
	 * @param optionTemplateEnable the optionTemplateEnable to set
	 */
	public void setOptionTemplateEnable(boolean optionTemplateEnable) {
		this.optionTemplateEnable = optionTemplateEnable;
	}
	/**
	 * @param optionTemplateId the optionTemplateId to set
	 */
	public void setOptionTemplateId(String optionTemplateId) {
		this.optionTemplateId = optionTemplateId;
	}
	/**
	 * @param optionTemplateKey the optionTemplateKey to set
	 */
	public void setOptionTemplateKey(String optionTemplateKey) {
		this.optionTemplateKey = optionTemplateKey;
	}
	/**
	 * @param optionTemplateValue the optionTemplateValue to set
	 */
	public void setOptionTemplateValue(String optionTemplateValue) {
		this.optionTemplateValue = optionTemplateValue;
	}
	/**
	 * @param optionCopytoTemplateId the optionCopytoTemplateId to set
	 */
	public void setOptionCopytoTemplateId(String optionCopytoTemplateId) {
		this.optionCopytoTemplateId = optionCopytoTemplateId;
	}
	/**
	 * @param optionCopyTofield the optionCopyTofield to set
	 */
	public void setOptionCopyTofield(String optionCopyTofield) {
		this.optionCopyTofield = optionCopyTofield;
	}	
	/**
	 * @param skipAttributeForValidation the skipAttributeForValidation to set
	 */
	public void setSkipAttributeForValidation(String skipAttributeForValidation) {
		this.skipAttributeForValidation = skipAttributeForValidation; 
	}
	/**
	 * @param overrideTemplate the overrideTemplate to set
	 */
	public void setOverrideTemplate(boolean overrideTemplate) {
		this.overrideTemplate = overrideTemplate;
	}
	
	/**
	 * @return the defaultTemplate
	 */
	@XmlElement
	@Column(name = "DEFAULTTEMPLATE", nullable = true)
	@org.hibernate.annotations.Type(type = "yes_no")	
	public boolean getDefaultTemplate() {
		return defaultTemplate;
	}
	
	/**
	 * @param defaultTemplate the defaultTemplate to set
	 */
	public void setDefaultTemplate(boolean defaultTemplate) {
		this.defaultTemplate = defaultTemplate;
	}
	
	@XmlElement
	@Column(name = "FILTERENABLE", nullable = true)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isFilterEnable() {
		return filterEnable;
	}
	
	
	public void setFilterEnable(boolean filterEnable) {
		this.filterEnable = filterEnable;
	}
	
	@XmlElement(nillable=true)
	@Column(name = "FILTERPROTOCOL", nullable = true, length=300)
	public String getFilterProtocol() {
		return filterProtocol;
	}
	
	public void setFilterProtocol(String filterProtocol) {
		this.filterProtocol = filterProtocol;
	}
	
	@XmlElement(nillable=true)
	@Column(name = "FILTERTRANSPORT", nullable = true, length=300)
	public String getFilterTransport() {
		return filterTransport;
	}
	
	public void setFilterTransport(String filterTransport) {
		this.filterTransport = filterTransport;
	}
	
	@XmlElement(nillable=true)
	@Column(name = "FILTERPORT", nullable = true, length=300)
	public String getFilterPort() {
		return filterPort;
	}
	
	public void setFilterPort(String filterPort) {
		this.filterPort = filterPort;
	}
	
}
