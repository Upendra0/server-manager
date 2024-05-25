/**
 * 
 */
package com.elitecore.sm.parser.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorType;
import javax.persistence.Entity;
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
import org.json.simple.JSONValue;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;

/**
 * @author elitecore
 *
 */

@Component(value = "parserAttribute")
@Scope(value = "prototype")
@Entity
@Table(name = "TBLTPARSERATTR")
@DynamicUpdate
@XmlSeeAlso({NatFlowParserAttribute.class,AsciiParserAttribute.class,ASN1ParserAttribute.class, XMLParserAttribute.class, FixedLengthASCIIParserAttribute.class,RAPParserAttribute.class,TAPParserAttribute.class,NRTRDEParserAttribute.class,FixedLengthBinaryParserAttribute.class,HtmlParserAttribute.class,PDFParserAttribute.class,XlsParserAttribute.class,VarLengthAsciiParserAttribute.class,VarLengthBinaryParserAttribute.class,JsonParserAttribute.class,MTSiemensParserAttribute.class})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING,length=100)
public class ParserAttribute extends BaseModel implements Comparable<ParserAttribute>, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2672101149097253798L;
	private int id;
	private String unifiedField;
	private String sourceField;
	private String defaultValue;
	private String trimChars;
	private String trimPosition;
	private ParserMapping parserMapping;
	private String description;
	private int attributeOrder;
	private String sourceFieldFormat;
	private ParserGroupAttribute parserGroupAttribute;
	private boolean associatedByGroup;
	private String dateFormat;
	
	public ParserAttribute() {
		super();
	}
	

	public ParserAttribute(int attributeOrder, String unifiedField, String sourceField,String defaultValue, String trimChars,String description , String trimPosition) {
		super();
		this.attributeOrder = attributeOrder;
		this.unifiedField = unifiedField;
		this.sourceField = sourceField;
		this.defaultValue = defaultValue;
		this.trimChars = trimChars;
		this.trimPosition = trimPosition;
		this.description= description;
	}
	
	public ParserAttribute(int attributeOrder, String unifiedField, String sourceField,String defaultValue, String trimChars,String description , String trimPosition,String dateFormat) {
		super();
		this.attributeOrder = attributeOrder;
		this.unifiedField = unifiedField;
		this.sourceField = sourceField;
		this.defaultValue = defaultValue;
		this.trimChars = trimChars;
		this.trimPosition = trimPosition;
		this.description= description;
		this.dateFormat=dateFormat;
	}

	
	
	@XmlElement
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="ParserAttribute",allocationSize=1)
	/**
	 * represent the auto generated primary key
	 * @return Id 
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the unifiedField
	 */
	@XmlElement
	@Column(name = "UNIFIEDFIELD", nullable = true, length = 100, unique = false)
	public String getUnifiedField() {
		return unifiedField;
	}

	/**
	 * @return the sourceField
	 */
	@XmlElement
	@Column(name = "SRCFIELD", nullable = true, length = 100, unique = false)
	public String getSourceField() {
		return sourceField;
	}

	/**
	 * @return the defaultValue
	 */
	@XmlElement
	@Column(name = "DEFAULTVAL", nullable = true, length = 100)
	public String getDefaultValue() {
		return defaultValue;
	}

	/**
	 * @return the trimChars
	 */
	@XmlElement
	@Column(name = "TRIMCHARS", nullable = true, length = 100)
	public String getTrimChars() {
		return trimChars;
	}

	@XmlElement
	@Column(name = "DESCRIPTION", nullable = true, length = 200)
	public String getDescription() {
		return description;
	}


	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param unifiedField
	 *            the unifiedField to set
	 */
	public void setUnifiedField(String unifiedField) {
		this.unifiedField = unifiedField;
	}

	/**
	 * @param sourceField
	 *            the sourceField to set
	 */
	public void setSourceField(String sourceField) {
		this.sourceField = sourceField;
	}

	/**
	 * @param defaultValue
	 *            the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	/**
	 * @param trimChars
	 *            the trimChars to set
	 */
	public void setTrimChars(String trimChars) {
		this.trimChars = trimChars;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the attributeOrder
	 */
	@XmlElement
	@Column(name = "APPLICATIONORDER", nullable = true)
	public int getAttributeOrder() {
		return attributeOrder;
	}

	/**
	 * @param attributeOrder the attributeOrder to set
	 */
	public void setAttributeOrder(int attributeOrder) {
		this.attributeOrder = attributeOrder;
	}

	/**
	 * @return the parserMapping
	 */
	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "PARSERMAPPINGID", nullable = true, foreignKey = @ForeignKey(name = "FK_PARSER_MAPPING_ID_PARSERATT"))
	public ParserMapping getParserMapping() {
		return parserMapping;
	}

	/**
	 * @param parserMapping the parserMapping to set
	 */
	public void setParserMapping(ParserMapping parserMapping) {
		this.parserMapping = parserMapping;
	}
	/**
	 * 
	 * @return the SourceFieldFormat
	 */
	@XmlElement
	@Column(name = "SOURCEFIELDFORMAT", nullable = true)
	public String getSourceFieldFormat() {
		return sourceFieldFormat;
	}

	/**
	 * 
	 * @param sourceFieldFormat
	 */
	public void setSourceFieldFormat(String sourceFieldFormat) {
		this.sourceFieldFormat = sourceFieldFormat;
	}

	@XmlElement
	@Column(name = "TRIMPOSITION", nullable = true)	
	public String getTrimPosition() {
		return trimPosition;
	}

	public void setTrimPosition(String trimPosition) {
		this.trimPosition = trimPosition;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException {
		ParserAttribute parserAttribute = (ParserAttribute) super.clone();
		Date date = new Date();
		parserAttribute.setId(0);
		parserAttribute.setCreatedDate(date);
		parserAttribute.setLastUpdatedDate(date);
		return parserAttribute;
	}
	@XmlTransient
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "PARSERGROUPATTRID", nullable = true, foreignKey = @ForeignKey(name = "FK_PARSER_GROUP_ATTR"))
	public ParserGroupAttribute getParserGroupAttribute() {
		return parserGroupAttribute;
	}

	public void setParserGroupAttribute(ParserGroupAttribute parserGroupAttribute) {
		this.parserGroupAttribute = parserGroupAttribute;
	}
	
	@Transient
	@XmlElement
	public boolean isAssociatedByGroup() {
		if(getParserGroupAttribute() != null){
			this.associatedByGroup = true;
		}
		return associatedByGroup;
	}

	public void setAssociatedByGroup(boolean associatedByGroup) {
		this.associatedByGroup = associatedByGroup;
	}

	@XmlElement
	@Column(name = "DATEFORMAT", nullable = true ,length = 100, unique = false)
	public String getDateFormat() {
		return dateFormat;
	}


	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	@Override
	public int compareTo(ParserAttribute parserAttribute) {//NOSONAR
		
		return this.attributeOrder < parserAttribute.attributeOrder ? -1 : this.attributeOrder > parserAttribute.attributeOrder ? 1 : 0;
	}
	
	@Override
	public String toString(){
		Map<String, Object> map = new HashMap<>();
		map.put("id", id);
		map.put("unifiedField", unifiedField);
		map.put("sourceField", sourceField);
		map.put("defaultValue", defaultValue);
		map.put("trimChars", trimChars);
		map.put("trimPosition", trimPosition);
		map.put("parserMapping", parserMapping);
		map.put("description", description);
		map.put("attributeOrder", attributeOrder);
		map.put("sourceFieldFormat", sourceFieldFormat);
		map.put("parserGroupAttribute", parserGroupAttribute);
		map.put("associatedByGroup", associatedByGroup);
		map.put("dateFormat", dateFormat);
		
		return JSONValue.toJSONString(map);
	}
}
