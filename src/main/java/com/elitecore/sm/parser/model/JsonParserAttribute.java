package com.elitecore.sm.parser.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.DynamicUpdate;


@Entity
@DiscriminatorValue("JsonParserAtrribute")
@DynamicUpdate
public class JsonParserAttribute extends ParserAttribute{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1330511788776491165L;
}
