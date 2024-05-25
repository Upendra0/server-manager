package com.elitecore.sm.rulelookup.dao;

import java.util.List;

import com.elitecore.sm.common.dao.GenericDAO;
import com.elitecore.sm.rulelookup.model.RuleLookupTableConfiguration;

public interface IRuleLookupTableConfigurationDao extends GenericDAO<RuleLookupTableConfiguration> {

	public List<RuleLookupTableConfiguration> getListOfImmediateExecutionByViewName(String viewName);
	
}
