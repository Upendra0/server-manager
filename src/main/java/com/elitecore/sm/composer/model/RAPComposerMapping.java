package com.elitecore.sm.composer.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.EngineConstants;

@Component
@Entity
@DiscriminatorValue(EngineConstants.RAP_COMPOSER_PLUGIN)
public class RAPComposerMapping extends RoamingComposerMapping{
	
	private static final long serialVersionUID = 5679585744726606092L;
	
}