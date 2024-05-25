/**
 * 
 */
package com.elitecore.sm.migration.model;

import java.util.Date;

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

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Type;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.server.model.Server;
import com.elitecore.sm.services.model.MigrationStatusEnum;


/**
 * @author Ranjitsinh Reval
 *
 */

@Entity()
@Table(name = "TBLTMIGRATIONTRACKDETAILS")
@DynamicUpdate
public class MigrationTrackDetails extends  BaseModel {
	
	private static final long serialVersionUID = 1L;

	private int id;
	private Server server;
	private Date migrationStartDate;
	private Date migrationEndDate;
	private String serverInstancePrefix;
	private int serverInstancePort;
	private String serverInstanceScriptName;
	private MigrationStatusEnum migrationStatus = MigrationStatusEnum.OPEN;
	private int reprocessAttemptNumber = 0;
	private String remark;
	private String servicesList;
	

	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="MigrationTrackDetails",allocationSize=1)
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
	 * @return the server
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SERVERID", nullable = false, foreignKey = @ForeignKey(name = "FK_SERVER_MIGRATION_TRACKING"))
	public Server getServer() {
		return server;
	}
	
	/**
	 * @param server the server to set
	 */
	public void setServer(Server server) {
		this.server = server;
	}
	
	/**
	 * @return the migrationStartDate
	 */
	@Column(name = "MIGRATIONSTARTDATE", nullable = false)
	@Type(type = "timestamp")
	public Date getMigrationStartDate() {
		return migrationStartDate;
	}
	
	/**
	 * @param migrationStartDate the migrationStartDate to set
	 */
	public void setMigrationStartDate(Date migrationStartDate) {
		this.migrationStartDate = migrationStartDate;
	}
	
	/**
	 * @return the migrationEndDate
	 */
	@Column(name = "MIGRATIONENDDATE", nullable = true)
	@Type(type = "timestamp")
	public Date getMigrationEndDate() {
		return migrationEndDate;
	}
	
	/**
	 * @param migrationEndDate the migrationEndDate to set
	 */
	public void setMigrationEndDate(Date migrationEndDate) {
		this.migrationEndDate = migrationEndDate;
	}
	
	/**
	 * @return the serverInstancePrefix
	 */
	@Column(name = "PREFIX", nullable = false,length = 30)
	public String getServerInstancePrefix() {
		return serverInstancePrefix;
	}
	
	/**
	 * @param serverInstancePrefix the serverInstancePrefix to set
	 */
	public void setServerInstancePrefix(String serverInstancePrefix) {
		this.serverInstancePrefix = serverInstancePrefix;
	}
	
	/**
	 * @return the serverInstancePort
	 */
	@Column(name = "PORT", nullable = false, length = 6)
	public int getServerInstancePort() {
		return serverInstancePort;
	}

	/**
	 * @param serverInstancePort the serverInstancePort to set
	 */
	public void setServerInstancePort(int serverInstancePort) {
		this.serverInstancePort = serverInstancePort;
	}
	
	/**
	 * @return the serverInstanceScriptName
	 */
	@Column(name = "SCRIPTNAME", nullable = false, length = 50)
	public String getServerInstanceScriptName() {
		return serverInstanceScriptName;
	}
	
	/**
	 * @param serverInstanceScriptName the serverInstanceScriptName to set
	 */
	public void setServerInstanceScriptName(String serverInstanceScriptName) {
		this.serverInstanceScriptName = serverInstanceScriptName;
	}
	
	/**
	 * @return the migrationStatus
	 */
	@Column(name = "MIGRATIONSTATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	public MigrationStatusEnum getMigrationStatus() {
		return migrationStatus;
	}
	
	/**
	 * @param migrationStatus the migrationStatus to set
	 */
	public void setMigrationStatus(MigrationStatusEnum migrationStatus) {
		this.migrationStatus = migrationStatus;
	}
	
	/**
	 * @return the reprocessAttemptNumber
	 */
	@Column(name = "REPROCESSNUMBER", nullable = false)
	public int getReprocessAttemptNumber() {
		return reprocessAttemptNumber;
	}
	/**
	 * @param reprocessAttemptNumber the reprocessAttemptNumber to set
	 */
	public void setReprocessAttemptNumber(int reprocessAttemptNumber) {
		this.reprocessAttemptNumber = reprocessAttemptNumber;
	}
	/**
	 * @return the remark
	 */
	@Column(name = "REMARK", length = 4000)
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
	 * 
	 * @return services
	 */
	@Column(name = "SERVICESLIST", length = 4000, nullable=true)
	public String getServicesList() {
		return servicesList;
	}

	public void setServicesList(String servicesList) {
		this.servicesList = servicesList;
	}
}
