/**
 * 
 */
package com.elitecore.sm.systemaudit.model;

import java.io.Serializable;
import java.util.ArrayList;
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

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.StateEnum;
import com.elitecore.sm.common.model.ToStringProcessor;

/**
 * @author Ranjitsinh 
 *
 */
@Component(value="auditSubEntity")
@Scope(value="prototype")
@Entity()
@Table(name = "TBLMAUDITSUBENTITY")
@DynamicUpdate
@XmlRootElement
public class AuditSubEntity  extends ToStringProcessor implements Serializable{

	private static final long serialVersionUID = -5944917390014929891L;
	
	private int id;
	private String name;
	private String alias;
	private String discription;
	private AuditEntity auditEntity;
	private List<AuditActivity>   auditActivities = new ArrayList<>(0);
	//private List<SystemAudit> systemAudits = new ArrayList<SystemAudit>(0);
	
	private StateEnum status = StateEnum.ACTIVE;
	
	/**
	 * @return the subEntityId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="AuditSubEntity",allocationSize=1)
	
	/**
	 * @return the id
	 */
	@Column(name="ID")
	@XmlElement
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
	 * @return the name
	 */

	@Column(name = "NAME", nullable = false, length = 60)
	@XmlElement
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the alias
	 */
	@Column(name = "ALIAS", nullable = false, length = 60)
	@XmlElement
	public String getAlias() {
		return alias;
	}
	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.alias = alias;
	}
	/**
	 * @return the discription
	 */
	@Column(name = "DESCRIPTION", length = 4000)
	@XmlElement
	public String getDiscription() {
		return discription;
	}
	/**
	 * @param discription the discription to set
	 */
	public void setDiscription(String discription) {
		this.discription = discription;
	}
	/**
	 * @return the auditEntity
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ENTITYID", nullable = false, insertable = false, updatable = false,  foreignKey = @ForeignKey(name = "FK_AUDIT_SUBENTITY_ENTITYID"))
	@XmlElement
	public AuditEntity getAuditEntity() {
		return auditEntity;
	}
	/**
	 * @param auditEntity the auditEntity to set
	 */
	public void setAuditEntity(AuditEntity auditEntity) {
		this.auditEntity = auditEntity;
	}
	/**
	 * @return the auditActivities
	 */
	@OneToMany(mappedBy = "auditSubEntity")
	@XmlElement
	public List<AuditActivity> getAuditActivities() {
		return auditActivities;
	}
	/**
	 * @param auditActivities the auditActivities to set
	 */
	public void setAuditActivities(List<AuditActivity> auditActivities) {
		this.auditActivities = auditActivities;
	}
	/**
	 * @return the systemAudits
	 */
	/*@OneToMany(mappedBy = "systemAuditSubEntity")
	@XmlElement
	public List<SystemAudit> getSystemAudits() {
		return systemAudits;
	}*/
	/**
	 * @param systemAudits the systemAudits to set
	 */
	/*public void setSystemAudits(List<SystemAudit> systemAudits) {
		this.systemAudits = systemAudits;
	}*/
	/**
	 * @return the status
	 */
	@XmlElement
	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS" ,nullable=false)
	public StateEnum getStatus() {
		return status;
	}
	public void setStatus(StateEnum status) {
		this.status = status;
	}
	
	
}
