/**
 * 
 */
package com.elitecore.sm.iam.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.json.simple.JSONValue;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.util.DateFormatter;

import javax.persistence.UniqueConstraint;

/**
 * @author Sunil Gulabani
 * Mar 31, 2015
 */


@Component(value="accessGroupObject")
@Scope(value="prototype")
@Entity
@Table(name="TBLMACCESSGROUP")
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="accessGroupCache")
public class AccessGroup  extends BaseModel implements Serializable{
	private static final long serialVersionUID = -5964486511066748715L;

	private int id;
	
	private String name;
	
	private String description;
	
	private List<Action> actions = new ArrayList<>(0);

	private StateEnum accessGroupState;
	
	private transient Staff createdByStaff;

	private Date createDate;
	
	private Date lastUpdateDate;
	
	private int lastUpdateByStaffId;
	
	private String accessGroupType = "LOCAL";
	
	@Override
	public String toString() {
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("name", name);
		map.put("description", description);
		map.put("accessGroupType", accessGroupType);
		if(accessGroupState!=null){
			map.put("accessGroupState", accessGroupState.toString());
		}
		map.put("createdByStaffId", createdByStaffId);
		if(createDate!=null){
			map.put("createDate", DateFormatter.formatDate(createDate));
		}
		map.put("lastUpdateByStaffId", lastUpdateByStaffId);
		
		if(lastUpdateDate!=null){
			map.put("lastUpdateDate", DateFormatter.formatDate(lastUpdateDate));
		}
		return JSONValue.toJSONString(map);
	}
	
	public AccessGroup() {
		createDate = new Date();
		lastUpdateDate = new Date();
	}
	
	public AccessGroup(Staff staff) {
		
		if(staff!=null){
			createdByStaffId = staff.getId();
			lastUpdateByStaffId = staff.getId();
		}
		createDate = new Date();
		lastUpdateDate = new Date();
	}
	
	public void initializeValues(Staff staff, boolean isUpdate){
		
		if(isUpdate){
			if(staff!=null){
				setLastUpdateByStaffId(staff.getId());
			}
			setLastUpdateDate(new Date());
		}else{
			setAccessGroupState(StateEnum.ACTIVE);
			if(staff!=null){
				setCreatedByStaffId(staff.getId());
				setLastUpdateByStaffId(staff.getId());
			}
			setCreateDate(new Date());
			setLastUpdateDate(new Date());
		}
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="AccessGroup",allocationSize=1,uniqueConstraints=@UniqueConstraint(columnNames={"TABLE_NAME"}))
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name="name", nullable = false, length = 60,unique=true)
	public String getName() {
		if(name !=null)
			name = name.trim();
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name="description", nullable = true, length = 250)
	public String getDescription() {
		if(description!=null)
			description = description.trim();
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@ManyToMany(targetEntity=Action.class, fetch = FetchType.EAGER)
	@JoinTable(name="TBLTACCESSGROUPACTIONREL",joinColumns=@JoinColumn (name="accessgroupid",referencedColumnName="id"),inverseJoinColumns=@JoinColumn(name="actionid",referencedColumnName="id"))
	public List<Action> getActions() {
		return actions;
	}

	public void setActions(List<Action> actions) {
		this.actions = actions;
	}

	@Enumerated(EnumType.STRING)
	@Column(name="state")
	public StateEnum getAccessGroupState() {
		return accessGroupState;
	}

	public void setAccessGroupState(StateEnum accessGroupState) {
		this.accessGroupState = accessGroupState;
	}
	
	@Column(name = "createdate",nullable=false)
	@Type(type="timestamp")
	@DiffIgnore
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	@Column(name = "lastupdatedate",nullable=false)
	@Type(type="timestamp")
	@DiffIgnore
	public Date getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

    @Column(name = "lastupdatebystaffid",nullable=false)
	public int getLastUpdateByStaffId() {
		return lastUpdateByStaffId;
	}

	public void setLastUpdateByStaffId(int lastUpdateByStaffId) {
		this.lastUpdateByStaffId = lastUpdateByStaffId;
	}

	@Column(name = "ACCESSGROUPTYPE",nullable=false)
	public String getAccessGroupType() {
		return accessGroupType;
	}

	public void setAccessGroupType(String accessGroupType) {
		this.accessGroupType = accessGroupType;
	}

	@Transient
	public Staff getCreatedByStaff() {
		return createdByStaff;
	}

	public void setCreatedByStaff(Staff createdByStaff) {
		this.createdByStaff = createdByStaff;
	}
}