package com.elitecore.sm.services.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * @author himanshu.dobaria
 *
 */
@Component(value="GTPPrimeCollectionService")
@XmlRootElement
@Entity
@Table(name = "TBLTGTPPRIMESERVICE")
@Scope(value = "prototype")
public class GTPPrimeCollectionService extends NetflowBinaryCollectionService {
	
	private static final long serialVersionUID = 1L;

}
