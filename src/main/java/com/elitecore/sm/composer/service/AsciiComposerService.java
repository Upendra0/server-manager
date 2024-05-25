package com.elitecore.sm.composer.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.composer.model.ASCIIComposerMapping;

/**
 * 
 * @author avani.panchal
 *
 */
public interface AsciiComposerService {
	
	public ResponseObject getAsciiComposerMappingById(int asciiComposerMappingId);
	
	public ResponseObject updateAsciiComposerMapping(ASCIIComposerMapping asciiComposer);

}
