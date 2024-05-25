/**
 * 
 */
package com.elitecore.sm.services.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author vandana.awatramani
 *
 */
@Component("netflowCollectionService")
@Entity()
@Table(name = "TBLTNETFLOWCOLLSVC")
@Scope(value = "prototype")
@DynamicUpdate
@XmlRootElement
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="serviceCache")
@XmlType(propOrder = { "enableParallelBinaryWrite","readTemplateOnInit", 
						"optionTemplateEnable","optionTemplateId","optionTemplateKey",
						"optionTemplateValue","optionCopytoTemplateId","optionCopyTofield"})
public class NetflowCollectionService extends NetflowBinaryCollectionService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean enableParallelBinaryWrite = false;
	private boolean readTemplateOnInit = true;
	
	private boolean optionTemplateEnable=false;
	private String optionTemplateId="263,262";
	private String optionTemplateKey = "234";
	private String optionTemplateValue = "236";
	private String optionCopytoTemplateId="271,272";
	private String optionCopyTofield = "234,235";
	
	@XmlElement
	@Column(name = "PARRLBINWRTENBL", nullable = true,length=1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isEnableParallelBinaryWrite() {
		return enableParallelBinaryWrite;
	}
	public void setEnableParallelBinaryWrite(boolean enableParallelBinaryWrite) {
		this.enableParallelBinaryWrite = enableParallelBinaryWrite;
	}
	
	@XmlElement
	@Column(name = "READTMPINIT", nullable = true,length=1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isReadTemplateOnInit() {
		return readTemplateOnInit;
	}
	public void setReadTemplateOnInit(boolean readTemplateOnInit) {
		this.readTemplateOnInit = readTemplateOnInit;
	}
	
	@XmlElement
	@Column(name = "OPTTMPENBL", nullable = true,length=1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isOptionTemplateEnable() {
		return optionTemplateEnable;
	}
	public void setOptionTemplateEnable(boolean optionTemplateEnable) {
		this.optionTemplateEnable = optionTemplateEnable;
	}
	
	@XmlElement(nillable=true)
	@Column(name = "OPTTMPID", nullable = true,length=50)
	public String getOptionTemplateId() {
		return optionTemplateId;
	}
	public void setOptionTemplateId(String optionTemplateId) {
		this.optionTemplateId = optionTemplateId;
	}
	
	@XmlElement(nillable=true)
	@Column(name = "OPTTMPKEY", nullable = true,length=50)
	public String getOptionTemplateKey() {
		return optionTemplateKey;
	}
	public void setOptionTemplateKey(String optionTemplateKey) {
		this.optionTemplateKey = optionTemplateKey;
	}
	
	@XmlElement(nillable=true)
	@Column(name = "OPTTMPVAL", nullable = true,length=50)
	public String getOptionTemplateValue() {
		return optionTemplateValue;
	}
	public void setOptionTemplateValue(String optionTemplateValue) {
		this.optionTemplateValue = optionTemplateValue;
	}
	
	@XmlElement(nillable=true)
	@Column(name = "CPYTOTMPID", nullable = true,length=50)
	public String getOptionCopytoTemplateId() {
		return optionCopytoTemplateId;
	}
	public void setOptionCopytoTemplateId(String optionCopytoTemplateId) {
		this.optionCopytoTemplateId = optionCopytoTemplateId;
	}
	
	@XmlElement(nillable=true)
	@Column(name = "CPYTOFIELD", nullable = true,length=50)
	public String getOptionCopyTofield() {
		return optionCopyTofield;
	}
	public void setOptionCopyTofield(String optionCopyTofield) {
		this.optionCopyTofield = optionCopyTofield;
	}
}
