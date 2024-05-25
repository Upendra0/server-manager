/**
 * 
 */
package com.elitecore.sm.parser.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.persistence.Cacheable;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.model.CharSetEnum;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.util.EliteUtils;

/**
 * @author jay.shah
 *
 */
@Component(value="parserMapping")
@Entity()
@Table(name = "TBLTPARSERMAPPING")
@DynamicUpdate
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 100)
@XmlSeeAlso({NATFlowParserMapping.class,NatflowASN1ParserMapping.class,AsciiParserMapping.class,ASN1ParserMapping.class,DetailLocalParserMapping.class,FixedLengthASCIIParserMapping.class,
	RegexParserMapping.class,SstpXmlParserMapping.class,XMLParserMapping.class,RapParserMapping.class, TapParserMapping.class,NRTRDEParserMapping.class,FixedLengthBinaryParserMapping.class,HtmlParserMapping.class,PDFParserMapping.class,XlsParserMapping.class,VarLengthAsciiParserMapping.class, VarLengthBinaryParserMapping.class,JsonParserMapping.class,MTSiemensParserMapping.class})
@XmlType(propOrder = { "id", "name", "parserType","device","dateFormat","srcDateFormat","srcCharSetName","mappingType","parserAttributes","groupAttributeList"})
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="parserMappingCache")
	
public class ParserMapping extends BaseModel implements Serializable, Cloneable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4542533627738497837L;
	private int id;
	private String name;
	private PluginTypeMaster parserType;
	private Device device;
	private String dateFormat = "MM-dd-yyyy HH:mm:ss";
	private String srcDateFormat ="MM-dd-yyyy HH:mm:ss";
	private CharSetEnum srcCharSetName=CharSetEnum.UTF8;
	private int mappingType=1;  // It can be either 0 or 1.   0 for system generated and 1 for user defined mapping
	private List<ParserAttribute> parserAttributes = new ArrayList<>(0);
	private List<ParserGroupAttribute> groupAttributeList = new ArrayList<>(0);
	private List<Parser> parserWrapper = new ArrayList<>(0);

	@XmlElement
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="ParserMapping",allocationSize=1)
	/**
	 * represent the auto generated primary key
	 * @return Id 
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	@XmlElement
	@Column(name = "NAME", nullable = false, length = 250, unique = false)
	public String getName() {
		return name;
	}
	
	/**
	 * @return the parserType
	 */
	@XmlElement
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PARSERTYPEID", nullable = false, foreignKey = @ForeignKey(name = "FK_Plugin_PluginTYPE"))
	public PluginTypeMaster getParserType() {
		return parserType;
	}

	
	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param parserType the parserType to set
	 */
	public void setParserType(PluginTypeMaster parserType) {
		this.parserType = parserType;
	}

	
	@XmlElement
	//@OneToMany(fetch = FetchType.LAZY)
	//@Cascade(value = org.hibernate.annotations.CascadeType.ALL) 
	//@JoinColumn(name = "PARSERMAPPINGID", nullable = true,foreignKey = @ForeignKey(name = "FK_PARSER_MAPPING_ID_PARSERATTRIBUTE"))
	@OneToMany(mappedBy = "parserMapping", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<ParserAttribute> getParserAttributes() {
		if(parserAttributes != null) {
			Collections.sort(parserAttributes);
		}
		return parserAttributes;
	}
	
	/**
	 * @param parserAttributes the parserAttributes to set
	 */
	public void setParserAttributes(List<ParserAttribute> parserAttributes) {
		this.parserAttributes = parserAttributes;
	}
	
	/**
	 * @return the groupAttributeList
	 */
	@XmlElement
	@OneToMany(mappedBy = "parserMapping", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<ParserGroupAttribute> getGroupAttributeList() {
		return groupAttributeList;
	}
	
	/**
	 * @param groupAttributeList
	 *            the groupAttributeList to set
	 */
	public void setGroupAttributeList(List<ParserGroupAttribute> groupAttributeList) {
		this.groupAttributeList = groupAttributeList;
	}
	
	/**
	 * @return the device
	 */
	@XmlElement
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "DEVICEID", nullable = true, foreignKey = @ForeignKey(name = "FK_DEVICE_ID_DEVICE"))
	public Device getDevice() {
		return device;
	}

	/**
	 * @param device the device to set
	 */
	public void setDevice(Device device) {
		this.device = device;
	}
	
	/**
	 * 
	 * @return sourceDateFormat
	 */
	@XmlElement
	@Column(name = "SRCDATAFORMAT", nullable = true, length = 100)
	public String getSrcDateFormat() {
		return srcDateFormat;
	}

	/**
	 * 
	 * @param sourceDateFormat
	 */
	public void setSrcDateFormat(String srcDateFormat) {
		this.srcDateFormat = srcDateFormat;
	}
	
	/**
	 * 
	 * @return srcCharSetName
	 */
	@XmlElement
	@Column(name = "SRCCHARSET", nullable = true, length = 100)
	@Enumerated  (EnumType.STRING)
	public CharSetEnum getSrcCharSetName() {
		return srcCharSetName;
	}

	/**
	 * 
	 * @param srcCharSetName
	 */
	public void setSrcCharSetName(CharSetEnum srcCharSetName) {
		this.srcCharSetName = srcCharSetName;
	}

	/**
	 * @return the parserWrapper
	 */
	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY,mappedBy="parserMapping")
	public List<Parser> getParserWrapper() {
		return parserWrapper;
	}

	/**
	 * @param parserWrapper the parserWrapper to set
	 */
	public void setParserWrapper(List<Parser> parserWrapper) {
		this.parserWrapper = parserWrapper;
	}

	/**
	 * @return the mappingType
	 */
	@Column(name = "MAPPINGTYPE", nullable = false, length = 1)
    @XmlElement
	public int getMappingType() {
		return mappingType;
	}

	/**
	 * @param mappingType the mappingType to set
	 */
	public void setMappingType(int mappingType) {
		this.mappingType = mappingType;
	}
	
	/**
	 * 
	 * @return SourceDateFormatEnum
	 */
	@XmlElement
	@Column(name = "DATEFORMAT", nullable = true, length = 100)
	public String getDateFormat() {
		return dateFormat;
	}

	/**
	 * 
	 * @param dateFormat
	 */
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}
	
	@Override
	public Object clone() throws CloneNotSupportedException{
		ParserMapping parserMapping = (ParserMapping) super.clone();
		Date date = new Date();
		parserMapping.setId(0);
		parserMapping.setCreatedDate(date);
		parserMapping.setLastUpdatedDate(date);
		parserMapping.setName(EliteUtils.checkForNames(BaseConstants.IMPORT, parserMapping.getName()));
		parserMapping.setParserWrapper(null);
		
		List<ParserAttribute> oldList = parserMapping.getParserAttributes();
		List<ParserAttribute> newList = new ArrayList<>();
		
		if(oldList != null && !oldList.isEmpty()) {
			int length = oldList.size();
			for(int i = length-1; i >= 0; i--) {
				ParserAttribute parserAttribute = (ParserAttribute) oldList.get(i).clone();
				parserAttribute.setParserMapping(parserMapping);
				parserAttribute.setParserGroupAttribute(null);
				newList.add(parserAttribute);
			}
		}
		
		parserMapping.setParserAttributes(newList);
		
		List<ParserGroupAttribute> oldGroupList = parserMapping.getGroupAttributeList();
		List<ParserGroupAttribute> newGroupList = new ArrayList<>();
		
		if(oldGroupList != null && !oldGroupList.isEmpty()) {
			int length = oldGroupList.size();
			for(int i = length-1; i >= 0; i--) {
				ParserGroupAttribute groupAttribute = (ParserGroupAttribute) oldGroupList.get(i).clone();
				groupAttribute.setParserMapping(parserMapping);
				groupAttribute.setBaseGroup(null);
				newGroupList.add(groupAttribute);
			}
		}
		
		parserMapping.setGroupAttributeList(newGroupList);
		
		return parserMapping;
	}
}
