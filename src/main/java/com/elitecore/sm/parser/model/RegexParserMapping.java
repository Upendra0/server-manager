/**
 * 
 */
package com.elitecore.sm.parser.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.xml.bind.annotation.XmlElement;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.EngineConstants;

/**
 * @author vandana.awatramani
 *
 */
@Component(value="RegexParserMapping")
@Entity
@DiscriminatorValue(EngineConstants.REGEX_PARSING_PLUGIN)
@DynamicUpdate
public class RegexParserMapping extends ParserMapping {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8536350219165147752L;
	private String logPatternRegex;
	private String logPatternRegexId;
	private String avilablelogPatternRegexId;

	private List<RegExPattern> patternList=new ArrayList<>(0);
	
	/**
	 * 
	 * @return logPatternRegex
	 */
	@XmlElement
	@Column(name = "LOGPATTERNREGEX", nullable = true, length = 500)
	public String getLogPatternRegex() {
		return logPatternRegex;
	}
	
	/**
	 * 
	 * @param logPatternRegex
	 */
	public void setLogPatternRegex(String logPatternRegex) {
		this.logPatternRegex = logPatternRegex;
	}
	
	/**
	 * 
	 * @return logPatternRegexId
	 */
	@XmlElement
	@Column(name = "LOGPATTERNREGEXID", nullable = true, length = 500)
	public String getLogPatternRegexId() {
		return logPatternRegexId;
	}
	
	/**
	 * 
	 * @param logPatternRegexId
	 */
	public void setLogPatternRegexId(String logPatternRegexId) {
		this.logPatternRegexId = logPatternRegexId;
	}
	
	/**
	 * 
	 * @return availablelogPatternRegexId
	 */
	@Column(name = "AVILOGPATTERNREGEXID", nullable = true, length = 500)
	public String getAvilablelogPatternRegexId() {
		return avilablelogPatternRegexId;
	}

	/**
	 * 
	 * @param logPatternRegexId
	 */
	public void setAvilablelogPatternRegexId(String avilablelogPatternRegex) {
		this.avilablelogPatternRegexId = avilablelogPatternRegex;
	}
	
	/**
	 * 
	 * @return patternList
	 */
	@XmlElement
	@OneToMany(mappedBy = "parserMapping",fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	public List<RegExPattern> getPatternList() {
		return patternList;
	}
	
	/**
	 * 
	 * @param patternList
	 */
	public void setPatternList(List<RegExPattern> patternList) {
		this.patternList = patternList;
	}
}
