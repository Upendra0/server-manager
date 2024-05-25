package com.elitecore.sm.consolidationservice.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.consolidationservice.model.DataConsolidation;

public interface IConsolidationDefinitionDao extends GenericDAO<DataConsolidation>{
	
	public List<DataConsolidation> getConsolidationDefintionListByServiceId(int serviceId);
}
