package com.elitecore.sm.parser.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.XlsParserMapping;

public interface XlsParserService {
	
	public ResponseObject getXlsParserMappingById(int htmlParserMappingId);
	
	public ResponseObject updateXlsParserMapping(XlsParserMapping xlsParser);

}
