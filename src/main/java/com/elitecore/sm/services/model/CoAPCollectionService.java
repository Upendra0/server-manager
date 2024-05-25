package com.elitecore.sm.services.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component(value="coAPCollectionService")
@Entity
@Table(name = "TBLTCOAPCOLLSVC")
@Scope(value = "prototype")
@DynamicUpdate
@XmlRootElement
public class CoAPCollectionService extends NetflowBinaryCollectionService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


}
