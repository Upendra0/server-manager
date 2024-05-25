package com.elitecore.sm.composer.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.composer.model.RAPComposerMapping;

public interface RapComposerService {

	public ResponseObject getRapComposerMappingById(int asn1ComposerMappingId);

	public ResponseObject updateRapComposerMapping(RAPComposerMapping rapComposer);

}
