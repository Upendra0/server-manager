package com.elitecore.sm.parser.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.MTSiemensParserMapping;

/**
 * 
 * @author avani.panchal
 *
 */
public interface MTSiemensParserService {
	
	public ResponseObject getMTSiemensParserMappingById(int mtsiemensParserMappingId);
	
	public ResponseObject updateMTSiemensParserMapping(MTSiemensParserMapping mtsiemensParser);

}
