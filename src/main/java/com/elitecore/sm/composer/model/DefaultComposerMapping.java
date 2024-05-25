/**
 * 
 */
package com.elitecore.sm.composer.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * @author vandana.awatramani
 *
 */
@Entity
@DiscriminatorValue("DefaultComposerMapping")
public class DefaultComposerMapping extends ComposerMapping {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7272049791604548063L;
	// this is dummy as no data transformation actually happens here.
}
