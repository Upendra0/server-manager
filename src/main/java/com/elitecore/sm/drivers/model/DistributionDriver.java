/**
 * 
 */
package com.elitecore.sm.drivers.model;

import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import com.elitecore.sm.common.model.SequenceManagement;

/**
 * @author vandana.awatramani
 *
 */
@Entity
@DiscriminatorValue("DD")
@XmlSeeAlso({FTPDistributionDriver.class,LocalDistributionDriver.class,DatabaseDistributionDriver.class,HadoopDistributionDriver.class})
@XmlType(propOrder = {"driverControlFileParams", "controlFileSeq"})
public class DistributionDriver extends Drivers {

	private static final long serialVersionUID = 9122862507365785185L;
	private SequenceManagement controlFileSeq;
	private ControlFileParams driverControlFileParams;
	
	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name= "SEQUENCEID", nullable = true, foreignKey = @ForeignKey(name= "FK_SEQUENCE_ID"))
	public SequenceManagement getControlFileSeq() {
		return controlFileSeq;
	}
	public void setControlFileSeq(SequenceManagement controlFileSeq) {
		this.controlFileSeq = controlFileSeq;
	}
	
	@Embedded
	@XmlElement
	public ControlFileParams getDriverControlFileParams() {
		return driverControlFileParams;
	}
	public void setDriverControlFileParams(ControlFileParams driverControlFileParams) {
		this.driverControlFileParams = driverControlFileParams;
	}
}
