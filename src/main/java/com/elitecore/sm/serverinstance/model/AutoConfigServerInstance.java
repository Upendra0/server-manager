package com.elitecore.sm.serverinstance.model;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;


@Component(value = "autoConfigServerInstance")
@Scope(value = "prototype")
@Entity
@Table(name = "TBLTAUTOCONFIGSI")
public class AutoConfigServerInstance extends BaseModel {
	
	private static final long serialVersionUID = 8971443972275858057L;
	
	private int id;
	private String ipaddress;
	private int utilityPort;
	private int siPort;
	private Blob crestelNetServerData;//NOSONAR
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="AutoConfigServerInstance",allocationSize=1)
	@Column(name="ID")
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "IPADDRESS", nullable = false)
	public String getIpaddress() {
		return ipaddress;
	}
	
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}
	
	@Column(name = "UTILITYPORT", nullable = false)
	public int getUtilityPort() {
		return utilityPort;
	}
	
	public void setUtilityPort(int utilityPort) {
		this.utilityPort = utilityPort;
	}
	
	@Column(name = "SIPORT", nullable = false)
	public int getSiPort() {
		return siPort;
	}
	
	public void setSiPort(int siPort) {
		this.siPort = siPort;
	}
	
	@Lob
	@Column(name = "CRESTELNETSERVERDATA", nullable = true)
	public Blob getCrestelNetServerData() {
		return crestelNetServerData;
	}
	
	public void setCrestelNetServerData(Blob crestelNetServerData) {
		this.crestelNetServerData = crestelNetServerData;
	}
	
}
