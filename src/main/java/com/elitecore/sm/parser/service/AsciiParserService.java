package com.elitecore.sm.parser.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.AsciiParserMapping;

/**
 * 
 * @author avani.panchal
 *
 */
public interface AsciiParserService {
	
	public ResponseObject getAsciiParserMappingById(int asciiParserMappingId);
	
	public ResponseObject updateAsciiParserMapping(AsciiParserMapping asciiParser);

}
