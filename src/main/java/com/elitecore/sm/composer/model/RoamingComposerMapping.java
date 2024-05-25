package com.elitecore.sm.composer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import org.springframework.stereotype.Component;

@Component
@Entity
@XmlSeeAlso({TAPComposerMapping.class,RAPComposerMapping.class,NRTRDEComposerMapping.class})
public class RoamingComposerMapping extends ComposerMapping {
	
	private static final long serialVersionUID = 5679585744726606090L;
	private String recMainAttribute;
	private String startFormat;
	private String multiContainerDelimiter;
	private boolean composeAsSingleRecordEnable = false;
	
	@XmlElement
	@Column(name = "RECMAINATTR", nullable = true, length = 500)
	public String getRecMainAttribute() {
		return recMainAttribute;
	}

	@XmlElement
	@Column(name = "STARTFORMAT", nullable = true, length = 500)
	public String getStartFormat() {
		return startFormat;
	}

	@XmlElement
	@Column(name = "MULTICONTAINERDELIMITER", nullable = true, length = 500)
	public String getMultiContainerDelimiter() {
		return multiContainerDelimiter;
	}

	@XmlElement
	@Column(name = "COMPOSEASSINGLERECORDENABLE", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isComposeAsSingleRecordEnable() {
		return composeAsSingleRecordEnable;
	}

	public void setRecMainAttribute(String recMainAttribute) {
		this.recMainAttribute = recMainAttribute;
	}

	public void setStartFormat(String startFormat) {
		this.startFormat = startFormat;
	}

	public void setMultiContainerDelimiter(String multiContainerDelimiter) {
		this.multiContainerDelimiter = multiContainerDelimiter;
	}
	
	public void setComposeAsSingleRecordEnable(boolean composeAsSingleRecordEnable) {
		this.composeAsSingleRecordEnable = composeAsSingleRecordEnable;
	}

}
