package com.elitecore.sm.parser.service;

import org.springframework.web.multipart.MultipartFile;

import com.elitecore.sm.common.util.ResponseObject;
import com.elitecore.sm.parser.model.VarLengthAsciiParserMapping;

/**
 * 
 * @author hardik.loriya
 *
 */
public interface VarLengthAsciiParserService {
	
	public ResponseObject getVarLengthAsciiParserMappingById(int varLengthAsciiParserMappingId);
	
	public ResponseObject updateVarLengthAsciiParserMapping(VarLengthAsciiParserMapping varLengthAsciiParserMapping);
	
	public ResponseObject uploadDataDefinitionFile(MultipartFile file, int mappingId, int staffId, int serverInstanceId);
	
	public String getDataDefinitionFileNameById(int mappingId);

}
