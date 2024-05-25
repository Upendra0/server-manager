package com.elitecore.sm.parser.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.DetailLocalParserMapping;

/**
 * 
 * @author saumil.vachheta
 * @date 07 Feb 2022
 *
 */
public interface DetailLocalParserService {
	
	public ResponseObject getDetailLocalParserMappingById(int detailLocalParserMappingId);
	
	public ResponseObject updateDetailLocalParserMapping(DetailLocalParserMapping detailLocalParser);

}
