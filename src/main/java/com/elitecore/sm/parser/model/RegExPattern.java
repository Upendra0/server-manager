package com.elitecore.sm.parser.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.DynamicUpdate;

import com.elitecore.sm.common.model.BaseModel;

@Entity(name="RegExPattern")
@Table(name="TBLTREGEXPATTERN")
@DynamicUpdate
public class RegExPattern extends BaseModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1456816322079263279L;
	private int id;
	private String patternRegExName;
	private String patternRegExId;
	private String patternRegEx;
	private List<RegexParserAttribute> attributeList=new ArrayList<>(0);
	private RegexParserMapping parserMapping;

	/**
	 * 
	 * @return the id
	 */
	@XmlElement
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.TABLE,generator="UniqueIdGenerator")
	@TableGenerator(name="UniqueIdGenerator",table="TBLTPRIMARYKEY",
								 pkColumnName="TABLE_NAME",valueColumnName="VALUE",
								 pkColumnValue="RegExPattern",allocationSize=1)
	public int getId() {
		return id;
	}
	
	/**
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return patternRegExName
	 */
	@XmlElement
	@Column(name = "NAME", nullable = false, length = 250, unique = true)
	public String getPatternRegExName() {
		return patternRegExName;
	}
	
	/**
	 * 
	 * @param patternRegExName
	 */
	public void setPatternRegExName(String patternRegExName) {
		this.patternRegExName = patternRegExName;
	}
	
	/**
	 * 
	 * @return patternRegExId
	 */
	@XmlElement
	@Column(name = "PATTERNREGEXID", nullable = false, length = 100)
	public String getPatternRegExId() {
		return patternRegExId;
	}
	
	/**
	 * 
	 * @param patternRegExId
	 */
	public void setPatternRegExId(String patternRegExId) {
		this.patternRegExId = patternRegExId;
	}
	
	/**
	 * 
	 * @return patternRegEx
	 */
	@XmlElement
	@Column(name = "PATTERNREGEX", nullable = false, length = 500)
	public String getPatternRegEx() {
		return patternRegEx;
	}
	
	/**
	 * 
	 * @param patternRegEx
	 */
	public void setPatternRegEx(String patternRegEx) {
		this.patternRegEx = patternRegEx;
	}
	
	/**
	 * 
	 * @return attributeList
	 */
	@XmlElement
	@OneToMany(mappedBy="pattern",cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	public List<RegexParserAttribute> getAttributeList() {
		return attributeList;
	}
	
	/**
	 * 
	 * @param attributeList
	 */
	public void setAttributeList(List<RegexParserAttribute> attributeList) {
		this.attributeList = attributeList;
	}
	
	/**
	 * 
	 * @return parserMapping
	 */
	@XmlTransient
	@ManyToOne(fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	@JoinColumn(name="PARSERMAPPINGID", foreignKey = @ForeignKey(name = "FK_PATTERN_PARSER_ID"))
	public RegexParserMapping getParserMapping() {
		return parserMapping;
	}

	/**
	 * 
	 * @param parserMapping
	 */
	public void setParserMapping(RegexParserMapping parserMapping) {
		this.parserMapping = parserMapping;
	}
}
