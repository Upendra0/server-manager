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
@Component(value="correlationService")
@XmlRootElement
@Entity()
@Table(name = "TBLTCORRSERVICE")
@DynamicUpdate
@PrimaryKeyJoinColumn(name = "SERVICEID")
public class CorrelationService extends Service {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
