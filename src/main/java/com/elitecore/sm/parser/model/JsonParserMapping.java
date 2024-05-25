/**
 * 
 */
package com.elitecore.sm.parser.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.EngineConstants;

/**
 * @author sanket.bhalodia
 *
 */
@Component(value="jsonParser")
@Entity
@DiscriminatorValue(EngineConstants.JSON_PARSING_PLUGIN)
@DynamicUpdate
public class JsonParserMapping extends ParserMapping {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3181347410786606592L;

}
