/**
 * 
 */
package com.elitecore.sm.composer.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.EngineConstants;

/**
 * @author Mitul.Vora
 *
 */

@Component(value="xmlComposer")
@Entity
@DiscriminatorValue(EngineConstants.XML_COMPOSER_PLUGIN)
@DynamicUpdate
public class XMLComposerMapping extends ComposerMapping {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8035636830105612815L;
	
	/**
	 * 
	 */

}
