package com.elitecore.sm.composer.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.composer.model.FixedLengthASCIIComposerMapping;

/**
 * @author Sagar Ghetiya
 *
 */
public interface FixedLengthAsciiComposerService {
	
	public ResponseObject getFixedLengthAsciiComposerMappingById(int fixedLengthAsciiComposerMappingId);

	ResponseObject updateFixedLengthAsciiComposerMapping(FixedLengthASCIIComposerMapping fixedLengthASCIIComposerMapping);
	
}
