package com.elitecore.sm.pathlist.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
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

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.util.EliteUtils;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

/**
 * @author nilesh.jadav
 */

@Entity()
@DynamicUpdate
@Table(name = "TBLTPATHLISTHISTORY")
@Component(value = "pathListHistory")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PathListHistory extends BaseModel implements Serializable {

	private static final long serialVersionUID = -8920286394902484874L;

	private int id;
	private String serverId;
	private String serviceId;
	private String driverId;
	private String pluginId;
	private String groupServerId = "DEFAULT";
	private String sourceFilePath;
	private String destinationFilePath;
	private String errorFilePath;
	private String duplicateFilePath;
	private String filterFilePath;
	private String invalidFilePath;
	private String aggrDestPath;
	private String nonAggrDestPath;
	private String invalidAggrDestPath;
	private String errAggrDestPath;
	private String remoteFileOperation;
	private String remotePathAfterColl;
	private String remoteFileNameAfterColl;
	private PathList pathList;
	private String pathId;
	private String networkElement;
	private String refNetworkElement;
	
	public PathListHistory() {
		// default no arg constructor
	}

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "UniqueIdGenerator")
	@TableGenerator(name = "UniqueIdGenerator", table = "TBLTPRIMARYKEY", pkColumnName = "TABLE_NAME", valueColumnName = "VALUE", pkColumnValue = "PathListHistory", allocationSize = 1)
	public int getId() {
		return id;
	}

	@Column(name = "SERVICEID", nullable = false, length = 100)
	public String getServiceId() {
		return serviceId;
	}

	@Column(name = "SERVERID", nullable = false, length = 100)
	public String getServerId() {
		return serverId;
	}

	@Column(name = "DRIVERID", length = 100)
	public String getDriverId() {
		return driverId;
	}

	@Column(name = "PLUGINID", length = 100)
	public String getPluginId() {
		return pluginId;
	}

	@Column(name = "SRCFILEPATH", length = 500)
	public String getSourceFilePath() {
		return sourceFilePath;
	}

	@Column(name = "ERRFILEPATH", length = 500)
	public String getErrorFilePath() {
		return errorFilePath;
	}

	@Column(name = "DUPFILEPATH", length = 500)
	public String getDuplicateFilePath() {
		return duplicateFilePath;
	}

	@Column(name = "FILFILEPATH", length = 500)
	public String getFilterFilePath() {
		return filterFilePath;
	}

	@Column(name = "INVFILEPATH", length = 500)
	public String getInvalidFilePath() {
		return invalidFilePath;
	}

	@Column(name = "AGGRDESTPATH", length = 500)
	public String getAggrDestPath() {
		return aggrDestPath;
	}

	@Column(name = "NONAGGRDESTPATH", length = 500)
	public String getNonAggrDestPath() {
		return nonAggrDestPath;
	}

	@Column(name = "INVALIDAGGRDESTPATH", length = 500)
	public String getInvalidAggrDestPath() {
		return invalidAggrDestPath;
	}

	@Column(name = "DESTFILEPATH", length = 500)
	public String getDestinationFilePath() {
		return destinationFilePath;
	}

	@Column(name = "REMOTEFILEOPERATION", length = 2)
	public String getRemoteFileOperation() {
		return remoteFileOperation;
	}

	@Column(name = "REMOTEPATHAFTERCOLL", length = 500)
	public String getRemotePathAfterColl() {
		return remotePathAfterColl;
	}

	@Column(name = "REMOTEFILENAMEAFTERCOLL", length = 500)
	public String getRemoteFileNameAfterColl() {
		return remoteFileNameAfterColl;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public void setPluginId(String pluginId) {
		this.pluginId = pluginId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public void setSourceFilePath(String sourceFilePath) {
		this.sourceFilePath = sourceFilePath;
	}

	public void setErrorFilePath(String errorFilePath) {
		this.errorFilePath = errorFilePath;
	}

	public void setDuplicateFilePath(String duplicateFilePath) {
		this.duplicateFilePath = duplicateFilePath;
	}

	public void setFilterFilePath(String filterFilePath) {
		this.filterFilePath = filterFilePath;
	}

	public void setInvalidFilePath(String invalidFilePath) {
		this.invalidFilePath = invalidFilePath;
	}

	public void setAggrDestPath(String aggrDestPath) {
		this.aggrDestPath = aggrDestPath;
	}

	public void setNonAggrDestPath(String nonAggrDestPath) {
		this.nonAggrDestPath = nonAggrDestPath;
	}

	public void setInvalidAggrDestPath(String invalidAggrDestPath) {
		this.invalidAggrDestPath = invalidAggrDestPath;
	}

	public void setDestinationFilePath(String destinationFilePath) {
		this.destinationFilePath = destinationFilePath;
	}

	public void setRemoteFileOperation(String remoteFileOperation) {
		this.remoteFileOperation = remoteFileOperation;
	}

	public void setRemotePathAfterColl(String remotePathAfterColl) {
		this.remotePathAfterColl = remotePathAfterColl;
	}

	public void setRemoteFileNameAfterColl(String remoteFileNameAfterColl) {
		this.remoteFileNameAfterColl = remoteFileNameAfterColl;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PATHLISTID", nullable = true, foreignKey = @ForeignKey(name = "FK_PATHLIST_HISTORY_ID"))
	public PathList getPathList() {
		return pathList;
	}

	public void setPathList(PathList pathList) {
		this.pathList = pathList;
	}
	
	@Column(name = "PATHID", nullable = false, length = 100)
	public String getPathId() {
		return pathId;
	}
	
	public void setPathId(String pathId) {
		this.pathId = pathId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.getSourceFilePath(), this.getDestinationFilePath(), this.getErrorFilePath(), this.getDuplicateFilePath(), this.getFilterFilePath(), this.getInvalidFilePath(), this.getAggrDestPath(), this.getNonAggrDestPath(), this.getInvalidAggrDestPath(), this.getRemoteFileOperation(), this.getRemotePathAfterColl(), this.getRemoteFileNameAfterColl());
	}
	
	@Column(name = "NETWORKELEMENT", nullable = true, length = 400)
	@XmlElement
	public String getNetworkElement() {
		return networkElement;
	}
	public void setNetworkElement(String networkElement) {
		this.networkElement = networkElement;
	}
	
	@Column(name = "REFNETWORKELEMENT", nullable = true, length = 400)
	@XmlElement
	public String getRefNetworkElement() {
		return refNetworkElement;
	}

	public void setRefNetworkElement(String refNetworkElement) {
		this.refNetworkElement = refNetworkElement;
	}

	@Column(name = "ERRAGGRDESTPATH", nullable = true, length = 400)
	@XmlElement
	public String getErrAggrDestPath() {
		return errAggrDestPath;
	}

	public void setErrAggrDestPath(String errAggrDestPath) {
		this.errAggrDestPath = errAggrDestPath;
	}
	
	@Column(name = "GROUPSERVERID", nullable = true)
	@XmlElement
	public String getGroupServerId() {
		return groupServerId;
	}

	
	public void setGroupServerId(String groupServerId) {
		this.groupServerId = groupServerId;
	}

	@Override
	public boolean equals(Object object) {
		if (object == null) {
			return false;
		}
		if (!(object instanceof PathListHistory)) {
			return false;
		}

		PathListHistory history = (PathListHistory) object;

		if (!EliteUtils.compare(this.getSourceFilePath(), history.getSourceFilePath())) {
			return false;
		}
		if (!EliteUtils.compare(this.getDestinationFilePath(), history.getDestinationFilePath())) {
			return false;
		}
		if (!EliteUtils.compare(this.getErrorFilePath(), history.getErrorFilePath())) {
			return false;
		}
		if (!EliteUtils.compare(this.getDuplicateFilePath(), history.getDuplicateFilePath())) {
			return false;
		}
		if (!EliteUtils.compare(this.getFilterFilePath(), history.getFilterFilePath())) {
			return false;
		}
		if (!EliteUtils.compare(this.getInvalidFilePath(), history.getInvalidFilePath())) {
			return false;
		}
		if (!EliteUtils.compare(this.getAggrDestPath(), history.getAggrDestPath())) {
			return false;
		}
		if (!EliteUtils.compare(this.getNonAggrDestPath(), history.getNonAggrDestPath())) {
			return false;
		}
		if (!EliteUtils.compare(this.getInvalidAggrDestPath(), history.getInvalidAggrDestPath())) {
			return false;
		}
		if (!EliteUtils.compare(this.getRemoteFileOperation(), history.getRemoteFileOperation())) {
			return false;
		}
		if (!EliteUtils.compare(this.getRemotePathAfterColl(), history.getRemotePathAfterColl())) {
			return false;
		}
		if (!EliteUtils.compare(this.getRemoteFileNameAfterColl(), history.getRemoteFileNameAfterColl())) {
			return false;
		}
		if (!EliteUtils.compare(this.getNetworkElement(), history.getNetworkElement())) {
			return false;
		}
		if (!EliteUtils.compare(this.getErrAggrDestPath(), history.getErrAggrDestPath())) {
			return false;
		}
		return true;
	}

}
