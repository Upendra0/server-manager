/**
 * 
 */
package com.elitecore.sm.services.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author vandana.awatramani
 *
 */
@Component(value="syslogCollectionService")
@Entity
@Table(name = "TBLTSYSLOGCOLLSVC")
@Scope(value = "prototype")
@DynamicUpdate
@XmlRootElement
public class SysLogCollectionService extends NetflowBinaryCollectionService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
