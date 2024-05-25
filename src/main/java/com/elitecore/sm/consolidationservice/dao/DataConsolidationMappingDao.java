package com.elitecore.sm.consolidationservice.dao;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.consolidationservice.model.DataConsolidationMapping;

public interface DataConsolidationMappingDao extends GenericDAO<DataConsolidationMapping>{

	public long getDataConsolidationMappingCountByMappingNameAndDestPath(int mappingId, String mappingName, String destPath);

}
