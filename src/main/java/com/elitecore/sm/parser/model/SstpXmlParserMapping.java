/**
 * 
 */
package com.elitecore.sm.parser.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.EngineConstants;

/**
 * @author vandana.awatramani
 *
 */

@Component(value="sstpXmlParser")
@Entity
@DiscriminatorValue(EngineConstants.SSTP_XML_PARSING_PLUGIN)
public class SstpXmlParserMapping extends ParserMapping {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7958173892788414019L;

	/**
	 * 
	 */
	public SstpXmlParserMapping() {
		// default constructor for hibernate
	}

}
