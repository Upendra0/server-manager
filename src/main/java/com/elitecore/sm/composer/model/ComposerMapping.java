/**
 * 
 */
package com.elitecore.sm.composer.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.DynamicUpdate;

import com.elitecore.sm.common.constants.BaseConstants;
import com.elitecore.sm.common.model.BaseModel;
import com.elitecore.sm.common.model.CharSetEnum;
import com.elitecore.sm.device.model.Device;
import com.elitecore.sm.parser.model.PluginTypeMaster;
import com.elitecore.sm.util.EliteUtils;

/**
 * @author vandana.awatramani
 *
 */
@Entity()
@Table(name = "TBLTCOMPOSERMAPPING")
@DynamicUpdate
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING, length = 100)
@XmlSeeAlso({ASCIIComposerMapping.class,DefaultComposerMapping.class,ASN1ComposerMapping.class,DetailLocalComposerMapping.class,FixedLengthASCIIComposerMapping.class,XMLComposerMapping.class,RoamingComposerMapping.class})
public class ComposerMapping extends BaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1813967816865505681L;
	private int id;
	private String name;
	private PluginTypeMaster composerType;
	private Device device;
	private int mappingType;  // It can be either 0 or 1.   0 for system generated and 1 for user defined mapping
	private CharSetEnum destCharset = CharSetEnum.UTF8;
	private String dateFormatEnum = "MM-dd-yyyy HH:mm:ss";
	private String destDateFormat="MM-dd-yyyy HH:mm:ss";
	private String destFileExt;
	private List<ComposerAttribute> attributeList = new ArrayList<>(0);
	private List<ComposerGroupAttribute> groupAttributeList = new ArrayList<>(0);
	private List<Composer> composerWrapper = new ArrayList<>(0);

	/**
	 * represent the auto generated primary key
	 * 
	 * @return Id
	 */
	@XmlElement
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
	 							 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
	 							 pkColumnValue="ComposerMapping",allocationSize=1)
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	@XmlElement
	@Column(name = "NAME", nullable = false, length = 250, unique = true)
	public String getName() {
		return name;
	}

	/**
	 * @return the attributeList
	 */
	@XmlElement
	@OneToMany(fetch = FetchType.LAZY)
	@Cascade(value = org.hibernate.annotations.CascadeType.ALL) 
	@JoinColumn(name = "COMPOSERMAPPINGID", nullable = true,foreignKey = @ForeignKey(name = "FK_COMPOSER_MAPPING_ID_CMPOSERATTRIBUTE"))
	public List<ComposerAttribute> getAttributeList() {
		if(attributeList != null) {
			Collections.sort(attributeList);
		}
		return attributeList;
	}
	
	/**
	 * @return the groupAttributeList
	 */
	@XmlElement
	@OneToMany(mappedBy = "myComposer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	public List<ComposerGroupAttribute> getGroupAttributeList() {
		return groupAttributeList;
	}

	/**
	 * @return the composerType
	 */
	@XmlElement
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COMPOSERTYPEID", nullable = false, foreignKey = @ForeignKey(name = "FK_Composer_PluginTYPE"))
	public PluginTypeMaster getComposerType() {
		return composerType;
	}


	/**
	 * @return the device
	 */
	@XmlElement
	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade=CascadeType.ALL)
	@JoinColumn(name = "DEVICEID", nullable = true, foreignKey = @ForeignKey(name = "FK_DEVICE_ID_COMPOSER"))
	public Device getDevice() {
		return device;
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
	 * @return the composerWrapper
	 */
	@XmlTransient
	@OneToMany(fetch = FetchType.LAZY,mappedBy="composerMapping")
	public List<Composer> getComposerWrapper() {
		return composerWrapper;
	}
	
	/**
	 * @return the destCharset
	 */
	@Column(name = "DESTCHARSET", nullable = true, length = 100)
	@Enumerated(EnumType.STRING)
	@XmlElement
	public CharSetEnum getDestCharset() {
		return destCharset;
	}

	/**
	 * @return the destDateFormat
	 */
	@Column(name = "DESTDATEFORMAT", nullable = true, length = 50)
	@XmlElement
	public String getDestDateFormat() {
		return destDateFormat;
	}

	/**
	 * @return the destFileExt
	 */
	@Column(name = "DESTFILEEXT", nullable = true, length = 100)
	@XmlElement
	public String getDestFileExt() {
		return destFileExt;
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
	 * @param attributeList
	 *            the attributeList to set
	 */
	public void setAttributeList(List<ComposerAttribute> attributeList) {
		this.attributeList = attributeList;
	}
	
	/**
	 * @param groupAttributeList
	 *            the groupAttributeList to set
	 */
	public void setGroupAttributeList(List<ComposerGroupAttribute> groupAttributeList) {
		this.groupAttributeList = groupAttributeList;
	}

	/**
	 * @param composerType
	 *            the composerType to set
	 */
	public void setComposerType(PluginTypeMaster composerType) {
		this.composerType = composerType;
	}


	/**
	 * @param device the device to set
	 */
	public void setDevice(Device device) {
		this.device = device;
	}

	/**
	 * @param mappingType the mappingType to set
	 */
	public void setMappingType(int mappingType) {
		this.mappingType = mappingType;
	}

	/**
	 * @param composerWrapper the composerWrapper to set
	 */
	public void setComposerWrapper(List<Composer> composerWrapper) {
		this.composerWrapper = composerWrapper;
	}
	
	/**
	 * @param destCharset
	 *            the destCharset to set
	 */
	public void setDestCharset(CharSetEnum destCharset) {
		this.destCharset = destCharset;
	}

	/**
	 * @param destDateFormat
	 *            the destDateFormat to set
	 */
	public void setDestDateFormat(String destDateFormat) {
		this.destDateFormat = destDateFormat;
	}
	
	/**
	 * @param destFileExt
	 *            the destFileExt to set
	 */
	public void setDestFileExt(String destFileExt) {
		this.destFileExt = destFileExt;
	}
	
	/**
	 * 
	 * @return
	 */
	@Transient
	public String getDateFormatEnum() {
		return dateFormatEnum;
	}
	
	/**
	 * 
	 * @param dateFormatEnum
	 */
	public void setDateFormatEnum(String dateFormatEnum) {
		this.dateFormatEnum = dateFormatEnum;
	}

	@Override
    public Object clone() throws CloneNotSupportedException {
		ComposerMapping composerMapping = (ComposerMapping) super.clone();
		Date date = new Date();
		composerMapping.setId(0);
		composerMapping.setCreatedDate(date);
		composerMapping.setLastUpdatedDate(date);
		composerMapping.setName(EliteUtils.checkForNames(BaseConstants.IMPORT, composerMapping.getName()));
		composerMapping.setComposerWrapper(null);
		
		List<ComposerAttribute> oldList = composerMapping.getAttributeList();
		List<ComposerAttribute> newList = new ArrayList<>();
		
		if(oldList != null && !oldList.isEmpty()) {
			int length = oldList.size();
			for(int i = length-1; i >= 0; i--) {
				ComposerAttribute attribute = (ComposerAttribute) oldList.get(i).clone();
				attribute.setMyComposer(composerMapping);
				newList.add(attribute);
			}
		}
		
		composerMapping.setAttributeList(newList);
		
		List<ComposerGroupAttribute> oldGroupList = composerMapping.getGroupAttributeList();
		List<ComposerGroupAttribute> newGroupList = new ArrayList<>();
		
		if(oldGroupList != null && !oldGroupList.isEmpty()) {
			int length = oldGroupList.size();
			for(int i = length-1; i >= 0; i--) {
				ComposerGroupAttribute groupAttribute = (ComposerGroupAttribute) oldGroupList.get(i).clone();
				groupAttribute.setMyComposer(composerMapping);
				newGroupList.add(groupAttribute);
			}
		}
		
		composerMapping.setGroupAttributeList(newGroupList);
		
        return composerMapping;
    }

}