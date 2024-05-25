/**
 * 
 */
package com.elitecore.sm.composer.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.model.DataTypeEnum;
import com.elitecore.sm.common.model.TrimPositionEnum;

/**
 * @author vandana.awatramani
 *
 */
@Entity
@Component(value = "composerAttribute")
@Table(name = "TBLTCOMPOSERATTR")
@DynamicUpdate
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING,length=100)
@XmlSeeAlso({ASCIIComposerAttr.class,ASN1ComposerAttribute.class,FixedLengthASCIIComposerAttribute.class,XMLComposerAttribute.class,RoamingComposerAttribute.class})
public class ComposerAttribute extends BaseModel implements Comparable<ComposerAttribute>, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9071421575375743271L;
	private int id;
	private int sequenceNumber;
	private String destinationField;
	private String unifiedField;
	private String defualtValue;
	private String trimchars;
	private String trimPosition = TrimPositionEnum.BOTH.getValue();
	private String description;
	private DataTypeEnum dataType;
	private String dateFormat;
	private int attributeOrder=0;
	private ComposerMapping myComposer;
	private ComposerGroupAttribute composerGroupAttribute;
	private boolean associatedByGroup;

	@XmlElement
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="ComposerAttribute",allocationSize=1)
	/**
	 * represent the auto generated primary key
	 * @return Id 
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the sequenceNumber
	 */
	@XmlElement
	@Column(name = "SEQNUMBER", nullable = false, length = 3)
	public int getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @return the destinationField
	 */
	@XmlElement
	@Column(name = "DESTFIELD", nullable = true, length = 100)
	public String getDestinationField() {
		return destinationField;
	}

	/**
	 * @return the unifiedField
	 */
	@XmlElement
	@Column(name = "UNIFIEDFIELD", nullable = true, length = 100)
	public String getUnifiedField() {
		return unifiedField;
	}

	/**
	 * @return the defualtValue
	 */
	@XmlElement
	@Column(name = "DEFAULTVAL", nullable = true, length = 100)
	public String getDefualtValue() {
		return defualtValue;
	}

	/**
	 * @return the trimchars
	 */
	@XmlElement
	@Column(name = "TRIMCHARS", nullable = true, length = 100)
	public String getTrimchars() {
		return trimchars;
	}

	/**
	 * @return the dataType
	 */
	@XmlElement
	@Column(name = "DATATYPE", nullable = true, length = 100)
	@Enumerated(EnumType.STRING)
	public DataTypeEnum getDataType() {
		return dataType;
	}

	/**
	 * @return the dateFormat
	 */
	@XmlElement
	@Column(name = "DATEFORMAT", nullable = true, length = 100)
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * @return the myComposer
	 */
	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "COMPOSERMAPPINGID", nullable = true, foreignKey = @ForeignKey(name = "FK_COMPOSER_COMPATTR"))
	public ComposerMapping getMyComposer() {
		return myComposer;
	}

	@XmlElement
	@Column(name = "DESCRIPTION", nullable = true, length = 100)
	public String getDescription() {
		return description;
	}

	@Column(name = "APPLICATIONORDER", nullable = true)
	public Integer getAttributeOrder() {
		return attributeOrder;
	}
	
	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY, optional = true, cascade =CascadeType.ALL)
	@JoinColumn(name = "COMPOSERGROUPATTRID", nullable = true, foreignKey = @ForeignKey(name = "FK_COMPOSER_GROUP_ATTR"))
	public ComposerGroupAttribute getComposerGroupAttribute() {
		return composerGroupAttribute;
	}

	@Transient
	@XmlElement
	public boolean isAssociatedByGroup() {
		if(getComposerGroupAttribute() != null){
			this.associatedByGroup = true;
		}
		return associatedByGroup;
	}
	
	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param sequenceNumber
	 *            the sequenceNumber to set
	 */
	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @param destinationField
	 *            the destinationField to set
	 */
	public void setDestinationField(String destinationField) {
		this.destinationField = destinationField;
	}

	/**
	 * @param unifiedField
	 *            the unifiedField to set
	 */
	public void setUnifiedField(String unifiedField) {
		this.unifiedField = unifiedField;
	}

	/**
	 * @param defualtValue
	 *            the defualtValue to set
	 */
	public void setDefualtValue(String defualtValue) {
		this.defualtValue = defualtValue;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	
	
	/**
	 * @param trimchars
	 *            the trimchars to set
	 */
	public void setTrimchars(String trimchars) {
		this.trimchars = trimchars;
	}

	/**
	 * @param dataType
	 *            the dataType to set
	 */
	public void setDataType(DataTypeEnum dataType) {
		this.dataType = dataType;
	}

	/**
	 * @param dateFormat
	 *            the dateFormat to set
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	/**
	 * @param myComposer
	 *            the myComposer to set
	 */
	public void setMyComposer(ComposerMapping myComposer) {
		this.myComposer = myComposer;
	}
	

	public void setAttributeOrder(int attributeOrder) {
		this.attributeOrder = attributeOrder;
	}
	
	@XmlElement
	@Column(name = "TRIMPOSITION", nullable = true)	
	public String getTrimPosition() {
		return trimPosition;
	}

	public void setTrimPosition(String trimPosition) {
		this.trimPosition = trimPosition;
	}
	
	public void setComposerGroupAttribute(ComposerGroupAttribute composerGroupAttribute) {
		this.composerGroupAttribute = composerGroupAttribute;
	}
	
	public void setAssociatedByGroup(boolean associatedByGroup) {
		this.associatedByGroup = associatedByGroup;
	}
	
	@Override
	public int compareTo(ComposerAttribute composerAttribute) {//NOSONAR
		
		return this.attributeOrder < composerAttribute.attributeOrder ? -1 : this.attributeOrder > composerAttribute.attributeOrder ? 1 : 0;
	}

	@Override
    public Object clone() throws CloneNotSupportedException {
		Date date = new Date();
		ComposerAttribute composerAttribute = (ComposerAttribute) super.clone();
		composerAttribute.setId(0);
		composerAttribute.setCreatedDate(date);
		composerAttribute.setLastUpdatedDate(date);
        return composerAttribute;
    }	

}
