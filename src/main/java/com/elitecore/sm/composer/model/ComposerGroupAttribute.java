package com.elitecore.sm.composer.model;

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

@Entity
@Component
@Table(name = "TBLTCOMPOSERGROUPATTRIBUTE")
@DynamicUpdate
public class ComposerGroupAttribute extends BaseModel implements Serializable{
	
	private static final long serialVersionUID = 9071421575399943271L;

	private int id;
	private String name;
	private ComposerGroupAttribute baseGroup;
	private ComposerMapping myComposer;
	private List<ComposerAttribute> attributeList = new ArrayList<>(0);
	private List<ComposerGroupAttribute> groupAttributeList = new ArrayList<>(0);
	private boolean associatedByGroup;
	
	@Id
    @XmlElement
    @Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="ComposerGroupAttribute",allocationSize=1)
	public int getId() {
		return id;
	}
	
	@Column(name = "NAME")
	public String getName() {
		return name;
	}
	
	@XmlTransient
	@ManyToOne(targetEntity = ComposerGroupAttribute.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "BASEGROUPID", nullable = true, foreignKey = @ForeignKey(name = "FK_COMPOSERBASEGROUPID"))
	public ComposerGroupAttribute getBaseGroup() {
		return baseGroup;
	}
	
	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "COMPOSERMAPPINGID", nullable = true, foreignKey = @ForeignKey(name = "FK_GROUPATTR_COMPOSERMAPPING"))
	public ComposerMapping getMyComposer() {
		return myComposer;
	}

	@XmlElement
	@OneToMany(targetEntity = ComposerAttribute.class, mappedBy = "composerGroupAttribute", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<ComposerAttribute> getAttributeList() {
		return attributeList;
	}
	
	@XmlElement
	@OneToMany(targetEntity = ComposerGroupAttribute.class, mappedBy = "baseGroup", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<ComposerGroupAttribute> getGroupAttributeList() {
		return groupAttributeList;
	}	
	
	@Transient
	@XmlElement
	public boolean isAssociatedByGroup() {
		if(getBaseGroup() != null){
			this.associatedByGroup = true;
		}
		return associatedByGroup;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setBaseGroup(ComposerGroupAttribute baseGroup) {
		this.baseGroup = baseGroup;
	}
	
	public void setMyComposer(ComposerMapping myComposer) {
		this.myComposer = myComposer;
	}

	public void setAttributeList(List<ComposerAttribute> attributeList) {
		this.attributeList = attributeList;
	}

	public void setGroupAttributeList(List<ComposerGroupAttribute> groupAttributeList) {
		this.groupAttributeList = groupAttributeList;
	}

	public void setAssociatedByGroup(boolean associatedByGroup) {
		this.associatedByGroup = associatedByGroup;
	}
	
	@Override
    public Object clone() throws CloneNotSupportedException {
		Date date = new Date();
		ComposerGroupAttribute composerGroupAttribute = (ComposerGroupAttribute) super.clone();
		composerGroupAttribute.setId(0);
		composerGroupAttribute.setName(EliteUtils.checkForNames(BaseConstants.IMPORT, composerGroupAttribute.getName()));
		composerGroupAttribute.setCreatedDate(date);
		composerGroupAttribute.setLastUpdatedDate(date);
		
		List<ComposerAttribute> oldList = composerGroupAttribute.getAttributeList();
		List<ComposerAttribute> newList = new ArrayList<>();
		
		if(oldList != null && !oldList.isEmpty()) {
			int length = oldList.size();
			for(int i = length-1; i >= 0; i--) {
				ComposerAttribute attribute = (ComposerAttribute) oldList.get(i).clone();
				attribute.setMyComposer(composerGroupAttribute.getMyComposer());
				newList.add(attribute);
			}
		}
		
		composerGroupAttribute.setAttributeList(newList);
		
		List<ComposerGroupAttribute> oldGroupList = composerGroupAttribute.getGroupAttributeList();
		List<ComposerGroupAttribute> newGroupList = new ArrayList<>();
		
		if(oldGroupList != null && !oldGroupList.isEmpty()) {
			int length = oldGroupList.size();
			for(int i = length-1; i >= 0; i--) {
				ComposerGroupAttribute groupAttribute = (ComposerGroupAttribute) oldGroupList.get(i).clone();
				groupAttribute.setMyComposer(composerGroupAttribute.getMyComposer());
				groupAttribute.setBaseGroup(composerGroupAttribute);
				newGroupList.add(groupAttribute);
			}
		}
		
		composerGroupAttribute.setGroupAttributeList(newGroupList);
		
        return composerGroupAttribute;
    }
	
}