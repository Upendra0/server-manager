package com.elitecore.sm.rulelookup.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.job.model.CrestelSMJob;
import com.elitecore.sm.serverinstance.model.ServerInstance;

@Entity
@Component
@Table(name = "TBLTAUTORELOADJOBDETAIL")
public class AutoReloadJobDetail extends BaseModel implements Serializable{

	private static final long serialVersionUID = 8052340161596784685L;
	private int id;
	private RuleLookupTableData ruleLookupTableData;
	private ServerInstance serverInstance;
	private String databaseQueryList;
	private ScheduleTypeEnum scheduleType;	
	private CrestelSMJob scheduler;
	private ReloadOptionEnum reloadOptions;
	
	@Id
    @XmlElement
    @Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="AutoReloadCacheConfig",allocationSize=1)
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "LOOKUPTABLEID",nullable = false,foreignKey = @ForeignKey(name = "FK_AUTORELOAD_LOOKUPTABLE"))
	public RuleLookupTableData getRuleLookupTableData() {
		return ruleLookupTableData;
	}
	
	public void setRuleLookupTableData(RuleLookupTableData ruleLookupTableData) {
		this.ruleLookupTableData = ruleLookupTableData;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "SERVERINSTANCEID",nullable = true,foreignKey = @ForeignKey(name = "FK_AUTORELOAD_SERVERINSTANCE"))
	public ServerInstance getServerInstance() {
		return serverInstance;
	}
	
	public void setServerInstance(ServerInstance serverInstance) {
		this.serverInstance = serverInstance;
	}
	
	@Column(name = "DBQUERYIDLIST", nullable = true, length = 500)
	public String getDatabaseQueryList() {
		return databaseQueryList;
	}
	
	public void setDatabaseQueryList(String databaseQueryList) {
		this.databaseQueryList = databaseQueryList;
	}
	
	@Column(name = "SCHEDULETYPE", nullable = false, length = 100)
	@Enumerated(EnumType.STRING)
	public ScheduleTypeEnum getScheduleType() {
		return scheduleType;
	}
	
	public void setScheduleType(ScheduleTypeEnum scheduleType) {
		this.scheduleType = scheduleType;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "JOBID",nullable = true,foreignKey = @ForeignKey(name = "FK_AUTORELOAD_SCHEDULER"))
	public CrestelSMJob getScheduler() {
		return scheduler;
	}
	
	public void setScheduler(CrestelSMJob scheduler) {
		this.scheduler = scheduler;
	}
	
	@Column(name = "RELOADOPTIONS", nullable = false, length = 100)
	@Enumerated(EnumType.STRING)
	public ReloadOptionEnum getReloadOptions() {
		return reloadOptions;
	}
	
	public void setReloadOptions(ReloadOptionEnum reloadOptions) {
		this.reloadOptions = reloadOptions;
	}
}
