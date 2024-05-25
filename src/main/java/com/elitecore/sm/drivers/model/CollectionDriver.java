/**
 * 
 */
package com.elitecore.sm.drivers.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.pathlist.model.FileGroupingParameterCollection;

/**
 * @author vandana.awatramani
 *
 */
@Component(value="collectionDriver")
@Entity
@XmlSeeAlso({FTPCollectionDriver.class,SFTPCollectionDriver.class,LocalCollectionDriver.class})
@DynamicUpdate
@XmlType(propOrder = {"fileGroupingParameter"})
public class CollectionDriver extends Drivers implements Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8438946549577000863L;
	private FileGroupingParameterCollection fileGroupingParameter;

	

	/**
	 * @return the fileGroupingParameter
	 */
	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "FILEGRPID", nullable = true, foreignKey = @ForeignKey(name = "FK_DRV_FILEGRP"))
	@XmlElement
	public FileGroupingParameterCollection getFileGroupingParameter() {
		return fileGroupingParameter;
	}


	/**
	 * @param fileGroupingParameter
	 *            the fileGroupingParameter to set
	 */
	public void setFileGroupingParameter(
			FileGroupingParameterCollection fileGroupingParameter) {
		this.fileGroupingParameter = fileGroupingParameter;
	}

	@Override
    public CollectionDriver clone() throws CloneNotSupportedException {
        return (CollectionDriver)super.clone();
    }
}
