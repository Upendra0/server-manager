/**
 * 
 */
package com.elitecore.sm.services.model;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

/**
 * @author vandana.awatramani
 * 
 */
@Entity()
@Table(name = "TBLTCOLLSVC")
@DynamicUpdate
@PrimaryKeyJoinColumn(name = "SERVICEID")
@XmlRootElement
@XmlSeeAlso({NetflowBinaryCollectionService.class,NetflowCollectionService.class,SysLogCollectionService.class,MqttCollectionService.class,CoAPCollectionService.class,Http2CollectionService.class,DiameterCollectionService.class,RadiusCollectionService.class})
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="schedulingParameterCache")
@XmlType(propOrder = { "serviceSchedulingParams"})
public class CollectionService extends Service {

	private static final long serialVersionUID = 1L;
	private ServiceSchedulingParams serviceSchedulingParams;

	/**
	 * @return the serviceSchedulingParams
	 */
	@XmlElement
	@OneToOne(fetch = FetchType.EAGER, optional = true,cascade=CascadeType.ALL)
	@JoinColumn(name = "SCHEDULEID", nullable = true, foreignKey = @ForeignKey(name = "FK_CSVC_Schedule"))
	public ServiceSchedulingParams getServiceSchedulingParams() {
		return serviceSchedulingParams;
	}

	/**
	 * @param serviceSchedulingParams
	 *            the serviceSchedulingParams to set
	 */
	public void setServiceSchedulingParams(
			ServiceSchedulingParams serviceSchedulingParams) {
		this.serviceSchedulingParams = serviceSchedulingParams;
	}
	
	

}
