/**
 * 
 */
package com.elitecore.sm.composer.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.EngineConstants;

/**
 * @author vandana.awatramani
 *
 */
@Entity
@Component(value="fixedLengthAsciiComposerMapping")
@DiscriminatorValue(EngineConstants.FIXED_LENGTH_ASCII_COMPOSER_PLUGIN)
public class FixedLengthASCIIComposerMapping extends ASCIIComposerMapping {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8094921083634857151L;
	
}
