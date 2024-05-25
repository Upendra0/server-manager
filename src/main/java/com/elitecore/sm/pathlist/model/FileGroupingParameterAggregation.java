/**
 * 
 */
package com.elitecore.sm.pathlist.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@Entity()
@DiscriminatorValue("FileGroupParamAggregation")
@XmlType
public class FileGroupingParameterAggregation extends FileGroupingParameter implements Serializable {

	private static final long serialVersionUID = -40664784630686048L;
	private String archivePath;
	
	@Column(name = "ARCHIVEDIRPATH", nullable = true, length = 500)
	@XmlElement
	public String getArchivePath() {
		return archivePath;
	}
	
	public void setArchivePath(String archivePath) {
		this.archivePath = archivePath;
	}

}
