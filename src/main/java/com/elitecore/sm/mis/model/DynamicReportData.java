package com.elitecore.sm.mis.model;

import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
/**
 * 
 * @author Chetan Kaila
 *
 */

@Component(value = "dynamicReportData")
@Entity
@Table(name = "TBLMDYNAMICREPORT")
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="dynamicReportDataCache")
@XmlRootElement
public class DynamicReportData extends BaseModel implements Serializable {

	private static final long serialVersionUID = 8458036235639094701L;
	private int id;
	private String reportName;
	private String description;
	private String viewName;
	private String displayFields;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="DynamicReportData",allocationSize=1)
	@Column(name="ID")
	@XmlElement
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	
	@Column(name = "REPORTNAME", nullable = true, length = 250)
	@XmlElement
	public String getReportName() {
		return reportName;
	}

	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	
	@Column(name = "DESCRIPTION", nullable = true, length = 500)
	@XmlElement
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "VIEWNAME", nullable = true, length = 250)
	@XmlElement
	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	
	@Column(name = "DISPLAYFIELDS", nullable = true, length = 500)
	@XmlElement
	public String getDisplayFields() {
		return displayFields;
	}

	public void setDisplayFields(String displayFields) {
		this.displayFields = displayFields;
	}
	
	
}
