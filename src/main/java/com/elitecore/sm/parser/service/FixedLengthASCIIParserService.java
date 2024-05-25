package com.elitecore.sm.parser.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.FixedLengthASCIIParserMapping;

/**
 * The Class FixedLengthASCIIParserService.
 * 
 * @author Sagar Ghetiya
 */
public interface FixedLengthASCIIParserService {
	
	public ResponseObject getFixedLengthASCIIParserMappingById(int fixedLengthASCIIParserMappingId);
	
	public ResponseObject updateFixedLengthASCIIParser(FixedLengthASCIIParserMapping asciiParser);

}

