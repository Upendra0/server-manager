package com.elitecore.sm.scripteditor.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import com.elitecore.sm.common.model.BaseModel;


/**
 * The persistent class for the TBLMSERVERCONFIGMST database table.
 * 
 */
@Entity
@Table(name="TBLMSERVERCONFIGMST")
@NamedQuery(name="ServerConfiguration.findAll", query="SELECT s FROM ServerConfiguration s")
public class ServerConfiguration extends BaseModel {
	
	private static final long serialVersionUID = 1L;

	private long serverId;

	private String os;

	private String password;

	private String serverIp;

	private String serverName;

	private String serverPort;

	private String userName;

	private List<FileConfiguration> tblmfileconfigmsts;

	public ServerConfiguration() {
	}

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
							     pkColumnName="TABLE_NAME",valueColumnName="VALUE",
							     pkColumnValue="ScriptMgr-ServerConfig",allocationSize=1)
	@Column(name="SERVERID", nullable=false)
	public long getServerId() {
		return serverId;
	}

	public void setServerId(long serverId) {
		this.serverId = serverId;
	}

	@Column(name="OS", nullable=false)
	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	@Column(name="PASSWORD", nullable=false)
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name="SERVERIP", nullable=false)
	public String getServerIp() {
		return serverIp;
	}

	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}

	@Column(name="SERVERNAME", nullable=false)
	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	@Column(name="SERVERPORT", nullable=false)
	public String getServerPort() {
		return serverPort;
	}

	public void setServerPort(String serverPort) {
		this.serverPort = serverPort;
	}

	@Column(name="USERNAME", nullable=false)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	//bi-directional many-to-one association to FileConfiguration
	@OneToMany(mappedBy="tblmserverconfigmst")
	public List<FileConfiguration> getTblmfileconfigmsts() {
		return this.tblmfileconfigmsts;
	}

	public void setTblmfileconfigmsts(List<FileConfiguration> tblmfileconfigmsts) {
		this.tblmfileconfigmsts = tblmfileconfigmsts;
	}

	public FileConfiguration addTblmfileconfigmst(FileConfiguration tblmfileconfigmst) {
		getTblmfileconfigmsts().add(tblmfileconfigmst);
		tblmfileconfigmst.setTblmserverconfigmst(this);

		return tblmfileconfigmst;
	}

	public FileConfiguration removeTblmfileconfigmst(FileConfiguration tblmfileconfigmst) {
		getTblmfileconfigmsts().remove(tblmfileconfigmst);
		tblmfileconfigmst.setTblmserverconfigmst(null);

		return tblmfileconfigmst;
	}

}