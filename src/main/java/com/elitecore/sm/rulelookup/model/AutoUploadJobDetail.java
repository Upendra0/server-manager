package com.elitecore.sm.rulelookup.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.job.model.CrestelSMJob;


@Entity
@Component
@Table(name = "TBLTAUTOUPLOADJOBDETAIL")
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="autoUploadJobDetail")
public class AutoUploadJobDetail extends BaseModel implements Serializable{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6614393929501302553L;
	private int id;	 
	private String sourceDirectory;
	private String action;
	private String filePrefix;
	private String fileContains;
		 
	private CrestelSMJob scheduler;
	
	private RuleLookupTableData ruleLookupTableData;
	
	
	@Id
    @Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="AutoUploadConfig",allocationSize=1)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "SOURCEDIRECTORY", nullable = true, length = 500)
	public String getSourceDirectory() {
		return sourceDirectory;
	}
	public void setSourceDirectory(String sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}
	
	@Column(name = "ACTION", nullable = true, length = 500)
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	
	@Column(name = "FILEPREFIX", nullable = true, length = 500)
	public String getFilePrefix() {
		return filePrefix;
	}
	public void setFilePrefix(String filePrefix) {
		this.filePrefix = filePrefix;
	}
	
	@Column(name = "FILECONTAINS", nullable = true, length = 500)
	public String getFileContains() {
		return fileContains;
	}
	public void setFileContains(String fileContains) {
		this.fileContains = fileContains;
	}
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "JOBID",nullable = true,foreignKey = @ForeignKey(name = "TBLTAUTOUPLOAD_JOB_FK"))
	public CrestelSMJob getScheduler() {
		return scheduler;
	}
	public void setScheduler(CrestelSMJob scheduler) {
		this.scheduler = scheduler;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "TABLENAME",nullable = false,foreignKey = @ForeignKey(name = "FK_AUTOUPLOAD_LOOKUPTABLE"))
	public RuleLookupTableData getRuleLookupTableData() {
		return ruleLookupTableData;
	}
	public void setRuleLookupTableData(RuleLookupTableData ruleLookupTableData) {
		this.ruleLookupTableData = ruleLookupTableData;
	}
	
	
}
