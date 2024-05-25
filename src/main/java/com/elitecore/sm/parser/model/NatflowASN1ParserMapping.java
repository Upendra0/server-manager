/**
 * 
 */
package com.elitecore.sm.parser.model;

import javax.persistence.Cacheable;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.elitecore.sm.common.constants.EngineConstants;

/**
 * @author vandana.awatramani
 *
 */

@Entity
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE, region="parserMappingCache")
@DiscriminatorValue(EngineConstants.NATFLOW_ASN_PARSING_PLUGIN)
public class NatflowASN1ParserMapping extends NATFlowParserMapping {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8376079559709839792L;
	// TODO detail out the params
}
