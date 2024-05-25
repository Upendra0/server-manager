package com.elitecore.sm.parser.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.RapParserMapping;

public interface RapParserService {
	
	public ResponseObject getRapParserMappingById	(int rapParserMappingId);
	
	public ResponseObject updateRapParserMappings(RapParserMapping rapParserMapping);
	

}
