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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

/**
 * 
 * @author Sagar Ghetiya
 *
 */
@Component(value = "lookupFieldDetailData")
@Scope(value = "prototype")
@Entity()
@Table(name = "TBLTLOOKUPFIELDDETAIL")
@DynamicUpdate	
@XmlRootElement
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="ruleLookupTableCache")
public class LookupFieldDetailData  extends BaseModel implements Serializable {
	
	
	private static final long serialVersionUID = -8677488313189848538L;
	private int id;
	private RuleLookupTableData ruleLookUpTable;
	private String fieldName;
	private String viewFieldName;
	private String displayName;
	private boolean isUnique;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="LookUpFieldDetail",allocationSize=1)
	@Column(name="ID")
	@XmlElement
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "LOOKUPTABLEID",nullable = false,foreignKey = @ForeignKey(name = "FK_LOOKUPFIELD_LOOKUPTABLE"))
	@XmlTransient
	@DiffIgnore
	public RuleLookupTableData getRuleLookUpTable() {
		return ruleLookUpTable;
	}
	public void setRuleLookUpTable(RuleLookupTableData ruleLookUpTable) {
		this.ruleLookUpTable = ruleLookUpTable;
	}
	
	
	@Column(name = "FIELDNAME", nullable = true, length = 250)
	@XmlElement
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	
	@Column(name = "FIELDVIEWNAME", nullable = true, length = 250)
	@XmlElement
	public String getViewFieldName() {
		return viewFieldName;
	}
	public void setViewFieldName(String viewFieldName) {
		this.viewFieldName = viewFieldName;
	}
	
	
	@Column(name = "DISPLAYNAME", nullable = true, length = 250)
	@XmlElement
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	
	@Column(name = "ISUNIQUE", nullable = true, length = 250)
	@XmlElement
	public boolean getIsUnique() {
		return isUnique;
	}
	public void setIsUnique(boolean isUnique) {
		this.isUnique = isUnique;
	}
	
}
