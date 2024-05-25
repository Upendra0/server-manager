/**
 * 
 */
package com.elitecore.sm.agent.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Cacheable;
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.pathlist.model.CharRenameOperation;
import com.elitecore.sm.services.model.Service;

/**
 * @author vandana.awatramani
 *
 */
@Component(value = "serviceFileRenameConfig")
@Scope(value = "prototype")
@Entity()
@Table(name = "TBLTSERVICEFILERENAME")
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="serviceFileRenameConfigCache")
@XmlRootElement
@XmlType(propOrder = { "id","service", "destinationPath", "fileExtensitonList", "extAfterRename", 
		"charRenameOperationEnable","charRenameOpList"})
public class ServiceFileRenameConfig extends BaseModel implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 973803458124373481L;
	private int id;
	private Service service;
	private String destinationPath;
	// Comma separated file extensions .inp,.warn,.error
	// may decide to have list of enums or such.
	private String fileExtensitonList;
	private String extAfterRename;
	
	private boolean charRenameOperationEnable=false;
	private List<CharRenameOperation> charRenameOpList;
	private FileRenameAgent agent;

	/**
	 * @return the id
	 */

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "UniqueIdGenerator")
	@TableGenerator(name = "UniqueIdGenerator", table = "TBLTPRIMARYKEY", pkColumnName = "TABLE_NAME", valueColumnName = "VALUE", pkColumnValue = "SERVICEFILERENAME", allocationSize = 1)
	public int getId() {
		return id;
	}

	/**
	 * @return the service
	 */
	@XmlElement
	@OneToOne(fetch = FetchType.EAGER, optional = false)
	@Cascade(value = { org.hibernate.annotations.CascadeType.ALL })
	@JoinColumn(name = "SERVICEID", nullable = true, foreignKey = @ForeignKey(name = "FK_ServiceFileRename"))
	public Service getService() {
		return service;
	}

	/**
	 * @return the destinationPath
	 */
	@Column(name = "DESTPATH", nullable = true, length = 500)
	@XmlElement
	public String getDestinationPath() {
		return destinationPath;
	}

	/**
	 * @return the fileExtensitonList
	 */

	@Column(name = "FILEEXTLIST", nullable = false, length = 500)
	@XmlElement
	public String getFileExtensitonList() {
		return fileExtensitonList;
	}

	/**
	 * @return the extAfterRename
	 */
	@Column(name = "EXTAFTERRENAME", nullable = true, length = 500)
	@XmlElement
	public String getExtAfterRename() {
		return extAfterRename;
	}

	/**
	 * @return the charRenameOpList
	 */
	@OneToMany(mappedBy = "svcFileRenConfig" , fetch = FetchType.EAGER)
	@Cascade(value = { org.hibernate.annotations.CascadeType.ALL })
	@XmlElement
	public List<CharRenameOperation> getCharRenameOpList() {
		return charRenameOpList;
	}

	/**
	 * @return the agent
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "AGENTID", nullable = false, foreignKey = @ForeignKey(name = "FK_SERVICEFILEREN_AGENT"))
	@XmlTransient
	public FileRenameAgent getAgent() {
		return agent;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param service
	 *            the service to set
	 */
	public void setService(Service service) {
		this.service = service;
	}

	/**
	 * @param destinationPath
	 *            the destinationPath to set
	 */
	public void setDestinationPath(String destinationPath) {
		this.destinationPath = destinationPath;
	}

	/**
	 * @param fileExtensitonList
	 *            the fileExtensitonList to set
	 */
	public void setFileExtensitonList(String fileExtensitonList) {
		this.fileExtensitonList = fileExtensitonList;
	}

	/**
	 * @param extAfterRename
	 *            the extAfterRename to set
	 */
	public void setExtAfterRename(String extAfterRename) {
		this.extAfterRename = extAfterRename;
	}

	/**
	 * @param charRenameOpList
	 *            the charRenameOpList to set
	 */
	public void setCharRenameOpList(List<CharRenameOperation> charRenameOpList) {
		this.charRenameOpList = charRenameOpList;
	}

	/**
	 * @param agent
	 *            the agent to set
	 */
	public void setAgent(FileRenameAgent agent) {
		this.agent = agent;
	}
	
	
	/**
	 * @return the charRenameOperationEnable
	 */
	@Column(name = "CHARRENAMEENABLE", nullable = true, length = 1)
	@org.hibernate.annotations.Type(type = "yes_no")
	@XmlElement
	public boolean isCharRenameOperationEnable() {
		return charRenameOperationEnable;
	}
	
	/**
	 * @param charRenameOperationEnable
	 *            the charRenameOperationEnable to set
	 */
	public void setCharRenameOperationEnable(boolean charRenameOperationEnable) {
		this.charRenameOperationEnable = charRenameOperationEnable;
	}

}
