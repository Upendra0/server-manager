package com.elitecore.sm.parser.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.JsonParserMapping;

/**
 * 
 * @author sanket.bhalodia
 *
 */
public interface JsonParserService {
	
	public ResponseObject getJsonParserMappingById(int jsonParserMappingId);
	
	public ResponseObject updateJsonParserMapping(JsonParserMapping jsonParser);

}
