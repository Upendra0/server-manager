package com.elitecore.sm.parser.dao;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.parser.model.VarLengthAsciiDataDefinitionFile;

/**
 * @author hardik.loriya
 *
 */
public interface VarLengthAsciiDataDefinitionFileDao extends GenericDAO<VarLengthAsciiDataDefinitionFile> {

	public VarLengthAsciiDataDefinitionFile getDataDefinitionByMappingId(int mappingId);
	
	public boolean isDataDefinitionFileExist(String fileName);
	
}
