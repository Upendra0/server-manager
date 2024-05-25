	/**
 * 
 */
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

/**
 * @author Ranjitsinh
 *
 */

@Component(value="systemAuditDetails")
@Scope(value="prototype")
@Entity()
@Table(name = "TBLTSYSTEMAUDITDETAIL")
@DynamicUpdate
@XmlRootElement
public class SystemAuditDetails implements Serializable{

	private static final long serialVersionUID = -5944917390014929891L;
	private int id;
	private String fieldname;
	private String oldvalue;
	private String newvalue;
	private SystemAudit systemAudit;
	
	private StateEnum status = StateEnum.ACTIVE;
	
	public SystemAuditDetails(){
		//Default Constructor
	}
	public SystemAuditDetails(String fieldname, String oldvalue, String newvalue) {
		super();
		this.fieldname = fieldname;
		this.oldvalue = oldvalue;
		this.newvalue = newvalue;
	}
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="SystemAuditDetails",allocationSize=1)
	@Column(name="ID")
	@XmlElement
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the fieldname
	 */
	@Column(name = "FIELDNAME", length = 100, nullable = false)
	@XmlElement
	public String getFieldname() {
		return fieldname;
	}
	/**
	 * @param fieldname the fieldname to set
	 */
	public void setFieldname(String fieldname) {
		this.fieldname = fieldname;
	}
	/**
	 * @return the oldvalue
	 */
	@Column(name = "OLDVALUE", length = 4000, nullable = true)
	@XmlElement
	public String getOldvalue() {
		return oldvalue;
	}
	/**
	 * @param oldvalue the oldvalue to set
	 */
	public void setOldvalue(String oldvalue) {
		this.oldvalue = oldvalue;
	}
	/**
	 * @return the newvalue
	 */
	@Column(name = "NEWVALUE", length = 4000, nullable = true)
	@XmlElement
	public String getNewvalue() {
		return newvalue;
	}
	/**
	 * @param newvalue the newvalue to set
	 */
	public void setNewvalue(String newvalue) {
		this.newvalue = newvalue;
	}
	/**
	 * @return the systemAudit
	 */
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SYSTEMAUDITID", nullable = false, foreignKey = @ForeignKey(name = "FK_SYS_ADT_DTLS_SYSADTID"))
	@XmlElement
	public SystemAudit getSystemAudit() {
		return systemAudit;
	}
	/**
	 * @param systemAudit the systemAudit to set
	 */
	public void setSystemAudit(SystemAudit systemAudit) {
		this.systemAudit = systemAudit;
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

}
