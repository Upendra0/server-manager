/**
 * 
 */
package com.elitecore.sm.composer.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

/**
 * @author Mitul.Vora
 *
 */
@Entity
@Component(value="xmlComposerAttribute")
@DiscriminatorValue("XMLComposer")
@DynamicUpdate
public class XMLComposerAttribute extends ComposerAttribute {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5566307153976310949L;

	/**
	 * 
	 */
	
}
