package com.elitecore.sm.composer.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.springframework.stereotype.Component;

import com.elitecore.sm.common.constants.EngineConstants;

@Component
@Entity
@DiscriminatorValue(EngineConstants.NRTRDE_COMPOSER_PLUGIN)
public class NRTRDEComposerAttribute extends RoamingComposerAttribute{

	private static final long serialVersionUID = -7515797564897948072L;
	
}
