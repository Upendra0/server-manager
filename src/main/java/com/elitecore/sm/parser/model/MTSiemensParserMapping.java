/**
 * 
 */
package com.elitecore.sm.parser.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.EngineConstants;

@Component(value = "MTSiemensBinaryParserMapping")
@Entity
@DiscriminatorValue(EngineConstants.MTSIEMENS_BINARY_PARSING_PLUGIN)
@XmlRootElement
public class MTSiemensParserMapping extends ParserMapping {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8390318303085416992L;
}
