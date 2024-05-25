package com.elitecore.sm.systemaudit.model;

import java.io.Serializable;

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
@Component(value="auditActivity")
@Scope(value="prototype")
@Entity()
@Table(name = "TBLMAUDITACTIVITY")
@DynamicUpdate
@XmlRootElement
public class AuditActivity extends ToStringProcessor implements Serializable{

	private static final long serialVersionUID = -5944917390014929891L;
	
	private int id;
	private String name;	
	private String alias;
	private String message;
	private String discription;
	
	private AuditSubEntity auditSubEntity;
	/*private List<SystemAudit> systemAudits = new ArrayList<SystemAudit>(0);*/
	
	private StateEnum status = StateEnum.ACTIVE;
	/**
	 * @return the activityId
	 */
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="AuditActivity",allocationSize=1)
	/**
	 * @return the id
	 */
	@Column(name = "ID")
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
	 * @return the message
	 */
	@Column(name = "MESSAGE", length = 4000)
	@XmlElement
	public String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
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
	 * @return the auditSubEntity
	 */
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "SUBENTITYID", nullable = false, foreignKey = @ForeignKey(name = "FK_AUDIT_ACTIVITY_SUBENTITYID"))
	@XmlElement
	public AuditSubEntity getAuditSubEntity() {
		return auditSubEntity;
	}
	/**
	 * @param auditSubEntity the auditSubEntity to set
	 */
	public void setAuditSubEntity(AuditSubEntity auditSubEntity) {
		this.auditSubEntity = auditSubEntity;
	}
	/**
	 * @return the systemAudits
	 */
	/*@OneToMany(mappedBy = "systemAuditActivity")
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
