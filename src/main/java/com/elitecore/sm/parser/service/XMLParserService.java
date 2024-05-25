package com.elitecore.sm.parser.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.XMLParserMapping;

/**
 * 
 * @author jaidip.trivedi
 *
 */
public interface XMLParserService {
	
	public ResponseObject getXMLParserMappingById(int xmlParserMappingId);
	
	public ResponseObject updateXMLParserMapping(XMLParserMapping xmlParser);

}
