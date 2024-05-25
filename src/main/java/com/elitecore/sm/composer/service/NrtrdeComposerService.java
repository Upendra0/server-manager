package com.elitecore.sm.composer.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.composer.model.NRTRDEComposerMapping;

public interface NrtrdeComposerService {

	public ResponseObject getNrtrdeComposerMappingById(int nrtrdeComposerMappingId);
	
	public ResponseObject updateNrtrdeComposerMapping(NRTRDEComposerMapping nrtrdeComposerMapping);
	
}
