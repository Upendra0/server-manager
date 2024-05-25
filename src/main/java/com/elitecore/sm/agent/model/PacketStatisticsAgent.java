/**
 * 
 */
package com.elitecore.sm.agent.model;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

/**
 * @author vandana.awatramani
 *
 */
@Component(value="packetStatisticsAgent")
@Entity
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="agentCache")
public class PacketStatisticsAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 11922931462917283L;
	private String storageLocation;
	private List<ServicePacketStatsConfig> serviceList;
	
	/**
	 * @return the storageLocation
	 */
	@Column(name = "STORAGELOCATION", nullable = true)
	@XmlElement
	public String getStorageLocation() {
		return storageLocation;
	}

	/**
	 * @return the serviceList
	 */
	@OneToMany(mappedBy = "agent")
	@Cascade (value={org.hibernate.annotations.CascadeType.ALL})
	@XmlElement	
	public List<ServicePacketStatsConfig> getServiceList() {
		return serviceList;
	}

	/**
	 * @param storageLocation the storageLocation to set
	 */
	public void setStorageLocation(String storageLocation) {
		this.storageLocation = storageLocation;
	}


	/**
	 * @param serviceList
	 *            the serviceList to set
	 */
	public void setServiceList(List<ServicePacketStatsConfig> serviceList) {
		this.serviceList = serviceList;
	}

}
