package com.elitecore.sm.errorreprocess.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.services.model.Service;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author Urvashi Varsani
 *
 */

@Component(value="autoErrorReprocessDetail")
@Entity()
@Table(name = "TBLTAUTOERRORREPROCESSDETAIL")
@DynamicUpdate
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id", scope=AutoErrorReprocessDetail.class)
public class AutoErrorReprocessDetail extends BaseModel{
	
	private static final long serialVersionUID = 3946223438608509490L;
	private int id;
	private Service service;
	private int serverInstanceId;
	private String severity;
	private String category;
	private String reasoncategory;
	private String rule;
	private String errorCode;
	private CrestelSMJob job;
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							 pkColumnValue="AutoErrorReprocessDetail",allocationSize=1)
	@Column(name="id")
	public int getId() {
		return id;
	}

	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SERVICEID", nullable = false, foreignKey = @ForeignKey(name = "Service_FK"))
	public Service getService() {
		return service;
	}

	public void setService(Service service) {
		this.service = service;
	}
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "JOBID", nullable = false, foreignKey = @ForeignKey(name = "Job_FK"))
	public CrestelSMJob getJob() {
		return job;
	}

	public void setJob(CrestelSMJob job) {
		this.job = job;
	}


	@Column(name="SERVERINSTANCEID")
	public int getServerInstanceId() {
		return serverInstanceId;
	}


	public void setServerInstanceId(int serverInstanceId) {
		this.serverInstanceId = serverInstanceId;
	}

	@Column(name="SEVERITY")
	public String getSeverity() {
		return severity;
	}


	public void setSeverity(String severity) {
		this.severity = severity;
	}

	@Column(name="CATEGORY")
	public String getCategory() {
		return category;
	}


	public void setCategory(String category) {
		this.category = category;
	}

	@Column(name="REASONCATEGORY")
	public String getReasoncategory() {
		return reasoncategory;
	}


	public void setReasoncategory(String reasoncategory) {
		this.reasoncategory = reasoncategory;
	}

	@Column(name="RULE")
	public String getRule() {
		return rule;
	}


	public void setRule(String rule) {
		this.rule = rule;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name="ERRORCODE")
	public String getErrorCode() {
		return errorCode;
	}


	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	
	
	

	
}
