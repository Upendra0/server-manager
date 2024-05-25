package com.elitecore.sm.parser.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.NRTRDEParserMapping;

public interface NRTRDEParserService {

	public ResponseObject getNRTRDEParserMappingById(int nrtrdeParserMappingId);
	public ResponseObject updateNrtrdeParserMapping(NRTRDEParserMapping nrtrdeParser);
}
