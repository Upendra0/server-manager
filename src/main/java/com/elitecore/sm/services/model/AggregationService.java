/**
 * 
 */
package com.elitecore.sm.services.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.aggregationservice.model.AggregationDefinition;
import com.elitecore.sm.pathlist.model.FileGroupingParameterAggregation;


/**
 * @author vandana.awatramani
 */

@Component(value="aggregationService")
@XmlRootElement
@Entity()
@Table(name = "TBLTAGGREGATIONSERVICE")
@DynamicUpdate
@PrimaryKeyJoinColumn(name = "SERVICEID")
@XmlType(propOrder = { "minFileRange", "maxFileRange","noFileAlert","errorPath","delimiter","fileGroupingParameter","serviceSchedulingParams","aggregationDefinition"})
public class AggregationService extends Service {

	private static final long serialVersionUID = 1L;
	private int minFileRange = 10;
	private int maxFileRange = 300;
	private int noFileAlert = 10;
	private String errorPath;
	private String delimiter;
	private FileGroupingParameterAggregation fileGroupingParameter;
	private ServiceSchedulingParams serviceSchedulingParams;
	private AggregationDefinition aggregationDefinition;
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
	
	/**
	 * @return the aggregationFileGroupParams
	 */
	
	@OneToOne(cascade = CascadeType.ALL,fetch=FetchType.EAGER)
	@JoinColumn(name = "FILEGRPID", nullable = true, foreignKey = @ForeignKey(name = "FK_AGG_FILEGRP"))
	public FileGroupingParameterAggregation getFileGroupingParameter() {
		return fileGroupingParameter;
	}
	
	/**
	 * @param aggregationFileGroupParams
	 *            the aggregationFileGroupParams to set
	 */
	
	public void setFileGroupingParameter(
			FileGroupingParameterAggregation fileGroupingParameter) {
		this.fileGroupingParameter = fileGroupingParameter;
	}
	/**
	 * @return the minFileRange
	 */
	@XmlElement
	@Column(name = "MINFILERANGE", nullable = true)
	public int getMinFileRange() {
		return minFileRange;
	}

	/**
	 * @param minFileRange the minFileRange to set
	 */
	public void setMinFileRange(int minFileRange) {
		this.minFileRange = minFileRange;
	}
	

	/**
	 * @return the maxFileRange
	 */
	@XmlElement
	@Column(name = "MAXFILERANGE", nullable = true)
	public int getMaxFileRange() {
		return maxFileRange;
	}

	/**
	 * @param maxFileRange the maxFileRange to set
	 */
	public void setMaxFileRange(int maxFileRange) {
		this.maxFileRange = maxFileRange;
	}
	/**
	 * @return the noFileAlert
	 */
	@XmlElement
	@Column(name = "NOFILEALERT", nullable = true, length = 3)
	public int getNoFileAlert() {
		return noFileAlert;
	}
	
	/**
	 * @param noFileAlert
	 *            the noFileAlert to set
	 */
	public void setNoFileAlert(int noFileAlert) {
		this.noFileAlert = noFileAlert;
	}
	
	@XmlElement
	@Column(name = "ERRORPATH", nullable = true)
	public String getErrorPath() {
		return errorPath;
	}

	/**
	 * @param maxFileRange the maxFileRange to set
	 */
	public void setErrorPath(String errorPath) {
		this.errorPath = errorPath;
	}
	
	@XmlElement
	@OneToOne(fetch = FetchType.LAZY,optional = true,cascade=CascadeType.ALL)
	@JoinColumn(name = "AGGDEFID", nullable = true, foreignKey = @ForeignKey(name = "FK_TBLTAGGDEFINITION"))
	public AggregationDefinition getAggregationDefinition() {
		return aggregationDefinition;
	}

	public void setAggregationDefinition(AggregationDefinition aggregationDefinition) {
		this.aggregationDefinition = aggregationDefinition;
	}
	
	@XmlElement
	@Column(name = "DELIMITER", nullable = true)
	/**
	 * @return the delimiter
	 */
	public String getDelimiter() {
		return delimiter;
	}

	/**
	 * @param delimiter the delimiter to set
	 */
	public void setDelimiter(String delimiter) {
		this.delimiter = delimiter;
	}
}
