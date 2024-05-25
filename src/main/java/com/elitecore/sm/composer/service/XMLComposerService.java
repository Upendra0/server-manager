package com.elitecore.sm.composer.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.composer.model.XMLComposerMapping;

/**
 * 
 * @author Mitul.Vora
 *
 */
public interface XMLComposerService {
	
	public ResponseObject getXMLComposerMappingById(int xmlComposerMappingId);
	
	public ResponseObject updateXMLComposerMapping(XMLComposerMapping xmlComposer);

}
