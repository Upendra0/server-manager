package com.elitecore.sm.parser.model;

import javax.persistence.Entity;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.stereotype.Component;

@Component(value="XMLParserAtrribute")
@Entity
@DynamicUpdate
public class XMLParserAttribute extends ParserAttribute{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1330511788776491165L;
}
