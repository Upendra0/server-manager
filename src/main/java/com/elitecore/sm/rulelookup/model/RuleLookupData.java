package com.elitecore.sm.rulelookup.model;


import java.io.Serializable;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicUpdate;
import org.javers.core.metamodel.annotation.DiffIgnore;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.model.BaseModel;
/**
 * 
 * @author Sagar Ghetiya
 *
 */

@Component(value = "ruleLookUpData")
@Scope(value = "prototype")
@Entity()
@Table(name = "TBLTRULELOOKUPDATA")
@DynamicUpdate	
@XmlRootElement
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="ruleLookupTableCache")
public class RuleLookupData  extends BaseModel implements Serializable {

	
	private static final long serialVersionUID = 1L;
	private int id;
	private RuleLookupTableData ruleLookUpTable;
	private String strParam1;
	private String strParam2;
	private String strParam3;
	private String strParam4;
	private String strParam5;
	private String strParam6;
	private String strParam7;
	private String strParam8;
	private String strParam9;
	private String strParam10;
	private String strParam11;
	private String strParam12;
	private String strParam13;
	private String strParam14;
	private String strParam15;
	private String strParam16;
	private String strParam17;
	private String strParam18;
	private String strParam19;
	private String strParam20;
	private String key1;
	private String key2;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="RuleLookUpData",allocationSize=1)
	@Column(name="ID")
	@XmlElement
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "LOOKUPTABLEID",nullable = false,foreignKey = @ForeignKey(name = "FK_LOOKUPDATA_LOOKUPTABLE"))
	@XmlTransient
	@DiffIgnore
	public RuleLookupTableData getRuleLookUpTable() {
		return ruleLookUpTable;
	}
	public void setRuleLookUpTable(RuleLookupTableData ruleLookUpTable) {
		this.ruleLookUpTable = ruleLookUpTable;
	}
	
	
	@Column(name = "STRPARAM1", nullable = true, length = 500)
	@XmlElement
	public String getStrParam1() {
		return strParam1;
	}
	public void setStrParam1(String strParam1) {
		this.strParam1 = strParam1;
	}
	
	
	@Column(name = "STRPARAM2", nullable = true, length = 500)
	@XmlElement
	public String getStrParam2() {
		return strParam2;
	}
	public void setStrParam2(String strParam2) {
		this.strParam2 = strParam2;
	}
	
	
	@Column(name = "STRPARAM3", nullable = true, length = 500)
	@XmlElement
	public String getStrParam3() {
		return strParam3;
	}
	public void setStrParam3(String strParam3) {
		this.strParam3 = strParam3;
	}
	
	
	@Column(name = "STRPARAM4", nullable = true, length = 500)
	@XmlElement
	public String getStrParam4() {
		return strParam4;
	}
	public void setStrParam4(String strParam4) {
		this.strParam4 = strParam4;
	}
	
	
	@Column(name = "STRPARAM5", nullable = true, length = 500)
	@XmlElement
	public String getStrParam5() {
		return strParam5;
	}
	public void setStrParam5(String strParam5) {
		this.strParam5 = strParam5;
	}
	
	
	@Column(name = "STRPARAM6", nullable = true, length = 500)
	@XmlElement
	public String getStrParam6() {
		return strParam6;
	}
	public void setStrParam6(String strParam6) {
		this.strParam6 = strParam6;
	}
	
	
	@Column(name = "STRPARAM7", nullable = true, length = 500)
	@XmlElement
	public String getStrParam7() {
		return strParam7;
	}
	public void setStrParam7(String strParam7) {
		this.strParam7 = strParam7;
	}
	
	
	@Column(name = "STRPARAM8", nullable = true, length = 500)
	@XmlElement
	public String getStrParam8() {
		return strParam8;
	}
	public void setStrParam8(String strParam8) {
		this.strParam8 = strParam8;
	}
	
	
	@Column(name = "STRPARAM9", nullable = true, length = 500)
	@XmlElement
	public String getStrParam9() {
		return strParam9;
	}
	public void setStrParam9(String strParam9) {
		this.strParam9 = strParam9;
	}
	
	
	@Column(name = "STRPARAM10", nullable = true, length = 500)
	@XmlElement
	public String getStrParam10() {
		return strParam10;
	}
	public void setStrParam10(String strParam10) {
		this.strParam10 = strParam10;
	}
	
	
	@Column(name = "STRPARAM11", nullable = true, length = 500)
	@XmlElement
	public String getStrParam11() {
		return strParam11;
	}
	public void setStrParam11(String strParam11) {
		this.strParam11 = strParam11;
	}
	
	
	@Column(name = "STRPARAM12", nullable = true, length = 500)
	@XmlElement
	public String getStrParam12() {
		return strParam12;
	}
	public void setStrParam12(String strParam12) {
		this.strParam12 = strParam12;
	}
	
	
	@Column(name = "STRPARAM13", nullable = true, length = 500)
	@XmlElement
	public String getStrParam13() {
		return strParam13;
	}
	public void setStrParam13(String strParam13) {
		this.strParam13 = strParam13;
	}
	
	
	@Column(name = "STRPARAM14", nullable = true, length = 500)
	@XmlElement
	public String getStrParam14() {
		return strParam14;
	}
	public void setStrParam14(String strParam14) {
		this.strParam14 = strParam14;
	}
	
	
	@Column(name = "STRPARAM15", nullable = true, length = 500)
	@XmlElement
	public String getStrParam15() {
		return strParam15;
	}
	public void setStrParam15(String strParam15) {
		this.strParam15 = strParam15;
	}
	
	
	@Column(name = "STRPARAM16", nullable = true, length = 500)
	@XmlElement
	public String getStrParam16() {
		return strParam16;
	}
	public void setStrParam16(String strParam16) {
		this.strParam16 = strParam16;
	}
	
	
	@Column(name = "STRPARAM17", nullable = true, length = 500)
	@XmlElement
	public String getStrParam17() {
		return strParam17;
	}
	public void setStrParam17(String strParam17) {
		this.strParam17 = strParam17;
	}
	
	
	@Column(name = "STRPARAM18", nullable = true, length = 500)
	@XmlElement
	public String getStrParam18() {
		return strParam18;
	}
	public void setStrParam18(String strParam18) {
		this.strParam18 = strParam18;
	}
	
	
	@Column(name = "STRPARAM19", nullable = true, length = 500)
	@XmlElement
	public String getStrParam19() {
		return strParam19;
	}
	public void setStrParam19(String strParam19) {
		this.strParam19 = strParam19;
	}
	
	
	@Column(name = "STRPARAM20", nullable = true, length = 500)
	@XmlElement
	public String getStrParam20() {
		return strParam20;
	}
	public void setStrParam20(String strParam20) {
		this.strParam20 = strParam20;
	}	
	
	@Column(name = "KEY1", nullable = true, length = 500)
	@XmlElement
	public String getKey1() {
		return key1;
	}
	public void setKey1(String key1) {
		this.key1 = key1;
	}
	
	@Column(name = "KEY2", nullable = true, length = 500)
	@XmlElement
	public String getKey2() {
		return key2;
	}
	public void setKey2(String key2) {
		this.key2 = key2;
	}

}
