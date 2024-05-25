/**
 * 
 */
package com.elitecore.sm.services.model;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

/**
 * @author vandana.awatramani
 *
 */

@Component(value="radiusCollectionService")
@XmlRootElement
@Entity()
@Table(name = "TBLTRADIUSSERVICE")
@DynamicUpdate
@PrimaryKeyJoinColumn(name = "SERVICEID")

public class RadiusCollectionService extends NetflowBinaryCollectionService {
	
	private static final long serialVersionUID = 1L;
	
}
