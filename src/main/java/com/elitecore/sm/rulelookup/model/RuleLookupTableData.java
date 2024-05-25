package com.elitecore.sm.rulelookup.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
/**
 * 
 * @author Sagar Ghetiya
 *
 */

@Component(value = "ruleLookUpTableData")
@Entity
@Table(name = "TBLTRULELOOKUPTABLES")
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="ruleLookupTableCache")
@XmlRootElement
public class RuleLookupTableData  extends BaseModel implements Serializable {
	
	

	private static final long serialVersionUID = 8458036235639094701L;
	private int id;
	private String viewName;
	private String description;
	private List<LookupFieldDetailData> lookUpFieldDetailData;
	private List<RuleLookupData> ruleLookupData;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="RuleLookUpTable",allocationSize=1)
	@Column(name="ID")
	@XmlElement
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	
	@Column(name = "VIEWNAME", nullable = true, length = 250)
	@XmlElement
	public String getViewName() {
		return viewName;
	}
	
	public void setViewName(String viewName) {
		this.viewName = viewName;
	}
	
	
	@Column(name = "DESCRIPTION", nullable = true, length = 500)
	@XmlElement
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	@OneToMany(mappedBy = "ruleLookUpTable",orphanRemoval=true,fetch = FetchType.LAZY)
	@LazyCollection(LazyCollectionOption.FALSE)
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL) 
	@DiffIgnore
	public List<LookupFieldDetailData> getLookUpFieldDetailData() {
		return lookUpFieldDetailData;
	}
	
	public void setLookUpFieldDetailData(List<LookupFieldDetailData> lookUpFieldDetailData) {
		this.lookUpFieldDetailData = lookUpFieldDetailData;
	}
	
	
	@OneToMany(mappedBy = "ruleLookUpTable", fetch = FetchType.LAZY,orphanRemoval=true)
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
	@LazyCollection(LazyCollectionOption.FALSE)
	@DiffIgnore
	public List<RuleLookupData> getRuleLookupData() {
		return ruleLookupData;
	}
	
	public void setRuleLookupData(List<RuleLookupData> ruleLookupData) {
		this.ruleLookupData = ruleLookupData;
	}
	
	public void clearLookupFieldDetailData(){
		this.lookUpFieldDetailData.clear();
	}
	
	public void addLookupFieldDetailData(List<LookupFieldDetailData> lookUpFieldDetailData){
		this.lookUpFieldDetailData.addAll(lookUpFieldDetailData);
	}
	
	public void clearRuleLookupData(){
		this.ruleLookupData.clear();
	}
	
	public void addRuleLookupData(List<RuleLookupData> ruleLookupData){
		this.ruleLookupData.addAll(ruleLookupData);
	}
}
