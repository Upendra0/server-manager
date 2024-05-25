package com.elitecore.sm.parser.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.util.EliteUtils;

/**
 * @author ELITECOREADS\kiran.kagithapu
 *
 */
@Component
@Entity
@Table(name = "TBLTPARSERGROUPATTRIBUTE")
@DynamicUpdate
public class ParserGroupAttribute extends BaseModel implements Serializable {
	
	private static final long serialVersionUID = 9071421575399943272L;
	
	private int id;
	private String name;
	private ParserGroupAttribute baseGroup;
	private ParserMapping parserMapping;
	private List<ParserAttribute> attributeList = new ArrayList<>(0);
	private List<ParserPageConfiguration> parserPageConfigurationList = new ArrayList<>(0);
	private List<ParserGroupAttribute> groupAttributeList = new ArrayList<>(0);
	private boolean associatedByGroup;
	private String tableStartIdentifier;
	private String tableStartIdentifierTdNo;
	private String tableEndIdentifier;
	private String tableEndIdentifierTdNo;
	private String tableRowsToIgnore;
	private String tableStartIdentifierCol;
	private String tableEndIdentifierCol;
	private String tableEndIdentifierRowLocation;
	private String tableEndIdentifierOccurence;
	private String tableRowIdentifier;
	
	@Id
    @XmlElement
    @Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="ParserGroupAttribute",allocationSize=1)
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlTransient
	@ManyToOne(targetEntity = ParserGroupAttribute.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "BASEGROUPID", nullable = true, foreignKey = @ForeignKey(name = "FK_PARSERBASEGROUPID"))
	public ParserGroupAttribute getBaseGroup() {
		return baseGroup;
	}
	public void setBaseGroup(ParserGroupAttribute baseGroup) {
		this.baseGroup = baseGroup;
	}
	
	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "PARSERMAPPINGID", nullable = true, foreignKey = @ForeignKey(name = "FK_GROUPATTR_PARSERMAPPING"))
	public ParserMapping getParserMapping() {
		return parserMapping;
	}
	public void setParserMapping(ParserMapping parserMapping) {
		this.parserMapping = parserMapping;
	}
	
	@XmlElement
	@OneToMany(targetEntity = ParserAttribute.class, mappedBy = "parserGroupAttribute", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<ParserAttribute> getAttributeList() {
		return attributeList;
	}
	public void setAttributeList(List<ParserAttribute> attributeList) {
		this.attributeList = attributeList;
	}
	
	@XmlElement
	@OneToMany(targetEntity = ParserPageConfiguration.class, mappedBy = "parserGroupAttribute", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	public List<ParserPageConfiguration> getParserPageConfigurationList() {
		return parserPageConfigurationList;
	}
	public void setParserPageConfigurationList(List<ParserPageConfiguration> parserPageConfigurationList) {
		this.parserPageConfigurationList = parserPageConfigurationList;
	}
	
	@XmlElement
	@OneToMany(targetEntity = ParserGroupAttribute.class, mappedBy = "baseGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<ParserGroupAttribute> getGroupAttributeList() {
		return groupAttributeList;
	}
	public void setGroupAttributeList(List<ParserGroupAttribute> groupAttributeList) {
		this.groupAttributeList = groupAttributeList;
	}
	
	@Transient
	@XmlElement
	public boolean isAssociatedByGroup() {
		if(getBaseGroup() != null){
			this.associatedByGroup = true;
		}
		return associatedByGroup;
	}
	
	public void setAssociatedByGroup(boolean associatedByGroup) {
		this.associatedByGroup = associatedByGroup;
	}
	
	@Column(name = "TABLESTARTIDENTIFIER", length = 500)
	public String getTableStartIdentifier() {
		return tableStartIdentifier;
	}
	
	public void setTableStartIdentifier(String tableStartIdentifier) {
		this.tableStartIdentifier = tableStartIdentifier;
	}
	
	@Column(name = "TABLESTARTIDENTIFIERTDNO")
	public String getTableStartIdentifierTdNo() {
		return tableStartIdentifierTdNo;
	}
	
	@Column(name = "TABLESTARTIDENTIFIERCOL", length = 500)
	public String getTableStartIdentifierCol() {
		return tableStartIdentifierCol;
	}
	public void setTableStartIdentifierCol(String tableStartIdentifierCol) {
		this.tableStartIdentifierCol = tableStartIdentifierCol;
	}
	
	public void setTableStartIdentifierTdNo(String tableStartIdentifierTdNum) {
		this.tableStartIdentifierTdNo = tableStartIdentifierTdNum;
	}
	
	@Column(name = "TABLEENDIDENTIFIER", length = 500)
	public String getTableEndIdentifier() {
		return tableEndIdentifier;
	}
	
	public void setTableEndIdentifier(String tableEndIdentifier) {
		this.tableEndIdentifier = tableEndIdentifier;
	}
	
	@Column(name = "TABLEENDIDENTIFIERTDNO")
	public String getTableEndIdentifierTdNo() {
		return tableEndIdentifierTdNo;
	}
	
	@Column(name = "TABLEENDIDENTIFIERCOL", length = 500)
	public String getTableEndIdentifierCol() {
		return tableEndIdentifierCol;
	}
	public void setTableEndIdentifierCol(String tableEndIdentifierCol) {
		this.tableEndIdentifierCol = tableEndIdentifierCol;
	}
	
	public void setTableEndIdentifierTdNo(String tableEndIdentifierTdNum) {
		this.tableEndIdentifierTdNo = tableEndIdentifierTdNum;
	}
	
	@Column(name = "TABLEROWSTOIGNORE")
	public String getTableRowsToIgnore() {
		return tableRowsToIgnore;
	}
	
	public void setTableRowsToIgnore(String tableRowsToIgnore) {
		this.tableRowsToIgnore = tableRowsToIgnore;
	}

	
	@Override
    public Object clone() throws CloneNotSupportedException {
		Date date = new Date();
		ParserGroupAttribute parserGroupAttribute = (ParserGroupAttribute) super.clone();
		parserGroupAttribute.setId(0);
		parserGroupAttribute.setName(EliteUtils.checkForNames(BaseConstants.IMPORT, parserGroupAttribute.getName()));
		parserGroupAttribute.setCreatedDate(date);
		parserGroupAttribute.setLastUpdatedDate(date);
		
		List<ParserAttribute> oldList = parserGroupAttribute.getAttributeList();
		List<ParserAttribute> newList = new ArrayList<>();
		
		if(oldList != null && !oldList.isEmpty()) {
			int length = oldList.size();
			for(int i = length-1; i >= 0; i--) {
				ParserAttribute attribute = (ParserAttribute) oldList.get(i).clone();
				attribute.setParserMapping(parserGroupAttribute.getParserMapping());
				attribute.setParserGroupAttribute(parserGroupAttribute);
				newList.add(attribute);
			}
		}
		
		parserGroupAttribute.setAttributeList(newList);
		
		List<ParserGroupAttribute> oldGroupList = parserGroupAttribute.getGroupAttributeList();
		List<ParserGroupAttribute> newGroupList = new ArrayList<>();
		
		if(oldGroupList != null && !oldGroupList.isEmpty()) {
			int length = oldGroupList.size();
			for(int i = length-1; i >= 0; i--) {
				ParserGroupAttribute groupAttribute = (ParserGroupAttribute) oldGroupList.get(i).clone();
				groupAttribute.setParserMapping(parserGroupAttribute.getParserMapping());
				groupAttribute.setBaseGroup(parserGroupAttribute);
				newGroupList.add(groupAttribute);
			}
		}
		
		parserGroupAttribute.setGroupAttributeList(newGroupList);
		
        return parserGroupAttribute;
    }
	
	@Column(name = "TABLEENDIDENTIFIERROWLOC", length = 100)
	public String getTableEndIdentifierRowLocation() {
		return tableEndIdentifierRowLocation;
	}
	public void setTableEndIdentifierRowLocation(String tableEndIdentifierRowLocation) {
		this.tableEndIdentifierRowLocation = tableEndIdentifierRowLocation;
	}
	
	@Column(name = "TABLEENDIDENTIFIEROCC", length = 300)
	public String getTableEndIdentifierOccurence() {
		return tableEndIdentifierOccurence;
	}
	public void setTableEndIdentifierOccurence(String tableEndIdentifierOccurence) {
		this.tableEndIdentifierOccurence = tableEndIdentifierOccurence;
	}
	
	@Column(name = "TABLEROWIDENTIFIER", length = 300)
	public String getTableRowIdentifier() {
		return tableRowIdentifier;
	}
	public void setTableRowIdentifier(String tableRowIdentifier) {
		this.tableRowIdentifier = tableRowIdentifier;
	}
	
}
