package com.elitecore.sm.rulelookup.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

@Component(value = "ruleLookupTableConfiguration")
@Entity
@Table(name = "TBLTRULELOOKUPTABLECONFIG")
@DynamicUpdate
//@Cacheable
//@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="ruleLookupTableCache")
@XmlRootElement
public class RuleLookupTableConfiguration  extends BaseModel implements Serializable {
	
	private static final long serialVersionUID = 950095396125167594L;
	private int id;
	private String viewName;
	private String databaseQueryName;
	private boolean immediateExecution;
	private int serverInstanceId;
	private Timestamp lookUpReloadFromDate;
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="RuleLookupTableConfiguration",allocationSize=1)
	@Column(name="ID")
	@XmlElement
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "VIEWNAME", nullable = false, length = 400)
	@XmlElement
	public String getViewName() {
		return viewName;
	}
	
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	@Column(name = "DATABASEQUERYNAME", nullable = false, length = 400)
	@XmlElement
	public String getDatabaseQueryName() {
		return databaseQueryName;
	}

	public void setDatabaseQueryName(String databaseQueryName) {
		this.databaseQueryName = databaseQueryName;
	}

	@XmlElement
	@Column(name = "IMMEDIATEEXECUTION", nullable = false, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	public boolean isImmediateExecution() {
		return immediateExecution;
	}

	public void setImmediateExecution(boolean immediateExecution) {
		this.immediateExecution = immediateExecution;
	}
	
	@Column(name = "SERVERINSTANCEID", nullable = false)
	@XmlElement
	public int getServerInstanceId() {
		return serverInstanceId;
	}

	public void setServerInstanceId(int serverInstanceId) {
		this.serverInstanceId = serverInstanceId;
	}

	@Transient
	public Timestamp getLookUpReloadFromDate() {
		return lookUpReloadFromDate;
	}

	public void setLookUpReloadFromDate(Timestamp lookUpReloadFromDate) {
		this.lookUpReloadFromDate = lookUpReloadFromDate;
	}
			
}