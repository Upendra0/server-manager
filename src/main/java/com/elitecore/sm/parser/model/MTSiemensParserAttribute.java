package com.elitecore.sm.parser.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.DynamicUpdate;

@Entity
@DiscriminatorValue("MTSiemensParserAttribute")
@DynamicUpdate
public class MTSiemensParserAttribute extends ParserAttribute{

	
	
}
