package com.elitecore.sm.roaming.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.springframework.stereotype.Component;



@Component
@Entity
@Table(name = "TBLMROAMINGPARAMETER")
public class RoamingParameter extends RoamingConfiguration implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6889655421158709363L;
	private String tapinFrequency;
	private String nrtrdeInFrequency;
	private String nrtrdeOutFrequency;
	private String notifyOnRecord;
	private int id;
	@Column(name="tapinFrequency", nullable = false, length = 50)
	public String getTapinFrequency() {
		return tapinFrequency;
	}
	
	public void setTapinFrequency(String tapinFrequency) {
		this.tapinFrequency = tapinFrequency;
	}
	@Column(name="nrtrdeInFrequency", nullable = false, length = 50)
	public String getNrtrdeInFrequency() {
		return nrtrdeInFrequency;
	}
	
	public void setNrtrdeInFrequency(String nrtrdeInFrequency) {
		this.nrtrdeInFrequency = nrtrdeInFrequency;
	}
	@Column(name="nrtrdeOutFrequency", nullable = false, length = 50)
	public String getNrtrdeOutFrequency() {
		return nrtrdeOutFrequency;
	}
	public void setNrtrdeOutFrequency(String nrtrdeOutFrequency) {
		this.nrtrdeOutFrequency = nrtrdeOutFrequency;
	}
	@Column(name="notifyOnRecord", nullable = true, length = 50)
	public String getNotifyOnRecord() {
		return notifyOnRecord;
	}
	public void setNotifyOnRecord(String notifyOnRecord) {
		this.notifyOnRecord = notifyOnRecord;
	}
	@Id
	@Column(name = "ID")
	@XmlElement
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
}
