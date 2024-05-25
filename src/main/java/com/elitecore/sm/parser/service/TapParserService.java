package com.elitecore.sm.parser.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.TapParserMapping;

public interface TapParserService {
	
public ResponseObject	getTapParserMappingById(int tapParserMappingId);
public ResponseObject updateTapParserMappings(TapParserMapping tapParserMapping);

}
