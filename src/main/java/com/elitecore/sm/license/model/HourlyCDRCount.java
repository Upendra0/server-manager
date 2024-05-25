package com.elitecore.sm.license.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

/**
 * 
 * @author nandish.patel
 *
 */
@Component(value = "hourlycdrcount")
@Entity()
@Table(name = "TBLTHOURLYCDRCOUNT")
@DynamicUpdate	
public class HourlyCDRCount extends BaseModel {
	
	private static final long serialVersionUID = -981338975139824950L;
	private long id;
	private String serverIp;
	private int serverPort;
	private String serviceInstanceId;
	private Date processStartDate;
	private Date processEndDate;
	private Date cdrDate;
	private long totalCDRCount;
	private int utilityPort; 
	private String groupServerId;
	private String deviceType;
	private Circle circle;
	
	public HourlyCDRCount() {
	}
	
	public HourlyCDRCount(long id, String serverIp, int serverPort, String serviceInstanceId, Date processStartDate,
			Date processEndDate, Date cdrDate, long totalCDRCount) {
		super();
		this.id = id;
		this.serverIp = serverIp;
		this.serverPort = serverPort;
		this.serviceInstanceId = serviceInstanceId;
		this.processStartDate = processStartDate;
		this.processEndDate = processEndDate;
		this.cdrDate = cdrDate;
		this.totalCDRCount = totalCDRCount;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTHOURLYCDRCOUNT",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="HourlyCDRCount",allocationSize=1)
	@Column(name="ID")
	@XmlElement
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "SERVERIP", nullable = false)
	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	@Column(name = "SERVERPORT", nullable = false)
	public int getServerPort() {
		return serverPort;
	}

	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	@Column(name = "SERVICEINSTANCEID", nullable = false)
	public String getServiceInstanceId() {
		return serviceInstanceId;
	}

	public void setServiceInstanceId(String serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}
	
	@Column(name = "PROCESSENDDATE", nullable = false)
	public Date getProcessEndDate() {
		return processEndDate;
	}

	public void setProcessEndDate(Date processEndDate) {
		this.processEndDate = processEndDate;
	}

	@Column(name = "CDRDATE", nullable = false)
	public Date getCdrDate() {
		return cdrDate;
	}

	public void setCdrDate(Date cdrDate) {
		this.cdrDate = cdrDate;
	}

	@Column(name = "TOTALCDRCOUNT", nullable = false, length=500)
	public long getTotalCDRCount() {
		return totalCDRCount;
	}

	@Column(name = "PROCESSSTARTDATE", nullable = false)
	public Date getProcessStartDate() {
		return processStartDate;
	}

	public void setProcessStartDate(Date processStartDate) {
		this.processStartDate = processStartDate;
	}

	public void setTotalCDRCount(long totalCDRCount) {
		this.totalCDRCount = totalCDRCount;
	}

	@Column(name = "UTILITYPORT")
	public int getUtilityPort() {
		return utilityPort;
	}

	public void setUtilityPort(int utilityPort) {
		this.utilityPort = utilityPort;
	}

	@Column(name = "GROUPSERVERID")
	public String getGroupServerId() {
		return groupServerId;
	}

	public void setGroupServerId(String groupServerId) {
		this.groupServerId = groupServerId;
	}

	@Column(name = "DEVICETYPE")
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "CIRCLEID")
	public Circle getCircle() {
		return circle;
	}

	public void setCircle(Circle circle) {
		this.circle = circle;
	}		

}
