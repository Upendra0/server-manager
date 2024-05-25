package com.elitecore.sm.parser.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.VarLengthBinaryParserMapping;

/**
 * 
 * @author hardik.loriya
 *
 */
public interface VarLengthBinaryParserService {
	
	public ResponseObject getVarLengthBinaryParserMappingById(int varLengthBinaryParserMappingId);
	
	public ResponseObject updateVarLengthBinaryParserMapping(VarLengthBinaryParserMapping varLengthBinaryParserMapping);
	
}
