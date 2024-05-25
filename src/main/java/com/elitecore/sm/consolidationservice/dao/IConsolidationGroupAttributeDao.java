package com.elitecore.sm.consolidationservice.dao;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;
import com.elitecore.sm.consolidationservice.model.DataConsolidationGroupAttribute;

public interface IConsolidationGroupAttributeDao extends GenericDAO<DataConsolidationGroupAttribute>{
	
	public DataConsolidation getDataConsolidationById(int id);

}
