package com.elitecore.sm.roaming.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.springframework.stereotype.Component;

@Entity
@Component
@Table(name = "TBLMHOSTCONFIGURATION")
public class HostConfiguration extends RoamingConfiguration implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9096712222432310164L;
	private int id;
	private String name;
	private String description;
	private String timezone;
	private String pmncode;
	private String tadigcode;
	
	
	@Column(name="name", nullable = false, length = 50)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name="description", nullable = true,length=500)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column(name="pmncode", nullable = false,length=50)
	public String getPmncode() {
		return pmncode;
	}
	public void setPmncode(String pmncode) {
		this.pmncode = pmncode;
	}
	@Column(name="tadigcode", nullable = false,length=50)
	public String getTadigcode() {
		return tadigcode;
	}
	public void setTadigcode(String tadigcode) {
		this.tadigcode = tadigcode;
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
	@Column(name="timezone", nullable = false,length=50)
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

}
