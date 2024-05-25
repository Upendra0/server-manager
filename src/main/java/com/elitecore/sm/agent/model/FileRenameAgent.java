/**
 * 
 */
package com.elitecore.sm.agent.model;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

/**
 * @author vandana.awatramani
 *
 */
@Component(value="fileRenameAgent")
@Entity
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="agentCache")
@XmlType(propOrder = {"serviceList"})
public class FileRenameAgent extends Agent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8688755014173714472L;
	private List<ServiceFileRenameConfig> serviceList;
	/**
	 * @return the serviceList
	 */
	@OneToMany(mappedBy = "agent")
	@Cascade (value={org.hibernate.annotations.CascadeType.ALL})
	@XmlElement	
	public List<ServiceFileRenameConfig> getServiceList() {
		return serviceList;
	}
	/**
	 * @param serviceList the serviceList to set
	 */
	public void setServiceList(List<ServiceFileRenameConfig> serviceList) {
		this.serviceList = serviceList;
	}
	
	

}
