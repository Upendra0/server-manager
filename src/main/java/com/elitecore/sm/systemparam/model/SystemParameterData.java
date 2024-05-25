/**
 * 
 */
package com.elitecore.sm.systemparam.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.model.StateEnum;

/**
 * @author vandana.awatramani
 *
 */

@Entity
@Table(name = "TBLMSYSTEMPARAMETER")
@DynamicUpdate
@NamedQueries({@NamedQuery(name="findSysParamDataByAlias",query="from SystemParameterData where alias=:arg1"),
							@NamedQuery(name="getAllSystemParam" , query="from SystemParameterData where enabled=:arg1")})
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="systemParameterCache")
public class SystemParameterData extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String name;
	private String description;
	private String alias;
	private String value;
	private List<SystemParameterValuePoolData> parameterDetail;
	private String regularExpression;
	private String errorMessage;
	private SystemParameterGroupData parameterGroup;
	private boolean enabled;
	private int displayOrder;
	private byte[] image;
	
	private String passwordDescription;
	
	@Transient
	public String getPasswordDescription() {
		return passwordDescription;
	}


	public void setPasswordDescription(String passwordDescription) {
		this.passwordDescription = passwordDescription;
	}


	public SystemParameterData() {
		//Default constructor
	}

	
	/**
	 * 
	 * @param name
	 * @param alias
	 * @param value
	 * @param enabled
	 * @param description
	 * @param regularExpression
	 * @param errorMessage
	 * @param parameterGroup
	 * @param displayOrder
	 * @param createdDate
	 * @param createdByStaffId
	 * @param lastUpdatedDate
	 * @param lastUpdatedByStaffId
	 */
	public SystemParameterData(String name, String alias, String value,boolean enabled, String description,String regularExpression, 
			String errorMessage,SystemParameterGroupData parameterGroup, int displayOrder,Date createdDate,int createdByStaffId,
			Date lastUpdatedDate,int lastUpdatedByStaffId) {
		super(createdDate,createdByStaffId,lastUpdatedDate,lastUpdatedByStaffId,StateEnum.ACTIVE);
		this.name = name;
		this.description = description;
		this.alias = alias;
		this.value = value;
		this.regularExpression = regularExpression;
		this.errorMessage = errorMessage;
		this.parameterGroup = parameterGroup;
		this.enabled = enabled;
		this.displayOrder = displayOrder;
	}
	

	/**
	 * @return the name
	 */
	@Column(name = "NAME", nullable = false, length = 50, unique = true)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	@Column(name = "DESCRIPTION", nullable = false, length = 255)
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the alias
	 */
	@Column(name = "ALIAS", nullable = false, length = 60, unique = true)
	public String getAlias() {
		return alias;
	}

	/**
	 * @param alias
	 *            the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @return the value
	 */
	@Column(name = "VALUE", length = 255,nullable=true)
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the parameterDetail
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentSystemParameter", cascade = CascadeType.ALL)
	public List<SystemParameterValuePoolData> getParameterDetail() {
		return parameterDetail;
	}

	/**
	 * @param parameterDetail
	 *            the parameterDetail to set
	 */
	public void setParameterDetail(
			List<SystemParameterValuePoolData> parameterDetail) {
		this.parameterDetail = parameterDetail;
	}

	/**
	 * @return the regularExpression
	 */
	@Column(name = "REGEX", nullable = false, length = 100)
	public String getRegularExpression() {
		return regularExpression;
	}

	/**
	 * @param regularExpression
	 *            the regularExpression to set
	 */
	public void setRegularExpression(String regularExpression) {
		this.regularExpression = regularExpression;
	}

	/**
	 * @return the errorMessage
	 */
	@Column(name = "ERRORMSG", length = 400)
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *            the errorMessage to set
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	/**
	 * @return the parameterGroupId
	 */

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SYSTEMPARAMGROUPID", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "FK_SYSPARAM_GROUP"))
	public SystemParameterGroupData getParameterGroup() {
		return parameterGroup;
	}

	/**
	 * @param parameterGroupId
	 *            the parameterGroupId to set
	 */

	public void setParameterGroup(SystemParameterGroupData parameterGroup) {
		this.parameterGroup = parameterGroup;
	}

	/**
	 * @param enabled
	 *            the enabled to set
	 */
	public void setenabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the enabled
	 */
	@Type(type = "yes_no")
	@Column(name = "enabled")
	public boolean isenabled() {
		return enabled;
	}

	/**
	 * @return the displayOrder
	 */
	@Column(name = "DISPLAYORDER", nullable = false, unique = true, length = 5)
	public int getDisplayOrder() {
		return displayOrder;
	}

	/**
	 * @param displayOrder
	 *            the displayOrder to set
	 */
	public void setDisplayOrder(int displayOrder) {
		this.displayOrder = displayOrder;
	}


	/**
	 * @return the id
	 */
	@Id
	@Column(name = "PARAMETERID", length = 8)
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							pkColumnValue="SystemParameterData",allocationSize=1)
	public int getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	@Lob
	@Basic(fetch=FetchType.EAGER)
	@Column(name = "IMAGE")
	public byte[] getImage() { 
		return image; 
	}
	
	public void setImage(byte[] image){
		this.image = image; 
	}

	
	}
