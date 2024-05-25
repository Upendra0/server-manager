package com.elitecore.sm.parser.service;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.ASN1ParserMapping;

public interface Asn1ParserService {

	public ResponseObject getAsn1ParserMappingById(int asn1ParserMappingId);
	
	public ResponseObject updateAsn1ParserMapping(ASN1ParserMapping asn1Parser);
}
