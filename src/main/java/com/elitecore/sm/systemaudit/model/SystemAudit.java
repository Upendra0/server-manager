/**
 * 
 */
package com.elitecore.sm.systemaudit.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.StateEnum;

/**
 * @author Ranjitsinh Reval
 *
 */

@Component(value="systemAudit")
@Scope(value="prototype")
@Entity()
@Table(name = "TBLTSYSTEMAUDIT")
@DynamicUpdate
@XmlRootElement
public class SystemAudit  implements Serializable{

	private static final long serialVersionUID = -5944917390014929891L;
	
	private int id;
	private Date auditDate;
	private String staffId;
	private String userName;
	private String IPAddress;
	private String remark;
	private String actionType;
	private AuditEntity systemAuditEntity;
	private AuditSubEntity systemAuditSubEntity;
	private AuditActivity systemAuditActivity;
	private List<SystemAuditDetails> systemAuditDetails  = new ArrayList<>(0);
	private StateEnum status = StateEnum.ACTIVE;
	
	/**
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="SystemAudit",allocationSize=1)
	@Column(name="ID")
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	
	
	/**
	 * @return the auditDate
	 */
	@Column(name = "AUDITDATE", nullable = false, length = 60)
	@XmlElement
	public Date getAuditDate() {
		return auditDate;
	}
	/**
	 * @param auditDate the auditDate to set
	 */
	public void setAuditDate(Date auditDate) {
		this.auditDate = auditDate;
	}
	/**
	 * @return the iPAddress
	 */
	@Column(name = "IPADDRESS", length = 16)
	@XmlElement
	public String getIPAddress() {
		return IPAddress;
	}
	/**
	 * @param iPAddress the iPAddress to set
	 */
	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

	/**
	 * @return the systemAuditEntity
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTITYID", nullable = false,  foreignKey = @ForeignKey(name = "FK_SYS_ADT_ENTID"))
	@XmlElement
	public AuditEntity getSystemAuditEntity() {
		return systemAuditEntity;
	}
	/**
	 * @param systemAuditEntity the systemAuditEntity to set
	 */
	public void setSystemAuditEntity(AuditEntity systemAuditEntity) {
		this.systemAuditEntity = systemAuditEntity;
	}
	/**
	 * @return the systemAuditSubEntity
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SUBENTITYID", nullable = false,foreignKey = @ForeignKey(name = "FK_SYS_ADT_SUBID"))
	@XmlElement
	public AuditSubEntity getSystemAuditSubEntity() {
		return systemAuditSubEntity;
	}
	/**
	 * @param systemAuditSubEntity the systemAuditSubEntity to set
	 */
	public void setSystemAuditSubEntity(AuditSubEntity systemAuditSubEntity) {
		this.systemAuditSubEntity = systemAuditSubEntity;
	}
	/**
	 * @return the systemAuditActivity
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACTIVITYID", nullable = false, foreignKey = @ForeignKey(name = "FK_SYS_ADT_ACTID"))
	@XmlElement
	public AuditActivity getSystemAuditActivity() {
		return systemAuditActivity;
	}
	/**
	 * @param systemAuditActivity the systemAuditActivity to set
	 */
	public void setSystemAuditActivity(AuditActivity systemAuditActivity) {
		this.systemAuditActivity = systemAuditActivity;
	}
	/**
	 * @return the systemAuditDetails
	 */
	@OneToMany(mappedBy = "systemAudit",fetch = FetchType.LAZY)
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL)
	@XmlElement
	public List<SystemAuditDetails> getSystemAuditDetails() {
		return systemAuditDetails;
	}
	/**
	 * @param systemAuditDetails the systemAuditDetails to set
	 */
	public void setSystemAuditDetails(List<SystemAuditDetails> systemAuditDetails) {
		this.systemAuditDetails = systemAuditDetails;
	}
	/**
	 * @return the staffId
	 */
	@Column(name = "STAFFID", nullable = false, length = 7)
	@XmlElement
	public String getStaffId() {
		return staffId;
	}
	/**
	 * @param staffId the staffId to set
	 */
	public void setStaffId(String staffId) {
		this.staffId = staffId;
	}
	/**
	 * @return the userName
	 */
	@Column(name = "USERNAME", nullable = false, length = 60)
	@XmlElement
	public String getUserName() {
		return userName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}
	/**
	 * @return the remark
	 */
	@Column(name = "REMARK",length = 4000)
	@XmlElement
	public String getRemark() {
		return remark;
	}
	/**
	 * @param remark the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}
	/**
	 * @return the status
	 */
	@XmlElement
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS" ,nullable=false)
	public StateEnum getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(StateEnum status) {
		this.status = status;
	}
	/**
	 * @return the actionType
	 */
	@XmlElement
	@Column(name = "ACTIONTYPE" ,nullable=false)
	public String getActionType() {
		return actionType;
	}
	/**
	 * @param actionType the actionType to set
	 */
	public void setActionType(String actionType) {
		this.actionType = actionType;
	}
	
}
