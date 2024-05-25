package com.elitecore.sm.services.model;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;

import com.elitecore.sm.common.model.BaseModel;

/**
 * @author vishal.lakhyani
 *
 */
@Entity()
@Table(name = "TBLTPARTITIONPARAM")
@DynamicUpdate
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="partitionParamCache")
public class PartitionParam extends BaseModel {
	
	private static final long serialVersionUID = -6295651591992807091L;
	
	private int id;
	private PartitionFieldEnum partitionField;
	private String unifiedField;
	private String partitionRange;
	private String baseUnifiedField; 
	private int netMask = -1; 
	
	private Service parsingService;
	
	public PartitionParam(){
		// default hibernate constructor	
	}
	/**
	 * @return the id
	 */
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
							     pkColumnName="TABLE_NAME",valueColumnName="VALUE",
							     pkColumnValue="PartitionParam",allocationSize=1)
	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return partitionField
	 */
	@XmlElement
	@Enumerated(EnumType.STRING)
	@Column(name = "PARTITIONFIELD" ,nullable=false)
	public PartitionFieldEnum getPartitionField() {
		return partitionField;
	}
	
	
	/**
	 * @param partitionField
	 */
	public void setPartitionField(PartitionFieldEnum partitionField) {
		this.partitionField = partitionField;
	}
	
	/**
	 * 
	 * @return unifiedField
	 */
	@XmlElement
	@Column(name = "UNIFIEDFIELD" ,nullable=false)
	public String getUnifiedField() {
		return unifiedField;
	}
	
	/**
	 * @param unifiedField
	 */
	public void setUnifiedField(String unifiedField) {
		this.unifiedField = unifiedField;
	}
	
	/**
	 * @return partitionRange
	 */
	@Column(name = "PARTITIONRANGE", nullable = false, length = 10)
	@XmlElement
	public String getPartitionRange() {
		return partitionRange;
	}


	/**
	 * @param partitionRange
	 */
	public void setPartitionRange(String partitionRange) {
		this.partitionRange = partitionRange;
	}


	/**
	 * @return the parsingService
	 */
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "SERVICEID", nullable = true, foreignKey = @ForeignKey(name = "FK_SERVICE_PARITION_PARAM"))
	@XmlTransient
	public Service getParsingService() {
		return parsingService;
	}

	/**
	 * @param parsingService
	 */
	public void setParsingService(Service parsingService) {
		this.parsingService = parsingService;
	}
	
	/**
	 * @return the baseUnifiedField
	 */
	@Column(name = "BASEUNIFIEDFIELD" , nullable = true)
	public String getBaseUnifiedField() {
		return baseUnifiedField;
	}
	
	/**
	 * @param baseUnifiedField the baseUnifiedField to set
	 */
	public void setBaseUnifiedField(String baseUnifiedField) {
		this.baseUnifiedField = baseUnifiedField;
	}
	
	/**
	 * @return the netMask
	 */
	@Column(name = "NETMASK" ,nullable = true)
	public int getNetMask() {
		return netMask;
	}
	
	/**
	 * @param netMask the netMask to set
	 */
	public void setNetMask(int netMask) {
		this.netMask = netMask;
	}

}