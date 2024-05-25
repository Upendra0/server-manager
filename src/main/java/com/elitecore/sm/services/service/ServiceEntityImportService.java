package com.elitecore.sm.services.service;

import com.elitecore.sm.common.exception.SMException;
import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.services.model.IPLogParsingService;
import com.elitecore.sm.services.model.ParsingService;
import com.elitecore.sm.services.model.Service;


public interface ServiceEntityImportService {
	
	public ResponseObject importService(int serviceId, Service exportedService, int staffId, String jaxbXMLPath, int serverId, int importMode) 
			throws SMException;
	
	public void importParsingService(ParsingService dbService, ParsingService exportedService, int importMode);
	public void importIpLogParsingService(IPLogParsingService dbService, IPLogParsingService exportedService, int importMode);
}
