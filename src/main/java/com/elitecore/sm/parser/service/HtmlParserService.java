package com.elitecore.sm.parser.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.AsciiParserMapping;
import com.elitecore.sm.parser.model.HtmlParserMapping;

public interface HtmlParserService {
	
	public ResponseObject getHtmlParserMappingById(int htmlParserMappingId);
	
	public ResponseObject updateHtmlParserMapping(HtmlParserMapping htmlParser);

}
