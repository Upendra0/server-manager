package com.elitecore.sm.composer.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.composer.model.TAPComposerMapping;

public interface TapComposerService {

	public ResponseObject getTapComposerMappingById(int tapComposerMappingId);
	
	public ResponseObject updateTapComposerMapping(TAPComposerMapping tapComposerMapping);
	
}
