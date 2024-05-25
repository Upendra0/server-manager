package com.elitecore.sm.parser.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.FixedLengthBinaryParserMapping;

/**
 * @author ELITECOREADS\kiran.kagithapu
 *
 */
public interface FixedLengthBinaryParserService{
	
	public ResponseObject getFixedLengthBinaryParserMappingById(int fixedLengthBinaryParserMappingId);
	
	public ResponseObject updateFixedLengthBinaryParser(FixedLengthBinaryParserMapping fixedLengthBinaryParser);


}
