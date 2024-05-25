package com.elitecore.sm.pathlist.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.stereotype.Component;

import com.elitecore.sm.consolidationservice.model.DataConsolidationMapping;
import com.elitecore.sm.device.model.Device;

@Entity()
@DynamicUpdate
@DiscriminatorValue("DCPL")
@Component(value="dataconsolidationPathList")
public class DataConsolidationPathList extends CommonPathList {

	private static final long serialVersionUID = -6126790950297804767L;

	// PathList Parameters
	private int maxCounterLimit = 10;

	// OneToMany Relation with PathList & MappingList
	private List<DataConsolidationMapping> conMappingList = new ArrayList<>();
	private Device parentDevice;
	private String referenceDevice;
	
	@XmlElement
	@Column(name = "MAXCOUNTERLIMIT", nullable = true, length = 10)
	public int getMaxCounterLimit() {
		return maxCounterLimit;
	}

	public void setMaxCounterLimit(int maxCounterLimit) {
		this.maxCounterLimit = maxCounterLimit;
	}

	@XmlElement
	@OneToMany(mappedBy = "dataConsPathList", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@Fetch(value = FetchMode.SUBSELECT)
	public List<DataConsolidationMapping> getConMappingList() {
		return conMappingList;
	}

	public void setConMappingList(List<DataConsolidationMapping> conMappingList) {
		this.conMappingList = conMappingList;
	}
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "PARENTDEVICE", nullable = true, foreignKey = @ForeignKey(name = "FK_PARENTDEVICE_ID"))
	@XmlElement
	@DiffIgnore
	public Device getParentDevice() {
		return parentDevice;
	}
	public void setParentDevice(Device parentDevice) {
		this.parentDevice = parentDevice;
	}
	
	@Column(name = "REFERENCEDEVICE", nullable = true, length = 400)
	@XmlTransient
	public String getReferenceDevice() {
		return referenceDevice;
	}
	public void setReferenceDevice(String referenceDevice) {
		this.referenceDevice = referenceDevice;
	}
}
